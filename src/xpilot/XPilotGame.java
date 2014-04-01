package xpilot;

import gameObjects.*;
import utilities.JEasyFrameFull;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class XPilotGame implements Game {

    ArrayList<GameObject> objects;
    Ship ship;
    Controller ctrl;
    ArrayList<GameObject> pending;
    ArrayList<GameObject> alive;
    LinkedList<GameObject> particles;
    int score = 0;
    int lives = 3;
    int level = 1;
    int asteroidAmount = 1;
    int saucerCount = 0;
    int asteroidsLeft;
    boolean gameOver = false;
    boolean gameStarted = false;
    public static Game game;
    public static View view;
    public static boolean fullScreenMode = false;

    public static void main(String[] args) throws Exception {
        game = new XPilotGame();
        view = new View(game);
        SoundManager.load();
        JEasyFrameFull win = new JEasyFrameFull(view);
        win.addKeyListener((Keys)game.getCtrl());
        // run the game
        while (true) {
            game.update();
            Action act = game.getCtrl().action(game);
            if (!fullScreenMode && act.fullScreen) {
                fullScreenMode = true;
                win.setDimensionsToFullScreen();
                view = new View(game);
                win.setFullScreen(view);
            }
            else if (fullScreenMode && !act.fullScreen) {
                fullScreenMode = false;
                win.setDimensionsToSmallScreen();
                view = new View(game);
                act.fullScreen = false;
                win.setSmallScreen(view);
            }
            view.repaint();
            Thread.sleep(Constants.DELAY);
        }

    }

    public XPilotGame() {
        objects = new ArrayList<GameObject>();
        pending = new ArrayList<GameObject>();
        alive = new ArrayList<GameObject>();
        particles = new LinkedList<GameObject>();
        ctrl = new Keys();
        ship = new Ship(this, ctrl);
        objects.add(ship);
        resetGame();
    }

    public void resetToLevel1() {
        score = 0;
        lives = 3;
        level = 1;
        asteroidAmount = 1;
        saucerCount = 0;
        gameOver = false;
    }

    public void resetGame() {
        particles.clear();
        objects.clear();
        alive.clear();
        ship.reset();
        objects.add(ship);
        for (int i = 0; i < asteroidAmount; i++) {
            objects.add(Asteroid.makeRandomAsteroid(this));
        }
        for (int i = 0; i < saucerCount; i++) {
            objects.add(new Saucer(this));
        }
        for (int i = 0; i < Constants.BACKGROUND_PARTICLES; i++) {
            particles.add(Particle.makeRandomParticle(this));
        }
        asteroidsLeft = asteroidAmount;
        waitUntilSafe();
    }

    public void waitUntilSafe() {
        while(!checkSafeGameState()) {
            for (GameObject object : objects) {
                object.update();
            }
            //wait until game is safe to begin
        }
    }

    public boolean hasGameStarted() {
        return gameStarted;
    }

    public void startGame() {
        gameStarted = true;
    }

    public void runLevel() {
        if (ship.dead) {
            ship.deathTimeout--;
            if (ship.deathTimeout <= 0) {
                ship.reset();
                objects.add(ship);
                //waitUntilSafe();
            }
        }
        if (asteroidsLeft <= 0) {
            //level is over, up to next level
            level++;
            asteroidAmount++;
            if (saucerCount < 5)
                saucerCount++;
            resetGame(); //TODO Needs updating to have timeout between levels
        }
        else if(lives <= 0) {
            explosion(new Vector2D(ship.s), 300, 1000, Color.ORANGE);
            explosion(new Vector2D(ship.s), 300, 1000, Color.YELLOW);
            explosion(new Vector2D(ship.s), 300, 1000, Color.RED);
            explosion(new Vector2D(ship.s), 300, 1000, Color.BLUE);
            explosion(new Vector2D(ship.s), 300, 1000, Color.GREEN);
            gameOver = true;
            //System.exit(1);
        }
    }

    public int getLevel() {
        return level;
    }

    public boolean checkSafeGameState() {
        for (GameObject otherObject : objects) {
            if (!(otherObject instanceof Ship) && this.ship.s.dist(otherObject.s) <= this.ship.radius() + otherObject.radius() + 300) {
                return false;
            }
        }
        return true;
    }

    public void update() {
        Action action = this.ctrl.action(this);
        if (!action.paused && !gameOver && gameStarted) {
            for (GameObject object : objects) {
                object.update();
                if (object instanceof Ship || object instanceof Asteroid || object instanceof Saucer || (object instanceof EnergyShield && ((EnergyShield)object).isActive()))
                    checkCollision(object);
                if (!object.dead)
                    alive.add(object);
                else {
                //dead object
                    if (object instanceof Asteroid) asteroidsLeft--;
                }


            }
            synchronized (this) {
                objects.clear();
                objects.addAll(pending);
                objects.addAll(alive);
            }
            pending.clear();
            alive.clear();
            updateParticles();
            runLevel();
        }
        if (gameOver) {
            updateParticles();
            for (GameObject object : objects) {
                object.update();
            }
        }
    }



    public void updateParticles() {
        // iterate over the set of particles, removing any dead ones
        ListIterator<GameObject> it = particles.listIterator();
        synchronized (this) {
            while (it.hasNext()) {
                Particle p = (Particle) it.next();
                p.update();
                if (p.dead) {
                    it.remove();
                }
            }
        }
    }

    public void addScore(int s) {
        score += s;
    }

    public void removeLife() {
        lives--;
    }

    public void addLife() {
        lives++;
    }


    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public Ship getShip() {
        return ship;
    }

    public void checkCollision(GameObject object) {
        // check with other game objects
        if (!object.dead) {
            for (GameObject otherObject : objects) {
                if (!(object instanceof Asteroid && otherObject instanceof EnergyShield)) {
                    if (object.hittable(otherObject)
                            && overlap(object, otherObject)) {
                        object.hit(otherObject);
                        otherObject.hit(object);
                        return;
                    }
                }

            }
        }
    }

    public boolean overlap(GameObject x, GameObject y) {
        return x.s.dist(y.s) <= x.radius() + y.radius();
    }


    public void explosion(Vector2D s, int n, int ttl, Color col) {
        for (int i = 0; i < n; i++)
            particles.add(new Particle(this, s, (1 + Math.random() * ttl), col));
    }

    public void add(GameObject object) {
        pending.add(object);
        if (object instanceof Asteroid) {
            asteroidsLeft++;
        }
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    @Override
    public Iterable<GameObject> getGameObjects() {
        return this.objects;
    }

    @Override
    public Iterable<GameObject> getParticles() {
        return this.particles;
    }

    @Override
    public Controller getCtrl() {
        return ctrl;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
