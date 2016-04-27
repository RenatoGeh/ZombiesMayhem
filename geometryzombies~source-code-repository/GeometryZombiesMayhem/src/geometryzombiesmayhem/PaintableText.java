package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Renato Lui Geh
 */
public class PaintableText implements Paintable {
    private Vector2D position;
    private Font font;
    private String text;
    private Color color;
    private int maxChars = 0;
    
    public PaintableText(Vector2D position, String text, Font font, Color color, int maxChars) {
        this(position, text, font, color);
        if(this.maxChars<text.length()) throw new IllegalArgumentException("Maximum numbers of characters per line is greater than the length of the provided text.");
        this.maxChars = maxChars;
    }
    public PaintableText(Vector2D position, String text, Font font, Color color) {
        this.position = position;
        this.text = text;
        this.font = font;
        this.color = color;
    }

    @Override
    public boolean isActive() {return true;}
    @Override
    public void setActive(boolean active) {}

    private void drawText(Graphics2D g2, Component c) {
        if(maxChars==0) {g2.drawString(text, position.getX(), position.getY()); return;}
        
        String[] words = text.split(" ");
        ArrayList<String> lines = new ArrayList<>();
        lines.add(words[0]);
        
        for(int i=1;i<words.length;i++)
            if(lines.get(lines.size()-1).length()+1+words[i].length()<=maxChars)
                lines.set(lines.size()-1, lines.get(lines.size()-1).concat(" "+words[i].length()));
        
        float height = ZM.GRAPHICS.getFontMetrics(font).getHeight();
        
        for(int j=0;j<lines.size();j++)
            g2.drawString(lines.get(j), position.getX(), position.getY()+j*height);
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        Color defColor = g2.getColor();
        Font defFont = g2.getFont();
        g2.setFont(font);
        g2.setColor(color);
        this.drawText(g2, c);
        g2.setColor(defColor);
        g2.setFont(defFont);
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }

}
