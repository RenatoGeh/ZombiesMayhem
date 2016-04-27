package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Heatbeat bar showing current player's health.
 * 
 * @author Renato Lui Geh
 * @see AnimatedBar
 */
public class HeartbeatBar extends AnimatedBar {
    public HeartbeatBar(Vector2D position, int width, int height, float speed, MutableImage... heartbeat) {
        super(position, width, height, speed, heartbeat);
    }
    
    public static MutableImage[] cropImage(Image im, int maxDist, int gaps, int noBeatDist) {
        im = new javax.swing.ImageIcon(im).getImage();
        
        MutableImage buffer = new MutableImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graph = buffer.createGraphics();
        
        graph.drawImage(im, 0, 0, null);
        
        MutableImage[] result = new MutableImage[gaps+1];
                
        for(int i=0;i<(gaps+1);i++) 
            result[i] = new MutableImage(buffer.getSubimage(0, 0, buffer.getWidth() - ((i*(buffer.getWidth() - maxDist))/(gaps)), buffer.getHeight()),"");
        
        result[gaps] = new MutableImage(buffer.getSubimage(noBeatDist, 0, buffer.getWidth() - noBeatDist, buffer.getHeight()),"");
        
        return result;
    }
    
    private void update() {
        float health = Player.getDefaultPlayer().getHealth();
        
        if(health <= 75) {
            if(health <= 50) {
                if(health <= 25) {
                    if(health <= 10) {
                        if(health <= 0) {
                            this.setIndex(5);
                            return;
                        }
                        this.setIndex(4);
                        return;
                    }
                    this.setIndex(3);
                    return;
                }
                this.setIndex(2);
                return;
            }
            this.setIndex(1);
        } else {
            this.setIndex(0);
        }
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        this.update();
        this.managePainting(g2);
    }
    
    private static HeartbeatBar hb;
    
    public static void initialize(Image heartbeat, Vector2D position, int width, int height, float speed, int maxDist, int gaps, int noBeatDist) {
        hb = new HeartbeatBar(position, width, height, speed, HeartbeatBar.cropImage(heartbeat, maxDist, gaps, noBeatDist));
        Paintables.registerInterface(hb);
    }
    
    public static HeartbeatBar getBar() {return hb;}
}