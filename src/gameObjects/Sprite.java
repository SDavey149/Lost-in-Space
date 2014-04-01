package gameObjects;

import utilities.Vector2D;
import xpilot.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;


public abstract class Sprite extends GameObject{
    Image img;
    double width, height, direction;

    public Sprite(Game g, Vector2D s, Vector2D v) {
        super(g, s, v);
    }

    public Sprite() {
    }

    public double radius() {
        return (width + height)/ 4.0;
    }

    public void draw(Graphics2D g) {
        if (isTarget) {
            g.setColor(Color.RED);
            g.drawOval((int)(s.x-radius()), (int)(s.y-radius()), (int)radius()*2, (int)radius()*2);
        }
        double imW = img.getWidth(null);
        double imH = img.getHeight(null);
        AffineTransform t = new AffineTransform();
        //t.rotate(120, 0, 0);
        t.scale(width/imW, height/imH);
        //t.scale(2, 2);
        t.translate(-imW/2.0, -imH/2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(s.x,s.y);
        g.drawImage(img, t, null);
        g.setTransform(t0);

    }

}
