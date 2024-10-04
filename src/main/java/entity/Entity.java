package entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class Entity {

    final int spriteUpdateSpeed = 12;
    int x, y;
    int speed;
    int spriteCounter = 0;
    int spriteNumber = 1;

    BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    Direction direction;

}
