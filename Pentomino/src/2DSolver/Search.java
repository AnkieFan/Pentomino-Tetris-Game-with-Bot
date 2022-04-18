import java.util.Arrays;

public class Search {
    //Initialize the input
    public static int horizontalGridSize = setHorizontalGridSize();
    public static int verticalGridSize = setVerticalGridSize();

    public static char[] input = setInput();
    public static Piece[] pieces = setPieces(); // An array to store all the valid pentominos. See setPieces method

    //Static UI class to display the board
    public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

    /**
     * Read the input from users
     *
     * @return
     */
    public static int setHorizontalGridSize() {
        java.util.Scanner s = new java.util.Scanner(System.in);
        System.out.println("Please type in the column number of rectangle (integer):");
        System.out.print("-->");
        return s.nextInt();
    }

    public static int setVerticalGridSize() {
        java.util.Scanner s = new java.util.Scanner(System.in);
        System.out.println("Please type in the row number of rectangle (integer):");
        System.out.print("-->");
        return s.nextInt();
    }

    /**
     * Turn the input from string to char[]
     *
     * @return pentominos name
     */
    public static char[] setInput() {
        java.util.Scanner s = new java.util.Scanner(System.in);
        System.out.println("Please type in the valid types of Pentominos (characters separated with ','):");
        System.out.print("-->");
        String chars = s.nextLine();
        char[] tempInput = new char[chars.length() / 2 + 1];
        for (int i = 0; i < chars.length(); i += 2) {
            tempInput[i / 2] = chars.toUpperCase().charAt(i);
        }
        return tempInput;
    }

    /**
     * Turn every character in input[] into pentID, and create new piece with these pentID, to initialize piece[]
     * So that the piece[] is exactly the valid pentominos that user input
     *
     * @return Piece array stores all usable pentominos
     */
    public static Piece[] setPieces() {
        Piece[] pieces = new Piece[input.length];
        for (int i = 0; i < input.length; i++) {
            int pentID = characterToID(input[i]);
            pieces[i] = new Piece(pentID);
        }
        return pieces;
    }

    /**
     * Get as input the character representation of a pentomino and translate it into its corresponding numerical value (ID)
     *
     * @param character a character representating a pentomino
     * @return the corresponding ID (numerical value)
     */
    private static int characterToID(char character) {
        int pentID = -1;
        if (character == 'I') {
            pentID = 0;
        } else if (character == 'X') {
            pentID = 1;
        } else if (character == 'Z') {
            pentID = 2;
        } else if (character == 'V') {
            pentID = 3;
        } else if (character == 'T') {
            pentID = 4;
        } else if (character == 'W') {
            pentID = 5;
        } else if (character == 'U') {
            pentID = 6;
        } else if (character == 'L') {
            pentID = 7;
        } else if (character == 'N') {
            pentID = 8;
        } else if (character == 'Y') {
            pentID = 9;
        } else if (character == 'F') {
            pentID = 10;
        } else if (character == 'P') {
            pentID = 11;
        }
        return pentID;
    }

    /**
     * Initialize the field, and call branchSearch method
     */
    public static void search() {
        //Quick check:
        if ((horizontalGridSize * verticalGridSize) % 5 != 0 || (input.length * 5) < (horizontalGridSize * verticalGridSize)) {
            System.out.println("No solution!");
            System.exit(0);
        }

        // Initialize an empty board
        int[][] field = new int[horizontalGridSize][verticalGridSize];

        for (int i = 0; i < field.length; i++) {
            // -1 in the state matrix corresponds to empty square
            // Any positive number identifies the ID of the pentomino
            Arrays.fill(field[i], -1);
        }
        // Start the DFS search
        // branches: we need to cut meaningless branches
        branchSearch(field);
        System.out.println(Arrays.deepToString(field));

        if (isFilled(field)) {
            //display the field
            ui.setState(field);
            System.out.println("Solution found");
        } else {
            System.out.println("No Solution!");
            System.exit(0);
        }

    }

    /**
     * Do the whole DFS search
     * Piece by piece: pentominos first, then find empty grid
     * Grid by grid: find empty grid, then pentomino
     *
     * @param field the field you want to fill
     * @return the field with solution. But if there is no solution, it will return null.
     */
    public static int[][] branchSearch(int[][] field) { // try to
        if (isFilled(field))
            return field;
        for (int i = 0; i < field.length; i++) { // 2 for loops to find the empty block in field, to try to put the piece in
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == -1) {

                    for (int k = 0; k < pieces.length; k++) { // Pick a pentomino from the valid pentominos which are defined by user
                        if (pieces[k].usedState) continue; // Only use the pentomino which has not been used


                        for (int p = 0; p < pieces[k].possiblePent.length; p++) { // try every rotated/flipped piece, see if it can be put in specific position
                            pieces[k].setCurrentPent(p); // set current pentomino (its exactly the process of rotate or flip)
                            int[][] newField = putPiece(pieces[k], field, i, j);
                            // try if the piece can be put in field. if newField is null, means the piece can not be put.
                            if (newField != null) { // if the piece is put in successfully, then go on
                                pieces[k].setUsed(true, i, j); // set the used state of the piece to 'true', and record its position
                                if (obviousBlockExists(field)) { // pruning
                                    removePiece(pieces[k], field);
                                    continue;
                                }
                                branchSearch(newField); // recursion
                                if (isFilled(field))
                                    return field;
                                else {
                                    removePiece(pieces[k], field);
                                }
                                // if it met that all of its branches are failed, it will remove the current piece, and continue to do the loop
                            }
                        }
                        pieces[k].setCurrentPent(0);
                    }
                }
            }
        }
        return field;
    }

    /**
     * It will put the current pentomino of the piece in to specific position.
     * It will check if you can put the piece at first. If not, it will immediately return 'null' and will not change the field.
     * If the piece pass the checks, it will been put on the field. And what is return is the field with this piece put in
     *
     * @param p     the piece you wan to put
     * @param field the board
     * @param row   the row number of the position you want to put
     * @param col   the column number of the position you want to put
     * @return return the field with the piece put in. But if the piece can not be put in this position, it will return 'null'
     */
    public static int[][] putPiece(Piece p, int[][] field, int row, int col) {
        if (isFilled(field))
            return field;
        if (!checkValid(p, field, row, col, -1))
            return null;

        field[row][col] = p.pentID;
        for (int i = 1; i < 9; i += 2) {
            field[row + p.currentPent[i]][col + p.currentPent[i + 1]] = p.pentID;
        }
        ui.setState(field);
        return field;
    }

    /**
     * It will remove the current pentomino of the piece from field
     *
     * @param p     the piece you wan to remove
     * @param field the board
     */
    public static void removePiece(Piece p, int[][] field) {
        if (!checkValid(p, field, p.posRow, p.posCol, p.pentID))
            return;

        field[p.posRow][p.posCol] = -1;
        for (int i = 1; i < 9; i += 2) {
            field[p.posRow + p.currentPent[i]][p.posCol + p.currentPent[i + 1]] = -1;
        }
        p.setUsed(false, -1, -1);
    }


    /**
     * Check if the field is filled.
     *
     * @param field the field
     * @return if the field is filled, return true.
     */
    public static boolean isFilled(int[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if you put the piece in the field, it will be out of field or not
     *
     * @param p     the piece you want to check
     * @param field the field
     * @param row   the position of where you want to put the piece
     * @param col   the position of where you want to put the piece
     * @return if it is in the range, return true
     */
    public static boolean checkSide(Piece p, int[][] field, int row, int col) {
        if (row < 0 || col < 0 || row > field.length - 1 || col > field[0].length - 1) // Check if the top-left block is outside
            return false;

        for (int i = 1; i < 9; i += 2) {
            if (row + p.currentPent[i] > field.length - 1 || col + p.currentPent[i + 1] > field[0].length - 1 || row + p.currentPent[i] < 0 || col + p.currentPent[i + 1] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the specific area in field is all filled with checkedValue.
     * If checkedValue = -1, this method can be used for check if the area is empty. If yes, you can put the piece in.
     * If checkedValue = number, this method can be used for check if the area is filled with this number(such as pentID). If yes, you can remove the piece.
     *
     * @param p            the piece which should cover in the field
     * @param field        current field
     * @param row          the row number which the piece will be put, which is equal to the row of top-left block of the piece
     * @param col          the col number which the piece will be put, which is equal to the col of top-left block of the piece
     * @param checkedValue If checkedValue = -1, this method can be used for check if the area which will be filled is empty.
     * @return if the area is filled with checkedValue
     */
    public static boolean checkValid(Piece p, int[][] field, int row, int col, int checkedValue) {
        if (!checkSide(p, field, row, col))
            return false;

        if (field[row][col] != checkedValue)// Check if the top-left block is empty/equal to checkedValue
            return false;

        for (int i = 1; i < 9; i += 2) {
            if (field[row + p.currentPent[i]][col + p.currentPent[i + 1]] != checkedValue)
                return false;
        }

        return true;
    }

    /**
     * Test if there is obvious block exits, which means there are less than 5 grids are surrounded by pentominos.
     *
     * @param field the current field.
     * @return if there is obvious block
     */
    public static boolean obviousBlockExists(int[][] field) {
        boolean[][] tempTest = new boolean[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                int count = countEmptyBlock(i, j, field, tempTest);
                if (count < 5 && count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * To count how many grids are surrounded by pentominos.
     *
     * @param row      the current grid's row position
     * @param col      the current grid's column position
     * @param field    current field
     * @param tempTest the boolean array to record if this grid is been test
     * @return the number of grids which are surrounded by pentominos
     */
    private static int countEmptyBlock(int row, int col, int[][] field, boolean[][] tempTest) {
        if (row < 0 || row >= field.length || col < 0 || col >= field[0].length || tempTest[row][col] || field[row][col] != -1) {
            return 0;
        }
        tempTest[row][col] = true;
        return countEmptyBlock(row, col + 1, field, tempTest) + countEmptyBlock(row + 1, col, field, tempTest) + 1;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        search();
        long endTime = System.currentTimeMillis();
        System.out.println("Running time： " + ((endTime - startTime) / 1000) + "s and " + (endTime - startTime) % 1000 + " ms.");
    }

}

