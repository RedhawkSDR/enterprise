/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redbus.core.orchestration.components.redhawk.filemanager;

import java.io.ByteArrayOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileBinding;

import CF.File;
import CF.FileException;
import CF.InvalidFileName;
import CF.OctetSequenceHolder;
import CF.FilePackage.IOException;

public class RedhawkFileBinding  implements GenericFileBinding<RedhawkFileContainer> {
    private File body;
    private byte[] content;

    public Object getBody(GenericFile<RedhawkFileContainer> file) {
        // if file content has been loaded then return it
        if (content != null) {
            return content;
        }
        
        // as we use java.io.File itself as the body (not loading its content into a OutputStream etc.)
        // we just store a java.io.File handle to the actual file denoted by the
        // file.getAbsoluteFilePath. We must do this as the original file consumed can be renamed before
        // being processed (preMove) and thus it points to an invalid file location.
        // GenericFile#getAbsoluteFilePath() is always up-to-date and thus we use it to create a file
        // handle that is correct
//        if (body == null || !file.getAbsoluteFilePath().equals(body.fileName())) {
//            body = new File(file.getAbsoluteFilePath());
//        }
        return body;
    }

    public void setBody(GenericFile<RedhawkFileContainer> file, Object body) {
        // noop
    }

    public void loadContent(Exchange exchange, GenericFile<?> file) throws java.io.IOException {
        if (content == null) {
            File f = null;
            ByteArrayOutputStream bos = null;
            try {
                 f = ((RedhawkFileContainer) file.getFile()).getFile();
                OctetSequenceHolder holder = new OctetSequenceHolder();
                
                int fileSize;
                try {
                    fileSize = f.sizeOf();
                } catch (FileException e) {
                    throw new java.io.IOException("IOException on LoadContent");
                }
                
                int readSize = 1024*2000;
                
                bos = new ByteArrayOutputStream();
                
                for(int i=0; i < (fileSize/readSize)+1; i++){
                    f.read(holder, readSize);
                    bos.write(holder.value);
                }                
                
                content = bos.toByteArray();
            } catch (IOException e) {
                throw new java.io.IOException("IOException on LoadContent");
            } catch (InvalidFileName e1) {
                e1.printStackTrace();
            } catch (FileException e1) {
                e1.printStackTrace();
            } finally{
                try {
                    
                    if(f!= null){
                        f.close();
                    }
                    
                    if(bos != null){
                        bos.close();
                    }
                } catch (FileException e) {
                    throw new java.io.IOException("FileException on LoadContent: " + e.getMessage());
                }
            }
        }
    }
}