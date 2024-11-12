package entity;

import main.Drawable;
import image.ImageProperties;

import java.awt.image.BufferedImage;
import java.util.List;

public interface DrawableEntity extends Drawable {

    void loadImages(String basePath, List<ImageProperties> imageProperties);

    BufferedImage readImage(String basePath, String imageName, Integer width, Integer height);

    BufferedImage getSpriteImage();

}
