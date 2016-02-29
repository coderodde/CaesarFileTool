package net.coderodde.encryption;

import java.util.Arrays;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class CipherToolsTest {

    private static final int ITERATIONS = 100;
    private static final int MAXIMUM_LENGTH = 1000;

    @Test
    public void testEncryptionDecryption() {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        System.out.println("Seed = " + seed);

        for (int iteration = 0; iteration < ITERATIONS; iteration++) {
            int cipherKey = random.nextInt();

            if (cipherKey == 0) {
                cipherKey = 1;
            }

            byte[] before = new byte[random.nextInt(MAXIMUM_LENGTH + 1)];
            random.nextBytes(before);

            byte[] encrypted = CipherTools.encrypt(before, cipherKey);
            byte[] after = CipherTools.decrypt(encrypted,  cipherKey);

            assertTrue(Arrays.equals(before, after));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncryptionThrowsOnZeroCipher() {
        CipherTools.encrypt(new byte[2], 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecryptionThrowsOnZeroCipher() {
        CipherTools.decrypt(new byte[2], 0);
    }
}
