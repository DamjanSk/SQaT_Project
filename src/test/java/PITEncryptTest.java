import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

class EncryptData {
    public int[] val;
    public boolean reverse;
    public boolean bits;
    public int[] expected;
    
    public EncryptData(int[] val, boolean reverse, boolean bits, int[] expected) {
        this.val = val;
        this.reverse = reverse;
        this.bits = bits;
        this.expected = expected;
    }
}

@RunWith(Parameterized.class)
public class PITEncryptTest {

    @Parameterized.Parameters
    public static Collection<EncryptData> data() {
        return Arrays.asList(
                new EncryptData(new int[]{12, 100, 4, 123},                  false,  true,   null),
                new EncryptData(new int[]{0, 0, 255, 255, 0, 2, 0},          false, false,   null),
                new EncryptData(new int[]{34, 51},                            true,  true,   null),
                new EncryptData(new int[]{},                                  true, false,   null),
                new EncryptData(new int[]{1, 0, 0, 1, 0, 1, 1, 0},           false,  true,   null),
                new EncryptData(new int[]{0, 0, 1, 1, 0},                    false, false,   null),
                new EncryptData(new int[]{1,1,1,1,1,0,1,0,0,0,0,1,0,1},       true,  true,   null),
                new EncryptData(new int[]{0, 1, 0},                           true, false,   null),
                new EncryptData(new int[]{-10, 12000, 800, 257},             false,  true,   null),
                new EncryptData(new int[]{-1, -2, -1, -3},                   false, false,   null),
                new EncryptData(new int[]{10006, 932, 425},                   true,  true,   null),
                new EncryptData(new int[]{-500},                              true, false,   null),

                new EncryptData(new int[]{0,0,0,0,1,1,1,1, 1,0,1,1,0,1,0,0,
                        1,1,0,0,1,1,0,0, 0,1,0,0,1,0,0,1,
                        0,1,0,1,0,1,1,1, 0,0,1,0,0,1,0,0,
                        0,1,0,1,0,1,0,1, 1,1,1,1,1,0,1,1},    false,  true,   new int[]{1,1,0,0,0,1,0,1, 1,1,1,0,1,0,1,1,
                        0,1,0,1,1,0,0,1, 0,0,0,0,0,1,1,0,
                        1,0,1,0,1,0,0,1, 0,0,1,0,1,1,1,0,
                        0,0,1,1,0,0,1,0, 0,1,0,0,1,1,1,1}),
                new EncryptData(new int[]{1,1,0,1,1,0,0,0, 0,1,1,1,0,1,0,0,
                        0,0,0,1,1,1,1,0, 1,1,1,0,0,1,1,0,
                        1,1,0,0,1,0,0,0, 1,1,0,1,0,1,0,1,
                        0,1,0,1,0,1,1,1, 1,1,0,0,0,0,1,0},    false,  false,   null),
                new EncryptData(new int[]{1,1,0,0,0,1,0,1, 1,1,1,0,1,0,1,1,
                        0,1,0,1,1,0,0,1, 0,0,0,0,0,1,1,0,
                        1,0,1,0,1,0,0,1, 0,0,1,0,1,1,1,0,
                        0,0,1,1,0,0,1,0, 0,1,0,0,1,1,1,1},    true,   true,   new int[]{0,0,0,0,1,1,1,1, 1,0,1,1,0,1,0,0,
                        1,1,0,0,1,1,0,0, 0,1,0,0,1,0,0,1,
                        0,1,0,1,0,1,1,1, 0,0,1,0,0,1,0,0,
                        0,1,0,1,0,1,0,1, 1,1,1,1,1,0,1,1}),
                new EncryptData(new int[]{0,1,1,0,1,0,1,0, 1,0,0,1,1,1,0,1,
                        0,1,1,0,0,1,1,1, 1,0,0,0,0,0,1,1,
                        1,0,0,1,1,1,0,1, 1,0,0,1,1,1,0,1,
                        0,0,0,1,1,1,1,0, 1,1,0,1,0,0,1,0},    true,  false,   null),
                new EncryptData(new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},         false,  true, null),
                new EncryptData(new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},         false, false, null),
                new EncryptData(new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},          true,  true, null),
                new EncryptData(new int[]{12,3,100,8,17,56,203,4,  15,55,123,165,144,202,84,9,
                        15,16,102,107,98,214,200,3, 133,111,253,182,90,66,15,1,
                        13,18,99,109,201,181,33,37, 176,177,190,105,213,62,6,7,
                        155,46,88,77,47,126,101,22, 4,112,134,76,77,52,100,2},          true, false, null),
                new EncryptData(new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-20, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},     false,  true, null),
                new EncryptData(new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-24, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},     false, false, null),
                new EncryptData(new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-23, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},      true,  true, null),
                new EncryptData(new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
                        -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
                        555,-4,912,-5,643,555,-1,-25, 920,-5,-7,-4,-3,-5,-8,7043,
                        -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270},      true, false, null),

                new EncryptData(new int[]{0, 0, 1, 0, 1, 1, 1, 0},                false,  true,   null),
                new EncryptData(new int[]{1, 1, 0, 0, 1, 1, 0, 0},                false, false,   new int[]{0,0,1,1,0,1,0,0, 0,0,1,1,0,1,0,0,
                        1,0,0,0,1,1,1,0, 1,1,0,0,1,1,0,0,
                        1,0,1,1,1,1,1,1, 0,0,1,1,0,1,0,1,
                        0,0,0,1,1,1,1,1, 0,0,1,1,0,1,1,1}),
                new EncryptData(new int[]{0, 1, 1, 0, 1, 0, 0, 1},                 true,  true,   null),
                new EncryptData(new int[]{1, 1, 0, 1, 0, 1, 1, 0},                 true, false,   new int[]{0,0,0,0,1,1,0,0, 1,1,1,1,0,1,0,1,
                        0,0,0,1,1,1,0,1, 0,0,1,0,1,1,1,0,
                        1,0,0,1,1,1,0,0, 0,1,1,0,0,0,0,0,
                        1,1,0,1,1,0,0,1, 0,0,0,1,1,1,0,1}),
                new EncryptData(new int[]{15, 132, 184, 8, 99, 80, 14, 3},        false,  true,   null),
                new EncryptData(new int[]{32, 126, 205, 201, 8, 8, 74, 6},        false, false,   new int[]{1,0,0,1,0,1,0,0, 0,1,1,0,1,0,0,1,
                        1,0,1,0,0,0,0,1, 0,1,1,0,1,0,0,1,
                        0,1,0,0,0,0,1,0, 1,1,1,1,1,0,1,0,
                        1,0,0,1,1,1,0,0, 1,0,0,1,1,1,1,1}),
                new EncryptData(new int[]{177, 234, 255, 0, 0, 2, 22, 222},        true,  true,   null),
                new EncryptData(new int[]{0, 0, 0, 212, 0, 0, 0, 0},               true, false,   new int[]{1,1,1,1,1,0,0,0, 1,1,0,0,1,0,1,0,
                        1,1,0,1,0,0,1,0, 0,0,1,1,0,0,0,0,
                        0,0,0,0,0,0,1,1, 0,1,0,0,1,0,1,0,
                        1,1,1,1,0,1,1,1, 1,0,1,0,1,1,0,0}),
                new EncryptData(new int[]{300, -1, -15, 800, -43, -2, -33, 639},  false,  true,   null),
                new EncryptData(new int[]{-643, 654, -4, 769, -12, 257, -1, -1},  false, false,   null),
                new EncryptData(new int[]{431, -2, -19, 342, 566, 653, -5, -3},    true,  true,   null),
                new EncryptData(new int[]{388, 920, -16, -6, 342, -33, -80, -2},   true, false,   null),

                new EncryptData(new int[]{0, 0, 0, 255, 0, 0, 0, 0},               true, false,   new int[]{1,0,1,1,1,1,0,0, 0,0,1,0,1,1,1,1,
                        1,0,1,0,0,0,0,1, 0,0,1,0,1,1,1,1,
                        1,0,1,1,0,0,1,1, 1,1,0,1,1,1,0,1,
                        0,0,0,0,1,0,0,1, 0,1,0,0,1,1,1,1}),
                new EncryptData(new int[]{1, 2, 3, 4, 5}, true, true, null),

                new EncryptData(null,    true,  true,   null),
                new EncryptData(null,    true,  false,   null),
                new EncryptData(null,    false,  true,   null),
                new EncryptData(null,    false,  false,   null)
        );
    }

    public int[] val;
    public boolean reverse;
    public boolean bits;
    public int[] expected;
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    public PITEncryptTest(EncryptData data) {
        this.val = data.val;
        this.reverse = data.reverse;
        this.bits = data.bits;
        this.expected = data.expected;
    }

    @Test
    public void encryptTest() {
        DES des = new DES(key);
        if(val == null) {
            try {
                des.encrypt(null, reverse, bits);
                Assert.fail();
            }
            catch (NullPointerException ex) {
                Assert.assertTrue(true);
            }
        }
        else if(expected != null) {
            System.out.println(Arrays.toString(des.encrypt(val, reverse, bits)));
            Assert.assertArrayEquals(des.encrypt(val, reverse, bits), expected);
        }
        else {
            try{
                des.encrypt(val, reverse, bits);
                Assert.fail();
            }
            catch (IllegalArgumentException ex) {
                Assert.assertTrue(true);
            }
        }
    }

//    @Test
//    public void PITEncryptDecryptTest() {
//        DES test = new DES(key);
//
//        test.encrypt(new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
//                -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
//                555,-4,912,-5,643,555,-1,-25, 920,-5,-7,-4,-3,-5,-8,7043,
//                -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270});
//        test.decrypt(new int[]{800,-2,-5,-8,-1,742,257,4, 1500,-55,-13,333,524,-1,-5,-9,
//                -12,624,-77,507,-3,-2,280,-3, -5,-3,-8,682,790,-9,-3,-1,
//                555,-4,912,-5,643,555,-1,-25, 920,-5,-7,-4,-3,-5,-8,7043,
//                -63,439,-8,950,-5,-4,356,291, -7,723,-4,-1,960,-5,-8,270});
//    }
}
