package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class DoorObj extends SuperObject {

    public DoorObj() {
        name = "Door";
        collisionEnabled = true;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
