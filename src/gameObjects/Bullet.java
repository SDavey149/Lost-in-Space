package gameObjects;


import utilities.Vector2D;
import xpilot.Constants;
import xpilot.Game;

import java.awt.*;

public class Bullet extends GameObject {
	int ttl = 200; //time to live
    boolean enemy;
	public Bullet(Game g, Vector2D s, Vector2D v, boolean enemy) {
		super(g, s, v);
        this.enemy = enemy;
        if (enemy) ttl=100;
	}

	@Override
	public void draw(Graphics2D g) {
		int x = (int) s.x;
	    int y = (int) s.y;
        if (!enemy)
	        g.setColor(Color.RED);
        else g.setColor(Color.BLUE);
	    int rad = (int)radius();
	    g.fillOval(x - rad, y - rad, 2 * rad, 2 * rad);

		
	}

	@Override
	public void update() {
		s.add(v, Constants.DT);
		s.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        ttl--;
        if (ttl <= 0) dead = true;
		
	}

	@Override
	public void hit(GameObject obj) {
		dead = true;
		
	}

	@Override
	public double radius() {
		return 2;
	}

    public String toString() {
        return "I'm a bullet!";
    }

    public boolean hittable(GameObject obj) {
        if (!enemy) {
            if (obj instanceof Ship || obj instanceof Asteroid || obj instanceof EnergyShield || obj instanceof Saucer) {
                return true;
            }
            return false;
        }
        if (obj instanceof Ship || obj instanceof EnergyShield) {
            return true;
        }
        return false;
    }

}
