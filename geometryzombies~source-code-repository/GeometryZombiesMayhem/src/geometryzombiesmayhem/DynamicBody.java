package geometryzombiesmayhem;

/**
 * <p><code>Body</code> obeying Dynamics laws.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract class DynamicBody extends Body {
    private float elasticity = 1; //1=ideally elastic;0=non-elastic
    
    public DynamicBody(float mass, float x, float y, float Vx, float Vy, float elasticity) {
        super(mass, x, y, Vx, Vy);
        this.elasticity = elasticity;
        this.interactible = true;
    }
    
    public void setVelocity(Vector2D v) {this.Vx = v.getX(); this.Vy = v.getY();}
    public void setVelocity(float angle, float v) {
        this.Vx = v*(float)Math.cos(angle);
        this.Vy = v*(float)Math.sin(angle);
    }
    public void setVelocity(Vector2D start, Vector2D end) {this.setVelocity(end.subtract(start));}
    
    @Override
    protected boolean manageCollision(Body b) {        
        Vector direction = new Vector(Vx, Vy);
        direction.add(new Vector(b.Vx, b.Vy));
//        Vector A = new Vector(Vx, Vy);
//        Vector B = new Vector(b.Vx, b.Vy);
        
        //Va' = {Ma*Va + Mb*[Vb - e*(Va - Vb)]}/(Ma + Mb)
        float va = (float)Math.sqrt(Vx*Vx + Vy*Vy);
        float vb = (float)Math.sqrt(b.Vx*b.Vx + b.Vy*b.Vy);
        float v = (mass*va + b.mass*(vb - elasticity*(va - vb)))/(mass + b.mass);
        
        float theta = -direction.getAngle();
//        float theta = A.getAngle() - B.getAngle();
        System.err.println("Post: " + this.Vx + "\t" + this.Vy);
        System.err.println("Angles: " + theta + "\t" + Math.cos(theta) + "\t" + Math.sin(theta));
        this.Vx = v*(float)Math.cos(theta);
        this.Vy = v*(float)Math.sin(theta);
        
//        System.err.println(direction.x + "\t" + direction.y);
        
        return false;
    }
    
    private static class Vector {
        private float x, y;
        private Vector(float x, float y) {this.x = x; this.y = y;}
        private float getSqValue() {return (float)Math.sqrt(x*x + y*y);} 
        private float getValue() {return x*x + y*y;}
        private void add(Vector v) {
            this.x += v.x;
            this.y += v.y;
        }
        private float getAngle() {
            if(x==0 && y==0) return 0;
            else if(x==0) return (float)(Math.signum(y)*Math.PI/2);
            else if(y==0) return (float)(Math.signum(y)*Math.PI);
            float theta = (float)Math.atan(x/y);
            if(x>0 && y>0) theta-=Math.PI; //1st quadrant
            else if(x>0 && y<0) theta+=Math.PI; //4th quadrant
            return theta;
        }
    }
    
    private static class Momentum extends Vector {
        private float distance; //distance from origin point
        private Momentum(float x, float y, float distance) {super(x, y); this.distance = distance;}
    }
}
