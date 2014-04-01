package gameObjects;

import utilities.Vector2D;
import xpilot.Game;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public abstract class GameObject {
	public Game game;
	public Vector2D s, v;
	public boolean dead;
    public boolean isTarget = false;
	
	public GameObject(Game g, Vector2D s, Vector2D v) {
		this.game = g;
		this.s = s;
		this.v = v;
		this.dead = false;
	}

	public GameObject() {
		dead = false;
	}

	public abstract void draw(Graphics2D g);
	public abstract void update();
	public abstract void hit(GameObject obj);
	public abstract double radius();

    public Vector2D to(Vector2D v) {
        return new Vector2D(v.x-this.v.x, v.y-this.v.y);
    }

    public Shape getBounds2D() {
        return new Ellipse2D.Double(s.x, s.y, this.radius()*2, this.radius()*2);
    }

    public abstract boolean hittable(GameObject obj);
	
	
}
