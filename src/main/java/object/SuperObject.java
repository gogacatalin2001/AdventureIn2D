package object;

import lombok.Getter;
import lombok.Setter;
import main.GamePanel;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Getter
public class SuperObject {
    BufferedImage image;
    String name;
    @Setter
    boolean collisionEnabled = false;
    @Setter
    int worldX, worldY;
    Rectangle collisionBox = new Rectangle(0, 0, GamePanel.tileSize, GamePanel.tileSize);
    int collisionBoxDefaultX = 0;
    int collisionBoxDefaultY = 0;

    public SuperObject(String imageName, String objectName) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/objects/" + imageName));
            this.image = ImageScalingUtil.scaleImage(img, GamePanel.tileSize, GamePanel.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.name = objectName;
    }

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
