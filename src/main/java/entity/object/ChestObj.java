package entity.object;

import entity.Entity;
import main.GamePanel;
import image.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

public class ChestObj extends Entity {

    public ChestObj(GamePanel gp) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("chest.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp,  "/objects/", imageProperties);
        name = "Chest";
    }

    @Override
    public BufferedImage getSpriteImage() {
        return images.getFirst();
    }
}
