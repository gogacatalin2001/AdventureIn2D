package entity.object;

import entity.Entity;
import main.GamePanel;
import util.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class BootsObj extends Entity {

    public BootsObj(GamePanel gp) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("boots.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, "/objects/", imageProperties);
        name = "Boots";
    }

    @Override
    protected BufferedImage getSpriteImage() {
        return images.getFirst();
    }
}
