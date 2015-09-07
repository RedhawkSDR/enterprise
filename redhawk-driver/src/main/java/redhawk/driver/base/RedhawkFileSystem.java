package redhawk.driver.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface RedhawkFileSystem {

	List<String> findFilesInDirectory(String directory, String pattern);
	List<String> findDirectoriesInDirectory(String directory, String pattern);
	List<String> findFiles(String pattern);
	List<String> findDirectories(String pattern);
	
	void removeFile(String filePath) throws IOException;
	void removeDirectory(String directoryPath) throws IOException; 
	byte[] getFile(String filePath) throws IOException;
	void writeFile(InputStream inputStream, String destFile) throws IOException;
}
