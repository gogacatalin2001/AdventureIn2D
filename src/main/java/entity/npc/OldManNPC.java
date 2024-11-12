package entity.npc;

import entity.Direction;
import entity.Entity;
import entity.EntityManager;
import main.GamePanel;
import image.ImageProperties;

import java.util.*;

public class OldManNPC extends Entity {

    public OldManNPC(GamePanel gamePanel, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("oldman_down_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_down_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_up_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_up_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_left_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_left_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_right_1.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("oldman_right_2.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gamePanel, eh, "/npc/old_man/", imageProperties);
        speed = 1;
        name = "Old Man";
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

}
