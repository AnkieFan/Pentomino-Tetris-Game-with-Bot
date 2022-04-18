public class Piece { // a piece of pentominos

    public int pentID;

    public int[] currentPent;
    /* Every Piece only have 1 current Pentomino, which makes this object represent one Pentomino at one time.
    The default current Pentomino is the first Pentomino with this pentID in Database. */

    public int[][] possiblePent;
    //All the Pentominos with this pentID (rotate/flip from current Pentomino)
    // e.g: when pentID is 7, possiblePent will be:
    // [[0, 1, 0, 1, 1, 1, 2, 1, 3], [1, 1, 0, 2, 0, 3, -1, 3, 0], [2, 0, 1, 0, 2, 0, 3, 1, 3], [3, 0, 1, 1, 0, 2, 0, 3, 0], [4, 0, 1, 1, 1, 2, 1, 3, 1], [5, 0, 1, 0, 2, 0, 3, 1, 0], [6, 1, 0, 2, 0, 3, 0, 3, 1], [7, 1, -3, 1, -2, 1, -1, 1, 0]]
    // The first number is permutation, not pentID anymore
    public boolean usedState;
    // Because if a Piece has been used, it can not be used again.
    // This boolean is to record if this Piece is used. Default value is false.
    public int posRow, posCol;
    // If the piece is put on the field, the position of top-left one will be recorded as these two variables.

    /**
     * Constructor
     * create a new piece: execute: Piece pieceName = new Piece(pentID);
     *
     * @param pentID
     */
    public Piece(int pentID) {
        this.pentID = pentID;
        possiblePent = PentominoDatabase.getPossiblePiece(pentID);// You get the pentID, and you can get the remain information from PentominoDatabases
        usedState = false; // to set the usedState to the default value "false"
        currentPent = possiblePent[0];// to set the currentPent to the default value: the first Pentomino with this pentID in Database
        posRow = -1; // -1 means it is not been put on the field
        posCol = -1;
    }

    /**
     * Sometimes you want to use the rotate/flip Pentomino of this Piece, but a Piece can only represent a Pentomino at one time
     * So you switch the attribution "currentPent". You can choose from the array "possiblePent", that contains all the possible
     * rotated/flipped Pentominos. This method can replace all the flip or rotate methods.
     *
     * @param permutation the permutation of new Pentomino
     */
    public void setCurrentPent(int permutation) {
        if (permutation >= possiblePent.length || permutation < 0)
            return;

        this.currentPent = possiblePent[permutation];
    }


    /**
     * Set the usedState to be "true" after put this piece on field successfully, which means this Piece has already been used.
     * And also record down the position of it in the field.
     *
     * @param usedState the usedState you want to set
     * @param row       the row position of it in the field
     * @param col       the column position of it in the field
     */
    public void setUsed(boolean usedState, int row, int col) {
        this.usedState = usedState;
        this.posRow = row;
        this.posCol = col;
    }


    public int getPentID() {
        return pentID;
    }

    public int[][] getPossiblePent() {
        return possiblePent;
    }

    public boolean getUsed() {
        return usedState;
    }
}
