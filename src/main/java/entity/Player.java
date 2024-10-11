package entity;

import lombok.Getter;
import lombok.Setter;
import main.*;
import object.SuperObject;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final CollisionHandler collisionHandler;
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

    public Player(GamePanel gp, KeyHandler kh, CollisionHandler ch, AssetHandler ah) {
        this.gamePanel = gp;
        this.keyHandler = kh;
        this.collisionHandler = ch;
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
            } else if (keyHandler.isRightPressed()) {
                direction = Direction.RIGHT;
            }

            // CHECK TILE COLLISION
            collisionDetected = false;
//            collisionDetected = collisionHandler.checkTileCollision(this);
            // CHECK OBJECT COLLISION
            int collisionObjectIndex = collisionHandler.checkObjectCollision(this, true);
            reactToObject(collisionObjectIndex);

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

    public void getPlayerImage() {
        final String BOY_WALKING = "/BlueBoy/Walking/";
        up1 = readImage(BOY_WALKING + "boy_up_1.png");
        up2 = readImage(BOY_WALKING + "boy_up_2.png");
        down1 = readImage(BOY_WALKING + "boy_down_1.png");
        down2 = readImage(BOY_WALKING + "boy_down_2.png");
        left1 = readImage(BOY_WALKING + "boy_left_1.png");
        left2 = readImage(BOY_WALKING + "boy_left_2.png");
        right1 = readImage(BOY_WALKING + "boy_right_1.png");
        right2 = readImage(BOY_WALKING + "boy_right_2.png");
    }

    private BufferedImage readImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/entities" + path));
            image = ImageScalingUtil.scaleImage(image, GamePanel.tileSize, GamePanel.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void reactToObject(int objetIndex) {
        if (objetIndex >= 0) {

        }
    }

    public void draw(Graphics2D g2d) {
        // Draw player image
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

        int x = screenX;
        int y = screenY;

        if (screenX > worldX) {
            x = worldX;
        }
        if (screenY > worldY) {
            y = worldY;
        }
        int rightOffset = GamePanel.screenWidth - screenX;
        if (rightOffset > GamePanel.worldWidth - worldX) {
            x = GamePanel.screenWidth - (GamePanel.worldWidth - worldX);
        }
        int bottomOffset = GamePanel.screenHeight - screenY;
        if (bottomOffset > GamePanel.worldHeight - worldY) {
            y = GamePanel.screenHeight - (GamePanel.worldHeight - worldY);
        }

        g2d.drawImage(image, x, y, GamePanel.tileSize, GamePanel.tileSize, null);
    }
}
