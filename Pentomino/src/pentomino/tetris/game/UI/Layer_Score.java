package UI;

import javax.swing.*;
import java.awt.*;
/**
 * Score board part in panel.
 */
public class Layer_Score extends Layer {
    protected Image scoreIm = new ImageIcon("Graphics/Strings/Score.png").getImage();


    public Layer_Score(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /**
     * Draw the background of the score board
     * @param g Graphics
     */
    @Override
    protected void createWindow(Graphics g) {
        Image score = new ImageIcon("Graphics/Windows/Score.png").getImage();
        g.drawImage(score, x, y, w, h, null);
    }

    /**
     * Draw the current score on the score board
     * @param g Graphics
     */
    public void paint(Graphics g) {
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 64));
        g.setColor(new Color(250, 221, 165));
        // Draw String: Left-bottom coordinates
        g.drawString(this.gameDataManager.getCurrentScore() + "", this.x + 115, this.y + 170);
    }
}
