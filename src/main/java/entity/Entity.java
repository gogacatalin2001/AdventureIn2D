package entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class Entity {
    final int spriteUpdateSpeed = 12;
    int spriteCounter = 0;
    int spriteNumber = 1;

    // Position of the entity in the world
    int worldX, worldY;
    int speed;

    BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    Direction direction;

}
