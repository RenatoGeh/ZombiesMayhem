/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.circuitry;

import geometryzombiesmayhem.*;
import java.awt.Component;
import java.awt.Graphics2D;

/**
 *
 * @author Yan
 */
public class Emitter extends CircuitryBody implements Input{
    Emittable em;
    boolean eOS;
    Method m = Method.ONCE;
    private boolean emmited = false;
    MutableImage i = EMITTER.resize(.3f, .3f);
    float oldVoltage = 0;

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(GameFrame.onEditor) g2.drawImage(i,(int)position.getX(),(int)position.getY(),c);
    }

    @Override
    public Body copy() {
        return new Emitter(em,position,eOS);
    }
    

    public enum Method {ONCE,EVERY;};
    
    public void doEmmit() {
        Emittable e = em.getCopy();
        e.emmit(position.copy());
        emmited = true;
    }
    
    public Emitter() {super();}
    
    public Emitter(Emittable emittable, Vector2D position, boolean emmitOnStart) {       
        super(position);
        em = emittable;
        this.position = position;
        eOS = emmitOnStart; //toimplememnt
        i = EMITTER;
        i = i.resize(0.3f, 0.3f);
        this.bounds = new RotatableRectangle(position.getX(),position.getY(),i.getWidth(),i.getHeight(),0);
    }

    
    @Override
    public void updateVoltage(Output o,float newVoltage) {
        if((m == Method.ONCE && !emmited && newVoltage>0) || (m == Method.EVERY && oldVoltage == 0f && newVoltage!=0f)){
            doEmmit();
        } 
        oldVoltage = newVoltage;
    }
    
    @Override
    public void connected(Output o) {
        
    }
    @Override
    public void disconnected(Output o) {
        
    }
    
    @Override
    public float minX() {return this.position.getX();}
    @Override
    public float minY() {return this.position.getY();}
    @Override
    public float maxX() {return this.position.getX() + this.i.getWidth();}
    @Override
    public float maxY() {return this.position.getY() + this.i.getHeight();}
    
    private static MutableImage EMITTER = AssetManager.loadImage("resources/items/emitter.png");
}
