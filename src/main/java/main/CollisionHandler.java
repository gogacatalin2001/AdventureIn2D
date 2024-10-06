package main;

import entity.Entity;
import object.SuperObject;
import tile.TileManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CollisionHandler {
    private final GamePanel gamePanel;
    private final TileManager tileManager;
    private final AssetHandler assetHandler;

    public CollisionHandler(GamePanel gp, TileManager tm, AssetHandler ah) {
        this.gamePanel = gp;
        this.tileManager = tm;
        this.assetHandler = ah;
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

    public int checkObjectCollision(Entity entity, boolean isPlayer) {
        AtomicInteger objectIndex = new AtomicInteger(-1);
        List<SuperObject> objects = assetHandler.getObjects();
        objects.forEach(object -> {
            // Get entity collisionEnabled box position
            entity.getCollisionBox().x += entity.getWorldX();
            entity.getCollisionBox().y += entity.getWorldY();
            // Get object collisionEnabled box position
            object.getCollisionBox().x += object.getWorldX();
            object.getCollisionBox().y += object.getWorldY();
            // Check the collisionEnabled
            switch (entity.getDirection()) {
                case UP -> {
                    entity.getCollisionBox().y -= entity.getSpeed();
                    if (entity.getCollisionBox().intersects(object.getCollisionBox())) {
                        if (object.isCollisionEnabled()) {
                            entity.setCollisionDetected(true);
                        }
                        if (isPlayer) {
                            objectIndex.set(objects.indexOf(object));
                        }
                    }
                }
                case DOWN -> {
                    entity.getCollisionBox().y += entity.getSpeed();
                    if (entity.getCollisionBox().intersects(object.getCollisionBox())) {
                        if (object.isCollisionEnabled()) {
                            entity.setCollisionDetected(true);
                        }
                        if (isPlayer) {
                            objectIndex.set(objects.indexOf(object));
                        }
                    }
                }
                case LEFT -> {
                    entity.getCollisionBox().x -= entity.getSpeed();
                    if (entity.getCollisionBox().intersects(object.getCollisionBox())) {
                        if (object.isCollisionEnabled()) {
                            entity.setCollisionDetected(true);
                        }
                        if (isPlayer) {
                            objectIndex.set(objects.indexOf(object));
                        }
                    }
                }
                case RIGHT -> {
                    entity.getCollisionBox().x += entity.getSpeed();
                    if (entity.getCollisionBox().intersects(object.getCollisionBox())) {
                        if (object.isCollisionEnabled()) {
                            entity.setCollisionDetected(true);
                        }
                        if (isPlayer) {
                            objectIndex.set(objects.indexOf(object));
                        }
                    }
                }
            }
            // Reset collisionEnabled box coordinates after collisionEnabled check for each direction
            entity.getCollisionBox().x = entity.getCollisionBoxDefaultX();
            entity.getCollisionBox().y = entity.getCollisionBoxDefaultY();
            object.getCollisionBox().x = object.getCollisionBoxDefaultX();
            object.getCollisionBox().y = object.getCollisionBoxDefaultY();
        });
        System.out.println(objectIndex.get());
        return objectIndex.get();
    }
}
