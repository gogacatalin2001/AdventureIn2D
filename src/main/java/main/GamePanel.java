package main;

import entity.Entity;
import entity.EntityHandler;
import entity.Player;
import lombok.Getter;
import lombok.Setter;
import main.event.EventHandler;
import main.ui.UI;
import sound.SoundHandler;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Getter
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
    private final TileManager tileManager = new TileManager(this);
    private final EventHandler eventHandler = new EventHandler(this);
    private final EntityHandler entityHandler = new EntityHandler(this);
    private final SoundHandler soundHandler = new SoundHandler();
    private final KeyHandler keyHandler = new KeyHandler(this, soundHandler);
    // ENTITIES
    private final Player player = new Player(this, keyHandler, entityHandler);
    private final UI ui = new UI(this);
    @Setter
    private Thread gameThread;
    // GAME STATE
    @Setter
    private GameState gameState;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(keyHandler);
        this.setFocusable(true);
        setupGame();
    }

    public void setupGame() {
        gameState = GameState.TITLE_SCREEN;
        entityHandler.setObject();
        entityHandler.setNPC();
        entityHandler.setMonsters();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double NANOSECONDS_PER_SECOND = 1000000000.0;
        double drawInterval = NANOSECONDS_PER_SECOND / FPS;
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

            if (timer >= NANOSECONDS_PER_SECOND) {
                timer = 0;
            }
        }
    }

    public void update() {
        if (gameState == GameState.PLAY) {
            player.update();
            updateEntities(entityHandler.getNpcs());
            updateEntities(entityHandler.getMonsters());

        } else if (gameState == GameState.PAUSE) {
            // do nothing
        }
    }

    private void updateEntities(final List<Entity> entities) {
        entities.forEach(entity -> {
            if (entity != null) {
                if (entity.isAlive() && !entity.isDying()) {
                    entity.update();
                }
                if (!entity.isAlive()) {
                    entityHandler.removeEntity(entity, entityHandler.getMonsters());
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // TILE - background should be drawn before anything else
        tileManager.draw(g2d);
        // OBJECT
        entityHandler.drawObjects(g2d);
        // NPCs
        entityHandler.drawNPCs(g2d);
        // MONSTERS
        entityHandler.drawMonsters(g2d);
        // PLAYER
        player.draw(g2d);
        // UI - should be rendered over everything (last)
        ui.draw(g2d);

        g2d.dispose();
    }

    public boolean isWhitinScreenBoundaries(int worldX, int worldY) {
        return worldX + GamePanel.TILE_SIZE > player.getWorldX() - player.getScreenX() &&
                worldX - GamePanel.TILE_SIZE < player.getWorldX() + player.getScreenX() &&
                worldY + GamePanel.TILE_SIZE > player.getWorldY() - player.getScreenY() &&
                worldY - GamePanel.TILE_SIZE < player.getWorldY() + player.getScreenY();
    }
}
