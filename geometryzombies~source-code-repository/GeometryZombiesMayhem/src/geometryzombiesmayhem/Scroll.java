package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yan
 */
public class Scroll extends ComponentP {

    Sprite creating;
    MutableImage scroller;
    int scrollerY = 0, height,realheight;
    float scaling = 1;
    Vector2D pos;
    boolean using = false;

    public Scroll(Vector2D pos, int height, int realheight) {
        this.height = height;
        this.realheight = realheight;
        scroller = new MutableImage(AssetManager.loadRawImage("resources/interface/scroll/scroller.png"),"resources/interface/scroll/scroller.png");
        creating = new Sprite("resources/interface/scroll/scrollsprite.png", pos.subtract(20, 0), 20, 20, 0, 0, 30, 20, 1, false, false);
        creating.resizeImages(20, height);
        this.pos = pos;
        scaling = (realheight-height)/(height-20);

        bounds = new Rectangle2D.Float(pos.getX() - 20, pos.getY(), 20, realheight);
    }

    @Override
    protected void setWindow(WindowP p) {
        super.setWindow(p);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_ENTERED) {
            if (creating.frameIndex < creating.totalFrames - 1) {
                creating.Vmodifier = 1;
            }
            if (!creating.isRunning) {
                creating.startSprite();
            }
        } else if (e.getID() == MouseEvent.MOUSE_EXITED && !using) {
            if (creating.frameIndex > 0) {
                creating.Vmodifier = -1;
            }
        }
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            if (e.getID() == MouseEvent.MOUSE_PRESSED && Math.abs(e.getY() - pos.getY() - scrollerY) <= 20) {
                window.gA.add(ml);
                using = true;
            } else if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                window.deltaY = (int) (scaling*(scrollerY = (int) (e.getY() - pos.getY() - window.deltaY)));
            }
        
    }
    
    @Override
    protected void processMouseWheelEvent(MouseWheelEvent event) {
        super.processMouseWheelEvent(event);
        int deltaY = event.getWheelRotation()*20;
        scrollerY+= deltaY;
        if(scrollerY<0) {scrollerY = 0; }
        else if(scrollerY>height-20) {scrollerY = height-20; }
        window.deltaY = (int) (scrollerY*scaling);
    }
    
    private MouseAdapter ml = new MouseAdapter() {
        int currentY = -1;


        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if(currentY == -1) { currentY = e.getY(); return; }
            int deltaY = e.getY() - currentY;
            currentY = e.getY();
            scrollerY+= deltaY;
            if(scrollerY<0) {currentY -= scrollerY; scrollerY = 0; }
            else if(scrollerY>height-20) {currentY-= scrollerY -height + 20; scrollerY = height-20; }
            window.deltaY = (int) (scrollerY*scaling);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    window.gA.remove(ml);
                }
                
            });
            
            using = false;
            if(e.getX()<pos.getX())  {
                if (creating.frameIndex > 0) {
                creating.Vmodifier = -1;
            }
            }
        }
        
    };

    @Override
    public void setActive(boolean active) {
        System.out.println("hey tried changing stuff here yo");
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
        creating.paint(g2, c, (int) (pos.getX() - 20), (int) (pos.getY() + scrollerY));
        g2.drawImage(scroller, (int) (pos.getX() - 20), (int) (pos.getY() + scrollerY*(1+scaling)), null);
    }
}
