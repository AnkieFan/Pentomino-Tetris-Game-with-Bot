package bot;

import game.GameDataManager;
import game.Logic;
import game.Pentomino;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Bot extends Logic {
    protected boolean gameOver = false;

    public Bot(GameDataManager gameDataManager) {
        super(gameDataManager);
    }


    /**
     * Find the best placement of current pentomino
     * @param pent pentomino which should be place
     * @return the best placement with rotate number and position
     */
    public Position findBestPosition(Pentomino pent) {
        // Initialize the best position
        Position bestPosition = new Position(pent.getActPoints()[0], 0);
        // Check if the game is over
        if (isGameover()) {
            afterGameover();
            return null;
        }

        Pentomino p = pent.clone(); //Avoid changing the original pentomino

        int bestScore = -1000000000;

        for (int i = 0; i < 4; i++) { // Rotate for 3 times

            Pentomino temp = p.clone();
            Point pos = findPosInSpecificCol(temp);

            if (pos == null)
                break;

            // Find the position and score in initial column
            int score = evaluationFunction(temp);
            if (score > bestScore) {
                bestScore = score;
                bestPosition.setFirstPoint(temp.getActPoints()[0]);
                bestPosition.setRotateNo(i);
            }

            // Find the positions and scores in the left columns of initial column
            Pentomino pentRight = p.clone();
            while (p.move(-1, 0, this.gameDataManager.getGameMap())) { // Move to left
                temp = p.clone();
                pos = findPosInSpecificCol(temp);
                if (pos == null)
                    break;
                score = evaluationFunction(temp);
                if (score > bestScore) {
                    bestScore = score;
                    bestPosition.setFirstPoint(temp.getActPoints()[0]);
                    bestPosition.setRotateNo(i);
                }
            }

            // Find the positions and scores in the right columns of initial column
            while (pentRight.move(1, 0, this.gameDataManager.getGameMap())) { //Move to right
                temp = pentRight.clone();
                pos = findPosInSpecificCol(temp);
                if (pos == null)
                    break;

                score = evaluationFunction(temp);
                if (score > bestScore) {
                    bestScore = score;
                    bestPosition.setFirstPoint(temp.getActPoints()[0]);
                    bestPosition.setRotateNo(i);
                }
            }

            p.rotate(this.gameDataManager.getGameMap()); // Rotate the initial pentomino
        }
        return bestPosition;
    }

    /**
     * Find the position in specific column
     * This method will let the pentomino descend until it can't, and then return to the position of its first point.
     * @param pent the checked pentomino
     * @return the final position of its first point
     */
    public Point findPosInSpecificCol(Pentomino pent) {
        if (!pent.move(0, 0, this.gameDataManager.getGameMap())) {
            return null;
        }

        for (int i = 0; i < this.gameDataManager.getGameMap().length; i++) {
            if (!pent.move(0, 1, this.gameDataManager.getGameMap())) {
                return pent.getActPoints()[0];
            }
        }
        return null;
    }

    /**
     * When game is over, record down the score in local file
     */
    @Override
    public void afterGameover() {
        super.afterGameover();
        this.gameOver = true;
        outputRecord();
    }

    /**
     * Calculate the score of the current placement, to judge how good the placement is
     * @param pent The current pentomino
     * @return the score of the current placement
     */
    public int evaluationFunction(Pentomino pent) {
        // Three stage of map: before putting pentomino, after putting pentomino, after removing filled line
        boolean[][] mapBeforePut = copyMap(this.gameDataManager.getGameMap());
        boolean[][] mapBeforeRemove = copyMap(mapBeforePut);
        putPent(pent, mapBeforeRemove);
        boolean[][] mapResult = copyMap(mapBeforeRemove);

        // The number of filled lines
        int removeLineNo = removedLineNo(mapResult);
        //The height at which the pentomino has been placed.
        double landingHeight = calHeight(pent);
        //The row number of the highest occupied grid on the board.
        int pileHeight = calPileHeight(mapResult);
        // The number of rows removed multiplied by the grid of the current pentomino contribution in the row being removed.
        int erodedPieceGridsMetric = removeLineNo * calPentContribution(mapBeforePut, mapBeforeRemove);
        // The number of occupied grids on the board.
        int blocksNo = calBlocks(mapResult);
        // Like Blocks, but each occupied grid is multiplied by the number of rows it is in.
        int weightedBlocks = calWeightedBlocks(mapResult);
        // Sum of all horizontal occupied/unoccupied transitions on the board. The boundaries are counted as occupied.
        int boardRowTransitions = calBoardRowTransitions(mapResult);
        // As Row Transitions above but counts vertical transitions. The boundaries are counted as occupied.
        int boardColTransitions = calBoardColTransitions(mapResult);
        // The number of all unoccupied cells that have at least one occupied above them.
        int boardBuriedHoles = calBoardBuriedHoles(mapResult);
        // Sum of all wells on the board.
        int boardWells = calBoardWells(mapResult);

        return (int) ((int) ((-1) * landingHeight * (76.27)) + 33.4 * erodedPieceGridsMetric - 111.36 * boardRowTransitions
                - 111.29 * boardColTransitions - 57.29 * boardBuriedHoles - 33.11 * boardWells + removeLineNo * 31.86
                - pileHeight * 50.7 + blocksNo * 1.98 - weightedBlocks * 4.26);
    }

    /**
     * Calculate the number of weighted blocks
     * @param map game map
     * @return the number of weighted blocks
     */
    private int calWeightedBlocks(boolean[][] map) {
        int result = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                int count = 0;
                if (map[i][j]) {
                    count++;
                }
                result += count * (14 - i);
            }
        }
        return result;
    }

    /**
     * Calculate the number of occupied grids on the board.
     * @param map game map
     * @return the number of occupied grids on the board.
     */
    private int calBlocks(boolean[][] map) {
        int count = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                if (map[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Calculate pile height
     * @param map game map
     * @return the row number of the highest occupied grid on the board.
     */
    private int calPileHeight(boolean[][] map) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                if (map[i][j])
                    return (14 - i);
            }
        }
        return 0;
    }

    /**
     * Calculate landing height
     * @param pent game map
     * @return the height at which the pentomino has been placed.
     */
    public double calHeight(Pentomino pent) {
        int max = 0;
        int min = 0;
        for (int i = 0; i < 5; i++) {
            max = Math.max(pent.getActPoints()[i].y, max);
            min = Math.min(pent.getActPoints()[i].y, min);
        }
        return (14 - (max + min) / 2);
    }

    /**
     * Calculate pentomino's contribution
     * @param map game map before putting pentomino
     * @param newMap game map after putting pentomino
     * @return in filled line, how many grids is contributed by the current pentomino
     */
    public int calPentContribution(boolean[][] map, boolean[][] newMap) {
        int count = 0;
        for (int i = 0; i < 15; i++) {
            if (super.isFilled(i, newMap)) {

                for (int j = 0; j < 5; j++) {
                    if (map[i][j] != newMap[i][j])
                        count++;
                }

            }
        }
        return count;
    }

    /**
     * Calculate row transitions
     * @param mapResult game map
     * @return row transitions
     */
    public int calBoardRowTransitions(boolean[][] mapResult) {
        int result = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 5; j++) {
                if ((j == 0 || j == 4) && mapResult[i][j] == false)
                    result++;
                else if (j == 4)
                    break;
                else {
                    if (mapResult[i][j] != mapResult[i][j + 1])
                        result++;
                }
            }
        }
        return result;
    }

    /**
     * Calculate column transitions
     * @param mapResult game map
     * @return column transitions
     */
    public int calBoardColTransitions(boolean[][] mapResult) {
        int result = 0;
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 15; i++) {
                if ((i == 0 || i == 14) && mapResult[i][j] == false)
                    result++;
                else if (i == 14)
                    break;
                else {
                    if (mapResult[i][j] != mapResult[i + 1][j])
                        result++;
                }
            }
        }
        return result;
    }

    /**
     * Calculate the number of holes
     * @param mapResult game map
     * @return the number of holes
     */
    public int calBoardBuriedHoles(boolean[][] mapResult) {
        boolean[][] map = copyMap(mapResult);
        int result = 0;
        for (int j = 0; j < 5; j++) {
            for (int i = 1; i < 15; i++) {
                if (map[i - 1][j] == true && map[i][j] == false) {
                    result++;
                    map[i][j] = true;
                }
            }
        }
        return result;
    }

    /**
     * Calculate the number of wells
     * @param mapResult game map
     * @return the number of wells
     */
    public int calBoardWells(boolean[][] mapResult) {
        int result = 0;

        for (int j = 0; j < 5; j++) {
            int count = 0;
            for (int i = 1; i < 15; i++) {
                if (i == 14 && checkWell(mapResult, j, i) && checkWell(mapResult, j, i - 1)) {
                    count++;
                    result += count;
                }
                if (i == 14)
                    continue;
                if (checkWell(mapResult, j, i) && (checkWell(mapResult, j, i + 1) || checkWell(mapResult, j, i - 1))) {
                    count++;
                    result += count;
                }
            }
        }
        return result;
    }

    /**
     * Check if this grid is a part of well
     * @param map game map
     * @param x x coordinate of grid
     * @param y y coordinate of grid
     * @return if this grid is a part of well
     */
    private boolean checkWell(boolean[][] map, int x, int y) {
        if (x == 0 && !map[y][x] && map[y][x + 1])
            return true;
        else if (x == 0)
            return false;
        else if (x == 4 && !map[y][x] && map[y][x - 1])
            return true;
        else if (x == 4)
            return false;
        else if (map[y][x - 1] && !map[y][x] && map[y][x + 1])
            return true;
        else
            return false;
    }

    /**
     * Copy the map to avoid changing the original game map.
     * @param map original map
     * @return copied map
     */
    private boolean[][] copyMap(boolean[][] map) {
        boolean[][] newMap = new boolean[15][5];
        for (int i = 0; i < map.length; i++) {
            newMap[i] = Arrays.copyOf(map[i], map.length);
        }
        return newMap;
    }

    /**
     * Put pentomino in the map
     * @param pent pentomino
     * @param map game map
     * @return if put successfully
     */
    public boolean putPent(Pentomino pent, boolean[][] map) {
        for (int i = 0; i < 5; i++) {
            map[pent.getActPoints()[i].y][pent.getActPoints()[i].x] = true;
        }
        return true;
    }

    /**
     * Write out the record to bot records
     */
    public void outputRecord() {
        if (this.gameDataManager.getCurrentScore() > 500) //Exception Data
            return;

        FileWriter fw = null;
        try {
            File f = new File("BOTrecords.csv");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        pw.println(sdf.format(now) + "," + this.gameDataManager.getCurrentScore());
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
