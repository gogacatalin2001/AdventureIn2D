package main;

import entity.Entity;
import entity.EntityManager;
import entity.Player;
import lombok.Getter;
import lombok.Setter;
import main.event.EventHandler;
import main.ui.UIManager;
import sound.SoundManager;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Getter
public class GamePanel extends JPanel implements Runnable, Updatable {
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
    private final EntityManager entityManager = new EntityManager(this);
    private final SoundManager soundManager = new SoundManager();
    private final UIManager uiManager = new UIManager(this);
    private final KeyMouseHandler keyMouseHandler = new KeyMouseHandler(this);
    // ENTITIES
    private final Player player = new Player(this);
    @Setter
    private Thread gameThread;
    // GAME STATE
    @Setter
    private GameState gameState;
    @Setter
    private boolean debugging = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyMouseHandler);
        this.addMouseListener(keyMouseHandler);
        this.setFocusable(true);
        setupGame();
    }

    public void setupGame() {
        gameState = GameState.TITLE_SCREEN;
        entityManager.setObject();
        entityManager.setNPC();
        entityManager.setMonsters();
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
        switch (gameState) {
            case GameState.PLAY -> {
                soundManager.update();
                player.update();
                updateEntities(entityManager.getNpcs());
                updateEntities(entityManager.getMonsters());
            }
            case CHARACTER_SCREEN -> soundManager.update();
        }
    }

    private void updateEntities(final List<Entity> entities) {
        entities.forEach(entity -> {
            if (entity != null) {
                if (entity.isAlive() && !entity.isDying()) {
                    entity.update();
                }
                if (!entity.isAlive()) {
                    entityManager.removeEntity(entity, entityManager.getMonsters());
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
        entityManager.drawObjects(g2d);
        // NPCs
        entityManager.drawNPCs(g2d);
        // MONSTERS
        entityManager.drawMonsters(g2d);
        // PLAYER
        player.draw(g2d);
        // UIManager - should be rendered over everything (last)
        uiManager.draw(g2d);

        g2d.dispose();
    }

    public boolean isWithinScreenBoundaries(final int worldX, final int worldY) {
        return worldX + GamePanel.TILE_SIZE > player.getWorldX() - player.getScreenX() &&
                worldX - GamePanel.TILE_SIZE < player.getWorldX() + player.getScreenX() &&
                worldY + GamePanel.TILE_SIZE > player.getWorldY() - player.getScreenY() &&
                worldY - GamePanel.TILE_SIZE < player.getWorldY() + player.getScreenY();
    }

    /**
     * The map file must be saved before reloading for changes to be visible.
     * In <b>IntelliJ</b> use <b>Ctrl + F9</b>
     * @param filePath the path to the map to load
     */
    public void loadMap(final String filePath) {
        tileManager.loadMap(filePath);
    }
}
