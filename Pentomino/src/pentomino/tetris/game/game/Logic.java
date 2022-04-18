package game;

import java.awt.*;

/**
 * General Logic of the whole program, both player and bot mode
 * The gravity is Naive Gravity.
 * Implement the same thing with '7bags' in Tetris: There must be the same pentomino in 12 consecutive
 * The next pentomino is random
 * After a line is filled and removed, the score will increase 1.
 * Game over: when the new pentomino cannot be shown in board, the game is over.
 */
public abstract class Logic {
    public GameDataManager gameDataManager;
    public int pentIndex;

    /**
     * Connect with game data manager.
     * Set the first pentomino and next one's index.
     * @param gameDataManager Connected game data manager
     */
    public Logic(GameDataManager gameDataManager) {
        this.gameDataManager = gameDataManager;
        gameDataManager.setActPent(new Pentomino());
        this.pentIndex = 1;
    }

    /**
     * Check if the game is over.
     * When the new pentomino cannot be shown in board, the game is over.
     * @return the game is over or not
     */
    public boolean isGameover() {
        if(this.gameDataManager.getCurrentScore()>500)
            return true;

        Point[] act = this.gameDataManager.getActPent().getActPoints();

        boolean[][] map = this.gameDataManager.getGameMap();
        int UPPER_BORDER = 0;
        for (int i = 0; i < act.length; i++) {
            if (map[act[i].y][act[i].x])
                return true;
            if (act[i].y < UPPER_BORDER)
                return true;
        }
        return false;
    }

    /**
     * Get how many lines will be removed in the map
     * @param map The map need to remove the filled lines
     * @return The number of removed lines
     */
    public int removedLineNo(boolean[][] map) {
        int counter = 0;

        for (int y = 14; y > 0; y--) {
            if (isFilled(y, map)) {
                removeLines(y, map);
                counter++;
                y = 15;
            }

        }
        return counter;
    }

    /**
     * Remove the filled line from the map
     * @param row The line number which should be removed
     * @param map which should remove line
     */
    public void removeLines(int row, boolean[][] map) {
        for (int x = 0; x < 5; x++) {
            for (int y = row; y > 0; y--) {
                map[y][x] = map[y - 1][x];
            }
            map[0][x] = false;
        }
    }

    /**
     * Check if the line if filled
     * @param y Line's index
     * @param map which should be checked
     * @return if this line is filled
     */
    public boolean isFilled(int y, boolean[][] map) {
        for (int x = 0; x < 5; x++) {
            if (!map[y][x])
                return false;
        }
        return true;
    }

    /**
     * Try to move the pentomino one grid down
     * If the pentomino cannot be moved down, it will remove the filled line, add the score and check if the game is over.
     * @return If this pentomino cannot be move down
     */
    public boolean keyDown() {
        if (!gameDataManager.isStart() || gameDataManager.isPause())
            return false;
        if (this.gameDataManager.getActPent().move(0, 1, this.gameDataManager.getGameMap()))
            return true;

        synchronized (this.gameDataManager) {
            boolean[][] map = this.gameDataManager.getGameMap();
            Point[] act = this.gameDataManager.getActPent().getActPoints();
            for (int i = 0; i < act.length; i++) {
                map[act[i].y][act[i].x] = true;
            }

            // After moving down: Remove the lines, refresh, calculate score
            int removedLineNo = this.removedLineNo(this.gameDataManager.getGameMap()); // Remove filled lines
            gameDataManager.setCurrentScore(gameDataManager.getCurrentScore() + removedLineNo);// Add score

            this.gameDataManager.getActPent().setActPoints(this.gameDataManager.getNext()); // Set next pentomino to be current one
            if (this.pentIndex >= 11) {
                this.pentIndex = 1;
            }
            this.gameDataManager.setNext(++pentIndex); // Set next pentomino's index

            // Check if the game is over
            if (isGameover()) {
                afterGameover();
            }
        }
        return false;
    }

    /**
     * Set the start state to be true
     */
    public void startGame() {
        gameDataManager.setStart(true);
    }

    /**
     * When game is over, set the start state to be false
     */
    public void afterGameover() {
        this.gameDataManager.setStart(false);
    }
}
