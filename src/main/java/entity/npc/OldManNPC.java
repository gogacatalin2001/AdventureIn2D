package entity.npc;

import entity.Direction;
import entity.Entity;
import main.GamePanel;

import java.util.List;
import java.util.Random;

public class OldManNPC extends Entity {

    public OldManNPC(GamePanel gamePanel) {
        super(
                gamePanel, "/npc/old_man/",
                List.of("oldman_down_1.png",
                        "oldman_down_2.png",
                        "oldman_up_1.png",
                        "oldman_up_2.png",
                        "oldman_left_1.png",
                        "oldman_left_2.png",
                        "oldman_right_1.png",
                        "oldman_right_2.png")
        );
        speed = 1;
        name = "Old Man";
        setSpriteImages();
        setDialogue();
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

    void setDialogue() {
        dialogues.add("Hello, lad!");
        dialogues.add("So you've come to this island for \nthe treasure?");
        dialogues.add("I used to be a great wizard, but now... \nI'm a bit too old for adventures \nlike these.");
        dialogues.add("Well, I wish you good luck!");
    }

    @Override
    public void speak() {
        super.speak();
    }

    @Override
    protected void setSpriteImages() {
        super.setSpriteImages();
    }
}
