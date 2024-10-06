package entity;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
public class Entity {
    // SPRITE SETTINGS
    @Setter
    BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public static final int spriteUpdateSpeed = 12;
    int spriteCounter = 0;
    int spriteNumber = 1;
    // MOVEMENT
    Rectangle collisionBox;
    int collisionBoxDefaultX, collisionBoxDefaultY;
    @Setter
    boolean collisionDetected = false;
    @Setter
    int worldX, worldY; // Position of the entity in the world
    @Setter
    int speed;
    @Setter
    Direction direction;
}
