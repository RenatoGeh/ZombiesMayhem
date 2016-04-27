package geometryzombiesmayhem;

import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;

/**
 *
 * <p>A Rotatable Rectangle.</p>
 *
 * @author Renato Lui Geh
 * @author Yan Soares Couto
 */
public class RotatableRectangle implements Paintable, Shape, java.io.Serializable {

    private Line2D[] sides;
    private boolean isActive = true;
    protected float w, h, angle;
    private float maxX, maxY, minX, minY;
    private Polygon poly;

    public RotatableRectangle(float x, float y, float w, float h, float angle) {
        sides = new Line2D[4];

        init(x, y, w, h, angle);
    }

    public RotatableRectangle(Rectangle2D rect, float angle) {
        this((float) rect.getX(), (float) rect.getY(),
                (float) rect.getWidth(), (float) rect.getHeight(), angle);
    }
    
    private RotatableRectangle(Line2D[] s, float w,float h,float angle,float maxX,float maxY,float minX,float minY,Polygon p) {
        sides = new Line2D[4];
        for(int i = 0;i<4;i++)
            sides[i] = s[i];
        this.w = w; this.h = h; this.angle = angle; this.maxX = maxX; this.minX = minX; this.minY = minY;
        this.poly = new Polygon();
        poly.npoints = 4;
        poly.xpoints = Arrays.copyOf(p.xpoints, 4);
        poly.ypoints = Arrays.copyOf(p.ypoints, 4);
       
    }
    
   
    
    public RotatableRectangle copy() {
        return new RotatableRectangle(sides,w,h,angle,maxX,maxY,minX,minY,poly);
    }

    private void init(float x, float y, float w, float h, float angle) {
        this.w = w;
        this.h = h;
        this.angle = angle;

        maxX = maxY = Integer.MIN_VALUE;
        minX = minY = Integer.MAX_VALUE;

        int[] xrect = new int[4];
        int[] yrect = new int[4];

        xrect[0] = (int) x;
        yrect[0] = (int) y;
        angle = (float) Math.toRadians(90 - angle);

        float xl = (float) (w * Math.sin(angle));
        float yl = (float) (w * Math.cos(angle));

        xrect[1] = (int) (x + xl);
        yrect[1] = (int) (y - yl);

        float xll = (float) (h * Math.cos(angle));
        float yll = (float) (h * Math.sin(angle));

        xrect[2] = (int) (x + xl + xll);
        yrect[2] = (int) (y - yl + yll);
        xrect[3] = (int) (x + xll);
        yrect[3] = (int) (y + yll);

        for (int i = 0; i < 3; i++) {
            if (maxX < xrect[i]) {
                maxX = xrect[i];
            }
            if (maxY < yrect[i]) {
                maxY = yrect[i];
            }
            if (minX > xrect[i]) {
                minX = xrect[i];
            }
            if (minY > yrect[i]) {
                minY = yrect[i];
            }
            sides[i] = new Line2D.Float(xrect[i], yrect[i], xrect[i + 1], yrect[i + 1]);
        }

        if (maxX < xrect[3]) {
            maxX = xrect[3];
        }
        if (maxY < yrect[3]) {
            maxY = yrect[3];
        }
        if (minX > xrect[3]) {
            minX = xrect[3];
        }
        if (minY > yrect[3]) {
            minY = yrect[3];
        }
        sides[3] = new Line2D.Float(xrect[3], yrect[3], xrect[0], yrect[0]);

        poly = new Polygon(xrect, yrect, 4);
    }

    public boolean intersects(RotatableRectangle rect) {
        for (int i = 0; i < this.sides.length; i++) {
            for (int j = 0; j < rect.getSides().length; j++) {
                if (this.sides[i].intersectsLine(rect.sides[j])) {
                    return true;
                }
            }
        }

        return poly.contains(rect.sides[0].getP1()) || rect.poly.contains(sides[0].getP1());
        //checks if there a point of this rectangle inside the other one or vice-versa
        //maybe there's a more efficient way
    }

    public boolean intersects(Line2D line) {
        for (int i = 0; i < this.sides.length; i++) {
            if (this.sides[i].intersectsLine(line)) {
                return true;
            }
        }

        return poly.contains(line.getP1());
    }

    public Line2D[] getSides() {
        return this.sides;
    }

    public void setAngle(float angle) {
        init((float) sides[0].getX1(), (float) sides[0].getY1(), w, h, angle);
    }

    public void set(float x, float y, float w, float h, float angle) {
        init(x, y, w, h, angle);
    }

    public void setPos(float x, float y) {
        double dx = x - sides[0].getX1();
        double dy = y - sides[0].getY1();
        if (dx < 1 && dx > -1 && dy < 1 && dy > -1) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            sides[i].setLine(sides[i].getX1() + dx, sides[i].getY1() + dy, sides[i].getX2() + dx, sides[i].getY2() + dy);
        }
        poly = createPoly();
    }

    public float getX() {
        return (float) sides[0].getX1();
    }

    public float getY() {
        return (float) sides[0].getY1();
    }

    public float getMinY() {
        return minY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMaxX() {
        return maxX;
    }

    public Polygon createPoly() {
        int[] x = new int[4], y = new int[4];

        for (int i = 0; i < 4; i++) {
            x[i] = (int) sides[i].getX1();
            y[i] = (int) sides[i].getY1();
        }

        return new Polygon(x, y, 4);
    }

    @Override
    public Rectangle2D getBounds2D() {
        float x = (float) sides[0].getX1();
        float y = (float) sides[0].getY1();

        float width = (float) Math.abs((sides[1].getX1() - sides[0].getX1()));
        float height = (float) Math.abs((sides[1].getY1() - sides[0].getY1()));

        return new Rectangle2D.Float(x, y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        int x = (int) sides[0].getX1();
        int y = (int) sides[0].getY1();

        int width = (int) Math.abs((sides[1].getX1() - sides[0].getX1()));
        int height = (int) Math.abs((sides[1].getY1() - sides[0].getY1()));

        return new Rectangle(x, y, width, height);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
        for (int i = 0; i < sides.length; i++) {
            g2.draw(sides[i]);
        }
    }

    @Override
    public boolean contains(double x, double y) {
        return poly.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return poly.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return poly.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return poly.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return poly.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return poly.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return poly.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return poly.getPathIterator(at, flatness);
    }
    
    private static final long serialVersionUID = -78684332168451L;

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}
