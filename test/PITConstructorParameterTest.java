import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

class Data {
    public int[] val;
    public int res;
    public String message;
    public Data(int[] val, int res, String message) {
        this.val = val;
        this.res = res;
        this.message = message;
    }
}

@RunWith(Parameterized.class)
public class PITConstructorParameterTest {
    private static int FAIL = 1;
    public static int OK = 0;

    @Parameterized.Parameters
    public static Collection<Data> data() {
        return Arrays.asList(
                new Data(new int[]{0b00001111, 0b00101101},  FAIL, "Case: Not enough arguments." ),
                new Data(new int[]{ },       FAIL, "Case: Empty key" ),
                new Data(new int[]{ 0b00001111, 0b00000000, 0b11111111, 0b00000000, 0b11111111, 0b00000000, 0b11111111, 0b00000000}, OK , "Case: OK"  ),
                new Data(new int[]{ 0b00001111, 0b00000000, 0b11111111, 0b00000000, 0b11111111, 0b00000000, 0b11111111, 0b00000000, 0b00000000, 0b11111111, 0b00000000 }, OK,  "Case: Key too long."));
    }

    private int[] val;
    private int res;
    private String message;

    public PITConstructorParameterTest(Data data) {
        this.val = data.val;
        this.res = data.res;
        this.message = data.message;
    }

    @Test
    public void constructorTest() {
        try {
            new DES(val);
            if (res == FAIL) {
                Assert.fail(message);
            }
        }
        catch (Exception e) {
            if(res == OK) {
                Assert.fail(message);
            }
        }
    }
}
