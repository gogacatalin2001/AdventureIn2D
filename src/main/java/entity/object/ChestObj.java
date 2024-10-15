package entity.object;

import entity.Entity;
import main.GamePanel;

import java.util.List;

public class ChestObj extends Entity {

    public ChestObj(GamePanel gp) {
        super(gp, "/objects/", List.of("chest.png"));
        name = "Chest";
        setSpriteImages();
    }

    @Override
    protected void setSpriteImages() {
        down1 = images.get(0);
    }
}
