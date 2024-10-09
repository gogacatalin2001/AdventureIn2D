package main;

import lombok.Getter;
import lombok.Setter;
import object.KeyObj;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    private final GamePanel gamePanel;
    private final Font ARIAL_40, ARIAL_80_B;
    private final BufferedImage keyImage;
    private boolean displayMessage = false;
    private String message = "";
    private int messageDisplayCounter = 0;
    private double gameTime = 0.0;
    @Getter
    @Setter
    private boolean gameFinished = false;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        ARIAL_40 = new Font("Aerial", Font.PLAIN, 40);
        ARIAL_80_B = new Font("Aerial", Font.BOLD, 80);
        keyImage = new KeyObj().getImage();
    }


    public void showMessage(String text) {
        message = text;
        displayMessage = true;
    }

    public void draw(Graphics2D g2d) {
        int x;
        int y;
        String text;
        int textLength;

        if (gameFinished) {
            g2d.setFont(ARIAL_80_B);
            g2d.setColor(Color.ORANGE);
            text = "YOU WON!";
            textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
            x = GamePanel.screenWidth / 2 - textLength / 2;
            y = GamePanel.screenHeight / 2 - GamePanel.tileSize * 4;
            g2d.drawString(text, x, y);

            g2d.setFont(ARIAL_40);
            g2d.setColor(Color.WHITE);
            text = "You found the treasure!";
            textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
            x = GamePanel.screenWidth / 2 - textLength / 2;
            y = GamePanel.screenHeight / 2 - GamePanel.tileSize * 3;
            g2d.drawString(text, x, y);

            g2d.setFont(ARIAL_40);
            g2d.setColor(Color.WHITE);
            text = String.format("Your Time was: %.2f!", gameTime);
            textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
            x = GamePanel.screenWidth / 2 - textLength / 2;
            y = GamePanel.screenHeight / 2 - GamePanel.tileSize * 2;
            g2d.drawString(text, x, y);

            gamePanel.setGameThread(null);

        } else {
            g2d.setFont(ARIAL_40);
            g2d.setColor(Color.WHITE);
            g2d.drawImage(keyImage, GamePanel.tileSize / 2, GamePanel.tileSize / 2, GamePanel.tileSize, GamePanel.tileSize, null);
            g2d.drawString("x " + gamePanel.getPlayer().getKeyCount(), 74, 65);

            // TIME
            gameTime += (double) 1 / 60;
            g2d.drawString(String.format("Time: %.2f", gameTime), GamePanel.screenWidth - GamePanel.tileSize * 5, 65);

            // MESSAGE
            if (displayMessage) {
                g2d.setFont(g2d.getFont().deriveFont(30F));
                g2d.drawString(message, GamePanel.screenWidth / 2 - GamePanel.tileSize * 2, GamePanel.tileSize * 2);
                messageDisplayCounter++;

                if (messageDisplayCounter > 120) {
                    displayMessage = false;
                    messageDisplayCounter = 0;
                }
            }
        }
    }
}
