package main.event;

import entity.Direction;
import main.GamePanel;
import main.GameState;

public class EventHandler {
    private final GamePanel gamePanel;
    private final EventCollisionBox[][] eventCollisionBox;
    private int previousEventX;
    private int previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        eventCollisionBox = new EventCollisionBox[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];
        setEventCollisionBox();
    }

    public void checkEvent() {
        // Only trigger an event again when the player is 1 tile away from the {@link EventCollisionBox} position
        int xDistance = Math.abs(gamePanel.getPlayer().getWorldX() - previousEventX);
        int yDistance = Math.abs(gamePanel.getPlayer().getWorldY() - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > GamePanel.TILE_SIZE) {
            canTouchEvent = true;
        }

        if (canTouchEvent) {
            if (generateEvent(27, 16, Direction.ANY)) {
                damagePitEvent(27, 16, GameState.DIALOGUE);
            }
            if (generateEvent(23, 12, Direction.UP)) {
                healingPool(23, 12, GameState.DIALOGUE);
            }
        }
    }

    private void setEventCollisionBox() {
        int col = 0;
        int row = 0;
        while (col < GamePanel.MAX_WORLD_COL && row < GamePanel.MAX_WORLD_ROW) {
            eventCollisionBox[col][row] = new EventCollisionBox();
            eventCollisionBox[col][row].x = 23;
            eventCollisionBox[col][row].y = 23;
            eventCollisionBox[col][row].width = 2;
            eventCollisionBox[col][row].height = 2;
            eventCollisionBox[col][row].defaultX = eventCollisionBox[col][row].x;
            eventCollisionBox[col][row].defaultY = eventCollisionBox[col][row].y;

            col++;
            if (col == GamePanel.MAX_WORLD_COL) {
                row++;
                col = 0;
            }
        }
    }

    public boolean generateEvent(int eventCol, int eventRow, Direction requiredDirection) {
        boolean detectedPlayer = false;

        gamePanel.getPlayer().getCollisionBox().x = gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getCollisionBox().x;
        gamePanel.getPlayer().getCollisionBox().y = gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getCollisionBox().y;
        eventCollisionBox[eventCol][eventRow].x = eventCol * GamePanel.TILE_SIZE + eventCollisionBox[eventCol][eventRow].x;
        eventCollisionBox[eventCol][eventRow].y = eventRow * GamePanel.TILE_SIZE + eventCollisionBox[eventCol][eventRow].y;

        if (gamePanel.getPlayer().getCollisionBox().intersects(eventCollisionBox[eventCol][eventRow])) {
            if (gamePanel.getPlayer().getDirection().equals(requiredDirection) ||
                    requiredDirection.equals(Direction.ANY)) {
                detectedPlayer = true;
                previousEventX = gamePanel.getPlayer().getWorldX();
                previousEventY = gamePanel.getPlayer().getWorldY();
            }
        }

        gamePanel.getPlayer().getCollisionBox().x = gamePanel.getPlayer().getCollisionBoxDefaultX();
        gamePanel.getPlayer().getCollisionBox().y = gamePanel.getPlayer().getCollisionBoxDefaultY();
        eventCollisionBox[eventCol][eventRow].x = eventCollisionBox[eventCol][eventRow].defaultX;
        eventCollisionBox[eventCol][eventRow].y = eventCollisionBox[eventCol][eventRow].defaultY;

        return detectedPlayer;
    }

    private void damagePitEvent(int col, int row, GameState gameState) {
        if (!eventCollisionBox[col][row].eventTriggered) {
            gamePanel.setGameState(gameState);
            gamePanel.getUi().setCurrentDialogue("You fell into a pit!");
            gamePanel.getPlayer().setLife(gamePanel.getPlayer().getLife() - 1);
            canTouchEvent = false;
        }
    }

    private void healingPool(int col, int row, GameState gameState) {
        if (gamePanel.getKeyHandler().isEnterPressed() && !eventCollisionBox[col][row].eventTriggered) {
            gamePanel.setGameState(gameState);
            gamePanel.getUi().setCurrentDialogue("You drank water. Your life has been \nrestored!");
            gamePanel.getPlayer().setLife(gamePanel.getPlayer().maxLife);
        }
        gamePanel.getKeyHandler().setEnterPressed(false);
    }
}
