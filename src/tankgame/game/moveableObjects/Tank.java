package tankgame.game.moveableObjects;



import tankgame.GameConstants;
import tankgame.game.CollidableObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Wameedh Mohammed Ali
 */
public class Tank extends Moveable {

    private long lastAttack;
    private int health;
    private int lives;
    private long cooldown;
    private int dmg;
    private int spawnX;
    private int spawnY;
    private int spawnA;


    private final float ROTATIONSPEED = 4;

    private BufferedImage bulletImage;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean FirePressed;

    ArrayList<Bullet> allBullets = new ArrayList<>();

    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage image,BufferedImage bulletImage , int speed) {
        super(x, y, vx, vy, angle, image, speed);
        this.bulletImage = bulletImage;
        this.spawnA = getAngle();
        this.spawnX = getX();
        this.spawnY = getY();
        this.cooldown = 1000;
        this.health = 100;
        this.dmg = 25;
        this.lives = 2;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleFirePressed() { this.FirePressed = true; }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleFirePressed() { this.FirePressed = false; }

    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.FirePressed) {
            this.shoot();
            FirePressed = false;
        }
        for(int i = 0; i < allBullets.size(); i++){
            if(allBullets.get(i).getCollided()) {
                allBullets.remove(i);
            }else{
                allBullets.get(i).update();
            }
        }

    }

    private void rotateLeft() {
       int angle = getAngle();
       angle -= this.ROTATIONSPEED;
       setAngle(angle);

    }

    private void rotateRight() {
        int angle = getAngle();
        angle += this.ROTATIONSPEED;
        setAngle(angle);
    }

    private void moveBackwards() {

        settingVxVyForMovement();
        int x = getX();
        int y = getY();

        x -= getVx();
        y -= getVy();
        setX(x);
        setY(y);
       checkBorder();
    }

    private void moveForwards() {
        settingVxVyForMovement();
        int x = getX();
        int y = getY();
        x += getVx();
        y += getVy();
        setX(x);
        setY(y);
        checkBorder();
    }

    private void checkBorder() {
        if (getX() < 30) {
            setX(30);
        }
        if (getX() >= GameConstants.GAME_SCREEN_WIDTH - 88) {
            setX(GameConstants.GAME_SCREEN_WIDTH - 88);
        }
        if (getY() < 40) {
            setY(40);
        }
        if (getY() >= GameConstants.GAME_SCREEN_HEIGHT - 80) {
            setY(GameConstants.GAME_SCREEN_HEIGHT - 80);
        }
    }


    public void damage(){                   // 25dmg per shot, 2 lives, respawn location
        this.health = this.health - dmg;
        if (this.health <= 0 && this.lives != 0){
            this.health = 100;
            this.lives--;
            setX(spawnX);
            setY(spawnY);
            setAngle(spawnA);
            this.cooldown = 1000;
        }
    }

    public void addLife() { this.lives += 1; }

    public void SpeedBoost(){
        setSpeed(8);
    }


    private void shoot(){       //shoot once per second
        long time = System.currentTimeMillis();
        if(time > lastAttack + cooldown) {
            int y =  getY() + (getImage().getHeight()/10);
            int x = getX() + (getImage().getWidth()/10);
            Bullet b = new Bullet(x, y,0,0, getAngle(), bulletImage,12);
            allBullets.add(b);
            lastAttack = time;
        }

    }
    /******************************
                 GETTERS
     *****************************/
    public int getHealth(){
        return health;
    }
    public int getLives(){
        return lives;
    }

//    @Override
//    public String toString() {
//        return "x=" + getX() + ", y=" + getY() + ", angle=" + getAngle();
//    }

    @Override
    public void checkCollision(CollidableObject c) {
        if(getRectangle().intersects(c.getRectangle())){
            if(c instanceof Bullet){
                damage();
            } else {

                Rectangle intersection = getRectangle().intersection(c.getRectangle());
                if(intersection.height > intersection.width  && getX() < intersection.x){
                    //left of the wall
                    setX(getX() - intersection.width/2);
                }
                else if(intersection.height > intersection.width  && getX() > c.getRectangle().x){
                    //right of the wall
                    setX(getX() + intersection.width/2);
                }
                else if(intersection.height < intersection.width  && getY() < intersection.y){
                    //up of the wall
                    setY(getY() - intersection.height/2);
                }
                else if(intersection.height < intersection.width  && getY() > c.getRectangle().y){
                    //down of the wall
                    setY(getY() + intersection.height/2);
                }
            }
        }
        for(Bullet b : allBullets){
            b.checkCollision(c);
            c.checkCollision(b);
        }
    }


    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for(Bullet b : allBullets){
            b.drawImage(g2d);
        }
        AffineTransform rotation = AffineTransform.getTranslateInstance(getX(), getY());
        rotation.rotate(Math.toRadians(getAngle()), getImage().getWidth() / 2.0, getImage().getHeight() / 2.0);
        g2d.drawImage(getImage(), rotation, null);
    }
}
