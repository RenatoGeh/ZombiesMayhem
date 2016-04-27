/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Yan
 */
public abstract class Bound extends Body{
    Shape boundShape;
    MutableImage boundImage;
    
//    public static enum BoundType { UP,DOWN,LEFT,RIGHT,COMPLEX_ALL };
    
    /**
     * Serialization constructor.
     */
    protected Bound() {super();}
    
    protected Bound(Shape bs,MutableImage bi) {
        super(0,(float) bs.getBounds2D().getMinX(),(float) bs.getBounds2D().getMinY(),0,0);
        if(bs instanceof Line2D) {
            Line2D l = (Line2D) bs;
            if(l.getY1() == l.getY2()) {
                l.setLine(l.getP1(), new Point2D.Float((float)l.getX2(),(float) l.getY2()+1));
            }
        }
        this.boundsAffected = false;
        this.interactible = true;
        boundShape = bs;
        boundImage = bi;
        this.checkPriority = 5;
        this.managePriority = 2;
        this.id = Body.ID_BOUND;
        doNotCheckCollision = new int[2];
        doNotCheckCollision[0] = ID_BOUND;
        doNotCheckCollision[1] = ID_WARP;
    }
    
    public static Polygon rect(float x,float y,float w, float h,float angle) {
        int[] xrect = new int[4];
        int[] yrect = new int[4];
        xrect[0] = (int) x; yrect[0] = (int) y;
        angle = (float) Math.toRadians(90 - angle);
        float xl,yl;
        xl = (float) (w*Math.sin(angle));
        yl = (float) (w*Math.cos(angle));
        System.err.println(xl + " " + yl);
        xrect[1] = (int) (x + xl); yrect[1] = (int) (y - yl);
        float xll,yll;
        xll = (float) (h*Math.cos(angle));
        yll = (float) (h*Math.sin(angle));
        System.err.println(xll + " " + yll);
        xrect[2] = (int) (x + xl + xll); yrect[2] = (int) (y - yl + yll);
        xrect[3] = (int) (x + xll); yrect[3] = (int) (y + yll);
                 
        return new Polygon(xrect,yrect,4);
    }
    
    public static class Simple extends Bound{
        public boolean up;
        public double sin,cos;
        private double a,b;
        Line2D line;

        /**
         * Serialization constructor.
         */
        public Simple() {super();}
        
        public Simple(Line2D bs, MutableImage i, boolean up) {
            super(bs,i);
            this.up = up;
            line = bs;
            double y = (bs.getY2() - bs.getY1());
            double x = bs.getX2() - bs.getX1();
            double d = Math.sqrt(x*x + y*y);
            sin = y/d;
            cos = x/d;
            angle = (float)Math.atan(sin/cos);
            
            if(Math.abs(cos)<0.7071) this.up = false;
            
            calcFunc(bs);
        }
        
        protected final void calcFunc(Line2D bs) {
            a = (bs.getY1() - bs.getY2())/(bs.getX1() - bs.getX2());
            b = bs.getY1() - (a*bs.getX1());
        }
        
        public double getY(double x) {
            return a*x + b;
        }
        
        public double getX(double y) {
            return (y-b)/a;
        }
        
        public Line2D getLine() {return line;}
        
        @Override
        protected boolean checkCollides(Body b) {
            return b.getBounds().intersects(line);
        }

        @Override
        public void setPosition(Vector2D position) {
            super.setPosition(position);
            
            line.setLine(position.getX(), position.getY(), 
                    position.getX() + (line.getX2() - line.getX1()), 
                    position.getY() + (line.getY2() - line.getY1()));
            
            double y = (line.getY2() - line.getY1());
            double x = line.getX2() - line.getX1();
            double d = Math.sqrt(x*x + y*y);
            sin = y/d;
            cos = x/d;
            angle = (float)Math.atan(sin/cos);
            
            this.calcFunc(line);
            
            boundShape = line;
        }
        
        @Override
        public boolean contains(Point position) {return line.ptLineDist(position) <= 9;}
        
        @Override
        protected synchronized boolean manageCollision(Body b) {
            if(b.getID() == Body.ID_PROJECTILE) b.destroy();
            
            if(up) {
                if((b.Vy>=0)) {
                    b.currentBound = this;
                    return true;
                }
            }
            else {
                if(Math.abs(cos)>=0.7071) {
                    if(b.Vy < 0) {
                        b.Vy=0;
                        b.setMinY((float) getY(b.minX()));
                        return true;
                    }
                } else if(Math.signum(sin)*Math.signum(cos)== 1) {
                    if(b.Vx > 0) {
                        b.setMaxX((float) getX(b.maxY()));
                        return true;
                    }
                } else if(Math.signum(sin)*Math.signum(cos)== -1){
                    if(b.Vx<0) {
                        b.setMinX((float) getX(b.maxY()));
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Body copy() {
            return new Simple(new Line2D.Float(line.getP1(),line.getP2()),boundImage==null? null: boundImage.getRawMutableImage(),up);
        }
        
        @Override
        public float lookAt(float angle) {
//            AffineTransform trans = new AffineTransform();
//            trans.rotate(angle, this.boundShape.getBounds().getCenterX(), this.boundShape.getBounds().getCenterY());
//            this.boundShape = trans.createTransformedShape(boundShape);
//            double alpha = angle;
//            double beta = (Math.PI - alpha)/2 - this.angle;
//            double l = Math.cos(this.angle)*(line.getX2() - line.getX1());
//            
//            double d = l*Math.sin(alpha/2);
//            double deltaX = d*Math.cos(beta);
//            double deltaY = d*Math.sin(beta);
//            
//            this.line.setLine(line.getX1() - deltaX, line.getY1() + deltaY, 
//                    line.getX2() + deltaX, line.getY2() - deltaY);
//            
//            super.lookAt(angle);
            
            return this.angle;
        }
        
        @Override
        public void paintThis(Component c, Graphics2D g2) {
            super.paintThis(c, g2);
//            Color def = g2.getColor();

//            g2.setColor(Color.BLUE);
//            g2.drawString("1", (float)this.line.getX1()-5, (float)this.line.getY1() - 10);
//            g2.fill(new Ellipse2D.Double(this.line.getX1()-5, this.line.getY1()-5, 10, 10));
            
//            g2.setColor(Color.RED);
//            g2.drawString("2", (float)this.line.getX2()-5, (float)this.line.getY2() - 10);
//            g2.fill(new Ellipse2D.Double(this.line.getX2()-5, this.line.getY2()-5, 10, 10));
            
//            g2.setColor(def);
        }
        
        private Object readResolve(){
            return new Simple(line,boundImage,up); 
        }
    }
    
    
    public static class SimpleMove extends Simple {
        long time;
        int fromX,fromY;
        int toX,toY;
        float vx,vy;
        
        Line2D bb;
        
        /**
         * Serialization constructor.
         */
        public SimpleMove() {super();}
        
        public SimpleMove(Line2D bs,MutableImage i, int toX,int toY,long timee,TimeUnit t) {
            super(bs,i,true);
            fromX = (int) bs.getX1();
            fromY = (int) bs.getY1();
            this.toX = toX;
            this.toY = toY;
            this.time = t.toMillis(timee);
            time = time/ZM.milliToTick;
            vx = Math.abs((float)(toX-fromX)/time);
            vy = Math.abs((float)(toY-fromY)/time);
            this.Vx = vx;
            this.Vy = vy;
            
            if(fromX>toX) {
                Vx = -vx;
                int auxx = toX;
                toX = fromX;
                fromX = auxx;
            }
            if(fromY>toY) {
                Vy = -vy;
                int auxx = toY;
                toY = fromY;
                fromY = auxx;
            }
            
            bb = bs;
        }
        
        @Override
        protected void update(long deltaN) {
            if(position.getX()>=toX)Vx = -vx;
            else if(position.getX()<=fromX) Vx = vx;
            if(position.getY()>=toY) Vy = - vy;
            else if (position.getY()<=fromY) Vy = vy;
        }
       
        @Override
        protected boolean checkCollides(Body b) {
            bb.setLine(position.getX(),position.getY(),position.getX() + bb.getX2() - bb.getX1(), position.getY() + bb.getY2() - bb.getY1());
            calcFunc(bb);
            return super.checkCollides(b);
        }

        @Override
        public Body copy() {
            return new SimpleMove(new Line2D.Float(line.getP1(),line.getP2()),this.boundImage.getRawMutableImage(),toX,toY,time,TimeUnit.MILLISECONDS);
        }
        
        private Object readResolve(){
            return new SimpleMove(new Line2D.Float(line.getP1(),line.getP2()),this.boundImage.getRawMutableImage(),toX,toY,time,TimeUnit.MILLISECONDS);
        }
    }

    public static class Rectangular extends Bound {
        Bound.Simple[] lines = new Bound.Simple[4];
        
        public Rectangular(Rectangle2D rectangle) {
            java.util.ArrayList<Vector2D> points = Vector2D.getPoints(rectangle);
            for(int i=1;i<points.size();i++)
                lines[i-1] = new Bound.Simple(
                        new Line2D.Float(points.get(i-1).getX(), points.get(i-1).getY(),
                        points.get(i).getX(), points.get(i).getY()), null, i==1);
            
        }
        
        @Override
        protected boolean manageCollision(Body b) {
            for(Bound.Simple line : lines)
                return b.manageCollision(b);
            return false;
        }
        
        @Override
        public void setPosition(Vector2D position) {
            Vector2D diff = position.subtract(this.position);
            for(int i=0;i<lines.length;i++)
                lines[i].setPosition(lines[i].getPosition().add(diff));
        }

        @Override
        public boolean contains(Point position) {
            for(int i=0;i<lines.length;i++)
                if(lines[i].contains(position))
                    return true;
            return false;
        }
        
        @Override
        public float lookAt(float angle) {return lines[0].lookAt(angle);}
        
        @Override
        public Body copy() {
            return new Bound.Rectangular(new Rectangle2D.Double(
                    lines[0].line.getX1(), lines[0].line.getY1(),
                    lines[0].line.getX2()-lines[0].line.getX1(),
                    lines[0].line.getY2()-lines[0].line.getY1()));
        }
        
        @Override
        public void paintThis(Component c, Graphics2D g2) {for(Bound.Simple bound:lines)bound.paintThis(c, g2);}
    }
    
    @Override
    public Shape gBA() {
        return boundShape;
    }
    
    @Override
    public RotatableRectangle getBounds() {
        return null;
    }
    
    @Override
    public RotatableRectangle getRoundedBounds() {
        if(this.bounds == null) bounds = new RotatableRectangle(this.boundShape.getBounds(), this.angle);
        return bounds;
    }
    
    @Override
    protected boolean checkCollides(Body b) {
        return boundShape.intersects(b.position.getX(),b.position.getY(),b.maxX()-b.minX(),b.maxY()-b.minY());
    }
    @Override
    protected abstract boolean manageCollision(Body b);

    @Override
    public void paintThis(Component c, Graphics2D g2) {
        if(boundImage==null) return;
        g2.drawImage(boundImage, (int) boundShape.getBounds2D().getMinX(),(int) boundShape.getBounds2D().getMinY(), c);
    }

    @Override
    public void paintThis(Component c, Graphics2D g2, AffineTransform at) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getWidth() {return (float)boundShape.getBounds2D().getWidth();}
    @Override
    public float getHeight() {return (float)boundShape.getBounds2D().getHeight();}
    
    @Override
    public float minX() {
        return (float) boundShape.getBounds2D().getMinX();
    }

    @Override
    public float maxX() {
        return (float) boundShape.getBounds2D().getMaxX();
    }

    @Override
    public float minY() {
        return (float) boundShape.getBounds2D().getMinY();
    }

    @Override
    public float maxY() {
        return (float) boundShape.getBounds2D().getMaxY();
    }

    @Override
    public void setMaxX(float x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMaxY(float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMinX(float x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMinY(float y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
