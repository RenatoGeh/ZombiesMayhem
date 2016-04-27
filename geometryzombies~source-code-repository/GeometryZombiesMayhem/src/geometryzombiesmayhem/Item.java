package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * <p>An item.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract class Item extends Body implements Paintable {
    protected Sprite s;
    protected MutableImage icon;
    private boolean isActive = true;
    private boolean isStored = false;
    protected String name;

    public Item() {super();}
    
    public Item(String name, Vector2D position, MutableImage icon, Sprite s, float mass) {
        super(mass, position.getX(), position.getY(), 0, 0);
        
        this.name = name;
        
        this.icon = icon;
        this.s = s;
        this.interactible = true;
        this.id = Body.ID_ITEM;
        int[] a = {Body.ID_ITEM,Body.ID_PROJECTILE,Body.ID_ZOMBIE};
        doNotCheckCollision = a;    
    }
    
    public Item(String name, MutableImage icon, Sprite s, float mass) {
        this(name, Vector2D.CENTER.copy(), icon, s, mass);
        
        this.isStored = true;
    }
    
//    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
//        in.defaultReadObject();
//        this.icon = MutableImage.readStream(in);
//    }
//    
//    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
//        out.defaultWriteObject();
//        MutableImage.writeStream(out, this.icon);
//    }
    
    public void setIcon(MutableImage e) {MutationManager.remove(this.icon); this.icon = e;}
    
    public MutableImage getIcon() {return icon;}

    @Override
    public String getName() {return this.name;}
    
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(s!=null)
            s.paint(g2, c, position);
        else
            g2.drawImage(icon, null, (int)this.position.getX(), (int)this.position.getY());
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        if(s!=null)
            s.paint(g2, c, at, position);
        else
            g2.drawImage(icon, null, (int)this.position.getX(), (int)this.position.getY());
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        if(isStored)
            return;
        
        if(at == null)
            this.paintThis(c, g2);
        else
            this.paintThis(c, g2, at);
    }
    
    public void setStored(boolean isStored) {this.isStored = isStored;}
    public boolean isStored() {return this.isStored;}
    
    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean isActive) {this.isActive = isActive;}
    
    public void checkState() {        
        if(isStored)
            return;
        
        for(Player p : Player.getPlayers()) {
            if(this.getBounds().intersects(p.getBounds().getBounds2D())) {
                onCollision(p);
            }
        }
    }
    
    @Override
    public boolean manageCollision(Body b) {
        if(isStored)
            return false;
        
        if(b.id == Body.ID_PLAYER) {
            onCollision((Player) b);
            return true;
        }
        return false;
    }
    
    protected void onCollision(Player p){
        onUse(p); //override if you want to do something different
    }
    protected abstract void onUse(Player p);
    
    @Override
    public void destroy() {
        super.destroy();
        
        if(s!=null) {
            this.s.stop();
            this.s.setActive(false);
        } else
            MutationManager.remove(icon);
        
        this.setActive(false);
        
        Paintables.remove(this);
    }
    

    @Override
    public void load() {
        super.load();
        Paintables.register(this);
    }

    @Override
    public float minX() {return position.getX();}
    @Override
    public float maxX() {return position.getX() + s.frameWidth;}

    @Override
    public float minY() {return position.getY();}
    @Override
    public float maxY() {return position.getY() + s.frameHeight;}

    @Override
    public void setMaxX(float x) {position.setX(x - s.frameWidth);}
    @Override
    public void setMaxY(float y) {position.setY(y - s.frameHeight);}

    @Override
    public void setMinX(float x) {position.setX(x);}
    @Override
    public void setMinY(float y) {position.setY(y);}
    
    public static Item createHealthItem(Vector2D position) {
        Sprite foo = new Sprite("resources/items/health.png", position, 28, 30,
                0, 0, 50, 33, 1);
        return new Item.Health("Health Pack", position, foo.getImage(0), foo, 20);
    }
    
    public static Item createAmmoItem(Vector2D position, ProjectileType type) {
        Sprite foo = new Sprite("resources/items/ammo.png", position, 32, 32,
                0, 0, 50, 6, 1);
        return new Item.Ammo("Ammo Crate", position, foo.getImage(0), foo, type, 100);
    }
    
    public static Item createFlashlightItem(Vector2D position) {
//        Sprite foo = new Sprite("", position, 0, 0, 
//                0, 0, 50, 6, 1);
        return new Item.Flashlight("Flashlight", position, AssetManager.loadImage("resources/items/flashlight.png"), null, 
                new FlareShape.Conical(Vector2D.TOP_LEFT.copy(), Vector2D.ZERO.copy(), 
                    200, (float)Math.PI/9, java.awt.Color.BLACK));
    }
    
    protected static class Flashlight extends Item {
        private FlareShape light;
        
        public Flashlight() {super();}
        public Flashlight(String name, Vector2D position, MutableImage icon, Sprite sprite, FlareShape light) {
            super(name, position, icon, sprite, 10);
            this.light = light;
        }
        
        @Override
        protected void onUse(Player p) {
            if(p.getFlashlight()==null) return;
            LightManager.remove(light);
            p.setFlashlight(light);
            light.setPosition(p.getPosition());
            light.setDirection(p.getDirection());
            this.destroy();
        }

        @Override
        public float maxX() {return position.getX() + (s==null?icon.getWidth():s.frameWidth);}

        @Override
        public float maxY() {return position.getY() + (s==null?icon.getHeight():s.frameHeight);}

        @Override
        public void setMaxX(float x) {position.setX(x - (s==null?icon.getWidth():s.frameWidth));}
        @Override
        public void setMaxY(float y) {position.setY(y - (s==null?icon.getHeight():s.frameHeight));}
        
        @Override
        public Body copy() {
            return new Flashlight(this.name, this.position, this.icon, this.s, this.light);
        }
    }
    
    protected static class Health extends Item {
        private float h;
        
        public Health() {super();}
        
        public Health(String name, Vector2D position, MutableImage icon, Sprite sprite, float heal) {
            super(name, position,icon,sprite,10);
            h = heal;
        }

        @Override
        protected void onUse(Player p) {
            p.heal(h);
            this.destroy();
        }

        @Override
        public Body copy() {
            return new Health(this.name,position,this.icon,this.s.copy(),h);
        }
        
    }
    
    protected static class Ammo extends Item {
        private ProjectileType pt;
        private int q;
        
        public Ammo() {super();}
        
        public Ammo(String name, Vector2D position, MutableImage icon, Sprite sprite, ProjectileType type, int quantity) {
            super(name, position,icon,sprite,10);
            pt = type;
            q = quantity;
        }

        @Override
        protected void onUse(Player p) {
            p.getGun().addAmmo(pt, q);
            this.destroy();
        }

        @Override
        public Body copy() {
            return new Ammo(this.name,position.copy(),this.icon,this.s.copy(),pt,q);
        }
        
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
    
}