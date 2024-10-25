package entity.monster;

import entity.Direction;
import entity.Entity;
import main.GamePanel;
import util.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class GreenSlimeMonster extends Entity {

    public GreenSlimeMonster(GamePanel gp) {
        List<ImageProperties> imageMap = new ArrayList<>();
        imageMap.add(new ImageProperties("greenslime_down_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageMap.add(new ImageProperties("greenslime_down_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, "/monsters/", imageMap);
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
    protected BufferedImage getSpriteImage() {
        return images.get(spriteNumber - 1);
    }
}
