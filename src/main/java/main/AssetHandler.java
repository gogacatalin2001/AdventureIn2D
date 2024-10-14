package main;

import entity.*;
import lombok.Getter;
import object.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AssetHandler {
    private final GamePanel gamePanel;
    @Getter
    private final List<SuperObject> objects = new ArrayList<>();
    @Getter
    private final List<Entity> npcs = new ArrayList<>();

    public AssetHandler(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void setObjects() {

    }

    public SuperObject getObject(int index) {
        return objects.get(index);
    }

    public void drawObjects(Graphics2D g2d) {
        objects.forEach(obj -> obj.draw(g2d, gamePanel));
    }

    public void setNPC() {

        Entity oldMan = new OldManNPC(gamePanel);
        oldMan.setWorldX(GamePanel.TILE_SIZE * 21);
        oldMan.setWorldY(GamePanel.TILE_SIZE * 21);
        npcs.add(oldMan);
    }

    public Entity getNPC(int index) {
        return npcs.get(index);
    }

    public void drawNPCs(Graphics2D g2d) {
        npcs.forEach(npc-> npc.draw(g2d));
    }
}
