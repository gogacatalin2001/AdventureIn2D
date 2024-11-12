package entity.weapon;

import entity.EntityManager;
import main.GamePanel;
import sound.SoundManager;
import image.ImageProperties;

import java.util.ArrayList;
import java.util.List;

public class WoodenShield extends Weapon {
    public WoodenShield(GamePanel gp, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("shield_wood.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties, 0, 1, SoundManager.NO_SOUND);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        name = "Wooden Shield";
        defenseValue = 1;
    }
}
