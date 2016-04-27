package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @version 0.0.1
 * @author Renato Lui Geh
 */

public class SceneLayer implements Sidescrollable, Serializable {
    private ArrayList<Bound> bounds;
    private MutableImage backImage;
    private Vector2D backPosition;
    private float deviation = 1f;
    private float speed;
    private boolean repeat;
    private boolean followPlayer;
    private String name;
    byte boundsType; // 0 = too small/ 1 = normal/ 2 = too big
    
    public SceneLayer(String name, MutableImage backImage, float speed, boolean repeat) {
        this.backImage = backImage;
        this.speed = speed;
        backPosition = Vector2D.TOP_LEFT.copy();
        this.repeat = repeat;
        this.name = name;
    }
    
    public SceneLayer(String name, MutableImage backImage, float speed, boolean repeat, boolean followPlayer, boolean autoBoundaries) {
        this(name,backImage,speed,repeat,followPlayer,autoBoundaries,1);
    }
    
    public SceneLayer(String name, MutableImage backImage, float speed, boolean repeat, boolean followPlayer, boolean autoBoundaries,int size) {
        this(name, backImage, speed, repeat, followPlayer);
        if(size<0) size = 0; if(size>2) size = 2;
        boundsType = (byte)size;
        
        if(autoBoundaries)
            this.registerBoundaries();
    }
    
    public SceneLayer(String name, MutableImage backImage) {
        this(name, backImage,0,false);
    }
    
    public SceneLayer(String name, MutableImage backImage, float speed,boolean repeat, boolean followPlayer) {
        this(name, backImage, speed, repeat);
        this.followPlayer = followPlayer;
    }
    
    public SceneLayer(String name, MutableImage backImage, float speed, boolean repeat, Vector2D position) {
        this(name, backImage, speed, repeat);
        backPosition = position;
    }
    
    public SceneLayer(String name, MutableImage backImage, float speed, boolean repeat, Vector2D position, boolean followPlayer) {
        this(name, backImage, speed, repeat, position);
        this.followPlayer = followPlayer;
    }
    
    public SceneLayer(String name, MutableImage backImage, boolean repeat, boolean followPlayer, Vector2D position, float deviation) {
        this(name, backImage, 0, repeat, position);
        this.followPlayer = followPlayer;
        this.deviation = deviation;
    }
    
    public SceneLayer(String name, MutableImage backImage, boolean repeat, boolean followPlayer, Vector2D position, float deviation, boolean autoBoundaries) {
        this(name, backImage, repeat, followPlayer, position, deviation);
        
        if(autoBoundaries)
            this.registerBoundaries();
    }
    int baseX;
    int safeZone = 200;
    int lastX;
    int curBound;
    
    private void registerBoundaries() {
        this.bounds = ImageManipulator.getSimpleBounds(backImage, true);

        for(Bound b : bounds) {
            b.setPosition(b.getPosition().add(0, this.getPosition().getY() +
                    (b.angle < 0? b.getHeight() : 0)));
        }
        baseX = (int) bounds.get(0).minX();
        lastX = baseX;
        curBound = -1;
        do {
            curBound++;
            Bound b = bounds.get(curBound);
            Body.register(b);
            lastX = (int) b.maxX();
        } while(lastX<GameFrame.FRAME_WIDTH + safeZone);
    }
        
    public MutableImage getImage() {
        return backImage;
    }
    
    public void setFollow(boolean followPlayer) {
        this.followPlayer = followPlayer;
    }
    
    public boolean followsPlayer() {
        return followPlayer;
    }
    
    public void setImage(MutableImage backImage) {
        this.backImage = backImage;
    }
    
    public Vector2D getPosition() {
        return backPosition;
    }
    
    public void setPosition(Vector2D backPosition) {
        this.backPosition = backPosition;
    }
    
    public void setDeviation(float deviation) {
        this.deviation = deviation;
    }
    
    public float getDeviation() {
        return deviation;
    }
    
    public String getName() {
        return name;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public void repaintWithPlayer(Graphics2D g, Component c, float Vx) {
        followPlayer(Vx);
        singleMove();
        backPaint(g, c);        
    }
    
    public void repaint(Graphics2D g, Component c) {
        //if(followPlayer) 
        //    followPlayer(Vector2D.BOTTOM_LEFT.add(100, 0), Vector2D.BOTTOM_RIGHT.subtract(100, 0), player);
        handleBounds();
        singleMove();
        backPaint(g, c);        
        //System.err.println("Position: " + backPosition);
    }
    
    public void singleMove() {
        backPosition.subtractLocal(speed, 0);
        checkBounds();
    }
    
    public void move(float pixels) {
        backPosition.subtractLocal(pixels, 0);
        checkBounds();
    }
    
    public void backPaint(Graphics2D g, Component c) {
        float x,y;
        if(deviation!=1f) { //the if is not really necessary, but if deviation == 1, all these steps are not necessary
            g.translate(GameFrame.paintArea.tx, 0);
            g.translate((int) (-GameFrame.translateX*deviation), 0);
        }
		
        x = backPosition.getX();
        y = backPosition.getY();
        g.drawImage(backImage, (int)x , (int)y, c);
		
        if(!repeat && deviation!=1f) {
            g.translate((int) (GameFrame.translateX*deviation), 0);
            g.translate(-GameFrame.paintArea.tx, 0);
            return;
        }
			
        while((x += backImage.getWidth(null)) < GameFrame.FRAME_WIDTH + GameFrame.translateX*deviation)
            g.drawImage(backImage,(int) x , (int)y, c);
			
        x =  backPosition.getX();
		
        while (x > GameFrame.translateX*deviation)		//pintando pra tras
            g.drawImage(backImage,(int) (x -= backImage.getWidth(null)) , (int)y, c);
        
        if(deviation!=1f) {
            g.translate((int) (GameFrame.translateX*deviation), 0);
            g.translate(-GameFrame.paintArea.tx, 0);
        }
    }

    @Override
    public void checkBounds() {
        if(repeat) {
            if(backPosition.getX() < -backImage.getWidth(null) + GameFrame.translateX || backPosition.getX() > GameFrame.FRAME_WIDTH + GameFrame.translateX)
                backPosition.setX(GameFrame.translateX); 
        } else {
            if(backPosition.getX() + backImage.getWidth(null) > GameFrame.FRAME_WIDTH + GameFrame.translateX)
                backPosition.setX(GameFrame.FRAME_WIDTH + GameFrame.translateX - backImage.getWidth(null));
            if(backPosition.getX() < 0)
                backPosition.setX(0);
        }
    }
    
    @Override
    public void followPlayer(Vector2D leftLimit, Vector2D rightLimit, Player player) {
        if(player.getPosition().getX() < rightLimit.getX() || player.getPosition().getX() > leftLimit.getX()) 
            speed = deviation * player.Vx;
        else
            speed = player.Vx;
    }
    boolean hasLooped = false;
    private void handleBounds() {
        if(!this.repeat || bounds==null)
            return;
        
        if((GameFrame.translateX+GameFrame.FRAME_WIDTH+safeZone)>lastX) {
            curBound++;
            if(curBound==bounds.size()){
                hasLooped = true;
                curBound = 0;
            }
            Bound b = bounds.get(curBound);
            if(Math.abs(lastX - b.minX())>10) { //it is dislocated
                b.setPosition(new Vector2D(lastX,b.getPosition().getY()));
            }
            if(!hasLooped) Body.register(b);
            else {
                int nextBound = curBound+1;
                if(nextBound==bounds.size()) nextBound = 0;
                baseX = (int) bounds.get(nextBound).minX();
                
            }
            lastX = (int) b.maxX();
        }

        if(GameFrame.translateX>safeZone+1 && GameFrame.translateX-safeZone<baseX) {
            Bound b = bounds.get(curBound);
            b.setPosition(b.getPosition().add(baseX - b.getWidth() - b.getPosition().getX(), 0));
            baseX = (int) b.minX();
            curBound--;
            if(curBound==-1) curBound = bounds.size()-1;
            lastX = (int) bounds.get(curBound).maxX();
        }
    }
    
    @Override
    public void followPlayer(float Vx) {
        speed = deviation * Vx;
    }

    @Override
    public void checkBounds(ArrayList<Vector2D> initialPosition, ArrayList<Image> images) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private static final long serialVersionUID = 9717418229645L;
}
