package bot;

import controllers.GameController;
import game.PentominoDB;

/**
 * This class is for testing bot separately.
 */
public class BotTest {
    public static void main(String[] args) {
        // Change Thread sleep time before you run
        test(10000);
    }

    public static void test(int testNo){
        for (int i = 0; i < testNo; i++) {
            PentominoDB.shufflePent();
            GameController gameController = new GameController();
            gameController.startBot();
            while (!gameController.getBot().gameOver) ;
            if (i != (testNo-1))
                gameController.getFrameGame().setVisible(false);
        }
    }
}
