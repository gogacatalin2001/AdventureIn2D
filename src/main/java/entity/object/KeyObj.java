package entity.object;


import entity.Entity;
import main.GamePanel;

import java.util.List;

public class KeyObj extends Entity {

    public KeyObj(GamePanel gp) {
        super(gp, "/objects/", List.of("key.png"));
        name = "Key";
        setSpriteImages();
    }

    @Override
    protected void setSpriteImages() {
        down1 = images.get(0);
    }
}
