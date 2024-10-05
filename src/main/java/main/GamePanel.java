package main;

import entity.Player;
import lombok.Getter;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int originalTileSize = 16; // 16x16 tile
    public static final int scale = 3;
    // SCREEN SETTINGS
    public static int maxScreenCol = 16;
    public static int maxScreenRow = 12;
    public static int tileSize = originalTileSize * scale;
    public static int screenWidth = tileSize * maxScreenCol;
    public static int screenHeight = tileSize * maxScreenRow;
    public static int FPS = 60;
    // WORLD SETTINGS
    public static final int maxWorldCol = 50;
    public static final int maxWorldRow = 50;
    public static final int worldWidth = tileSize * maxWorldCol;
    public static final int worldHeight = tileSize * maxWorldRow;

    private final KeyHandler keyHandler = new KeyHandler(this);
    private final TileManager tileManager = new TileManager(this);
    @Getter
    private final Player player = new Player(this, keyHandler);
    private Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
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
        int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
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
        tileManager.draw(g2d);  // Background should be drawn before anything else

        player.draw(g2d);

        g2d.dispose();
    }

    public void zoomInOut(int zoomValue) {
        int oldWorldWidth = tileSize * maxWorldCol;
        tileSize += zoomValue;
        int newWorldWidth = tileSize * maxWorldCol;
        // Scale the values
        double multiplier = (double) newWorldWidth / oldWorldWidth;
        double newPlayerWorldX = player.getWorldX() * multiplier;
        double newPlayerWorldY = player.getWorldY() * multiplier;
        // Update player position and speed
        player.setSpeed(newWorldWidth / 600);
        player.setWorldX(newPlayerWorldX);
        player.setWorldY(newPlayerWorldY);

        System.out.println("tileSize: " + tileSize);
        System.out.println("worldWidth: " + newWorldWidth);
        System.out.println("player worldX: " + player.getWorldX());

    }

}
