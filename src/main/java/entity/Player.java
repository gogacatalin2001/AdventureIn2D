package entity;

import lombok.Getter;
import lombok.Setter;
import main.CollisionHandler;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import util.ImageProperties;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final EntityHandler entityHandler;
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

    public Player(GamePanel gp, KeyHandler kh, EntityHandler ah) {
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
        this.gamePanel = gp;
        this.keyHandler = kh;
        this.entityHandler = ah;
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
//        worldX = GamePanel.TILE_SIZE * 23;
//        worldY = GamePanel.TILE_SIZE * 21;
        screenX = GamePanel.SCREEN_WIDTH / 2 - (GamePanel.TILE_SIZE / 2);
        screenY = GamePanel.SCREEN_HEIGHT / 2 - (GamePanel.TILE_SIZE / 2);
        collisionBox = new Rectangle();
        collisionBox.x = 8;
        collisionBox.y = 16;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
        collisionBox.width = 32;
        collisionBox.height = 32;
        // Player starting position in world coordinates
        worldX = GamePanel.TILE_SIZE * 10;
        worldY = GamePanel.TILE_SIZE * 13;
        speed = 4;
        life = maxLife;
    }


    @Override
    public void update() {
        switch (action) {
            case WALK -> {
                if (keyHandler.isUpPressed() || keyHandler.isDownPressed() ||
                        keyHandler.isLeftPressed() || keyHandler.isRightPressed() || keyHandler.isEnterPressed()) {
                    // MOVEMENT
                    if (keyHandler.isUpPressed()) {
                        direction = Direction.UP;
                    } else if (keyHandler.isDownPressed()) {
                        direction = Direction.DOWN;
                    } else if (keyHandler.isLeftPressed()) {
                        direction = Direction.LEFT;
                    } else if (keyHandler.isRightPressed()) {
                        direction = Direction.RIGHT;
                    }
                    // COLLISION
                    // tiles
                    collisionDetected = false;
                    CollisionHandler.checkTileCollision(this, gamePanel.getTileManager());
                    // objects
                    int collisionObjectIndex = CollisionHandler.checkEntityCollision(this, entityHandler.getObjects());
                    reactToObject(collisionObjectIndex);
                    // NPCs
                    int npcIndex = CollisionHandler.checkEntityCollision(this, entityHandler.getNpcs());
                    System.out.println("NPC: " + npcIndex);
                    interactNPC(npcIndex);
                    // monsters
                    int monsterIndex = CollisionHandler.checkEntityCollision(this, entityHandler.getMonsters());
                    System.out.println("MON: " + monsterIndex);
                    touchMonster(monsterIndex);
                    // EVENTS
                    gamePanel.getEventHandler().checkEvent();

                    move();

                    keyHandler.setEnterPressed(false);
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
        if (keyHandler.isLmbPressed()) {
            action = Action.ATTACK;
            System.out.println("LMB pressed");
        }

        // INTERACTION
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void attack() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNumber = 1;
        } else if (spriteCounter <= 25) {
            spriteNumber = 2;
        } else {
            spriteNumber = 1;
            spriteCounter = 0;
            action = Action.WALK;
        }
    }

    @Override
    protected void move() {
        if (!collisionDetected && !keyHandler.isEnterPressed()) {
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
            if (!invincible) {
                life--;
                invincible = true;
            }
        }
    }

    private void interactNPC(int npcIndex) {
        if (npcIndex >= 0) {
            if (keyHandler.isEnterPressed()) {
                gamePanel.setGameState(GameState.DIALOGUE);
                entityHandler.getNPC(npcIndex).speak();
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
