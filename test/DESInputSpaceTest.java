import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.InvalidParameterException;
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


    // int[] shift(int[] array, int amount)

    // Relevant characteristics for array:
    // - Length: 0, 1, >1
    // - Null: null, non-null
    // Relevant characteristics for amount:
    // - Relation to zero: <0, 0, >0
    // - Relation to size of array: <size, size, >size

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


    // int getBit(int[] array, int pos)

    // Relevant characteristics for array:
    // - Elements: <0, <=byte, >byte
    // - Null: null, non-null
    // Relevant characteristics for pos:
    // - Relation to zero: <0, 0, >0
    // - Relation to array: outside, inside

    private static Object[][] getBitTestValues() {
        return new Object[][]{
                // array,                                 pos,    expected result
                {new int[]{-5, -43, -2},                   -4,    new InvalidParameterException("Invalid position.")},
                {new int[]{-3, -8, -500, -8},               0,    new InvalidParameterException("Invalid element(s) in array.")},
                {new int[]{-22, -75},                       2,    new InvalidParameterException("Invalid element(s) in array.")},
                {new int[]{-200, -1, -1, -1, -1},         855,    new InvalidParameterException("Invalid position.")},
                {new int[]{-22, -75},                       3,    new InvalidParameterException("Invalid element(s) in array.")},

                {new int[]{8, 122, 6},                     -4,    new InvalidParameterException("Invalid position.")},
                {new int[]{33, 12, 2, 95},                  0,    0},
                {new int[]{122, 175},                       8,    1},
                {new int[]{4, 5, 44, 55, 45},             855,    new InvalidParameterException("Invalid position.")},
                {new int[]{220, 75},                       12,    1},

                {new int[]{4, 152, 9, 9, 9, 9},            40,    0},
                {new int[]{42, 21, 4, 98},                  7,    0},
                {new int[]{12, 52, 157},                   23,    1},
                {new int[]{4, 5, 6, 5, 4},                 21,    1},
                {new int[]{101, 112},                       2,    1},

                {new int[]{1200, 800, 600},                -4,    new InvalidParameterException("Invalid position.")},
                {new int[]{33333},                          0,    new InvalidParameterException("Invalid element(s) in array.")},
                {new int[]{1202, 7000},                     8,    new InvalidParameterException("Invalid element(s) in array.")},
                {new int[]{257, 257, 257},                855,    new InvalidParameterException("Invalid position.")},
                {new int[]{2200, 750},                     12,    new InvalidParameterException("Invalid element(s) in array.")},
        };
    }

    private static Object[][] getBitNullValues() {
        return new Object[][]{
                {null,                                     -4, new NullPointerException("Array cannot be null.")},
                {null,                                      0, new NullPointerException("Array cannot be null.")},
                {null,                                      8, new NullPointerException("Array cannot be null.")},
                {null,                                    855, new NullPointerException("Array cannot be null.")},
                {null,                                     12, new NullPointerException("Array cannot be null.")},
        };
    }

    @ParameterizedTest
    @MethodSource("getBitTestValues")
    void getBitAccuracy(int[] array, int pos, Object expected) {
        if(expected instanceof Exception) {
            // Handle exceptions.
            assertThrows(((Exception) expected).getClass(), () -> des.getBit(array, pos));
        } else {
            // Handle valid values.
            assertEquals((int) expected, des.getBit(array, pos));
        }
    }

    @ParameterizedTest
    @MethodSource("getBitNullValues")
    void getBitNull(int[] array, int pos, Exception expected) {
        assertThrows(expected.getClass(), () -> des.getBit(array, pos));
    }

    @Test
    void getBitSpeed() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            for(int i=0; i<1000; i++)
                des.getBit(new int[]{1, 2, 3}, 1);
        });
    }
}