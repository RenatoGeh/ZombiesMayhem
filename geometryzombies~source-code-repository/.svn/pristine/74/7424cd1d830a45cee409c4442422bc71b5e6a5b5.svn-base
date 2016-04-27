package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ObjectStreamException;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;

/**
 * Represents a series of images from a sprite sheet.
 * 
 * <br><br>
 * 
 * <b>Note: </b>This class deprecates the use of Animation. By using
 * the second constructor and setting repeat to false, the same effect
 * is achieved.
 * 
 * @author Renato Lui Geh
 * @version 0.0.1
 */
public class Sprite implements Paintable, WarpAffected, java.io.Serializable{
    private MutableImage spritesheet;
    
    protected int frameLength;
    protected int totalFrames;
    protected int cols, lines;
    protected int frameWidth, frameHeight;
    protected int initPosX, initPosY;
    protected int posX, posY;
    protected int frameX, frameY;
    protected int frameIndex;
    protected boolean repeats = true;
    protected int modifierLength;
    protected boolean reverse = false,isActive = true;
    public boolean dUS = false; //debugUglyStuff
    
    protected boolean isRunning = false;
    
    public float Vmodifier = 1f;
    
    private MutableImage[] frames;
    
    //TODO:Make a main constructor that all the others call
        
    /**
     * Serialization constructor.
     */
    public Sprite() {};
    
    public Sprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines) {
        
        this(spritesheet,position,frameWidth,frameHeight,initPosX,initPosY,
                frameLength,cols,lines,true);
    }
    
    public Sprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, boolean repeats) {
        
        this(spritesheet,position,frameWidth,frameHeight,initPosX,initPosY,
                frameLength,cols,lines,repeats,true);
    }
    
    public Sprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, boolean repeats, boolean starts) {
         this(new MutableImage(AssetManager.loadRawImage(spritesheet),spritesheet, false),position,frameWidth,frameHeight,initPosX,initPosY,
                frameLength,cols,lines,repeats,starts);
    }
        
    public Sprite(MutableImage spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, boolean repeats, boolean starts) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameLength = frameLength;
        
        this.initPosX = initPosX;
        this.initPosY = initPosY;
        
        this.frameX = initPosX;
        this.frameY = initPosY;
        
        this.cols = cols;
        this.lines = lines;
        
        this.spritesheet = spritesheet;
        
        this.frameLength = frameLength;
       
        this.posX = (int)position.getX();
        this.posY = (int)position.getY();
        
        this.totalFrames = lines*cols;
        this.repeats = repeats;
        
        this.modifierLength = frameLength;
        
        frames = this.retrieveImages();
        
        if(starts)
            this.startSprite();
    }
    
    protected Sprite(Sprite s) {
        this.frameWidth = s.frameWidth;
        this.frameHeight = s.frameHeight;
        this.frameLength = s.frameLength;
        
        this.initPosX = s.initPosX;
        this.initPosY = s.initPosY;
        
        this.frameX = s.initPosX;
        this.frameY = s.initPosY;
        
        this.cols = s.cols;
        this.lines = s.lines;
        
        this.spritesheet = s.spritesheet;
        
        this.frameLength = s.frameLength;
       
        this.posX = s.posX;
        this.posY = s.posY;
        
        this.totalFrames = lines*cols;
        this.repeats = s.repeats;
        
        this.modifierLength = frameLength;
        
        frames = this.retrieveImages();
        
        this.startSprite();
    }
    
    public Sprite(String spritesheet, Vector2D position,
            int frameWidth, int frameHeight, int initPosX, int initPosY,
            int frameLength, int cols, int lines, byte rawInput) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameLength = frameLength;
        
        this.initPosX = initPosX;
        this.initPosY = initPosY;
        
        this.frameX = initPosX;
        this.frameY = initPosY;
        
        this.cols = cols;
        this.lines = lines;
        
        if(rawInput == 0)
            this.spritesheet = this.loadSheet(spritesheet);
        else
            this.spritesheet = this.loadSheet(spritesheet).getRawMutableImage();
        
        this.frameLength = frameLength;
        
        this.posX = (int)position.getX();
        this.posY = (int)position.getY();
        
        this.totalFrames = lines*cols;
        
        frames = this.retrieveImages();
        
        this.modifierLength = frameLength;
        this.startSprite();
    }
    
    {
        Paintables.sprites.add(this);
    }

    public Sprite copy() {
        Sprite s = new Sprite(this);
        return s;
    }
    
    public void refreshPosition(Vector2D position) {
        this.posX = (int)position.getX();
        this.posY = (int)position.getY();
    }
    
    private static MutableImage loadSheet(String path) {
        return new MutableImage(AssetManager.loadRawImage(path),path, false);
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
        if(!isRunning) return;
        g2.drawImage(this.getCurrentFrame(), this.posX, this.posY, null);
    }
    
    public void paint(Graphics2D g2, Component c,int x,int y) {
        if(!isRunning) return;
        posX = x;
        posY = y;
        this.paint(g2, c);
    }
    
    public void paint(Graphics2D g2,Component c,Vector2D pos) {
        if(!isRunning) return;
        posX = (int) pos.getX();
        posY = (int) pos.getY();
        this.paint(g2, c);
    }
    
    public void paint(Graphics2D g2,Component c,AffineTransform at,Vector2D pos) {
        if(!isRunning) return;
        paint(g2,c,at,(int)pos.getX(),(int)pos.getY());
    }
    
    public void paint(Graphics2D g2,Component c,AffineTransform at,int x,int y) {
        if(!isRunning) return;
        posX = x;
        posY = y;
        
        g2.drawImage(this.getCurrentFrame(),at, c);
    }
    
    public void updatePosition(int x,int y) {
        posX = x;
        posY = y;
    }
    
    @Deprecated
    public void setSpeed(float modifier) {        
        if(modifier != 0) {
            reverse = ((modifier < 0)? true : false);
            float vel = 1/Math.abs(modifier);
            if(modifierLength != (int)(vel * frameLength)) {
                if(modifierLength != 0)
                    this.stop();
                
                modifierLength = (int)(vel * frameLength);
                //System.err.println(modifierLength);
                AwesomeTimer.addAction(frameAction, modifierLength, true, TimeUnit.MILLISECONDS);
            }
        } else if((int)modifier == 0) {
            modifierLength = 0;
            this.stop();
        }
    }
    
    public void setSpeed(long speed) {
        System.err.println(speed);
        
        if(speed <= 0)
            throw new IllegalArgumentException("Speed cannot be lower or equal to zero!");
        
        this.frameAction.timeToSet = speed;
    }
    
    public void resizeImages(float scale) {
        frameHeight *= scale;
        frameWidth *= scale;
        
        for(int i=0;i<frames.length;i++)
            frames[i] = new MutableImage(frames[i].getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH),"");
    }
    
    public void resizeImages(int width, int height) {
        frameHeight = height;
        frameWidth = width;
        
        for(int i=0;i<frames.length;i++)
            frames[i] = new MutableImage(frames[i].getScaledInstance(width, height, Image.SCALE_SMOOTH),"");
        spritesheet = spritesheet.resize(width*cols, height*lines);
    }
    
    public void unmarkImage(Vector2D pos, int index) {
        frames[index].mutate(baseMarking(pos, index, true, null));
    }
    
    public void markImage(Vector2D pos, int index) {
        frames[index].mutate(baseMarking(pos, index, false, Color.RED.getRGB(), Color.GREEN.getRGB(), Color.BLUE.getRGB(), 0));
    }
    
    public void filterSprite(GameSettings.Graphics.Filter f) {
        for(int i=0;i<frames.length;i++) {
            switch(f) {
                case Default:
                    return;
                case Blur:
                    frames[i].mutate(ImageManipulator.blur(frames[i]));
                    break;
                case Brighten:
                    frames[i].mutate(ImageManipulator.brighten(frames[i], 10));
                    break;
                case Darken:
                    frames[i].mutate(ImageManipulator.darken(frames[i], 10));
                    break;
                case Negative:
                    frames[i].mutate(ImageManipulator.negative(frames[i], 0));
                    break;
                case Gray:
                    frames[i].mutate(ImageManipulator.gray(frames[i], ImageManipulator.GRAYSCALE_AVERAGE.ARITHMETIC));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected exception caught: No such filter.");
            }
        }
    }
    
    private Image baseMarking(Vector2D pos, int index, boolean unmark, int... rgb) {
        BufferedImage buffer = new BufferedImage(frames[index].getWidth(null), frames[index].getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        
        graph.drawImage(frames[index], 0, 0, null);
        
        if(!unmark) {
            buffer.setRGB((int)pos.getX(), (int)pos.getY(), rgb[0]);
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY(), rgb[1]);
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY() + 1, rgb[2]);
            buffer.setRGB((int)pos.getX(), (int)pos.getY() + 1, rgb[3]);
        } else {
            buffer.setRGB((int)pos.getX(), (int)pos.getY(), buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY(), buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY() + 1, buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
            buffer.setRGB((int)pos.getX(), (int)pos.getY() + 1, buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
        }
        
        Image result = buffer.getScaledInstance(buffer.getWidth(), buffer.getHeight(), BufferedImage.SCALE_SMOOTH);
        result = new javax.swing.ImageIcon(result).getImage();
        
        return result;
    }
    
    public void flipImages(int type) {
        for(int i=0;i<frames.length;i++) {
            MutableImage im = frames[i];
            
            int width = im.getWidth(null);
            int height = im.getHeight(null);
            
            BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graph = buffer.createGraphics();
            GameSettings.Graphics.apply(graph);
            
            if(type == Sprite.FLIP_HORIZONTAL)
                graph.drawImage(im, 0, 0, width, height, width, 0, 0, height, null);
            else
                graph.drawImage(im, 0, 0, width, height, 0, height, width, 0, null);
            
            graph.dispose();
            
            frames[i].mutate(buffer.getSubimage(0, 0, width, height));
        }
    }
    
    protected void startSprite() {
        frameIndex = 0;
        isRunning = true;
        AwesomeTimer.addAction(frameAction, modifierLength, true, TimeUnit.MILLISECONDS);
    }
    
    public void reset() {frameIndex = 0;}
    
    public MutableImage getCurrentFrame() {
        return frames[frameIndex];
    }
    
    private MutableImage[] retrieveImages() {
        this.frameIndex = 0;
        
        BufferedImage buffSheet = this.buffImage(spritesheet);
        MutableImage[] result = new MutableImage[totalFrames];
        for(int i=0;i<totalFrames;i++) {
            try {
                result[i] = new MutableImage(buffSheet.getSubimage(frameX, frameY,
                        frameWidth, frameHeight), "");
            } catch (Exception e) {
                try {
                    result[i] = new MutableImage(buffSheet.getSubimage(frameX, frameY,
                            frameWidth - 1, frameHeight), "");
                } catch (Exception ee) {
//                    System.out.println(frameX +"\t"+frameY+"\t"+frameWidth+"\t"+frameHeight+"\n"+buffSheet.getWidth()+"\t"+buffSheet.getHeight());
                    System.err.println(buffSheet.getWidth()/totalFrames+"\t"+frameWidth);
                }
            }
            this.update();
        }
        
        return result;
    }
    
    private BufferedImage buffImage(Image frame) {
        frame = new ImageIcon(frame).getImage();
        
        BufferedImage bi = new BufferedImage(frame.getWidth(null), frame.getHeight(null),
                BufferedImage.TYPE_INT_ARGB_PRE);
               
        Graphics2D graph = bi.createGraphics();
        
        GameSettings.Graphics.apply(graph);
        
        graph.drawImage(frame, 0, 0, null);
        
        return bi;
    }
    
    protected void reverseUpdate() {
        frameIndex--;
        
        if(frameIndex < 0) {
            frameX = frameWidth * cols;
            frameY = frameHeight * lines;
            frameIndex = (cols*lines)-1;
        } else {
            if((frameX - frameWidth) < 0)
                frameX = frameWidth * cols;
            else
                frameX -= frameWidth;
            
            if(frameX == frameWidth * cols) {
                if((frameY - frameHeight) < 0)
                    frameY = frameHeight * lines;
                else
                    frameY -= frameHeight;
            }
        }
        
    }

    protected void update() { 
        frameIndex++;
        
        if(frameIndex == 0) {
            frameX = initPosX;
            frameY = initPosY;
        } else {
            if((frameX + frameWidth) < (frameWidth * cols)) 
                frameX += frameWidth;
            else 
                frameX = 0;

            if(frameX == 0) {
                if((frameY + frameHeight) < (frameHeight * lines))
                    frameY += frameHeight;
                else
                    frameY = 0;
            }
        }
    }
    
    protected void handleReverseFrame() {
        if(dUS) System.out.println(frameIndex);
        if(frameIndex == 0) {
            if(!repeats) {
                System.out.println("h");
                Vmodifier = 0;
                return;
            } else {
                frameIndex = (lines*cols)-1;
            }
        }
        
        reverseUpdate();
    }
    
    protected void handleFrame() {
        if(dUS) System.out.println(frameIndex);
        if(frameIndex >= totalFrames-1) {
            if(!repeats) {
                Vmodifier = 0 ;
                System.out.println("t");
                return;
            } else {
                frameIndex = 0;
            }
        }
        update();
    }
    
    protected synchronized void perFrameAction() {}
    
    protected AwesomeAction frameAction = new AwesomeAction() {
        
        {
            this.timeAffected = true;
            this.timeBody = Sprite.this;
        }
        
        @Override
        public void run() {        
            perFrameAction();
            
            if(!onReverse)
                handleFrame();
            else
                handleReverseFrame();
        }
    };
    
    private final Rectangle2D bounds = new Rectangle2D.Float();
    
    public Rectangle2D getBounds() {
        bounds.setRect(posX, posY, frameWidth, frameHeight);
        return bounds;
    }
    
    public static final int FLIP_HORIZONTAL = 0;
    public static final int FLIP_VERTICAL = 1;
    
    public void stop() {AwesomeTimer.remove(frameAction); isRunning = false;}
    public MutableImage getImage(int index) {return frames[index];}
    public Vector2D getPosition() {return new Vector2D(this.posX, this.posY);}
    public int getTotalLength() {return this.frameLength * this.totalFrames;}

    @Override
    public float getWarpVelocity() {
        return Vmodifier;
    }

    @Override
    public void setWarpVelocity(float V) {
        Vmodifier = V;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
        if(!isActive) {
            isRunning = false;
            stop();
        }
    }
    
    public boolean isRunning() {return isRunning;}    
    
    public float getWidth() {return this.frameWidth;}
    public float getHeight() {return this.frameHeight;}
    
    public void centerOn(Vector2D point) {
        this.posX = (int)(point.getX() - (this.getWidth()/2));
        this.posY = (int)(point.getY() - (this.getHeight()/2));
    }
    
    public void centerOn(float x, float y) {
        this.posX = (int)(x - (this.getWidth())/2);
        this.posY = (int)(y - (this.getHeight()/2));
    }
    
    public Vector2D getCenter() {return new Vector2D(posX + frameWidth/2, posY + frameHeight/2);}
    
    private Object readResolve() {
        return copy();
    }
    
    private static final long serialVersionUID = 217978123894L;

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }

}
