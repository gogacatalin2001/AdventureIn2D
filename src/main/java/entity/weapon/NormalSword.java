package entity.weapon;

import entity.EntityHandler;
import main.GamePanel;
import util.ImageProperties;

import java.util.ArrayList;
import java.util.List;

public class NormalSword extends Weapon {

    public NormalSword(GamePanel gp, EntityHandler eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("sword_normal.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        name = "Normal Sword";
        damageValue = 1;
    }
}
