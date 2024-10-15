package entity.object;

import entity.Entity;
import main.GamePanel;

import java.util.List;

public class DoorObj extends Entity {

    public DoorObj(GamePanel gp) {
        super(gp, "/objects/", List.of("door.png"));
        name = "Door";
        collisionEnabled = true;

        collisionBox.x = 0;
        collisionBox.y = 16;
        collisionBox.width = GamePanel.TILE_SIZE;
        collisionBox.height = 32;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
        setSpriteImages();
    }

    @Override
    protected void setSpriteImages() {
        down1 = images.get(0);
    }
}
