package entity;

import entity.weapon.NormalSword;
import entity.weapon.WoodenShield;
import lombok.Getter;
import lombok.Setter;
import main.*;
import sound.SoundManager;
import image.ImageProperties;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private final KeyMouseHandler keyMouseHandler;
    // MOVEMENT
    @Getter
    @Setter
    private boolean collision = false;

    public Player(GamePanel gp) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("boy_down_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_down_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_up_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_up_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_left_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_left_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_right_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_right_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_attack_down_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 2));
        imageProperties.add(new ImageProperties("boy_attack_down_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 2));
        imageProperties.add(new ImageProperties("boy_attack_up_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 2));
        imageProperties.add(new ImageProperties("boy_attack_up_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 2));
        imageProperties.add(new ImageProperties("boy_attack_left_1.png", GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_attack_left_2.png", GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_attack_right_1.png", GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("boy_attack_right_2.png", GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE));
        super(gp, "/entities/blue_boy/", imageProperties);
        this.keyMouseHandler = gp.getKeyMouseHandler();
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {

        // SCREEN
        screenX = GamePanel.SCREEN_WIDTH / 2 - (GamePanel.TILE_SIZE / 2);
        screenY = GamePanel.SCREEN_HEIGHT / 2 - (GamePanel.TILE_SIZE / 2);
        // COLLISION
        collisionBox = new Rectangle();
        collisionBox.x = 8;
        collisionBox.y = 16;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
        collisionBox.width = 32;
        collisionBox.height = 32;
        // ATTACK
        attackArea.width = 36;
        attackArea.height = 36;
        // POSITION
        worldX = GamePanel.TILE_SIZE * 23;
        worldY = GamePanel.TILE_SIZE * 21;
//        worldX = GamePanel.TILE_SIZE * 10;
//        worldY = GamePanel.TILE_SIZE * 13;
        // CHARACTER
        speed = 4;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        experience = 0;
        nextLevelExperience = 5;
        coins = 0;
        currentWeapon = new NormalSword(gamePanel);
        currentShield = new WoodenShield(gamePanel);
        attack = getAttackValue();
        defense = getDefenseValue();
        hitSound = SoundManager.RECEIVE_DAMAGE_SOUND;
    }


    @Override
    public void update() {
        // INVINCIBLE STATE
        updateInvincibleState();
        switch (action) {
            case WALK -> {
                if (keyMouseHandler.isUpPressed() || keyMouseHandler.isDownPressed() ||
                        keyMouseHandler.isLeftPressed() || keyMouseHandler.isRightPressed() || keyMouseHandler.isEnterPressed()) {
                    // MOVEMENT
                    if (keyMouseHandler.isUpPressed()) {
                        direction = Direction.UP;
                    } else if (keyMouseHandler.isDownPressed()) {
                        direction = Direction.DOWN;
                    } else if (keyMouseHandler.isLeftPressed()) {
                        direction = Direction.LEFT;
                    } else if (keyMouseHandler.isRightPressed()) {
                        direction = Direction.RIGHT;
                    }
                    // COLLISION
                    // tiles
                    collisionDetected = false;
                    CollisionHandler.checkTileCollision(this, gamePanel.getTileManager());
                    // objects
                    int collisionObjectIndex = CollisionHandler.checkEntityCollision(this, entityManager.getObjects());
                    reactToObject(collisionObjectIndex);
                    // NPCs
                    int npcIndex = CollisionHandler.checkEntityCollision(this, entityManager.getNpcs());
                    System.out.println("NPC: " + npcIndex);
                    interactNPC(npcIndex);
                    // monsters
                    int monsterIndex = CollisionHandler.checkEntityCollision(this, entityManager.getMonsters());
                    System.out.println("MON: " + monsterIndex);
                    touchMonster(monsterIndex);
                    // EVENTS
                    gamePanel.getEventHandler().checkEvent();

                    move();

                    keyMouseHandler.setEnterPressed(false);
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
                }
            }
            case ATTACK -> attack();
        }
        // MOUSE
        if (keyMouseHandler.isLmbPressed()) {
            action = Action.ATTACK;
            System.out.println("LMB pressed");
        }
    }

    public int getAttackValue() {
        return attack = strength * currentWeapon.getDamageValue();
    }

    public int getDefenseValue() {
        return defense = dexterity * currentShield.getDefenseValue();
    }

    @Override
    protected void move() {
        if (!collisionDetected && !keyMouseHandler.isEnterPressed()) {
            switch (direction) {
                case UP -> worldY -= speed;
                case DOWN -> worldY += speed;
                case LEFT -> worldX -= speed;
                case RIGHT -> worldX += speed;
            }
        }
    }

    private void touchMonster(int monsterIndex) {
        if (monsterIndex >= 0 && collisionDetected) {
            Entity entity = entityManager.getMonsters().get(monsterIndex);
            if (!invincible) {
                gamePanel.getSoundManager().playSound(hitSound);
                int damage = entity.attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex >= 0) {
            if (keyMouseHandler.isEnterPressed()) {
                gamePanel.setGameState(GameState.DIALOGUE);
                entityManager.getNPC(npcIndex).speak();
            }
        }
    }

    public void reactToObject(int objetIndex) {
        if (objetIndex >= 0) {

        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        BufferedImage image = getSpriteImage();
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        // Offset sprite image to correct position if width or height are bigger than {@link GamePanel.TILE_SIZE}
        if (action == Action.ATTACK) {
            switch (direction) {
                case UP -> tempScreenY -= GamePanel.TILE_SIZE;
                case LEFT -> tempScreenX -= GamePanel.TILE_SIZE;
            }
        }
        if (invincible) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        // Draw player image
        g2d.drawImage(image, tempScreenX, tempScreenY, image.getWidth(), image.getHeight(), null);
        // Reset alpha
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
