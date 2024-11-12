package entity.object;


import entity.Entity;
import entity.EntityManager;
import main.GamePanel;
import image.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class KeyObj extends Entity {

    public KeyObj(GamePanel gp, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add( new ImageProperties("key.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        name = "Key";
    }

    @Override
    public BufferedImage getSpriteImage() {
        return images.getFirst();
    }
}
