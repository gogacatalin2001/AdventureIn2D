package main;

import entity.Entity;
import tile.TileManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CollisionHandler {

    private CollisionHandler() {}

    public static void checkTileCollision(final Entity entity, final TileManager tileManager) {
        int entityLeftCollisionX = entity.getWorldX() + entity.getCollisionBox().x;
        int entityRightCollisionX = entity.getWorldX() + entity.getCollisionBox().x + entity.getCollisionBox().width;
        int entityTopCollisionY = entity.getWorldY() + entity.getCollisionBox().y;
        int entityBottomCollisionY = entity.getWorldY() + entity.getCollisionBox().y + entity.getCollisionBox().height;

        int entityLeftCol = entityLeftCollisionX / GamePanel.TILE_SIZE;
        int entityRightCol = entityRightCollisionX / GamePanel.TILE_SIZE;
        int entityTopRow = entityTopCollisionY / GamePanel.TILE_SIZE;
        int entityBottomRow = entityBottomCollisionY / GamePanel.TILE_SIZE;

        int tileNum1 = 0, tileNum2 = 0;

        switch (entity.getDirection()) {
            case UP -> {
                entityTopRow = (entityTopCollisionY - entity.getSpeed()) / GamePanel.TILE_SIZE;
                tileNum1 = tileManager.getTileNumber(entityLeftCol, entityTopRow);
                tileNum2 = tileManager.getTileNumber(entityRightCol, entityTopRow);
            }
            case DOWN -> {
                entityBottomRow = (entityBottomCollisionY + entity.getSpeed()) / GamePanel.TILE_SIZE;
                tileNum1 = tileManager.getTileNumber(entityLeftCol, entityBottomRow);
                tileNum2 = tileManager.getTileNumber(entityRightCol, entityBottomRow);
            }
            case LEFT -> {
                entityLeftCol = (entityLeftCollisionX - entity.getSpeed()) / GamePanel.TILE_SIZE;
                tileNum1 = tileManager.getTileNumber(entityLeftCol, entityTopRow);
                tileNum2 = tileManager.getTileNumber(entityLeftCol, entityBottomRow);
            }
            case RIGHT -> {
                entityRightCol = (entityLeftCollisionX + entity.getSpeed()) / GamePanel.TILE_SIZE;
                tileNum1 = tileManager.getTileNumber(entityRightCol, entityTopRow);
                tileNum2 = tileManager.getTileNumber(entityRightCol, entityBottomRow);
            }
        }

        entity.setCollisionDetected(tileManager.getTile(tileNum1).isCollisionEnabled() || tileManager.getTile(tileNum2).isCollisionEnabled());
    }

    public static int checkEntityCollision(final Entity entity, final List<Entity> targetEntities) {
        AtomicInteger targetIndex = new AtomicInteger(-1);
        targetEntities.forEach(target -> {
            boolean collisionDetected = checkCollision(entity, target);
            entity.setCollisionDetected(entity.isCollisionDetected() || collisionDetected);
            if (collisionDetected) {
                targetIndex.set(targetEntities.indexOf(target));
            }
        });
        return targetIndex.get();
    }

    public static boolean checkCollision(final Entity entity, final Entity target) {
        boolean collisionDetected = false;
        // Get entity collisionEnabled box position
        target.getCollisionBox().x += target.getWorldX();
        target.getCollisionBox().y += target.getWorldY();
        // Get entity.object collisionEnabled box position
        entity.getCollisionBox().x += entity.getWorldX();
        entity.getCollisionBox().y += entity.getWorldY();
        // Check the collisionEnabled
        switch (entity.getDirection()) {
            case UP -> entity.getCollisionBox().y -= entity.getSpeed();
            case DOWN -> entity.getCollisionBox().y += entity.getSpeed();
            case LEFT -> entity.getCollisionBox().x -= entity.getSpeed();
            case RIGHT -> entity.getCollisionBox().x += entity.getSpeed();
        }
        if (entity.getCollisionBox().intersects(target.getCollisionBox()) && !entity.equals(target)) {
            collisionDetected = true;
        }
        // Reset collisionEnabled box coordinates after collisionEnabled check for each direction
        target.getCollisionBox().x = target.getCollisionBoxDefaultX();
        target.getCollisionBox().y = target.getCollisionBoxDefaultY();
        entity.getCollisionBox().x = entity.getCollisionBoxDefaultX();
        entity.getCollisionBox().y = entity.getCollisionBoxDefaultY();

        return collisionDetected;
    }

}
