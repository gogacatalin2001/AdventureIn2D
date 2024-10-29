package entity.weapon;

import entity.EntityHandler;
import main.GamePanel;
import util.ImageProperties;

import java.util.ArrayList;
import java.util.List;

public class WoodenShield extends Weapon {
    public WoodenShield(GamePanel gp, EntityHandler eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("shield_wood.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        name = "Wooden Shield";
        defenseValue = 1;
    }
}
