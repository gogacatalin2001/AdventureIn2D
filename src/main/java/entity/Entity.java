package entity;

import lombok.Getter;
import lombok.Setter;
import main.GamePanel;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
public abstract class Entity {
    final GamePanel gamePanel;
    // SPRITE SETTINGS
    @Setter
    BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public static final int spriteUpdateSpeed = 12;
    int spriteCounter = 0;
    int spriteNumber = 1;
    // MOVEMENT
    int actionLockCounter = 0;
    Rectangle collisionBox = new Rectangle(0, 0, 48, 48);
    int collisionBoxDefaultX, collisionBoxDefaultY;
    @Setter
    boolean collisionDetected = false;
    @Setter
    int worldX, worldY; // Position of the entity in the world
    @Setter
    int speed;
    @Setter
    Direction direction;
    // NPC INTERACTIONS
    List<String> dialogues = new ArrayList<>();
    int dialogueIndex = 0;

    public Entity(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void update() {
        setAction();
        // CHECK TILE COLLISION
        collisionDetected = false;
        gamePanel.getCollisionHandler().checkTileCollision(this);
        // CHECK OBJECT COLLISION
        gamePanel.getCollisionHandler().checkObjectCollision(this, false);
        // CHECK PLAYER COLLISION
        gamePanel.getCollisionHandler().checkPlayerCollision(gamePanel.getPlayer(), this);

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

    public void draw(Graphics2D g2d) {
        int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
        int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

        if (gamePanel.isWhitinScreenBoundaries(worldX, worldY)) {
            BufferedImage image = getSpriteImage();
            g2d.drawImage(image, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        }
    }

    void setAction() {

    }

    public void speak() {
        if (dialogueIndex == dialogues.size()) {
            dialogueIndex = 0;
        }
        gamePanel.getUi().setCurrentDialogue(dialogues.get(dialogueIndex));
        dialogueIndex++;

        switch (gamePanel.getPlayer().getDirection()) {
            case UP -> direction = Direction.DOWN;
            case DOWN -> direction = Direction.UP;
            case LEFT -> direction = Direction.RIGHT;
            case RIGHT -> direction = Direction.LEFT;
        }
    }

    BufferedImage readImage(String basePath, String imageName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(basePath + imageName));
            image = ImageScalingUtil.scaleImage(image, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    BufferedImage getSpriteImage() {
        BufferedImage image = null;
        switch (direction) {
            case UP -> {
                if (spriteNumber == 1) {
                    image = up1;
                }
                if (spriteNumber == 2) {
                    image = up2;
                }
            }
            case DOWN -> {
                if (spriteNumber == 1) {
                    image = down1;
                }
                if (spriteNumber == 2) {
                    image = down2;
                }
            }
            case LEFT -> {
                if (spriteNumber == 1) {
                    image = left1;
                }
                if (spriteNumber == 2) {
                    image = left2;
                }
            }
            case RIGHT -> {
                if (spriteNumber == 1) {
                    image = right1;
                }
                if (spriteNumber == 2) {
                    image = right2;
                }
            }
        }
        return image;
    }

}
