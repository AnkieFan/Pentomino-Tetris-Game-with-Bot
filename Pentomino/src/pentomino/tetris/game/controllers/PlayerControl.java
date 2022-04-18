package controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * Get instruction from player and pass it to game logic to execute.
 */

public class PlayerControl extends KeyAdapter {
    private GameController gameController;

    public PlayerControl(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Left/right: move
     * Up: rotate
     * Down: move 1 grid more
     * Space: Fall to the bottom directly
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO
        switch(e.getKeyCode()){
            case KeyEvent.VK_RIGHT:
                this.gameController.keyRight();
                break;
            case KeyEvent.VK_LEFT:
                this.gameController.keyLeft();
                break;
            case KeyEvent.VK_UP:
                this.gameController.keyUp();
                break;
            case KeyEvent.VK_DOWN:
                this.gameController.keyDown();
                break;
            case KeyEvent.VK_SPACE:
                this.gameController.keySpace();
                break;
            default:
                break;
        }
    }
}
