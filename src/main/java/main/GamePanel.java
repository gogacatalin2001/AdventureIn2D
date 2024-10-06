package main;

import entity.Player;
import lombok.Getter;
import lombok.Setter;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
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

    private final KeyHandler keyHandler = new KeyHandler();
    private final TileManager tileManager = new TileManager(this);
    private final CollisionHandler collisionHandler = new CollisionHandler(this, tileManager);
    @Getter
    private final Player player = new Player(keyHandler, collisionHandler);
    private Thread gameThread;
    private AssetHandler assetHandler = new AssetHandler(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetHandler.setObjects();
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
        player.update();
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

        g2d.dispose();
    }
}
