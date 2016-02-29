package net.coderodde.encryption;

import java.util.Arrays;
import java.util.Random;

/**
 * This class provides static methods for encrypting and decrypting binary data
 * represented by arrays of bytes.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Feb 29, 2016)
 */
public class CipherTools {

    private static final int BYTES_PER_INT = 4;

    /**
     * Encrypts the input data {@code input} using the cipher key 
     * {@code cipherKey}.
     * 
     * @param  input     the data to encrypt.
     * @param  cipherKey the cipher key.
     * @return           the encrypted data.
     */
    public static byte[] encrypt(byte[] input, int cipherKey) {
        checkCipherNotZero(cipherKey);
        byte[] output = input.clone();

        for (int i = 0; i <= output.length - BYTES_PER_INT; ++i) {
            writeInt(output, i, readInt(output, i) + cipherKey);
        }

        return output;
    }

    /**
     * Decrypts the input data {@code input} using the cipher key 
     * {@code cipherKey}. 
     * 
     * @param  input     the input data to decrypt.
     * @param  cipherKey the cipher key.
     * @return           the decrypted data.
     */
    public static byte[] decrypt(byte[] input, int cipherKey) {
        checkCipherNotZero(cipherKey);
        byte[] output = input.clone();

        for (int i = output.length - BYTES_PER_INT; i >= 0; --i) {
            writeInt(output, i, readInt(output, i) - cipherKey);
        }

        return output;
    }

    private static void checkCipherNotZero(int cipherKey) {
        if (cipherKey == 0) {
            throw new IllegalArgumentException(
                    "The input cipher key is zero. For this reason, the data " +
                    "would not be encrypted.");
        }
    }

    /**
     * Returns the integer represented by bytes {@code data[offset], 
     * data[offset + 1], data[offset + 2], data[offset + 3]}, where the bytes
     * are listed from least significant to most significant.
     * 
     * @param data   the data array holding the bytes.
     * @param offset the offset of the integer to read.
     * @return       a four byte integer value.
     */
    private static int readInt(byte[] data, int offset) {
        int b0 = Byte.toUnsignedInt(data[offset]);
        int b1 = Byte.toUnsignedInt(data[offset + 1]);
        int b2 = Byte.toUnsignedInt(data[offset + 2]);
        int b3 = Byte.toUnsignedInt(data[offset + 3]);

        return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
    }

    /**
     * Writes the value {@code value} to the byte array {@code data} starting
     * from index {@code offset}, or namely, to the bytes {@code data[offset],
     * data[offset + 1], data[offset + 2], data[offset + 3]}, where the least
     * significant byte of the value is stored in the byte {@code data[offset]},
     * i.e., we assume a <b>little-endian</b> machine.
     * 
     * @param data   the array holding the data to write to.
     * @param offset the index of the least significant byte of the target
     *               data integer.
     * @param value  the value to write.
     */
    private static void writeInt(byte[] data, int offset, int value) {
        data[offset] = (byte)(value & 0xff);
        data[offset + 1] = (byte)((value >>> 8) & 0xff);
        data[offset + 2] = (byte)((value >>> 16) & 0xff);
        data[offset + 3] = (byte)((value >>> 24) & 0xff);
    }

    public static void main(final String... args) {
        Random random = new Random();
        byte[] before = new byte[10];
        random.nextBytes(before);

        int cipherKey = random.nextInt();
        byte[] encrypted = encrypt(before, cipherKey);
        byte[] after = decrypt(encrypted, cipherKey);

        System.out.println("Before:    " + Arrays.toString(before));
        System.out.println("Encrypted: " + Arrays.toString(encrypted));
        System.out.println("After:     " + Arrays.toString(after));
        System.out.println("Match: " + Arrays.equals(before, after));
    }
}
