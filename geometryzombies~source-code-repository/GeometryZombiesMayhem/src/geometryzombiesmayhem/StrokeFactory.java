package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Random;

/**
 * <p>A <code>Stroke</code> Factory.<p>
 * @author Renato Lui Geh
 */
public final class StrokeFactory {
    /**
     * <p>Creates a multiple-layer <code>Stroke</code>.</p>
     * 
     * <p>Each layer of <code>Stroke</code> can have a different style of cap or join.</p>
     *
     * @author Renato Lui Geh
     */
    public static class MultipleStroke implements Stroke {
        private BasicStroke[] strokes;
        
        public MultipleStroke(int n, float[] width, int[] cap, int[] join) {
            if(n*3 != width.length + cap.length + join.length)
                throw new IllegalArgumentException("Arrays do not match estipulated number!");
            
            strokes = new BasicStroke[n];
            
            for(int i=0;i<n;i++)
                strokes[i] = new BasicStroke(width[i], cap[i], join[i]);
        }
        
        public MultipleStroke(int n, float[] width) {
            if(n != width.length)
                throw new IllegalArgumentException("Arrays do not match estipulated number!");
            
            strokes = new BasicStroke[n];
            
            for(int i=0;i<n;i++)
                strokes[i] = new BasicStroke(width[i]);
        }
        
        @Override
        public Shape createStrokedShape(Shape p) {
            Shape outline = strokes[0].createStrokedShape(p);
            
            for(int i=1;i<strokes.length;i++)
                outline = strokes[i].createStrokedShape(outline);
            
            return outline;
        }
        
    }
    
    /**
     * <p>As a child of <code>MultipleStroke</code>, <code>DoubleStroke</code> 
     * only creates two layers of strokes.</p>
     * 
     * @author Renato Lui Geh
     */
    public static class DoubleStroke extends MultipleStroke {
        public DoubleStroke(float w1, float w2, int cap1, int cap2, int join1, int join2) {
            super(2, new float[]{w1, w2}, new int[]{cap1, cap2}, new int[]{join1, join2});
        }
        
        public DoubleStroke(float w1, float w2) {
            super(2, new float[]{w1, w2});
        }
    }
    
    /**
     * <p>By randomly perturbing the edges of a shape, <code>NoiseStroke</code> causes
     * a "noise" effect of a hand-drawn stroke.</p> 
     * 
     * @author Renato Lui Geh
     */
    public static class NoiseStroke implements Stroke {
        private BasicStroke stroke;
        private float noise;
        
        public NoiseStroke(float width, float noise) {
            this.stroke = new BasicStroke(width);
            this.noise = noise;
        }
        
        public NoiseStroke(float width, float noise, int cap, int join) {
            this.stroke = new BasicStroke(width, cap, join);
            this.noise = noise;
        }
        
        @Override
        public Shape createStrokedShape(Shape p) {
            GeneralPath outline = new GeneralPath();
            float[] coords = new float[6];
            
            for(PathIterator it = p.getPathIterator(null);!it.isDone();it.next()) {
                int i = it.currentSegment(coords);
                
                switch(i) {
                    case PathIterator.SEG_MOVETO:
                        this.applyNoise(coords, 2);
                        outline.moveTo(coords[0], coords[1]);
                    break;
                    case PathIterator.SEG_LINETO:
                        this.applyNoise(coords, 2);
                        outline.lineTo(coords[0], coords[1]);
                    break;
                    case PathIterator.SEG_CUBICTO:
                        this.applyNoise(coords, 3);
                        outline.curveTo(coords[0], coords[1], coords[2], coords[3],
                                coords[4], coords[5]);
                    break;
                    case PathIterator.SEG_QUADTO:
                        this.applyNoise(coords, 4);
                        outline.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                    case PathIterator.SEG_CLOSE:
                        outline.closePath();
                    break;
                }
            }
            
            return this.stroke.createStrokedShape(outline);
        }
        
        protected Random getRandomInstance() {return ZM.RANDOM;}
        
        private void applyNoise(float[] coords, int coordType) {
            for(int i=0;i<coordType;i++)
                coords[i] += (this.getRandomInstance().nextBoolean()? 1 : -1)*(this.getRandomInstance().nextFloat() * this.noise);
        }
        
        public static Shape forceNoise(StrokeFactory.NoiseStroke stroke, Shape s) {
            return stroke.createStrokedShape(s);
        }
        
        public static Shape forceNoise(float width, float noise, Shape s) {
            return new StrokeFactory.NoiseStroke(width, noise).createStrokedShape(s);
        }
    }
    
    public static class StaticNoiseStroke extends NoiseStroke {
        private int seed;
        
        public StaticNoiseStroke(float width, float noise) {this(width, noise, ZM.RANDOM.nextInt());}
        public StaticNoiseStroke(float width, float noise, int cap, int join) {this(width, noise, cap, join, ZM.RANDOM.nextInt());}
        public StaticNoiseStroke(float width, float noise, int seed) {
            super(width, noise);
            this.seed = seed;
        }
        public StaticNoiseStroke(float width, float noise, int cap, int join, int seed) {
            super(width, noise, cap, join);
            this.seed = seed;
        }
        
        @Override
        public Shape createStrokedShape(Shape p) {
            ZM.SEEDED_RANDOM.setSeed(this.seed);
            return super.createStrokedShape(p);
        }
        
        @Override
        protected Random getRandomInstance() {
            return ZM.SEEDED_RANDOM;
        }
    }
}
