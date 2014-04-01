package gameObjects;

import utilities.SoundManager;
import utilities.Vector2D;
import xpilot.Action;
import xpilot.Constants;
import xpilot.Controller;
import xpilot.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ship extends GameObject{
    static final double STEER_RATE = 2* Math.PI;  // in radians per second
    // magnitude of acceleration when thrust is applied
    static final double MAG_ACC = 200;
    // constant speed loss factor 
    static final double LOSS = 0.99;
    static final int DEATH_TIMEOUT = 100;
    int lastShot = 0;
    static final Color COLOR = Color.LIGHT_GRAY;
    static final int[] XP = {1,1,0,-1,-1,-3,-3,-1,-2,2,1,3,3};
    static final int[] YP = {0,2,4,2,0,-1,-2,-2,-3,-3,-2,-2,-1};
    static final int[] XPTHRUST = {-1,-1,0,1,1};
    static final int[] YPTHRUST = {-3,-4,-3,-4,-3};
    Controller ctrl;
    public final int INITIAL_ENERGY = 1000;
    public final int ENERGY_LOSS = 2;
    public int energy = INITIAL_ENERGY;
    public EnergyShield shield;


    public int deathTimeout = DEATH_TIMEOUT;
    
    
    // direction in which ship is turning
    // this is a "unit" vector (so magnitude is 1) 
    Vector2D d;

    public Ship(Game game, Controller ctrl) {
      super(game, new Vector2D(), new Vector2D());
      d = new Vector2D();
      this.ctrl = ctrl;
      shield = new EnergyShield(this.game, this.s);
      game.add(shield);
      reset();
    }

    public void reset() {
    	s.set(Constants.WORLD_WIDTH/2, Constants.WORLD_HEIGHT/2);
    	v.set(0,0);
    	d.set(0,1);
        deathTimeout = DEATH_TIMEOUT;
    	dead = false;
        shield = new EnergyShield(this.game, this.s);
        energy = INITIAL_ENERGY;
        game.add(shield);
    }

    
    public void draw(Graphics2D g) {
    	Action action = this.ctrl.action(this.game);
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        double rot = d.theta() + Math.PI / 2;
        g.rotate(rot);
        g.scale(5, 5);
        g.setColor(COLOR);
        g.fillPolygon(XP, YP, XP.length);
        if (action.thrust == -1) {
            g.setColor(Color.CYAN);
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        g.setTransform(at);
        if (isTarget) {
            g.drawOval((int)s.x, (int)s.y, (int)radius()*2, (int)radius()*2);
        }

      }

	@Override
	public void update() {
		Action action = this.ctrl.action(this.game);
		d.rotate(action.turn*STEER_RATE*Constants.DT);
	    //calc new v
	    v.add(d, MAG_ACC*Constants.DT*action.thrust);
	    v.mult(LOSS);
	    s.add(v, Constants.DT);
	    s.wrap(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
	    if (action.shoot && lastShot == 0) {
            shootBullet();
            lastShot = 20;
        }
        if (action.thrust == -1) {
            SoundManager.play(SoundManager.thrust);
            thrustParticles();
        }
        if (action.energyShield && !shield.active && shield.hasEnergy()) shield.activate();
        else if (!action.energyShield && shield.active) shield.deactivate();
        if (lastShot > 0) lastShot--;
		
	}

	
	private void shootBullet() {
	    Vector2D bV = new Vector2D(v);
	    bV.add(d, -110);
	    Vector2D bS = new Vector2D(s);
	    bS.add(d, -radius()-1);
	    Bullet b = new Bullet(this.game, bS, bV, false);
	    game.add(b);
        SoundManager.fire();
    }

    public void thrustParticles() {
        Vector2D ns = new Vector2D(s);
        ns.add(d, radius());
        for (int i = 0; i< 5; i++)
            this.game.addParticle(new Particle(this.game, ns, (1 + Math.random() * 10), Color.CYAN));
    }

	@Override
	public void hit(GameObject obj) {
        if (!dead) {
            if (obj instanceof Powerup) {
                energy = 1000;
            }
            else {
                dead = true;
                shield.deactivate();
                shield.dead = true;
                this.game.removeLife();
                game.explosion(s,200,100, Color.RED);
                game.explosion(s,200,100, Color.ORANGE);
                game.explosion(s,200,100, Color.YELLOW);
            }
        }


    }

    public String toString() {
        return "I'm a ship!";
    }

	@Override
	public double radius() {
		return 15;
	}

    @Override
    public boolean hittable(GameObject obj) {
        //ship can collide with all other objects
        if (obj instanceof Ship || obj instanceof EnergyShield) return false;
        return true;
    }


}
