package xpilot;

import gameObjects.GameObject;
import gameObjects.Particle;
import gameObjects.Ship;
import utilities.Vector2D;

import java.awt.*;

public interface Game {
    public Iterable<GameObject> getGameObjects();

    public void add(GameObject obj);

    public void addParticle(Particle part);

    public void update();

    public Controller getCtrl();

    public void resetGame();

    public void addScore(int score);

    public void removeLife();

    public int getScore();

    public int getLives();

    public Iterable<GameObject> getParticles();

    public Ship getShip();

    public void addLife();

    public int getLevel();

    public boolean isGameOver();

    public void resetToLevel1();

    public boolean hasGameStarted();

    public void startGame();

    public void explosion(Vector2D s, int n, int ttl, Color col);




}
