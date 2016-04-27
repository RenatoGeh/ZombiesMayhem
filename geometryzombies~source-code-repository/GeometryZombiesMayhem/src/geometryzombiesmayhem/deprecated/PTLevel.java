/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.deprecated;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Level;
import geometryzombiesmayhem.MutableImage;
import geometryzombiesmayhem.Player;
import geometryzombiesmayhem.Scene;
import geometryzombiesmayhem.SceneLayer;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.Zombie;
import java.awt.Image;

/**
 *
 * @author Yan
 */
public class PTLevel extends Level{
    Scene s;

    @Override
    public void load() {
//        GameFrame.velocity = 0;
//        Player p = new Player("test", new Vector2D(12, 555), 100F);
//        
//        Body.register(p); 
//        Player.registerAsDefault(p);
//        s = new Scene("PTLevel");
//        MutableImage i = new MutableImage(AssetManager.loadRawImage("resources/backgrounds/map_1.png").getScaledInstance(-1, GameFrame.FRAME_HEIGHT, Image.SCALE_SMOOTH));
//        s.addLayer(new SceneLayer("back",i,false,false,Vector2D.TOP_LEFT.copy(),1f));
//        int w = -1;
//        while(w==-1) {
//            w = i.getWidth(null); System.err.println("...");
//        }
//        w = w - GameFrame.FRAME_WIDTH;
//        GameFrame.maxTX = w;
//        GameFrame.velocity = 1;
    }
    
    private boolean b1 = false;

    @Override
    public void update() {
        Player p = Player.getDefaultPlayer();
        Vector2D po = p.getPosition();
        
        if(!b1 && po.getX()>500) {
            b1 = true;
            Vector2D v2 = new Vector2D(800,GameFrame.FRAME_WIDTH - Zombie.DEFAULT_WIDTH);
            Body.register(new Zombie("z",v2.copy(),Zombie.getDefaultSprite(),100));
            Body.register(new Zombie("z",v2.add(50, 0),Zombie.getDefaultSprite(),100));
            Body.register(new Zombie("z",v2.add(150, 0),Zombie.getDefaultSprite(),100));
            Body.register(new Zombie("z",v2.add(100, 0),Zombie.getDefaultSprite(),100));
        }
    }

    @Override
    public Scene getScene() {
        return s;
    }

    @Override
    public void close() {
        
    }

    
    
}
