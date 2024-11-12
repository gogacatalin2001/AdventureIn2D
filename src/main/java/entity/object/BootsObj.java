package entity.object;

import entity.Entity;
import entity.EntityManager;
import main.GamePanel;
import image.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class BootsObj extends Entity {

    public BootsObj(GamePanel gp, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("boots.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        name = "Boots";
    }

    @Override
    public BufferedImage getSpriteImage() {
        return images.getFirst();
    }
}
