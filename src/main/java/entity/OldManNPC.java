package entity;

import main.GamePanel;

import java.util.Random;

public class OldManNPC extends Entity {

    public OldManNPC(GamePanel gamePanel) {
        super(gamePanel);
        direction = Direction.DOWN;
        speed = 1;
        getEntityImage();
    }

    public void setAction() {
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

    private void getEntityImage() {
        final String OLD_MAN = "/npc/old_man/";
        up1 = readImage(OLD_MAN, "oldman_up_1.png");
        up2 = readImage(OLD_MAN, "oldman_up_2.png");
        down1 = readImage(OLD_MAN, "oldman_down_1.png");
        down2 = readImage(OLD_MAN, "oldman_down_2.png");
        left1 = readImage(OLD_MAN, "oldman_left_1.png");
        left2 = readImage(OLD_MAN, "oldman_left_2.png");
        right1 = readImage(OLD_MAN, "oldman_right_1.png");
        right2 = readImage(OLD_MAN, "oldman_right_2.png");
    }
}