package bot;

import java.awt.*;

/**
 * This is for recording the best position.
 * It will record the rotate number and the position of first point in a pentomino.
 */
public class Position {
    private Point firstPoint;
    private int rotateNo;

    public Position(Point firstPoint, int rotateNo) {
        this.firstPoint = firstPoint;
        this.rotateNo = rotateNo;
    }

    public int getRotateNo() {
        return rotateNo;
    }

    public Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    public void setRotateNo(int rotateNo) {
        this.rotateNo = rotateNo;
    }
}
