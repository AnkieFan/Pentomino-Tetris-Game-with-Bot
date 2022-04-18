package UI;

import javax.swing.*;
import java.awt.*;

/**
 * The game page.
 */
public class Frame_Game extends JFrame{
    private Panel_Game panelGame;


    public Frame_Game(Panel_Game panelGame) {
        this.setLayout(null);
        //Set title
        this.setTitle("Katty is looking at you");
        //Set default shut down program
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set window size
        this.setSize(1060, 833); //4:3
        // Not allow user to change size
        this.setResizable(false);
        this.panelGame = panelGame;

        // Make the window in the middle
        this.setLocationRelativeTo(null);

        // Set frame icon
        this.setIconImage(new ImageIcon("Graphics/logo2.png").getImage());
        this.setContentPane(panelGame);
        //Set default visible
        this.setVisible(false);
    }

    public void setStart(){
        panelGame.setStart();
    }

    public void setBot(){
        panelGame.setBot();
    }

}
