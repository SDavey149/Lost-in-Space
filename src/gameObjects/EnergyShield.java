package gameObjects;

import utilities.Vector2D;
import xpilot.Game;

import java.awt.*;

/**
 * Created by sdaveyb on 03/03/14.
 */
public class EnergyShield extends GameObject {
    public static final int RADIUS = 60;
    public static final Color COLOR = Color.GREEN;
    boolean active;

    public EnergyShield(Game game, Vector2D s) {
        super(game, s, new Vector2D());
        active = false;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void update() {
        Ship ship = game.getShip();
        s = ship.s;
        if (ship.energy <= 0) {
            active = false;
            ship.energy = 0;
        }
        //if (!active && energy < INITIAL_ENERGY) energy++; //recharge
        else if (active && ship.energy > 0) ship.energy-=ship.ENERGY_LOSS;
    }

    @Override
    public void draw(Graphics2D g) {
        if (active) {
            g.setColor(new Color(0,255,17,100));
            g.fillOval((int) s.x - RADIUS, (int) s.y - RADIUS,
                    2 * RADIUS, 2 * RADIUS);
            g.setColor(new Color(0,255,17));
            g.drawOval((int) s.x - RADIUS, (int) s.y - RADIUS,
                    2 * RADIUS, 2 * RADIUS);

        }

    }

    @Override
    public double radius() {
        return RADIUS;
    }


    @Override
    public void hit(GameObject obj) {
    }

    public String toString() {
        return "I'm an energy shield!";
    }

    public boolean hittable(GameObject obj) {
        if (!active) return false;
        if (obj instanceof Asteroid || obj instanceof Bullet) {
            return true;
        }
        return false;
    }

    public boolean hasEnergy() {
        return game.getShip().energy > 0;
    }
}
