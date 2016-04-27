package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>A light source. Emits light.</p>
 * 
 * @author Renato Lui Geh
 */
public interface LightSource extends Serializable, geometryzombiesmayhem.circuitry.Input {    
    public Shape getShape();
    public Rectangle getBounds();
    public Rectangle2D getBounds2D();
    
    public Vector2D getPosition();
    public void setPosition(Vector2D position);
    
    public Vector2D getDirection();
    public void setDirection(Vector2D direction);
    
    public Area getArea();
    public double getAreaValue();
    
    public java.awt.Polygon getInterferenceArea(Bound.Simple e);
    
    public float currentVoltage();
    
    public Color getLightColor();
    
    public Intensity getIntensity();
    public void setIntensity(Intensity i);
    
    public Paint getPaint();
    public LightSource copy();
    
    public boolean isEnabled();
    public void setEnabled(boolean isEnabled);
    public void toggleEnabled();
    
    public boolean isOn();
    public boolean isOff();
    
    public void paintExtra(Graphics2D g2, Component c);
    
    public static class Intensity implements Serializable {
        private int range;
        private int flareWidth;
        private int luminosity;
        
        private Intensity(int range, int flareWidth, int luminosity) {
            this.range = range;
            this.flareWidth = flareWidth;
            this.luminosity = luminosity;
        }
        
        public Shape getScaled(Shape s) {
            AffineTransform at = new AffineTransform();
            at.scale((range/10)*s.getBounds().getX(), (range/10)*s.getBounds().getY());
            return at.createTransformedShape(s);
        }
        
        public Intensity copy() {return new Intensity(this.range, this.flareWidth, this.luminosity);}
        
        public int getRange() {return range;}
        public int getFlareWidth() {return flareWidth;}
        public int getLuminosity() {return luminosity;}
        
        public void setRange(int range) {this.range = range;}
        public void setFlareWidth(int flareWidth) {this.flareWidth = flareWidth;}
        public void setLuminosity(int luminosity) {this.luminosity = luminosity;}
        
        public static Intensity getInstance(int range, int flareWidth, int luminosity) {return new Intensity(range, flareWidth, luminosity);}
    }
    
    public static interface Blinkable extends Serializable {
        public long[] getPeriods();
        public void setPeriods(long... deltaTs);
        public void setBlinking(boolean blinks);
        public boolean isBlinking();
        
        public static class BlinkAction extends AwesomeAction implements Serializable {
            private int n=0;
            private long[] periods = null;
            private LightSource e;
            private boolean state = false;
            private TimeUnit unit;
            
            public BlinkAction() {};
            public BlinkAction(LightSource e, long... periods) {this(e, TimeUnit.MILLISECONDS, periods);}
            public BlinkAction(LightSource e, TimeUnit unit, long... periods) {
                this.e = e;
                this.unit = unit;
                this.periods = periods;
                this.state = LightManager.contains(e);
            }
            
            public boolean isOn() {return this.state;}
            public boolean isOff() {return !this.state;}
            
            @Override
            public void run() {
                if(n==periods.length) n=0;
                this.state = !this.state;
                this.timeToSet = unit.toNanos(periods[n++]);
            }
        }
    }
}
