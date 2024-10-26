package entity;

import lombok.Getter;
import lombok.Setter;
import main.CollisionHandler;
import main.GamePanel;
import util.ImageProperties;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Entity {
    protected final int SPRITE_UPDATE_SPEED = 12;
    protected final GamePanel gamePanel;
    protected final EntityHandler entityHandler;
    // STATE
    protected String name;
    @Setter
    protected int life;
    protected int maxLife = 6;
    @Setter
    protected boolean invincible = false;
    @Setter
    protected int invincibleCounter = 0;
    protected int invincibleTimer = 60;
    @Setter
    protected Action action = Action.WALK;
    protected boolean alive = true;
    protected boolean dying = false;
    protected int dyingCounter = 0;
    // SPRITE SETTINGS
    protected List<BufferedImage> images = new ArrayList<>();
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
    // INTERACTIONS
    protected List<String> dialogues = new ArrayList<>();
    protected int dialogueIndex = 0;
    // ACTIONS
    protected Rectangle attackArea = new Rectangle(0, 0, 0, 0);

    public Entity(GamePanel gp, EntityHandler eh, String imageBasePath, List<ImageProperties> imageProperties) {
        this.gamePanel = gp;
        this.entityHandler = eh;
        loadImages(imageBasePath, imageProperties);
        setDefaultValues();
    }

    public void update() {
        setAction();

        // COLLISION
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

        // SPRITE
        spriteCounter++;
        if (spriteCounter > SPRITE_UPDATE_SPEED) {
            if (spriteNumber == 1) {
                spriteNumber = 2;
            } else {
                spriteNumber = 1;
            }
            spriteCounter = 0;
        }
        // ATTACK
        updateInvincibleState();
    }

    protected void updateInvincibleState() {
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > invincibleTimer) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
        int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

        if (gamePanel.isWhitinScreenBoundaries(worldX, worldY)) {
            BufferedImage image = getSpriteImage();
            // Modify alpha when attacked
            if (invincible) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            if (dying) {
                dyingAnimation(g2d);
            }
            g2d.drawImage(image, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
            // Reset alpha
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    private void dyingAnimation(Graphics2D g2d) {
        dyingCounter++;

        if (dyingCounter % 10 != 0) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        }
        else {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        if (dyingCounter > 40) {
            dying = false;
            alive = false;
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

    protected void setDefaultValues() {
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

    protected void loadImages(String basePath, List<ImageProperties> imageProperties) {
        imageProperties.forEach(properties -> {
            BufferedImage image = readImage(basePath, properties.imageName(), properties.width(), properties.height());
            if (image != null) {
                images.add(image);
            }
        });
    }

    protected BufferedImage readImage(String basePath, String imageName, Integer width, Integer height) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(basePath + imageName));
            image = ImageScalingUtil.scaleImage(image, width, height);
        } catch (IOException e) {
            System.out.println("Failed to load image: " + basePath + " " + imageName);
        }
        return image;
    }


    protected BufferedImage getSpriteImage() {
        int actionIndex = action.ordinal() * 8;
        int directionIndex = direction.ordinal() * 2;

        if (spriteNumber == 1) {
            return images.get(actionIndex + directionIndex);
        } else {
            return images.get(actionIndex + directionIndex + 1);
        }
    }
}
