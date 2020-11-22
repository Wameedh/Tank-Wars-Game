/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.game;


import tankgame.GameConstants;
import tankgame.Launcher;
import tankgame.game.moveableObjects.Tank;
import tankgame.game.moveableObjects.TankControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {

    private static boolean gameOver = false;
    private BufferedImage world;
    private Map map;
    private Tank t1;
    private Tank t2;

    private Launcher lf;
    private long tick = 0;

    private BufferedImage t1img;
    private BufferedImage t2img;

    private BufferedImage bg;
    private BufferedImage bulletimg;
    private BufferedImage unbreakableWall;
    private BufferedImage breakableWall;
    private BufferedImage extraLifeImage;
    private BufferedImage speedBoostImage;

    private JFrame jf;

    public TRE(Launcher lf){
        this.lf = lf;
    }

    @Override
    public void run(){
        try {
            this.resetGame();
            while (!gameOver) {
                this.tick++;
                this.repaint();   // redraw game
                Thread.sleep(1000 / 144); //sleep for a few milliseconds
                /*
                 * simulate an end game event
                 * we will do this with by ending the game when drawn 2000 frames have been drawn
                 */
//                if(this.tick > 2000){
//                    this.lf.setFrame("end");
//                    return;
//                }
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        this.tick = 0;
        this.t1.setX(200);
        this.t1.setY(200);

        this.t2.setX(GameConstants.GAME_SCREEN_WIDTH - 200);
        this.t2.setY(GameConstants.GAME_SCREEN_HEIGHT - 200);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.GAME_SCREEN_WIDTH,
                GameConstants.GAME_SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            t1img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png")));
            t2img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank2.png")));

            bg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.bmp")));
            bulletimg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("bullet.png")));
            unbreakableWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall.gif")));
            breakableWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("WoodBox.gif")));
            extraLifeImage = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("extraLife.png")));
            speedBoostImage = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("speedBoost.png")));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }


        t1 = new Tank(200, 200, 0, 0, 0,t1img,bulletimg, 5);
        t2 = new Tank(GameConstants.GAME_SCREEN_WIDTH - 200, GameConstants.GAME_SCREEN_HEIGHT - 200, 0, 0, 180, t2img, bulletimg, 5);

        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
        this.map = new Map(bg, unbreakableWall, breakableWall, extraLifeImage, speedBoostImage);
        map.readMap(); // initial map

    }

    /**
     X coordinate for split screen mode.
     */
    private int getXCoordinate(Tank t){
        int x = t.getX();
        if(x < GameConstants.SCREEN_WIDTH / 4)
            x = GameConstants.SCREEN_WIDTH / 4;
        if(x > GameConstants.GAME_SCREEN_WIDTH - GameConstants.SCREEN_WIDTH / 4)
            x = GameConstants.GAME_SCREEN_WIDTH - GameConstants.SCREEN_WIDTH / 4;
        return x;
    }

    /**
     Y coordinate for split screen mode.
     */
    public int getYCoordinate(Tank t){
        int y = t.getY();
        if(y < GameConstants.SCREEN_HEIGHT / 2)
            y = GameConstants.SCREEN_HEIGHT / 2;
        if(y > GameConstants.GAME_SCREEN_HEIGHT - GameConstants.SCREEN_HEIGHT / 2)
            y = GameConstants.GAME_SCREEN_HEIGHT - GameConstants.SCREEN_HEIGHT / 2;
        return y;
    }

    private void update(){
        t1.update();
        t2.update();
        t1.checkCollision(t2);
        t2.checkCollision(t1);
        map.handleCollision(t1);
        map.handleCollision(t2);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        update();

        Graphics2D buffer = world.createGraphics();
        super.paintComponent(g2);

        // Set the background and draw the map
        this.setBackground(Color.GRAY);
        map.drawBackGround(buffer);
        map.mapUpdate(buffer);
        map.drawImage(buffer);

        // Draw tanks
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);


        BufferedImage leftScreen = world.getSubimage(getXCoordinate(t1) - GameConstants.SCREEN_WIDTH / 4, getYCoordinate(t1) - GameConstants.SCREEN_HEIGHT / 2, GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT);
        BufferedImage rightScreen = world.getSubimage(getXCoordinate(t2) - GameConstants.SCREEN_WIDTH / 4, getYCoordinate(t2) - GameConstants.SCREEN_HEIGHT / 2, GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT);

        // Draw split screen
        g2.drawImage(leftScreen, 0, 0, null);
        g2.drawImage(rightScreen, GameConstants.SCREEN_WIDTH / 2 + 10, 0, null);

        // print lives and hp
        g2.setColor(Color.white);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g2.drawString("PLAYER 1", 10, 690);

        g2.setColor(Color.white);
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g2.drawString("PLAYER 2", GameConstants.SCREEN_WIDTH - 425, 690);

        g2.setColor(Color.darkGray);
        g2.fillRect(10, 710, 400, 20);
        g2.fillRect(GameConstants.SCREEN_WIDTH - 425, 710, 400, 20);
        g2.setColor(Color.red);
        g2.fillRect(10, 710, t1.getHealth() * 4, 20);
        g2.fillRect(GameConstants.SCREEN_WIDTH - 425, 710, t2.getHealth() * 4, 20);
        g2.setColor(Color.darkGray);

        g2.drawRect(10, 710, 400, 20);
        g2.drawRect(GameConstants.SCREEN_WIDTH - 425, 710, 400, 20);

        for(int i = 0; i < t1.getLives(); i++) {
            if(i == 0){
                g2.drawImage(t1img, 10 , 760, null);
            } else {
                g2.drawImage(t1img, 10 + (20 +t1img.getWidth()) * i, 760, null);
            }

        }
        for(int i = 0; i < t2.getLives(); i++){

            if(i == 0){
                g2.drawImage(t2img, GameConstants.SCREEN_WIDTH - 425 , 760, null);
            } else {
                g2.drawImage(t2img, GameConstants.SCREEN_WIDTH - 425 + (20 + t1img.getWidth()) * i, 760, null);
            }
        }

        /**
         * Add minimap. Set its width and length to 1/5 of WORLD_WIDTH and WORLD_HEIGHT respectively.
         */
        g2.drawImage(world, GameConstants.SCREEN_WIDTH / 2 - GameConstants.GAME_SCREEN_WIDTH / 10, GameConstants.SCREEN_HEIGHT + 10, GameConstants.GAME_SCREEN_WIDTH / 5, GameConstants.GAME_SCREEN_HEIGHT / 5, null);

        if(t1.getLives() == 0){

            g2.setColor(Color.BLACK);

            g2.fillRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g2.drawString("GAME OVER PLAYER 2 WINS", (GameConstants.SCREEN_WIDTH/4) , GameConstants.SCREEN_HEIGHT/2);
            gameOver = true;
        }

        if(t2.getLives() == 0) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
            g2.setColor(Color.white);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            g2.drawString("GAME OVER PLAYER 1 WINS", (GameConstants.SCREEN_WIDTH/4), GameConstants.SCREEN_HEIGHT/2);
            gameOver = true;
        }

    }

}
