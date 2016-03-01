package net.coderodde.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileToolsTest {

    private static final int ITERATIONS = 100;
    private static final int MAXIMUM_DATA_LENGTH = 20_000;

    @Test
    public void test() {
        long seed = System.nanoTime();
        Random random = new Random(seed);
        System.out.println("Seed = " + seed);

        for (int i = 0; i < ITERATIONS; ++i) {

            File file = new File("funky.txt");
            byte[] data = new byte[random.nextInt(MAXIMUM_DATA_LENGTH)];
            random.nextBytes(data);

            try {
                FileTools.writeFile(file, data);
                byte[] result = FileTools.readFile(file);
                assertTrue(Arrays.equals(result, data));
            } catch (FileNotFoundException ex) {
                fail(ex.getMessage());
            } catch (IOException ex) {
                fail(ex.getMessage());
            }

            file.delete();
        }
    }
}
