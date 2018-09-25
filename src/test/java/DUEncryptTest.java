import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DUEncryptTest {

    private DES des;
    private int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };
    
    @Test
    public void paths() {
        des = new DES(key);

        // A test with these values detours all DU subpaths listed in the table.
        // (1, 2), (5, 6, 7, 9), (6, 7, 9), (7, 8), (8, 7)
        int[] message = {0,0,0,0,1,1,1,1, 1,0,1,1,0,1,0,0,
                1,1,0,0,1,1,0,0, 0,1,0,0,1,0,0,1,
                0,1,0,1,0,1,1,1, 0,0,1,0,0,1,0,0,
                0,1,0,1,0,1,0,1, 1,1,1,1,1,0,1,1};
        boolean reverse = false;
        boolean bits = false;
        int[] expected = {1,1,0,0,0,1,0,1, 1,1,1,0,1,0,1,1,
                0,1,0,1,1,0,0,1, 0,0,0,0,0,1,1,0,
                1,0,1,0,1,0,0,1, 0,0,1,0,1,1,1,0,
                0,0,1,1,0,0,1,0, 0,1,0,0,1,1,1,1};

        assertArrayEquals(expected, des.encrypt(message, reverse, bits));
    }
}
