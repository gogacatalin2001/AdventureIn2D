package entity;

import entity.npc.OldManNPC;
import entity.object.DoorObj;
import lombok.Getter;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityHandler {
    private final GamePanel gamePanel;
    @Getter
    private final List<Entity> objects = new ArrayList<>();
    @Getter
    private final List<Entity> npcs = new ArrayList<>();

    public EntityHandler(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void setObject() {
        Entity door1 = new DoorObj(gamePanel);
        door1.setWorldX(GamePanel.TILE_SIZE * 21);
        door1.setWorldY(GamePanel.TILE_SIZE * 22);
        objects.add(door1);

        Entity door2 = new DoorObj(gamePanel);
        door2.setWorldX(GamePanel.TILE_SIZE * 23);
        door2.setWorldY(GamePanel.TILE_SIZE * 25);
        objects.add(door2);
    }

    public void drawObjects(Graphics2D g2d) {
        objects.forEach(obj -> obj.draw(g2d));
    }

    public void setNPC() {

        Entity oldMan = new OldManNPC(gamePanel);
        oldMan.setWorldX(GamePanel.TILE_SIZE * 21);
        oldMan.setWorldY(GamePanel.TILE_SIZE * 21);
        npcs.add(oldMan);

        Entity oldMan1 = new OldManNPC(gamePanel);
        oldMan1.setWorldX(GamePanel.TILE_SIZE * 21);
        oldMan1.setWorldY(GamePanel.TILE_SIZE * 21);
        npcs.add(oldMan1);

        Entity oldMan2 = new OldManNPC(gamePanel);
        oldMan2.setWorldX(GamePanel.TILE_SIZE * 21);
        oldMan2.setWorldY(GamePanel.TILE_SIZE * 21);
        npcs.add(oldMan2);
    }

    public Entity getNPC(int index) {
        return npcs.get(index);
    }

    public void drawNPCs(Graphics2D g2d) {
        npcs.forEach(npc-> npc.draw(g2d));
    }
}
