import java.util.Arrays;

public class Main {
    private static int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    private static int[] message = {0, 0, 0, 212, 0, 0, 0, 0};
    private static int[] message2 = {0,0,0,0,1,1,1,1, 1,0,1,1,0,1,0,0,
            1,1,0,0,1,1,0,0, 0,1,0,0,1,0,0,1,
            0,1,0,1,0,1,1,1, 0,0,1,0,0,1,0,0,
            0,1,0,1,0,1,0,1, 1,1,1,1,1,0,1,1};

    public static void main(String[] args) {
        DES des = new DES(key);

        int[] encrypted = des.encrypt(message, true, false);
        int[] decrypted = des.encrypt(encrypted, true, true);

        System.out.println("Message: " + Arrays.toString(message));
        System.out.println("Encrypted: " + Arrays.toString(encrypted));
        System.out.println("Decrypted: " + Arrays.toString(decrypted));
    }

    // Converts a bit array to a byte array.
    private static int[] toByteArray(int[] bitArray) {
        int[] byteArray = new int[bitArray.length/8];

        for(int i=0; i<bitArray.length/8; i++) {
            int val = 0;
            int multiplier = 128;
            for(int j=0; j<8; j++) {
                val += bitArray[i*8+j]*multiplier;
                multiplier /= 2;
            }

            byteArray[i] = val;
        }

        return byteArray;
    }
}