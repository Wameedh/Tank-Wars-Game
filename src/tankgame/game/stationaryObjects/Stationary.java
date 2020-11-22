package tankgame.game.stationaryObjects;

import tankgame.game.CollidableObject;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 *
 * @author Wameedh Mohammed Ali
 */
public abstract class Stationary implements CollidableObject {

    private int x;
    private int y;

    private BufferedImage image; // This Image could be anything depending on which class is extending this abstract class
    private Graphics2D buffer;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private boolean collided;

    public Stationary(int x, int y, int tileWidth, int tileHeight, BufferedImage image, Graphics2D buffer, int width, int height) {
        this.x = x;
        this.y = y;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.image = image;
        this.buffer = buffer;
        this.width = width;
        this.height = height;
        this.collided = false;
    }

    /*********************************
                   SETTERS
     *******************************/
    public void setCollided(boolean collided){
        this.collided = collided;
    }

    /*********************************
                  GETTERS
     *******************************/


    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getTileWidth(){
        return tileWidth;
    }

    public int getTileHeight(){
        return tileHeight;
    }

    public int getArrayW(){ return width; }

    public int getArrayH(){
        return height;
    }


    public void drawImage(Graphics g){
        g.drawImage(image,x,y,null);
    }

    @Override
    public void checkCollision(CollidableObject c) { }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(x, y, tileWidth, tileHeight);
    }
    @Override
    public boolean getCollided(){
        return collided;
    }

}
