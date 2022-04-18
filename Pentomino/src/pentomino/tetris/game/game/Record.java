package game;

import java.io.Serializable;

/**
 * The record of human players
 */
public class Record implements Comparable<Record>, Serializable{
    private String name;
    private int score;

    public Record(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    /**
     * For sorting
     * @param record Record to be compared
     * @return difference between the record and this record
     */
    @Override
    public int compareTo(Record record) {
        return record.score - this.score;
    }
}
