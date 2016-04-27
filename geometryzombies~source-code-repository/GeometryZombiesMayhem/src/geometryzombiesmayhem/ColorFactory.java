package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.color.ColorSpace;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Utility class for fast and easy color picking.
 * 
 * @author Renato Lui Geh
 * @see Color
 */
public final class ColorFactory {
    public static Color getColor(int red, int green, int blue, int alpha) {return new Color(red, green, blue, alpha);}
    public static Color getColor(int red, int green, int blue) {return new Color(red, green, blue);}
    public static Color getColor(Color c, int alpha) {return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);}

    public static Color getColor(float red, float green, float blue, float alpha) {return new Color(red, green, blue, alpha);}
    public static Color getColor(float red, float green, float blue) {return new Color(red, green, blue);}
    public static Color getColor(Color c, float alpha) {return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);}
    
    public static Color getColor(int rgb) {return new Color(rgb);}
    public static Color getColor(int rgba, boolean hasAlpha) {return new Color(rgba, hasAlpha);}
    public static Color getColor(int rgb, int alpha) {return ColorFactory.getColor(new Color(rgb), alpha);}
    
    public static Color getColor(ColorSpace cspace, float[] components, float alpha) {return new Color(cspace, components, alpha);}
    
    public static Color copy(Color color) {return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());}
    
    /**
     * Random Color Generator.
     * 
     * Returns a random color based on the 0-255 Integer or 0.0-1.0 Floater 
     * system of the default <code>Color</code> class in the 
     * JavaSE <code>Color</code> class.
     * 
     * If the given boolean is true, the color outputted will present alpha
     * channel characteristics in the given <code>RGB</code> color.
     * 
     * If the given boolean is false, the color outputted will be solid with
     * no alpha whatsoever.
     * 
     * Keep in mind that if the given Type is not one of the <code>ColorFactory</code>
     * <code>Type</code>, an <code>IllegalArgumentException</code> is thrown.
     * 
     * @param hasAlpha - whether the output color presents alpha channeling or not
     * @return A random <code>Color</code> class.
     * @throws IllegalArgumentException
     * 
     * @see Color
     */
    public static Color getRandomColor(Type e, boolean hasAlpha) {
        Random rand = ZM.RANDOM;
        if(e.equals(Type.Integer))
            return ColorFactory.getColor(rand.nextInt(255 + 1), rand.nextInt(255 + 1), rand.nextInt(255 + 1),
                    ((hasAlpha)? rand.nextInt(255 + 1) : 255));
        else if(e.equals(Type.Floater))
            return ColorFactory.getColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(),
                    ((hasAlpha)? rand.nextFloat() : 1));
        else
            throw new IllegalArgumentException("No such Type!");
    }
    
    /**
     * Random Pre-Defined Color Generator
     * 
     * <p>Returns a random, but pre-defined (see <i>post-scriptum</i> below for more information),
     * color with the standard JavaSE <code>Color</code> class.</p>
     * 
     * <p>This is achieved through the use of constant (a.k.a. <code>final</code>) <code>Color</code>
     * instances from the Java Standard Edition class.</p>
     * 
     * <p>Since the sorting of the colors is accomplished with the use of a <code>HashMap</code>, 
     * performance-wise, this might not be the very best approach when using multi-threading 
     * real-time applications.</p>
     * 
     * <p>Keep in mind that the colors returned <b>DO NOT</b> present any alpha channel. To 
     * set the alpha of a returned color, use one of the methods that present alpha. Example:</p>
     * 
     * <br/><br/>
     * 
     * <code>
     * Color c = ColorFactory.getColor(ColorFactory.getPDRandomColor(), alphaValue);
     * </code>
     * 
     * <br/>
     * 
     * <i><p><b>P.S.:</b> All of the colors are from the <code>Color</code> class, therefore, the 
     * actual amount of colors are based on the constant fields from that class, and may require
     * updating of this class if the <code>Color</code> class' constant fields is modified.</p></i>
     * 
     * @return A pre-defined color with no alpha.
     */
    public static Color getPDRandomColor() {return mapper.get(ZM.RANDOM.nextInt(mapper.size()));}
    
    public static enum Type {Integer, Floater};
    
    private static final Map<Integer, Color> mapper = new HashMap<>();
    static {
        mapper.put(0, Color.BLACK); mapper.put(5, Color.ORANGE); mapper.put(9, Color.PINK);
        mapper.put(1, Color.BLUE); mapper.put(6, Color.CYAN); mapper.put(10, Color.GRAY);
        mapper.put(2, Color.DARK_GRAY); mapper.put(7, Color.GREEN); mapper.put(11, Color.YELLOW);
        mapper.put(3, Color.LIGHT_GRAY); mapper.put(8, Color.WHITE); mapper.put(12, Color.RED);
        mapper.put(4, Color.MAGENTA); 
    }
    
    /**
     * <p>Blends the given colors evenly.</p>
     * 
     * <p>This method uses the <code>Color</code> class to calculate their respective
     * color components. This allows the algorithm to divide the red, green and blue
     * values to a <code>1/n</code> ratio, where <code>n</code> is the number of
     * color, and then add them up, forming a blended color between all of the colors.</p>
     * 
     * @param colors Colors to be blended together.
     * @return A blended color.
     */
    public static Color blend(Color... colors) {
        Color result;
        
        float[][] rgb = new float[colors.length][3];
        
        for(int i=0;i<colors.length;i++)
            rgb[i] = colors[i].getColorComponents(rgb[i]);
        
        float[] comps = {0f, 0f, 0f};
        float ratio = 1.0f/colors.length;

        for(int i=0;i<colors.length;i++) 
            for(int j=0;j<3;j++) 
                comps[j] += rgb[i][j]*ratio;
            
        result = new Color(comps[0], comps[1], comps[2]);
        
        return result;
    }
    
    /**
     *<p>Blends the given colors according to the given ratios.</p>
     * 
     * <p>This method uses the <code>Color</code> class to calculate their respective
     * color components. This allows the algorithm to divide the red, green and blue
     * values to a given ratio regarding each color, and then add them up, 
     * forming a blended color between all of the colors.</p>
     * 
     * <p>Note that if the length of the ratio array does not equal the length of the
     * colors array, an <code>IllegalArgumentException</code> is thrown.</p>
     * 
     * @param colors Colors to be blended together.
     * @param ratios The ratios of the to be blended colors.
     * @return A blended color.
     */
    public static Color blend(Color[] colors, int[] ratios) {
        if(colors.length != ratios.length)
            throw new IllegalArgumentException("Colors must have the same length as their ratios!");
        
        Color result;
        
        float[][] rgb = new float[colors.length][3];
        
        for(int i=0;i<colors.length;i++)
            rgb[i] = colors[i].getColorComponents(rgb[i]);
        
        float[] comps = {0f, 0f, 0f};

        for(int i=0;i<colors.length;i++) 
            for(int j=0;j<3;j++) 
                comps[j] += rgb[i][j]*ratios[i];
            
        result = new Color(comps[0], comps[1], comps[2]);
        
        return result;
    }
    
    /**
     * <p>Creates a gradient color model.</p>
     * 
     * <p>This method is an instance creator for <code>GradientPaint</code>.
     * You can find more information there.</p>
     * 
     * <p>It basically creates a gradient color from a starting color
     * at <code>(x, y)</code> position to an ending color at <code>(w, z)</code>.</p>
     * 
     * <p>As with the cyclic operation, it basically means whether the method should
     * cycle the operation (repeat the gradient) or keep it single.</p>
     * 
     * <p>The <code>GradientPaint</code> class should be used with <code>Graphics</code>'
     * <code>setPaint</code> as the example shows:</p>
     * 
     * <pre><code>
     *      Graphics2D g2 = ClassName.getGraphics();
     *      g2.setPaint(ColorFactory.gradient(0, 0, ColorFactory.getPDRandomColor(), 50, 50, ColorFactory.getPDRandomColor(), true));
     * </pre></code>
     * 
     * @param x0 The starting X-axis position for the starting color.
     * @param y0 The starting Y-axis position for the starting color.
     * @param startColor The starting color.
     * @param x1 The starting X-axis position for the ending color.
     * @param y1 The starting Y-axis position for the ending color.
     * @param endColor The ending color.
     * @param cyclic Whether the gradient should be cycled or not.
     * 
     * @see GradientPaint
     * 
     * @return A <code>GradientPaint</code> with the gradient colors.
     */
    public static GradientPaint gradient(int x0, int y0, Color startColor, int x1, int y1, Color endColor, boolean cyclic) {
        return new GradientPaint(x0, y0, startColor, x1, y1, endColor, cyclic);
    }
    
    /**
     * 
     * <p>Creates a gradient color with the given dimensions.</p>
     * 
     * <p>This method basically is a child of the super method <code>gradient</code>
     * in this very same utility class, with the exception of restricting the user
     * to a set of arguments, making color gradient more user friendly. To create a 
     * single set color gradient variation, input the dimensions of the frame/shape
     * that utilizes this color and a boolean to indicate whether it cycles or not.
     * This will output a single threaded (in the visual sense of the word) gradient
     * color.</p>
     * 
     * @param startColor The starting color.
     * @param endColor The ending color.
     * @param dimX The X-axis dimension.
     * @param dimY The Y-axis dimension.
     * @param cyclic Whether the gradient should be cycled or not.
     * 
     * @see GradientPaint
     * 
     * @return A <code>GradientPaint</code> with the given colors in gradient.
     */
    public static GradientPaint gradient(Color startColor, Color endColor, int dimX, int dimY, boolean cyclic) {
        return ColorFactory.gradient(dimX/2, dimY/2, startColor, dimX, dimY, endColor, cyclic);
    }
    
    /**
     * <p>Creates a gradient color with the given dimensions.</p>
     * 
     * <p>This method basically is a child of the super method <code>gradient</code>
     * in this very same utility class, with the exception of restricting the user
     * to a set of arguments, making color gradient more user friendly. To create a 
     * single set color gradient variation, input the dimensions of the frame/shape
     * that utilizes this color. This will output a single threaded (in the visual 
     * sense of the word) gradient color.</p>
     * 
     * @param startColor The starting color.
     * @param endColor The ending color.
     * @param dimX The X-axis dimension.
     * @param dimY The Y-axis dimension.
     *
     * @see GradientPaint
     * 
     * @return A <code>GradientPaint</code> with the given colors in gradient.
     */
    public static GradientPaint gradient(Color startColor, Color endColor, int dimX, int dimY) {
        return ColorFactory.gradient(startColor, endColor, dimX, dimY);
    }
    
    /**
     * Inverts the given <code>Color</code> <code>RGB<code>'s properties.
     * 
     * @param c <code>Color</code> to be inverted.
     * @return The inverted result of the given <code>Color</code>.
     */
    public static Color invert(Color c) {
        int red = 255 - c.getRed();
        int green = 255 - c.getGreen();
        int blue = 255 - c.getBlue();
        
        return ColorFactory.getColor(red, green, blue, c.getAlpha());
    }
    
    /**
     * Inverts the given <code>Color</code> <code>RGBA<code>'s properties.
     * 
     * @param c <code>Color</code> to be inverted.
     * @return The inverted result of the given <code>Color</code>.
     */
    public static Color invertAlpha(Color c) {
        int red = 255 - c.getRed();
        int green = 255 - c.getGreen();
        int blue = 255 - c.getBlue();
        int alpha = 255 - c.getAlpha();
        
        return ColorFactory.getColor(red, green, blue, alpha);
    }
}