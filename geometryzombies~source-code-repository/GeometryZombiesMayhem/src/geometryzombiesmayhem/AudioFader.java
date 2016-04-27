package geometryzombiesmayhem;

import javax.sound.sampled.FloatControl;

/**
 * <p>Fades the sound to a minimum or maximum level.</p>
 * 
 * @author Renato Lui Geh
 */
public final class AudioFader extends Thread {
    private float currDB = 0f;
    private float targetDB = 0f;
    private float fadePerStep = 1;
    private boolean fading = false;
    private int speed;
    private FloatControl control;
    
    public AudioFader(AudioLine line, int speed, double value) {
        control = (FloatControl)line.getClip().getControl(
                FloatControl.Type.MASTER_GAIN);
        
        this.speed = speed;
        this.shiftVolumeTo(value);
    }
    
    public void setVolume(AudioLine al, double value) {        
        value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
        
        try {
            float dB = (float)(Math.log(value)/Math.log(10.0)*20.0);
            control.setValue(dB);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void shiftVolumeTo(double value) {
        value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
        targetDB = (float)(Math.log(value)/Math.log(10.0)*20.0);
        if (!fading) {
            Thread t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run()
    {
        fading = true;
        if (currDB > targetDB) {
            while (currDB > targetDB) {
                currDB -= fadePerStep;
                control.setValue(currDB);
                try {Thread.sleep(speed);} catch (Exception exc) {}
            }
        }
        else if (currDB < targetDB) {
            while (currDB < targetDB) {
                currDB += fadePerStep;
                control.setValue(currDB);
                try {Thread.sleep(speed);} catch (Exception exc) {}
            }
        }
        fading = false;
        currDB = targetDB;
    }
}