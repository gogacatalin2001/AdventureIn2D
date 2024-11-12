package entity.object;

import entity.Entity;
import entity.EntityManager;
import lombok.Getter;
import main.GamePanel;
import image.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.*;

@Getter
public class HeartObj extends Entity {

    private final BufferedImage full, half, empty;

    public HeartObj(GamePanel gp, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("heart_full.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("heart_half.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        imageProperties.add(new ImageProperties("heart_blank.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        full = images.get(0);
        half = images.get(1);
        empty = images.get(2);
        name = "Heart";
    }

    @Override
    public BufferedImage getSpriteImage() {
        return full;
    }
}
