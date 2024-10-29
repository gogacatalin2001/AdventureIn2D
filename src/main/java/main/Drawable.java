package main;

import util.ImageProperties;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public interface Drawable {

    void loadImages(String basePath, List<ImageProperties> imageProperties);

    BufferedImage readImage(String basePath, String imageName, Integer width, Integer height);

    BufferedImage getSpriteImage();

    void draw(Graphics2D g2d);
}
