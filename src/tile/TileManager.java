package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/world01.txt");
    }

    private InputStream loadResource(String path) {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("Resource not found: " + path);
        }
        return is;
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(loadResource("/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(loadResource("/tiles/wall.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(loadResource("/tiles/water.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(loadResource("/tiles/earth.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(loadResource("/tiles/tree.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(loadResource("/tiles/sand.png"));

            System.out.println("Tiles loaded successfully");

        } catch (Exception e) {
            throw new RuntimeException("Tile image loading failed", e);
        }
    }

    public void loadMap(String filePath) {
        try {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(loadResource(filePath)));

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.split(" ");

                for (int col = 0; col < gp.maxWorldCol; col++) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                }
            }

            br.close();
            System.out.println("Map loaded successfully");

        } catch (Exception e) {
            throw new RuntimeException("Map loading failed", e);
        }
    }

    public void draw(Graphics2D g2) {
        int worldRow = 0;
        int worldCol = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol  * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX= worldX - gp.player.worldX+gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize  < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            }



            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;


            }
        }
    }
}