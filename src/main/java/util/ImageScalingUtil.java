package util;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageScalingUtil {

    public static BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight,null);
        g2d.dispose();

        return scaledImage;
    }
}
