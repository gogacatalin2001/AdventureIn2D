package main;

import entity.Player;
import lombok.Getter;
import lombok.Setter;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // TODO: maybe add achievements (finish one level before the theme song finishes playing once??)
    // SCREEN SETTINGS
    public static final int originalTileSize = 16; // 16x16 tile
    public static final int scale = 3;
    public static final int maxScreenCol = 16;
    public static final int maxScreenRow = 12;
    public static final int tileSize = originalTileSize * scale;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;
    public static final int FPS = 60;
    // WORLD SETTINGS
    public static final int maxWorldCol = 50;
    public static final int maxWorldRow = 50;
    public static final int worldWidth = tileSize * maxWorldCol;
    public static final int worldHeight = tileSize * maxWorldRow;
    // GAME SYSTEM
    private final KeyHandler keyHandler = new KeyHandler(this);
    private final TileManager tileManager = new TileManager(this);
    private final AssetHandler assetHandler = new AssetHandler(this);
    private final CollisionHandler collisionHandler = new CollisionHandler(this, tileManager, assetHandler);
    private final SoundHandler musicHandler = new SoundHandler();
    private final SoundHandler soundEffectHandler = new SoundHandler();
    @Getter
    private final UI ui = new UI(this);
    @Getter
    @Setter
    private Thread gameThread;
    // ENTITIES
    @Getter
    private final Player player = new Player(this, keyHandler, collisionHandler, assetHandler);
    // GAME STATE
    @Getter
    @Setter
    private GameState gameState;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetHandler.setObjects();
        playMusic(0);
        gameState = GameState.PLAY;
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
        } else if (gameState == GameState.PAUSE) {
            // do nothing
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // TILE
        tileManager.draw(g2d);  // Background should be drawn before anything else
        // OBJECT
        assetHandler.drawObjects(g2d);
        // PLAYER
        player.draw(g2d);
        // UI
        ui.draw(g2d); // UI should be rendered over everything (last)

        g2d.dispose();
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
