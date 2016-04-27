package geometryzombiesmayhem;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A convenience positioning class. Not to be confused with a Force Vector.
 * Composed of two floats, x and y.
 * 
 * @author Renato Lui Geh
 * @version 0.0.6
 */
public final class Vector2D implements java.io.Serializable{
    private float x;
    private float y;
    
    public Vector2D(double x, double y) { this((float) x,(float) y); }
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2D(Vector2D position) {
         this.set(position);
    }
    public Vector2D() {
        set(ZERO);
    }
    public Vector2D(Point p) {
        set(p);
    }
    
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    
    public Vector2D set(Vector2D position) {
        this.x = position.getX();
        this.y = position.getY();
        
        return this;
    }
    
    public Vector2D set(Point p) {
        this.x = p.x;
        this.y = p.y;
        
        return this;
    }
    
    public Vector2D set(float x, float y) {
        this.x = x;
        this.y = y;
        
        return this;
    }
    public Vector2D setX(float x) { 
        this.x = x;
        
        return this;
    }
    public Vector2D setY(float y){
        this.y = y;
        
        return this;
    }
    
    public Vector2D add(Vector2D position) {        
        return new Vector2D(this.x + position.getX(), this.y + position.getY());
    }
    public Vector2D add(float x, float y) {
        return new Vector2D(this.x + x, this.y + y);
    }
    public Vector2D addLocal(Vector2D position) {
        this.x += position.getX();
        this.y += position.getY();
        
        return position;
    }
    public Vector2D addLocal(float x, float y) {
        this.x += x;
        this.y += y;
        
        return this;
    }
    
    public static java.awt.Point convertToPoint(Vector2D v) {
        if(v == null)
            return new Point(0, 0);
        else
            return new Point((int)v.getX(), (int)v.getY());
    }
    
    public static java.awt.Dimension convertToDimension(Vector2D v) {
        if(v == null)
            return new java.awt.Dimension(0, 0);
        else
            return new java.awt.Dimension((int)v.getX(), (int)v.getY());
    }
    
    public static Vector2D convertFrom(java.awt.Point point) {
        if(point == null)
            return Vector2D.BOTTOM_RIGHT.copy();
        else
            return new Vector2D((float)point.getX(), (float)point.getY());
    }
    
    public static Vector2D convertFrom(java.awt.Dimension dim) {
        if(dim == null)
            return Vector2D.BOTTOM_RIGHT.copy();
        else
            return new Vector2D((float)dim.getWidth(), (float)dim.getHeight());
    }
    
    public static Vector2D convertFrom(java.awt.geom.Point2D point) {
        if(point == null)
            return Vector2D.BOTTOM_RIGHT.copy();
        else
            return new Vector2D(point.getX(), point.getY());
    }
    
    public Vector2D subtract(Vector2D position) {
        return new Vector2D(this.x - position.getX(), this.y - position.getY());
    }
    
    public Vector2D subtract(float x, float y) {
        return new Vector2D(this.x - x, this.y - y);
    }
    
    public Vector2D subtractLocal(Vector2D position) {
        this.x -= position.getX();
        this.y -= position.getY();
        
        return this;
    }
    public Vector2D subtractLocal(float x, float y) {
        this.x -= x;
        this.y -= y;
        
        return this;
    }
    
    public Vector2D mult(Vector2D position) {
        return new Vector2D(this.x * position.getX(), this.y * position.getY());
    } 
    public Vector2D multLocal(Vector2D position) {
        this.x *= position.getX();
        this.y *= position.getY();
        
        return this;
    }
    public Vector2D multConstLocal(float m) {
        this.x *= m;
        this.y *= m;
        
        return this;
    }
    public Vector2D multConst(float m) {
        return new Vector2D(x * m, y * m);
    }
    
    public Vector2D divide(Vector2D position) {
        return new Vector2D(this.x / position.getX(), this.y / position.getY());
    }
    public Vector2D divideLocal(Vector2D position) {
        this.x /= position.getX();
        this.y /= position.getY();
        
        return this;
    }
    
    public Vector2D distanceVector(Vector2D position) {
        return new Vector2D(this.x - position.getX(), this.y - position.getY());
    }
    public Vector2D distanceLocal(Vector2D position) {
        this.x -= position.getX();
        this.y -= position.getY();
        
        return this;
    }
    public float distance(Vector2D position) {
        return (float)(Math.sqrt(distanceSqr(position)));
    }
    public float distanceSqr(Vector2D p) {
        return (p.getX()-getX())*(p.getX()-getX()) + (p.getY()-getY())*(p.getY()-getY());
    }
    
    public Vector2D copy() {
        return new Vector2D(this);
    }
    
    public Vector2D negate() {
        return new Vector2D(this.mult(NEGATIVE));
    }
    
    public void zero() {
        this.x = 0; this.y = 0;
    }
    
    public Vector2D negateLocal() {
        return this.multLocal(NEGATIVE);
    }
    
    public static Vector2D parseVector(String v) {
        String message = "Incoherent vector string: ";
        String line = v.replace(',', ' ');
        String position[];
        Vector2D vector;
        
        if(v.charAt(0) == '[') {
            if(v.charAt(v.length()-1) == ']') {
                line = line.replace('[', ' ');
                line = line.replace(']', ' ');
            } else {
                throw new IllegalArgumentException(message + "missing final bracket.");
            }
        } else if(v.charAt(v.length()-1) == ']') {
            throw new IllegalArgumentException(message + "missing initial bracket.");
        }
        
        position = line.split(" ");
        String aux[] = new String[2];
        int tracker = 0;
        
        for(String s : position) {    
            s = s.trim();
            
            if(s.isEmpty()) {
                continue;
            } else {
                aux[tracker] = s;
                tracker++;
            }
        }
        
        vector = parser(aux);
        
        return vector;
    }
    
    private static Vector2D parser(String[] fields) {
        if(fields.length != 2) {
            for(String s : fields)
                System.err.println(s);
            
            throw new IllegalArgumentException("Incoherent vector string: found more or less than 2 fields.");
        } else {
            Vector2D vector;

            for(String s : fields)
                s = s.trim();

            float a = Float.parseFloat(fields[0]);
            float b = Float.parseFloat(fields[1]);

            vector = new Vector2D(a, b);

            return vector;
        }
    }
    
    @Override
    public String toString() {
        return ("[" + this.x + "," + this.y + "]");
    }
    
    public String toFormattedString(int decimals) {
        String pattern = "#0.";
        
        for(int i=0;i<decimals;i++)
            pattern += 0;
        
        if(decimals == 0)
            pattern = "#0";
        
        FORMATTER.applyPattern(pattern);
        
        return ("[" + FORMATTER.format(this.x) + "," + FORMATTER.format(this.y) + "]");
    }
    
    public boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }
    
    public boolean equalsRounded(Vector2D e) {return distanceSqr(e)<100;}
    
    @Override
    public boolean equals(Object e) {
        if(e instanceof Vector2D) {
            Vector2D copy = (Vector2D)e;
            return this.equals(copy.getX(), copy.getY());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Float.floatToIntBits(this.x);
        hash = 37 * hash + Float.floatToIntBits(this.y);
        return hash;
    }
    
    public static boolean isVector(String e) {
        if(e == null) return false;

        String s = e;
        if(s.startsWith("[") || s.endsWith("]")) {
            s = s.replaceAll("\\[", "");
            s = s.replaceAll("]", "");
        }

        String[] split = s.split(" ");
        if(split.length == 2) {
            split[0] = split[0].trim();
            split[1] = split[1].trim();

            if(split[0].endsWith(","))
                split[0] = split[0].substring(0, split.length-1);
            if(split[1].startsWith(","))
                split[1] = split[1].substring(1, split.length);

            try {
                Float.parseFloat(split[0]);
                Float.parseFloat(split[1]);
            } catch(NumberFormatException exc) {
                return false;
            }

            return true;
        } else if(split.length == 1) {
            split = s.split(",");
            if(split.length == 2) {
                try {
                    Float.parseFloat(split[0]);
                    Float.parseFloat(split[1]);
                } catch(NumberFormatException exc) {
                    return false;
                }

                return true;
            }
            return false;
            //TODO: x,y
        }
        return false;
    }
    
    public static Vector2D getIntersectionPoint(Vector2D p0, Vector2D p1, Vector2D p2, Vector2D p3) {
        float x = ((p0.x*p1.y-p1.x*p0.y)*(p2.x-p3.x)/(p0.x-p1.x)-(p2.x*p3.y-p3.x*p2.y))/
                ((p2.y-p3.y)-(p0.y-p1.y)*(p2.x-p3.x)/(p0.x-p1.x));
//        float y = ((p0.x*p1.y-p1.x*p0.y)*(p2.y-p3.y)/(p0.y-p1.y)-(p2.x*p3.y-p3.x*p2.y))/
//                ((p0.x-p1.x)*(p2.y-p3.y)/(p0.y-p1.y)-(p2.x-p3.x));
//        float x = (y*(p0.x-p1.x)-(p0.x*p1.y-p1.x*p0.y))/(p0.y-p1.y);
        float y = (x*(p0.y-p1.y)+(p0.x*p1.y-p1.x*p0.y))/(p0.x-p1.x);
        if(isNaN(x)||isNaN(y)) throw new IllegalArgumentException("Lines do not intersect!");
        return new Vector2D(x, y);
    }
    public static Vector2D getIntersectionPoint(Line2D l0, Line2D l1) {
        return getIntersectionPoint(convertFrom(l0.getP1()), convertFrom(l0.getP2()), 
                convertFrom(l1.getP1()), convertFrom(l1.getP2()));
    }
    
    private static boolean isNaN(float n) {
        return Float.floatToIntBits(n)==Float.floatToIntBits(Float.NaN);
    }
    
    public static ArrayList<Vector2D> getPoints(Shape s) {
        ArrayList<Vector2D> pts = new ArrayList<>();

        for(PathIterator it = s.getPathIterator(null);!it.isDone();it.next()) {
            float coords[] = new float[6];
            int type = it.currentSegment(coords);

            if(type != PathIterator.SEG_LINETO)
                if(type != PathIterator.SEG_MOVETO)
                    continue;
            pts.add(new Vector2D(coords[0], coords[1]));
        }            
        return pts;
    }
    
    public float distanceToLine(Vector2D p0, Vector2D p1) {
        float a = p0.y-p1.y;
        float b = p1.x-p0.x;
        float c = p0.x*p1.y-p1.x*p0.y;
        float poly = (a*this.x+b*this.y+c); 
        return poly*poly/(a*a+b*b);
    }
    public float distanceToLineSqrt(Vector2D p0, Vector2D p1) {return (float)Math.sqrt(distanceToLine(p0, p1));}
    
    //DO NOT EDIT THE FINAL MEMBERS BELOW
    
    public final static Vector2D ZERO = new Vector2D(0, 0);
    public final static Vector2D UNIT_X = new Vector2D(1, 0);
    public final static Vector2D UNIT_Y = new Vector2D(0, 1);
    public final static Vector2D N_UNIT_X = new Vector2D(-1, 0);
    public final static Vector2D N_UNIT_Y = new Vector2D(0, -1);
    public final static Vector2D NEGATIVE = new Vector2D(-1, -1);
    
    public final static Vector2D BOTTOM_LEFT = new Vector2D(0, GameFrame.FRAME_HEIGHT);
    public final static Vector2D BOTTOM_RIGHT = new Vector2D(GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT);
    public final static Vector2D TOP_LEFT = new Vector2D();
    public final static Vector2D TOP_RIGHT = new Vector2D(GameFrame.FRAME_WIDTH, 0);
    
    public final static float PLAYER_LIMIT_LEFT = GameFrame.FRAME_WIDTH/4;
    public final static float PLAYER_LIMIT_RIGHT = 3*GameFrame.FRAME_WIDTH/4;
    
    public final static Vector2D CENTER = new Vector2D(GameFrame.FRAME_WIDTH/2, GameFrame.FRAME_HEIGHT/2);

    public final static DecimalFormat FORMATTER = new DecimalFormat("#0.00");
    
    private static final long serialVersionUID = 6871641568451658655L;
}
