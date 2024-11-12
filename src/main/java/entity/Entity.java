package entity;

import entity.weapon.Weapon;
import lombok.Getter;
import lombok.Setter;
import main.CollisionHandler;
import main.GamePanel;
import main.Updatable;
import sound.SoundManager;
import image.ImageProperties;
import image.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Entity implements Updatable, DrawableEntity {
    protected final GamePanel gamePanel;
    protected final EntityManager entityManager;
    // STATE
    protected String name = "";
    protected int maxLife = 6;
    @Setter
    protected int life = maxLife;
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
    protected boolean damageReceived = false;
    // SOUND
    protected String hitSound = SoundManager.NO_SOUND;
    // SPRITE
    protected List<BufferedImage> images = new ArrayList<>();
    protected final int SPRITE_UPDATE_SPEED = 12;
    protected int spriteCounter = 0;
    protected int spriteNumber = 1;
    // MOVEMENT
    @Getter
    @Setter
    protected int screenX;
    @Getter
    @Setter
    protected int screenY;
    protected int actionLockCounter = 0;
    protected Rectangle collisionBox = new Rectangle(0, 0, 48, 48);
    protected int collisionBoxDefaultX = collisionBox.x;
    protected int collisionBoxDefaultY = collisionBox.y;
    @Setter
    protected boolean collisionDetected = false;
    @Setter
    protected boolean collisionEnabled = false;
    @Setter
    protected int worldX, worldY; // Position of the entity in the world
    @Setter
    protected int speed = 1;
    @Setter
    protected Direction direction = Direction.DOWN;
    // INTERACTIONS
    protected List<String> dialogues = new ArrayList<>();
    protected int dialogueIndex = 0;
    // ACTIONS
    protected Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    // CHARACTER ATTRIBUTES
    protected int level = 0;
    protected int strength = 1;
    protected int dexterity = 1;
    protected int intelligence = 1;
    protected int attack = 1;
    protected int defense = 1;
    protected int experience = 0;
    protected int nextLevelExperience = 5;
    protected int coins = 0;
    protected Weapon currentWeapon;
    protected Weapon currentShield;

    public Entity(GamePanel gp, EntityManager eh, String imageBasePath, List<ImageProperties> imageProperties) {
        this.gamePanel = gp;
        this.entityManager = eh;
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
        CollisionHandler.checkEntityCollision(this, gamePanel.getEntityManager().getObjects());
        // player
        CollisionHandler.checkEntityCollision(this, List.of(gamePanel.getPlayer()));
        // npc
        CollisionHandler.checkEntityCollision(this, gamePanel.getEntityManager().getNpcs());
        // monster
        CollisionHandler.checkEntityCollision(this, gamePanel.getEntityManager().getMonsters());

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

    @Override
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

    protected void dyingAnimation(Graphics2D g2d) {
        dyingCounter++;

        if (dyingCounter % 15 < 7) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        else {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        if (dyingCounter > 60) {
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

    protected void setAction() {}

    protected void setDefaultValues() {}

    protected void reactToDamage() {}

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

    protected void attack() {
        spriteCounter++;
        gamePanel.getSoundManager().playSound(currentWeapon.getSound());

        if (spriteCounter <= 5) {
            spriteNumber = 1;
        } else if (spriteCounter <= 25) {
            spriteNumber = 2;
            // Save current position
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            // Move to attacked position
            switch (direction) {
                case DOWN -> worldY = this.worldY + attackArea.height;
                case UP -> worldY = this.worldY - attackArea.height;
                case LEFT -> worldX = this.worldX - attackArea.width;
                case RIGHT -> worldX = this.worldX + attackArea.width;
            }
            // Check collision
            int entityIndex = CollisionHandler.checkEntityCollision(this, entityManager.getMonsters());
            Entity entityToAttack = entityIndex >= 0 ? entityManager.getMonsters().get(entityIndex) : null;
            // Perform attack
            dealDamageToEntity(entityToAttack);
            // Restore default position
            worldX = currentWorldX;
            worldY = currentWorldY;
        } else {
            spriteNumber = 1;
            spriteCounter = 0;
            action = Action.WALK;
        }
    }

    protected void dealDamageToEntity(final Entity entity) {
        if (entity != null) {
            if (!entity.invincible && !entity.damageReceived) {
                gamePanel.getSoundManager().playSound(entity.hitSound);
                int damage = this.attack - entity.defense;
                damage = Math.max(damage, 0);
                entity.life -= damage;
                entity.invincible = true;
                entity.damageReceived = true;
                gamePanel.getUi().addOnScreenMessage(damage + " DAMAGE");
                if (entity.life <= 0) {
                    entity.dying = true;
                    gamePanel.getUi().addOnScreenMessage("KILLED " + entity.name);
                }
            }
            System.out.println(this.name + ": attacking entity at (x, y): (" + entity.worldX + "," + entity.worldY + ")");
        } else {
            System.out.println("Miss!");
        }
    }

    @Override
    public void loadImages(String basePath, List<ImageProperties> imageProperties) {
        imageProperties.forEach(properties -> images.add(readImage(basePath, properties.imageName(), properties.width(), properties.height())));
    }

    @Override
    public BufferedImage readImage(String basePath, String imageName, Integer width, Integer height) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(basePath + imageName)));
            image = ImageScalingUtil.scaleImage(image, width, height);
        } catch (IOException e) {
            System.err.println("Failed to load image: " + basePath + " " + imageName);
            System.err.println(e.getMessage());
        }
        return image;
    }

    @Override
    public BufferedImage getSpriteImage() {
        int actionIndex = action.ordinal() * 8;
        int directionIndex = direction.ordinal() * 2;

        if (spriteNumber == 1) {
            return images.get(actionIndex + directionIndex);
        } else {
            return images.get(actionIndex + directionIndex + 1);
        }
    }
}
