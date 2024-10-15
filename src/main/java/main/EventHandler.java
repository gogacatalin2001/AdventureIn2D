package main;

import entity.Direction;

import java.awt.*;

public class EventHandler {
    private final GamePanel gamePanel;
    int eventRectDefaultX, eventRectDefaultY;
    private final Rectangle eventRect;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        eventRect = new Rectangle();
        eventRect.x = GamePanel.TILE_SIZE / 2 - 2; // Place the rectangle in the center of the tile
        eventRect.y = GamePanel.TILE_SIZE / 2 - 2;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {
        if (generateEvent(27, 16, Direction.RIGHT)) {
            damagePitEvent(GameState.DIALOGUE);
        }
        if (generateEvent(23, 12, Direction.UP)) {
            healingPool(GameState.DIALOGUE);
        }
    }

    public boolean generateEvent(int eventCol, int eventRow, Direction requiredDirection) {
        boolean detectedPlayer = false;

        gamePanel.getPlayer().getCollisionBox().x = gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getCollisionBox().x;
        gamePanel.getPlayer().getCollisionBox().y = gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getCollisionBox().y;
        eventRect.x = eventCol * GamePanel.TILE_SIZE + eventRect.x;
        eventRect.y = eventRow * GamePanel.TILE_SIZE + eventRect.y;

        if (gamePanel.getPlayer().getCollisionBox().intersects(eventRect)) {
            if (gamePanel.getPlayer().getDirection().equals(requiredDirection) ||
                    gamePanel.getPlayer().getDirection().equals(Direction.ANY)) {
                detectedPlayer = true;
            }
        }

        gamePanel.getPlayer().getCollisionBox().x = gamePanel.getPlayer().getCollisionBoxDefaultX();
        gamePanel.getPlayer().getCollisionBox().y = gamePanel.getPlayer().getCollisionBoxDefaultY();
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return detectedPlayer;
    }

    private void damagePitEvent(GameState gameState) {
        gamePanel.setGameState(gameState);
        gamePanel.getUi().setCurrentDialogue("You fell into a pit!");
        gamePanel.getPlayer().setLife(gamePanel.getPlayer().getLife() - 1);

    }

    private void healingPool(GameState gameState) {
        if (gamePanel.getKeyHandler().isEnterPressed()) {
            gamePanel.setGameState(gameState);
            gamePanel.getUi().setCurrentDialogue("You drank water. Your life has been \nrestored!");
            gamePanel.getPlayer().setLife(gamePanel.getPlayer().maxLife);
        }
        gamePanel.getKeyHandler().setEnterPressed(false);
    }
}
