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
        mapTileNumber = new int[GamePanel.maxScreenCol][GamePanel.maxScreenRow];
        getTileImage();
        loadMap("/maps/map01.txt");
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        int row = 0, col = 0, x = 0, y = 0;

        while (col < GamePanel.maxScreenCol && row < GamePanel.maxScreenRow) {
            int tileNumber = mapTileNumber[col][row];
            g2d.drawImage(tiles.get(tileNumber).image, x, y, GamePanel.tileSize, GamePanel.tileSize, null);
            col++;
            x += GamePanel.tileSize;

            if (col == GamePanel.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += GamePanel.tileSize;
            }
        }
    }

    public void loadMap(String mapFilePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(mapFilePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            int col = 0, row = 0;
            while (col < GamePanel.maxScreenCol && row < GamePanel.maxScreenRow) {
                String line = bufferedReader.readLine();

                while (col < GamePanel.maxScreenCol) {
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
