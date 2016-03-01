package net.coderodde.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * This class contains utility methods for writing and reading binary data in 
 * files.
 * 
 * @author Rodion "rodde" Efremov 
 * @version 1.6 (Feb 29, 2016)
 */
public class FileTools {

    /**
     * This method returns the byte array that represent the contents of 
     * {@code file}.
     * 
     * @param  file the file to read.
     * @return the array of bytes representing the contents of the input file.
     */
    public static byte[] readFile(File file) 
    throws IOException, FileNotFoundException {
        Objects.requireNonNull(file, "The input file is null.");
        long size = file.length();
        checkSize(size);

        byte[] data;
        int bytesRead;

        try (FileInputStream stream = new FileInputStream(file)) {
            data = new byte[(int) size];
            bytesRead = stream.read(data);
        }

        if (bytesRead != size) {
            throw new IllegalStateException(
                    "File size and read count mismatch. File size: " +
                    size + ", bytes read: " + bytesRead);
        }

        return data;
    }

    /**
     * Writes the byte array {@code data} to the file {@code file}. After 
     * successful operation of this method, the input file will contain exactly
     * the contents of the input data.
     * 
     * @param file the file to write to.
     * @param data the data array to write.
     * @throws java.io.IOException           if file IO fails.
     * @throws java.io.FileNotFoundException if file does not exist.
     */
    public static void writeFile(File file, byte[] data)
    throws IOException, FileNotFoundException {
        Objects.requireNonNull(file, "The input file is null.");
        Objects.requireNonNull(data, "The input data to write is null.");

        try (BufferedOutputStream stream = new BufferedOutputStream(
                                           new FileOutputStream(file))) {
            stream.write(data);
        }
    }

    // This method ensures that file size is small enough to be represented 
    // using a variable of type 'int'.
    private static final void checkSize(long size) {
        if (size > Integer.MAX_VALUE) {
            throw new IllegalStateException(
                    "The target file is too large: " + size + " bytes. " +
                    "Maximum allowed size is " + Integer.MAX_VALUE + 
                    "bytes.");
        }
    }
}
