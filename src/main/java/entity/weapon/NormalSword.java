package entity.weapon;

import entity.EntityHandler;
import main.GamePanel;
import sound.SoundHandler;
import util.ImageProperties;

import java.util.ArrayList;
import java.util.List;

public class NormalSword extends Weapon {

    public NormalSword(GamePanel gp, EntityHandler eh) {
        List<ImageProperties> imageProperties = new ArrayList<>();
        imageProperties.add(new ImageProperties("sword_normal.png", GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
        super(gp, eh, "/objects/", imageProperties, 4, 0, SoundHandler.WEAPON_SWING_SOUND);
        setDefaultValues();
    }

    @Override
    protected void setDefaultValues() {
        name = "Normal Sword";
    }
}
