/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.*;
import java.io.*;


/**
 *
 * @author Yan
 */
public class BaseLevel extends Level implements Serializable{

    @Override
    public void load() {
        MutableImage landBack = AssetManager.loadImage("resources/levels/scene/main.png");
        MutableImage rubbleFore = AssetManager.loadImage("resources/levels/scene/foreground_rubble.png");
        MutableImage skyBack = AssetManager.loadImage("resources/levels/scene/sky_city_burn_big.png");
        
        Vector2D basePosition = new Vector2D(0, -(landBack.getHeight()-GameSettings.Graphics.getResolution().getY()+30));

        Scene mainScene = new Scene("Main Scene");
        mainScene.addLayer(new SceneLayer("Sky", skyBack, false, false, basePosition, .4f));
        mainScene.addForegroundLayer(new SceneLayer("Rubble", rubbleFore, false, false, basePosition, 1f));
        mainScene.addLayer(new SceneLayer("Land", landBack, false, false, basePosition, 1f));
        this.setScene(mainScene);
        
        Player p = new Player("test", new Vector2D(12, 555), 10F);
        
        this.setName("Test");
        
        Body.register(p); 
        Player.registerAsDefault(p);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void close() {
        
    }
    
    private static final long serialVersionUID = -19873941239812L;
}
