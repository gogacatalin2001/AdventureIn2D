import main.GamePanel;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // GAME CONTAINER COMPONENT
        GamePanel gamePanel = new GamePanel();

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Adventure in 2D");
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}
