package tile;

import main.GamePanel;
import util.ImageScalingUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    private final GamePanel gamePanel;
    private final List<Tile> tiles;
    private final int[][] mapTileNumber;

    public TileManager(GamePanel gp) {
        this.gamePanel = gp;
        this.tiles = new ArrayList<>();
        mapTileNumber = new int[GamePanel.maxWorldCol][GamePanel.maxWorldRow];
        loadTileImages();
        loadMap("/maps/worldV2.txt");
    }

    /**
     * Loads tile images and populates the {@link #tiles} field.
     * <p>
     *     First 10 positions of the list are NOT used (map file contains
     *     only 2-digit numbers for better readability).
     * <p>
     *     The index of a tile is used in {@link #draw(Graphics2D) drawing} the map.
     */
    public void loadTileImages() {
        // NOT USED
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        createTile("grass00.png", false);
        // Tile numbers start from 10
        createTile("grass00.png", false);
        createTile("grass01.png", false);
        createTile("water00.png", true);
        createTile("water01.png", true);
        createTile("water02.png", true);
        createTile("water03.png", true);
        createTile("water04.png", true);
        createTile("water05.png", true);
        createTile("water06.png", true);
        createTile("water07.png", true);
        createTile("water08.png", true);
        createTile("water09.png", true);
        createTile("water10.png", true);
        createTile("water11.png", true);
        createTile("water12.png", true);
        createTile("water13.png", true);
        createTile("road00.png", false);
        createTile("road01.png", false);
        createTile("road02.png", false);
        createTile("road03.png", false);
        createTile("road04.png", false);
        createTile("road05.png", false);
        createTile("road06.png", false);
        createTile("road07.png", false);
        createTile("road08.png", false);
        createTile("road09.png", false);
        createTile("road10.png", false);
        createTile("road11.png", false);
        createTile("road12.png", false);
        createTile("earth.png", false);
        createTile("wall.png", true);
        createTile("tree.png", true);
    }

    private void createTile(String imageFileName, boolean collisionEnabled) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageFileName));
            BufferedImage scaledImage = ImageScalingUtil.scaleImage(image, GamePanel.tileSize, GamePanel.tileSize);
            Tile tile = new Tile();
            tile.setImage(scaledImage);
            tile.setCollisionEnabled(collisionEnabled);
            tiles.add(tile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTileNumber(int x, int y) {
        return mapTileNumber[x][y];
    }

    public Tile getTile(int tileNumber) {
        return tiles.get(tileNumber);
    }

    public void draw(Graphics2D g2d) {
        int worldRow = 0;
        int worldCol = 0;

        while (worldCol < GamePanel.maxWorldCol && worldRow < GamePanel.maxWorldRow) {
            // Tile number from the map file
            int tileNumber = mapTileNumber[worldCol][worldRow];
            // Coordinates of the tile on the world map
            int worldX = worldCol * GamePanel.tileSize;
            int worldY = worldRow * GamePanel.tileSize;
            // Coordinates of the tile on the screen
            // Player is always in the center of the screen
            int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
            int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

            if (worldX + GamePanel.tileSize > gamePanel.getPlayer().getWorldX() - gamePanel.getPlayer().getScreenX() &&
                    worldX - GamePanel.tileSize < gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX() &&
                    worldY + GamePanel.tileSize > gamePanel.getPlayer().getWorldY() - gamePanel.getPlayer().getScreenY() &&
                    worldY - GamePanel.tileSize < gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY()
            ) {
                g2d.drawImage(tiles.get(tileNumber).getImage(), screenX, screenY, null);
            }

            worldCol++;
            if (worldCol == GamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void loadMap(String mapFilePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(mapFilePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            int col = 0, row = 0;
            while (col < GamePanel.maxWorldCol && row < GamePanel.maxWorldRow) {
                String line = bufferedReader.readLine();

                while (col < GamePanel.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int tileNumber = Integer.parseInt(numbers[col]);
                    mapTileNumber[col][row] = tileNumber;
                    col++;
                }
                col = 0;
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
