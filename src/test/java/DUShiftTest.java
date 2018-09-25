import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.IllegalArgumentException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DUShiftTest {

    private DES des;
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };


    @Test
    public void paths() {
        des = new DES(key);

        // A test with these values detours all DU subpaths:
        // (1, 2), (5, 6, 7, 9), (6, 7, 9), (7, 8), (8, 7)
        int[] test = {1, 2, 3};
        int amount = -14;
        int[] expected = {2, 3, 1};

        assertArrayEquals(expected, des.shift(test, amount));
    }
}
