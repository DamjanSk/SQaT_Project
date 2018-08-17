import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


class DESInputSpaceTest {

    private DES des;
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };


    // Create a new object for each test method.
    @BeforeEach
    void each() {
        des = new DES(key);
    }


    // Testing the shift(int[] array, int amount) method.
    // Characteristics for array:
    // - Length: 0, 1, >1
    // - Null: null, non-null
    // Characteristics for amount:
    // - Relation to zero: <0, 0, >0
    // - Relation to size of array: <size, size, >size

    // Tests for all combination coverage except for nulls.
    private static Object[][] shiftTestValues() {
        return new Object[][]{
                // array,                              amount,    expected result
                {new int[]{},                              -1,    new int[]{}},
                {new int[]{},                               0,    new int[]{}},
                {new int[]{},                               1,    new int[]{}},
                {new int[]{},                              -5,    new int[]{}},
                {new int[]{},                               0,    new int[]{}},
                {new int[]{},                              50,    new int[]{}},

                {new int[]{5},                             -1,    new int[]{5}},
                {new int[]{3},                              0,    new int[]{3}},
                {new int[]{-1},                             1,    new int[]{-1}},
                {new int[]{9},                             -5,    new int[]{9}},
                {new int[]{-5},                             1,    new int[]{-5}},
                {new int[]{0},                             50,    new int[]{0}},

                {new int[]{1, 2, 3},                       -1,    new int[]{3, 1, 2}},
                {new int[]{2, 2, 2, 2},                     0,    new int[]{2, 2, 2, 2}},
                {new int[]{-5, 0, 5},                       1,    new int[]{0, 5, -5}},
                {new int[]{1, 2},                          -5,    new int[]{2, 1}},
                {new int[]{10, 20, 30, 40, 50, 60},         6,    new int[]{10, 20, 30, 40, 50, 60}},
                {new int[]{1, 1, 2, 3, 5, 8, 13},          50,    new int[]{1, 2, 3, 5, 8, 13, 1}}
        };
    }

    @ParameterizedTest
    @MethodSource("shiftTestValues")
    void shiftAccuracy(int[] array, int amount, int[] expected) {
        assertArrayEquals(expected, des.shift(array, amount));
    }

    @Test
    void shiftNull() {
        assertNull(des.shift(null, 2));
    }

    @Test
    void shiftSpeed() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            for(int i=0; i<1000; i++)
                des.shift(new int[]{1, 2, 3}, i);
        });
    }
}