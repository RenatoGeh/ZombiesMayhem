package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;

public abstract class NPC extends Person implements ArtificialIntelligence {
    private String name = "UnknownNPC";
    private Vector2D labelSize;
    private Color nameColor = Color.WHITE;
    private Font font = ZM.GRAPHICS.getFont();
    private float maxInteractionDistance = 150;
    
    protected DialogTree mainTree;
    protected DialogTree secondaryTree;
    
    public NPC() {super();}
    
    public NPC(String name, Sprite s, float mass, Vector2D position, float health) {
        super(mass, s, position.getX(), position.getY(), health);
        
        this.init(name);
    }
    
    private void init(String name) {
        this.name = name;
        labelSize = new Vector2D(ZM.GRAPHICS.getFontMetrics(font).stringWidth(name), ZM.GRAPHICS.getFontMetrics(font).getHeight());
        ZM.gameFrame.addKeyListener(gAdapter);
        
        this.initNodes();
    }

    public abstract void initMovementPattern();
    public abstract void initNodes();
    public abstract void interact();
    
    @Override
    public void paintThis(Component c, Graphics2D g2) {
        super.paintThis(c, g2);
        
        Color defColor = g2.getColor();
        Font defFont = g2.getFont();
        g2.setFont(font);
        g2.setColor(nameColor);
        g2.drawString(name, this.getPosition().getX() - labelSize.getX()/4, this.getPosition().getY() - labelSize.getY());
        g2.setColor(defColor);
        g2.setFont(defFont);
    }
    
    public void setNameColor(Color c) {this.nameColor = c;}
    public void setNameFont(Font f) {
        this.font = f;
        labelSize.set(ZM.GRAPHICS.getFontMetrics(font).stringWidth(name), ZM.GRAPHICS.getFontMetrics(font).getHeight());
    }
    
    public DialogTree getDialogTree() {return mainTree;}
    
    @Override
    public String getName() {return name;}
    public void setMaxInteractionDistance(float maxInteractionDistance) {this.maxInteractionDistance = maxInteractionDistance;}
    
    private GeneralAdapter gAdapter = new GeneralAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            super.keyReleased(e);
            
            if(getPosition().distanceSqr(Player.getDefaultPlayer().getPosition()) <= maxInteractionDistance*maxInteractionDistance)
                if(GameSettings.Controls.containsKey(e.getKeyCode()))
                    if(GameSettings.Controls.getValue(e.getKeyCode()).equals("interact"))
                        interact();
        }
    };
}