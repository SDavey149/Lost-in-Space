package xpilot;

/**
 * Author: Scott Davey
 * Date: 25/02/14
 * Time: 21:44
 * www.sd149.co.uk
 */
public class RotateNShoot implements Controller {
    Action action;

    public RotateNShoot() {
        action = new Action();
    }

    @Override
    public Action action(Game game) {
        action.shoot = true;
        action.turn = -1;
        return action;
    } }
