import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PITPermuteExceptionTests {

    DES des;

    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    @Before
    public void setUp() {
        des = new DES(key);
    }

    private void runnerNull(Runnable runnable) {
        try {
            runnable.run();
            Assert.fail("Should throw exception");
        }
        catch (NullPointerException e) {
            // ok
        }
        catch (Exception e) {
            Assert.fail("Wrong exception was thrown");
        }
    }

    private void runnerIllegalArgument(String message, Runnable runnable) {
        try {
            runnable.run();
            Assert.fail("Should throw exception");
        }
        catch (IllegalArgumentException e) {
            Assert.assertEquals(message, e.getMessage());
        }
        catch (Exception e) {
            Assert.fail("Wrong exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void nullTests() {
        // bit true
        runnerNull(() -> des.permute(null, null, true));
        runnerNull(() -> des.permute(null, new int[] {1, 2, 3}, true));
        runnerNull(() -> des.permute(new int[] {1, 1, 1}, null, true));

        runnerNull(() -> des.permute(null, null, false));
        runnerNull(() -> des.permute(null, new int[] {1, 2, 3}, false));
        runnerNull(() -> des.permute(new int[] {1, 1, 1}, null, false));
    }

    @Test
    public void argumentArrayTests() {
        String message = "Element(s) in array not in byte range.";
        runnerIllegalArgument(message, () -> des.permute(new int[]{1, 2, 3, 4, 256}, new int[] {1, 2 ,3}, false));
        runnerIllegalArgument(message, () -> des.permute(new int[] {1, 2, -1, 4, 5}, new int[] {1, 2, 3}, false));

        runnerIllegalArgument(message, () -> des.permute(new int[]{1, 2, 3, 4, 256}, new int[] {1, 2 ,3}, true));
        runnerIllegalArgument(message, () -> des.permute(new int[] {1, 2, -1, 4, 5}, new int[] {1, 2, 3}, true));
    }

    @Test
    public void orderTests() {
        String message = "Invalid order.";

        runnerIllegalArgument(message, () -> des.permute(new int[]{}, new int[] {1}, true));
        runnerIllegalArgument(message, () -> des.permute(new int[]{}, new int[] {1}, false));

        runnerIllegalArgument(message, () -> des.permute(new int[] {1}, new int[] {0}, true));
        runnerIllegalArgument(message, () -> des.permute(new int[] {1}, new int[] {0}, false));

        runnerIllegalArgument(message, () -> des.permute(new int[] {1}, new int[] {9}, false));
        runnerIllegalArgument(message, () -> des.permute(new int[] {1}, new int[] {2}, true));
    }

}
