package main;

import lombok.Getter;
import lombok.Setter;
import main.ui.UIManager;
import sound.SoundManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Getter
public class KeyMouseHandler implements KeyListener, MouseListener {
    private final GamePanel gamePanel;
    private final SoundManager soundManager;
    private final UIManager uiManager;
    @Setter
    private boolean upPressed;
    @Setter
    private boolean downPressed;
    @Setter
    private boolean leftPressed;
    @Setter
    private boolean rightPressed;
    @Setter
    private boolean enterPressed;
    private boolean lmbPressed;

    public KeyMouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.soundManager = gamePanel.getSoundManager();
        this.uiManager = gamePanel.getUiManager();
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        // not used
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        int code = e.getKeyCode();
        switch (gamePanel.getGameState()) {
            case PLAY -> handlePlayState(code);
            case PAUSE -> handlePauseState(code);
            case DIALOGUE -> handleDialogueState(code);
            case TITLE_SCREEN -> handleTitleScreenState(code);
            case CHARACTER_SCREEN -> handleCharacterScreenState(code);
        }
    }

    private void handleCharacterScreenState(int code) {
        switch (code) {
            case KeyEvent.VK_C -> gamePanel.setGameState(GameState.PLAY);
            case KeyEvent.VK_LEFT -> moveInventoryCursor(Direction.LEFT);
            case KeyEvent.VK_RIGHT -> moveInventoryCursor(Direction.RIGHT);
            case KeyEvent.VK_UP -> moveInventoryCursor(Direction.UP);
            case KeyEvent.VK_DOWN -> moveInventoryCursor(Direction.DOWN);
        }
    }

    private void moveInventoryCursor(Direction direction) {
        soundManager.playSound(SoundManager.CURSOR_SOUND);
        switch (direction) {
            case UP -> uiManager.decreaseSlotRow();
            case DOWN -> uiManager.increaseSlotRow();
            case LEFT -> uiManager.decreaseSlotColumn();
            case RIGHT -> uiManager.increaseSlotColumn();
        }
    }

    private void handleTitleScreenState(int code) {
        switch (code) {
            case KeyEvent.VK_UP -> uiManager.changeMenuCommand(-1);
            case KeyEvent.VK_DOWN -> uiManager.changeMenuCommand(1);
            case KeyEvent.VK_ENTER -> uiManager.executeMenuCommand();
        }
    }

    private void handleDialogueState(int code) {
        switch (code) {
            case KeyEvent.VK_ENTER -> gamePanel.setGameState(GameState.PLAY);
        }
    }

    private void handlePauseState(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> gamePanel.setGameState(GameState.PLAY);
        }
    }

    private void handlePlayState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> setUpPressed(true);
            case KeyEvent.VK_A -> setLeftPressed(true);
            case KeyEvent.VK_S -> setDownPressed(true);
            case KeyEvent.VK_D -> setRightPressed(true);
            case KeyEvent.VK_ESCAPE -> gamePanel.setGameState(GameState.PAUSE);
            case KeyEvent.VK_ENTER -> setEnterPressed(true);
            case KeyEvent.VK_C -> gamePanel.setGameState(GameState.CHARACTER_SCREEN);
            case KeyEvent.VK_BACK_QUOTE -> gamePanel.setDebugging(!gamePanel.isDebugging());
            case KeyEvent.VK_L ->  gamePanel.loadMap(GameConfig.INITIAL_GAME_MAP);
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> setUpPressed(false);
            case KeyEvent.VK_A -> setLeftPressed(false);
            case KeyEvent.VK_S -> setDownPressed(false);
            case KeyEvent.VK_D -> setRightPressed(false);
            case KeyEvent.VK_ENTER -> setEnterPressed(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> lmbPressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> lmbPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
