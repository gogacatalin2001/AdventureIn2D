package entity.weapon;

import entity.Entity;
import entity.EntityHandler;
import lombok.Getter;
import main.GamePanel;
import util.ImageProperties;

import java.util.List;

@Getter
public abstract class Weapon extends Entity {

    protected int damageValue = 0;
    protected int defenseValue = 0;

    public Weapon(GamePanel gp, EntityHandler eh, String imageBasePath, List<ImageProperties> imageProperties) {
        super(gp, eh, imageBasePath, imageProperties);
    }

}
