package geometryzombiesmayhem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * Fades any <code>Image</code>, <code>Sentence</code> or <code>String</code>.
 * 
 * @author Renato Lui Geh
 */
public class ImageFader {
    private BufferedImage buffer;
    private Graphics2D graph;
    private Vector2D position;
    private float alpha = 1;
    private float fadingSpeed = .05f;
    private javax.swing.Timer timer;
    private Vector2D movement = new Vector2D(0, 0);
        
    public ImageFader(float mov) {this.movement.set(0, mov);}
    public ImageFader(Vector2D movement) {this.movement = movement;}
    public ImageFader() {}
    
    private void applyAlpha(Graphics2D g2, float alpha) {
        this.position.subtractLocal(movement);
        
        GameSettings.Graphics.apply(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }
    
    public class Sprite implements ActionListener,geometryzombiesmayhem.Paintable{
        private geometryzombiesmayhem.Sprite s;
        protected boolean isActive = true;
        
        public Sprite(geometryzombiesmayhem.Sprite s, float speed) {
            this.s = s;
            position = s.getPosition();
            fadingSpeed = speed;
            
            initBuffer();
            initTimer();
        }
        
        private void initTimer() {
            timer = new javax.swing.Timer(50, this);
            timer.setRepeats(true);
            timer.start();
        }
        
        private void initBuffer() {
            buffer = new BufferedImage(s.frameWidth, s.frameHeight, BufferedImage.TYPE_INT_ARGB_PRE);
            graph = buffer.createGraphics();
            
            applyAlpha(graph, alpha);
            graph.drawImage(s.getCurrentFrame(), 0, 0, null);
            graph.dispose();
        }
        
        private void refreshBuffer() {initBuffer();}

        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            alpha -= fadingSpeed;
        }
        
        private void update() {
            if(alpha > 0) 
                refreshBuffer();
            else
                isActive = false;
        }

        @Override
        public void paint(Graphics2D g2, Component c) {
            g2.drawImage(buffer, null, (int)position.getX(), (int)position.getY());
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
    
    public class Sentence implements ActionListener,geometryzombiesmayhem.Paintable{
        private geometryzombiesmayhem.Sentence s;
        private boolean isAnimated = false;
        private float animationSpeed = .5f;
        protected boolean isActive = true;
        
        public Sentence(geometryzombiesmayhem.Sentence s, float speed) {
            this.s = s;
            position = s.position;
            fadingSpeed = speed;

            initBuffer();
            initTimer();
        }
        
        public Sentence(geometryzombiesmayhem.Sentence s, float speed, float animationSpeed) {
            this(s, speed);
            this.isAnimated = true;
            this.animationSpeed = animationSpeed;
        }
        
        private void initTimer() {
            timer = new javax.swing.Timer(50, this);
            timer.setRepeats(true);
            timer.start();
        }
        
        private void initBuffer() {
            int n = 0;
            
            buffer = new BufferedImage((int)(s.getWidth() + (s.words.size() * s.getWhitespace())),
                    (int)(Letter.IMAGE_HEIGHT * s.getScale()), BufferedImage.TYPE_INT_ARGB_PRE);
            graph = buffer.createGraphics();
            
            applyAlpha(graph, alpha);
            
            for(int i=0;i<s.words.size();i++) {
                for(int j=0;j<s.words.get(i).getLetters().size();j++) {                    
                    graph.drawImage(s.words.get(i).getLetters().get(j).getImage(), 
                            (int)((i*s.getWhitespace()) + (int)((n*s.getSpacing()) + (int)(n*Letter.IMAGE_WIDTH*s.getScale()))), 0, null);

                    n++;
                }
            }
            
            graph.dispose();
        }
        
        private void refreshBuffer() {initBuffer();}
        
        protected void performAnimation() {
            position.subtractLocal(0, animationSpeed);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            alpha -= fadingSpeed;
        }
        
        private void update() {
            if(alpha > 0)
                refreshBuffer();
            else
                isActive = false;
        }

        @Override
        public void paint(Graphics2D g2, Component c) {
            g2.drawImage(buffer, null, (int)position.getX(), (int)position.getY());
            if(isAnimated)
                performAnimation();
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
    
    public class Image implements ActionListener,geometryzombiesmayhem.Paintable{
        private java.awt.Image im;
        protected boolean isActive = true;
        
        public Image(java.awt.Image im, int speed) {
            fadingSpeed = speed;
            this.im = im;
            
            initBuffer();
            initTimer();
        }
        
        private void initTimer() {
            timer = new javax.swing.Timer(50, this);
            timer.setRepeats(true);
            timer.start();
        }
        
        private void initBuffer() {
            im = new ImageIcon(im).getImage();
            buffer = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
            applyAlpha(graph, alpha);
            graph = buffer.createGraphics();
            graph.drawImage(im, 0, 0, null);
            graph.dispose();
        }

        private void refreshBuffer() {initBuffer();}
        
        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            alpha -= fadingSpeed;
        }
        
        private void update() {
            if(alpha > 0)
                refreshBuffer();
            else
                isActive = false;
        }

        @Override
        public void paint(Graphics2D g2, Component c) {
            g2.drawImage(buffer, null, (int)position.getX(), (int)position.getY());
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
    
    public class Text implements ActionListener,geometryzombiesmayhem.Paintable {
        private String text;
        private Font font;
        private int fontHeight = 0;
        private Color color;
        private boolean hasColor = false;
        protected boolean isActive = true;
        
        public Text(Vector2D pos, String text, Font font, float speed) {
            position = pos;
            fadingSpeed = speed;
            this.text = text;
            this.font = font;
            
            initBuffer();
            initTimer();
        }
        
        public Text(Vector2D pos, String text, Font font, Color color, float speed) {
            position = pos;
            fadingSpeed = speed;
            this.text = text;
            this.font = font;
            
            this.color = color;
            this.hasColor = true;
            
            initBuffer();
            initTimer();
        }
        
        private void instancializeBuffer(int width, int height) {
            buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            graph = buffer.createGraphics();
            graph.setFont(font);
         
            if(hasColor)
                graph.setColor(color);
            
            applyAlpha(graph, alpha);
        }
        
        private void initBuffer() {
            instancializeBuffer(10, 10);
            
            FontMetrics fm = graph.getFontMetrics(font);
            
            int width = fm.stringWidth(text);
            int height = fm.getHeight() + fm.getDescent();
            fontHeight = fm.getHeight();
            
            instancializeBuffer(width, height);
            
            graph.drawString(text, 0, fontHeight);
            
            graph.dispose();
        }
        
        private void refreshBuffer() {
            instancializeBuffer(buffer.getWidth(), buffer.getHeight());
            graph.drawString(text, 0, fontHeight);
            graph.dispose();
        }
        
        private void initTimer() {
            timer = new javax.swing.Timer(50, this);
            timer.setRepeats(true);
            timer.start();
        }
        
        private void update() {
            if(alpha > 0)
                refreshBuffer();
            else
                isActive = false;
        }

        @Override
        public void paint(Graphics2D g2, Component c) {
            g2.drawImage(buffer, null, (int)position.getX(), (int)position.getY());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            alpha -= fadingSpeed;
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
    
    public class Paintable implements ActionListener,geometryzombiesmayhem.Paintable {

        public Paintable() {
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isActive() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setActive(boolean active) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void paint(Graphics2D g2, Component c) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        public boolean showOnlyOnEditor() {
            return false;
        }
        
    }
}