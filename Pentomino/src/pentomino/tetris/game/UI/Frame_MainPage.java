package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The menu page.
 */
public class Frame_MainPage extends JFrame implements ActionListener {
    JButton btnStart, btnBot;
    Frame_Game frameGame;
    JLabel labelBG;

    public Frame_MainPage(Frame_Game frameGame) {
        this.setTitle("Pentomeow");
        this.setSize(596, 833);

        this.setResizable(false);
        this.setLayout(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("Graphics/logo1.png").getImage());

        // Make the window in the middle
        this.setLocationRelativeTo(null);

        this.initBG();
        this.initButtons();
        this.frameGame = frameGame;
        this.setVisible(true);
    }

    /**
     * Initialize the background picture
     */
    public void initBG() {
        ImageIcon mainBG = new ImageIcon("Graphics/Windows/mainBG.jpg");
        labelBG = new JLabel(mainBG);
        labelBG.setBounds(0, 0, mainBG.getIconWidth(), mainBG.getIconHeight());
        this.getLayeredPane().add(labelBG, new Integer(Integer.MIN_VALUE));
        JPanel jp = (JPanel) this.getContentPane();
        jp.setOpaque(false);
    }

    /**
     * Initialize the buttons.
     */
    private void initButtons() {
        //create start button
        ImageIcon start = new ImageIcon("Graphics/Buttons/Start.png");
        ImageIcon start2 = new ImageIcon("Graphics/Buttons/Start2.png");
        btnStart = new JButton();
        btnStart.setIcon(start);
        btnStart.setRolloverIcon(start2);
        btnStart.setBorderPainted(false);
        btnStart.setContentAreaFilled(false);
        btnStart.setBounds(200, 200, 200, 200);
        btnStart.addActionListener(this);
        this.add(btnStart);

        // Create bot button
        ImageIcon bot = new ImageIcon("Graphics/Buttons/Bot.png");
        ImageIcon bot1 = new ImageIcon("Graphics/Buttons/Bot2.png");
        btnBot = new JButton();
        btnBot.setIcon(bot);
        btnBot.setRolloverIcon(bot1);
        btnBot.setBorderPainted(false);
        btnBot.setContentAreaFilled(false);
        btnBot.setBounds(200, 450, 200, 200);
        btnBot.addActionListener(this);
        this.add(btnBot);
    }

    /**
     * The method for action performed is for the button. so, when the button is pressed,
     * the game frame is set visible and it opens. Otherwise if the button for the bot is pressed,
     * the game for bot starts.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object mode = e.getSource();
        if (mode == btnStart) {
            frameGame.setVisible(true);
            this.setVisible(false);
            frameGame.setStart();
        } else if (mode == btnBot) {
            frameGame.setBot();
        }
    }

}
