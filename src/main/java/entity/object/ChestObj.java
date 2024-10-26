package entity.object;

import entity.Entity;
import entity.EntityHandler;
import main.GamePanel;
import util.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class ChestObj extends Entity {

    public ChestObj(GamePanel gp, EntityHandler eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("chest.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        name = "Chest";
    }

    @Override
    protected BufferedImage getSpriteImage() {
        return images.getFirst();
    }
}
