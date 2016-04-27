package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.FlareShape;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.LightManager;
import geometryzombiesmayhem.LightSource;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.Vector2D;
import java.awt.Component;
import java.awt.Graphics2D;

/**
 * <p>Emits rainbows. And light.</p>
 * @author Renato Lui Geh
 */
public class LightBulb extends CircuitryBody implements Input {
    private LightSource source;
    private MutableImage im;
    private boolean visible;
    private boolean state = false;
    
    public LightBulb() {super();}
    public LightBulb(LightSource source, Vector2D position) {this(source, 0, position);}
    public LightBulb(LightSource source, float voltage, Vector2D position) {this(source, voltage, false, position);}
    public LightBulb(LightSource source, float voltage, boolean visible, Vector2D position) {
        super(position);
        this.visible = visible;
        this.source = source;
        this.source.setEnabled(true);
        this.position = position;
        this.state = voltage!=0;
        
        this.updateVoltage(null, voltage);
        
        this.im = voltage==0?LightBulb.BULB_OFF:LightBulb.BULB_ON;
    }
    
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(GameFrame.onEditor)
            g2.drawImage(im, null, (int)position.getX(), (int)position.getY());
    }
    
    public float currentVoltage() {return source.currentVoltage();}
    
    @Override
    public synchronized final void updateVoltage(Output out, float newVoltage) {
        source.updateVoltage(out, newVoltage);
        if(newVoltage > 0 && this.im == LightBulb.BULB_OFF) this.im = LightBulb.BULB_ON;
        else if(newVoltage <= 0 && this.im == LightBulb.BULB_ON) this.im = LightBulb.BULB_OFF;
        if(source.getIntensity().getLuminosity()==LightManager.getDarkness() && this.state) {
            this.state = false;
            if(LightManager.contains(source))
                LightManager.remove(source);
        } else if(!state) {
            this.state = true;
            if(!LightManager.contains(source))
                LightManager.register(source);
        }
    }
    
    @Override
    public float minX() {return this.position.getX();}
    @Override
    public float minY() {return this.position.getY();}
    @Override
    public float maxX() {return this.position.getX() + this.im.getWidth();}
    @Override
    public float maxY() {return this.position.getY() + this.im.getHeight();}
    

    @Override
    public void connected(Output o) {}
    @Override
    public void disconnected(Output out) {};

    @Override
    public Body copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static class Conical extends LightBulb {
        public Conical() {super();}
        public Conical(Vector2D position, Vector2D direction, float width, float angle) {this(0, position, direction, width, angle);}
        public Conical(float voltage, Vector2D position, Vector2D direction, float width, float angle) {this(voltage, false, position, direction, width, angle);}
        public Conical(float voltage, boolean visible, Vector2D position, Vector2D direction, float width, float angle) {
            super(new FlareShape.Conical(position, direction, width, angle, LightManager.getColor()),
                    voltage, visible, position);
        }
        public Conical(float voltage, boolean visible, FlareShape.Conical source) {super(source, voltage, visible, source.getPosition());}
    }
    
    public static class Elliptic extends LightBulb {
        public Elliptic() {super();}
        public Elliptic(Vector2D position, Vector2D direction, Vector2D size, int range, int width, int luminosity) {this(0, position, size, range, width, luminosity);}
        public Elliptic(float voltage, Vector2D position, Vector2D size, int range, int width, int luminosity) {this(voltage, false, position, size, range, width, luminosity);}
        public Elliptic(float voltage, boolean visible, Vector2D position, Vector2D size, int range, int width, int luminosity) {
            super(new FlareShape.Elliptic(position, size, range, width, luminosity, LightManager.getColor()),
                    voltage, visible, position);
        }
        public Elliptic(float voltage, boolean visible, FlareShape.Elliptic source) {super(source, voltage, visible, source.getPosition());}
    }
    
    public static class Polygonal extends LightBulb {
        public Polygonal() {super();}
        public Polygonal(Vector2D[] points, int range, int flareWidth, int luminosity) {this(1, false, points, range, flareWidth, luminosity);}
        public Polygonal(float voltage, Vector2D[] points, int range, int flareWidth, int luminosity) {this(voltage, false, points, range, flareWidth, luminosity);}
        public Polygonal(float voltage, boolean visible, Vector2D[] points, int range, int flareWidth, int luminosity) {
            super(new FlareShape.Polygonal(points, range, flareWidth, luminosity, LightManager.getColor()), 
                    voltage, visible, points[0]);
        }
        public Polygonal(float voltage, boolean visible, FlareShape.Polygonal source) {super(source, voltage, visible, source.getPosition());}
    }
    
    public static class Trapezoid extends LightBulb {
        public Trapezoid() {super();}
        public Trapezoid(Vector2D focus, Vector2D direction, float minBase, float maxBase, float width, int flareWidth) {this(0, focus, direction, minBase, maxBase, width, flareWidth);}
        public Trapezoid(float voltage, Vector2D focus, Vector2D direction, float minBase, float maxBase, float width, int flareWidth) {this(voltage, false, focus, direction, minBase, maxBase, width, flareWidth);}
        public Trapezoid(float voltage, boolean visible, Vector2D focus, Vector2D direction, float minBase, float maxBase, float width, int flareWidth) {
            super(new FlareShape.Trapezoid(focus, direction, minBase, maxBase, width, flareWidth, LightManager.getColor()),
                    voltage, visible, focus);
        }
        public Trapezoid(float voltage, boolean visible, FlareShape.Polygonal source) {super(source, voltage, visible, source.getPosition());}
    }
    
    private static final MutableImage BULB_ON = AssetManager.loadImage("resources/circuitry/light_bulb/bulb_on.png");
    private static final MutableImage BULB_OFF = AssetManager.loadImage("resources/circuitry/light_bulb/bulb_off.png");
}
