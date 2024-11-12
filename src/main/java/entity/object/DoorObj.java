package entity.object;

import entity.Entity;
import entity.EntityManager;
import main.GamePanel;
import image.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class DoorObj extends Entity {

    public DoorObj(GamePanel gp, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("door.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        name = "Door";
        collisionEnabled = true;
        collisionBox.x = 0;
        collisionBox.y = 16;
        collisionBox.width = GamePanel.TILE_SIZE;
        collisionBox.height = 32;
        collisionBoxDefaultX = collisionBox.x;
        collisionBoxDefaultY = collisionBox.y;
    }

    @Override
    public BufferedImage getSpriteImage() {
        return images.getFirst();
    }
}
