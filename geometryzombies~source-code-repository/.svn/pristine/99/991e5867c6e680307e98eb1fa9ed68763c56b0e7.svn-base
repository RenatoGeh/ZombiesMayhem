package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.AwesomeAction;
import geometryzombiesmayhem.AwesomeTimer;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Renato Lui Geh
 */
public class Button extends CircuitryBody implements Output {
    private float voltage, deviation;
    private MutableImage im;
    private List<Input> inputs = new ArrayList<>();
    private boolean isRunning = false;

    public Button() {super();}
    public Button(Vector2D position) {this(1, position);}
    public Button(float voltage, Vector2D position) {
        super(position);
        this.voltage = this.deviation = voltage;
        this.interactible = true;
    }
    
    @Override
    public void add(Input in) {
        this.inputs.add(in);
        in.connected(this);
        in.updateVoltage(this, this.voltage);
    }
    
    @Override
    public void remove(Input in) {this.inputs.remove(in);}
    
    private void press() {
        if(this.isRunning) return;
        this.voltage = deviation;
        AwesomeTimer.addAction(timer, DEFAULT_PRESS_TIME, false);
        this.isRunning = true;
    }
    @Override
    public float currentVoltage() {return this.voltage;}
    
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        g2.drawImage(im, null, (int)position.getX(), (int)position.getY());
    }
    
    private AwesomeAction timer = new AwesomeAction() {
        @Override
        public void run() {
            Button.this.voltage = 0;
            for(Input in : Button.this.inputs) 
                in.updateVoltage(Button.this, Button.this.voltage);
            Button.this.isRunning = false;
        }
    };
    
    private static final long DEFAULT_PRESS_TIME = TimeUnit.MILLISECONDS.toNanos(2500);

    @Override
    public Body copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
