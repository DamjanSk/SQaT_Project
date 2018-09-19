import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

class PermuteData{
    int[] array;
    int[] order;
    boolean bit;
    int[] expected;

    public PermuteData(int[] array, int[] order, boolean bit, int[] expected) {
        this.array = array;
        this.order = order;
        this.bit = bit;
        this.expected = expected;
    }
}

@RunWith(Parameterized.class)
public class PITPermuteTest {
    @Parameterized.Parameters
   public static Collection<PermuteData> data() {
        return Arrays.asList(
                new PermuteData(new int[]{0b10000000, 2, 3, 4, 5}, new int[]{1, 15 }, false, new int[]{1, 1}),
                new PermuteData(new int[]{0b11110000, 0b01010101, 0b00000100}, new int[]{}, false, new int[]{}),
                new PermuteData(new int[]{0b11110000, 0b01010101, 0b00000100}, new int[]{3 , 8+2, 8+3, 16, 17}, false, new int[]{1, 1, 0, 1, 0}),
                new PermuteData(new int[]{0b01111111}, new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }, false, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }),
                new PermuteData(new int[]{0, 1, 1, 0, 0, 1, 1, 0}, new int[]{5, 3, 4, 1, 8, 2, 6, 7}, true, new int[] { 0, 1, 0, 0, 0, 1, 1, 1})
        );
    }

    int[] array;
    int[] order;
    boolean bit;
    int[] expected;

    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    public PITPermuteTest(PermuteData pd) {
        array = pd.array;
        order = pd.order;
        bit = pd.bit;
        expected = pd.expected;
    }

    @Test
    public void test() {
        DES des = new DES(key);
        Assert.assertArrayEquals(des.permute(array, order, bit), expected);
    }
}
