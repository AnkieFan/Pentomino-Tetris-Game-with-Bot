import java.util.Random;

public class PentDB {
    public int[][] pieces;
    public PentDB(int length, int width) {
        this.pieces = new int[][]{
                {4, 2, length + 2, length * 2 + 1, length * 2 + 2, length * 2 + 3},// The "T"
                {4, 3, length + 1, length + 2, length + 3, length * 2 + 3},
                {4, 1, 2, 3, length + 2, length * 2 + 2},
                {4, 1, length + 1, length + 2, length + 3, length * 2 + 1},
                {7, 1, 2, 3, 4, length + 1}, // The "L"
                {7, 1, 2, length + 2, length * 2 + 2, length * 3 + 2},
                {7, 4, length + 1, length + 2, length + 3, length + 4},
                {7, 1, length + 1, length * 2 + 1, length * 3 + 1, length * 3 + 2},
                {7, 2, length + 2, length * 2 + 2, length * 3 + 1, length * 3 + 2},
                {7, 1, length + 1, length + 2, length + 3, length + 4},
                {7, 1, 2, length + 1, length * 2 + 1, length * 3 + 1},
                {7, 1, 2, 3, 4, length + 4},
                {11, 2, length + 1, length + 2, length * 2 + 1, length * 2 + 2},// The "P"
                {11, 1, 2, length + 1, length + 2, length + 3},
                {11, 1, 2, length + 1, length + 2, length * 2 + 1},
                {11, 1, 2, 3, length + 2, length + 3},
                {11, 1, 2, 3, length + 1, length + 2},
                {11, 1, 2, length + 1, length + 2, length * 2 + 2},
                {11, 2, 3, length + 1, length + 2, length + 3},
                {11, 1, length + 1, length + 2, length * 2 + 1, length * 2 + 2},

                {0, 1, 2, 3, 4, 5, length * width + 1},  // Describes piece 1 (the "I" pentomino) in its horizontal orientation.
                {0, 1, length + 1, 2 * length + 1, 3 * length + 1, 4 * length + 1, length * width + 1},  // Describes piece 1 (the "I" pentomino) in its vertical orientation.
                {1, 2, length + 1, length + 2, length + 3, 2 * length + 2, length * width + 2}, // The "X" pentomino, in its only orientation.
                {2, 1, 2, length + 2, length * 2 + 2, length * 2 + 3, length * width + 2}, // The "Z"
                {2, 3, length + 1, length + 2, length + 3, 2 * length + 1, length * width + 2},
                {2, 2, 3, length + 2, 2 * length + 1, 2 * length + 2, length * width + 2},
                {2, 1, length + 1, length + 2, length + 3, 2 * length + 3, length * width + 2},
                {3, 1, 2, 3, length + 1, length * 2 + 1, length * width + 3}, // The "V"
                {3, 1, length + 1, 2 * length + 1, 2 * length + 2, 2 * length + 3, length * width + 3},
                {3, 1, 2, 3, length + 3, length * 2 + 3, length * width + 3},
                {3, 3, length + 3, length * 2 + 1, length * 2 + 2, length * 2 + 3, length * width + 3},

                {5, 2, 3, length + 1, length + 2, length * 2 + 1, length * width + 6}, // The "W"
                {5, 1, 2, length + 2, length + 3, length * 2 + 3, length * width + 6},
                {5, 3, length + 2, length + 3, length * 2 + 1, length * 2 + 2, length * width + 6},
                {5, 1, length + 1, length + 2, length * 2 + 2, length * 2 + 3, length * width + 6},
                {6, 1, 3, length + 1, length + 2, length + 3, length * width + 7}, // The "U"
                {6, 1, 2, length + 2, length * 2 + 1, length * 2 + 2, length * width + 7},
                {6, 1, 2, 3, length + 1, length + 3, length * width + 7},
                {6, 1, 2, length + 1, length * 2 + 1, length * 2 + 2, length * width + 7},

                {8, 1, 2, 3, length + 3, length + 4, length * width + 9}, // The "N"
                {8, 2, length + 2, length * 2 + 1, length * 2 + 2, length * 3 + 1, length * width + 9},
                {8, 1, 2, length + 2, length + 3, length + 4, length * width + 9},
                {8, 2, length + 1, length + 2, length * 2 + 1, length * 3 + 1, length * width + 9},
                {8, 2, 3, 4, length + 1, length + 2, length * width + 9},
                {8, 1, length + 1, length + 2, length * 2 + 2, length * 3 + 2, length * width + 9},
                {8, 3, 4, length + 1, length + 2, length + 3, length * width + 9},
                {8, 1, length + 1, length * 2 + 1, length * 2 + 2, length * 3 + 2, length * width + 9},
                {9, 1, 2, 3, 4, length + 3, length * width + 10}, // The "Y"
                {9, 2, length + 2, length * 2 + 1, length * 2 + 2, length * 3 + 2, length * width + 10},
                {9, 2, length + 1, length + 2, length + 3, length + 4, length * width + 10},
                {9, 1, length + 1, length + 2, length * 2 + 1, length * 3 + 1, length * width + 10},
                {9, 3, length + 1, length + 2, length + 3, length + 4, length * width + 10},
                {9, 1, length + 1, length * 2 + 1, length * 2 + 2, length * 3 + 1, length * width + 10},
                {9, 1, 2, 3, 4, length + 2, length * width + 10},
                {9, 2, length + 1, length + 2, length * 2 + 2, length * 3 + 2, length * width + 10},
                {10, 3, length + 1, length + 2, length + 3, length * 2 + 2, length * width + 11}, // The "F"
                {10, 2, length + 1, length + 2, length * 2 + 2, length * 2 + 3, length * width + 11},
                {10, 2, length + 1, length + 2, length + 3, length * 2 + 1, length * width + 11},
                {10, 1, 2, length + 2, length + 3, length * 2 + 2, length * width + 11},
                {10, 2, length + 1, length + 2, length + 3, length * 2 + 3, length * width + 11},
                {10, 2, length + 2, length + 3, length * 2 + 1, length * 2 + 2, length * width + 11},
                {10, 1, length + 1, length + 2, length + 3, length * 2 + 2, length * width + 11},
                {10, 2, 3, length + 1, length + 2, length * 2 + 2, length * width + 11},
        };
        shuffleArray();
    }

    public void shuffleArray(){
        Random random = new Random();
        for (int i = this.pieces.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            // Swap array[i] and array[index]
            int[] temp = this.pieces[i];
            this.pieces[i] = this.pieces[index];
            this.pieces[index] = temp;
        }
    }
}
