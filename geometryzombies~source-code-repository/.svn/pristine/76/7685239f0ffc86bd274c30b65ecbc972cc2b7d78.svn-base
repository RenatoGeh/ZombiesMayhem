package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * A Collection of <code>Words</code>
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 * 
 * @see Word
 * @see Letter
 */
public class Sentence implements Paintable {
    protected Vector2D position;
    private String s;
    protected ArrayList<Word> words = new ArrayList<>();
    private float spacing;
    private float whitespace;
    private boolean isScaled = false;
    private float scale = 1;
    protected boolean isActive = true;
    
    public Sentence(Vector2D position, String s, float whitespace, float spacing) {
        this.position = position;
        this.s = s;
        this.spacing = spacing;
        this.whitespace = whitespace;
        
        this.parseSentence(s);
    }
    
    public Sentence(Vector2D position, String s, float whitespace, float spacing, float scale) {
        this.position = position;
        this.s = s;
        this.spacing = spacing;
        this.whitespace = whitespace;
        
        this.scale = scale;
        this.isScaled = true;
        
        this.parseSentence(s);
    }
    
    public Sentence(Vector2D position, String s, float whitespace, float spacing, float scale, Color c) {
        this.position = position;
        this.s = s;
        this.spacing = spacing;
        this.whitespace = whitespace;
        
        this.scale = scale;
        this.isScaled = true;
        
        this.parseColoredSentence(s, c);
    }
    
    private void parseColoredSentence(String s, Color c) {
        String[] w = this.separateWords(s);
        float width = 0;
        
        for(int i=0;i<w.length;i++) {
            words.add(new Word(position.add((i*whitespace) + width, 0), w[i], spacing, scale, c));
            width = words.get(i).getWidth();
        }
    }
    
    private void parseSentence(String s) {
        String[] w = this.separateWords(s);
        float width = 0;
        
        for(int i=0;i<w.length;i++) {
            if(!isScaled)
                words.add(new Word(position.add((i*whitespace) + width, 0), w[i], spacing));
            else
                words.add(new Word(position.add((i*whitespace) + width, 0), w[i], spacing, scale));
            width = words.get(i).getWidth();
        }
    }
    
    public void reparseSentence(String s) {
        String[] w = this.separateWords(s);
        float width = 0;
        
        for(int i=0;i<w.length;i++) {
            if(!isScaled)
                words.set(i, new Word(position.add(width + (i*whitespace), 0), w[i], spacing));
            else
                words.set(i, new Word(position.add((i*whitespace) + width, 0), w[i], spacing, scale));
            width = words.get(i).getWidth();
        }
    }
    
    public int getWidth() {
        int width = 0;
        for(Word w : words) {
            width += w.getWidth();
        }
        return width;
    }
    
    public String getText() {return s;}
    
    private String[] separateWords(String s) {
        return s.split(" ");
    }
    
    public float getSpacing() {return spacing;}
    public float getWhitespace() {return whitespace;}
    public float getScale() {return scale;}
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        for(Word w : words) 
            w.paint(g2, c);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}