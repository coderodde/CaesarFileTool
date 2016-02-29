package net.coderodde.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

/**
 *
 * @author Rodion "rodde" Efremov 
 * @version 1.6 (Feb 29, 2016)
 */
public class FileTools {
    
    public static byte[] readFile(File file) {
        Objects.requireNonNull(file, "The input file is null.");
        checkFile(file);
        long size = file.length();
        checkSize(size);
        
        try {
            FileInputStream stream = new FileInputStream(file);
            byte[] data = new byte[(int) size];
            int bytesRead = stream.read(data);
            stream.close();
            
            if (bytesRead != size) {
                throw new IllegalStateException(
                        "File size and read count mismatch. File size: " +
                        size + ", bytes read: " + bytesRead);
            }
            
            return data;
        } catch (FileNotFoundException ex) {
            // This should not happen as we check in 'checkFile' that the file 
            // exists.           
            throw new IllegalStateException(
                    "File \"" + file.getAbsolutePath() +
                    "\" magically disappeared.");
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Could not process the file \"" + file.getAbsolutePath() + 
                    "\".");
        }
    }
    
    public static void writeFile(File file, byte[] data) {
        Objects.requireNonNull(file, "The input file is null.");
        Objects.requireNonNull(data, "The input data to write is null.");
        checkFile(file);
        
        try {
            BufferedOutputStream stream = new BufferedOutputStream(
                                              new FileOutputStream(file));
            stream.write(data);
            stream.close();
        } catch (FileNotFoundException ex) {
            // This should not happen either as we check in 'checkFile' that the
            // file exists.
            throw new IllegalStateException(
                    "File \"" + file.getAbsolutePath() + 
                    "\" magically disappeared.");
        } catch (IOException ex) {
            
        }
    }
    
    private static final void checkFile(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(
                    "The file \"" + file.getAbsolutePath() + 
                    "\" does not exist.");
        }
        
        if (!file.isFile()) {
            throw new IllegalArgumentException(
                    "The file \"" + file.getAbsolutePath() + 
                    "\" is not a regular file.");
        }
    }
    
    private static final void checkSize(long size) {
        if (size > Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "The target file is too large: " + size + " bytes. " +
                    "Maximum allowed size is " + Integer.MAX_VALUE + 
                    "bytes.");
        }
    }
}
