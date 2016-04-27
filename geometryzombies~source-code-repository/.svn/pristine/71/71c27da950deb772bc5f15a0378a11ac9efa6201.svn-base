package geometryzombiesmayhem;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

/**
 * A Text in a Box.
 * 
 * @author Renato Lui Geh
 * @author Yan
 * @version 0.0.2
 */
public class TextBox extends ComponentP implements Paintable{
    protected String text;
    private String[] formatted;
    private Color color;
    private Color fontColor;
    private RectangularShape box;
    private int MAX_CHARS = 50;
    private int MAX_PIXELS = -1;
    private int style, size;
    private boolean usesFont = false;
    private boolean hasFontColor = false;
    private Font font;
    private boolean pagesDown = false;
    private boolean wordWrap = true;
    private boolean isFixed = false;
    
    private Vector2D offset = new Vector2D(10, 20);
    private Vector2D position;
    private boolean active = true;

    public TextBox(Vector2D position, String text, Color c, Font f, int maxChars) {
        
        this.text = text;
        this.position = position;
        
        this.setFontColor(c);
        this.color = ColorFactory.getColor(0x00, true);
        
        this.font = f;
        this.MAX_CHARS = maxChars;
        
        this.usesFont = true;
        
        Vector2D dim = this.getDimension(this.formatText(),ZM.GRAPHICS.getFontMetrics(f));
        float width = dim.getX();
        float height = dim.getY();
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public TextBox(Vector2D position, String text, Color color, int alpha, int style, int size, int maxChars) {
        
        this.text = text;
        this.position = position;
        this.MAX_CHARS = maxChars;
        
        this.size = size;
        this.style = style; 

        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    
        this.text = (text);
        
        Vector2D dim = this.getDimension(this.formatText(),ZM.GRAPHICS.getFontMetrics(ZM.GRAPHICS.getFont().deriveFont(style, size)));
        float width = dim.getX();
        float height = dim.getY();
        
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public TextBox(Vector2D position, String text, Color color, int alpha, Font f, int maxChars) {
        
        this.text = text;
        this.position = position;
        this.MAX_CHARS = maxChars;
        
        this.font = f;      

        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        
        usesFont = true;
        
        this.text = (text);
        
        Vector2D dim = this.getDimension(this.formatText(),ZM.GRAPHICS.getFontMetrics(f));
        float width = dim.getX();
        float height = dim.getY();
        
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public TextBox(String text, Vector2D position, Color color, Font font, int maxPixels) {
        this.position = position;
        this.text = text;
        this.font = font;
        this.MAX_PIXELS = maxPixels;
        this.color = color;
        
        this.usesFont = true;
        
        this.formatted = format(text);
        FontMetrics metrics = ZM.GRAPHICS.getFontMetrics(font);
        float width = maxPixels + 20; //20 for the space between text and box.
        float height = (metrics.getDescent()+metrics.getAscent())*formatted.length + 20;
        
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    private String[] format(String text) {
        FontMetrics metrics = ZM.GRAPHICS.getFontMetrics(this.font);
        String[] words = text.split(" ");
        java.util.List<String> lines = new java.util.ArrayList<>();
        int i=0;
        do {
            String line = "";
            for(;i<words.length;i++)
                if(metrics.stringWidth(line+" "+words[i])<=this.MAX_PIXELS)
                    line += " " + words[i];
                else
                    break;
            if(!line.isEmpty())
                lines.add(line);
        } while(i<words.length);
        return lines.toArray(new String[0]);
    }
    
    public int getMaxCharsPerLine() {return this.MAX_CHARS;}
    public Color getColor() {return this.color;}
    
    private Vector2D getDimension(String[] text,FontMetrics fm) {        
        float height = (fm.getHeight()) * text.length;
        float width = fm.stringWidth(text[0]);
        
        for(String s : text) 
            width = ((width >= fm.stringWidth(s))? width : fm.stringWidth(s));
        
        width += offset.getX();
        height += offset.getY();
        
        return new Vector2D(width, height);
    }
    
    public Color getFontColor() {return this.fontColor;}
    public void setColor(Color c) {this.color = c;}
    
    public void setRoundedEdges(boolean round, float wArc, float hArc) {
        this.box = round? new RoundRectangle2D.Float((float)box.getX(), (float)box.getY(), (float)box.getWidth(), (float)box.getHeight(), wArc, hArc):
                new Rectangle2D.Float((float)box.getX(), (float)box.getY(), (float)box.getWidth(), (float)box.getHeight());
    }
    
    public void setRoundedEdges(boolean round) {this.setRoundedEdges(round, 5, 5);}
    
    public void setFixedSize(float width, float height) {
        this.isFixed = true;
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    private void setFont(Graphics2D g2, int style, float size) {
        g2.setFont(g2.getFont().deriveFont(style, size));
    }
    
    public String getText() {
        return text;
    }
    
    @Override
    public Font getFont() {return font;}
    public Vector2D getPosition() {return position;}
    
    @Override
    public Dimension getSize() {return Vector2D.convertToDimension(new Vector2D((float)box.getWidth(), (float)box.getHeight()));}
    
    public Vector2D getVectoredSize() {return new Vector2D((float)box.getWidth(), (float)box.getHeight());}
    
    @Override
    public Rectangle getBounds() {return box.getBounds();}
    
    public void addText(String s) {        
        text += s;
        
        if(isFixed)
            return;
        
        Vector2D dim = this.getDimension(this.formatText(),ZM.GRAPHICS.getFontMetrics(font));
        float width = dim.getX();
        float height = dim.getY();
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public void setText(String s) {
        this.text = s;
        
        if(isFixed)
            return;
        
        Vector2D dim = this.getDimension(this.formatText(),ZM.GRAPHICS.getFontMetrics(font));
        float width = dim.getX();
        float height = dim.getY();
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }
    
    public void writeLine(String line) {        
        this.text += (line + "\n");
        
        if(isFixed)
            return;
        
        Vector2D dim = this.getDimension(this.formatText(),ZM.GRAPHICS.getFontMetrics(font));
        float width = dim.getX();
        float height = dim.getY();
        box = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public void refreshPosition(Vector2D position) {
        this.position = position;
        
        ((Rectangle2D)box).setRect(position.getX(), position.getY(), box.getWidth(), box.getHeight());
    }
    
    public final void setFontColor(Color c) {
        hasFontColor = true;
        this.fontColor = c;
    }
    
    public void pagesDown(boolean pagesDown) {
        this.pagesDown = pagesDown;
    }   
    
    public void clear() {this.text = "";}
    
    private String[] formatText() {
        if(text.isEmpty()) text = "\0";
        int n = 0;
        
         String[] lines = text.split("\n");
         for(int t = 0;t<lines.length;t++) {
             float v = (float) lines[t].length()/MAX_CHARS;
                n += ((int)v);
             if(v > (int) v) n++;
         }
         n+=lines.length>1?lines.length:0;
         
        formatted = new String[n];
        
        if(!wordWrap) {
            for(int i=0;i<n;i++)
                formatted[i] = text.substring(i*MAX_CHARS, Math.min((i+1)*MAX_CHARS,text.length()));
            return formatted;
        }
        //else
       
        int i = 0;
        for(String line:lines) {
            String[] word = line.split(" ");
            int j = 0;
            for(;i<n;i++) {
                int textSize = 0;
                if(j>=word.length)
                    break;
                formatted[i] = word[j];
                textSize += word[j].length(); j++; 
                for(;j<word.length && (textSize = (textSize + 1 + word[j].length())) <= MAX_CHARS;j++)
                    formatted[i]+= " " + word[j];
            }
        }
        
        for(int k=0;k<formatted.length;k++)
            formatted[k]=formatted[k]==null?"":formatted[k];

        return formatted=n>=lines.length?formatted:lines;
    }
    
    @Deprecated
     private String[] formatTextOld() {
        boolean isDone = false;
        int i = 0;
        
        int n = (text.length()/MAX_CHARS);
        if((float)text.length()/MAX_CHARS > n)
            n++;
        
        String[] format = new String[n];
        
        for(int j=0;j<format.length;j++) {
            format[j] = "";
        }
        
        while(!isDone) {
            if(text.length() > MAX_CHARS && (i+1) * MAX_CHARS < text.length()) {
                if(!wordWrap) {
                    format[i] = text.substring(i * MAX_CHARS, (i + 1) * MAX_CHARS);
                } else {
                    if(i==0) {
                        String line = text.substring(i * MAX_CHARS, (i + 1) * MAX_CHARS);
                        String word = getLastWord(text.split(" "), line.split(" "));

                        if(word.isEmpty()) {
                            format[i] = line;
                        } else {
                            format[i] = getRest(line.split(" "), word);
                            format[i+1] = word + " ";
                        }                    
                    } else {
                        String line, original;

                        if(!format[i].isEmpty()) {
                            line = eraseFirstWord(text.substring(i * MAX_CHARS, (i + 1) * MAX_CHARS));     
                            original = eraseFirstWord(text.substring(i * MAX_CHARS, text.length()));
                        } else {
                            line = text.substring(i * MAX_CHARS, (i + 1) * MAX_CHARS);
                            original = text.substring(i * MAX_CHARS, text.length());
                        }
                        String word = getLastWord(original.split(" "), line.split(" "));

                        if(word.isEmpty()) {
                            format[i] += line;
                        } else {
                            format[i] += getRest(line.split(" "), word);
                            format[i+1] = word + " ";
                        }
                    }
                }
            } else if(text.length() > MAX_CHARS) {
                if(!wordWrap) {
                    format[i] = text.substring((i * MAX_CHARS), text.length());
                } else {
                    String line  = text.substring((i * MAX_CHARS), text.length());
                
                    if(format[i].isEmpty()) {
                        format[i] = line;
                    } else {
                        format[i] += eraseFirstWord(line);
                    }
                }
                isDone = true;
            } else {
                if(!wordWrap) {
                    format[i] = text;
                } else {
                    if(format[i].isEmpty())
                        format[i] = text;
                    else
                        format[i] += text;
                }
                isDone = true;
            }
            i++;
        }
        
        if(wordWrap) {
            for(int k=0;k<format.length;k++) {
                if(format[k].startsWith(" ")) {
                    format[k] = format[k].substring(1, format[k].length());
                }
            }
        }
        
        return format;
    }
    
    @Deprecated
    private String getRest(String[] full, String word) {
        String[] result = new String[full.length - 1];
        String s = "";
        
        for(int i=0;i<result.length;i++) {
            if(!full[i].equals(word))
                result[i] = full[i];
            else
                continue;
        }
        
        for(int i=0;i<result.length;i++) {
            if(i==result.length-1)
                s += result[i];
            else
                s += result[i] + " ";
        }
        
        return s;
    }
    
    @Deprecated
    private String eraseFirstWord(String current) {
        String[] words = current.split(" ");
        String result = "";
        
        for(int i=1;i<words.length;i++) {
            if(i==words.length-1)
                result += words[i];
            else
                result += words[i] + " ";
        }
        
        return result;
    }
    
    @Deprecated
    private String getLastWord(String[] full, String[] line) {
        String word = "";
        
        for(int i=0;i<line.length;i++) {
            if(line[i].equals(full[i]))
                continue;
            else 
                word = full[i];
        }
        return word;
    }
    
    protected void handlePainting(Graphics2D g2, Component c) {
        Color defaultColor = g2.getColor();

        g2.setColor(color);
        g2.draw(box);
        g2.fill(box);
        g2.setColor(Color.BLACK);

        String s[] = formatted;
        if(s == null) return;

        Font defaultFont = g2.getFont();
        
        if(!usesFont)
            this.setFont(g2, style, size);
        else
            g2.setFont(font);
        
        if(pagesDown)
            if(text.length() > MAX_CHARS * ((box.getHeight()/(g2.getFontMetrics().getHeight()))/2))
                text = text.substring(MAX_CHARS, text.length());
        
        if(hasFontColor)
            g2.setColor(fontColor);
        
        if(MAX_PIXELS==-1) {
            for(int i=0;i<s.length;i++)
                g2.drawString(s[i], (int)position.getX() + offset.getX(), (int)position.getY() + (14 * i) + offset.getY());
        } else {
            FontMetrics metrics = ZM.GRAPHICS.getFontMetrics(font);
            int height = metrics.getDescent()+metrics.getAscent();
            for(int i=0;i<s.length;i++)
                g2.drawString(s[i], (int)position.getX() + offset.getX(),
                        (int)position.getY() + offset.getY() + i*height);
        }
        
        g2.setColor(defaultColor);
        g2.setFont(defaultFont);
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        handlePainting(g2, c);
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void setActive(boolean active) { this.active = active; }
    
    public static Vector2D getEstimatedSize(String caption, Font font, boolean wordWrap, final int MAX_CHARS) { 
        String text[] = getEstimatedFormat(caption, wordWrap, MAX_CHARS);
        FontMetrics fm = ZM.GRAPHICS.getFontMetrics(font);
        
        float height = (fm.getHeight()) * text.length;
        float width = fm.stringWidth(text[0]);
        
        for(String s : text) 
            width = ((width >= fm.stringWidth(s))? width : fm.stringWidth(s));
        
        return new Vector2D(width, height);
    }
    
    private static String[] getEstimatedFormat(String text, boolean wordWrap, final int MAX_CHARS) {
        String[] formatted;
        
        if(text.isEmpty()) text = "\0";
        int n = 0;
        
        String[] lines = text.split("\n");
        for(int t = 0;t<lines.length;t++) {
            float v = (float) lines[t].length()/MAX_CHARS;
            n += ((int)v);
            if(v > (int) v) n++;

        }
         
        formatted = new String[n];
        
        if(!wordWrap) {
            for(int i=0;i<n;i++)
                formatted[i] = text.substring(i*MAX_CHARS, Math.min((i+1)*MAX_CHARS,text.length()));
            return formatted;
        }

        int i = 0;
        for(String line:lines) {
            String[] word = line.split(" ");
            int j = 0;
            for(;i<n;i++) {
                int textSize = 0;
                if(j>=word.length) {
                    break;
                }
                formatted[i] = word[j];

                textSize += word[j].length(); j++; 
                for(;j<word.length && (textSize = (textSize + 1 + word[j].length())) <= MAX_CHARS;j++) {
                    formatted[i]+= " " + word[j];
                }
            }
        }
        
        return formatted;
    }

    @Override
    public void updateWindowDefaultPosition() {
        position.add(window.x, window.y);
    }
}