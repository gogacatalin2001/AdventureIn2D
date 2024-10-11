package main;

import lombok.Getter;
import lombok.Setter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Getter
@Setter
public class KeyHandler implements KeyListener {
    private final GamePanel gamePanel;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> {
                setUpPressed(true);
            }
            case KeyEvent.VK_A -> {
                setLeftPressed(true);
            }
            case KeyEvent.VK_S -> {
                setDownPressed(true);
            }
            case KeyEvent.VK_D -> {
                setRightPressed(true);
            }
            case KeyEvent.VK_ESCAPE -> {
                if (gamePanel.getGameState() == GameState.PLAY) {
                    gamePanel.setGameState(GameState.PAUSE);
                } else {
                    gamePanel.setGameState(GameState.PLAY);
                }
            }
            default -> {}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> {
                setUpPressed(false);
            }
            case KeyEvent.VK_A -> {
                setLeftPressed(false);
            }
            case KeyEvent.VK_S -> {
                setDownPressed(false);
            }
            case KeyEvent.VK_D -> {
                setRightPressed(false);
            }
            default -> {
            }
        }
    }
}
