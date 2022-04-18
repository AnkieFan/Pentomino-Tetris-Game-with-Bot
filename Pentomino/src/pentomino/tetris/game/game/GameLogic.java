package game;

import UI.Frame_SaveScore;

/**
 * The pure logic part of player mode.
 * Manage left, right, up, down and space command from player
 */
public class GameLogic extends Logic {
    private Frame_SaveScore frame_saveScore;

    public GameLogic(GameDataManager gameDataManager) {
        super(gameDataManager);
    }

    /**
     * Try to move the pentomino one grid to the Right
     * If the game is not started or is paused, the command will not be executed
     */
    public void keyRight() {
        if (!gameDataManager.isStart() || gameDataManager.isPause())
            return;
        synchronized (this.gameDataManager) { // Thread security
            this.gameDataManager.getActPent().move(1, 0, this.gameDataManager.getGameMap());
        }
    }

    /**
     * Try to move the pentomino one grid to the Left
     * If the game is not started or is paused, the command will not be executed
     */
    public void keyLeft() {
        if (!gameDataManager.isStart() || gameDataManager.isPause())
            return;
        synchronized (this.gameDataManager) {
            this.gameDataManager.getActPent().move(-1, 0, this.gameDataManager.getGameMap());
        }
    }

    /**
     * Try to rotate the pentomino one grid to the Right
     * If the game is not started or is paused, the command will not be executed
     */
    public void keyUp() {
        if (!gameDataManager.isStart() || gameDataManager.isPause())
            return;
        synchronized (this.gameDataManager) {
            this.gameDataManager.getActPent().rotate(this.gameDataManager.getGameMap());
        }
    }

    /**
     * Try to move the pentomino directly to the bottom
     * If the game is not started or is paused, the command will not be executed
     */
    public void keySpace() {
        if (!gameDataManager.isStart() || gameDataManager.isPause())
            return;

        while (this.keyDown()) ;
    }

    /**
     * After the game is over, the page to enter your name is displayed.
     * It will get the player's name and pass it to the data processor.
     */
    @Override
    public void afterGameover() {
        super.afterGameover();
        frame_saveScore.showSave(gameDataManager.getCurrentScore());
    }

    public void setFrame_saveScore(Frame_SaveScore frame_saveScore) {
        this.frame_saveScore = frame_saveScore;
    }
}
