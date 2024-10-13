package entity;

import lombok.Getter;
import lombok.Setter;
import main.AssetHandler;
import main.CollisionHandler;
import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;

    private final AssetHandler assetHandler;
    // Position of the screen center
    @Getter
    @Setter
    private int screenX;
    @Getter
    @Setter
    private int screenY;
    @Getter
    @Setter
    private boolean collision = false;

    public Player(GamePanel gp, KeyHandler kh, AssetHandler ah) {
        super(gp);
        this.gamePanel = gp;
        this.keyHandler = kh;
        this.assetHandler = ah;
        screenX = GamePanel.screenWidth / 2 - (GamePanel.tileSize / 2);
        screenY = GamePanel.screenHeight / 2 - (GamePanel.tileSize / 2);
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
        worldX = GamePanel.tileSize * 23;
        worldY = GamePanel.tileSize * 21;
        speed = 4;
        direction = Direction.DOWN;
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
            System.out.println("NPC interaction: " + npcIndex);
        }
    }

    public void reactToObject(int objetIndex) {
        if (objetIndex >= 0) {

        }
    }

    public void draw(Graphics2D g2d) {
        // Draw player image
        BufferedImage image = getSpriteImage();
        g2d.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
    }
}
