package main;

import entity.Entity;
import entity.Player;
import lombok.Getter;
import lombok.Setter;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // TODO: maybe add achievements (finish one level before the theme song finishes playing once??)
    // SCREEN SETTINGS
    public static final int ORIGINAL_TILE_SIZE = 16; // 16x16 tile
    public static final int SCALE = 3;
    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 12;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    public static final int FPS = 60;
    // WORLD SETTINGS
    public static final int MAX_WORLD_COL = 50;
    public static final int MAX_WORLD_ROW = 50;
    // GAME SYSTEM
    private final KeyHandler keyHandler = new KeyHandler(this);
    private final TileManager tileManager = new TileManager(this);
    private final AssetHandler assetHandler = new AssetHandler(this);
    @Getter
    private final CollisionHandler collisionHandler = new CollisionHandler(tileManager, assetHandler);
    private final SoundHandler musicHandler = new SoundHandler();
    private final SoundHandler soundEffectHandler = new SoundHandler();
    @Getter
    private final UI ui = new UI(this);
    @Getter
    @Setter
    private Thread gameThread;
    // ENTITIES
    @Getter
    private final Player player = new Player(this, keyHandler, assetHandler);
    // GAME STATE
    @Getter
    @Setter
    private GameState gameState;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        setupGame();
    }

    public void setupGame() {
        gameState = GameState.TITLE_SCREEN;
        assetHandler.setObjects();
        assetHandler.setNPC();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == GameState.PLAY) {
            player.update();
            assetHandler.getNpcs().forEach(Entity::update);
        } else if (gameState == GameState.PAUSE) {
            // do nothing
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        if (gameState == GameState.TITLE_SCREEN) {
            ui.draw(g2d);
        } else {
            // TILE
            tileManager.draw(g2d);  // Background should be drawn before anything else
            // OBJECT
            assetHandler.drawObjects(g2d);
            // NPCs
            assetHandler.drawNPCs(g2d);
            // PLAYER
            player.draw(g2d);
            // UI
            ui.draw(g2d); // UI should be rendered over everything (last)
        }

        g2d.dispose();
    }

    public boolean isWhitinScreenBoundaries(int worldX, int worldY) {
        return worldX + GamePanel.TILE_SIZE > player.getWorldX() - player.getScreenX() &&
                worldX - GamePanel.TILE_SIZE < player.getWorldX() + player.getScreenX() &&
                worldY + GamePanel.TILE_SIZE > player.getWorldY() - player.getScreenY() &&
                worldY - GamePanel.TILE_SIZE < player.getWorldY() + player.getScreenY();
    }

    public void playMusic(int index) {
        musicHandler.setFile(index);
        musicHandler.play();
        musicHandler.loop();
    }

    public void stopMusic() {
        musicHandler.stop();
    }

    public void playSoundEffect(int index) {
        soundEffectHandler.setFile(index);
        soundEffectHandler.play();
    }
}
