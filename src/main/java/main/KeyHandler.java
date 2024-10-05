package main;

import lombok.Getter;
import lombok.Setter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    @Getter
    @Setter
    private boolean upPressed;
    @Getter
    @Setter
    private boolean downPressed;
    @Getter
    @Setter
    private boolean leftPressed;
    @Getter
    @Setter
    private boolean rightPressed;

    private GamePanel gamePanel;

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
            case KeyEvent.VK_UP -> {
                gamePanel.zoomInOut(1);
            }
            case KeyEvent.VK_DOWN -> {
                gamePanel.zoomInOut(-1);
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
