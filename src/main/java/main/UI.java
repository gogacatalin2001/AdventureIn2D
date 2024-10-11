package main;

import lombok.Getter;
import lombok.Setter;
import object.KeyObj;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    private final GamePanel gamePanel;
    private Graphics2D g2d;
    private final Font ARIAL_40, ARIAL_80_B;
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
    }

    public void showMessage(String text) {
        message = text;
        displayMessage = true;
    }

    public void draw(Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setFont(ARIAL_40);
        g2d.setColor(Color.WHITE);

        // Handle game state
        if (gamePanel.getGameState() == GameState.PLAY) {
            // play state stuff
        } else {
            drawPauseScreen();
        }
    }

    private void drawPauseScreen() {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 80));
        String text = "PAUSED";
        int x, y;

        x = getCenteredTextXCoordinate(text);
        y = GamePanel.screenHeight / 2;
        g2d.drawString(text, x, y);
    }

    public int getCenteredTextXCoordinate(String text) {
        int textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return GamePanel.screenWidth / 2 - textLength / 2;
    }
}
