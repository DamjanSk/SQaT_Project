import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

//class BitData {
//    public int[] val;
//    public int pos;
//    public int expected;
//
//    public BitData(int[] val, int pos, int expected) {
//        this.val = val;
//        this.pos = pos;
//        this.expected = expected;
//    }
//}
//@RunWith(Parameterized.class)
public class PITGetBitTest {

//    @Parameterized.Parameters
//    public static Collection<BitData> data() {
//        return Arrays.asList(
//                new BitData(new int[]{1, 2, 3, 4, 5}, 3, 4),
//                new BitData()
//        );
//    }
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    @Test
    public void testOk() {
        DES des = new DES(key);
        int[] arr = {1, 2, 3, 4, 5};
        Assert.assertEquals(1, des.getBit(arr, 7));
        Assert.assertEquals(0, des.getBit(arr, 0));
        Assert.assertEquals(1, des.getBit(arr, 5*8 -1));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNull() throws NullPointerException {
        DES des = new DES(key);

        expectedException.expect(NullPointerException.class);

        des.getBit(null, 1);
    }

    @Test
    public void testBadPosition() throws IllegalArgumentException{
        int[] arr = {1, 2, 3, 4, 5};
        DES des = new DES(key);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid position.");

        des.getBit(arr, 5*8);
        Assert.fail();
    }

    @Test
    public void testNegativePosition() throws IllegalArgumentException{
        int[] arr = {1, 2, 3, 4, 5};
        DES des = new DES(key);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid position.");

        des.getBit(arr, -1);
    }

    @Test
    public void testBadArray() throws IllegalArgumentException {
        int[] arr = {1, 2, 3, 294, 5};
        DES des = new DES(key);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Element(s) in array not in byte range.");

        des.getBit(arr, 2);
    }
}
