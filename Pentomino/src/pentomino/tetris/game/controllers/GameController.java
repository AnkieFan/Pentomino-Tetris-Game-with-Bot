package controllers;

import UI.Frame_Game;
import UI.Frame_MainPage;
import UI.Frame_SaveScore;
import UI.Panel_Game;
import bot.Bot;
import bot.Position;
import game.GameDataManager;
import game.GameLogic;
import game.RecordDB;


/**
 * Receive action of player's keyboard
 * Control the interface
 * Control the logic of game
 */
public class GameController {
    private Frame_SaveScore fSaveScore;
    private Panel_Game panelGame;
    private GameLogic gameLogic;
    private GameDataManager gameDataManager;
    private Frame_Game frameGame;
    private Frame_MainPage mainPage;
    private Bot bot;

    private RecordDB data;

    private Thread gameThread = null;

    public GameController() {
        // Create game data manager
        this.gameDataManager = new GameDataManager();
        // Create game logic algorithm unit
        this.gameLogic = new GameLogic(gameDataManager);

        // Create game panel
        this.panelGame = new Panel_Game(this, gameDataManager);

        this.data = new RecordDB();
        this.gameDataManager.setRecords(data.loadData());

        this.fSaveScore = new Frame_SaveScore(this);
        gameLogic.setFrame_saveScore(fSaveScore);

        frameGame = new Frame_Game(panelGame);
        mainPage = new Frame_MainPage(frameGame);
    }

    /**
     * Receive instruction from player controller. Call the method in gameLogic to do logic calculate.
     * After calculation, let panel to repaint the UI.
     */
    public void keyRight() {
        this.gameLogic.keyRight();
        this.panelGame.repaint();
    }

    public void keyLeft() {
        this.gameLogic.keyLeft();
        this.panelGame.repaint();
    }

    public void keyUp() {
        this.gameLogic.keyUp();
        this.panelGame.repaint();
    }

    public void keyDown() {
        this.gameLogic.keyDown();
        this.panelGame.repaint();
    }

    public void keySpace() {
        this.gameLogic.keySpace();
        this.panelGame.repaint();
    }


    /**
     * If the game is started, the thread will control the pentomino fall down
     */
    public void start() {
        this.gameLogic.startGame();

        // Create a thread
        this.gameThread = new Thread() {
            @Override
            public void run() {
                while (gameDataManager.isStart()) {
                    try {
                        panelGame.repaint(); // Refresh panel
                        Thread.sleep(700);
                        if (gameDataManager.isPause()) {
                            continue;
                        }
                        gameLogic.keyDown();
                        panelGame.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.gameThread.start();
        this.panelGame.repaint();
    }

    /**
     * Start bot mode, activate bot logic.
     * If the bot mode has been activated, the thread will control pentomino to move.
     */
    public void startBot(){
        this.bot = new Bot(this.gameDataManager);
        this.frameGame.setVisible(true);
        this.mainPage.setVisible(false);
        this.bot.startGame();
        this.gameThread = new Thread() {
            @Override
            public void run() {
                while (gameDataManager.isStart()) {
                    try{
                        panelGame.repaint();
                        Thread.sleep(700);
                        if (gameDataManager.isPause()) {
                            continue;
                        }

                        Position bestPos = bot.findBestPosition(bot.gameDataManager.getActPent());//Find the best position
                        if(bestPos==null)
                            break;

                        for(int i = 0;i<bestPos.getRotateNo();i++){ // Rotate the pentomino according to best position
                            bot.gameDataManager.getActPent().rotate(bot.gameDataManager.getGameMap());
                        }

                        // Move to the best place
                        int newX = bestPos.getFirstPoint().x - bot.gameDataManager.getActPent().getActPoints()[0].x;
                        int newY = bestPos.getFirstPoint().y - bot.gameDataManager.getActPent().getActPoints()[0].y;
                        bot.gameDataManager.getActPent().move(newX,newY,bot.gameDataManager.getGameMap());

                        // Execute refresh actions in key down
                        bot.keyDown();
                        panelGame.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.gameThread.start();
        this.panelGame.repaint();
    }

    /**
     * To save score, we first ask player for name, and then we save score in the records with name.
     * @param name is the name of the player. User enter name after losing the game to save the particular score.
     */
    public void saveScore(String name) {
        gameDataManager.addRecord(name);
        data.saveData(gameDataManager.getRecords());
    }


    public GameDataManager getGameDataManager() {
        return gameDataManager;
    }

    public Bot getBot() {
        return bot;
    }

    public Frame_Game getFrameGame() {
        return frameGame;
    }
}
