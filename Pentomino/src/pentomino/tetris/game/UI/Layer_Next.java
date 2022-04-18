package UI;

import javax.swing.*;
import java.awt.*;

/**
 * Next pentomino board part in panel.
 */
public class Layer_Next extends Layer {

    public Layer_Next(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /**
     * Draw the background of the next pentomino board
     * @param g Graphics
     */
    @Override
    protected void createWindow(Graphics g) {
        Image next = new ImageIcon("Graphics/Windows/Next.png").getImage();
        g.drawImage(next, x, y, w, h, null);
    }

    /**
     * Draw the next pentomino on next pentomino board
     * @param g Graphics
     */
    public void paint(Graphics g) {
        if (this.gameDataManager.isStart()) {
            String docStr = "Graphics/Game/block" + gameDataManager.getNext() + ".png";
            Image blocki = new ImageIcon(docStr).getImage();

            // Print grids
            Point[] points = gameDataManager.getActPent().getPoints(gameDataManager.getNext());
            if (points == null)
                return;

            for (int i = 0; i < points.length; i++) {
                g.drawImage(blocki, this.x + 45 + points[i].x * 40, this.y + 120 + points[i].y * 40, this.x + 45 + points[i].x * 40 + 40, this.y + 120 + points[i].y * 40 + 40, 0, 0, 40, 40, null);
            }
        }
    }
}
