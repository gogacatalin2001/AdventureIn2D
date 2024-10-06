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
        SuperObject key1 = new KeyObj();
        key1.setWorldX(23 * GamePanel.tileSize);
        key1.setWorldY(7 * GamePanel.tileSize);
        objects.add(key1);

        SuperObject key2 = new KeyObj();
        key2.setWorldX(23 * GamePanel.tileSize);
        key2.setWorldY(40 * GamePanel.tileSize);
        objects.add(key2);

        SuperObject key3 = new KeyObj();
        key3.setWorldX(38 * GamePanel.tileSize);
        key3.setWorldY(9 * GamePanel.tileSize);
        objects.add(key3);

        SuperObject door1 = new DoorObj();
        door1.setWorldX(12 * GamePanel.tileSize);
        door1.setWorldY(22 * GamePanel.tileSize);
        objects.add(door1);

        SuperObject door2 = new DoorObj();
        door2.setWorldX(8 * GamePanel.tileSize);
        door2.setWorldY(28 * GamePanel.tileSize);
        objects.add(door2);

        SuperObject door3 = new DoorObj();
        door3.setWorldX(10 * GamePanel.tileSize);
        door3.setWorldY(11 * GamePanel.tileSize);
        objects.add(door3);

        SuperObject chest = new ChestObj();
        chest.setWorldX(10 * GamePanel.tileSize);
        chest.setWorldY(7 * GamePanel.tileSize);
        objects.add(chest);

        SuperObject boots = new BootsObj();
        boots.setWorldX(37 * GamePanel.tileSize);
        boots.setWorldY(42 * GamePanel.tileSize);
        objects.add(boots);
    }

    public void drawObjects(Graphics2D g2d) {
        objects.forEach(obj -> obj.draw(g2d, gamePanel));
    }

    public void deleteObject(int index) {
        objects.remove(index);
    }

}
