package tankgame.game.moveableObjects;

import tankgame.game.CollidableObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author Wameedh Mohammed Ali
 */
public abstract class Moveable implements CollidableObject {

    private boolean collided = false;
    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int R;

    private BufferedImage image;

    public Moveable(int x, int y, int vx, int vy, int angle, BufferedImage image, int speed) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.image = image;
        this.angle = angle;
        this.R = speed;
    }


    /**********************************
                   SETTERS
     *********************************/

    public void setCollided(boolean collided){
        this.collided = collided;
    }
    public void setAngle(int angle) {
        this.angle = angle;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void settingVxVyForMovement(){
        this.vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        this.vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }
    public void setSpeed(int speed){
        this.R = speed;
    }
    public void setVx(int vx){
        this.vx = vx;
    }
    public void setVy(int vy){
        this.vy = vy;
    }
    /**********************************
                  GETTERS
     *********************************/

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
//    public Rectangle getRectangle(){
//        return rect;
//    }
    public int getSpeed(){
        return R;
    }
    public int getAngle(){
        return angle;
    }
    public BufferedImage getImage(){
        return image;
    }
    public int getVx(){
        return this.vx;
    }

    public int getVy(){
        return this.vy;
    }

    @Override
    public boolean getCollided(){
        return this.collided;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getX(), getY(), getImage().getWidth(), getImage().getHeight());
    }

    @Override
    public void checkCollision(CollidableObject c) { }





}
