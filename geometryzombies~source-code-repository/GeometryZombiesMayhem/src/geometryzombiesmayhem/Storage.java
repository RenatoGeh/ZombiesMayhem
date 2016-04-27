package geometryzombiesmayhem;

import geometryzombiesmayhem.TabbedPane.Tab;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Storage
 * 
 * @author Renato Lui Geh
 */
public class Storage extends Window {
    private Slot[][] slots = {};
    
    public Storage(Vector2D position, Slot[][] slots) {this(position); this.slots = slots;}
    public Storage(Vector2D position) {super(position);};
    
    public static class Inventory extends Storage {
        private Set<ActionListener> listeners = new HashSet<>();
        
        public Inventory(Vector2D position) {this(position, new Slot[][] {});}
        public Inventory(Vector2D position, Slot[][] slots) {
            super(position, slots);
            
            ImageButton im = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/hide_button_hover.png"), 
                    AssetManager.loadImage("resources/interface/radio_player/hide_button.png"), new Vector2D(0, 0), 6);
            im.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.err.println("I liek to lulz!");
                }
            });
//            this.add(im);
//            
//            TextButton tb = new TextButton(new Vector2D(0, 150), 30,
//                    "Hello World!", Color.BLUE, 1);
//            tb.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent event) {
//                    System.err.println("I liek cheezburgerz.");
//                }
//            });
//            this.add(tb);
            
            TabbedPane tablet = new TabbedPane(this, new Vector2D(10, 50), new Vector2D(250, 200));
            
            tablet.register(new Tab(tablet, 25, "Herp"), im);
            tablet.register(new Tab(tablet, 25, "Derp"));
            tablet.register(new Tab(tablet, 25, "Merp"));
            tablet.register(new Tab(tablet, 25, "Meh"));
            
            this.add(tablet);
            
            this.add(new Triggerable() {
                @Override
                public void mouseTriggered(MouseEvent event) {
                    if(event.getID() == MouseEvent.MOUSE_CLICKED)
                        System.err.println("Herp");
                }
                @Override
                public void mouseWheelTriggered(MouseWheelEvent event) {}
                @Override
                public void keyTriggered(KeyEvent event) {}
            });
            
            this.setSize(new Vector2D(300, 400));
        }
    }
}