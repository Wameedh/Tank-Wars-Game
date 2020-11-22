package tankgame.game.Stationary;

import tankgame.game.CollidableObject;
import tankgame.game.moveableObjects.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author Wameedh Mohammed Ali
 */
public class Wall extends Stationary {

    private boolean breakable;

    public Wall(int x, int y, int tileWidth, int tileHeight, BufferedImage image, Graphics2D buffer, int width, int height, boolean breakable) {
        super(x, y, tileWidth, tileHeight, image, buffer, width, height);
        this.breakable = breakable;
    }

    @Override
    public void checkCollision(CollidableObject c) {
        if(c instanceof Bullet){
            if(this.getRectangle().intersects(c.getRectangle())){
                if(breakable) {
                    setCollided(true);
                }
            }
        }
    }

}
