package xpilot;

import gameObjects.GameObject;
import gameObjects.Saucer;
import utilities.Vector2D;


/**
 * Author: Scott Davey
 * Date: 25/02/14
 * Time: 22:04
 */
public class AimNShoot implements Controller {

    public static final double SHOOTING_DISTANCE = 400;
    public static final double SHOOTING_ANGLE = Math.PI / 12;
    GameObject target;
    Action action = new Action();
    Saucer ship;

    public AimNShoot(Saucer ship) {
        this.ship = ship;
    }

    @Override
    public Action action(Game game) {
        GameObject nextTarget = findTarget();
        if (nextTarget == null)
            return new Action();
        switchTarget(nextTarget);
        aim();

        action.shoot = ((Math.abs(angleToTarget()) < SHOOTING_ANGLE)
                && inShootingDistance());
        if (inShootingDistance()) action.thrust = -1;
        return action;
    }

    public GameObject findTarget(){
        double minDistance = 2 * SHOOTING_DISTANCE;
        GameObject closestTarget = null;
        double dist = ship.s.dist(ship.game.getShip().s);
        if (dist < minDistance) {
            closestTarget = ship.game.getShip();
        }
        return closestTarget;
    }

    public double angleToTarget() {
        Vector2D v = ship.to(target.v);
        v.rotate(-ship.d.theta());
        return v.theta();
    }

    public boolean inShootingDistance() {
        return  ship.s.dist(target.s) < SHOOTING_DISTANCE + target.radius(); }

    public void aim() {
        double angle = angleToTarget();
        action.turn = (int) Math.signum(Math.sin(angle));
    }

    public void switchTarget(GameObject newTarget) {
        target = newTarget;
    }

}
