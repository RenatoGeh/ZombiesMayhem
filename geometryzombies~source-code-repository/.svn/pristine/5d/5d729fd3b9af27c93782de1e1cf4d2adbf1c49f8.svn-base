package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

/**
 * <p>A utility image manipulator class.</p>
 * 
 * <p>Provides several methods that ease image manipulation,
 * reading, writing and converting.</p>
 * 
 * <p>Apart from the usual image manipulation, this class also provides
 * image saving and loading through the use of the <code>ImageIO</code> class.</p>
 * 
 * <p>Most of the methods come in pair, each of them concerning a different
 * image class format. For different permutations of the same method, use
 * the <code>convert</code> method to achieve different results.</p>
 * 
 * <p>This source code is divided into four sections:</p>
 * 
 * <ul>
 *  <li><b>Simple Image Manipulation Section (SIMS)</b>:
 *   <ul>
 *    <li>Converting</li>
 *    <li>Flipping</li>
 *    <li>Resizing</li>
 *    <li>Rotating</li>
 *    <li>Cropping</li>
 *   </ul>
 *  </li>
 *  <li><b>Marking and Unmarking Section (MUS)</b>:
 *   <ul>
 *    <li>Marking</li>
 *    <li>Unmarking</li>
 *   </ul>
 *  </li>
 *  <li><b>Input and Output Section (IOS)</b>:
 *   <ul>
 *    <li>Saving</li>
 *    <li>Loading</li>
 *   </ul>
 *  </li>
 *  <li><b>Advanced Image Manipulation Section (AIMS)</b>:
 *   <ul>
 *    <li>Brightening</li>
 *    <li>Darkening</li>
 *    <li>Blurring</li>
 *    <li>Sharpening</li>
 *   </ul>
 *  </li>
 *  <li><b>Bounds Search Section (BSS)</b>:
 *   <ul>
 *    <li>Get Bounds (in <code>Line2D</code> format)</li>
 *    <li>Bind Bounds</li>
 *   </ul>
 *  </li>
 * </ul>
 * 
 * <p>Each represent a different kind of image manipulation.</p>
 * 
 * <p>Different sub-sections are contained within some of these sections. Inside
 * Simple Image Manipulation, the <code>enum</code> <code>Flip</code> can be found
 * as a variable to be used on that section <i>only</i>. Global variables will be 
 * put outside the sections. Inner classes, such as <code>DataType</code> also fall
 * into these instructions. </p>
 * 
 * @author Renato Lui Geh
 * 
 * @see BufferedImage
 * @see Image
 */
public final class ImageManipulator {
    
    /*
     * Simple Image Manipulation Section (SIMS):
     * 
     * Methods such as resizing, flipping (x and y axis),
     * converting and rotating are located in this section.
     * 
     * The methods come in pair. Every pair concerns a different
     * image format.
     * 
     * Most of these methods can be converted from Image to BufferedImage,
     * or vice-versa, through the use of the convert method, located in this
     * very same section.
     */
    
    /**
     * <p>Converts a <code>BufferedImage</code> into an <code>Image</code>.</p>
     * 
     * <p>Converting is achieved through the use of <code>getScaledInstance</code> with
     * the buffer's width and height as arguments, therefore preserving its dimensions.
     * Since casting is not reliable, I chose not to use such method.</p>
     * 
     * <p>Note that the scaling prioritizes smoothness, sacrificing performance.</p>
     * 
     * <p>The width, height and type of the <code>BufferedImage</code> are maintained.</p>
     * 
     * @param buffer The <code>BufferedImage</code> to be converted.
     * @return The resulting image of the conversion.
     */
    public static Image convert(BufferedImage buffer) {return buffer.getScaledInstance(buffer.getWidth(), buffer.getHeight(), buffer.getType());}
    /**
     * <p>Converts an <code>Image</code> into a <code>BufferedImage</code>.</p>
     * 
     * <p>Converting is achieved through the use of <code>Graphics2D</code>.
     * Since casting is not reliable, I chose not to use such method.</p>
     * 
     * <p>The width and height of the <code>Image</code> are maintained.
     * If the <code>Image</code> had any kind of alpha channel, it will be 
     * maintained.</p>
     * 
     * @param im The to-be-converted image.
     * @return A <code>BufferedImage</code> containing the original <code>Image</code>
     */
    public static BufferedImage convert(Image im) {
        im = new javax.swing.ImageIcon(im).getImage();
        BufferedImage buffer = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE); 
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        graph.drawImage(im, 0, 0, null); 
        graph.dispose();
        return buffer;
    }
    
    /**
     * <p>Resizes the image.</p>
     * 
     * <p>The resizing prioritizes, by default, image smoothness.</p>
     * 
     * @param im The image to be resized.
     * @param width The width of the resulting image.
     * @param height The height of the resulting image.
     * @return A resized version of the given image.
     */
    public static Image resize(Image im, int width, int height) {return im.getScaledInstance(width, height, Image.SCALE_SMOOTH);}
    /**
     * <p>Resizes a <code>BufferedImage</code>.</p>
     * 
     * <p>The resizing is through the use of <code>Graphics2D</code>. 
     * Because of that, the method uses <code>GameSettings</code> to
     * perform a Rendering Hints sweep through the image.</p>
     * 
     * @param e The image to be resized.
     * @param width The width of the resulting image.
     * @param height The height of the resulting image.
     * @return A resized version of the given image.
     */
    public static BufferedImage resize(BufferedImage e, int width, int height) {
        BufferedImage buffer = new BufferedImage(width, height, e.getType());
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        Image im = e.getScaledInstance(width, height, e.getType());
        graph.drawImage(im, 0, 0, null);
        graph.dispose();
        return buffer;
    }
    
    /**
     * <p>Resizes a <code>BufferedImage</code>.</p>
     * 
     * <p>The resizing is through the use of <code>Graphics2D</code>. 
     * Because of that, the method uses <code>GameSettings</code> to
     * perform a Rendering Hints sweep through the image.</p>
     * 
     * <p>This resizing in this method is scaled through <code>width * scale</code>.
     * This means that if a given floater is, for example, a two, the returned instance
     * of the image will be the double size of the given row scale (width and height).</p>
     * 
     * @param e The image to be resized.
     * @param wScale The width's scale.
     * @param hScale The height's scale.
     * @return A scaled <code>BufferedImage</code>.
     */
    public static BufferedImage resize(BufferedImage e, float wScale, float hScale) {return ImageManipulator.resize(e, (int)(e.getWidth() * wScale), (int)(e.getHeight() * hScale));}
           
    /**
     * <p>Resizes an <code>Image</code>.</p>
     * 
     * <p>The resizing is through the use of <code>Graphics2D</code>. 
     * Because of that, the method uses <code>GameSettings</code> to
     * perform a Rendering Hints sweep through the image.</p>
     * 
     * <p>This resizing in this method is scaled through <code>width * scale</code>.
     * This means that if a given floater is, for example, a two, the returned instance
     * of the image will be the double size of the given row scale (width and height).</p>
     * 
     * @param e The image to be resized.
     * @param wScale The width's scale.
     * @param hScale The height's scale.
     * @return A scaled <code>Image</code>.
     */
    public static Image resize(Image im, float wScale, float hScale) {return im.getScaledInstance((int)(im.getWidth(null) * wScale), (int)(im.getHeight(null) * hScale), Image.SCALE_SMOOTH);}
    
    /**
     * <p>Resizes a <code>MutableImage</code>.</p>
     * 
     * <p>This method is to be specifically used when the parameter image is not
     * the same as the receiving end image. That is, it must not be used as a 
     * self-assigning method. When in need for said procedure, please utilize
     * <code>MutableImage</code>'s <code>resize</code> method.</p> 
     * 
     * <p>The resizing is through the use of <code>Graphics2D</code>. 
     * Because of that, the method uses <code>GameSettings</code> to
     * perform a Rendering Hints sweep through the image.</p>
     * 
     * @param e The image to be resized.
     * @param width The width of the resulting image.
     * @param height The height of the resulting image.
     * @return A resized version of the given image.
     */
    public static MutableImage resize(MutableImage im, int width, int height) {
        MutableImage post = new MutableImage(width, height, im.getType(), MutationManager.contains(im));
        Image sub = ImageManipulator.resize(im.getImage(), width, height);
        post.mutate(sub);
        return post;
    }
    /**
     * <p>Resizes a <code>MutableImage</code>.</p>
     * 
     * <p>This method is to be specifically used when the parameter image is not
     * the same as the receiving end image. That is, it must not be used as a 
     * self-assigning method. When in need for said procedure, please utilize
     * <code>MutableImage</code>'s <code>resize</code> method.</p> 
     * 
     * <p>The resizing is through the use of <code>Graphics2D</code>. 
     * Because of that, the method uses <code>GameSettings</code> to
     * perform a Rendering Hints sweep through the image.</p>
     * 
     * <p>This resizing in this method is scaled through <code>width * scale</code>.
     * This means that if a given floater is, for example, a two, the returned instance
     * of the image will be the double size of the given row scale (width and height).</p>
     * 
     * @param e The image to be resized.
     * @param wScale The width's scale.
     * @param hScale The height's scale.
     * @return A scaled <code>MutableImage</code>.
     */
    public static MutableImage resize(MutableImage im, float wScale, float hScale) {return resize(im, (int)(im.getWidth()*wScale), (int)(im.getHeight()*hScale));}
    
    /**
     * <p>Flips a <code>BufferedImage</code> according to the <code>Flip</code> type.
     * 
     * <p>Image flipping is achieved by swapping the <code>x</code> axis with the
     * image <code>width</code> and the <code>y</code> axis with the <code>height</code>.
     * 
     * <p>The <code>Flip</code> type rules what to swap, giving out different results to 
     * each kind of flip.</p>
     * 
     * @param im The image to be flipped.
     * @param f The type of <code>Flip</code>.
     * @return A flipped image.
     */
    public static BufferedImage flip(BufferedImage im, Flip f) {
        Image foo = ImageManipulator.flip(im.getScaledInstance(im.getWidth(), im.getHeight(), BufferedImage.SCALE_SMOOTH), f);
        BufferedImage buffer = new BufferedImage(foo.getWidth(null), foo.getHeight(null), im.getType());
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        graph.drawImage(foo, 0, 0, null);
        graph.dispose();
        return buffer;
    }
    /**
     * <p>Flips an image according to the <code>Flip</code> type.
     * 
     * <p>Image flipping is achieved by swapping the <code>x</code> axis with the
     * image <code>width</code> and the <code>y</code> axis with the <code>height</code>.
     * 
     * <p>The <code>Flip</code> type rules what to swap, giving out different results to 
     * each kind of flip.</p>
     * 
     * @param im The image to be flipped.
     * @param f The type of <code>Flip</code>.
     * @return A flipped image.
     */
    public static Image flip(Image im, Flip f) {
        im = new javax.swing.ImageIcon(im).getImage();
        int width = im.getWidth(null);int height = im.getHeight(null);
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        
        if(f == Flip.HORIZONTAL)
            graph.drawImage(im, 0, 0, width, height, width, 0, 0, height, null);
        else if(f == Flip.VERTICAL)
            graph.drawImage(im, 0, 0, width, height, 0, height, width, 0, null);
        else
            throw new IllegalArgumentException("No such Flip Type!");
        
        graph.dispose();
        
        return buffer.getScaledInstance(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
    }
    
    /**
     * <p>Rotates an image.</p>
     * 
     * <p>Rotation is achieved through <code>Graphics2D</code>'s <code>rotate</code>
     * method. This version of <code>rotate</code>, since no anchor point is given,
     * uses a coordinate of <code>[0, 0]</code> to rotate it.</p>
     * 
     * @param e The image to be rotated.
     * @param angle The angle, in radians, of the rotation operation.
     * @return A rotated image with anchor point of <code>[0, 0]</code>.
     */
    public static BufferedImage rotate(BufferedImage e, float angle) {return ImageManipulator.rotate(e, angle, new Vector2D(0, 0));}
    /**
     * <p>Rotates an image on the anchor point given.</p>
     * 
     * <p>Rotation is achieved through <code>Graphics2D</code>'s <code>rotate</code>
     * method. This version of <code>rotate</code> allows an anchor point argument.
     * The anchor point is where the image will be rotated. For instance, to rotate
     * the image with no translation offset, it is best to rotate it with the anchor
     * point located in the center of the image. Thus: </p>
     * 
     * <p><code>BufferedImage result = ImageManipulator(result, angle, new Vector2D(result.getWidth()/2, result.getHeight()/2));</code></p>
     * 
     * <p>The above example rotates the image from the center <code>angle</code> radians.
     * To rotate an image with degree measures, use <code>Math.toDegrees(angle)</code> to
     * convert the angle.</p>
     * 
     * @param e The image to be rotated.
     * @param angle The angle, in radians.
     * @param anchor The anchor point where the image is to be rotated.
     * @return A rotated image with the given anchor point.
     */
    public static BufferedImage rotate(BufferedImage e, float angle, Vector2D anchor) {
        BufferedImage buffer = new BufferedImage(e.getWidth(), e.getHeight(), e.getType());
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        graph.drawImage(e, null, 0, 0);
        graph.rotate(angle, anchor.getX(), anchor.getY());
        return buffer;
    }
    
    /**
     * <p>Crops a rectangular part of a given <code>BufferedImage</code>.</p>
     * 
     * <p>Cropping is achieved by getting a sub-image of the image with position
     * <code>x</code> and <code>y</code> as initial coordinates and the width
     * and height to establish the final coordinates as in <code>x + width</code> and
     * <code>y + height</code>.</p>
     * 
     * <p>This method returns a cropped counterpart of the original image, with
     * no change to its type or colors.</code>
     * 
     * @param e The <code>BufferedImage</code> to be cropped.
     * @param x The initial <code>x</code> position.
     * @param y The initial <code>y</code> position.
     * @param w The width of the rectangular part of the image.
     * @param h The height of the rectangular part of the image.
     * @return A cropped <code>BufferedImage</code> from its original counterpart.
     */
    public static BufferedImage crop(BufferedImage e, int x, int y, int w, int h) {
        BufferedImage result = new BufferedImage(w, h, e.getType());
        Graphics2D graph = result.createGraphics();
        GameSettings.Graphics.apply(graph);
        graph.drawImage(e.getSubimage(x, y, w, h), null, 0, 0);
        return result;
    }
    /**
     * <p>Crops a rectangular part of a given <code>Image</code>.</p>
     * 
     * <p>Cropping is achieved by getting a sub-image of the image with position
     * <code>x</code> and <code>y</code> as initial coordinates and the width
     * and height to establish the final coordinates as in <code>x + width</code> and
     * <code>y + height</code>.</p>
     * 
     * <p>This method returns a cropped counterpart of the original image, with
     * no change to its type or colors.</p>
     * 
     * <p>This version differs from its sibling method by simply converting the
     * result and source images to <code>Image</code> and <code>BufferedImage</code>
     * respectively.</p>
     * 
     * @param e The <code>Image</code> to be cropped.
     * @param x The initial <code>x</code> position.
     * @param y The initial <code>y</code> position.
     * @param w The width of the rectangular part of the image.
     * @param h The height of the rectangular part of the image.
     * @return A cropped <code>Image</code> from its original counterpart.
     */
    public static Image crop(Image img, int x, int y, int w, int h) {return ImageManipulator.convert(ImageManipulator.crop(ImageManipulator.convert(img), x, y, w, h));}
    
    /**
     * <p>The <code>Flip</code> type.</p>
     * 
     * <p>The flipping method in this class uses this <code>enum</code>.
     * The horizontal element of this <code>enum</code> flips the image
     * horizontally, swapping the <code>x</code> coordinate and the width,
     * causing the image to be rotated horizontally. The vertical element
     * swaps the <code>y</code> axis and the height, rotating it 
     * vertically.</p>
     * 
     * <p>See the <code>flip</code> method for more information.</p>
     */
    public static enum Flip {HORIZONTAL, VERTICAL};
    
    /*
     * Marking and Unmarking Section (MUS):
     * 
     * This section concerns all methods that have to be used by Sprite and Model.
     * They are not to be used if not for their corresponding concerning classes.
     */
    
    /**
     * <p>Marking and Anchor retrieval.</p>
     * 
     * <p>This method is mainly a utility method for <code>Sprite</code>, <code>
     * Member</code> and <code>Model</code>. It retrieves the anchor point of a given
     * image according to the marking embedded in the image. A marking can either be
     * manually embedded or through the method <code>markImage</code> located in this 
     * very same class.</p>
     * 
     * @param s The Sprite to be scanned.
     * @return An anchor point on the coordinates where the marking was.
     */
    public static Vector2D retrieveEmbeddedAnchor(Sprite s) {        
        Vector2D anchor;
        Image im = new javax.swing.ImageIcon(s.getImage(0)).getImage();
        BufferedImage aux = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
        
        Graphics2D graph = aux.createGraphics();
        graph.drawImage(im, 0, 0, null);
        
        for(int i=0;i<aux.getWidth()-1;i++) {
            for(int j=0;j<aux.getHeight()-1;j++) {
                Vector2D foo = ImageManipulator.checkAnchorMark(i, j, aux);
                if(foo != null) {
                    anchor = foo;
                    s.unmarkImage(anchor, 0);
                    return anchor;
                }
            }
        }
        throw new IllegalArgumentException("No marking found!");
    }
    
    /**
     * <p>Checks the anchor marks according to the pre-defined color marks</p>
     * 
     * <p><b>ATTENTION:</b> Local operation only! Do not modify!</p>
     * 
     * @param i The <code>x</code> coordinate.
     * @param j The <code>y</code> coordinate.
     * @param aux The buffered image to be scanned.
     * @return A coordinate representing the anchor point.
     */
    private static Vector2D checkAnchorMark(int i, int j, BufferedImage aux) {
        if(aux.getRGB(i, j) == Color.RED.getRGB())
            if(aux.getRGB(i+1, j) == Color.GREEN.getRGB())
                if(aux.getRGB(i+1, j+1) == Color.BLUE.getRGB())
                    if(aux.getRGB(i, j+1) == 0)
                        return new Vector2D(i, j);
        return null;
    }
    
    /**
     * <p>Marks an image according to the position given.</p>
     * 
     * <p>Marking is achieved through the embedding of a pre-defined mark.
     * In this case, a mark is composed of four elements. At the current
     * state of development, and subject to change, the mark is a:</p>
     * 
     * <ul>
     *  <li>Rectangle inside an image:</li>
     *  <li>Composed of four elements:</li>
     *     <ol>
     *      <li>Red</li>
     *      <li>Green</li>
     *      <li>Blue</li>
     *      <li>0x00 - Transparent</li>
     *     </ol>
     * </ul>
     * 
     * <p>Marking will be embedded into the image, but will only change the local
     * instance of the image, not changing the directory file. To change it 
     * permanently, use this class' <code>save</code> method.</p>
     * 
     * @param im The image to be marked.
     * @param pos The position of the mark.
     * @return A resulting image. A copy of the given image plus the mark.
     */
    public static Image markImage(Image im, Vector2D pos) {return ImageManipulator.baseMarking(im, pos, true, null);}
    /**
     * <p>Unmarks an image according to the position given.</p>
     * 
     * <p>Marking is achieved through the embedding of a pre-defined mark.
     * In this case, a mark is composed of four elements. At the current
     * state of development, and subject to change, the mark is a:</p>
     * 
     * <ul>
     *  <li>Rectangle inside an image:</li>
     *  <li>Composed of four elements:</li>
     *     <ol>
     *      <li>Red</li>
     *      <li>Green</li>
     *      <li>Blue</li>
     *      <li>0x00 - Transparent</li>
     *     </ol>
     * </ul>
     * 
     * <p>Marking will be embedded into the image, but will only change the local
     * instance of the image, not changing the directory file. To change it 
     * permanently, use this class' <code>save</code> method.</p>
     * 
     * <p>Unmarking an image takes out the mark and covers the last with the color
     * of the last pixel before the mark. As with marking, unmarking only changes
     * the local instance of the image.</p>
     * 
     * @param im The image to be unmarked.
     * @param pos The position of the mark.
     * @return A resulting image. A copy of the given image minus the mark.
     */
    public static Image unmarkImage(Image im, Vector2D pos) {return ImageManipulator.baseMarking(im, pos, false, Color.RED.getRGB(), Color.GREEN.getRGB(), Color.BLUE.getRGB(), 0);}
            
    /**
     * <p>Does all the hard work for marking and unmarking.</p>
     * 
     * <p><b>ATTENTION:</b> Local operation only! Do not modify!</p>
     * 
     * @param im The image to be marked/unmarked.
     * @param pos The position of the mark or soon-to-be mark.
     * @param unmark Whether to mark or unmark. <code>true</code> means unmark.
     * @param rgb The colors to mark with.
     * @return A marked/unmarked image.
     */
    private static Image baseMarking(Image im, Vector2D pos, boolean unmark, int... rgb) {
        BufferedImage buffer = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graph = buffer.createGraphics();
        GameSettings.Graphics.apply(graph);
        graph.drawImage(im, 0, 0, null);
        
        if(!unmark) {
            buffer.setRGB((int)pos.getX(), (int)pos.getY(), rgb[0]);
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY(), rgb[1]);
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY() + 1, rgb[2]);
            buffer.setRGB((int)pos.getX(), (int)pos.getY() + 1, rgb[3]);
        } else {
            buffer.setRGB((int)pos.getX(), (int)pos.getY(), buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY(), buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
            buffer.setRGB((int)pos.getX() + 1, (int)pos.getY() + 1, buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
            buffer.setRGB((int)pos.getX(), (int)pos.getY() + 1, buffer.getRGB((int)pos.getX()-1, (int)pos.getY()));
        }
        
        Image result = buffer.getScaledInstance(buffer.getWidth(), buffer.getHeight(), BufferedImage.SCALE_SMOOTH);
        result = new javax.swing.ImageIcon(result).getImage();
        
        return result;
    }    
        
    /*
     * Input and Output Section (IOS):
     * 
     * All I/O methods, loading and saving images to
     * an external directory, are located in this section.
     */
    
    /**
     * <p>Saves the <code>BufferedImage</code> to the given directory.</p>
     * 
     * @param im The image to be saved.
     * @param ref The destination path.
     */
    public static void save(BufferedImage im, String ref) {
        try {
            String format;
            if(ref.endsWith(".png"))
                format = "png";
            else if(ref.endsWith(".jpg"))
                format = "jpg";
            else
                throw new IllegalArgumentException("Unsupported extension found!");
            
            ImageIO.write(im, format, new File(ref));
        } catch(IOException exc) {
            System.err.println("IOException thrown:" + "\nMessage: " + exc.getMessage() + "\nLocalized: " + exc.getLocalizedMessage());
        }
    }
    /**
     * <p>Loads an <code>Image</code> from the given path.</p>
     * 
     * @param path The source path.
     * @return The loaded image.
     */
    public static Image load(String path) {
        Image im = null;
        try {
            im = ImageIO.read(AssetManager.load(path));
        } catch(IOException exc) {
            System.err.println("IOException thrown:" + "\nMessage: " + exc.getMessage() + "\nLocalized: " + exc.getLocalizedMessage());
        }
        return im;
    }
    
    /*
     * Advanced Image Manipulation Section (AIMS):
     * 
     * This section goes from the simple task of brightening up an image to
     * sharpening/blurring techniques.
     * 
     * Since this section is achieved with the use of BufferedImages, paired
     * methods such as in the Simple Image Manipulation Section are non-existant
     * in this section.
     * 
     * Note that most of these operations require high CPU usage, therefore use 
     * them with care at with discretion.
     * 
     * To cause the same results as in the SIMS, use convert on the arguments.
     */
    
    /*
     * Convolution Operations:
     */
    
    /**
     * <p>Brightens a given image.</p>
     * 
     * <p>This method receives a <code>BufferedImage</code> and returns a lighten up
     * version of the same image.</p>
     * 
     * <p>This is achieved through the use of <code>Kernel</code> and </code>ConvolveOp</code>.
     * By receiving a <code>1x1</code> matrix with an element of <code>x + x*n/100</code>, with 
     * <code>x</code> as the default lighting index (normally 1.0) and <code>n</code> as the 
     * percentage of brightening to apply to the image.</p>
     * 
     * <p>Note that any convolution operation is very CPU intensive. Do not try to
     * filter images at paint time. Caching of images is recommended.</p>
     * 
     * @param im The image to be brightened.
     * @param percentage The percentage of brightening.
     * @return A brightened image.
     */
    public static BufferedImage brighten(BufferedImage im, int percentage) {return ImageManipulator.convolve(im, DataType.LUMINOSITY.applyPercentage(percentage));}
    /**
     * <p>Darkens a given image.</p>
     * 
     * <p>This method receives a <code>BufferedImage</code> and returns a darkened
     * version of the same image.</p>
     * 
     * <p>This is achieved through the use of <code>Kernel</code> and </code>ConvolveOp</code>.
     * By receiving a <code>1x1</code> matrix with an element of <code>x - x*n/100</code>, with 
     * <code>x</code> as the default lighting index (normally 1.0) and <code>n</code> as the 
     * percentage of darkening to apply to the image.</p>
     * 
     * <p>If the image has any transparency, the result is, instead of a darkened image,
     * a more transparent image. This is due to the fact that alpha channeled images 
     * have the <code>0x00</code> color number as transparent instead of black. Because
     * of that, the method, if it finds any transparency channeling inside the image,
     * back tracks all transparent pixels, stores them, converts the image to regular
     * RGB color model type, applies the convolution operation, converts it back to
     * RGBA color model type and restores the transparent pixels to their exact previous 
     * position.</p>
     * 
     * <p>Note that any convolution operation is very CPU intensive. Do not try to
     * filter images at paint time. Caching of images is recommended.</p>
     * 
     * @param im The image to be darkened.
     * @param percentage The percentage of darkened.
     * @return A darkened image.
     */
    public static BufferedImage darken(BufferedImage im, int percentage) {
        if(im.getType() == BufferedImage.TYPE_INT_ARGB_PRE) {
            Vector2D[] trans = ImageManipulator.getTransparentPixels(im, 0);
            
            BufferedImage foo = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graph = foo.createGraphics();
            GameSettings.Graphics.apply(graph);

            graph.drawImage(im, null, 0, 0);

            foo = ImageManipulator.convolve(foo, DataType.LUMINOSITY.applyPercentage(-percentage));

            BufferedImage result = new BufferedImage(foo.getWidth(), foo.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graph2 = result.createGraphics();
            GameSettings.Graphics.apply(graph2);
            
            graph2.drawImage(foo, null, 0, 0);
            
            for(int i=0;i<trans.length;i++)
                result.setRGB((int)trans[i].getX(), (int)trans[i].getY(), 0x00);

            return result;
        } else {
            return ImageManipulator.convolve(im, DataType.LUMINOSITY.applyPercentage(-percentage));
        }
    }
    
    public static BufferedImage sharpen(BufferedImage im) {return ImageManipulator.convolve(im, DataType.SHARPEN);}
    public static BufferedImage blur(BufferedImage im) {return ImageManipulator.convolve(im, DataType.BLUR);}
    
    /**
     * <p>A general convolution operation.</p>
     * 
     * <p>This method provides a general use for convolution operations. It allows
     * fully customizable filtering on <code>BufferedImage</code>s.</p>
     * 
     * <p>Convolution operations are achieved through the use of a <code>Kernel</code>,
     * where a matrix of a given order, be it quadratic or not, containing elements
     * that usually sum up to <code>1.0</code> modify a given image, resulting in a
     * different image.</p>
     * 
     * <p>This operation works by the multiplication and addition of colors of pixels
     * around each pixel. This can cause different results. It is preferred that the
     * order of the matrix is an odd number. This is due to the algorithm contained
     * within the <code>ConvolveOp</code> <code>filter</code> method. The use of even
     * numbers, although not forbidden, may cause unwanted results.</p>
     * 
     * <p>Note that any convolution operation is very CPU intensive. Do not try to
     * filter images at paint time. Caching of images is recommended.</p>
     * 
     * @param im The image to be filtered.
     * @param data A matrix containing the data to use as kernel.
     * @param order The order of the matrix.
     * @return A filtered image.
     */
    public static BufferedImage convolve(BufferedImage im, DataType data) {
        Kernel kernel = new Kernel((int)data.getOrder().getX(), (int)data.getOrder().getY(), data.getData());
        ConvolveOp op = new java.awt.image.ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, GameSettings.Graphics.getHints());
        
        BufferedImage buffer = op.filter(im, null);
        
        return buffer;
    } 
    
    /**
     * <p>A DataType concerning all Advanced Image Manipulation operations.</p>
     * 
     * <p>A data type contains a matrix and its order. This is used as kernel
     * for convolution operations.</p>
     * 
     */
    public static class DataType {
        public static DataType SHARPEN = new DataType(3, 3, 
                0.0f, -1.0f, 0.0f,
                -1.0f, 5.0f, -1.0f,
                0.0f, -1.0f, 0.0f);
        public static DataType BLUR = new DataType(3, 3,
            1/9f, 1/9f, 1/9f,
            1/9f, 1/9f, 1/9f,
            1/9f, 1/9f, 1/9f);
        public static DataType LUMINOSITY = new DataType(1, 1,
                1.0f);
        
        private float[] data;
        private int x, y;
        
        private DataType(int x, int y, float... data) {this.x = x; this.y = y; this.data = data;}
        
        public DataType applyPercentage(int percentage) {
            float[] foo = new float[data.length];
            for(int i=0;i<data.length;i++)
                foo[i] = this.applyPercentage(i, percentage);
            return new DataType(x, y, foo);
        }
        public float applyPercentage(int index, int percentage) {return data[index] + data[index]*percentage/100;}
        public float[] getData(){return data;}
        
        /**
         * <p>Creates a <code>DataType</code> for convolution operations.</p>
         * 
         * <p>By taking a parent, pre-defined, <code>DataType</code> instance,
         * the method know what to create. This allows easy permutations of the pre-defined
         * instances of different convolution methods.</p>
         * 
         * <p>To create your own <code>DataType</code> instance for customized convolution
         * operations, see this method's sibling <code>createData</code>.</p>
         * 
         * @param parent The parent <code>DataType</code>.
         * @param order The order, and therefore intensity, of the data matrix.
         * @return A <code>DataType</code> instance containing the matrix data and order.
         */
        public static DataType createData(DataType parent, Vector2D order) {
            int n = ((int)order.getX() * (int)order.getY());
            
            if(parent.equals(BLUR)) {
                float[] result = new float[n];
                for(int i=0;i<result.length;i++)
                    result[i] = 1f/((float)result.length);
                return new DataType((int)order.getX(), (int)order.getY(), result);
            } else if(parent.equals(SHARPEN)) {
                int even = 0;
                float[] result = new float[n];
                for(int i=0;i<result.length;i++) {
                    result[i] = ((i%2==0)? 0f : -1f);
                    even = ((result[i]==-1f)? even+1 : even);
                }
                result[result.length/2] = even + 1;
                return new DataType((int)order.getX(), (int)order.getY(), result);
            } else {
                throw new IllegalArgumentException("No such DataType or unsupported instance!");
            }
        }
        
        /**
         * <p>Creates a <code>DataType</code> with the given order and data matrix.</p>
         * 
         * <p><code>DataType</code>s are used for convolution operations. See <code>
         * convolve</code> for more information.</p>
         * 
         * @param x The <code>x</code> order of the matrix.
         * @param y The <code>y</code> order of the matrix.
         * @param data The data matrix containing the convolution indexes.
         * @return A <code>DataType</code> with the given matrix.
         */
        public static DataType createData(int x, int y, float... data) {return new DataType(x, y, data);}
        
        public Vector2D getOrder() {return new Vector2D(x, y);}
    }
    
    /*
     * Looking up Operations: 
     */
    
    public static BufferedImage lookup(BufferedImage buffer, int offset, short... data) {        
        LookupTable table = new ShortLookupTable(offset, data);
        LookupOp op = new LookupOp(table, GameSettings.Graphics.getHints());
        
        return op.filter(buffer, null);
    }
    
    public static BufferedImage lookup(BufferedImage buffer, int offset, short[] red, short[] green, short[] blue) {
        short[][] data = new short[][] {red, green, blue};
        
        LookupTable table = new ShortLookupTable(offset, data);
        LookupOp op = new LookupOp(table, GameSettings.Graphics.getHints());
        
        return op.filter(buffer, null);
    }
    
    public static BufferedImage lookup(BufferedImage buffer, int offset, short[][] data) {
        LookupTable table = new ShortLookupTable(offset, data);
        LookupOp op = new LookupOp(table, GameSettings.Graphics.getHints());
        
        return op.filter(buffer, null);
    }
    
    public static BufferedImage negative(BufferedImage buffer, int offset) {
        short[] data = new short[256];
        
        for(short i=0;i<256;i++) 
            data[i] = (short)(255 - i);
        
        if(buffer.getType() == BufferedImage.TYPE_INT_ARGB_PRE) {
            Vector2D[] trans = ImageManipulator.getTransparentPixels(buffer, 0);
            
            BufferedImage foo = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graph = foo.createGraphics();
            GameSettings.Graphics.apply(graph);

            graph.drawImage(buffer, null, 0, 0);

            foo = ImageManipulator.lookup(foo, offset, data);

            BufferedImage result = new BufferedImage(foo.getWidth(), foo.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graph2 = result.createGraphics();
            GameSettings.Graphics.apply(graph2);
            
            graph2.drawImage(foo, null, 0, 0);
            
            for(int i=0;i<trans.length;i++)
                result.setRGB((int)trans[i].getX(), (int)trans[i].getY(), 0x00);
            
            return result;
        } else {
            return lookup(buffer, offset, data);
        }
    }
    
    public static BufferedImage invert(BufferedImage buffer, int offset, byte color) {
        short[] red, green, blue = green = red = new short[256];
        short[][] data = new short[][] {red, green, blue};
        
        for(short i=0;i<256;i++) {
            data[color][i] = (short)(255 - i);
            if(color-1 < 0) // color = 0
                data[color+1][i] = data[color+2][i] = i;
            else if(color+1 == 3) //color = 2
                data[color-1][i] = data[color-2][i] = i;
            else //color = 1
                data[color+1][i] = data[color-1][i] = i;
        }
        
        return lookup(buffer, offset, data);
    }
    
    public static final byte RED = 0;
    public static final byte GREEN = 1;
    public static final byte BLUE = 2;
    
    private static Vector2D[] getTransparentPixels(BufferedImage buffer, int sensibility) {
        if(buffer.getType() == BufferedImage.TYPE_4BYTE_ABGR ||
                buffer.getType() == BufferedImage.TYPE_4BYTE_ABGR_PRE || 
                buffer.getType() == BufferedImage.TYPE_INT_ARGB ||
                buffer.getType() == BufferedImage.TYPE_INT_ARGB_PRE) {
            
            Vector2D[] trans; int n = 0;

            for(int i=0;i<buffer.getWidth();i++)
                for(int j=0;j<buffer.getHeight();j++)
                    if(((buffer.getRGB(i, j) >> 24) & 0xff) <= sensibility)
                        n++;
                

            trans = new Vector2D[n]; n = 0;

            for(int i=0;i<buffer.getWidth();i++)
                for(int j=0;j<buffer.getHeight();j++)
                    if(((buffer.getRGB(i, j) >> 24) & 0xff) <= sensibility) {
                        trans[n] = new Vector2D(i, j);
                        n++;
                    }

            return trans;
        } else {
            throw new IllegalArgumentException("Image type does not support transparency!");
        }
    }
    
    /*
     * Custom Manipulation Operations:
     */
    
    /**
     * <p>Returns the arithmetic average of the given values in double.</p>
     * 
     * <p>
     *  This method calculates the arithmetic average of <code>n</code> values
     *  by following the below formula:
     * </p>
     * 
     * <pre><code>
     *                               n
     *      (x0 + x1 + ... + xn)/n = &sum; x(i)
     *                               i=0
     * </pre></code>
     * 
     * @param values Elements to be averaged.
     * @return The arithmetic average of the given values in double.
     */
    public static double avArithmetic(double... values) {
        if(values.length == 0)
            return 0;
        
        double total = 0;
        
        for(double n : values)
            total += n;
        
        return total/(double)values.length;
    }
    
    /**
     * <p>Returns the geometric average of the given values in double.</p>
     * 
     * <p>
     *  This method calculates the geometric average of <code>n</code> values
     *  by following the below formula:
     * </p>
     * 
     * <pre><code>
     *      n                 n  n
     *      &radic;(x0*x1*...*xn) = &prod; &radic;x(i)
     *                        i=0
     * </pre></code>
     * 
     * @param values Elements to be averaged.
     * @return The geometric average of the given values in double.
     */
    public static double avGeometric(double... values) {
        double total = 1;
        
        for(double n : values)
            total *= n;
        
        return Math.pow(total, (1.0/(double)values.length));
    }
    
    /**
     * <p>Returns the harmonic average of the given values in double.</p>
     * 
     * <p>
     *  This method calculates the harmonic average of <code>n</code> values
     *  by following the below formula:
     * </p>
     * 
     * <pre><code>
     *                                        n
     *      n/(1/x0 + 1/x1 + ... + 1/xn) = n/(&sum;1/x(i))
     *                                        i=0
     * </pre></code>
     * 
     * @param values Elements to be averaged.
     * @return The harmonic average of the given values in double.
     */
    public static double avHarmonic(double... values) {
        double total = 0;

        for(double n : values)
            total += 1/n;
        
        return (double)values.length/total;
    }
    
    /**
     * <p>Converts an RGB-colored image into a gray-scaled image.</p>
     * 
     * <p>
     *  This method makes use of both Arithmetic and Harmonic averages according
     *  to the user's choice.
     * </p>
     * 
     * <p>
     *  When using arithmetic averages, the image will have higher RGB values, which
     *  causes both smoothing and more "colorful" images. Harmonic averages in the
     *  other hand causes the color converting to be more granulated and presents
     *  more contrast than arithmetic conversions. This is due to the lower RGB
     *  values in harmonic calculations. As with Geometric averages, RGB values
     *  are not as high as arithmetic averages nor as low as harmonic values. This
     *  gives a mid-term to gray scaling. Pixels do not present much granulation,
     *  but have a good amount of contrast between each pixel colors.
     * </p>
     * 
     * <p>
     *  However, both Geometric and Harmonic averages, due to its high differences
     *  between values when they're either too low or too high, may cause unexpected
     *  behaviors. Since both use multiplication and/or division, the pattern is
     *  not as regular as in arithmetic averages. For images that present either
     *  too much or too less brightness, arithmetic averages are preferable.
     * </p>
     * 
     * <p>
     *  Summarizing:
     * </p>
     * <br/>
     * 
     * <ul>Gray Scale Averaging:
     *  <li><b>Arithmetic average:</b> low granulation, low contrast.</li>
     *  <li><b>Geometric average:</b> medium granulation, medium contrast.</li>
     *  <li><b>Harmonic average:</b> high granulation, high contrast.</li>
     * </ul>
     * 
     * @param buffer The image to be grayed out.
     * @param type The average method to be used.
     * @return The gray-scaled version of the given image.
     */
    public static BufferedImage gray(BufferedImage buffer, GRAYSCALE_AVERAGE type) {
        BufferedImage result = new BufferedImage(buffer.getWidth(), buffer.getHeight(),
                buffer.getType());

        for(int i=0;i<buffer.getWidth();i++) {
            for(int j=0;j<buffer.getHeight();j++) {
                int rgb = buffer.getRGB(i, j);
                
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb >> 0) & 0xFF;
                
                int grayScale = ((type == GRAYSCALE_AVERAGE.GEOMETRIC)? (int)ImageManipulator.avGeometric(red, green, blue) :
                        (type == GRAYSCALE_AVERAGE.ARITHMETIC)? (int)ImageManipulator.avArithmetic(red, green, blue) :
                        (int)ImageManipulator.avHarmonic(red, green, blue));
                
                red = green = blue = grayScale;
                
                int value = ((alpha & 0xFF) << 24 |
                             (red & 0xFF) << 16 |
                             (green & 0xFF) << 8 |
                             (blue & 0xFF) << 0);
                
                result.setRGB(i, j, value);
            }
        }
        
        return result;
    }
    
    public static enum GRAYSCALE_AVERAGE {ARITHMETIC, GEOMETRIC, HARMONIC};
    
//    public BufferedImage colorify(BufferedImage buffer, Color c) {
//        short[] data = new short[] {(short)c.getRed(), (short)c.getGreen(), (short)c.getBlue()};
//        
//        for(short comp : data)
//            for(short i=0;i<comp;i++)
//                comp = (short)(255 - i);
//        
//        short[][] result = new short[][] {data[0], data[1], data[2]};
//        
//        return null;
//    }
    
    /*
     * Bounds Search Section (BSS)
     * 
     * This section refers to bounds "search and return" from a given image to
     * Bounds format.
     */
    
    /**
     * <p>Gets the next non-transparent point.</p>
     * 
     * @param posX The current <code>x-axis</code> position.
     * @param posY The current <code>y-axis</code> position.
     * @param buffer The image to be searched in.
     * @param path The current collection of points.
     * @return The next point available.
     * @deprecated
     */
    private static Vector2D next(int posX, int posY, BufferedImage buffer, ArrayList<Vector2D> path) {
        for(int i=0;i<8;i++) {            
            //  7 0 1       0 -> (0, 1)     1 -> (1, 1)     2 -> (1, 0)
            //  6 P 2       3 -> (1, -1)    4 -> (0, -1)    5 -> (-1, -1)
            //  5 4 3       6 -> (-1, 0)    7 -> (-1, 1)    ----------
            
            if(posX == 0)
                if(i>=5 && i<=7)
                    continue;
            if(posY == 0)
                if(i==0 || i==1 || i== 7)
                    continue;
            
            if(posX == buffer.getWidth()-1)
                if(i>=1 && i<=3)
                    continue;
            if(posY == buffer.getHeight()-1)
                if(i>=3 && i<=5)
                    continue;
            
            int x = ((i>=1 && i<=3)? 1 : (i>=5 && i<=7)? -1 : 0);
            int y = ((i==2 || i==6)? 0 : (i>=3 && i<=5)? 1 : -1);
                        
            Vector2D search = new Vector2D(posX + x, posY + y);
            boolean repeated = false;
            
            for(int j=0;j<path.size();j++)
                if(path.get(j).equals(search))
                    repeated = true;
            
            if(buffer.getRGB((int)search.getX(), (int)search.getY()) == 0 || repeated)
                continue;
            else
                return search;
        }
        return null;
    }
    
    /**
     * <p>Checks the points for lines.</p>
     * 
     * <p>This method checks all the points of the given array for specific
     * points of interests to create lines.</p>
     * 
     * <p>This is achieved through the use of determinants and geometry. To
     * spare the CPU of extra work, the determinant formula is automatically
     * applied to the points, saving extra work the computer would have to
     * perform with matrices.</p>
     * 
     * <p>The matrix's determinant and its consequent formula is as follows:</p>
     * <br/>
     * 
     * <code>
     * <pre>
     * >    | Xa  Ya  1 |            With Xn and Yn as the coordinates of point N.
     * >    | Xb  Yb  1 | = 0        Since a line's three points' determinant must be 0
     * >    | Xc  Yc  1 |            and let the matrix represent the line in question:
     * >    
     * >    By Laplace's determinant calculations, we have:
     * >
     * >    Xc[(Ya.1) - (Yb.1)] - Yc[(Xa.1) - (Xb.1)] + (Xa.Yb) - (Xb.Ya) = 0
     * >    Xc[Ya - Yb] - Yc[Xa - Xb] + Xa.Yb - Xb.Ya = 0
     * >    
     * >    Xc.Ya - Xc.Yb - Yc.Xa + Xb.Yc + Xa.Yb - Xb.Ya = 0
     * >    
     * >    Thus the above equation represents the condition of lineage.
     * </pre>
     * </code>
     * 
     * @param path The <code>ArrayList</code> of <code>Vector2D</code> representing the points
     * of the top-surface.
     * @return An <code>ArrayList</code> of <code>Line2D</code> representing the bounds lines.
     * @deprecated
     */
    private static ArrayList<Line2D> checkLines(ArrayList<Vector2D> path) {
        // det:
        //          |Xa   Ya   1|
        //          |Xb   Yb   1| = 0
        //          |Xc   Yc   1|
        //
        // Xc[(Ya.1) - (Yb.1)] - Yc[(Xa.1) - (Xb.1)] + (Xa.Yb) - (Xb.Ya) = 0
        // Xc[Ya - Yb] - Yc[Xa - Xb] + Xa.Yb - Xb.Ya = 0
        // Xc.Ya - Xc.Yb - Yc.Xa + Xb.Yc + Xa.Yb - Xb.Ya = 0

        long tt = System.nanoTime();
        ArrayList<Line2D> lines = new ArrayList<>();
        
        Vector2D head = null;
        Vector2D tails = new Vector2D(-1, -1);
        
        for(int i=2;i<path.size();i++) {
            int x0 = (int)path.get(i-2).getX();
            int y0 = (int)path.get(i-2).getY();
            
            int x1 = (int)path.get(i-1).getX();
            int y1 = (int)path.get(i-1).getY();
            
            int x2 = (int)path.get(i).getX();
            int y2 = (int)path.get(i).getY();
            
            int det = ((x2*y0) - (x2*y1) - (y2*x0) + (x1*y2) + (x0*y1) - (x1*y0));
            
            // Is a line?
            if(det == 0) {
                if(head == null)
                    head = new Vector2D(x0, y0);
                
                tails.set(x2, y2);
            } else {
                if(head == null)
                    continue;
                else
                    tails.set(x1, y1);
                
                if(head != null && !tails.equals(-1, -1)) {
                    lines.add(new Line2D.Float(head.getX(), head.getY(),
                            tails.getX(), tails.getY()));
                    head.set(x0, y0);
                }
            }
        }
        System.err.println("time " + (((double)System.nanoTime() - tt)/1000000));
        System.err.println(lines.size());
        System.err.println(path.size());
        
        return lines;
    }
    
    private static ArrayList<Line2D> getLines(ArrayList<Point2D> p) {
        long tt = System.nanoTime();
        ArrayList<Line2D> l = new ArrayList<>();
        byte max = 3;
        double cur = 0;
        Line2D t = new Line2D.Float(p.get(0),p.get(1));
        for(int i = 2;i<p.size();i++) {
            cur += (t.ptLineDist(p.get(i))*t.relativeCCW(p.get(i)));
            if(Math.abs(cur) >max) {
                l.add(t);
                t = new Line2D.Float(p.get(i-1), p.get(i));
                cur = 0;
            } else {
                t.setLine(t.getP1(), p.get(i));
                if(i==p.size()-1) {
                    l.add(t);
                }
                
            }
        }
        
        for(int i = 0; i< l.size()-1;i++) {
            Line2D c = l.get(i);
            if(Math.abs(c.getY2()-c.getY1())<=max+1) {
                c.setLine(c.getX1(), c.getY1(), c.getX2(), c.getY1());
                Line2D n = l.get(i+1);
                n.setLine(n.getX1(), c.getY1(), n.getX2(), n.getY2());
            }
        }
        System.err.println("time " + (((double)System.nanoTime() - tt)/1000000));
        System.err.println("points " + p.size());
        System.err.println("lines " + l.size());
        return l;
    }
    
    /**
     * <p>Searches the given image for top-surface segments.</p>
     * 
     * @param buffer Image to be searched for.
     * @return An <code>ArrayList</code> of <code>Vector2D</code> containing all
     * the points of the top-surface segment.
     * @deprecated 
     */
    private static ArrayList<Vector2D> search(BufferedImage buffer) {
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        
        ArrayList<Vector2D> path = new ArrayList<>();
        boolean foundPath = false;
        
        for(int j=0;j<width;j++) {
            for(int i=0;i<height;i++) {
                int posX = j;
                int posY = i;

                if(buffer.getRGB(posX, posY) != 0 && !foundPath) {
                    path.add(new Vector2D(posX, posY));
                } else {
                    Vector2D foo = next(posX, posY, buffer, path);
                    if(foo != null) {
                        path.add(foo);
                        
                        j = (int)((i==height-1)? (foo.getX()-1) : foo.getX());
                        i = (int)(foo.getY()-1);
                        
                        foundPath = true;
                        
                        if(j==width-1)
                            break;
                    }
                }
            }
        }
        return path;
    }
    
    private static ArrayList<Point2D> getPath2(BufferedImage b) {
        ArrayList<Point2D> path = new ArrayList<>();
                
        int w,h;
        w = b.getWidth(); h = b.getHeight();
        int x = 0,y;
        for(y=0;y<h;y++) {
            if(b.getRGB(x, y)!=0) break;
        }
        path.add(new Point2D.Float(x,y));
        for(x++;x<w;x++) {
            try{
            if(b.getRGB(x, y-1)==0) {
                for(int i=0;y+i<h;i++) {
                    if(b.getRGB(x,y+i)!=0) {
                        y+=i;
                        break;
                    }
                }
            }
            else {
                for(int i = -2;y+i>0;i--) {
                    if(b.getRGB(x,y+i)==0) {
                        y = y + i + 1;
                        break;                    
                    }
                }
            }
            path.add(new Point2D.Float(x,y));
            }catch(Exception e) {
                System.out.println(x + " " + y); System.exit(1);
            }
        }
        return path;
    }
    
    @Deprecated
    private static ArrayList<Vector2D> getPath(BufferedImage b) {
        ArrayList<Vector2D> path = new ArrayList<>();
                
        int w,h;
        w = b.getWidth(); h = b.getHeight();
        int x = 0,y;
        for(y=0;y<h;y++) {
            if(b.getRGB(x, y)!=0) break;
        }
        path.add(new Vector2D(x,y));
        for(x++;x<w;x++) {
            if(b.getRGB(x, y-1)==0) {
                for(int i=0;y+i<h;i++) {
                    if(b.getRGB(x,y+i)!=0) {
                        y+=i;
                        break;
                    }
                }
            }
            else {
                for(int i = -2;y+i>0;i--) {
                    if(b.getRGB(x,y+i)==0) {
                        y = y + i + 1;
                        break;                    
                    }
                }
            }
            path.add(new Vector2D(x,y));
        }
        return path;
    }
    
    /**
     * <p>Cleans the path array from duplicates and sorts it.</p>
     * 
     * <p><b>Pre-note:</b> This method is only to be used when trying to acquire
     * an integral format of bounds from an image. That means, differently from
     * the usual search path algorithm used in this class, the search method for
     * the use of this function should be a full-point search, meaning that all the
     * non-transparent areas of an image should be considered relevant. The usual
     * search should only acquire the top-surface segment from an image.</p>
     * 
     * <p><b>Duplicates deletion:</b> 
     * Deleting duplicates is achieved through the individual cleansing of
     * each element in a given path array of <code>Vector2D</code>. That means
     * if an element that is already contained within the given path, it will 
     * not be considered and, therefore, will be discarded.</p>
     * 
     * <p><b>Sorting criteria:</b>
     * Sorting is used to avoid the wrongly formation of lines when using the
     * method <code>checkLines</code>. Every point should have an abstract
     * index (abstract meaning imaginary) pointing whether the position comes
     * first of after the next point. To avoid excessive field creation (as well
     * as additional implementation at earlier stages of search path creation), 
     * the criteria used is whether the <code>x-axis</code> of a given position
     * is before or after the next one. If the <code>x-axis</code> is equal to
     * the next position, <code>y-axis<code> is taken into account, following
     * the same conditions.</p>
     * 
     * <p>Note that this method is evidently not focused on effectiveness. 
     * I strongly advise not using this array cleansing method anywhere other
     * than initial initialization, given the fact that this function uses
     * several class creation and field initialization.</p>
     * 
     * <p>As yet another sidenote, the class <code>PathVector</code>, present
     * within this method has the same function as <code>Vector2D</code> with
     * the addition of the implementation of the <code>Comparable</code> interface.
     * This is necessary for the sorting criteria, since <code>Vector2D</code> may
     * not necessarily present the same sorting criteria as <code>Vector2D</code>.</p>
     * 
     * @param path The <code>ArrayList</code> containing <code>Vector2D</code> as path.
     * @return A cleaned <code>ArrayList</code> of <code>Vector2D</code> derived from the given array.
     */
    private static ArrayList<Vector2D> cleanArray(ArrayList<Vector2D> path) {
        ArrayList<Vector2D> temp = new ArrayList<>();
        
        // Delete duplicates
        for(int i=0;i<path.size();i++) {
            if(i==0)
                temp.add(path.get(i));
            else 
                for(int j=0;j<temp.size();j++) {
                    if(temp.get(j).equals(path.get(i)))
                        break;
                    if(j==temp.size()-1)
                        temp.add(path.get(i));
                }
        }
        
        // Sort out path (criteria: X axis -> 0 to n, with n>0)
        class PathVector implements Comparable {
            int x, y;
            
            PathVector(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public int compareTo(Object o) {
                if(o instanceof PathVector) {
                    PathVector copy = (PathVector)o;
                    return ((this.x > copy.x)? 1 : (this.x == copy.x)? 0 : -1);
                } else {
                    throw new IllegalArgumentException("Given object is not supported!");
                }
            }
        }
        
        ArrayList<PathVector> sort = new ArrayList<>();
        
        for(int i=0;i<temp.size();i++)
            sort.add(new PathVector((int)temp.get(i).getX(), (int)temp.get(i).getY()));
        
        Collections.sort(sort);
        
        ArrayList<Vector2D> result = new ArrayList<>();
        
        for(int i=0;i<sort.size();i++) 
            result.add(new Vector2D(sort.get(i).x, sort.get(i).y));
        
        for(int i=0;i<result.size();i++)
            System.err.println(result.get(i));
        
        return result;
    }
    
    /**
     * <p>Retrieves an image's top-surface segment as an array of <code>Line2D</code>.</p>
     * 
     * <p>By tracking down all the possible lines from a top-surface segment,
     * this algorithm discards transparent or any below the segment's line areas,
     * returning an <code>ArrayList</code> of Line2D representing the top-surface's
     * lines that form the segment.</p>
     * 
     * <p>Note that this method is extremely CPU intensive due to the fact of having
     * to check each and every pixel contained within the image. This method is to be
     * used on small sized images that should not contain much curves or high counts
     * of edges.</p>
     * 
     * <p>If the above conditions are not met, the user may struggle with the loading
     * time and performance. In extreme conditions, the Java Virtual Machine (JVM) may
     * even run out of space, throwing an <code>java.lang.OutOfMemoryError</code>, due
     * to lack of Java heap space memory.</p>
     * 
     * @param buffer The image to retrieve the bounds.
     * @return An array of <code>Line2D</code>s representing the top-surface segment.
     */
    public static ArrayList<Line2D> getBounds(BufferedImage buffer) {
        return ImageManipulator.getLines(ImageManipulator.getPath2(buffer));
    }
    
    public static ArrayList<Bound> getSimpleBounds(BufferedImage buffer, boolean up) {
        ArrayList<Bound> e = new ArrayList<>();
        ArrayList<Line2D> base = ImageManipulator.getBounds(buffer);
        
        for(Line2D line : base)
            e.add(new Bound.Simple(line, null, up));
        
        return e;
    }
    
    /**
     * <p>Binds the <code>Line2D</code> bounds by drawing them onto a new image.</p>
     * 
     * <p>The image must meet the conditions from <code>getBounds</code>. Please
     * check its Javadoc for conditions and more information regarding bound search.</p>
     * 
     * @param buffer <code>BufferedImage</code> to be binded onto.
     * @return A binded image.
     */
    public static BufferedImage bindBounds(BufferedImage buffer) {
        BufferedImage foo = new BufferedImage(buffer.getWidth(), buffer.getHeight(),
                BufferedImage.TYPE_INT_ARGB_PRE);
        
        Graphics2D g2 = foo.createGraphics();
        GameSettings.Graphics.apply(g2);
        
        g2.setColor(Color.RED);
        
        for(Line2D line : ImageManipulator.getBounds(buffer))
            g2.draw(line);
        
        g2.dispose();
        
        return foo;
    }
}