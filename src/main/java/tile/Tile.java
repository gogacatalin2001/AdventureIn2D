package tile;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class Tile {
    private BufferedImage image;
    private boolean collisionEnabled = false;

}
