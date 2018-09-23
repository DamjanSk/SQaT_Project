import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Note: Pit test doesnt work with JUnit 5
 *
 * To run the tests select maven projects on the right side of
 * IntelliJ then open and double click on
 * Plugins -> pitest -> pitest:mutationCoverage
 *
 * The result is located in
 * target -> reports -> {TimeStamp of execution} -> index.html
 */

public class DESPitTest {
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    @Test
    public void test() {
        DES des = new DES(key);

        int[] message = {1, 2, 3, 4, 5, 6, 7, 8 };

        int[] res = new int[64];

        for(int i=0; i<8; i++) {
            int tmp = message[i];
            for(int j=0; j<8; j++) {
                res[i*8 + 7 - j] = tmp % 2;
                tmp /= 2;
            }
        }

        Assert.assertArrayEquals(res, des.decrypt(des.encrypt(message)));
    }
}

