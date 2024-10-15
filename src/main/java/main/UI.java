package main;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    private final GamePanel gamePanel;
    private Graphics2D g2d;
    private Font PRUISA_B;
    private boolean displayMessage = false;
    private String message = "";
    private int messageDisplayCounter = 0;
    @Getter
    @Setter
    private String currentDialogue = "";
    @Getter
    @Setter
    private boolean gameFinished = false;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        loadFont();
    }

    private void loadFont() {
        try (InputStream inputStream = getClass().getResourceAsStream("/fonts/Purisa Bold.ttf")) {
            PRUISA_B = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showMessage(String text) {
        message = text;
        displayMessage = true;
    }

    public int getCenteredTextXCoordinate(String text) {
        int textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return GamePanel.SCREEN_WIDTH / 2 - textLength / 2;
    }

    public void draw(Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setFont(PRUISA_B);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        switch (gamePanel.getGameState()) {
            case PLAY -> {}
            case PAUSE -> drawPauseScreen();
            case DIALOG -> drawDialogueScreen();
        }
    }

    private void drawPauseScreen() {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 30F));
        String text = "PAUSED";
        int x, y;

        x = getCenteredTextXCoordinate(text);
        y = GamePanel.SCREEN_HEIGHT / 2;
        g2d.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        // WINDOW
        int x = GamePanel.TILE_SIZE * 2;
        int y = GamePanel.TILE_SIZE / 2;
        int width = GamePanel.SCREEN_WIDTH - (GamePanel.TILE_SIZE * 4);
        int height = GamePanel.TILE_SIZE * 4;

        drawWindow(x, y, width, height);

        x += GamePanel.TILE_SIZE;
        y += GamePanel.TILE_SIZE;
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 20));

        for (String line : currentDialogue.split("\n")) {
            g2d.drawString(line, x, y);
            y += 40;
        }
    }

    private void drawWindow(int x, int y, int width, int height) {
        Color windowBackgroundColor = new Color(0, 0, 0, 200);
        g2d.setColor(windowBackgroundColor);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        Color windowBorderColor = new Color(255, 255, 255);
        g2d.setColor(windowBorderColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }


}
