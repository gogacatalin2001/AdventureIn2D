package object;

import lombok.Getter;
import lombok.Setter;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
public class SuperObject {
    BufferedImage image;
    String name;
    @Setter
    boolean collision = false;
    @Setter
    int worldX, worldY;

    public void draw(Graphics2D g2d, GamePanel gamePanel) {
        int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
        int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

        if (worldX + GamePanel.tileSize > gamePanel.getPlayer().getWorldX() - gamePanel.getPlayer().getScreenX() &&
                worldX - GamePanel.tileSize < gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX() &&
                worldY + GamePanel.tileSize > gamePanel.getPlayer().getWorldY() - gamePanel.getPlayer().getScreenY() &&
                worldY - GamePanel.tileSize < gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY()
        ) {
            g2d.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
        }
    }
}
