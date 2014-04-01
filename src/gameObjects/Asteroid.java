package gameObjects;

import utilities.SoundManager;
import utilities.Vector2D;
import xpilot.Constants;
import xpilot.Game;

import java.awt.*;

public class Asteroid extends Sprite {
    public static final int LARGE_SIZE = 80;
    public static final double MAX_SPEED = 100;

    public Asteroid() {
        super();
    }

    public Asteroid(Game g, Vector2D s, Vector2D v) {
        super(g, s, v);
        img = Constants.ASTEROID1;
        width = LARGE_SIZE;
        height = LARGE_SIZE;
    }

    public static Asteroid makeRandomAsteroid(Game g) {
        return new Asteroid(g, new Vector2D(Math.random() * Constants.WORLD_WIDTH, Math.random() * Constants.WORLD_HEIGHT), new Vector2D(Math.random() * MAX_SPEED, Math.random() * MAX_SPEED));
    }


    public void update() {
        s.add(v, Constants.DT);
        s.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }

    @Override
    public void hit(GameObject obj) {
        if (obj instanceof Bullet && !((Bullet)obj).enemy) {
            this.game.addScore(5);
            width = width / 2;
            height = height / 2;
            v = new Vector2D(Math.random() * MAX_SPEED, Math.random() * MAX_SPEED);
            if (width < 20 || height < 20) {
                dead = true;
                SoundManager.play(SoundManager.bangSmall);
            } else {
                Asteroid ast = new Asteroid(this.game, new Vector2D(s), new Vector2D(Math.random() * MAX_SPEED, Math.random() * MAX_SPEED));
                ast.width = width;
                ast.height = height;
                this.game.add(ast);
                SoundManager.play(SoundManager.bangLarge);
            }
            game.explosion(s,200,100, Color.LIGHT_GRAY);

        }
        else if (obj instanceof EnergyShield && ((EnergyShield)obj).active) {
            Vector2D nV = new Vector2D(obj.game.getShip().v);
            obj.game.getShip().v = v;
            v = nV;
        }

    }

    public String toString() {
        return "I'm an asteroid!";
    }

    public boolean hittable(GameObject obj) {
        if (obj instanceof Ship || obj instanceof Bullet || (obj instanceof EnergyShield && ((EnergyShield) obj).active)) return true;
        return false;
    }
}
