package entity.monster;

import com.sun.jdi.Method;
import entity.Direction;
import entity.Entity;
import entity.EntityHandler;
import main.GamePanel;
import util.ImageProperties;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class GreenSlimeMonster extends Entity {

    public GreenSlimeMonster(GamePanel gp, EntityHandler eh) {
        List<ImageProperties> imageMap = new ArrayList<>();
        imageMap.add(new ImageProperties("greenslime_down_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageMap.add(new ImageProperties("greenslime_down_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/monsters/", imageMap);
        setAction();
    }

    @Override
    protected void setDefaultValues() {
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        invincibleTimer = 40;
        collisionBox.x = 3;
        collisionBox.y = 18;
        collisionBox.width = 42;
        collisionBox.height = 30;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
    }

    @Override
    protected void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120 ) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                direction = Direction.UP;
            } else if (i <= 50) {
                direction = Direction.DOWN;
            } else if (i < 75) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
            actionLockCounter = 0;
        }
    }

    @Override
    protected void reactToDamage() {
        if (damageReceived) {
            actionLockCounter = 0;
            direction = gamePanel.getPlayer().getDirection();

            damageReceived = false;
        }
    }

    @Override
    public void update() {
        super.update();

        if (damageReceived) {
            reactToDamage();
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
            // Draw health bar
            if (life != maxLife) {
                double hpUnitScale = (double) GamePanel.TILE_SIZE / maxLife;
                double hpBarValue = life * hpUnitScale;
                g2d.setColor(new Color(75, 75, 75, 255));
                g2d.fillRect(screenX, screenY - 15, GamePanel.TILE_SIZE, 10);
                g2d.setColor(new Color(210, 5, 30));
                g2d.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
            }

            g2d.drawImage(image, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
            // Reset alpha
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    @Override
    protected BufferedImage getSpriteImage() {
        return images.get(spriteNumber - 1);
    }

}
