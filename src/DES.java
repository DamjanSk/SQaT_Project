import java.util.Arrays;

// Methods changed from private to package-private to allow testing.
@SuppressWarnings("FieldCanBeLocal")
class DES {
    // The 56 bit key, and the subkeys.
    private int[] key;
    private int[][] subkeys = new int[16][48];

    // The number of rotations that are to be done on each round.
    private final int[] rotations = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    // Permutation for creating the 56 bit key from the 8 byte input key.
    private final int[] PKey1 = {
            57, 49, 41, 33, 25, 17, 9,  1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 27, 19, 11, 3,
            60, 52, 44, 36, 63, 55, 47, 39,
            31, 23, 15, 7,  62, 54, 46, 38,
            30, 22, 14, 6,  61, 53, 45, 37,
            29, 21, 13, 5,  28, 20, 12, 4
    };

    // Permutation for creating the subkeys.
    private final int[] PKey2 = {
            14, 17, 11, 24, 1,  5,  3,  28,
            15, 6,  21, 10, 23, 19, 12, 4,
            26, 8,  16, 7,  27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };

    // Initial permutation for the message.
    private final int[] PMessage1 = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9,  1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Expansion table.
    private final int[] Expansion = {
            32, 1,  2,  3,  4,  5,  4,  5,
            6,  7,  8,  9,  8,  9,  10, 11,
            12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21,
            22, 23, 24, 25, 24, 25, 26, 27,
            28, 29, 28, 29, 30, 31, 32, 1
    };

    // Substitution/shrinking tables
    private final int[][] Substitution = { {
            14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
            0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
            4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
            15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
    }, {
            15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
            3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
            0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
            13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
    }, {
            10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
            13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
            13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
            1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
    }, {
            7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
            13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
            10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
            3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
    }, {
            2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
            14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
            4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
            11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
    }, {
            12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
            10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
            9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
            4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
    }, {
            4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
            13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
            1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
            6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
    }, {
            13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
            1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
            7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
            2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
    } };

    // Message permutation used after each round.
    private final int[] PMessage2 = {
            16, 7,  20, 21,
            29, 12, 28, 17,
            1,  15, 23, 26,
            5,  18, 31, 10,
            2,  8,  24, 14,
            32, 27, 3,  9,
            19, 13, 30, 6,
            22, 11, 4,  25
    };

    // The final message permutation.
    private final int[] PMessage3 = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9,  49, 17, 57, 25
    };


    DES(int[] inputKey) {
        // Generate the key.
        key = permute(inputKey, PKey1, false);

        // Generate the subarrays.
        int[] left = Arrays.copyOfRange(key, 0, 28);
        int[] right = Arrays.copyOfRange(key, 28, 56);

        for(int i=0; i<16; i++) {
            // Get the number of rotations to be done this round.
            int rotation = rotations[i];

            // Shift the left and right subarrays.
            left = shift(left, rotation);
            right = shift(right, rotation);

            // Join the subarrays.
            int[] both = new int[56];
            for(int j=0; j<28; j++) {
                both[j] = left[j];
                both[j+28] = right[j];
            }

            // Generate the subkey and save it.
            subkeys[i] = permute(both, PKey2, true);
        }
    }


    // Permute a bit or byte array. Return a bit array.
    int[] permute(int[] array, int[] order, boolean bit) {
        int[] newOrder = new int[order.length];
        for(int i=0; i<order.length; i++) {
            newOrder[i] = bit? array[order[i]-1] : getBit(array, order[i]-1);
        }

        return newOrder;
    }


    // Gets a bit from a byte array.
    int getBit(int[] array, int pos) {
        // Retrieve the right byte.
        int ibyte = array[pos/8];
        int offset = pos%8;

        // Convert the byte to an array of 8 bits.
        int[] abyte = {0, 0, 0, 0, 0, 0, 0, 0};
        int i = 7;
        while(ibyte>0) {
            abyte[i] = ibyte%2;
            ibyte /= 2;
            i -= 1;
        }

        // Retrieve the right bit.
        return abyte[offset];
    }


    // Shift left function.
    int[] shift(int[] array, int amount) {
        if(array == null || amount == 0 || array.length < 2)
            return array;

        // Speed optimization + dealing with negatives.
        amount = amount%array.length;
        if(amount < 0)
            amount += array.length;

        int[] result = Arrays.copyOf(array, array.length);

        int first = result[0];
        for(int i=1; i<result.length; i++)
            result[i-1] = result[i];
        result[result.length-1] = first;

        return shift(result, amount-1);
    }


    int[] encrypt(int[] message, boolean reverse, boolean bits) {
        // Do the initial permutation.
        int[] cipher = permute(message, PMessage1, bits);

        // Do the 16 rounds.
        int[] left = Arrays.copyOfRange(cipher, 0, 32);
        int[] right = Arrays.copyOfRange(cipher, 32, 64);

        for(int i=0; i<16; i++) {
            int[] rightOriginal = Arrays.copyOf(right, right.length);

            // Expand right.
            right = permute(right, Expansion, true);

            // Do XOR with the appropriate subkey.
            for(int j=0; j<right.length; j++) {
                right[j] = right[j] ^ subkeys[ reverse? 15-i : i ][j];
            }

            // Shrink right.
            int[] rightShrinked = new int[32];
            for(int j=0; j<8; j++) {
                int b = 6*j;
                int row = right[b]*2 + right[b+5];
                int column = right[b+1]*8 + right[b+2]*4 + right[b+3]*2 + right[b+4];

                int substitution = Substitution[j][row*16+column];
                int k=3;
                while (substitution > 0) {
                    rightShrinked[j*4 + k] = substitution%2;
                    substitution /= 2;
                    k--;
                }
            }
            right = rightShrinked;

            // Permute right.
            right = permute(right, PMessage2, true);

            // Do XOR with left.
            for(int j=0; j<right.length; j++) {
                right[j] = right[j] ^ left[j];
            }

            // Right has the value of (left XOR permute(shrink(subkey XOR expand(right))))
            // Give left the value of originalRight.
            left = rightOriginal;
        }

        // Combine left and right into cipher.
        for(int i=0; i<32; i++) {
            cipher[i] = right[i];
            cipher[i+32] = left[i];
        }

        // Do the final permutation.
        cipher = permute(cipher, PMessage3, true);

        return cipher;
    }

    int[] encrypt(int[] message) {
        return encrypt(message, false, false);
    }

    int[] decrypt(int[] message) {
        return encrypt(message, true, true);
    }
}