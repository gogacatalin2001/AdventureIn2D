package entity.weapon;

import entity.Entity;
import entity.EntityHandler;
import lombok.Getter;
import main.GamePanel;
import util.ImageProperties;

import java.util.List;

@Getter
public abstract class Weapon extends Entity {

    protected int damageValue;
    protected int defenseValue;
    protected String sound;

    public Weapon(GamePanel gp, EntityHandler eh, String imageBasePath, List<ImageProperties> imageProperties, int damageValue, int defenseValue, String sound) {
        super(gp, eh, imageBasePath, imageProperties);
        this.damageValue = damageValue;
        this.defenseValue = defenseValue;
        this.sound = sound;
    }

}
