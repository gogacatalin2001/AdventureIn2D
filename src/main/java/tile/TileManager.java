package tile;

import lombok.Getter;
import lombok.Setter;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TileManager {

    GamePanel gamePanel;
    List<Tile> tiles;
    int[][] mapTileNumber;

    public TileManager(GamePanel gp) {
        this.gamePanel = gp;
        this.tiles = new ArrayList<>();
        mapTileNumber = new int[GamePanel.maxWorldCol][GamePanel.maxWorldRow];
        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage() {
        try {
            Tile grassTile = new Tile();
            grassTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass00.png"));
            tiles.add(grassTile);

            Tile wallTile = new Tile();
            wallTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tiles.add(wallTile);

            Tile waterTile = new Tile();
            waterTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/water00.png"));
            tiles.add(waterTile);

            Tile earthTile = new Tile();
            earthTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
            tiles.add(earthTile);

            Tile treeTile = new Tile();
            treeTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tiles.add(treeTile);

            Tile sandTile = new Tile();
            sandTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
            tiles.add(sandTile);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                g2d.drawImage(tiles.get(tileNumber).image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
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
