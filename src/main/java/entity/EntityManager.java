package entity;

import entity.monster.GreenSlimeMonster;
import entity.npc.OldManNPC;
import entity.object.DoorObj;
import lombok.Getter;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private final GamePanel gamePanel;
    @Getter
    private final List<Entity> objects = new ArrayList<>();
    @Getter
    private final List<Entity> npcs = new ArrayList<>();
    @Getter
    private final List<Entity> monsters = new ArrayList<>();

    public EntityManager(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void setObject() {
        Entity door1 = new DoorObj(gamePanel, this);
        door1.setWorldX(GamePanel.TILE_SIZE * 21);
        door1.setWorldY(GamePanel.TILE_SIZE * 22);
        objects.add(door1);

    }

    public void drawObjects(final Graphics2D g2d) {
        objects.forEach(obj -> obj.draw(g2d));
    }

    public void setNPC() {

//        Entity oldMan = new OldManNPC(gamePanel);
//        oldMan.setWorldX(GamePanel.TILE_SIZE * 21);
//        oldMan.setWorldY(GamePanel.TILE_SIZE * 21);
//        npcs.add(oldMan);

        Entity oldMan = new OldManNPC(gamePanel, this);
        oldMan.setWorldX(GamePanel.TILE_SIZE * 9);
        oldMan.setWorldY(GamePanel.TILE_SIZE * 10);
        npcs.add(oldMan);
    }

    public Entity getNPC(final int index) {
        return npcs.get(index);
    }

    public void drawNPCs(final Graphics2D g2d) {
        npcs.forEach(npc -> npc.draw(g2d));
    }

    public void setMonsters() {
//        Entity slime1 = new GreenSlimeMonster(gamePanel);
//        slime1.setWorldX(GamePanel.TILE_SIZE * 23);
//        slime1.setWorldY(GamePanel.TILE_SIZE * 36);
//        monsters.add(slime1);
//
//        Entity slime2 = new GreenSlimeMonster(gamePanel);
//        slime2.setWorldX(GamePanel.TILE_SIZE * 23);
//        slime2.setWorldY(GamePanel.TILE_SIZE * 37);
//        monsters.add(slime2);

        Entity slime1 = new GreenSlimeMonster(gamePanel, this);
        slime1.setWorldX(GamePanel.TILE_SIZE * 11);
        slime1.setWorldY(GamePanel.TILE_SIZE * 10);
        monsters.add(slime1);

        Entity slime2 = new GreenSlimeMonster(gamePanel, this);
        slime2.setWorldX(GamePanel.TILE_SIZE * 10);
        slime2.setWorldY(GamePanel.TILE_SIZE * 10);
        monsters.add(slime2);
    }

    public void drawMonsters(final Graphics2D g2d) {
        monsters.forEach(monster -> {
            if (monster != null) {
                monster.draw(g2d);
            }
        });
    }

    public void removeEntity(final Entity entity, final List<Entity> entities) {
        int index = entities.indexOf(entity);
        monsters.set(index, null);
    }
}