package main.ui;

import entity.Entity;
import entity.object.HeartObj;
import lombok.Getter;
import lombok.Setter;
import main.Drawable;
import main.GamePanel;
import main.GameState;
import sound.SoundManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UIManager implements Drawable {
    private final GamePanel gamePanel;
    private final SoundManager soundManager;
    private final List<String> onScreenMessages = new ArrayList<>();
    private final List<Integer> messageCounters = new ArrayList<>();
    private final int MESSAGE_DISPLAY_TIME = GamePanel.FPS * 3;
    private Graphics2D g2d;
    private Font PRUISA_B;
    @Getter
    @Setter
    private String currentDialogue = "";
    @Getter
    @Setter
    private boolean gameFinished = false;
    @Getter
    private MenuCommand currentMenuCommand = MenuCommand.NEW_GAME;
    @Getter
    private BufferedImage heartFull, heartHalf, heartEmpty;
    private final int MAX_SLOT_COL = 4;
    private final int MAX_SLOT_ROWS = 3;
    private int slotCol = 0;
    private int slotRow = 0;

    public enum MenuCommand {
        NEW_GAME, LOAD_GAME, QUIT, NONE
    }

    public UIManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.soundManager = gamePanel.getSoundManager();
        loadFont();
        createHUDObject();
    }

    @Override
    public void draw(final Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setFont(PRUISA_B);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);

        switch (gamePanel.getGameState()) {
            case TITLE_SCREEN -> drawTitleScreen();
            case PLAY -> {
                drawPlayerLife();
                drawOnScreenMessages();
                if (gamePanel.isDebugging()) {
                    showDebugInfo();
                }
            }
            case PAUSE -> {
                drawPauseScreen();
                drawPlayerLife();
            }
            case DIALOGUE -> {
                drawDialogueScreen();
                drawPlayerLife();
            }
            case CHARACTER_SCREEN -> {
                drawCharacterScreen();
                drawInventory();
            }
        }
    }

    public void changeMenuCommand(final int direction) {
        int index = currentMenuCommand.ordinal();
        int size = MenuCommand.values().length;
        if (index + direction < 0) {
            index = size - 1;
        }
        index = (index + direction) % (size - 1);
        currentMenuCommand = MenuCommand.values()[index];
    }

    public void executeMenuCommand() {
        switch (currentMenuCommand) {
            case NEW_GAME -> {
                gamePanel.setGameState(GameState.PLAY);
                soundManager.playMusic(SoundManager.THEME_SONG);
            }
            case LOAD_GAME -> {
                // todo add game loading
            }
            case QUIT -> System.exit(0);
        }
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

    public void addOnScreenMessage(String text) {
        onScreenMessages.add(text);
        messageCounters.add(0);
    }

    public void increaseSlotColumn() {
        if (slotCol < MAX_SLOT_COL) {
            slotCol++;
        }
    }

    public void decreaseSlotColumn() {
        if (slotCol > 0) {
            slotCol--;
        }
    }

    public void increaseSlotRow() {
        if (slotRow < MAX_SLOT_ROWS) {
            slotRow++;
        }
    }

    public void decreaseSlotRow() {
        if (slotRow > 0) {
            slotRow--;
        }
    }

    private void drawInventory() {
        // WINDOW
        final int frameX = GamePanel.TILE_SIZE * 9;
        final int frameY = GamePanel.TILE_SIZE;
        final int frameWidth = GamePanel.TILE_SIZE * 6;
        final int frameHeight = GamePanel.TILE_SIZE * 5;
        drawWindow(frameX, frameY, frameWidth, frameHeight);
        // SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;

        // CURSOR
        int cursorX = slotXStart + (GamePanel.TILE_SIZE * slotCol);
        int cursorY = slotYStart + (GamePanel.TILE_SIZE * slotRow);
        int cursorWidth = GamePanel.TILE_SIZE;
        int cursorHeight = GamePanel.TILE_SIZE;

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
    }

    private void showDebugInfo() {
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.setColor(Color.WHITE);
        int x = 10;
        int y = 400;
        int lineHeight = 20;

        g2d.drawString("WorldX: " + gamePanel.getPlayer().getWorldX(), x, y);
        y += lineHeight;
        g2d.drawString("WorldY: " + gamePanel.getPlayer().getWorldY(), x, y);
        y += lineHeight;
        g2d.drawString("Col: " + (gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getCollisionBox().x) / GamePanel.TILE_SIZE, x, y);
        y += lineHeight;
        g2d.drawString("Row: " + (gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getCollisionBox().y) / GamePanel.TILE_SIZE, x, y);
        y += lineHeight;
    }

    private void drawOnScreenMessages() {
        int messageX = GamePanel.TILE_SIZE / 2;
        int messageY = GamePanel.TILE_SIZE * 8;
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 20F));

        for (int i = 0; i < onScreenMessages.size(); i++) {
            if (onScreenMessages.get(i) != null) {
                g2d.setColor(Color.BLACK);
                g2d.drawString(onScreenMessages.get(i), messageX + 2, messageY + 2);
                g2d.setColor(Color.WHITE);
                g2d.drawString(onScreenMessages.get(i), messageX, messageY);
                messageY += 25;

                int counter = messageCounters.get(i) + 1;
                messageCounters.set(i, counter);

                if (messageCounters.get(i) > MESSAGE_DISPLAY_TIME) {
                    onScreenMessages.remove(i);
                    messageCounters.remove(i);
                }
            }
        }
    }

    private void loadFont() {
        try (InputStream inputStream = getClass().getResourceAsStream("/fonts/Purisa Bold.ttf")) {
            PRUISA_B = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException e) {
            System.err.println("An error occurred while loading font!");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("An error occurred reading the font file!");
            System.err.println(e.getMessage());
        }
    }

    private void createHUDObject() {
        HeartObj heart = new HeartObj(gamePanel);
        heartFull = heart.getFull();
        heartHalf = heart.getHalf();
        heartEmpty = heart.getEmpty();
    }

    private int getCenteredTextXCoordinate(final String text) {
        int textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return GamePanel.SCREEN_WIDTH / 2 - textLength / 2;
    }

    private void drawCharacterScreen() {
        // DRAW FRAME
        final int frameX = GamePanel.TILE_SIZE;
        final int frameY = GamePanel.TILE_SIZE;
        final int frameWidth = GamePanel.TILE_SIZE * 6;
        final int frameHeight = GamePanel.TILE_SIZE * 10;
        drawWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(24F));
        int textX = frameX + 20;
        int textY = frameY + GamePanel.TILE_SIZE;
        final int lineHeight = 32;

        // NAMES
        g2d.drawString("Level", textX, textY);
        textY += lineHeight;
        g2d.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2d.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2d.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2d.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2d.drawString("Experience", textX, textY);
        textY += lineHeight;
        g2d.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2d.drawString("Coins", textX, textY);
        textY += lineHeight;
        textY += GamePanel.TILE_SIZE / 2;
        g2d.drawString("Weapon", textX, textY);
        textY += lineHeight;
        textY += GamePanel.TILE_SIZE / 2;
        g2d.drawString("Shield", textX, textY);

        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + GamePanel.TILE_SIZE;
        String value;
        Entity player = gamePanel.getPlayer();

        value = String.valueOf(player.getLevel());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getStrength());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getDexterity());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getAttack());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getDefense());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getExperience());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getNextLevelExperience());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(player.getCoins());
        textX = getRightAlignmentXCoordinate(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += GamePanel.TILE_SIZE / 2;

        g2d.drawImage(player.getCurrentWeapon().getSpriteImage(), tailX - GamePanel.TILE_SIZE, textY, null);
        textY += GamePanel.TILE_SIZE / 2;
        textY += lineHeight;
        g2d.drawImage(player.getCurrentShield().getSpriteImage(), tailX - GamePanel.TILE_SIZE, textY, null);
    }

    private int getRightAlignmentXCoordinate(String text, int rightX) {
        int length = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return rightX - length;
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
        if (currentMenuCommand == MenuCommand.NEW_GAME) {
            g2d.drawString(">", x - GamePanel.TILE_SIZE, y);
        }

        text = "LOAD GAME";
        x = getCenteredTextXCoordinate(text);
        y += GamePanel.TILE_SIZE;
        g2d.drawString(text, x, y);
        if (currentMenuCommand == MenuCommand.LOAD_GAME) {
            g2d.drawString(">", x - GamePanel.TILE_SIZE, y);

        }

        text = "QUIT GAME";
        x = getCenteredTextXCoordinate(text);
        y += GamePanel.TILE_SIZE;
        g2d.drawString(text, x, y);
        if (currentMenuCommand == MenuCommand.QUIT) {
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

    private void drawWindow(final int x, final int y, final int width, final int height) {
        Color windowBackgroundColor = new Color(0, 0, 0, 200);
        g2d.setColor(windowBackgroundColor);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        Color windowBorderColor = new Color(255, 255, 255);
        g2d.setColor(windowBorderColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }

}
