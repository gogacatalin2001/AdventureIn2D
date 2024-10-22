package entity;

import lombok.Getter;
import lombok.Setter;
import main.CollisionHandler;
import main.GamePanel;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Entity {
    public static final int SPRITE_UPDATE_SPEED = 12;
    protected final GamePanel gamePanel;
    // ENTITY
    protected String name;
    @Setter
    protected int life;
    protected int maxLife = 6;
    @Setter
    protected boolean invincible = false;
    @Setter
    protected int invincibleCounter = 0;
    // SPRITE SETTINGS
    protected List<BufferedImage> images = new ArrayList<>();
    protected BufferedImage down1, down2, up1, up2, left1, left2, right1, right2;
    protected int spriteCounter = 0;
    protected int spriteNumber = 1;
    // MOVEMENT
    protected int actionLockCounter = 0;
    protected Rectangle collisionBox = new Rectangle(0, 0, 48, 48);
    protected int collisionBoxDefaultX, collisionBoxDefaultY;
    @Setter
    protected boolean collisionDetected = false;
    @Setter
    protected boolean collisionEnabled = false;
    @Setter
    protected int worldX, worldY; // Position of the entity in the world
    @Setter
    protected int speed;
    @Setter
    protected Direction direction = Direction.DOWN;
    // NPC INTERACTIONS
    protected List<String> dialogues = new ArrayList<>();
    protected int dialogueIndex = 0;

    public Entity(GamePanel gp, String imageBasePath, List<String> imageNames) {
        this.gamePanel = gp;
        loadImages(imageBasePath, imageNames);
    }

    public void update() {
        setAction();

        // CHECK COLLISION
        // tile
        collisionDetected = false;
        CollisionHandler.checkTileCollision(this, gamePanel.getTileManager());
        // object
        CollisionHandler.checkEntityCollision(this, gamePanel.getEntityHandler().getObjects());
        // player
        CollisionHandler.checkEntityCollision(this, List.of(gamePanel.getPlayer()));
        // npc
        CollisionHandler.checkEntityCollision(this, gamePanel.getEntityHandler().getNpcs());
        // monster
        CollisionHandler.checkEntityCollision(this, gamePanel.getEntityHandler().getMonsters());

        move();

        // Change between sprites for character movement
        spriteCounter++;
        if (spriteCounter > SPRITE_UPDATE_SPEED) {
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

    protected void move() {
        if (!collisionDetected) {
            switch (direction) {
                case UP -> worldY -= speed;
                case DOWN -> worldY += speed;
                case LEFT -> worldX -= speed;
                case RIGHT -> worldX += speed;
            }
        }
    }

    protected void setAction() {

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

    /**
     * <p>
     *     Sets loaded images for displaying the entity from all sides.
     * <p>
     *     If the entity has less than 8 images, then the method MUST
     *     be overloaded and called in the constructor of the subclass
     *     for proper display of the object.
     */
    protected void setSpriteImages() {
        down1 = images.get(0);
        down2 = images.get(1);
        up1 = images.get(2);
        up2 = images.get(3);
        left1 = images.get(4);
        left2 = images.get(5);
        right1 = images.get(6);
        right2 = images.get(7);
    }

    protected void loadImages(String basePath, List<String> imageNames) {
        imageNames.forEach(imageName -> images.add(readImage(basePath, imageName)));
    }

    protected BufferedImage readImage(String basePath, String imageName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(basePath + imageName));
            image = ImageScalingUtil.scaleImage(image, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    protected BufferedImage getSpriteImage() {
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

    public BufferedImage getImage(int index) {
        return images.get(index);
    }

}
