package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.AwesomeAction;
import geometryzombiesmayhem.AwesomeTimer;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Renato Lui Geh
 */
public class Timer extends OutputBody {
    private float voltage;
    private float discharge;
    private long tempo;
    private TimeUnit unit;
    private boolean repeats;
    
    public Timer(Vector2D position, float voltage, float discharge, long tempo, TimeUnit tunit, boolean repeats) {
        this.voltage = voltage;
        this.discharge = discharge;
        this.tempo = tempo;
        this.unit = tunit;
        this.repeats = repeats;
        
        AwesomeTimer.addAction(action, tempo, repeats, tunit);
    }

    public void setTempo(long tempo) {this.action.timeToSet=unit.toNanos(this.tempo=tempo);}
    
    private AwesomeAction action = new AwesomeAction() {
        @Override
        public void run() {
            for(Input in : Timer.this.inputs) {
                in.updateVoltage(Timer.this, Timer.this.discharge);
                in.updateVoltage(Timer.this, Timer.this.voltage);
            }
        }
    };
    
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(GameFrame.onEditor)
            g2.drawImage(TIMER_IMAGE, null, (int)position.getX(), (int)position.getY());
    }

    @Override
    public Body copy() {
        return new Timer(this.position.copy(), this.currentVoltage(), this.getDischarge(), 
                this.tempo, unit, repeats);
    }

    public float getDischarge() {return discharge;}
    @Override
    public float currentVoltage() {return voltage;}
    
    private static final MutableImage TIMER_IMAGE = AssetManager.loadImage("resources/circuitry/timer/timer.png");
}
