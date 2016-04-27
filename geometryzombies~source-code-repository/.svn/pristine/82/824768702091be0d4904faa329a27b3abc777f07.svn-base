package geometryzombiesmayhem;

import geometryzombiesmayhem.deprecated.Animation;
import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Set;

/**
 * FXGenerator handles all animations and sound effects. It's mostly static, so a simple call to FXGenerator suffices.
 * Use the type finals to specify the animation and sound effects you want.
 * 
 * @see Animation 
 * 
 * @author Renato Lui Geh
 * @version 0.0.5
 */
public final class FXGenerator {
    
    private static java.util.Random random = ZM.RANDOM;
    private static AudioClip music;
    
    /**
     * Generates an animation with a given delay. It requires a final type from FXGenerator, a Set for the painting and the delay (in milliseconds).
     * This method automatically creates a sound effect for the animation.
     * 
     * @see Animation
     * 
     * @param type
     * @param animations
     * @param delay 
     */
    public static void generateScriptedAnimation(int type, Vector2D position, Set<Animation> animations, int delay) {
        if(type == TYPE_AMBIENT_EXPLOSION) {   
            playFx(type);
            animations.add(new Animation(delay, AssetManager.loadImage("resources/animations/explosions/explosion.gif"), position, 1500));
        }
    }
    
    /**
     * Generates an animation. It requires a type from FXGenerator specifying the animation and a Set for the painting.
     * This method automatically creates a sound effect for the animation.
     * 
     * @see Animation
     * 
     * @param type
     * @param animations 
     */
    public static void generateAnimation(int type, Vector2D position, Set<Animation> animations) {
        if(type == TYPE_AMBIENT_EXPLOSION) {   
            playFx(type);
            animations.add(new Animation(AssetManager.loadImage("resources/animations/explosions/explosion.gif"), position, 1500));
        }
    }
    
    /**
     * Main method for specifying a sound effect. Provide a type from the FXGenerator.
     * 
     * <b>NOTE: </b>This method DOES NOT play the sound. Together with play(Type), this method <u>specifies</u> the type of the sound effect.
     * 
     * @param type 
     */
    public static void playFx(int type) {
        if(type <= TYPE_PLAYER_RANDOM)
            playPlayer(type);
        else if(type == TYPE_ZOMBIE_RANDOM || type == TYPE_ZOMBIE_HIT)
            playZombie(type);
        else if(type == TYPE_AMBIENT_RANDOM || type == TYPE_AMBIENT_EXPLOSION)
            playAmbient(type);
        else if(type >= TYPE_MENU_SELECT && type <= TYPE_MENU_END)
            playMenu(type);
        else if(type == TYPE_GUN_NOISE || type == TYPE_GUN_LASER)
            playGun(type);
        
        //DEBUG ONLY!
        //playRandom();
    }
    
    /**
     * Sends a message to play to start the given sound effect.
     * 
     * @param type 
     */
    public static void playAmbient(int type) {
        if(type == TYPE_AMBIENT_EXPLOSION)
            play("ambient/explosions", false);
        else if(type == TYPE_AMBIENT_RANDOM)
            play("ambient", true);
    }
    /**
     * Sends a message to play to start the given sound effect.
     * 
     * @param type 
     */
    public static void playPlayer(int type) {
        play("player", false);
    }
    /**
     * Sends a message to play to start the given sound effect.
     * 
     * @param type 
     */
    public static void playZombie(int type) {
        play("zombie", false);
    }
    /**
     * Sends a message to play to start the given sound effect.
     * 
     * @param type 
     */
    public static void playMenu(int type) {
        if(type == TYPE_MENU_SELECT)
            play("menu/select", false);
    }
    /**
     * Sends a message to play to start the given sound effect.
     * 
     * @param type 
     */
    public static void playGun(int type) {
        play("gun", false);
    }
    
    /**
     * Plays a random sound.
     * <b>DEBUG AND TESTING ONLY!</b>
     */
    public static void playRandom() {
        try {
            Applet.newAudioClip(AssetManager.load("resources/sounds/fx/gun/" + 
                    random.nextInt(4) + ".wav")).play();
        } catch(Exception exc) {
            exc.printStackTrace();
        } 
    }
    
    /**
     * The play method simply plays a sound effect given a path to the type of sound effect and 
     * a boolean to indicate whether the sound effect is supposed to loop or not.
     * 
     * @param path
     * @param loop 
     */
    public static void play(String path, boolean loop) {
        try {
            if(!loop)
                Applet.newAudioClip(AssetManager.load("resources/sounds/fx/" + path + "/" + random.nextInt(4) + ".wav")).play();
            else
                Applet.newAudioClip(AssetManager.load("resources/sounds/fx/" + path + "/" + random.nextInt(4) + ".wav")).loop();
        } catch(Exception exc) {
            System.err.println(exc.getMessage());
        }
    }
    
    public static void playMusic() {
        try {
            music = Applet.newAudioClip(AssetManager.load("resources/sounds/music/ambient/music_low.wav"));
            music.loop();
        } catch(Exception exc) {
            System.err.println(exc.getMessage());
        }
    }
        
    public static final int TYPE_PLAYER_POINTS = 0;
    public static final int TYPE_PLAYER_HIT = 1;
    public static final int TYPE_PLAYER_POWERUP = 2;
    public static final int TYPE_PLAYER_JUMP = 3;
    public static final int TYPE_PLAYER_RANDOM = 4;
    
    public static final int TYPE_ZOMBIE_HIT = 5;
    public static final int TYPE_ZOMBIE_RANDOM = 6;
    
    public static final int TYPE_AMBIENT_RANDOM = 7;
    public static final int TYPE_AMBIENT_EXPLOSION = 8;
    
    public static final int TYPE_MENU_SELECT = 9;
    public static final int TYPE_MENU_START = 10;
    public static final int TYPE_MENU_END = 11;

    public static final int TYPE_GUN_NOISE = 12;
    public static final int TYPE_GUN_LASER = 13;
}