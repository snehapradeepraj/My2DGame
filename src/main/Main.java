package main;

import javax.swing.JFrame;

public class Main {

    static void main(String[] args) { // underscore used to ignore unused parameter warning

        // Create the game panel
        GamePanel gp = new GamePanel();


        // Create the JFrame
        JFrame window = new JFrame("Tile Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(gp);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Start the game loop
        gp.setUpGame();
        gp.startGameThread();

    }
}
