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
package redhawk.camel.components.filemanager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import org.apache.camel.util.FileUtil;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RedhawkFileUtil {

    public static final int BUFFER_SIZE = 128 * 1024;

    private static final transient Log LOG = LogFactory.getLog(RedhawkFileUtil.class);
    private static final int RETRY_SLEEP_MILLIS = 10;
    private static File defaultTempDir;

    private RedhawkFileUtil() {
    }

    /**
     * Normalizes the path to cater for Windows and other platforms
     */
    public static String normalizePath(String path) {
        if (path == null) {
            return null;
        }

        return path.replace('\\', '/');
    }
    
//    public static File createTempFile(String prefix, String suffix) throws IOException {
//        return createTempFile(prefix, suffix, null);
//    }
//
//    public static File createTempFile(String prefix, String suffix, File parentDir) throws IOException {
//        File parent = (parentDir == null) ? getDefaultTempDir() : parentDir;
//            
//        if (suffix == null) {
//            suffix = ".tmp";
//        }
//        if (prefix == null) {
//            prefix = "camel";
//        } else if (prefix.length() < 3) {
//            prefix = prefix + "camel";
//        }
//
//        // create parent folder
//        parent.s();
//
//        return File.createTempFile(prefix, suffix, parent);
//    }

    /**
     * Strip any leading separators
     */
    public static String stripLeadingSeparator(String name) {
        if (name == null) {
            return null;
        }
        while (name.startsWith("/") || name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        return name;
    }

    /**
     * Does the name start with a leading separator
     */
    public static boolean hasLeadingSeparator(String name) {
        if (name == null) {
            return false;
        }
        if (name.startsWith("/") || name.startsWith(File.separator)) {
            return true;
        }
        return false;
    }

    /**
     * Strip first leading separator
     */
    public static String stripFirstLeadingSeparator(String name) {
        if (name == null) {
            return null;
        }
        if (name.startsWith("/") || name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        return name;
    }

    /**
     * Strip any trailing separators
     */
    public static String stripTrailingSeparator(String name) {
        if (ObjectHelper.isEmpty(name)) {
            return name;
        }
        
        String s = name;
        
        // there must be some leading text, as we should only remove trailing separators 
        while (s.endsWith("/") || s.endsWith(File.separator)) {
            s = s.substring(0, s.length() - 1);
        }
        
        // if the string is empty, that means there was only trailing slashes, and no leading text
        // and so we should then return the original name as is
        if (ObjectHelper.isEmpty(s)) {
            return name;
        } else {
            // return without trailing slashes
            return s;
        }
    }

    /**
     * Strips any leading paths
     */
    public static String stripPath(String name) {
        if (name == null) {
            return null;
        }
        int posUnix = name.lastIndexOf('/');
        int posWin = name.lastIndexOf('\\');
        int pos = Math.max(posUnix, posWin);

        if (pos != -1) {
            return name.substring(pos + 1);
        }
        return name;
    }

    public static String stripExt(String name) {
        if (name == null) {
            return null;
        }
        int pos = name.lastIndexOf('.');
        if (pos != -1) {
            return name.substring(0, pos);
        }
        return name;
    }

    /**
     * Returns only the leading path (returns <tt>null</tt> if no path)
     */
    public static String onlyPath(String name) {
        if (name == null) {
            return null;
        }

        int posUnix = name.lastIndexOf('/');
        int posWin = name.lastIndexOf('\\');
        int pos = Math.max(posUnix, posWin);

        if (pos > 0) {
            return name.substring(0, pos);
        } else if (pos == 0) {
            // name is in the root path, so extract the path as the first char
            return name.substring(0, 1);
        }
        // no path in name
        return null;
    }

    /**
     * Compacts a path by stacking it and reducing <tt>..</tt>
     */
    public static String compactPath(String path) {
        if (path == null) {
            return null;
        }

        // only normalize path if it contains .. as we want to avoid: path/../sub/../sub2 as this can leads to trouble
        if (path.indexOf("..") == -1) {
            return path;
        }

        // only normalize if contains a path separator
        if (path.indexOf(File.separator) == -1) {
            return path;
        }

        Stack<String> stack = new Stack<String>();
        
        String separatorRegex = File.separator;
        if (FileUtil.isWindows()) {
            separatorRegex = "\\\\";
        }
        String[] parts = path.split(separatorRegex);
        for (String part : parts) {
            if (part.equals("..") && !stack.isEmpty()) {
                // only pop if there is a previous path
                stack.pop();
            } else {
                stack.push(part);
            }
        }

        // build path based on stack
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = stack.iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(File.separator);
            }
        }

        return sb.toString();
    }


   
    
}
