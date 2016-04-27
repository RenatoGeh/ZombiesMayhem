package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * A collection of <code>Letters</code>
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 * 
 * @see Letter
 */
public class Word {
    private ArrayList<Letter> string = new ArrayList<>();
    private Vector2D position;
    private float spacing;
    private boolean isScaled = false;
    private float scale = 1;
    
    public Word(Vector2D position, String s, float spacing) {
        this.position = position;
        this.spacing = spacing;
        
        this.parseString(s);
    }
    
    public Word(Vector2D position, String s, float spacing, float scale) {
        this.position = position;
        this.spacing = spacing;
        
        this.scale = scale;
        this.isScaled = true;
        
        this.parseString(s);
    }
    
    public Word(Vector2D position, String s, float spacing, float scale, Color c) {
        this.position = position;
        this.spacing = spacing;
        
        this.scale = scale;
        this.isScaled = true;
        
        this.parseColoredString(s, c);
    }
    
    private void parseColoredString(String s, Color c) {
        float width = 0;
        
        for(int i=0;i<s.length();i++) {
            string.add(new Letter(position.add(width + (i * spacing), 0), s.charAt(i), scale, c));
            width += string.get(i).getWidth();
        }
    }
    
    private void parseString(String s) {
        float width = 0;
        
        for(int i=0;i<s.length();i++) {
            if(!isScaled)
                string.add(new Letter(position.add(width + (i * spacing), 0), s.charAt(i)));
            else
                string.add(new Letter(position.add(width + (i * spacing), 0), s.charAt(i), scale));
            width += string.get(i).getWidth();
        }
    }
    
    public float getWidth() {
        float result = 0;
        
        for(Letter l : string) {
            result += l.getWidth();
        }
        
        return result;
    }
    
    public void reparseString(String s) {
        String path = Letter.resourcesPath;
        float width = 0;
        
        for(int i=0;i<s.length();i++) {
            if(!isScaled)
                string.set(i, new Letter(position.add((i * spacing) + width, 0), s.charAt(i)));
            else
                string.set(i, new Letter(position.add(width + (i * spacing), 0), s.charAt(i), scale));
            width += string.get(i).getWidth();
        }
    }
    
    public void paint(Graphics2D g2, Component c) {
        for(Letter l : string)
            l.paint(g2, c);
    }
    
    public ArrayList<Letter> getLetters() {return string;}
}