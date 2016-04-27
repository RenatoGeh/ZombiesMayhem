package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Model implements java.io.Serializable {
    private Player p;
    
    private Member[] members = new Member[4];
    
    private Head head;
    private Torso torso;
    private Legs legs;
    private Arms arms;
    
    private float relativeAngle = 0;
    
    private MutableImage buffer;
    private float x, y = x = 0;
    private int width, height = width = 0;
    
    private float multiplier = -2.25f;
            
    public Model() {};
    
    public Model(Player p) {
        this.p = p;
        
        members[0] = torso = new Torso(new Sprite("resources/models/player/idle_torso_headless_armless_enhanced.png", Vector2D.ZERO.copy(),
                33, 39, 0, 0, 250, 4, 1));
        members[1] = legs = new Legs(new Sprite("resources/models/player/idle_legs.png", Vector2D.ZERO.copy(),
                33, 39, 0, 0, 250, 4, 1));
        members[2] = head = new Head(new Sprite("resources/models/player/idle_head_marked.png", Vector2D.ZERO.copy(),
                33, 39, 0, 0, 250, 4, 1, (byte)1), new Vector2D(10, 15), new Vector2D(1.5f, 1f), true);
        members[3] = arms = new Arms(p.getGun().getGunType().getStance(Gun.GunType.Stance.IDLE),
                new Vector2D(10, 15), new Vector2D(0, 0), true, p.getGun());
        
        this.checkSize();
        
        buffer = new MutableImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
    }
    
    public Model(Player p, float multiplier) {
        this(p);
        this.multiplier = multiplier;
    }
    
    public void lookAt() {
        head.setAngle(Head.createAngle(p.getPosition(), GameFrame.mousePosition, head.mirrored));
        head.refresh();
        
        arms.setAngle(Arms.createAngle(p.getPosition(), GameFrame.mousePosition));
        arms.refresh();
        
        torso.setAngle(Member.createAngle(p.getPosition(), GameFrame.mousePosition));
        legs.setAngle(Member.createAngle(p.getPosition(), GameFrame.mousePosition));
    }
    
    public void manageBounds() {      
        if(p.currentBound != null)
            relativeAngle = p.currentBound.angle;
        else
            relativeAngle = 0;
        
        arms.setAngle(Arms.createAngle(p.getPosition(), GameFrame.mousePosition) - relativeAngle);
        arms.refresh();
    }
    
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    
    private void checkSize() {
        width = (int)members[0].getWidth();
        height = (int)members[0].getHeight();
        
        for(Member m : members) {
            width = (int)((m.getWidth() > width)? m.getWidth() : width);
            height = (int)((m.getHeight() > height)? m.getHeight() : height);
        }
    }
    
    private void checkStances() {
        if(p.isMoving() && legs.getStance().equals(Legs.Stance.IDLE)) {
            this.setSprite(legs, legs.getDefaultSprite(Legs.Stance.WALKING), false);
            legs.setStance(Legs.Stance.WALKING);
                    
            head.setOffset(multiplier*(legs.getWidth() - head.getWidth()), 0);
            torso.setOffset(multiplier*(legs.getWidth() - head.getWidth()), 0);
            arms.setOffset(multiplier*(legs.getWidth() - head.getWidth()), 0);
        } else if(!p.isMoving() && legs.getStance().equals(Legs.Stance.WALKING)) {
            this.setSprite(legs, legs.getDefaultSprite(Legs.Stance.IDLE), false);
            legs.setStance(Legs.Stance.IDLE);

            head.setOffset(0, 0);
            torso.setOffset(0, 0);
            arms.setOffset(0, 0);
        }

        if(p.isAiming() && arms.getStance().equals(Arms.Stance.IDLE)) {
            this.setSprite(Gun.GunType.Stance.AIM, true);
            arms.setStance(Arms.Stance.AIMING);
        } else if(!p.isAiming() && arms.getStance().equals(Arms.Stance.AIMING)) {
            this.setSprite(Gun.GunType.Stance.IDLE, true);
            arms.setStance(Arms.Stance.IDLE);
        }
        
        if(p.isShooting() || p.getGun().getGunType().getStance(Gun.GunType.Stance.FIRE).isRunning()) {
            if(arms.getStance().equals(Arms.Stance.IDLE) || arms.getStance().equals(Arms.Stance.AIMING)) {
                this.setSprite(Gun.GunType.Stance.FIRE, true);
                arms.setStance(Arms.Stance.FIRE);
//                arms.getSprite().setSpeed((p.getGun().getCurrentProperties().getTimeUnit().toNanos(p.getGun().getCurrentProperties().getDelay())));
            }
        } else if(!p.isShooting() && arms.getStance().equals(Arms.Stance.FIRE)) {
            this.setSprite(Gun.GunType.Stance.IDLE, true);
            arms.setStance(Arms.Stance.IDLE);
        }
    }

    private void setSprite(Gun.GunType.Stance stance, boolean autoMarking) {
        arms.getSprite().stop();
        arms.setSprite(p.getGun().getGunType().getStance(stance), autoMarking);
        arms.getSprite().startSprite();
    }
    
    private void setSprite(Member m, Sprite s, boolean autoMarking) {
        m.getSprite().stop();
        m.setSprite(s, autoMarking);
        m.getSprite().startSprite();
    }
    
    public void refresh() {
        if(!p.isAiming() && !p.isShooting())
            this.setSprite(Gun.GunType.Stance.IDLE, true);
    }
    
    private void handleMirroring(Graphics2D graph) {
        Vector2D d = p.getPosition().distanceVector(p.getDirection());
        
        x = buffer.getWidth()/2 - width/2;
        y = buffer.getHeight()/2 - height/2;
        
        graph.translate(x, y);
        
        if(d.getX() > 0) {
            for(Member m : members)
                m.mirrored = true;
            
            graph.translate(torso.getWidth(), 0);
            graph.scale(1, -1);
            graph.rotate(Math.PI);
        } else {
            for(Member m : members)
                m.mirrored = false;
        }
        
        if(!legs.mirrored) {
            graph.rotate(relativeAngle);
        } else if(Math.abs(relativeAngle) > .01) { //angle is minimal if < .01 in rads
            graph.rotate(-relativeAngle);            
            
            double dx = torso.getHeight()*Math.sin(relativeAngle);
            double dy = torso.getHeight()*(1-Math.cos(relativeAngle));
            
            double dx0 = torso.getWidth()*Math.cos(relativeAngle);
            double dy0 = torso.getWidth()*Math.sin(relativeAngle);
            
            dy += dy0;
            dx += dx0;
            
            if(relativeAngle > 0)
                dx = -dx/2;
            
            graph.translate(dx/2, dy);
        }
    }
    
    private void paintComponents(Graphics2D g2, Component c) {
        this.checkSize();
        buffer = new MutableImage(width*2, height + (width*2 - height), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);

        this.handleMirroring(graph);
        
        for(Member m : members) {
            m.paint(graph, p.getPosition());
            m.refresh();
        }
        
        graph.dispose();
    }
    
    public void paint(Graphics2D g2, Component c) {
        this.checkStances();
        this.paintComponents(g2, c);
        if(GameFrame.onEditor) g2.drawRect((int)(p.getPosition().getX()), (int)(p.getPosition().getY()), width, height);
        g2.drawImage(buffer, null, (int)(p.getPosition().getX() - x), (int)(p.getPosition().getY() - y));
    }
    
    private static final long serialVersionUID = 9879879564632132L;
}
