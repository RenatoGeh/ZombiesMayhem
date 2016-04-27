package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yan
 */
public abstract class Person extends Body{
    protected int level = 1;
    protected float health,maxHealth;
    protected Sprite sprite = null;

    /**
     * Serialization constructor.
     */
    protected Person() {super();};
    
    private Person(float mass,float x,float y,float Vx,float Vy,float health) {
        super(mass,x,y,Vx,Vy);
        this.health = health;
        maxHealth = health;
        
        this.health = health*level;
    }
    
    protected Person(float mass, Sprite sprite, float x, float y, float health) {
        this(mass, x, y, 0, 0, health);
        
        this.sprite = sprite;
        if(sprite!=null) this.sprite.refreshPosition(this.position);
    }
    
    protected Person(float mass, Sprite sprite, float x, float y, float Vx, float Vy, float health) {
        this(mass, x, y, Vx, Vy, health);
        
        this.sprite = sprite;
        this.sprite.refreshPosition(this.position);
    }
    
    protected void setSpeed(float modifier) {
        sprite.setSpeed(modifier);
    }
    
    public void heal(float h) { 
        health = Math.min(health + h, maxHealth);
        
        int x = ZM.RANDOM.nextBoolean()? ZM.RANDOM.nextInt(50) : -ZM.RANDOM.nextInt(50);
        int y = ZM.RANDOM.nextBoolean()? ZM.RANDOM.nextInt(5) : -ZM.RANDOM.nextInt(5);
        Paintables.register(new ImageFader(1.5f).new Text(this.position.add(x, y-75), Float.toString(h), ZM.GRAPHICS.getFont().deriveFont(Font.BOLD, 20), Color.GREEN, .05f));
    }
    
    public void damage(float d) { 
        health = Math.max(health - d, 0);
        
        int x = ZM.RANDOM.nextBoolean()? ZM.RANDOM.nextInt(50) : -ZM.RANDOM.nextInt(50);
        int y = ZM.RANDOM.nextBoolean()? ZM.RANDOM.nextInt(5) : -ZM.RANDOM.nextInt(5);
        Paintables.register(new ImageFader(1.5f).new Text(this.position.add(x, y-75), Float.toString(d), ZM.GRAPHICS.getFont().deriveFont(Font.BOLD, 20), Color.RED, .05f));
    }
    
    public void setHealth(float health) {this.health = health; damage(0); heal(0); }
    
    public float getHealth() { return health; }
    
    public int getLevel() {return level;}
    
    public void kill() { health = 0; }
    
    public Sprite getSprite() {return sprite;}
    
    public boolean isDead() {
        return health==0;
    }
    
    /**
     * Overrides the Body's paint method.
     * 
     * @param c
     * @param g 
     */
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        sprite.paint(g2, c,position);
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        sprite.paint(g2, c, at, position);
    }
    
    

    @Override
    public float minX() {
        return position.getX();
    }

    @Override
    public float maxX() {
            return position.getX() + sprite.frameWidth;
    }

    @Override
    public float minY() {
        return position.getY();
    }

    @Override
    public float maxY() {
            return position.getY() + sprite.frameHeight;
    }

    @Override
    public void setMaxX(float x) {
            position.setX(x - sprite.frameWidth);
    }

    @Override
    public void setMaxY(float y) {
            position.setY(y - sprite.frameHeight);
    }

    @Override
    public void setMinX(float x) {
        position.setX(x);
    }

    @Override
    public void setMinY(float y) {
        position.setY(y);
    }
}
