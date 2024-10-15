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
    private boolean enterPressed;

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
        switch (gamePanel.getGameState()) {
            case PLAY -> {
                switch (code) {
                    case KeyEvent.VK_W -> setUpPressed(true);
                    case KeyEvent.VK_A -> setLeftPressed(true);
                    case KeyEvent.VK_S -> setDownPressed(true);
                    case KeyEvent.VK_D -> setRightPressed(true);
                    case KeyEvent.VK_ESCAPE -> gamePanel.setGameState(GameState.PAUSE);
                    case KeyEvent.VK_ENTER -> setEnterPressed(true);
                }
            }
            case PAUSE -> {
                switch (code) {
                    case KeyEvent.VK_ESCAPE -> gamePanel.setGameState(GameState.PLAY);
                }
            }
            case DIALOGUE -> {
                switch (code) {
                    case KeyEvent.VK_ENTER -> gamePanel.setGameState(GameState.PLAY);
                }
            }
            case TITLE_SCREEN -> {
                switch (code) {
                    case KeyEvent.VK_UP -> gamePanel.getUi().changeCommand(-1);
                    case KeyEvent.VK_DOWN -> gamePanel.getUi().changeCommand(1);
                    case KeyEvent.VK_ENTER -> {
                        switch (gamePanel.getUi().getCurrentCommand()) {
                            case NEW_GAME -> {
                                gamePanel.setGameState(GameState.PLAY);
                                gamePanel.playMusic(0);
                            }
                            case LOAD_GAME -> {
                                // todo add game loading
                            }
                            case QUIT -> System.exit(0);
                        }
                    }
                }
            }

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
