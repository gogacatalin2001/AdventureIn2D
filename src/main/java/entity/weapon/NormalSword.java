package entity.weapon;

import entity.EntityManager;
import main.GamePanel;
import sound.SoundManager;
import image.ImageProperties;

import java.util.ArrayList;
import java.util.List;

public class NormalSword extends Weapon {

    public NormalSword(GamePanel gp, EntityManager eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("sword_normal.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties, 4, 0, SoundManager.WEAPON_SWING_SOUND);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        name = "Normal Sword";
    }
}
