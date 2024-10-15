package object;

import lombok.Getter;
import lombok.Setter;
import main.GamePanel;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class SuperObject {
    List<BufferedImage> images = new ArrayList<>();
    BufferedImage displayImage;
    String name;
    @Setter
    boolean collisionEnabled = false;
    @Setter
    int worldX, worldY;
    Rectangle collisionBox = new Rectangle(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    int collisionBoxDefaultX = 0;
    int collisionBoxDefaultY = 0;

    public SuperObject(List<String> imageNames, String objectName) {
        imageNames.forEach(imageName -> {
            try {
                BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/objects/" + imageName));
                images.add(ImageScalingUtil.scaleImage(img, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.displayImage = images.get(0);
        this.name = objectName;
    }

    public void draw(Graphics2D g2d, GamePanel gamePanel) {
        int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
        int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

        if (gamePanel.isWhitinScreenBoundaries(worldX, worldY)) {
            g2d.drawImage(displayImage, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        }
    }

    public void setDisplayImage(int index) {
        displayImage = images.get(index);
    }

    public BufferedImage getImage(int index) {
        return images.get(index);
    }
}
