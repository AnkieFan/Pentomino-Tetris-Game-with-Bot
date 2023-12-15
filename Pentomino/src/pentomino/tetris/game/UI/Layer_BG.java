package UI;

import javax.swing.*;
import java.awt.*;

/**
 * Back ground part of whole panel.
 */
public class Layer_BG extends Layer {
    protected Layer_BG(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /**
     * Draw the background image
     * @param g Graphics
     */
    @Override
    protected void createWindow(Graphics g) {
        Image bg = new ImageIcon("Pentomino/Graphics/Windows/bg.png").getImage();
        g.drawImage(bg, x, y, w, h, null);
    }

    /**
     * Draw other components in background
     * @param g Graphics
     */
    @Override
    protected void paint(Graphics g) {
        Image catLine = new ImageIcon("Pentomino/Graphics/Windows/CatLine.png").getImage();
        g.drawImage(catLine, 0, 715, 1060, 795, 0, 0, 2650, 200, null);
        g.drawImage(catLine, 0, 80, 1060, 0, 0, 0, 2650, 200, null);

        Image catColumn = new ImageIcon("Pentomino/Graphics/Windows/CatColumn.png").getImage();
        g.drawImage(catColumn, 370, 97, 430, 697, 0, 0, 150, 1500, null);
        g.drawImage(catColumn, 630, 697, 690, 97, 0, 0, 150, 1500, null);
    }
}
