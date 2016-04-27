package geometryzombiesmayhem;

import java.io.IOException;
import javax.sound.sampled.*;

/**
 * <p>An individual line of sound.</p>
 * 
 * @author Renato Lui Geh
 */
public class AudioLine {
    private String name;
    private String path;
    private boolean loop;
    private Clip clip;
    private AudioInputStream audioStream;
    private boolean permanent = false;
    private FloatControl control;
    private boolean isExternal = false;
    
    public AudioLine(String name, String path, boolean loop) {
        this.name = name;
        this.path = path;
        this.loop = loop;
        
        initLine(path, loop);
    }
    
    public AudioLine(String name, String path, boolean loop, boolean permanent) {        
        this.permanent = permanent;
        
        this.name = name;
        this.path = path;
        this.loop = loop;
        
        initLineNoSound(path, loop);
    }
    
    public AudioLine(String name, String path, boolean loop, boolean permanent, boolean startsOnCreation) {
        this.permanent = permanent;
        
        this.name = name;
        this.path = path;
        this.loop = loop;
        
        if(startsOnCreation)
            initLine(path, loop);
        else
            initLineNoSound(path, loop);
    }
    
    public AudioLine(String name, String path, boolean loop, boolean permanent, boolean startsOnCreation, boolean isExternal) {
        this.permanent = permanent;
        
        this.name = name;
        this.path = path;
        this.loop = loop;
        this.isExternal = isExternal;
        
        if(startsOnCreation)
            initLine(path, loop);
        else
            initLineNoSound(path, loop);
    }
    
    private void initLine(String path, boolean loop) {
        try {
            clip = AudioSystem.getClip();
            audioStream = AudioSystem.getAudioInputStream(isExternal? AssetManager.loadExternal(path) : AssetManager.load(path));
            
            clip.open(audioStream);
            
            if(!loop)
                clip.start();      
            else 
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            this.control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            
        } catch(LineUnavailableException | UnsupportedAudioFileException | IOException exc) {
            System.err.println(exc.getCause().toString() + ": " + exc.getMessage());
        }
    }
    
    private void initLineNoSound(String path, boolean loop) {
        try {
            clip = AudioSystem.getClip();

            audioStream = AudioSystem.getAudioInputStream(isExternal? AssetManager.loadExternal(path) : AssetManager.load(path));

            clip.open(audioStream);            

            this.control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch(LineUnavailableException | UnsupportedAudioFileException | IOException exc) {
            System.err.println(exc.getCause().toString() + ": " + exc.getMessage());
        }
    }
    
    public void setVolume(int vol) {this.control.setValue(this.scale(vol));}
    
    /**
     * Scales the 0-100 integer number to a decibel scale (logarithmic).
     * 
     * @param vol
     * @return 
     */
    private float scale(int vol) {
        double value = (double)vol;
        
        value = value/100;
        
        value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
        
        return (float)(Math.log(value)/Math.log(10.0)*20.0);
    }
    
    /**
     * Scales a logarithmic-decibel number to a 0-100 integer number.
     * 
     * @param dB
     * @return 
     */
    private int unscale(float dB) {
        return (int)(100*((float)Math.pow(10, dB/20)));
    }
    
    public float getVolume() {return unscale(control.getValue());}
    public boolean isPermanent() {return permanent;}
    public Clip getClip() {return clip;}
    public String getName() {return name;}
    public boolean isActive() {return clip.isActive();}
    public synchronized void play() {clip.start();}
    public synchronized void pause() {clip.stop();}
    public synchronized void stop() {clip.stop(); clip.flush(); clip.close();}
    public synchronized void restart() {stop(); initLine(path, loop);}
}