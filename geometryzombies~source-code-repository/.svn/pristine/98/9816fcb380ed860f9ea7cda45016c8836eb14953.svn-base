package geometryzombiesmayhem;

import geometryzombiesmayhem.circuitry.Output;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p>A light source that has a concrete shape.</p>
 * 
 * <p>This is a stand-alone Flare Effect emitter. It creates a lone light source with no
 * other objects as base.</p>
 * 
 * <p>Constitutes what I call the "Flare Effect". Such phenomena is based on
 * the <b>intensity</b> and <b>shape</b> of a light source.</p>
 * 
 * <p><b>Intensity</b> is defined by:</p>
 * <ul>
 *  <li>The range of the light according to the proposed shape (obeying a directly proportional law) in pixels.</li> 
 *  <li>The luminosity of the light source, represented as its lowest alpha channel.</li>
 *  <li>The "Flare Width", that is, the distance from the Flare's lowest alpha channel (usually 0) to the Ambience Light's alpha channel (usually 255) in pixels.</li>
 * </ul>
 * 
 * <p><b>Shape</b> must be a <code>java.awt.Shape</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract class FlareShape implements LightSource, LightSource.Blinkable {
    protected transient Shape shape;
    private Color color;
    private Intensity intensity;
    private boolean isEnabled = false;
    private long[] periods = null;
    
    public FlareShape(Shape s, int range, int flareWidth, int luminosity) {this(s, range, flareWidth, luminosity, Color.WHITE);}    
    public FlareShape(Shape s, int range, int flareWidth, int luminosity, Color c) {
        this.shape = s;
        this.intensity = Intensity.getInstance(range, flareWidth, luminosity);
        this.color = c;
    }
    
    @Override
    public Shape getShape() {return shape;}
    @Override
    public Rectangle getBounds() {return getShape().getBounds();}
    
    @Override
    public Color getLightColor() {return color;}
    
    @Override
    public Vector2D getDirection() {return this.getPosition();}
    @Override
    public void setDirection(Vector2D direction) {}
    
    @Override
    public Intensity getIntensity() {return intensity;}
    @Override
    public void setIntensity(Intensity i) {this.intensity = i;}

    @Override
    public Area getArea() {return new Area(getShape());}

    @Override
    public Rectangle2D getBounds2D() {return getShape().getBounds2D();}
    
    @Override
    public synchronized boolean isEnabled() {return this.isEnabled;}
    
    @Override
    public synchronized boolean isOn() {return this.isBlinking()?this.blinkAction.isOn():true;}
    @Override
    public synchronized boolean isOff() {return this.isBlinking()?this.blinkAction.isOff():false;}
    
    @Override
    public synchronized void setEnabled(boolean isEnabled) {
        if(isEnabled == this.isEnabled) return;
        
        if(isEnabled)
            LightManager.register(this);
        else
            LightManager.remove(this);
        
        this.isEnabled = isEnabled;
    }
    
    @Override
    public synchronized void toggleEnabled() {setEnabled(!this.isEnabled);}
    
    @Override
    public void setPeriods(long... deltaTs) {this.periods = deltaTs;}
    @Override
    public long[] getPeriods() {return periods;}
    @Override
    public synchronized void setBlinking(boolean blinks) {
        if(!this.isEnabled()) return;
        if(this.periods == null) return;
        if(blinks==isBlinking()) return;
        
        if(blinks)
            AwesomeTimer.addAction(blinkAction=new BlinkAction(this, periods), 0, true);
        else {
            AwesomeTimer.remove(blinkAction);
            blinkAction = null;
        }
    }
    @Override
    public boolean isBlinking() {return blinkAction!=null;}
    
    private BlinkAction blinkAction;
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        if(this.blinkAction != null)
            AwesomeTimer.addAction(this.blinkAction, 0, true);
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
    }
    
    @Override
    public void updateVoltage(Output out, float nVoltage) {
        int lumen = (int)(255*(1-nVoltage));
        lumen = lumen>LightManager.getDarkness()?LightManager.getDarkness():lumen;
        this.getIntensity().setLuminosity(lumen);
    }
    
    @Override
    public void connected(Output out) {}
    @Override
    public void disconnected(Output out) {}
    
    @Override
    public float currentVoltage() {return 1f-this.getIntensity().getLuminosity()/255f;}
    
    @Override
    public void paintExtra(Graphics2D g2, Component c) {}
    
    public static class Conical extends FlareShape {     
        private Triangle triangle;
        private transient Shape aux;
        
        public Conical(Vector2D focus, Vector2D direction, float width, float angle, Color c) {
            super(new Triangle(focus, direction, angle, width), (int)width, 255, 0, c);
            
            this.triangle = (Triangle)this.getShape();
            this.aux = triangle;
        }         
        
        @Override
        public void setPosition(Vector2D position) {
            this.shape = new Triangle(position, triangle.direction, triangle.baseAngle, triangle.width);
            this.triangle = (Triangle)super.getShape();
            this.aux = triangle;
        }
        @Override
        public Vector2D getPosition() {return this.triangle.focus;}
        
        @Override
        public Vector2D getDirection() {return this.triangle.direction;}
        public float getAngle() {return this.triangle.baseAngle;}
        public float getWidth() {return this.triangle.width;}
        @Override
        public void setDirection(Vector2D direction) {this.triangle.direction = direction;}
        public void setAngle(float angle) {this.triangle.setAngle(angle);}
        public void setWidth(float width) {this.triangle.setWidth(width);}
        
        @Override
        public final Shape getShape() {
            if(triangle == null)
                return super.getShape();
            return this.aux = triangle.getAffineTransform().createTransformedShape(super.getShape());
        }
        
        @Override
        public Polygon getInterferenceArea(Bound.Simple e) {
            java.awt.geom.Line2D boundLine = e.getLine();
            Vector2D[] pts = new Vector2D[this.triangle.npoints-1];
            for(int i=1;i<pts.length;i++) pts[i] = new Vector2D(this.triangle.xpoints[i], this.triangle.ypoints[i]);
            Vector2D focus = this.getPosition().copy();
            
            Vector2D[] polyPts = new Vector2D[4];
            polyPts[0] = Vector2D.convertFrom(boundLine.getP1()); 
            polyPts[1] = Vector2D.convertFrom(boundLine.getP2());

            float mult = 1;
            if(this.triangle.direction.getY() < focus.getY())
                mult = -1;
            
            return null;
        }
        
        @Override
        public double getAreaValue() {
            Vector2D basePoint = triangle.getBasePoint();
            Vector2D focus = triangle.focus.copy();
            double base = triangle.width;
            double dx = basePoint.getX()-focus.getX();
            double dy = basePoint.getY()-focus.getY();
            double height = Math.sqrt(dx*dx+dy*dy);
            return base*height/3;
        }
        
        @Override
        public LightSource copy() {
            Conical copy = new Conical(this.getTri().focus.copy(), this.getTri().direction.copy(), this.getTri().width, 
                    this.getTri().baseAngle, this.getLightColor());
            copy.setPeriods(this.getPeriods());
            copy.setBlinking(this.isBlinking());
            copy.setIntensity(this.getIntensity().copy());
            return copy;
        }
        
        public Shape getAux() {return aux;}
        public Triangle getTri() {return triangle;}
        
        public Vector2D[] getPoints(Shape s) {
            Vector2D[] pts = new Vector2D[3];
            int i=0;
            
            for(PathIterator it = s.getPathIterator(null);!it.isDone();it.next()) {
                float coords[] = new float[6];
                int type = it.currentSegment(coords);
                
                if(type != PathIterator.SEG_LINETO)
                    if(type != PathIterator.SEG_MOVETO)
                        continue;
                pts[i] = new Vector2D(coords[0], coords[1]);
                i++;
            }            
            return pts;
        }
        
        /**
         * Note to self: linear gradient should start on triangle's base point - width to base point.
         * */
        @Override
        public Paint getPaint() {       
            Vector2D basePoint = Triangle.getBasePoint(triangle.width, triangle.focus, triangle.direction);
            float mX = basePoint.getX() - this.getIntensity().getRange()*(float)Math.cos(Triangle.getAngle(triangle.focus, triangle.direction));
            float mY = basePoint.getY() - this.getIntensity().getRange()*(float)Math.sin(Triangle.getAngle(triangle.focus, triangle.direction));
            return new LinearGradientPaint(mX, mY, basePoint.getX(), basePoint.getY(), new float[] {0, 1},
                    new Color[]{ColorFactory.getColor(this.getLightColor(), this.getIntensity().getLuminosity()),
                        ColorFactory.getColor(LightManager.getColor(), LightManager.getDarkness())});
        }
        
        private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.shape = this.aux = (Shape)in.readObject();
        }
        
        private void writeObject(ObjectOutputStream out) throws java.io.IOException {
            out.defaultWriteObject();
            out.writeObject(this.triangle);
        }

        protected static class Triangle extends MutablePolygon {
            public Vector2D focus, direction;
            private Vector2D basePoint = new Vector2D();
            
            private float width;
            private float baseAngle;
            
            private AffineTransform at = new AffineTransform();
            
            public Triangle(Vector2D focus, Vector2D direction, float angle, float width) {                
                super(focus, focus, getP1(focus, width, angle), getP2(focus, width, angle));
                
                this.focus = focus;
                this.direction = direction;
                this.width = width;
                this.baseAngle = angle;
                
                at.rotate(direction.getX() - focus.getX(), direction.getY() - focus.getY(),
                        focus.getX(), focus.getY());
            }            
            
            /**
             * Just so we're clear, this is the Triangle's angle relative to the abscissa.
             * @return angle
             */
            public double getAngle() {
                double angle = Math.atan((direction.getY() - focus.getY())/(direction.getX() - focus.getX()));
                if(direction.getX() < focus.getX()) angle -= Math.PI;
                return angle;
            }
            
            public void setAngle(float angle) {
                this.baseAngle = angle;
                
                Vector2D p1 = getP1(focus, width, angle);
                this.xpoints[1] = (int)p1.getX();
                this.ypoints[1] = (int)p1.getY();
                
                Vector2D p2 = getP2(focus, width, angle);
                this.xpoints[2] = (int)p2.getX();
                this.ypoints[2] = (int)p2.getY();
            }
            
            public void setWidth(float width) {
                this.width = width;
                
                Vector2D p1 = getP1(focus, width, baseAngle);
                this.xpoints[1] = (int)p1.getX();
                this.ypoints[1] = (int)p1.getY();
                
                Vector2D p2 = getP2(focus, width, baseAngle);
                this.xpoints[2] = (int)p2.getX();
                this.ypoints[2] = (int)p2.getY();
            }
            
            /**
             * Note to self: the base point would be the point that is closer to the direction and
             * still within the triangle. That would be, considering the triangle is an isosceles,
             * the medium point of the triangle's base.
             * @return basePoint
             */
            private Vector2D getBasePoint() {
                float x = focus.getX() + width*(float)Math.cos(this.getAngle());
                float y = focus.getY() + width*(float)Math.sin(this.getAngle());
                
                return basePoint.set(x, y);
            }
            
            private Vector2D getRelativePoint() {
                float x = width*(float)Math.cos(getAngle()) + focus.getX();
                float y = direction.getY() - (x - direction.getX())*(float)Math.tan(getAngle());
                
                return new Vector2D(x, y);
            }
                        
            public static double getAngle(Vector2D focus, Vector2D direction) {
                double angle = Math.atan((direction.getY() - focus.getY())/(direction.getX() - focus.getX()));
                if(direction.getX() < focus.getX()) angle -= Math.PI;
                return angle;
            }
            
            public static Vector2D getRelativePoint(float width, Vector2D focus, Vector2D direction) {
                double angle = Math.atan((direction.getY() - focus.getY())/(direction.getX() - focus.getX()));
                if(direction.getX() < focus.getX()) angle -= Math.PI;
                
                float x = width*(float)Math.cos(angle) + focus.getX();
                float y = direction.getY() - (x - direction.getX())*(float)Math.tan(angle);
                
                return new Vector2D(x, y);
            }
            
            public static Vector2D getBasePoint(float width, Vector2D focus, Vector2D direction) {
                double angle = Math.atan((direction.getY() - focus.getY())/(direction.getX() - focus.getX()));
                if(direction.getX() < focus.getX()) angle -= Math.PI;
                
                float x = focus.getX() + width*(float)Math.cos(angle);
                float y = focus.getY() + width*(float)Math.sin(angle);
                
                return new Vector2D(x, y);
            }
            
            private static Vector2D getP1(Vector2D focus, float width, float angle) {
                float x0 = focus.getX() + width;
                float y0 = focus.getY() + (float)(width*Math.tan(angle));
                
                return new Vector2D(x0, y0);
            }
            
            private static Vector2D getP2(Vector2D focus, float width, float angle) {
                float x1 = focus.getX() + width;
                float y1 = focus.getY() - (float)(width*Math.tan(angle));
                
                return new Vector2D(x1, y1);
            }
            
            protected AffineTransform getAffineTransform() {return at;}
            
            @Override
            protected void refresh() {
                super.refresh();
                at.setToIdentity();
                at.rotate(getAngle(), focus.getX(), focus.getY());
            }
        }
        
        private static class MutablePolygon extends Polygon {
            protected Vector2D reference; //reference-by-value
            protected Vector2D constant; //constant value

            public MutablePolygon(Vector2D reference, Vector2D... points) {
                super(getX(points), getY(points), points.length);

                this.reference = reference;
                this.constant = reference.copy();
            }

            private static int[] getX(Vector2D[] points) {
                int[] x = new int[points.length];
                for(int i=0;i<points.length;i++)
                    x[i] = (int)points[i].getX();
                return x;
            }

            private static int[] getY(Vector2D[] points) {
                int[] y = new int[points.length];
                for(int i=0;i<points.length;i++)
                    y[i] = (int)points[i].getY();
                return y;
            }

            public Vector2D getReference() {return reference;}

            protected void refresh() {
                this.translate((int)(reference.getX() - constant.getX()), 
                        (int)(reference.getY() - constant.getY()));
                constant = reference.copy();
            }

            @Override
            public Rectangle getBounds() {
                this.refresh();
                return super.getBounds();
            }

            @Override
            public Rectangle2D getBounds2D() {
                this.refresh();
                return super.getBounds2D();
            }

            @Override
            public PathIterator getPathIterator(AffineTransform at) {
                this.refresh();
                return super.getPathIterator(at);
            }

            @Override
            public boolean contains(double x, double y) {
                this.refresh();
                return super.contains(x, y);
            }
        }
    }
    
    public static class Elliptic extends FlareShape {             
        public Elliptic(Vector2D pos, Vector2D size, int range, int flareWidth, int luminosity, Color c) {
            super(new MutableEllipse(pos, size), range, flareWidth, luminosity, c);
        }

        @Override
        public double getAreaValue() {
            //Circular for now. Don't know the formula for elliptic.
            MutableEllipse el = (MutableEllipse)this.getShape();
            double radius = el.getWidth();
            return Math.PI*radius*radius;
        }
        
        @Override
        public Polygon getInterferenceArea(Bound.Simple e) {
            return null;
        }
        
        @Override
        public Paint getPaint() {
            Point center = new Point((int)this.getBounds2D().getCenterX(), (int)this.getBounds2D().getCenterY());
            return new RadialGradientPaint(center, (float)(this.getBounds2D().getWidth()/2),
                    center, new float[] {0, 1}, 
                    new Color[]{ColorFactory.getColor(this.getLightColor(), this.getIntensity().getLuminosity()), 
                    ColorFactory.getColor(LightManager.getColor(), LightManager.getDarkness())}, 
                    MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }
        
        @Override
        public void setPosition(Vector2D position) {((MutableEllipse)this.shape).pos = position;}
        @Override
        public Vector2D getPosition() {return ((MutableEllipse)this.shape).pos;}

        @Override
        public LightSource copy() {
            MutableEllipse ellipse = (MutableEllipse)this.shape;
            Elliptic copy = new Elliptic(ellipse.pos.copy(), ellipse.size.copy(), 
                    this.getIntensity().getRange(), this.getIntensity().getFlareWidth(), this.getIntensity().getLuminosity(), 
                    this.getLightColor());
            copy.setPeriods(this.getPeriods());
            copy.setBlinking(this.isBlinking());
            copy.setIntensity(this.getIntensity().copy());
            return copy;
        }
        
        private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.shape = (Shape)in.readObject();
        }
        
        private void writeObject(ObjectOutputStream out) throws java.io.IOException {
            out.defaultWriteObject();
            out.writeObject((MutableEllipse)this.shape);
        }
        
        private static class MutableEllipse extends Ellipse2D.Float {
            private Vector2D pos, size;
            private MutableEllipse(Vector2D position, Vector2D size) {
                super(position.getX(), position.getY(), size.getX(), size.getY());
                this.pos = position;
                this.size = size;
            }
            
            @Override
            public double getX() {return pos.getX() - size.getX()/2;}
            @Override
            public double getY() {return pos.getY() - size.getY()/2;}
            @Override
            public double getWidth() {return size.getX();}
            @Override
            public double getHeight() {return size.getY();}
            @Override
            public Rectangle2D getBounds2D() {
                return new Rectangle2D.Double(getX(), getY(), size.getX(), size.getY());
            }
            @Override
            public Rectangle getBounds() {
                return new Rectangle((int)getX(), (int)getY(), (int)size.getX(), (int)size.getY());
            }
            
            public void setFrame(Vector2D pos, Vector2D size) {                
                this.pos = pos;
                this.size = size;
                
                this.setFrame(pos.getX(), pos.getY(), size.getX(), size.getY());
            }
        }
    }    
    
    public static class Polygonal extends FlareShape {
        private final int n;
        private Conical.MutablePolygon polygon;

        public Polygonal(Vector2D[] points, int range, int flareWidth, int luminosity, Color c) {
            super(new Conical.MutablePolygon(points[0], points), range, flareWidth, luminosity, c);
            
            this.n = points.length;
            this.polygon = (Conical.MutablePolygon)this.getShape();
        }
        
        public synchronized void addPoint(Vector2D point) {polygon.addPoint((int)point.getX(), (int)point.getY());}

        @Override
        public double getAreaValue() {
            //TODO: Triangulate the poly.
            int tris = n-2;
            double innerSides[] = new double[n-1];
//            for(int i=1;i<n;i++) {
//                double dx = polygon.xpoints[0]-polygon.xpoints[i];
//                double dy = polygon.ypoints[0]-polygon.ypoints[i];
//                innerSides[i-1] = Math.sqrt(dx*dx+dy*dy);
//            }
            double outerSides[] = new double[n-2];
//            for(int i=1;i<=outerSides.length;i++) {
//                double dx = polygon.xpoints[i]-polygon.xpoints[i+1];
//                double dy = polygon.ypoints[i]-polygon.ypoints[i+1];
//                outerSides[i-1] = Math.sqrt(dx*dx+dy*dy);
//            }
            double area = 0;
//            for(int i=1;i<=tris;i++) {
//                double a = innerSides[i-1];
//                double b = innerSides[i];
//                double c = outerSides[i-1];
//                double p = (a+b+c)/2;
//                double heron = Math.sqrt(p*(p-a)*(p-b)*(p-c));
//                area += heron;
//            }
            
            for(int i=1;i<n;i++) {
                {
                    double dx = polygon.xpoints[0]-polygon.xpoints[i];
                    double dy = polygon.ypoints[0]-polygon.ypoints[i];
                    innerSides[i-1] = Math.sqrt(dx*dx+dy*dy);
                }
                if(i<=outerSides.length) {
                    double dx = polygon.xpoints[i]-polygon.xpoints[i+1];
                    double dy = polygon.ypoints[i]-polygon.ypoints[i+1];
                    outerSides[i-1] = Math.sqrt(dx*dx+dy*dy);
                }
            }
            
            for(int i=1;i<=tris;i++) {
                double a = innerSides[i-1];
                double b = innerSides[i];
                double c = outerSides[i-1];
                double p = (a+b+c)/2;
                double heron = Math.sqrt(p*(p-a)*(p-b)*(p-c));
                area += heron;
            }
            
            return area;
        }
        
        @Override
        public Vector2D getPosition() {return polygon.reference;}
        @Override
        public void setPosition(Vector2D position) {polygon.reference = position;}

        @Override
        public synchronized Paint getPaint() {
            //Create first linear gradient paint from first line.
            int[] xpoints = new int[polygon.xpoints.length];
            int[] ypoints = new int[polygon.ypoints.length];

            for(int j=0;j<xpoints.length;j++) {
                xpoints[j] = polygon.xpoints[j];
                ypoints[j] = polygon.ypoints[j];
            }
            
            float startX = xpoints[0] + (xpoints[1]-xpoints[0])/2;
            float startY = ypoints[0] + (ypoints[1]-ypoints[0])/2;
            
            float f = this.getIntensity().getFlareWidth();
            double alpha = Math.abs(Math.atan2((ypoints[1]-ypoints[0]), (xpoints[1]-xpoints[0])));
//            alpha = alpha>Math.PI/2?Math.abs(Math.PI-alpha):alpha;
//            float x = alpha == 0 || Math.abs(alpha) == 2*Math.PI? 0 :
//                Math.abs(alpha) == Math.PI/2 || Math.abs(alpha) == 3*Math.PI/2? f :
//                ((xpoints[1]-xpoints[0])*f)/((float)Math.cos(alpha)*(ypoints[1]-ypoints[0]));
//            float y = alpha == 0 || Math.abs(alpha) == 2*Math.PI? f :
//                Math.abs(alpha) == Math.PI/2 || Math.abs(alpha) == 3*Math.PI/2? 0 :
//                (float)Math.tan(alpha)*x;
            
            float x = f*(float)Math.sin(alpha);
            float y = f*(float)Math.cos(alpha);

//            int inside = this.polygon.contains(startX + x, startY + y)?1:-1;
            
            float dx = Math.signum(xpoints[1]-xpoints[0]);
            float dy = Math.signum(ypoints[1]-ypoints[0]);
            float multX=1, multY=1;
            if(dx==-1&&dy==0){multX=0;multY=1;}
            else if(dx==-1&&dy==-1){multX=-1;multY=-1;}
            else if(dx==0&&dy==-1){multX=-1;multY=0;}
            else if(dx==1&&dy==-1){multX=-1;multY=-1;}
            else if(dx==1&&dy==0){multX=0;multY=-1;}
            else if(dx==1&&dy==1){multX=1;multY=-1;}
            else if(dx==0&&dy==1){multX=1;multY=0;}
            else if(dx==-1&&dy==1){multX=1;multY=-1;}

            float endX = startX + x*multX;
            float endY = startY + y*multY;
            
//            float multY = ypoints[1]-ypoints[0]>0&&xpoints[1]-xpoints[0]>0?1:
//                        ypoints[1]-ypoints[0]<0&&xpoints[1]-xpoints[0]>0?1:
//                        ypoints[1]-ypoints[0]>0&&xpoints[1]-xpoints[0]<0?-1:
//                        /*equivalent to: 
//                         * ypoints[k]-ypoints[i]<0&&xpoints[k]-xpoints[i]<0?*/-1;
//                
//                float endX = startX + x*inside;
//                float endY = startY + y*multY;
//                endY = !this.polygon.contains(endX, endY)?startY+y*(-multY):endY;
            
            return new LinearGradientPaint(startX, startY, endX, endY, 
                new float[]{0, 1}, new Color[]{ColorFactory.getColor(LightManager.getColor(), LightManager.getDarkness()), ColorFactory.getColor(this.getLightColor(), this.getIntensity().getLuminosity()/n)}, 
                LinearGradientPaint.CycleMethod.NO_CYCLE);
        }

        @Override
        public LightSource copy() {
            Vector2D[] points = new Vector2D[n];
            for(int i=0;i<n;i++) points[i] = new Vector2D(polygon.xpoints[i], polygon.ypoints[i]);
            Polygonal copy = new Polygonal(points, getIntensity().getRange(), getIntensity().getFlareWidth(), getIntensity().getLuminosity(), getLightColor());
            copy.setPeriods(this.getPeriods());
            copy.setBlinking(this.isBlinking());
            copy.setIntensity(this.getIntensity().copy());
            return copy;
        }
        
        @Override
        public synchronized void paintExtra(Graphics2D g2, Component c) {            
            for(int i=1;i<n;i++) {
                //Generic version of the first linear gradient paint from first liner.
                int k = i==n-1?0:i+1;

                int[] xpoints = new int[polygon.xpoints.length];
                int[] ypoints = new int[polygon.ypoints.length];

                for(int j=0;j<xpoints.length;j++) {
                    xpoints[j] = polygon.xpoints[j];
                    ypoints[j] = polygon.ypoints[j];
                }
                
                float startX = xpoints[i] + (xpoints[k] - xpoints[i])/2;
                float startY = ypoints[i] + (ypoints[k] - ypoints[i])/2;

                float f = this.getIntensity().getFlareWidth();
                double alpha = Math.abs(Math.atan2((ypoints[k]-ypoints[i]), (xpoints[k]-xpoints[i])));
//                alpha = alpha>Math.PI/2?Math.abs(Math.PI-alpha):alpha;
//                System.err.println("New: " + Math.toDegrees(alpha));
//                float x = alpha == 0 || Math.abs(alpha) == 2*Math.PI? 0 :
//                            Math.abs(alpha) == Math.PI/2 || Math.abs(alpha) == 3*Math.PI/2? f :
//                            ((xpoints[k]-xpoints[i])*f)/((float)Math.cos(alpha)*(ypoints[k]-ypoints[i]));
//                float y = alpha == 0 || Math.abs(alpha) == 2*Math.PI? f :
//                            Math.abs(alpha) == Math.PI/2 || Math.abs(alpha) == 3*Math.PI/2? 0 :
//                            (float)Math.tan(alpha)*x;
                
                float x = f*(float)Math.sin(alpha);
                float y = f*(float)Math.cos(alpha);

                int inside = this.polygon.contains(startX + x, startY + y)?1:-1;
                
//                float multY = ypoints[k]-ypoints[i]>0&&xpoints[k]-xpoints[i]>0?1:
//                        ypoints[k]-ypoints[i]<0&&xpoints[k]-xpoints[i]>0?1:
//                        ypoints[k]-ypoints[i]>0&&xpoints[k]-xpoints[i]<0?-1:
//                        /*equivalent to: 
//                         * ypoints[k]-ypoints[i]<0&&xpoints[k]-xpoints[i]<0?*/-1;
                
                /*  x_____|_____y               dx______|_______dy
                 *  0     |     1               -1      |       0           
                 *  -1    |     1               -1      |       -1
                 *  -1    |     0               0       |       -1
                 *  -1    |     -1              1       |       -1
                 *  0     |     -1              1       |       0
                 *  1     |     -1              1       |       1
                 *  1     |     0               0       |       1
                 *  1     |     1               -1      |       1
                 */
                float dx = Math.signum(xpoints[k]-xpoints[i]);
                float dy = Math.signum(ypoints[k]-ypoints[i]);
                float multX=1, multY=1;
                if(dx==-1&&dy==0){multX=0;multY=1;}
                else if(dx==-1&&dy==-1){multX=-1;multY=-1;}
                else if(dx==0&&dy==-1){multX=-1;multY=0;}
                else if(dx==1&&dy==-1){multX=-1;multY=-1;}
                else if(dx==1&&dy==0){multX=0;multY=-1;}
                else if(dx==1&&dy==1){multX=1;multY=-1;}
                else if(dx==0&&dy==1){multX=1;multY=0;}
                else if(dx==-1&&dy==1){multX=1;multY=-1;}
                
//                float endX = startX + x*inside;
//                float endY = startY + y*inside*multY;   
//                endY = !this.polygon.contains(endX, endY)?startY+y*inside*(-multY):endY;
                float endX = startX + x*multX;
                float endY = startY + y*multY;

                LinearGradientPaint paint = new LinearGradientPaint(startX, startY, endX, endY, 
                    new float[]{0, 1}, new Color[]{ColorFactory.getColor(LightManager.getColor(), LightManager.getDarkness()), ColorFactory.getColor(this.getLightColor(), this.getIntensity().getLuminosity()/n)}, 
                    LinearGradientPaint.CycleMethod.NO_CYCLE);
                
//                Color defColor = g2.getColor();
//                g2.setColor(Color.RED);
//                g2.fillOval((int)endX-5, (int)endY-5, 10, 10);
//                g2.setColor(Color.BLUE);
//                g2.fillOval((int)startX-5, (int)startY-5, 10, 10);
//                g2.setColor(Color.YELLOW);
//                g2.fillOval((int)(startX+x*(-inside))-5, (int)(startY+y*(-inside))-5, 10, 10);
//                g2.setColor(defColor);
                Paint defPaint = g2.getPaint();
                g2.setPaint(paint);
                g2.fill(this.getArea());
                g2.draw(this.getArea());
                g2.setPaint(defPaint);
            }
        }
        
        @Override
        public Polygon getInterferenceArea(Bound.Simple e) {
            return null;
        }
        
        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.shape = (Shape)in.readObject();
        }
        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            out.defaultWriteObject();
            out.writeObject((Conical.MutablePolygon)this.shape);
        }
    }
    
    public static class Trapezoid extends FlareShape {
        private Trapezium trapezium;
        
        public Trapezoid(Vector2D focus, Vector2D direction, float minBase, float maxBase, float width, int flareWidth, Color c) {
            super(new Trapezium(focus, direction, minBase, maxBase, width), flareWidth, 255, 0, c);
        
            this.trapezium = (Trapezium)this.getShape();
        }
        
        @Override
        public double getAreaValue() {
            Vector2D focus = trapezium.getFocus().copy();
            Vector2D basePoint = trapezium.getBasePoint().copy();
            double dx = basePoint.getX()-focus.getX();
            double dy = basePoint.getY()-focus.getY();
            double height = Math.sqrt(dx*dx+dy*dy);
            return (trapezium.getMinBase()+trapezium.getMaxBase())*height/2;
        }
        
        @Override
        public final Shape getShape() {
            return trapezium==null?super.getShape():trapezium.getAffineTransform().createTransformedShape(super.getShape());
        }
        
        @Override
        public Polygon getInterferenceArea(Bound.Simple e) {
            return null;
        }
        
        @Override
        public Vector2D getPosition() {return trapezium.getFocus();}
        @Override
        public void setPosition(Vector2D position) {trapezium.setPosition(position);}
        
        @Override
        public void setDirection(Vector2D direction) {this.trapezium.setDirection(direction);}
        @Override
        public Vector2D getDirection() {return this.trapezium.getBasePoint();}
        
        public float getWidth() {return trapezium.getWidth();}
        public void setWidth(float width) {this.trapezium.setWidth(width);}

        private Vector2D getReferencePoint() {
            Vector2D basePoint = this.trapezium.getBasePoint();
            float alpha = this.trapezium.getAngle();
            float f = this.getIntensity().getFlareWidth();      
            return new Vector2D(basePoint.getX()-f*Math.cos(alpha), basePoint.getY()-f*Math.sin(alpha));
        }
        
        @Override
        public Paint getPaint() {
            Vector2D reference = this.getReferencePoint();
            Vector2D basePoint = this.trapezium.getBasePoint();
            return new LinearGradientPaint(reference.getX(), reference.getY(), basePoint.getX(), basePoint.getY(),
                    new float[]{0, 1}, new Color[]{ColorFactory.getColor(this.getLightColor(), this.getIntensity().getLuminosity()),
                        ColorFactory.getColor(LightManager.getColor(), LightManager.getDarkness())}, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        }

        @Override
        public LightSource copy() {
            Trapezoid copy = new Trapezoid(this.trapezium.getFocus(), this.trapezium.getBasePoint(), 
                    this.trapezium.getMinBase(), this.trapezium.getMaxBase(), 
                    this.trapezium.getWidth(), this.getIntensity().getFlareWidth(), this.getLightColor());
            copy.setPeriods(this.getPeriods());
            copy.setBlinking(this.isBlinking());
            copy.setIntensity(this.getIntensity().copy());
            return copy;
        }
        
        private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.shape = (Shape)in.readObject();
        }
        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            out.defaultWriteObject();
            out.writeObject((Trapezium)this.shape);
        }
        
        private static class Trapezium extends Conical.MutablePolygon {
            private AffineTransform at = new AffineTransform();
            private Vector2D direction;
            private float width;
            private float maxBase, minBase;
            
            public Trapezium(Vector2D focus, Vector2D direction, float minBase, float maxBase, float width) {
                super(focus, focus.add(-minBase/2, 0), focus.add(minBase/2, 0), focus.add(maxBase/2, width), focus.add(-maxBase/2, width));
                
                this.direction = direction;
                this.width = width;
                this.maxBase = maxBase;
                this.minBase = minBase;
                
                double angle = Math.atan2(direction.getY()-reference.getY(), direction.getX()-reference.getX());
                at.rotate(angle, this.reference.getX(), this.reference.getY());
            }
            
            private float getAngle() {
//                float fX = this.xpoints[1]-this.xpoints[0]/2;
//                float fY = this.ypoints[0]-this.ypoints[1];
//                
//                float mX = this.xpoints[2]-this.xpoints[3];
//                float mY = this.ypoints[3]-this.ypoints[2];
//                
//                return (float)Math.atan2(mX-fX, mY-fY);
                return (float)Math.atan2(direction.getY()-reference.getY(), direction.getX()-reference.getX());
            }
            
            private float getMinBase() {
                return minBase;
//                float alpha = this.getAngle();
//                float sin = (float)Math.sin(alpha);
//                Vector2D focus = this.getFocus();
//                return sin!=0?2*(focus.getX()-xpoints[0])/sin:2*(ypoints[0]-focus.getY())/(float)Math.cos(alpha);
            }
            private float getMaxBase() {
                return maxBase;
//                float alpha = this.getAngle();
//                float sin = (float)Math.sin(alpha);
//                Vector2D base = this.getBasePoint();
//                return sin!=0?2*(base.getX()-xpoints[3])/sin:2*(ypoints[3]-base.getY())/(float)Math.cos(alpha);
            }
            private float getWidth() {
                return width;
//                float alpha = this.getAngle();
//                float sin = (float)Math.sin(alpha);
//                Vector2D focus = this.getFocus();
//                Vector2D base = this.getBasePoint();
//                return sin!=0?(base.getY()-focus.getY())/sin:(base.getX()-focus.getX())/(float)Math.cos(alpha);
            }
//            private Vector2D getFocus() {return new Vector2D(this.xpoints[1]-this.xpoints[0], this.ypoints[0]-this.ypoints[1]);}
            private Vector2D getFocus() {return this.reference;}
//            private Vector2D getBasePoint() {return new Vector2D(this.xpoints[2]-this.xpoints[3], this.ypoints[3]-this.ypoints[2]);}
            private Vector2D getBasePoint() {
                double angle = Math.atan2(direction.getY()-reference.getY(), direction.getX()-reference.getX());
                float dx = (float)Math.cos(angle)*width;
                float dy = (float)Math.sin(angle)*width;
                return getFocus().add(dx, dy);
            }
            @Deprecated
            private void setAngle(float angle) {
                Vector2D focus = this.getFocus();
                float minB = this.getMinBase();
                float maxB = this.getMaxBase();
                float w = this.getWidth();
                Vector2D dir = new Vector2D(focus.getX()+w*Math.cos(angle), focus.getY()+w*Math.sin(angle));
                
                Vector2D[] points = {getMinPort(focus, dir, minB), getMinStarboard(focus, dir, minB),
                    getMaxStarboard(focus, dir, maxB, w), getMaxPort(focus, dir, maxB, w)};                
                
                for(int i=0;i<points.length;i++) {
                    this.xpoints[i] = (int)points[i].getX();
                    this.ypoints[i] = (int)points[i].getY();
                }
            }
            private void setDirection(Vector2D direction) {
//                Vector2D focus = this.getFocus();
//                this.setAngle((float)Math.atan2(direction.getY()-focus.getY(), direction.getX()-focus.getX()));
                this.direction = direction;
            }
            private void setWidth(float width) {
//                Vector2D focus = this.getFocus();
//                Vector2D dir = this.getBasePoint();
//                float maxBase = this.getMaxBase();
//                Vector2D maxStarboard = getMaxStarboard(focus, dir, maxBase, width);
//                Vector2D maxPort = getMaxPort(focus, dir, maxBase, width);
//                this.xpoints[2] = (int)maxStarboard.getX(); this.ypoints[2] = (int)maxStarboard.getY();
//                this.xpoints[3] = (int)maxPort.getX(); this.ypoints[3] = (int)maxPort.getY();

//                float dw = this.width - width;
//                double angle = Math.atan2(direction.getY()-reference.getY(), direction.getX()-reference.getY());
//                float dx = -(float)Math.cos(angle)*dw;
//                float dy = -(float)Math.sin(angle)*dw;
//                
//                this.xpoints[2] += dx; this.ypoints[2] += dy;
//                this.xpoints[3] += dx; this.ypoints[3] += dy;
//                
//                this.width = width;
                Vector2D focus = reference;
                Vector2D[] points = {focus.add(-minBase/2, 0), focus.add(minBase/2, 0), 
                    focus.add(maxBase/2, width), focus.add(-maxBase/2, width)};
                
                for(int i=0;i<points.length;i++) {
                    xpoints[i] = (int)points[i].getX();
                    ypoints[i] = (int)points[i].getY();
                }
                
                this.width = width;
            }
            private void setPosition(Vector2D position) {
//                Vector2D ref = position.subtract(this.getFocus());
//                this.reference = position;
//                for(int i=0;i<this.npoints;i++) {
//                    this.xpoints[i] += ref.getX();
//                    this.ypoints[i] += ref.getY();
//                }
                this.reference = position;
            }
            @Deprecated
            private static Vector2D getMinPort(Vector2D focus, Vector2D direction, float minBase) {
                float alpha = (float)Math.atan2(direction.getX()-focus.getX(), direction.getY()-focus.getY());
                
                float x = focus.getX()-(minBase*(float)Math.sin(alpha))/2;
                float y = focus.getY()+(minBase*(float)Math.cos(alpha))/2;
                
                return new Vector2D(x, y);
            }
            @Deprecated
            private static Vector2D getMinStarboard(Vector2D focus, Vector2D direction, float minBase) {
                float alpha = (float)Math.atan2(direction.getX()-focus.getX(), direction.getY()-focus.getY());
            
                float x = focus.getX()+(minBase*(float)Math.sin(alpha))/2;
                float y = focus.getY()-(minBase*(float)Math.cos(alpha))/2;
                
                return new Vector2D(x, y);
            }
            @Deprecated
            private static Vector2D getMaxPort(Vector2D focus, Vector2D direction, float maxBase, float width) {
                float alpha = (float)Math.atan2(direction.getX()-focus.getX(), direction.getY()-focus.getY());
            
                float mX = focus.getX()+(float)Math.sin(alpha)*width;
                float mY = focus.getY()+(float)Math.cos(alpha)*width;
                
                float x = mX-(maxBase*(float)Math.sin(alpha))/2;
                float y = mY+(maxBase*(float)Math.cos(alpha))/2;
                
                return new Vector2D(x, y);
            }
            @Deprecated
            private static Vector2D getMaxStarboard(Vector2D focus, Vector2D direction, float maxBase, float width) {
                float alpha = (float)Math.atan2(direction.getX()-focus.getX(), direction.getY()-focus.getY());
                
                float mX = focus.getX()+(float)Math.sin(alpha)*width;
                float mY = focus.getY()+(float)Math.sin(alpha)*width;
                
                float x = mX+(maxBase*(float)Math.sin(alpha))/2;
                float y = mY-(maxBase*(float)Math.cos(alpha))/2;
                
                return new Vector2D(x, y);
            }
            
            protected AffineTransform getAffineTransform() {return at;}
            
            @Override
            protected void refresh() {
                super.refresh();
                at.setToIdentity();
                double angle = Math.atan2(direction.getY()-reference.getY(), direction.getX()-reference.getX())-Math.PI/2;
                at.rotate(angle, this.reference.getX(), this.reference.getY());
            }
        }
    }
}
