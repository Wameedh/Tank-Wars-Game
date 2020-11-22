package tankgame.game.moveableObjects;

import tankgame.game.CollidableObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Wameedh Mohammed Ali
 */
public class Bullet extends Moveable {

    public Bullet(int x, int y, int vx, int vy, int angle, BufferedImage imgBullet, int speed) {
        super(x, y, vx, vy, angle, imgBullet, speed);
    }

    public void update() {
        //move forward
        settingVxVyForMovement();
        int x = getX();
        int y = getY();
        x += getVx();
        y += getVy();
        setX(x);
        setY(y);
    }

    @Override
    public void checkCollision(CollidableObject c) {
        if(getRectangle().intersects(c.getRectangle())){
            setCollided(true);
        }
    }


    public void drawImage(Graphics2D buffer){
        AffineTransform rotation = AffineTransform.getTranslateInstance(getX(), getY());
        rotation.rotate(Math.toRadians(getAngle()), getImage().getWidth() / 2.0, getImage().getHeight() / 2.0);
        buffer.drawImage(getImage(), rotation, null);
    }

}
