package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * A bitmap letter.
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class Letter {
    private Image letterImage;
    private char character;
    private Vector2D position;
    private boolean isScaled = false;
    private float scale = 1;
    
    public Letter(Vector2D position, char character) {
        this.character = character;
        this.position = position;
        
        this.parseChar(character);
    }
    
    public Letter(Vector2D position, char character, float scale) {
        this.character = character;
        this.position = position;
        
        this.scale = scale;
        this.isScaled = true;
        
        this.parseChar(character);
    }
    
    public Letter(Vector2D position, char character, float scale, Color c) {
        this.character = character;
        this.position = position;
        
        this.scale = scale;
        this.isScaled = true;
        
        this.parseFixedChar(character);
        
        this.letterImage = this.getColoredLetter(c);
    }
    
    private void parseChar(char character) {
        if(character != CHAR_QUESTION && character != CHAR_EXCLAMATION && character != CHAR_MONEY) {
            letterImage = this.getLetterImage(resourcesPath + Character.toString(character).toLowerCase() + ".png", scale);
        } else {            
            if(character == CHAR_EXCLAMATION)
                letterImage = this.getLetterImage(resourcesPath + "exclamation.png", scale);
            else if(character == CHAR_QUESTION)
                letterImage = this.getLetterImage(resourcesPath + "question.png", scale);
            else if(character == CHAR_MONEY)
                letterImage = this.getLetterImage(resourcesPath + "money.png", scale);
        }
    }
    
    private void parseFixedChar(char character) {
        if(character != CHAR_QUESTION && character != CHAR_EXCLAMATION && character != CHAR_MONEY) {
            letterImage = this.getLetterImage(resourcesPath + Character.toString(character).toLowerCase() + ".png", 1);
        } else {            
            if(character == CHAR_EXCLAMATION)
                letterImage = this.getLetterImage(resourcesPath + "exclamation.png", 1);
            else if(character == CHAR_QUESTION)
                letterImage = this.getLetterImage(resourcesPath + "question.png", 1);
            else if(character == CHAR_MONEY)
                letterImage = this.getLetterImage(resourcesPath + "money.png", 1);
        }
    }
    
    public char getChar() {
        return this.character;
    }
    
    public float getWidth() {
        return IMAGE_WIDTH * scale;
    }
    
    public void paint(Graphics2D g2, Component c) {
        g2.drawImage(letterImage, (int)position.getX(), (int)position.getY(), c);
    }
    
    private Image getColoredLetter(Color c) {
        int width = (int)(IMAGE_WIDTH * scale);
        int height = (int)(IMAGE_HEIGHT * scale);
        
        BufferedImage bi = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB_PRE);

        this.letterImage = new javax.swing.ImageIcon(letterImage).getImage();
        
        Graphics2D graph = bi.createGraphics();
        graph.drawImage(letterImage, 0, 0, null);
        
        for(int i=0;i<IMAGE_WIDTH;i++) {
            for(int j=0;j<IMAGE_HEIGHT;j++) {
                if(isTransparent(bi, i, j))
                    continue;
                else
                    bi.setRGB(i, j, c.getRGB());
            }
        }
        
        graph.dispose();
        
        return bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
    }
    
    private boolean isTransparent(BufferedImage bi, int x, int y) {
        int pixel = bi.getRGB(x, y);
        
        if(pixel == 0x00) 
            return true;
        else 
            return false;
    }
    
    private Image getLetterImage(String path, float scale) {
        if(isScaled)
            return AssetManager.loadImage(path).getScaledInstance((int)(IMAGE_WIDTH * scale), (int)(IMAGE_HEIGHT * scale), Image.SCALE_SMOOTH);
        else
            return AssetManager.loadImage(path);
    }
    
    public Image getImage() {return letterImage;}
    
    public void setPosition(Vector2D position) {this.position = position;}
    public Vector2D getPosition() {return position;}
    
    public static final int CHAR_QUESTION = (int)'?';
    public static final int CHAR_EXCLAMATION = (int)'!';
    public static final int CHAR_MONEY = (int)'$';
    
    public static final int IMAGE_WIDTH = 62;
    public static final int IMAGE_HEIGHT = 92;
    
    public static final String resourcesPath = "resources/interface/labels/letters/";
}
