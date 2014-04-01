package utilities;

import xpilot.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;


public class JEasyFrameFull extends JFrame {
    public final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public final static GraphicsDevice device = env.getScreenDevices()[0];
    public static final Rectangle RECTANGLE = device.getDefaultConfiguration().getBounds();
    public static final int MAX_WIDTH = RECTANGLE.width;
    public static final int MAX_HEIGHT = RECTANGLE.height;

    public Component comp;

    public JEasyFrameFull(Component comp) {
        super();
        this.comp = comp;
        getContentPane().add(BorderLayout.CENTER, comp);
        //comp.setPreferredSize(new Dimension (Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));
        //this.setUndecorated(true);
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }

    public void setDimensionsToFullScreen() {
        Constants.FRAME_HEIGHT = MAX_HEIGHT;
        Constants.FRAME_WIDTH = MAX_WIDTH;
    }

    public void setDimensionsToSmallScreen() {
        Constants.FRAME_HEIGHT = 600;
        Constants.FRAME_WIDTH = 800;
    }

    public void setSmallScreen(Component comp) {
        dispose();
        this.comp = comp;
        getContentPane().removeAll();
        getContentPane().add(BorderLayout.CENTER, comp);
        this.setUndecorated(false);
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }

    public void setFullScreen(Component comp) {
        dispose();
        this.comp = comp;
        getContentPane().removeAll();
        getContentPane().add(BorderLayout.CENTER, comp);
        comp.setPreferredSize(new Dimension (Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));
        this.setUndecorated(true);
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
        repaint();
    }
}
