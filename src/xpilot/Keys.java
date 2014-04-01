package xpilot;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keys extends KeyAdapter implements Controller {
    Action action;
    private ArrayList<Integer> keyList;

    public Keys() {
        action = new Action();
        action.paused = false;
        action.fullScreen = false;
        keyList = new ArrayList<Integer>();
    }

    public Action action(Game game) {
        // this is defined to comply with the standard interface
        return action;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.thrust = -1;
                break;
            case KeyEvent.VK_LEFT:
                action.turn = -1;
                break;
            case KeyEvent.VK_RIGHT:
                action.turn = +1;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = true;
                break;
            case KeyEvent.VK_1:
                action.energyShield = !action.energyShield;
                break;
            case KeyEvent.VK_ESCAPE:
                action.paused = !action.paused;
                break;
            case KeyEvent.VK_ALT:
                keyList.add(KeyEvent.VK_ALT);
                break;
            case KeyEvent.VK_ENTER:
                keyList.add(KeyEvent.VK_ENTER);


        }
        if (keyList.contains(KeyEvent.VK_ALT) && keyList.contains(KeyEvent.VK_ENTER)){
            action.fullScreen = !action.fullScreen;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                action.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
                action.turn = 0;
                break;
            case KeyEvent.VK_RIGHT:
                action.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                break;
            case KeyEvent.VK_ALT:
                keyList.remove((Integer) KeyEvent.VK_ALT);
                break;
            case KeyEvent.VK_ENTER:
                keyList.remove((Integer) KeyEvent.VK_ENTER);
                break;
        }
    }
}