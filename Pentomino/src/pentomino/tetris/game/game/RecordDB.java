package game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Input and output the human players' records from local file
 */
public class RecordDB {

    /**
     * Input records from local file
     * @return The list of record
     */
    public List<Record> loadData() {
        List<Record> records = new ArrayList<Record>();
        Scanner in;
        try {
            in = new Scanner(new FileInputStream("records.csv"));
            while (in.hasNextLine()) {
                String[] str = in.nextLine().split("\\s+");
                records.add(new Record(str[0], Integer.parseInt(str[1])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }


    /**
     * Output the records from local file
     * @param records new records
     */
    public void saveData(List<Record> records) {
        try (PrintWriter pw = new PrintWriter("records.csv")) {
            for (Record record : records) {
                if ("No Data".equals(record.getName()))
                    continue;
                pw.println(record.getName() + " " + record.getScore());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fail to save the records data");
        }
    }


}
