package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameDataManager {
    private int currentScore;
    private List<Record> records;
    /**
     * In map, if this position is 'true', it means that this grid is occupied.
     * Please notice that the coordinates in 2D array is opposite with coordinates in UI!!!!!!!!!
     */
    private boolean[][] gameMap;
    /**
     * This means the pentomino which currently in the board
     */
    private Pentomino actPent;
    /**
     * The index value of next pentomino
     */
    private int next = 1;
    private boolean start, pause;

    public GameDataManager() {
        this.gameMap = new boolean[15][5]; // the coordinates in 2D array is opposite with coordinates in UI
    }

    public boolean[][] getGameMap() {
        return gameMap;
    }

    public Pentomino getActPent() {
        return actPent;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getNext() {
        return next;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setActPent(Pentomino pentomino) {
        this.actPent = pentomino;
    }

    public void setGameMap(boolean[][] gameMap) {
        this.gameMap = gameMap;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public void setRecords(List<Record> records) {
        // If no record: create a record list
        if (records == null)
            records = new ArrayList<Record>();

        // If the number of records is less than 5: fill with No Data
        while (records.size() < 5) {
            records.add(new Record("No Data", 0));
        }
        Collections.sort(records);
        this.records = records;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isPause() {
        return pause;
    }

    public void switchPause() {
        this.pause = !pause;
    }

    public void addRecord(String name) {
        records.add(new Record(name, getCurrentScore()));
    }
}
