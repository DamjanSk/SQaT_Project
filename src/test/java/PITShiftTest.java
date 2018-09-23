import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

class ShiftData {
    public int[] val;
    public int amount;
    public int[] expected;
    public ShiftData(int[] val, int amount, int[] expected) {
        this.val = val;
        this.amount = amount;
        this.expected = expected;
    }
}

@RunWith(Parameterized.class)
public class PITShiftTest {

    @Parameterized.Parameters
    public static Collection<ShiftData> data() {
        return Arrays.asList(
                new ShiftData(new int[]{},                              -1,    new int[]{}),
                new ShiftData(new int[]{},                               0,    new int[]{}),
                new ShiftData(new int[]{},                               1,    new int[]{}),
                new ShiftData(new int[]{},                              -5,    new int[]{}),
                new ShiftData(new int[]{},                               0,    new int[]{}),
                new ShiftData(new int[]{},                              50,    new int[]{}),

                new ShiftData(new int[]{5},                             -1,    new int[]{5}),
                new ShiftData(new int[]{3},                              0,    new int[]{3}),
                new ShiftData(new int[]{-1},                             1,    new int[]{-1}),
                new ShiftData(new int[]{9},                             -5,    new int[]{9}),
                new ShiftData(new int[]{-5},                             1,    new int[]{-5}),
                new ShiftData(new int[]{0},                             50,    new int[]{0}),

                new ShiftData(new int[]{1, 2, 3},                       -1,    new int[]{3, 1, 2}),
                new ShiftData(new int[]{2, 2, 2, 2},                     0,    new int[]{2, 2, 2, 2}),
                new ShiftData(new int[]{-5, 0, 5},                       1,    new int[]{0, 5, -5}),
                new ShiftData(new int[]{1, 2},                          -5,    new int[]{2, 1}),
                new ShiftData(new int[]{10, 20, 30, 40, 50, 60},         6,    new int[]{10, 20, 30, 40, 50, 60}),
                new ShiftData(new int[]{1, 1, 2, 3, 5, 8, 13},          50,    new int[]{1, 2, 3, 5, 8, 13, 1}),
                new ShiftData(null, 10 , null)
        );
    }

    private int[] val;
    private int amount;
    private int[] expected;
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    public PITShiftTest(ShiftData data) {
        this.val = data.val;
        this.amount = data.amount;
        this.expected = data.expected;
    }

    @Test
    public void constructorTest() {
        DES des = new DES(key);
        Assert.assertArrayEquals(des.shift(val, amount), expected);
    }
}
