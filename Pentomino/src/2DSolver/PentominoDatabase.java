public class PentominoDatabase {
    /**
     * This data structure represents the pieces.  There are 12 pieces, and each piece can be rotated
     * and flipped over.  Some of these pentominos leave the piece changed because of symmetry.  Each distinct
     * position of each piece has a line in this array.  Each line has 9 elements.  The first element is
     * the pentID of the piece, from 0 to 11.  The remaining 8 elements describe the shape of the piece
     * in the following peculiar way:  One square is assumed to be at position (0,0) in a grid; the square is
     * chosen as the "top-left" square in the piece, in the sense that all the other squares are either to the
     * right of this square in the same row, or are in lower rows.  The remaining 4 squares in the piece are
     * encoded by 8 numbers that give the row and column of each of the remaining squares.   If the eight numbers
     * that describe the piece are (a,b,c,d,e,f,g,h) then when the piece is placed on the board with the top-left
     * square at position (r,c), the remaining squares will be at positions (r+a,c+b), (r+c,c+d), (r+e,c+f), and
     * (r+g,c+h).  this representation is used in the putPiece() and removePiece() methods.
     * <p>
     * Database idea Reference:
     * Eck, D. (March 2006). Pentominos Puzzle Solver. Retrieved from https://math.hws.edu/xJava/PentominosSolver/
     */
    private static final int[][] piece_data = {
            {0, 0, 1, 0, 2, 0, 3, 0, 4},  // Describes piece 1 (the "I" pentomino) in its horizontal orientation.
            {0, 1, 0, 2, 0, 3, 0, 4, 0},  // Describes piece 1 (the "I" pentomino) in its vertical orientation.
            {1, 1, -1, 1, 0, 1, 1, 2, 0}, // The "X" pentomino, in its only orientation.
            {2, 0, 1, 1, 0, 2, -1, 2, 0}, // The "Z"
            {2, 1, 0, 1, 1, 1, 2, 2, 2},
            {2, 0, 1, 1, 1, 2, 1, 2, 2},
            {2, 1, -2, 1, -1, 1, 0, 2, -2},
            {3, 1, 0, 2, 0, 2, 1, 2, 2}, // The "V"
            {3, 0, 1, 0, 2, 1, 0, 2, 0},
            {3, 1, 0, 2, -2, 2, -1, 2, 0},
            {3, 0, 1, 0, 2, 1, 2, 2, 2},
            {4, 0, 1, 0, 2, 1, 1, 2, 1},// The "T"
            {4, 1, -2, 1, -1, 1, 0, 2, 0},
            {4, 1, 0, 2, -1, 2, 0, 2, 1},
            {4, 1, 0, 1, 1, 1, 2, 2, 0},
            {5, 1, 0, 1, 1, 2, 1, 2, 2}, // The "W"
            {5, 1, -1, 1, 0, 2, -2, 2, -1},
            {5, 0, 1, 1, 1, 1, 2, 2, 2},
            {5, 0, 1, 1, -1, 1, 0, 2, -1},
            {6, 0, 1, 0, 2, 1, 0, 1, 2}, // The "U"
            {6, 0, 1, 1, 1, 2, 0, 2, 1},
            {6, 0, 2, 1, 0, 1, 1, 1, 2},
            {6, 0, 1, 1, 0, 2, 0, 2, 1},
            {7, 1, 0, 1, 1, 1, 2, 1, 3}, // The "L"
            {7, 1, 0, 2, 0, 3, -1, 3, 0},
            {7, 0, 1, 0, 2, 0, 3, 1, 3},
            {7, 0, 1, 1, 0, 2, 0, 3, 0},
            {7, 0, 1, 1, 1, 2, 1, 3, 1},
            {7, 0, 1, 0, 2, 0, 3, 1, 0},
            {7, 1, 0, 2, 0, 3, 0, 3, 1},
            {7, 1, -3, 1, -2, 1, -1, 1, 0},
            {8, 0, 1, 1, -2, 1, -1, 1, 0}, // The "N"
            {8, 1, 0, 1, 1, 2, 1, 3, 1},
            {8, 0, 1, 0, 2, 1, -1, 1, 0},
            {8, 1, 0, 2, 0, 2, 1, 3, 1},
            {8, 0, 1, 1, 1, 1, 2, 1, 3},
            {8, 1, 0, 2, -1, 2, 0, 3, -1},
            {8, 0, 1, 0, 2, 1, 2, 1, 3},
            {8, 1, -1, 1, 0, 2, -1, 3, -1},
            {9, 1, -2, 1, -1, 1, 0, 1, 1}, // The "Y"
            {9, 1, -1, 1, 0, 2, 0, 3, 0},
            {9, 0, 1, 0, 2, 0, 3, 1, 1},
            {9, 1, 0, 2, 0, 2, 1, 3, 0},
            {9, 0, 1, 0, 2, 0, 3, 1, 2},
            {9, 1, 0, 1, 1, 2, 0, 3, 0},
            {9, 1, -1, 1, 0, 1, 1, 1, 2},
            {9, 1, 0, 2, -1, 2, 0, 3, 0},
            {10, 1, -1, 1, 0, 1, 1, 2, 1}, // The "F"
            {10, 0, 1, 1, -1, 1, 0, 2, 0},
            {10, 1, 0, 1, 1, 1, 2, 2, 1},
            {10, 1, 0, 1, 1, 2, -1, 2, 0},
            {10, 1, -2, 1, -1, 1, 0, 2, -1},
            {10, 0, 1, 1, 1, 1, 2, 2, 1},
            {10, 1, -1, 1, 0, 1, 1, 2, -1},
            {10, 1, -1, 1, 0, 2, 0, 2, 1},
            {11, 0, 1, 1, 0, 1, 1, 2, 1},// The "P"
            {11, 0, 1, 0, 2, 1, 0, 1, 1},
            {11, 1, 0, 1, 1, 2, 0, 2, 1},
            {11, 0, 1, 1, -1, 1, 0, 1, 1},
            {11, 0, 1, 1, 0, 1, 1, 1, 2},
            {11, 1, -1, 1, 0, 2, -1, 2, 0},
            {11, 0, 1, 0, 2, 1, 1, 1, 2},
            {11, 0, 1, 1, 0, 1, 1, 2, 0},
    };


    /**
     * This method help you to get all the information for a Piece from just a pentID
     * When you create a new Piece, you know the pentID of it. And it will use this method to get information from database
     * But you know, in Database the first number is pentID. If you have already known the pentID, this number is no longer important
     * Like, all the pentominos with same pentID, has the same pentID.
     * So when it return, the first number will be Permutation, instead of pentID
     * Like if pentID is 7, it has 8 different shapes (rotated/flipped from first one),
     * so the permutation will be 0 to 7, which represent different looks of a pentomino
     *
     * @param pentID
     * @return
     */
    public static int[][] getPossiblePiece(int pentID) {
        int[][] result = new int[8][9];
        int resultRow = 0;
        for (int i = 0; i < piece_data.length; i++) {
            if (piece_data[i][0] == pentID) {
                for (int j = 0; j < 9; j++) {
                    result[resultRow][j] = piece_data[i][j];
                }
                resultRow++;
            }
        }

        int emptyCol = 8;
        for (int i = 1; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (result[i][j] != 0) {
                    emptyCol = -1;
                    break;
                }
            }
            if (emptyCol == 8) {
                emptyCol = i;
                break;
            } else {
                emptyCol = 8;
            }
        }

        int[][] newResult = new int[emptyCol][9];
        System.arraycopy(result, 0, newResult, 0, newResult.length);

        // Turn the first position from pentID to Permutation
        for (int i = 0; i < newResult.length; i++) {
            newResult[i][0] = i;
        }
        return newResult;
    }
}
