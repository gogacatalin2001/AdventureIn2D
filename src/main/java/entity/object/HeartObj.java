package entity.object;

import entity.Entity;
import main.GamePanel;

import java.util.List;

public class HeartObj extends Entity {

    public HeartObj(GamePanel gp) {
        super(gp, "/objects/", List.of("heart_full.png", "heart_half.png", "heart_blank.png"));
        name = "Heart";
    }

}
