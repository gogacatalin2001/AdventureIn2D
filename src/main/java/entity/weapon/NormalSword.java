package entity.weapon;

import main.GamePanel;
import sound.SoundManager;
import image.ImageProperties;

import java.util.ArrayList;
import java.util.List;

public class NormalSword extends Weapon {

    public NormalSword(GamePanel gp) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("sword_normal.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, "/objects/", imageProperties, 1, 0, SoundManager.WEAPON_SWING_SOUND);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        name = "Normal Sword";
    }
}
