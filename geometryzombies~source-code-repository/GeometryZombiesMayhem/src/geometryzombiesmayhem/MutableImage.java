package geometryzombiesmayhem;

import geometryzombiesmayhem.GameSettings.Graphics.Filter;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * <p>Defines a self-mutable image.</p>
 *
 * <p>This variation of <code>BufferedImage</code> allows safe mutation from and to
 * a reference. This means, whilst allowing this class to mutate, changing its properties,
 * it also allows integrating a completely different image into itself, mirroring that
 * object.</p>
 * 
 * <p>Since this class also implements the <code>Filterable</code> interface, it is also
 * possible for the developer to filter the image, shifting it into a variation of itself.</p>
 * 
 * <p>To prevent total loss of the image's properties when filtering, every object also contains
 * the original image. This allows quickly shifting in between filters without having to load
 * from a resource database. This performance <i>versus</i> memory tradeoff is essential to
 * maintaining a cohesive entanglement between image manipulation functionalities.</p>
 * 
 * <p><b>Important note:</b> it is of extreme importance that the developer only create 
 * instances of this class when needed. Creating an instance also allocates this reference
 * to the <code>MutationManager</code>. Creating new instances precariously and without care
 * would cause significant performance loss.</p>
 * 
 * @author Renato Lui Geh
 */
public class MutableImage extends BufferedImage implements Filterable,Serializable{
    protected Image defImage = new javax.swing.ImageIcon().getImage();
    private boolean registers = false;
    String path = "";
    
    
    private Object writeReplace() throws ObjectStreamException {
         return new MIData(this);
    }
    
    public static MutableImage get(String path) {
        return new MutableImage(AssetManager.loadRawImage(path),path);
    }
    
    private static MutableImage get(MIData m) {
        if(!m.path.isEmpty()) {
            MutableImage i = new MutableImage(AssetManager.loadRawImage(m.path),m.path);
            if(i.getHeight()!=m.h || i.getWidth()!=m.w) {
                i = i.resize(m.w, m.h);
            }
            return i;
        } else {
            return new MutableImage(m.i.getImage(),"");
        }
    }
    
    public MutableImage(int width, int height, int type, boolean registers) {
        super(width, height, type);
        
        if(registers) 
            registerSelf();
    }
    
    public MutableImage(Image im,String path, boolean registers) {
        this(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE, registers);
        this.defImage = new javax.swing.ImageIcon(im).getImage();
        this.path = path;
        
        
        mutate(im);
    }
    
    public MutableImage(int width, int height, int type) {
        this(width, height, type, true);
    }
    
    public MutableImage(Image im,String path) {
        this(im, path, true);
    }
    
    private void registerSelf() {MutationManager.register(this); this.registers = true;}
    
    @Override
    public void filter(Filter f) {
        this.reset();
        
        Graphics2D g2 = this.createGraphics();
        GameSettings.Graphics.apply(g2);
        
        Image im = ((f.equals(Filter.Default))? defImage : AssetManager.filterImage(this.defImage, f));
        im = new javax.swing.ImageIcon(im).getImage();

        g2.drawImage(im, 0, 0, null);
        
        g2.dispose();
    }
    
    public MutableImage resize(int width, int height) {        
        return MutableImage.resizeFromOutside(this, width, height);
    }
    
    public MutableImage resize(float wScale, float hScale) {
        return this.resize((int)(wScale*this.getWidth()), (int)(hScale*this.getHeight()));
    }
    
    public MutableImage rotate(float angle) {
        return this.rotate(angle, new Vector2D(this.getWidth()/2, this.getHeight()/2));
    }
    
    public MutableImage rotate(float angle, Vector2D anchor) {
        return MutableImage.rotateFromOutside(this, anchor, angle);
    }
    
    public Image getImage() {return this.getScaledInstance(this.getWidth(), this.getHeight(), BufferedImage.SCALE_SMOOTH);}
    public BufferedImage getBufferedImage() {return this.getSubimage(0, 0, this.getWidth(), this.getHeight());}
    
    public Image getRawImage() {return this.defImage;}
    public MutableImage getRawMutableImage() {return new MutableImage(defImage,path);}
    
    public final void mutate(Image im) {
        this.reset();
        
        Graphics2D g2 = this.createGraphics();
        GameSettings.Graphics.apply(g2);
        
        g2.drawImage(im, 0, 0, null);
        
        g2.dispose();
    }
    
    public MutableImage copy() {
        MutableImage copy = this.copyRaw();
        MutationManager.register(copy);
        return copy;
    }
    
    public MutableImage copyRaw() {
        return new MutableImage(this.defImage, this.path, this.registers);
    }
    
    protected final void reset() {
        for(int i=0;i<this.getWidth();i++)
            for(int j=0;j<this.getHeight();j++)
                this.setRGB(i, j, 0x00);
    }
    
    private static MutableImage resizeFromOutside(MutableImage im, int width, int height) {
        MutationManager.remove(im);
        Image foo = new javax.swing.ImageIcon(ImageManipulator.resize(im.defImage, width, height)).getImage();
        im = new MutableImage(foo,im.path, im.registers);
        return im;
    }
    
    private static MutableImage rotateFromOutside(MutableImage im, Vector2D anchor, float angle) {
        MutationManager.remove(im);
        Image foo = new javax.swing.ImageIcon(ImageManipulator.rotate(ImageManipulator.convert(im.defImage), angle, anchor)).getImage();
        im = new MutableImage(foo, im.path, im.registers);
        return im;
    }
    
    private static class MIData implements Serializable{
        String path;
        int w,h;
        ImageIcon i;
        
        public MIData(MutableImage i) {
            path = i.path;
            if(path.isEmpty()) {
                if(i.defImage==null) i.defImage = i.getImage();
                this.i = new ImageIcon(i.defImage);
                w = h = 0;
            } else {
                this.i = null;
                w = i.getWidth();
                h = i.getHeight();
            }
        }
        
        private Object readResolve() throws ObjectStreamException {
            return MutableImage.get(this);
        }
        
        private static final long serialVersionUID = 66666661337999999L;
    }
    
    private static final long serialVersionUID = 1337331999912345L;
}