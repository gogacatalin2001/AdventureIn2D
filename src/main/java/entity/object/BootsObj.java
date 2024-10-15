package entity.object;

import entity.Entity;
import main.GamePanel;

import java.util.List;

public class BootsObj extends Entity {

    public BootsObj(GamePanel gp) {
        super(gp, "/objects/", List.of("boots.png"));
        name = "Boots";
        setSpriteImages();
    }

    @Override
    protected void setSpriteImages() {
        down1 = images.get(0);
    }
}
