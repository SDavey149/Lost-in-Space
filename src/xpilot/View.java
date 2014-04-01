package xpilot;

import gameObjects.*;
import utilities.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class View extends JComponent {
    final Game game;
    Image im = Constants.MILKYWAY1;
    Vector2D center;
    AffineTransform bgTransf;
    public final int MINIMAP_WIDTH = Constants.FRAME_WIDTH/6;
    public final int MINIMAP_HEIGHT = Constants.FRAME_WIDTH/6;
    public final int SHIELDBAR_WIDTH = Constants.FRAME_WIDTH/2;
    public final int SHIELDBAR_HEIGHT = 20;
    public final static int BUTTON_SPACER = 50;
    Rectangle2D resumeButton;
    Rectangle2D exitButton;



    public View(final Game game) {
        this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchx = (imWidth > Constants.FRAME_WIDTH ? 1 :
                Constants.FRAME_WIDTH/ imWidth);
        double stretchy = (imHeight > Constants.FRAME_HEIGHT ? 1 :
                Constants.FRAME_HEIGHT / imHeight);
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Action a = game.getCtrl().action(game);
                if (!game.hasGameStarted()) {
                    if (resumeButton.contains(e.getX(), e.getY())) {
                        game.startGame();
                    }
                    if (exitButton.contains(e.getX(), e.getY())) {
                        System.exit(0);
                    }
                }
                if (a.paused) {
                    if (resumeButton.contains(e.getX(), e.getY())) {
                        a.paused = false;
                    }
                    if (exitButton.contains(e.getX(), e.getY())) {
                        System.exit(0);
                    }
                }
                else if (game.isGameOver()) {
                    if (resumeButton.contains(e.getX(), e.getY())) {
                        game.resetToLevel1();
                        game.resetGame();
                    }
                    if (exitButton.contains(e.getX(), e.getY())) {
                        System.exit(0);
                    }
                }

            }
        });
    }


    @Override
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        Action a = game.getCtrl().action(game);
        // paint the background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        AffineTransform t0 = g.getTransform();
        center = game.getShip().s;
        g.translate(Constants.FRAME_WIDTH / 2 - center.x,
                Constants.FRAME_HEIGHT / 2 - center.y);
        synchronized (game) {
            drawObjects(g, game.getParticles());
            drawObjects(g, game.getGameObjects());
        }
        g.setTransform(t0);
        displayGameInfo(g);
        if (game.hasGameStarted()) {
            synchronized (game) {
                createMiniMap(g);
                displayShipEnergyLevel(g);
            }
        }
        if (!game.hasGameStarted()) {
            drawStartScreen(g);
        }
        else if (a.paused) {
            drawPauseMenu(g);
        }
        else if (game.isGameOver()) {
            drawGameOver(g);
        }
    }

    public void drawPauseMenu(Graphics2D g) {
        g.setColor(new Color(255,255,255, 150));
        g.fillRect(Constants.FRAME_WIDTH / 3, 0, Constants.FRAME_WIDTH / 3, Constants.FRAME_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawLine(Constants.FRAME_WIDTH / 3, 0, Constants.FRAME_WIDTH / 3, Constants.FRAME_HEIGHT);
        g.drawLine(Constants.FRAME_WIDTH / 3*2, 0, Constants.FRAME_WIDTH / 3*2, Constants.FRAME_HEIGHT);
        resumeButton = new Rectangle2D.Float(Constants.FRAME_WIDTH/3+10, Constants.FRAME_HEIGHT/2+60, Constants.FRAME_WIDTH/3-20, 30);
        exitButton = new Rectangle2D.Float(Constants.FRAME_WIDTH/3+10, Constants.FRAME_HEIGHT/2+60+BUTTON_SPACER, Constants.FRAME_WIDTH/3-20, 30);
        g.fill(resumeButton);
        g.fill(exitButton);
        g.setColor(Color.BLACK);
        g.drawString("Resume", (int)resumeButton.getMinX()+20, (int)resumeButton.getY()+20);
        g.drawString("Exit", (int)exitButton.getMinX()+20, (int)exitButton.getY()+20);

    }

    public void drawStartScreen(Graphics2D g) {
        g.setColor(Color.WHITE);
        int w = Constants.FRAME_WIDTH/4;
        resumeButton = new Rectangle2D.Float(Constants.FRAME_WIDTH/2-w/2, Constants.FRAME_HEIGHT/2+60, w, 30);
        exitButton = new Rectangle2D.Float(Constants.FRAME_WIDTH/2-w/2, Constants.FRAME_HEIGHT/2+60+BUTTON_SPACER, w, 30);
        g.fill(resumeButton);
        g.fill(exitButton);
        g.setColor(Color.BLACK);
        g.drawString("PLAY", (int)resumeButton.getMinX()+20, (int)resumeButton.getY()+20);
        g.drawString("EXIT", (int)exitButton.getMinX()+20, (int)exitButton.getY()+20);
    }

    public void drawGameOver(Graphics2D g) {
        g.setColor(Color.WHITE);
        Font f = g.getFont();
        g.setFont(new Font(f.getName(), Font.PLAIN, 26));
        g.drawString("GAME OVER", (Constants.FRAME_WIDTH/2)-80, Constants.FRAME_HEIGHT/2);
        g.setFont(f);
        int w = Constants.FRAME_WIDTH/4;
        resumeButton = new Rectangle2D.Float(Constants.FRAME_WIDTH/2-w/2, Constants.FRAME_HEIGHT/2+60, w, 30);
        exitButton = new Rectangle2D.Float(Constants.FRAME_WIDTH/2-w/2, Constants.FRAME_HEIGHT/2+60+BUTTON_SPACER, w, 30);
        g.fill(resumeButton);
        g.fill(exitButton);
        g.setColor(Color.BLACK);
        g.drawString("PLAY AGAIN", (int)resumeButton.getMinX()+20, (int)resumeButton.getY()+20);
        g.drawString("EXIT", (int)exitButton.getMinX()+20, (int)exitButton.getY()+20);
    }

    public void drawObjects(Graphics2D g, Iterable<GameObject> objs) {
        double minx = center.x - Constants.FRAME_WIDTH /2.0;
        double maxx = center.x + Constants.FRAME_WIDTH /2.0;
        double miny = center.y - Constants.FRAME_HEIGHT /2.0;
        double maxy = center.y + Constants.FRAME_HEIGHT /2.0;
        Rectangle2D screen = new Rectangle2D.Double(minx, miny, maxx-minx, maxy-miny);
        for (GameObject obj: objs) {
            Vector2D s0 = new Vector2D(obj.s);
            Vector2D s = obj.s;
            if (s.x < minx) s.add(Constants.WORLD_WIDTH, 0);
            else if (s.x > maxx) s.add(-Constants.WORLD_WIDTH, 0);
            if (s.y < miny) s.add(0, Constants.WORLD_HEIGHT);
            else if (s.y > maxy) s.add(0, -Constants.WORLD_HEIGHT);
            if(obj.getBounds2D().intersects(screen)) obj.draw(g);
            obj.s = s0;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }

    public void displayGameInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawString("SCORE: " + game.getScore(), 8, 15);
        g.drawString("LIVES: " + game.getLives(), 8, 30);
        g.drawString("LEVEL: " + game.getLevel(), 8, 45);
    }

    public void displayShipEnergyLevel(Graphics2D g) {
        synchronized (game) {
            double ratio = SHIELDBAR_WIDTH/(double)game.getShip().INITIAL_ENERGY;
            g.setColor(new Color(0,255,17,160));
            g.fillRect((Constants.FRAME_WIDTH-SHIELDBAR_WIDTH)/2, 20, ((int)(game.getShip().energy*ratio)), SHIELDBAR_HEIGHT);
            g.setColor(new Color(0,255,17));
            g.drawRect((Constants.FRAME_WIDTH-SHIELDBAR_WIDTH)/2, 20, SHIELDBAR_WIDTH, SHIELDBAR_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString("SHIP ENERGY",((Constants.FRAME_WIDTH-SHIELDBAR_WIDTH)/2)+10, 35);
        }
    }

    public void createMiniMap(Graphics2D g) {
        g.setColor(new Color(200,200,200,75));
        g.fillRect(Constants.FRAME_WIDTH - MINIMAP_WIDTH, Constants.FRAME_HEIGHT - MINIMAP_HEIGHT, MINIMAP_WIDTH, MINIMAP_HEIGHT);
        g.setColor(new Color(200,200,200,200));
        g.drawRect(Constants.FRAME_WIDTH - MINIMAP_WIDTH, Constants.FRAME_HEIGHT - MINIMAP_HEIGHT, MINIMAP_WIDTH, MINIMAP_HEIGHT);
        for (GameObject obj: game.getGameObjects()) {
            if (!(obj instanceof Powerup || obj instanceof EnergyShield || obj instanceof Bullet))  {
                int locX = (int)obj.s.x/(Constants.WORLD_WIDTH/MINIMAP_WIDTH);
                int locY = (int)obj.s.y/(Constants.WORLD_HEIGHT/MINIMAP_HEIGHT);
                g.setColor(Color.RED);
                if (obj instanceof Ship) g.setColor(Color.GREEN);
                if (obj instanceof Saucer) g.setColor(Color.BLUE);
                g.fillOval((Constants.FRAME_WIDTH - MINIMAP_WIDTH) + locX, (Constants.FRAME_HEIGHT - MINIMAP_HEIGHT) + locY,
                        5, 5);
            }
        }

    }


}
