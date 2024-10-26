package main;

import lombok.Getter;
import lombok.Setter;
import entity.object.HeartObj;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    private final GamePanel gamePanel;
    private Graphics2D g2d;
    private Font PRUISA_B;
    @Getter
    @Setter
    private String currentDialogue = "";
    @Getter
    @Setter
    private boolean gameFinished = false;
    @Getter
    private Command currentCommand = Command.NEW_GAME;
    @Getter
    private BufferedImage heartFull, heartHalf, heartEmpty;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        loadFont();
        createHUDObject();
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

    private void createHUDObject() {
        HeartObj heart = new HeartObj(gamePanel, gamePanel.getEntityHandler());
        heartFull = heart.getFull();
        heartHalf = heart.getHalf();
        heartEmpty = heart.getEmpty();
    }

    public int getCenteredTextXCoordinate(final String text) {
        int textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return GamePanel.SCREEN_WIDTH / 2 - textLength / 2;
    }

    public void draw(final Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setFont(PRUISA_B);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        switch (gamePanel.getGameState()) {
            case TITLE_SCREEN -> drawTitleScreen();
            case PLAY -> drawPlayerLife();
            case PAUSE -> drawPauseScreen();
            case DIALOGUE -> drawDialogueScreen();
        }
    }

    private void drawPlayerLife() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < gamePanel.getPlayer().getMaxLife() / 2; i++) {
            g2d.drawImage(heartEmpty, x, y, null);
            x += GamePanel.TILE_SIZE;
        }
        x = 0;
        for (int i = 0; i < gamePanel.getPlayer().getLife(); i++) {
            g2d.drawImage(heartHalf, x, y, null);
            i++;
            if (i < gamePanel.getPlayer().getLife()) {
                g2d.drawImage(heartFull, x, y, null);
            }
            x += GamePanel.TILE_SIZE;
        }

    }

    public void changeCommand(final int direction) {
        int index = currentCommand.ordinal();
        int size = Command.values().length;
        if (index + direction < 0) {
            index = size - 1;
        }
        index = (index + direction) % (size - 1);
        currentCommand = Command.values()[index];
    }

    private void drawTitleScreen() {
        // BACKGROUND
        g2d.setColor(new Color(50, 80, 70));
        g2d.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        // TITLE
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60F));
        String text = "Blue Boy Adventure";
        int x = getCenteredTextXCoordinate(text);
        int y = GamePanel.TILE_SIZE * 3;
        // Title shadow
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + 5, y + 5);
        // Title text
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
        // CHARACTER
        x = GamePanel.SCREEN_WIDTH / 2 - GamePanel.TILE_SIZE;
        y += GamePanel.TILE_SIZE * 2;
        g2d.drawImage(gamePanel.getPlayer().getImages().getFirst(), x, y, GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE * 2, null);
        // MENU
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 28F));

        text = "NEW GAME";
        x = getCenteredTextXCoordinate(text);
        y += GamePanel.TILE_SIZE * 4;
        g2d.drawString(text, x, y);
        if (currentCommand == Command.NEW_GAME) {
            g2d.drawString(">", x - GamePanel.TILE_SIZE, y);
        }

        text = "LOAD GAME";
        x = getCenteredTextXCoordinate(text);
        y += GamePanel.TILE_SIZE;
        g2d.drawString(text, x, y);
        if (currentCommand == Command.LOAD_GAME) {
            g2d.drawString(">", x - GamePanel.TILE_SIZE, y);

        }

        text = "QUIT GAME";
        x = getCenteredTextXCoordinate(text);
        y += GamePanel.TILE_SIZE;
        g2d.drawString(text, x, y);
        if (currentCommand == Command.QUIT) {
            g2d.drawString(">", x - GamePanel.TILE_SIZE, y);

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

    private void drawWindow(final int x, final int y, final int width, final int height) {
        Color windowBackgroundColor = new Color(0, 0, 0, 200);
        g2d.setColor(windowBackgroundColor);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        Color windowBorderColor = new Color(255, 255, 255);
        g2d.setColor(windowBorderColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }

    public enum Command {
        NEW_GAME, LOAD_GAME, QUIT, NONE
    }
}
