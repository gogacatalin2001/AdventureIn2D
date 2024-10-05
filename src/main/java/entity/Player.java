package entity;

import lombok.Getter;
import lombok.Setter;
import main.ColisionHandler;
import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    // Position of the screen center
    @Getter @Setter
    private int screenX;
    @Getter @Setter
    private int screenY;

    private final KeyHandler keyHandler;
    private final ColisionHandler colisionHandler;

    public Player(KeyHandler kh, ColisionHandler ch) {
        this.keyHandler = kh;
        this.colisionHandler = ch;
        screenX = GamePanel.screenWidth / 2 - (GamePanel.tileSize / 2);
        screenY = GamePanel.screenHeight / 2 - (GamePanel.tileSize / 2);
        int collisionBoxX = 8;
        int collisionBoxY = 16;
        int collisionBoxWidth = 32;
        int collisionBoxHeight = 32;
        collisionBox = new Rectangle(collisionBoxX, collisionBoxY, collisionBoxWidth, collisionBoxHeight);
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

            if (!colisionHandler.checkTileCollision(this)) {
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
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/BlueBoy/Walking/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
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
