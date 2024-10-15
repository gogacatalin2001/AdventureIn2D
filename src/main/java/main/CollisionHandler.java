package main;

import entity.Entity;
import entity.EntityHandler;
import tile.TileManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CollisionHandler {
    private final TileManager tileManager;
    private final EntityHandler entityHandler;

    public CollisionHandler(TileManager tm, EntityHandler ah) {
        this.tileManager = tm;
        this.entityHandler = ah;
    }

    public void checkTileCollision(Entity entity) {
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

    public int checkObjectCollision(Entity entity, boolean isPlayer) {
        AtomicInteger objectIndex = new AtomicInteger(-1);
        List<Entity> objects = entityHandler.getObjects();
        objects.forEach(object -> {
            // Get entity collisionEnabled box position
            entity.getCollisionBox().x += entity.getWorldX();
            entity.getCollisionBox().y += entity.getWorldY();
            // Get entity.object collisionEnabled box position
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

        return objectIndex.get();
    }

    public int checkEntityCollision(Entity entity) {
        AtomicInteger entityIndex = new AtomicInteger(-1);
        List<Entity> targetEntities = entityHandler.getNpcs();
        targetEntities.forEach(target -> {
            // Get entity collisionEnabled box position
            entity.getCollisionBox().x += entity.getWorldX();
            entity.getCollisionBox().y += entity.getWorldY();
            // Get entity.object collisionEnabled box position
            target.getCollisionBox().x += target.getWorldX();
            target.getCollisionBox().y += target.getWorldY();
            // Check the collisionEnabled
            switch (entity.getDirection()) {
                case UP -> {
                    entity.getCollisionBox().y -= entity.getSpeed();
                    if (entity.getCollisionBox().intersects(target.getCollisionBox())) {
                        entity.setCollisionDetected(true);
                        entityIndex.set(targetEntities.indexOf(target));
                    }
                }
                case DOWN -> {
                    entity.getCollisionBox().y += entity.getSpeed();
                    if (entity.getCollisionBox().intersects(target.getCollisionBox())) {
                        entity.setCollisionDetected(true);
                        entityIndex.set(targetEntities.indexOf(target));
                    }
                }
                case LEFT -> {
                    entity.getCollisionBox().x -= entity.getSpeed();
                    if (entity.getCollisionBox().intersects(target.getCollisionBox())) {
                        entity.setCollisionDetected(true);
                        entityIndex.set(targetEntities.indexOf(target));
                    }
                }
                case RIGHT -> {
                    entity.getCollisionBox().x += entity.getSpeed();
                    if (entity.getCollisionBox().intersects(target.getCollisionBox())) {
                        entity.setCollisionDetected(true);
                        entityIndex.set(targetEntities.indexOf(target));
                    }
                }
            }
            // Reset collisionEnabled box coordinates after collisionEnabled check for each direction
            entity.getCollisionBox().x = entity.getCollisionBoxDefaultX();
            entity.getCollisionBox().y = entity.getCollisionBoxDefaultY();
            target.getCollisionBox().x = target.getCollisionBoxDefaultX();
            target.getCollisionBox().y = target.getCollisionBoxDefaultY();
        });
        return entityIndex.get();
    }

    public void checkPlayerCollision(Entity player, Entity entity) {
        // Get entity collisionEnabled box position
        entity.getCollisionBox().x += entity.getWorldX();
        entity.getCollisionBox().y += entity.getWorldY();
        // Get entity.object collisionEnabled box position
        player.getCollisionBox().x += player.getWorldX();
        player.getCollisionBox().y += player.getWorldY();
        // Check the collisionEnabled
        switch (entity.getDirection()) {
            case UP -> {
                entity.getCollisionBox().y -= entity.getSpeed();
                if (entity.getCollisionBox().intersects(player.getCollisionBox())) {
                    entity.setCollisionDetected(true);
                }
            }
            case DOWN -> {
                entity.getCollisionBox().y += entity.getSpeed();
                if (entity.getCollisionBox().intersects(player.getCollisionBox())) {
                    entity.setCollisionDetected(true);
                }
            }
            case LEFT -> {
                entity.getCollisionBox().x -= entity.getSpeed();
                if (entity.getCollisionBox().intersects(player.getCollisionBox())) {
                    entity.setCollisionDetected(true);
                }
            }
            case RIGHT -> {
                entity.getCollisionBox().x += entity.getSpeed();
                if (entity.getCollisionBox().intersects(player.getCollisionBox())) {
                    entity.setCollisionDetected(true);
                }
            }
        }
        // Reset collisionEnabled box coordinates after collisionEnabled check for each direction
        entity.getCollisionBox().x = entity.getCollisionBoxDefaultX();
        entity.getCollisionBox().y = entity.getCollisionBoxDefaultY();
        player.getCollisionBox().x = player.getCollisionBoxDefaultX();
        player.getCollisionBox().y = player.getCollisionBoxDefaultY();
    }
}
