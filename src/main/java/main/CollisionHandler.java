package main;

import entity.Entity;
import tile.TileManager;

public class CollisionHandler {
    private final GamePanel gamePanel;
    private final TileManager tileManager;

    public CollisionHandler(GamePanel gp, TileManager tm) {
        this.gamePanel = gp;
        this.tileManager = tm;
    }

    public boolean checkTileCollision(Entity entity) {
        int entityLeftCollisionX = entity.getWorldX() + entity.getCollisionBox().x;
        int entityRightCollisionX = entity.getWorldX() + entity.getCollisionBox().x + entity.getCollisionBox().width;
        int entityTopCollisionY = entity.getWorldY() + entity.getCollisionBox().y;
        int entityBottomCollisionY = entity.getWorldY() + entity.getCollisionBox().y + entity.getCollisionBox().height;

        int entityLeftCol = entityLeftCollisionX / GamePanel.tileSize;
        int entityRightCol = entityRightCollisionX / GamePanel.tileSize;
        int entityTopRow = entityTopCollisionY / GamePanel.tileSize;
        int entityBottomRow = entityBottomCollisionY / GamePanel.tileSize;

        int tileNum1 = 0, tileNum2 = 0;

        switch (entity.getDirection()) {
            case UP -> {
                entityTopRow = (entityTopCollisionY - entity.getSpeed()) / GamePanel.tileSize;
                tileNum1 = tileManager.getTileNumber(entityLeftCol, entityTopRow);
                tileNum2 = tileManager.getTileNumber(entityRightCol, entityTopRow);
            }
            case DOWN -> {
                entityBottomRow = (entityBottomCollisionY + entity.getSpeed()) / GamePanel.tileSize;
                tileNum1 = tileManager.getTileNumber(entityLeftCol, entityBottomRow);
                tileNum2 = tileManager.getTileNumber(entityRightCol, entityBottomRow);
            }
            case LEFT ->{
                entityLeftCol = (entityLeftCollisionX - entity.getSpeed()) / GamePanel.tileSize;
                tileNum1 = tileManager.getTileNumber(entityLeftCol, entityTopRow);
                tileNum2 = tileManager.getTileNumber(entityLeftCol, entityBottomRow);
            }
            case RIGHT -> {
                entityRightCol = (entityLeftCollisionX + entity.getSpeed()) / GamePanel.tileSize;
                tileNum1 = tileManager.getTileNumber(entityRightCol, entityTopRow);
                tileNum2 = tileManager.getTileNumber(entityRightCol, entityBottomRow);
            }
        }

        return tileManager.getTile(tileNum1).isCollisionEnabled() || tileManager.getTile(tileNum2).isCollisionEnabled();
    }
}
