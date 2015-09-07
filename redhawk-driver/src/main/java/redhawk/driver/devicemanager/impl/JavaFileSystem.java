package redhawk.driver.devicemanager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CF.DataType;
import CF.ErrorNumberType;
import CF.FileException;
import CF.FileHelper;
import CF.FileSystemOperations;
import CF.InvalidFileName;
import CF.PropertiesHolder;
import CF.FileSystemPackage.FileInformationType;
import CF.FileSystemPackage.FileType;
import CF.FileSystemPackage.UnknownFileSystemProperties;

/**
 * 
 *
 */
public class JavaFileSystem implements FileSystemOperations {

	public static enum ScaFileInformationDataType {
		CREATED_TIME, MODIFIED_TIME, LAST_ACCESS_TIME, IOR_AVAILABLE, READ_ONLY
	}

	protected final POA poa; // SUPPRESS CHECKSTYLE Final protected field
	protected final ORB orb; // SUPPRESS CHECKSTYLE Final protected field

	private static final int MILLIS_PER_SEC = 1000;
	private final File root;

	public JavaFileSystem(final ORB orb, final POA poa, final File root) {
		this.orb = orb;
		this.poa = poa;
		this.root = root;
	}

	public void copy(final String sourceFileName, final String destinationFileName) throws InvalidFileName, FileException {
		if ("".equals(sourceFileName) || "".equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		if (sourceFileName.equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EINVAL, "Source file must be different from destination file.");
		}
		final File sourceFile = new File(this.root, sourceFileName);
		try {
			copyFile(sourceFile, new File(this.root, destinationFileName));
		} catch (final IOException e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		}
	}

	private void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists()){
			destFile.createNewFile();
		}
		
		FileChannel source = null;
		FileChannel destination = null;
				
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileInputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if(source != null){
				source.close();
			}
			
			if(destination != null){
				destination.close();
			}
		}
	}

	public void move(final String sourceFileName, final String destinationFileName) throws InvalidFileName, FileException {
		if ("".equals(sourceFileName) || "".equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		if (sourceFileName.equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EINVAL, "Source file must be different from destination file.");
		}
		final File sourceFile = new File(this.root, sourceFileName);
		if (!sourceFile.renameTo(new File(this.root, destinationFileName))) {
			throw new FileException(ErrorNumberType.CF_EIO, "Failed to rename file: " + sourceFileName + " to " + destinationFileName);
		}
	}

	public CF.File create(final String fileName) throws InvalidFileName, FileException {
		if ("".equals(fileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		final File file = new File(this.root, fileName);
		if (file.exists()) {
			throw new FileException(ErrorNumberType.CF_EEXIST, "File already exists of the name: " + fileName);
		}

		try {
			final JavaFileFileImpl impl = new JavaFileFileImpl(file, false);
			final byte[] id = this.poa.activate_object(impl);
			return FileHelper.narrow(this.poa.id_to_reference(id));
		} catch (final FileNotFoundException e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ServantAlreadyActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final WrongPolicy e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ObjectNotActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		}
	}

	public boolean exists(final String fileName) throws InvalidFileName {
		return new File(this.root, fileName).exists();
	}

	public FileInformationType[] list(final String fullPattern) throws FileException, InvalidFileName {
		final int index = fullPattern.lastIndexOf('/');
		final File container;
		if (index > 0) {
			container = new File(this.root, fullPattern.substring(0, index));
		} else {
			container = this.root;
		}

		final String pattern = fullPattern.substring(index + 1, fullPattern.length());

		final String[] fileNames;

		if (pattern.length() == 0) {
			fileNames = new String[] {
				""
			};
		} else {
			fileNames = container.list(new FilenameFilter() {

				@Override
				public boolean accept(final File dir, final String name) {
					if (!dir.equals(container)) {
						return false;
					}
					return wildcardMatch(name, pattern, IOCase.SENSITIVE);
				}
			});
		}
		
		if (fileNames == null) {
			return new FileInformationType[0];
		}

		final FileInformationType[] retVal = new FileInformationType[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			final FileInformationType fileInfo = new FileInformationType();
			final File file = new File(container, fileNames[i]);
			fileInfo.name = file.getName();
			fileInfo.size = file.length();
			if (file.isFile()) {
				fileInfo.kind = FileType.PLAIN;
			} else {
				fileInfo.kind = FileType.DIRECTORY;
			}
			final Any any = this.orb.create_any();
			any.insert_ulonglong(file.lastModified() / JavaFileSystem.MILLIS_PER_SEC);
			final DataType modifiedTime = new DataType("MODIFIED_TIME", any);

			fileInfo.fileProperties = new DataType[] {
				modifiedTime
			};

			retVal[i] = fileInfo;
		}
		return retVal;
	}

	public void mkdir(final String directoryName) throws InvalidFileName, FileException {
		if ("".equals(directoryName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		new File(this.root, directoryName).mkdir();
	}

	public CF.File open(final String fileName, final boolean readOnly) throws InvalidFileName, FileException {
		final File file = new File(this.root, fileName);
		if (!file.exists()) {
			throw new FileException(ErrorNumberType.CF_ENOENT, "No such file or directory\n");
		}
		if (file.isDirectory()) {
			throw new FileException(ErrorNumberType.CF_ENOENT, "Can not open a directory\n");
		}
		try {
			final JavaFileFileImpl impl = new JavaFileFileImpl(file, readOnly);
			
			final byte[] id = this.poa.activate_object(impl);
			return FileHelper.narrow(this.poa.id_to_reference(id));
		} catch (final FileNotFoundException e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ServantAlreadyActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final WrongPolicy e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ObjectNotActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		}
	}

	public void query(final PropertiesHolder fileSystemProperties) throws UnknownFileSystemProperties {
		final List<DataType> unknownProperties = new ArrayList<DataType>();
		for (final DataType dataType : fileSystemProperties.value) {
			if (dataType.id.equals("SIZE")) {
				final Any any = this.orb.create_any();
				// TODO For now we don't support SIZE
				any.insert_ulonglong(0);
				//				any.insert_ulonglong(this.root..getTotalSpace());
				dataType.value = any;
			} else if (dataType.id.equals("AVAILABLE SPACE")) {
				final Any any = this.orb.create_any();
				long freeKb;
				freeKb = new File(this.root.getAbsolutePath()).getFreeSpace() / 1024;
				any.insert_ulonglong(freeKb * 1024 / 8); // SUPPRESS CHECKSTYLE MagicNumber
				dataType.value = any;
			} else {
				unknownProperties.add(dataType);
			}
		}
		if (unknownProperties.size() > 0) {
			throw new UnknownFileSystemProperties(unknownProperties.toArray(new DataType[unknownProperties.size()]));
		}

	}

	public void remove(final String fileName) throws FileException, InvalidFileName {
		if ("".equals(fileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		final boolean result = new File(this.root, fileName).delete();
		if (!result) {
			throw new FileException(ErrorNumberType.CF_EIO, "Failed to delete file: " + fileName);
		}

	}

	public void rmdir(final String directoryName) throws InvalidFileName, FileException {
		if ("".equals(directoryName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		final File file = new File(this.root, directoryName);
		if (!file.isDirectory()) {
			throw new FileException(ErrorNumberType.CF_ENOTDIR, "Not a directory\n");
		}
		if (file.list().length != 0) {
			throw new FileException(ErrorNumberType.CF_ENOTEMPTY, "Directory not empty\n");
		}
		final boolean result = file.delete();
		if (!result) {
			throw new FileException(ErrorNumberType.CF_EIO, "Failed to delete directory: " + directoryName);
		}

	}
	
	public void dispose() {
		
	}

	
    private boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        filename = caseSensitivity.convertCase(filename);
        wildcardMatcher = caseSensitivity.convertCase(wildcardMatcher);
        String[] wcs = splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        Stack backtrack = new Stack();
        
        // loop around a backtrack stack, to handle complex * matching
        do {
            if (backtrack.size() > 0) {
                int[] array = (int[]) backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            
            // loop whilst tokens and text left to process
            while (wcsIdx < wcs.length) {
      
                if (wcs[wcsIdx].equals("?")) {
                    // ? so move to next text char
                    textIdx++;
                    anyChars = false;
                    
                } else if (wcs[wcsIdx].equals("*")) {
                    // set any chars status
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }
                    
                } else {
                    // matching text token
                    if (anyChars) {
                        // any chars then try to locate text token
                        textIdx = filename.indexOf(wcs[wcsIdx], textIdx);
                        if (textIdx == -1) {
                            // token not found
                            break;
                        }
                        int repeat = filename.indexOf(wcs[wcsIdx], textIdx + 1);
                        if (repeat >= 0) {
                            backtrack.push(new int[] {wcsIdx, repeat});
                        }
                    } else {
                        // matching from current position
                        if (!filename.startsWith(wcs[wcsIdx], textIdx)) {
                            // couldnt match token
                            break;
                        }
                    }
      
                    // matched text token, move text index to end of matched token
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }
      
                wcsIdx++;
            }
            
            // full match
            if (wcsIdx == wcs.length && textIdx == filename.length()) {
                return true;
            }
            
        } while (backtrack.size() > 0);
  
        return false;
    }
	
    /**
     * Splits a string into a number of tokens.
     * 
     * @param text  the text to split
     * @return the tokens, never null
     */
    private String[] splitOnTokens(String text) {
        // used by wildcardMatch
        // package level so a unit test may run on this
        
        if (text.indexOf("?") == -1 && text.indexOf("*") == -1) {
            return new String[] { text };
        }

        char[] array = text.toCharArray();
        ArrayList list = new ArrayList();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '?' || array[i] == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (array[i] == '?') {
                    list.add("?");
                } else if (list.size() == 0 ||
                        (i > 0 && list.get(list.size() - 1).equals("*") == false)) {
                    list.add("*");
                }
            } else {
                buffer.append(array[i]);
            }
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }

        return (String[]) list.toArray( new String[ list.size() ] );
    }
    
    
}
