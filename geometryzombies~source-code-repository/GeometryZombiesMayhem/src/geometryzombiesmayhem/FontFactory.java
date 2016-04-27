package geometryzombiesmayhem;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public final class FontFactory {
    public static Font getFont(String name, int style, int size) {return new Font(name, style, size);}
    public static Font getFont(Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {return new Font(attributes);}
    
    public static Font createFont(String name, int size, int... styles) {
        if(styles.length > 0) {
            Font f = new Font(name, size, styles[0]);
            
            for(int style : styles)
                f = f.deriveFont(style);
            
            return f;
        } else {
            return new Font(name, size, Font.PLAIN);
        }
    }
    
    public static Font ZOMBIE_FONT;
    
    static {
        try {
            ZOMBIE_FONT = Font.createFont(Font.TRUETYPE_FONT, AssetManager.getFile("resources/fonts/youmurdererbb_reg.ttf"));
            ZOMBIE_FONT = ZOMBIE_FONT.deriveFont(30f);
        } catch (FontFormatException | IOException ex) {
            System.err.println("Exception thrown: " + ex.getMessage());
        }
    }
}