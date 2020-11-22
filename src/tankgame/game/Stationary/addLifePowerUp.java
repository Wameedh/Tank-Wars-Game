package tankgame.game.Stationary;

import tankgame.game.CollidableObject;
import tankgame.game.moveableObjects.Tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class addLifePowerUp extends Stationary {

    public addLifePowerUp(int x, int y, int TileWidth, int TileHeight, BufferedImage image, Graphics2D buffer, int width, int height, int powerUp) {
        super(x, y, TileWidth, TileHeight, image, buffer, width, height);

    }

    @Override
    public void checkCollision(CollidableObject c) {
        if(c instanceof Tank){
            if(this.getRectangle().intersects(c.getRectangle())){
                setCollided(true);
                ((Tank) c).addLife();
            }
        }
    }

}
