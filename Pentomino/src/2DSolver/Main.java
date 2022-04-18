import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        DancingLink dancingLink = new DancingLink();
        long endTime = System.currentTimeMillis();
        System.out.println("Running time： " + ((endTime - startTime) / 1000) + "s and " + (endTime - startTime) % 1000 + " ms.");
    }
}
