package utilities;

// change package name to fit your own package structure!

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

// SoundManager for Asteroids

public class SoundManager {

    static int nBullet = 0;
    static boolean thrusting = false;

    // this may need modifying
    final static String path = "sounds/";

    // note: having too many clips open may cause
    // "LineUnavailableException: No Free Voices"
    public final static Clip[] bullets = new Clip[15];

    public final static Clip bangLarge = getClip("bangLarge");
    public final static Clip bangMedium = getClip("bangMedium");
    public final static Clip bangSmall = getClip("bangSmall");
    public final static Clip beat1 = getClip("beat1");
    public final static Clip beat2 = getClip("beat2");
    public final static Clip extraShip = getClip("extraShip");
    public final static Clip fire = getClip("fire");
    public final static Clip saucerBig = getClip("saucerBig");
    public final static Clip powerup = getClip("saucerSmall");
    public final static Clip thrust = getClip("thrust");

    public final static Clip[] clips = {bangLarge, bangMedium, bangSmall, beat1, beat2,
            extraShip, fire, saucerBig, powerup, thrust};

    static {
        for (int i = 0; i < bullets.length; i++)
            bullets[i] = getClip("fire");
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 20; i++) {
            fire();
            Thread.sleep(100);
        }
        for (Clip clip : clips) {
            play(clip);
            Thread.sleep(1000);
        }
    }

    // methods which do not modify any fields

    public static void play(Clip clip) {
        clip.setFramePosition(0);
        clip.loop(0);
        clip.start();
    }

    private static Clip getClip(String filename) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path
                    + filename + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    // methods which modify (static) fields

    public static void fire() {
        // fire the n-th bullet and increment the index
        Clip clip = bullets[nBullet];
        clip.setFramePosition(0);
        clip.start();
        nBullet = (nBullet + 1) % bullets.length;
    }


    public static void load() {
        //pass
    }


}
