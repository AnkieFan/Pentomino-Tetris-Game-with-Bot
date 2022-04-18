package game;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Pentomino {
    public Point[] actPoints = null; // 'act' means the pentomino which is now on the board
    private int pentIndex;

    private static List<Point[]> PENT_LIST = PentominoDB.PENT_LIST; // Random shuffled

    public Pentomino() {
        this.setActPoints(0);
    }

    /**
     * Set the current pentomino to be the pentomino with pentIndex in PENT_LIST
     * @param pentIndex the index value of pentomino
     */
    public void setActPoints(int pentIndex) {
        if (pentIndex > 11) {
            Collections.shuffle(PENT_LIST);
            pentIndex = 0;
        }

        this.pentIndex = pentIndex;

        Point[] points = PENT_LIST.get(pentIndex);
        actPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            actPoints[i] = new Point(points[i].x, points[i].y);
        }

    }

    /**
     * @return current pentomino
     */
    public Point[] getActPoints() {
        return actPoints;
    }

    /**
     * @param index pentomino's index value
     * @returnthe points of pentominos
     */
    public Point[] getPoints(int index) {
        if (index > 11)
            return null;

        return PENT_LIST.get(index);
    }

    /**
     * Change pentomino's coordinate with moveX and moveY
     * @param moveX the variation of x coordinate
     * @param moveY the variation of y coordinate
     * @param gameMap the map which pentomino will move on
     * @return if the pentomino can be moved to this position
     */
    public boolean move(int moveX, int moveY, boolean[][] gameMap) {
        // Check if the pentomino can be moved to this position
        for (int i = 0; i < actPoints.length; i++) {
            if (isOverZone(actPoints[i].x + moveX, actPoints[i].y + moveY, gameMap))
                return false;
        }

        //Refresh the coordinates
        for (int i = 0; i < actPoints.length; i++) {
            actPoints[i].x += moveX;
            actPoints[i].y += moveY;
        }
        return true;
    }

    /**
     * Rotate 90 degree
     * Assume A: final point  O: rotate center point  B:original point
     * A.x = O.y + O.x - B.y
     * A.y = O.y - O.x + B.x
     */
    public void rotate(boolean[][] gameMap) {
        synchronized (this) {
            if (!rotateShift(gameMap))
                return;
            for (int i = 1; i < actPoints.length; i++) {
                int newX = actPoints[0].y + actPoints[0].x - actPoints[i].y;
                int newY = actPoints[0].y - actPoints[0].x + actPoints[i].x;
                if(isOverZone(newX,newY,gameMap))
                    return;
                actPoints[i].x = newX;
                actPoints[i].y = newY;
            }
        }
    }

    /**
     * Check if this coordinate is out of the boundary
     * @param x x coordinate
     * @param y y coordinate
     * @param gameMap the one which is checked
     * @return if this coordinate is out of the boundary
     */
    private boolean isOverZone(int x, int y, boolean[][] gameMap) {
        return (x < 0 || x > 4 || y < 0 || y > 14 || gameMap[y][x]);
    }

    /**
     * If the pentomino is rotated at the boundary, it will cross the boundary.
     * This method allows it to shift back to a position where it can rotate before rotating.
     * @param gameMap the one which pentomino is rotating on
     * @return if shift is done or not
     */
    private boolean rotateShift(boolean[][] gameMap) {
        int horizontalShift = 0;
        int verticalShift = 0;
        for (int i = 0; i < actPoints.length; i++) {
            int newX = actPoints[0].y + actPoints[0].x - actPoints[i].y;
            int newY = actPoints[0].y - actPoints[0].x + actPoints[i].x;

            if (newX < 0) {
                horizontalShift = Math.max(horizontalShift, 0 - newX);
            } else if (newY < 0) {
                verticalShift = Math.max(verticalShift, 0 - newY);
            } else if (newX > 4) {
                horizontalShift = Math.min(horizontalShift, 4 - newX);
            } else if (newY > 14 || gameMap[newY][newX]) {
                return false;
            }
        }
        return this.move(horizontalShift, verticalShift, gameMap);
    }

    public int getPentIndex() {
        return pentIndex;
    }

    /**
     * Clone the pentomino
     * This is used in bot. When the bot is looking for the best position, it will avoid changing the original pentomino.
     * @return the copied pentomino
     */
    @Override
    public Pentomino clone() {
        Pentomino p = new Pentomino();
        p.pentIndex = this.pentIndex;
        for (int i = 0; i < 5; i++) {
            p.actPoints[i] = new Point(this.actPoints[i].x, this.actPoints[i].y);
        }
        return p;
    }

}
