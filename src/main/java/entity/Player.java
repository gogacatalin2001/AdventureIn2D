package entity;

import lombok.Getter;
import lombok.Setter;
import main.*;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final CollisionHandler collisionHandler;
    private final AssetHandler assetHandler;
    private int standCounter = 0;
    private boolean moving = false;
    private int pixelCounter = 0;
    // Position of the screen center
    @Getter
    @Setter
    private int screenX;
    @Getter
    @Setter
    private int screenY;
    @Getter
    private int keyCount = 0;
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
        collisionBox.x = 1;
        collisionBox.y = 1;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
        collisionBox.width = GamePanel.tileSize - 2;
        collisionBox.height = GamePanel.tileSize - 2;

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
        if (!moving) {
            if (keyHandler.isUpPressed() || keyHandler.isDownPressed() ||
                    keyHandler.isLeftPressed() || keyHandler.isRightPressed()) {
                moving = true;
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
                collisionDetected = collisionHandler.checkTileCollision(this);
                // CHECK OBJECT COLLISION
                int collisionObjectIndex = collisionHandler.checkObjectCollision(this, true);
                reactToObject(collisionObjectIndex);
            } else {
                standCounter++;
                if (standCounter == 16) {
                    spriteNumber = 1;
                    standCounter = 0;
                }
            }
        } else {
            // Only render tiles within the screen boundary
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

            pixelCounter += speed;
            if (pixelCounter == GamePanel.tileSize) {
                moving = false;
                pixelCounter = 0;
            }
        }
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/entities/BlueBoy/Walking/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reactToObject(int objetIndex) {
        if (objetIndex >= 0) {
            SuperObject collidedObject = assetHandler.getObjects().get(objetIndex);
            switch (collidedObject.getName()) {
                case "Key" -> {
                    gamePanel.playSoundEffect(1);
                    keyCount++;
                    assetHandler.deleteObject(objetIndex);
                    gamePanel.getUi().showMessage("You got a key!");
                }
                case "Door" -> {
                    if (keyCount > 0) {
                        gamePanel.playSoundEffect(3);
                        keyCount--;
                        assetHandler.deleteObject(objetIndex);
                        System.out.println("Opened door. Remaining keys: " + keyCount);
                        gamePanel.getUi().showMessage("Door opened!");
                    } else {
                        gamePanel.getUi().showMessage("You need a key!");
                    }
                }
                case "Boots" -> {
                    gamePanel.playSoundEffect(2);
                    speed += 2;
                    assetHandler.deleteObject(objetIndex);
                    gamePanel.getUi().showMessage("Speed increased!");
                }
                case "Chest" -> {
                    gamePanel.getUi().setGameFinished(true);
                    gamePanel.stopMusic();
                    gamePanel.playSoundEffect(4);
                }
            }
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
        g2d.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
    }
}
