import java.util.Arrays;

public class Main {
    private static int[] key = {
            0b00001111, 0b00000000, 0b11111111, 0b00000000,
            0b11111111, 0b00000000, 0b11111111, 0b00000000
    };

    private static int[] message = {51, 232, 204, 0, 255, 110, 63, 210};


    public static void main(String[] args) {
        DES des = new DES(key);

//        int[] encrypted = des.encrypt(message);
//        int[] decrypted = des.decrypt(encrypted);
//
//        System.out.println("Message: " + Arrays.toString(message));
//        System.out.println("Encrypted: " + Arrays.toString(toByteArray(encrypted)));
//        System.out.println("Decrypted: " + Arrays.toString(toByteArray(decrypted)));
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