package gameObjects;

import utilities.SoundManager;
import utilities.Vector2D;
import xpilot.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;


public class Saucer extends GameObject {

    public static final Color COLOR_BODY = Color.BLUE;
    public static final Color COLOR_BELT = Color.RED;
    public static final int HEIGHT = 24;
    public static final int WIDTH = 48;
    public int damage = 100;
    Controller ctrl;
    int lastShot = 30;
    public Vector2D d;

    public Saucer(Game game) {
        super(game, new Vector2D(Math.random()* Constants.WORLD_WIDTH, Math.random()*Constants.WORLD_HEIGHT), new Vector2D(0,0));
        ctrl = new AimNShoot(this);
        d = new Vector2D(0,1);
    }

    @Override
    public void update() {
        Action action = ctrl.action(game);
        d.rotate(action.turn*2* Math.PI*Constants.DT);
        //calc new v
        v.add(d, 50*Constants.DT*action.thrust);
        v.mult(0.99);
        s.add(v, Constants.DT);
        s.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        if (action.shoot && lastShot == 0) {
            shootBullet();
            lastShot = 30;
        }
        if (action.thrust == -1) {
            //SoundManager.play(SoundManager.thrust);
            //thrustParticles();
        }
        if (lastShot > 0) lastShot--;


    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        Ellipse2D ellipse = new Ellipse2D.Double
                (-WIDTH/2.0,-HEIGHT/2.0,
                        WIDTH, HEIGHT);
        g.setColor (COLOR_BODY);
        g.fill(ellipse);
        g.setColor (COLOR_BELT);
        g.drawLine(-WIDTH/2, 0, WIDTH/2, 0);
        g.setTransform(at);
    }

    public void shootBullet() {
        Vector2D bV = new Vector2D(v);
        bV.add(d, -100);
        Vector2D bS = new Vector2D(s);
        Bullet b = new Bullet(this.game, bS, bV, true);
        SoundManager.play(SoundManager.saucerBig);
        game.add(b);
    }

    @Override
    public double radius() {return WIDTH/2;}

    public void hit(GameObject o) {
        damage-=15;
        if (damage < 0) {
            if (Math.random() > 0.7) {
                game.add(new Powerup(game, new Vector2D(s)));
            }
            dead = true;
        }
        game.explosion(s,100,100, Color.BLUE);
        SoundManager.play(SoundManager.bangMedium);
    }

    public boolean hittable(GameObject o) {
        if ((o instanceof Bullet && !((Bullet)o).enemy)) return true;
        return false;
    }
}
