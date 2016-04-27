package geometryzombiesmayhem;

import geometryzombiesmayhem.io.StateManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * <p>Multi-player Menu</p>
 * 
 * <p>
 *  This class is supposed to handle all the functionality
 *  of the Multi-player menu.
 * </p>
 * 
 * <p>
 *  The Multi-player Menu refers to the multi-player menu the user chooses
 *  from the Main menu options.
 * </p>
 * 
 * <p>
 *  The multi-player menu offers connection with other players. For now, it offers
 *  connectivity for up to 2 players. This may change throughout the development.
 * </p>
 * 
 * <p>
 *  From this menu the user can choose the IP to which to connect to.
 *  The user may also choose the port and his/her username.
 * </p>
 * 
 * <p>
 *  Further implementations may include but will not be limited to:
 *      <ul>
 *          <li>Account username and password</li>
 *          <li>Remember me functionality</li>
 *          <li>Access to a friends list (which may also be available from Main menu)</li>
 *          <li>Leaderboard</li>
 *          <li>Add a friend</li>
 *          <li>Invite friend to game</li>
 *          <li>Compare profile</li>
 *          <li>And much more!</li>
 *      </ul>
 * </p>
 * 
 * <p>
 *  The main menu is connected to the following menus and frames:
 * </p>
 * 
 * <ul>
 *  <li> Single-player menu (SingleMenu.java) </li>
 *  <li> Options menu (OptionsMenu.java) </li>
 *  <li> Multi-player menu (MultiMenu.java) </li>
 *  <li> Extras menu (ExtrasMenu.java) </li>
 * </ul>
 * 
 * @see OptionsMenu
 * @see ExtrasMenu
 * @see GameFrame
 * @see MainMenu
 * @see SingleMenu
 * 
 * @author Renato Lui Geh
 * @author Yan
 */
public class MultiMenu extends Menu{    
    private Vector2D panelPosition;
    
    private InputBox ip,user;
    
    public MultiMenu() {
        initPositions();
        initButtons();
        initInputBoxes();
    }

    
    private void initInputBoxes() {
        InputBox ib;
        add(ip = ib = new InputBox(panelPosition.add(50 + 250 + 20, 175 + 50), 17, Font.PLAIN, 30), true);
        ib.setBounds(new Rectangle2D.Float(panelPosition.getX() + 250 + 50,
                panelPosition.getY() + 175, addressField.getWidth(null), addressField.getHeight(null)));
        ib.setTooltip(new Tooltip(Vector2D.CENTER.copy(), "The IP address and the port"
                + " of the host to connect to. The client must write the IP address "
                + "followed by the port separated with \":\". Example: 192.168.0.1:1234 . "
                + "If you wish to connect to a local server, replace the IP address to"
                + " localhost. Example: localhost:6432 .",
                Color.LIGHT_GRAY, 180, 500));
        
        add(user = ib = new InputBox(panelPosition.add(50 + 250 + 20, 300 + 50), 17, Font.PLAIN, 30), true);
        ib.setBounds(new Rectangle2D.Float(panelPosition.getX() + 250 + 50,
                panelPosition.getY() + 300, userField.getWidth(null), userField.getHeight(null)));
        ib.setTooltip(new Tooltip(Vector2D.CENTER.copy(), "The username to connect as."
                + " It has to be shorter than 17 letters and longer than 4 letters."
                + " It may contain special characters as long as they belong to the"
                + " ASCII code table.",
                Color.LIGHT_GRAY, 180, 500));
        
    }
    
    private void initButtons() {    
        add(new PaintableImage(getPanelImage("multi_panel.png"),panelPath("multi_panel.png"),panelPosition.copy()));
        add(new PaintableImage(getLabelImage("title.png"),labelPath("title.png"),panelPosition.add(210,60)));
        add(new PaintableImage(getLabelImage("ip_small.png"),labelPath("ip_small.png"), panelPosition.add(50, 170)));
        add(new PaintableImage(getLabelImage("username_small.png"),labelPath("username_small.png"), panelPosition.add(50, 295)));        
        
        ImageButton ib;
        add(ib = new ImageButton(getButtonImage("back_small.png"), panelPosition.add(30, 450), BUTTON_BACK));
        ib.addActionListener(gA);
        add(ib = new ImageButton(getButtonImage("connect_small.png"), panelPosition.add(480, 450), BUTTON_CONNECT));
        ib.addActionListener(gA);
        
        add(new PaintableImage(userField,"resources/interface/labels/multi/field.png", panelPosition.add(50 + 250, 175)));
        add(new PaintableImage(userField,"resources/interface/labels/multi/field.png", panelPosition.add(50 + 250, 300)));
        }
    
    private void initPositions() {
        panelPosition = new Vector2D(50, 0);  
    }
    
    public GeneralAdapter gA = new GeneralAdapter(){

        @Override
        public void actionPerformed(ActionEvent event) {
            ImageButton ib = (ImageButton) event.getSource();
//            AudioManager.doInterfaceClick(true);
            switch(ib.getID()) {
                case MultiMenu.BUTTON_BACK:
                        ZM.main.changeMenu(MainMenu.getMenu());
                break;
                case MultiMenu.BUTTON_CONNECT:          
                    boolean isCorrect;

                    String host = MultiMenu.getHost();
                    int port = MultiMenu.getPort();
                    String username = MultiMenu.getUsername();

                    if(host == null || port == 0 || username == null) {
                        JOptionPane.showConfirmDialog(null, "Herp Derp. No input was given that day.\n"
                            + "Please fill in the blanks before connecting.", "Okay...",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, new ImageIcon(AssetManager.load("resources/interface/okayguy.jpg")));
                        isCorrect = false;
                    } else
                        isCorrect = true;

                    if(isCorrect) 
                        try {ZM.main.play(true, host, port, username, StateManager.preloadLevel(), false);} catch(MalformedURLException | InterruptedException | AWTException exc) {}

                break;
            }
        }
        
    };
    
    public String parseHost() {
        String s = ip.getText();
        
        if(!s.isEmpty()) {
            s = s.trim();
            s = s.substring(0, s.indexOf(':'));

            return s;
        } else
            return null;
    }
    
    public int parsePort() {
        
        String s = ip.getText();
        
        if(!s.isEmpty()) {
            s = s.trim();
            s = s.substring(s.indexOf(':') + 1, s.length());

            int result = Integer.parseInt(s);

            return result;
        } else {
            return 0;
        }
    }
    
    public String parseUsername() {
        String s = user.getText();
        
        if(s.isEmpty())
            return null;
        else
            return s;
    }
    
    private MutableImage getButtonImage(String filename) {
        return AssetManager.loadImage("resources/interface/buttons/multi/" + filename);
    }
    
    private MutableImage getLabelImage(String filename) {
        return AssetManager.loadImage("resources/interface/labels/multi/" + filename);
    }
    
    private String labelPath(String filename) {
        return "resources/interface/labels/multi/" + filename;
    }
    
    private MutableImage getPanelImage(String filename) {
        return AssetManager.loadImage("resources/interface/panels/" + filename);
    }
    
    private String panelPath(String filename) {
        return "resources/interface/panels/" + filename;
    }
    
    private final MutableImage userField = this.getLabelImage("field.png");
    private final MutableImage addressField = this.getLabelImage("field.png");
    
    public static final int BUTTON_BACK = 2;
    public static final int BUTTON_CONNECT = 3;
    
    private static MultiMenu multi = null;
    
    public static MultiMenu getMenu() {
        if(multi==null) multi = new MultiMenu();
        return multi;
    }
    
    public static String getHost() {return multi.parseHost();}
    public static int getPort() {return multi.parsePort();}
    public static String getUsername() {return multi.parseUsername();}
}
