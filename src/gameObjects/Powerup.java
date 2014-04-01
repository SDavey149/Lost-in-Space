package gameObjects;

import utilities.SoundManager;
import utilities.Vector2D;
import xpilot.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class Powerup extends GameObject {
    public static final int RADIUS = 10;
    public static final Color COLOR = Color.GREEN;
    public static final int[] XP = {1, 0, -1, 0};
    public static final int[] YP = {0,2,0,-2};

    public Powerup(Game game, Vector2D s) {
        super(game, s, new Vector2D());
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        g.scale(6, 6);
        g.setColor(COLOR);
        g.fillPolygon(XP, YP, XP.length);
        g.setTransform(at);
    }

    @Override
    public double radius() {
        return RADIUS;
    }

    @Override
    public void hit(GameObject obj) {
        dead = true;
        SoundManager.play(SoundManager.powerup);
    }

    public boolean hittable(GameObject obj) {
        if (obj instanceof Ship) return true;
        return false;
    }

}
