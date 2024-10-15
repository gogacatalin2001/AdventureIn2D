package entity;

import lombok.Getter;
import lombok.Setter;
import main.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final AssetHandler assetHandler;
    // MOVEMENT
    @Getter
    @Setter
    private int screenX;
    @Getter
    @Setter
    private int screenY;
    @Getter
    @Setter
    private boolean collision = false;
    // CHARACTER STATUS
    @Getter
    @Setter
    int life;
    public final int maxLife = 6;

    public Player(GamePanel gp, KeyHandler kh, AssetHandler ah) {
        super(gp);
        this.gamePanel = gp;
        this.keyHandler = kh;
        this.assetHandler = ah;
        screenX = GamePanel.SCREEN_WIDTH / 2 - (GamePanel.TILE_SIZE / 2);
        screenY = GamePanel.SCREEN_HEIGHT / 2 - (GamePanel.TILE_SIZE / 2);
        collisionBox = new Rectangle();
        collisionBox.x = 8;
        collisionBox.y = 16;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
        collisionBox.width = 32;
        collisionBox.height = 32;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        // Player starting position in world coordinates
        worldX = GamePanel.TILE_SIZE * 23;
        worldY = GamePanel.TILE_SIZE * 21;
        speed = 4;
        direction = Direction.DOWN;
        life = maxLife;
    }

    private void getPlayerImage() {
        final String BOY_WALKING = "/entities/blue_boy/Walking/";
        up1 = readImage(BOY_WALKING, "boy_up_1.png");
        up2 = readImage(BOY_WALKING, "boy_up_2.png");
        down1 = readImage(BOY_WALKING, "boy_down_1.png");
        down2 = readImage(BOY_WALKING, "boy_down_2.png");
        left1 = readImage(BOY_WALKING, "boy_left_1.png");
        left2 = readImage(BOY_WALKING, "boy_left_2.png");
        right1 = readImage(BOY_WALKING, "boy_right_1.png");
        right2 = readImage(BOY_WALKING, "boy_right_2.png");
    }

    public void update() {
        // Only render tiles within the screen boundary
        if (keyHandler.isUpPressed() || keyHandler.isDownPressed() ||
                keyHandler.isLeftPressed() || keyHandler.isRightPressed()) {
            // Handle character movement
            if (keyHandler.isUpPressed()) {
                direction = Direction.UP;
            } else if (keyHandler.isDownPressed()) {
                direction = Direction.DOWN;
            } else if (keyHandler.isLeftPressed()) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }

            // CHECK TILE COLLISION
            collisionDetected = false;
            gamePanel.getCollisionHandler().checkTileCollision(this);
            // CHECK OBJECT COLLISION
            int collisionObjectIndex = gamePanel.getCollisionHandler().checkObjectCollision(this, true);
            reactToObject(collisionObjectIndex);
            // CHECK NPC COLLISION
            int npcIndex = gamePanel.getCollisionHandler().checkEntityCollision(this);
            interactNPC(npcIndex);
            // CHECK EVENT
            gamePanel.getEventHandler().checkEvent();

            keyHandler.setEnterPressed(false);

            
            if (!collisionDetected) {
                switch (direction) {
                    case UP -> worldY -= speed;
                    case DOWN -> worldY += speed;
                    case LEFT -> worldX -= speed;
                    case RIGHT -> worldX += speed;
                }
            }

            // Change between sprites for character movement
            spriteCounter++;
            if (spriteCounter > spriteUpdateSpeed) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex >= 0) {
            if (keyHandler.isEnterPressed()) {
                gamePanel.setGameState(GameState.DIALOGUE);
                assetHandler.getNPC(npcIndex).speak();
            }
        }
    }

    public void reactToObject(int objetIndex) {
        if (objetIndex >= 0) {

        }
    }

    public void draw(Graphics2D g2d) {
        // Draw player image
        BufferedImage image = getSpriteImage();
        g2d.drawImage(image, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
    }
}
