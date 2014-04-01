package xpilot;

import utilities.ImageManager;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Constants {
	  // frame dimensions 
	  public static int FRAME_HEIGHT = 600;
	  public static int FRAME_WIDTH =800;
	  public static final Dimension FRAME_SIZE = 
	    new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
      public static final Random RANDOM = new Random();
	  public static final int MAX_SPEED = 100;
	  public static final int DELAY = 20;
	  public static final double DT = DELAY / 1000.0;
      public static final int WORLD_WIDTH = 2000;
      public static final int WORLD_HEIGHT = 2000;
      public static final int BACKGROUND_PARTICLES = 1000;
	  // number of xpilot on 1st level
    public static Image ASTEROID1, MILKYWAY1;
    static {
        try {
            ASTEROID1 = ImageManager.loadImage("asteroid1");
            MILKYWAY1 = ImageManager.loadImage("milkyway1");
        } catch (IOException e) { System.exit(1); }
    }
	}
