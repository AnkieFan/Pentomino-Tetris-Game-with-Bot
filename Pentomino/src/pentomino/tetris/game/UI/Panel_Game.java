package UI;

import controllers.GameController;
import controllers.PlayerControl;
import game.GameDataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel_Game extends JPanel {
    private Layer[] layers;
    private JButton btnPause;
    private GameController gameController;
    private GameDataManager gameDataManager;

    public Panel_Game(GameController gameController, GameDataManager gameDataManager) {
        this.gameController = gameController;
        this.gameDataManager = gameDataManager;
        this.addKeyListener(new PlayerControl(gameController));
        this.setLayout(null);
        layers = new Layer[]{
                new Layer_BG(0, 0, 1060, 795), // Initialize background
                new Layer_Game(430, 97, 200, 600), // Initialize game board
                new Layer_Score(25, 97, 320, 240), // Initialize score board
                new Layer_Rank(25, 362, 320, 335), // Initialize rank board
                new Layer_Next(715, 97, 320, 320), // Initialize next pentomino board
        };
        initButton();
    }

    /**
     * Initialize the pause button
     */
    private void initButton() {
        this.btnPause = new JButton();
        btnPause.setIcon(getButton(gameDataManager.isPause()));
        btnPause.setBorderPainted(false);
        btnPause.setContentAreaFilled(false);
        btnPause.setBounds(745, 437, 260, 260);
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameDataManager.switchPause(); // Switch pause statement
                btnPause.setIcon(getButton(gameDataManager.isPause()));// Switch the icon of pause/resume
            }
        });
        this.add(btnPause);
    }

    /**
     * Switch the icon of pause/restart
     * @param b current pause statement
     * @return current icon
     */
    public ImageIcon getButton(boolean b) {
        ImageIcon pauseIm = new ImageIcon("Graphics/Buttons/Pause.png");
        ImageIcon continueIm = new ImageIcon("Graphics/Buttons/Restart.png");
        return b ? continueIm : pauseIm;
    }


    /**
     * Draw all the parts (layers) of panel
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        // Call basic search
        super.paintComponent(g);

        // Refresh game interface
        for (int i = 0; i < layers.length; i++) {
            layers[i].setGameDataManager(this.gameDataManager);
            layers[i].createWindow(g);
            //draw the windows
            layers[i].paint(g);
        }
        this.requestFocus();
    }

    public void setStart() {
        gameController.start();
    }
    public void setBot() {
        gameController.startBot();
    }
}
