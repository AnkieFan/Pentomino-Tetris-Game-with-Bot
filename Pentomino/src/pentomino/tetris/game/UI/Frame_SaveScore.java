package UI;

import controllers.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * The small page to let player enter the name when game is over
 */
public class Frame_SaveScore extends JFrame {
    private JButton btnOK;
    private JLabel jlScore, errMsg;
    private JTextField textField;
    private GameController gameController;

    public Frame_SaveScore(GameController gameController) {
        this.gameController = gameController;
        this.setTitle("Save?");
        this.setSize(250, 180);

        this.setIconImage(new ImageIcon("Graphics/logo3.png").getImage());

        this.setLocationRelativeTo(null);

        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.createCom();
        this.createAction();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setVisible(false);
    }

    /**
     * Create actions to the components.
     */
    private void createAction() {
        this.btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField.getText();
                if (name.length() > 15) { // Avoid the name being too long
                    errMsg.setText("Your name is too long:c");
                } else if (name == null || "".equals(name)) { // Avoid null input
                    errMsg.setText("Please enter your name:3");
                } else {
                    gameController.saveScore(name); // Valid input
                    setVisible(false);
                    try {
                        Thread.sleep(700); // Wait for a while
                        System.exit(0); // End the program
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Create components in the frame.
     */
    private void createCom() {
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.jlScore = new JLabel();
        north.add(jlScore);
        this.add(north, BorderLayout.NORTH);
        // Create space of error message
        this.errMsg = new JLabel();
        this.errMsg.setForeground(Color.RED);
        north.add(errMsg);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.textField = new JTextField(10);
        center.add(new JLabel("Please enter your name: "));
        center.add(textField);
        this.add(center, BorderLayout.CENTER);


        // Create OK button
        this.btnOK = new JButton();
        this.btnOK.setBorderPainted(false);
        this.btnOK.setContentAreaFilled(false);
        this.btnOK.setIcon(new ImageIcon("Graphics/Buttons/ok.png"));
        // Create the south panel
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Add button in south panel
        south.add(btnOK);
        this.add(south, BorderLayout.SOUTH);
    }

    /**
     * Set the score to the panel
     * @param score the current score
     */
    public void showSave(int score) {
        this.jlScore.setText("Your score: " + score);
        this.setVisible(true);
    }

}
