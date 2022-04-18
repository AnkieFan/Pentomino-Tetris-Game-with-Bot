package UI;

import javax.swing.*;
import java.awt.*;

/**
 * Game board part in panel.
 */
public class Layer_Game extends Layer {
    public Layer_Game(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public void paint(Graphics g) {
        this.createWindow(g);
        this.drawMainAct(g);
        this.drawMap(g);
    }

    /**
     * Draw the background of the game board
     * @param g Graphics
     */
    @Override
    protected void createWindow(Graphics g) {
        Image board = new ImageIcon("Graphics/Windows/Board.jpg").getImage();
        g.drawImage(board, x, y, w, h, null);
    }

    /**
     * Draw the current pentomino on the game board
     * @param g Graphics
     */
    private void drawMainAct(Graphics g) {
        if (!gameDataManager.isStart())
            return;
        // Print grids
        String docStr = "Graphics/Game/block" + gameDataManager.getActPent().getPentIndex() + ".png";
        Image blocki = new ImageIcon(docStr).getImage();
        Point[] points = this.gameDataManager.getActPent().getActPoints();
        for (int i = 0; i < points.length; i++) {
            g.drawImage(blocki, this.x + points[i].x * 40, this.y + points[i].y * 40, this.x + points[i].x * 40 + 40, this.y + points[i].y * 40 + 40, 0, 0, 40, 40, null);
        }

    }

    /**
     * Draw the fixed pentomino on the game board
     * @param g Graphics
     */
    private void drawMap(Graphics g) {
        // Print map
        Image block = new ImageIcon("Graphics/Game/block-1.png").getImage();
        boolean[][] map = this.gameDataManager.getGameMap();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x])
                    g.drawImage(block, this.x + x * 40, this.y + y * 40, this.x + x * 40 + 40, this.y + y * 40 + 40, 0, 0, 40, 40, null);
            }
        }
    }


}
