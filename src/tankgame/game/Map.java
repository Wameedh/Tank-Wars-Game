package tankgame.game;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import tankgame.GameConstants;
import tankgame.game.Stationary.*;

public class Map {

    private BufferedImage bg;
    private BufferedImage unbreakableWall;
    private BufferedImage breakableWall;
    private BufferedImage extraLife;
    private BufferedImage speedBoost;


    private ArrayList<Wall> allWalls = new ArrayList<>();
    private ArrayList<Stationary> allPowers = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> map = new ArrayList<>();

    public Map(BufferedImage bg, BufferedImage unbreakableWall, BufferedImage breakableWall, BufferedImage extraLife, BufferedImage speedBoost){

        this.bg = bg;
        this.unbreakableWall = unbreakableWall;
        this.breakableWall = breakableWall;
        this.extraLife = extraLife;
        this.speedBoost = speedBoost;

    }

    public void readMap() {        //read initial map, put into 2d array
        String file = "resources/map.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.isEmpty())
                    continue;
                ArrayList<Integer> rows = new ArrayList<>();
                String[] values = currentLine.trim().split(" ");
                for (String str : values) {
                    if (!str.isEmpty()) {
                        int num = Integer.parseInt(str);
                        rows.add(num);
                    }
                }
                map.add(rows);
            }
        } catch (IOException e) {
        }
    }

    public void mapChange(int arrayWidth, int arrayHeight){     //remove tile from map
        map.get(arrayHeight).set(arrayWidth, 0);
    }

    public void mapUpdate(Graphics2D buffer){    //print current map from 2d array
        allWalls.clear();
        allPowers.clear();
        int tileWidth = unbreakableWall.getWidth();
        int tileHeight = unbreakableWall.getHeight();

        int extraLifetileWidth = extraLife.getWidth();
        int extraLifetileHeight = extraLife.getHeight();

        int width = map.get(0).size();
        int height = map.size();


        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map.get(j).get(i) == 1) {
                    allWalls.add(new Wall(i * tileWidth, j * tileHeight, tileWidth, tileHeight, unbreakableWall, buffer, i, j,false));
                } else if (map.get(j).get(i) == 2) {
                    allWalls.add(new Wall(i * tileWidth, j * tileHeight, tileWidth, tileHeight, breakableWall, buffer, i, j, true));
                } else if (map.get(j).get(i) == 3) {
                    allPowers.add(new addLifePowerUp(i * tileWidth, j * tileHeight, extraLifetileWidth, extraLifetileHeight, extraLife,buffer,i,j, 1));
                } else if (map.get(j).get(i) == 4) {
                    allPowers.add(new SpeedBoost(i * tileWidth, j * tileHeight, tileWidth, tileHeight, speedBoost,buffer,i,j, 2));
                }
            }
        }
    }

    public void drawBackGround(Graphics2D buffer){      //draw sand background tiles
        int tileWidth = bg.getWidth();
        int tileHeight = bg.getHeight();

        int NumberX = (GameConstants.GAME_SCREEN_WIDTH / tileWidth);
        int NumberY = (GameConstants.GAME_SCREEN_HEIGHT / tileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                buffer.drawImage(bg, j * tileWidth,
                        i * tileHeight, tileWidth,
                        tileHeight, null);
            }
        }
    }

    public void handleCollision(CollidableObject c) {
        for (Wall allWall : allWalls) {
            c.checkCollision(allWall);
            allWall.checkCollision(c);
            if (allWall.getCollided()) {
                mapChange(allWall.getArrayW(), allWall.getArrayH());
            }
        }

        for (Stationary allPower : allPowers) {
            allPower.checkCollision(c);
            if (allPower.getCollided()) {
                mapChange(allPower.getArrayW(), allPower.getArrayH());

            }
        }
    }

    public void drawImage(Graphics g) {
        Graphics2D g2g = (Graphics2D) g;
        for (Wall allWall : allWalls) {
            allWall.drawImage(g2g);
        }
        for (Stationary allPower : allPowers) {
            allPower.drawImage(g2g);
        }
    }

}
