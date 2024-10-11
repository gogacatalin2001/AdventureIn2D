package main;

import lombok.Getter;
import object.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AssetHandler {
    private final GamePanel gamePanel;
    @Getter
    private List<SuperObject> objects = new ArrayList<>();

    public AssetHandler(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void setObjects() {

    }

    public void drawObjects(Graphics2D g2d) {
        objects.forEach(obj -> obj.draw(g2d, gamePanel));
    }

    public void deleteObject(int index) {
        objects.remove(index);
    }

}
