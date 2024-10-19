package entity.monster;

import entity.Direction;
import entity.Entity;
import main.GamePanel;

import java.util.List;
import java.util.Random;

public class GreenSlimeMonster extends Entity {

    public GreenSlimeMonster(GamePanel gp) {
        super(gp, "/monsters/", List.of("greenslime_down_1.png", "greenslime_down_2.png"));
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        collisionBox.x = 3;
        collisionBox.y = 18;
        collisionBox.width = 42;
        collisionBox.height = 30;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;

        setSpriteImages();
        setAction();
    }

    @Override
    protected void setSpriteImages() {
        down1 = images.get(0);
        down2 = images.get(1);
        up1 = images.get(0);
        up2 = images.get(1);
        left1 = images.get(0);
        left2 = images.get(1);
        right1 = images.get(0);
        right2 = images.get(1);
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
}
