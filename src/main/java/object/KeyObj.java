package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class KeyObj extends SuperObject {

    public KeyObj() {
        name = "Key";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
