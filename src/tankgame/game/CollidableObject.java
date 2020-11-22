package tankgame.game;

import java.awt.*;
/**
 *
 * @author Wameedh Mohammed Ali
 */
public interface CollidableObject {
    public void checkCollision(CollidableObject c);
    public Rectangle getRectangle();
    public boolean getCollided();
}
