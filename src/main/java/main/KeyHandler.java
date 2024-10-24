package main;

import lombok.Getter;
import lombok.Setter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Getter
@Setter
public class KeyHandler implements KeyListener, MouseListener {
    private final GamePanel gamePanel;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean enterPressed;
    private boolean lmbPressed;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        // not used
    }

    @Override
    public void keyPressed(final KeyEvent e) {
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
            case MouseEvent.BUTTON1 -> setLmbPressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> setLmbPressed(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
