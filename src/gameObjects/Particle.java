package gameObjects;

import utilities.Vector2D;
import xpilot.Constants;
import xpilot.Game;

import java.awt.*;

public class Particle extends GameObject {
    public static final int RADIUS = 1;
    public static final double PARTICLELOSS = 0.99;
    public Color col;
    double ttl;
    boolean liveForever = false;

    public Particle(Game game, Vector2D s, double ttl, Color col) {
        super(game, new Vector2D(s),
                new Vector2D(Constants.RANDOM.nextGaussian(),
                        Constants.RANDOM.nextGaussian()));
        this.ttl = ttl;
        this.col = col;
    }

    public Particle(Game game, Vector2D s, Color col) {
        super(game, new Vector2D(s),
                new Vector2D(0,0));
        liveForever = true;
        this.col = col;
    }

    public static Particle makeRandomParticle(Game game) {
        return new Particle(game, new Vector2D(Math.random()*Constants.WORLD_WIDTH, Math.random()*Constants.WORLD_HEIGHT), Color.WHITE);
    }

    @Override
    public void update() {
        s.add(v);
        v.mult(PARTICLELOSS);
        if (!liveForever) {
            ttl--;
            if (ttl < 0) dead = true;
        }

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(this.col);
        g.fillOval((int) s.x - RADIUS, (int) s.y - RADIUS,
                2 * RADIUS, 2 * RADIUS);
    }

    @Override
    public double radius() {
        return RADIUS;
    }

    @Override
    public void hit(GameObject obj) {

    }

    public boolean hittable(GameObject obj) {
        //particle does not collide with anything
        return false;
    }

}