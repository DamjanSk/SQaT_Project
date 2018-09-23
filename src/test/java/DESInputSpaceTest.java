import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.IllegalArgumentException;
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
                {new int[]{-5, -43, -2},                   -4,    new IllegalArgumentException()},
                {new int[]{-3, -8, -500, -8},               0,    new IllegalArgumentException()},
                {new int[]{-22, -75},                       2,    new IllegalArgumentException()},
                {new int[]{-200, -1, -1, -1, -1},         855,    new IllegalArgumentException()},
                {new int[]{-22, -75},                       3,    new IllegalArgumentException()},

                {new int[]{8, 122, 6},                     -4,    new IllegalArgumentException()},
                {new int[]{33, 12, 2, 95},                  0,    0},
                {new int[]{122, 175},                       8,    1},
                {new int[]{4, 5, 44, 55, 45},             855,    new IllegalArgumentException()},
                {new int[]{220, 75},                       12,    1},

                {new int[]{4, 152, 9, 9, 9, 9},            40,    0},
                {new int[]{42, 21, 4, 98},                  7,    0},
                {new int[]{12, 52, 157},                   23,    1},
                {new int[]{4, 5, 6, 5, 4},                 21,    1},
                {new int[]{101, 112},                       2,    1},

                {new int[]{1200, 800, 600},                -4,    new IllegalArgumentException()},
                {new int[]{33333},                          0,    new IllegalArgumentException()},
                {new int[]{1202, 7000},                     8,    new IllegalArgumentException()},
                {new int[]{257, 257, 257},                855,    new IllegalArgumentException()},
                {new int[]{2200, 750},                     12,    new IllegalArgumentException()},
        };
    }

    private static Object[][] getBitNullValues() {
        return new Object[][]{
                {null,                                     -4, new NullPointerException()},
                {null,                                      0, new NullPointerException()},
                {null,                                      8, new NullPointerException()},
                {null,                                    855, new NullPointerException()},
                {null,                                     12, new NullPointerException()},
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


    // int[] permute(int[] array, int[] order, boolean bit)

    // Relevant characteristics for array:
    // - Null: null, non-null
    // - Size: 0, 1, >1
    // - Elements: 0-256, outside that range
    // Relevant characteristics for order array:
    // - Size: <array, =array, >array
    // - Elements: within array, out of bounds
    // Bit can be true or false.

    private static Object[][] permuteTestValues() {
        return new Object[][]{
                // array,                                 order,                               bit,     expected result
                {new int[]{},                             new int[]{},                          true,    new int[]{}},
                {new int[]{},                             new int[]{},                         false,    new int[]{}},
                {new int[]{},                             new int[]{1, 6, 2},                   true,    new IllegalArgumentException()},
                {new int[]{},                             new int[]{3, 15},                    false,    new IllegalArgumentException()},

                {new int[]{4},                            new int[]{},                          true,    new int[]{}},
                {new int[]{18},                           new int[]{},                         false,    new int[]{}},
                {new int[]{22},                           new int[]{1},                         true,    new int[]{22}},
                {new int[]{5},                            new int[]{6},                        false,    new int[]{1}},
                {new int[]{8},                            new int[]{22},                        true,    new IllegalArgumentException()},
                {new int[]{112},                          new int[]{-5},                       false,    new IllegalArgumentException()},
                {new int[]{1},                            new int[]{1, 1},                      true,    new int[]{1, 1}},
                {new int[]{3},                            new int[]{1, 2, 3, 4, 5, 6, 7, 8},   false,    new int[]{0, 0, 0, 0, 0, 0, 1, 1}},
                {new int[]{15},                           new int[]{222, 333},                  true,    new IllegalArgumentException()},
                {new int[]{23},                           new int[]{-6, -800},                  true,    new IllegalArgumentException()},

                {new int[]{812},                          new int[]{},                          true,    new IllegalArgumentException()},
                {new int[]{-19},                          new int[]{},                         false,    new IllegalArgumentException()},
                {new int[]{257},                          new int[]{1},                         true,    new IllegalArgumentException()},
                {new int[]{-1},                           new int[]{1},                        false,    new IllegalArgumentException()},
                {new int[]{32417},                        new int[]{200},                       true,    new IllegalArgumentException()},
                {new int[]{-700},                         new int[]{-200},                     false,    new IllegalArgumentException()},
                {new int[]{1000},                         new int[]{1, 1},                      true,    new IllegalArgumentException()},
                {new int[]{-3},                           new int[]{3, 2},                     false,    new IllegalArgumentException()},
                {new int[]{1010},                         new int[]{-20, -20, -20},             true,    new IllegalArgumentException()},

                {new int[]{1, 1, 0, 1, 0, 0},             new int[]{5, 4, 3, 2, 1},             true,    new int[]{0, 1, 0, 1, 1}},
                {new int[]{127, 50, 44, 213, 9},          new int[]{32, 10, 12},               false,    new int[]{1, 0, 1}},
                {new int[]{10, 5, 10, 15},                new int[]{1, 2, 4, 3},                true,    new int[]{10, 5, 15, 10}},
                {new int[]{2, 4, 6, 8, 10, 12},           new int[]{35, 48, 1, 2, 5, 25},      false,    new int[]{0, 0, 0, 0, 0, 0}},
                {new int[]{1, 0, 1, 0, 1, 0},             new int[]{1, 1, 2, 1, 1, 3, 1, 1},    true,    new int[]{1, 1, 0, 1, 1, 1, 1, 1}},
                {new int[]{255, 0, 255, 1},               new int[]{7, 3, 5, 15, 4, 16, 17},   false,    new int[]{1, 1, 1, 0, 1, 0, 1}},
                {new int[]{1, 1, 1, 0, 0, 0},             new int[]{-5, 1000, -1, -1},          true,    new IllegalArgumentException()},
                {new int[]{156, 132},                     new int[]{12000},                    false,    new IllegalArgumentException()},
                {new int[]{22, 11, 33, 22, 44},           new int[]{-5, -5, -5, -5, -5},        true,    new IllegalArgumentException()},
                {new int[]{136, 210, 41, 43},             new int[]{800, -2, 700, -1},         false,    new IllegalArgumentException()},
                {new int[]{0, 0, 1, 0, 1},                new int[]{-3, 742, -5, 1000, -1, -1}, true,    new IllegalArgumentException()},
                {new int[]{0, 1, 1, 0, 1, 0},             new int[]{-5, 1000, -1, -1},         false,    new IllegalArgumentException()},
        };
    }

    private static Object[][] permuteNullValues() {
        return new Object[][]{
                {null,                                     new int[]{},    true, new NullPointerException()},
                {new int[]{},                                     null,    true, new NullPointerException()},
                {null,                                            null,    true, new NullPointerException()},
                {null,                                     new int[]{},   false, new NullPointerException()},
                {new int[]{},                                     null,   false, new NullPointerException()},
                {null,                                            null,   false, new NullPointerException()}
        };
    }

    @ParameterizedTest
    @MethodSource("permuteTestValues")
    void permuteAccuracy(int[] array, int[] order, boolean bit, Object expected) {
        if(expected instanceof Exception) {
            // Handle exceptions.
            assertThrows(((Exception) expected).getClass(), () -> des.permute(array, order, bit));
        } else {
            // Handle valid values.
            assertArrayEquals((int[]) expected, des.permute(array, order, bit));
        }
    }

    @ParameterizedTest
    @MethodSource("permuteNullValues")
    void permuteNull(int[] array, int[] order, boolean bit, Exception expected) {
        assertThrows(expected.getClass(), () -> des.permute(array, order, bit));
    }

    @Test
    void permuteSpeed() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            for(int i=0; i<1000; i++)
                des.permute(new int[]{1, 2, 3}, new int[]{3, 2, 1}, true);
        });
    }


    // int[] encrypt(int[] message, boolean reverse, boolean bits)

    // Relevant characteristics for message:
    // - Null: null, non-null
    // - Size: 8, 64, other
    // - Elements: bits, bytes, other
    // Reverse and bits can be true or false.

    private static Object[][] encryptTestValues() {
        return new Object[][]{
                // array,                                   reverse,  bits,   expected result
                {new int[]{12, 100, 4, 123},                  false,  true,   new IllegalArgumentException()},
                {new int[]{0, 0, 255, 255, 0, 2, 0},          false, false,   new IllegalArgumentException()},
                {new int[]{34, 51},                            true,  true,   new IllegalArgumentException()},
                {new int[]{},                                  true, false,   new IllegalArgumentException()},
                {new int[]{1, 0, 0, 1, 0, 1, 1, 0},           false,  true,   new IllegalArgumentException()},
                {new int[]{0, 0, 1, 1, 0},                    false, false,   new IllegalArgumentException()},
                {new int[]{1,1,1,1,1,0,1,0,0,0,0,1,0,1},       true,  true,   new IllegalArgumentException()},
                {new int[]{0, 1, 0},                           true, false,   new IllegalArgumentException()},
                {new int[]{-10, 12000, 800, 257},             false,  true,   new IllegalArgumentException()},
                {new int[]{-1, -2, -1, -3},                   false, false,   new IllegalArgumentException()},
                {new int[]{10006, 932, 425},                   true,  true,   new IllegalArgumentException()},
                {new int[]{-500},                              true, false,   new IllegalArgumentException()},

                {new int[]{0,0,0,0,1,1,1,1, 1,0,1,1,0,1,0,0,
                        1,1,0,0,1,1,0,0, 0,1,0,0,1,0,0,1,
                        0,1,0,1,0,1,1,1, 0,0,1,0,0,1,0,0,
                        0,1,0,1,0,1,0,1, 1,1,1,1,1,0,1,1},    false,  true,   new int[]{1,1,0,0,0,1,0,1, 1,1,1,0,1,0,1,1,
                                                                                    0,1,0,1,1,0,0,1, 0,0,0,0,0,1,1,0,
                                                                                    1,0,1,0,1,0,0,1, 0,0,1,0,1,1,1,0,
                                                                                    0,0,1,1,0,0,1,0, 0,1,0,0,1,1,1,1}},
                {new int[]{1,1,0,1,1,0,0,0, 0,1,1,1,0,1,0,0,
                        0,0,0,1,1,1,1,0, 1,1,1,0,0,1,1,0,
                        1,1,0,0,1,0,0,0, 1,1,0,1,0,1,0,1,
                        0,1,0,1,0,1,1,1, 1,1,0,0,0,0,1,0},    false,  false,   new IllegalArgumentException()},
                {new int[]{1,1,0,0,0,1,0,1, 1,1,1,0,1,0,1,1,
                        0,1,0,1,1,0,0,1, 0,0,0,0,0,1,1,0,
                        1,0,1,0,1,0,0,1, 0,0,1,0,1,1,1,0,
                        0,0,1,1,0,0,1,0, 0,1,0,0,1,1,1,1},    true,   true,   new int[]{0,0,0,0,1,1,1,1, 1,0,1,1,0,1,0,0,
                                                                                    1,1,0,0,1,1,0,0, 0,1,0,0,1,0,0,1,
                                                                                    0,1,0,1,0,1,1,1, 0,0,1,0,0,1,0,0,
                                                                                    0,1,0,1,0,1,0,1, 1,1,1,1,1,0,1,1}},
                {new int[]{0,1,1,0,1,0,1,0, 1,0,0,1,1,1,0,1,
                        0,1,1,0,0,1,1,1, 1,0,0,0,0,0,1,1,
                        1,0,0,1,1,1,0,1, 1,0,0,1,1,1,0,1,
                        0,0,0,1,1,1,1,0, 1,1,0,1,0,0,1,0},    true,  false,   new IllegalArgumentException()},
                {new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},         false,  true, new IllegalArgumentException()},
                {new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},         false, false, new IllegalArgumentException()},
                {new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},          true,  true, new IllegalArgumentException()},
                {new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},          true, false, new IllegalArgumentException()},
                {new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-20, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},     false,  true, new IllegalArgumentException()},
                {new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-24, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},     false, false, new IllegalArgumentException()},
                {new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-23, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},      true,  true, new IllegalArgumentException()},
                {new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-25, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},      true, false, new IllegalArgumentException()},

                {new int[]{0, 0, 1, 0, 1, 1, 1, 0},                false,  true,   new IllegalArgumentException()},
                {new int[]{1, 1, 0, 0, 1, 1, 0, 0},                false, false,   new int[]{0,0,1,1,0,1,0,0, 0,0,1,1,0,1,0,0,
                                                                                            1,0,0,0,1,1,1,0, 1,1,0,0,1,1,0,0,
                                                                                            1,0,1,1,1,1,1,1, 0,0,1,1,0,1,0,1,
                                                                                            0,0,0,1,1,1,1,1, 0,0,1,1,0,1,1,1}},
                {new int[]{0, 1, 1, 0, 1, 0, 0, 1},                 true,  true,   new IllegalArgumentException()},
                {new int[]{1, 1, 0, 1, 0, 1, 1, 0},                 true, false,   new int[]{0,0,0,0,1,1,0,0, 1,1,1,1,0,1,0,1,
                                                                                            0,0,0,1,1,1,0,1, 0,0,1,0,1,1,1,0,
                                                                                            1,0,0,1,1,1,0,0, 0,1,1,0,0,0,0,0,
                                                                                            1,1,0,1,1,0,0,1, 0,0,0,1,1,1,0,1}},
                {new int[]{15, 132, 184, 8, 99, 80, 14, 3},        false,  true,   new IllegalArgumentException()},
                {new int[]{32, 126, 205, 201, 8, 8, 74, 6},        false, false,   new int[]{1,0,0,1,0,1,0,0, 0,1,1,0,1,0,0,1,
                                                                                            1,0,1,0,0,0,0,1, 0,1,1,0,1,0,0,1,
                                                                                            0,1,0,0,0,0,1,0, 1,1,1,1,1,0,1,0,
                                                                                            1,0,0,1,1,1,0,0, 1,0,0,1,1,1,1,1}},
                {new int[]{177, 234, 255, 0, 0, 2, 22, 222},        true,  true,   new IllegalArgumentException()},
                {new int[]{0, 0, 0, 212, 0, 0, 0, 0},               true, false,   new int[]{1,1,1,1,1,0,0,0, 1,1,0,0,1,0,1,0,
                                                                                            1,1,0,1,0,0,1,0, 0,0,1,1,0,0,0,0,
                                                                                            0,0,0,0,0,0,1,1, 0,1,0,0,1,0,1,0,
                                                                                            1,1,1,1,0,1,1,1, 1,0,1,0,1,1,0,0}},
                {new int[]{300, -1, -15, 800, -43, -2, -33, 639},  false,  true,   new IllegalArgumentException()},
                {new int[]{-643, 654, -4, 769, -12, 257, -1, -1},  false, false,   new IllegalArgumentException()},
                {new int[]{431, -2, -19, 342, 566, 653, -5, -3},    true,  true,   new IllegalArgumentException()},
                {new int[]{388, 920, -16, -6, 342, -33, -80, -2},   true, false,   new IllegalArgumentException()},
        };
    }

    private static Object[][] encryptNullValues() {
        return new Object[][]{
                {null,                                     true,    true,   new NullPointerException()},
                {null,                                     true,   false,   new NullPointerException()},
                {null,                                    false,    true,   new NullPointerException()},
                {null,                                    false,   false,   new NullPointerException()}
        };
    }

    @ParameterizedTest
    @MethodSource("encryptTestValues")
    void encryptAccuracy(int[] array, boolean reverse, boolean bits, Object expected) {
        if(expected instanceof Exception) {
            // Handle exceptions.
            assertThrows(((Exception) expected).getClass(), () -> des.encrypt(array, reverse, bits));
        } else {
            // Handle valid values.
            assertArrayEquals((int[]) expected, des.encrypt(array, reverse, bits));
        }
    }

    @ParameterizedTest
    @MethodSource("encryptNullValues")
    void encryptNull(int[] array, boolean reverse, boolean bits, Exception expected) {
        assertThrows(expected.getClass(), () -> des.encrypt(array, reverse, bits));
    }

    @Test
    void encryptSpeed() {
        assertTimeout(Duration.ofSeconds(5), () -> {
            for(int i=0; i<1000; i++)
                des.encrypt(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, false, false);
        });
    }
}