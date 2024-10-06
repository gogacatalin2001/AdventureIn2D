package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class BootsObj extends SuperObject {

    public BootsObj() {
        name = "Boots";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/boots.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
