package UI;

import game.Record;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * Rank board part in panel.
 */
public class Layer_Rank extends Layer {
    public Layer_Rank(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /**
     * Draw the background of the rank board
     * @param g Graphics
     */
    @Override
    protected void createWindow(Graphics g) {
        Image rank = new ImageIcon("Pentomino/Graphics/Windows/Rank.png").getImage();
        g.drawImage(rank,x,y,w,h,null);
    }

    /**
     * Draw the top 5 records on rank board
     * @param g Graphics
     */
    public void paint(Graphics g) {
        List<Record> records = this.gameDataManager.getRecords();
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.setColor(new Color(90,43,32));
        for (int i = 0; i < 5; i++) {
            g.drawString(records.get(i).getName(), this.x + 100, this.y + 120 + 35 * i);
            g.drawString("" + records.get(i).getScore(), this.x + 245, this.y + 120 + 35 * i);
        }

    }
}
