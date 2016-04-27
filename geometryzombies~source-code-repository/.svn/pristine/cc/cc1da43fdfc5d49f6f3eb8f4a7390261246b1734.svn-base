package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Yan
 */
public class TestLevel extends Level{
    Dialog d,d2;
    Bound.Simple line;
    Bound.Simple line2;
    Bound.SimpleMove line3;
    Bound.SimpleMove line4;
    WarpBox w;
    WarpBox w2;

    @Override
    public void load() {
        GameFrame.velocity = 0;
        Player p = new Player("test", new Vector2D(12, 555), 100F);
        
        Body.register(p); 
        Player.registerAsDefault(p);
        
        d = Dialog.getDialog(Dialog.Predefined.TARMA,"I got tired of the previous text, and now you're trapped muahaha");
        
        line2 = new Bound.Simple(new Line2D.Double(500,500,1000,210), null, true);
        line3 = new Bound.SimpleMove(new Line2D.Double(1000,300,1100,300), null, 1000, 500, 5, TimeUnit.SECONDS);
        line4 = new Bound.SimpleMove(new Line2D.Double(1100, 200, 1300, 200), null, 1500, 200, 3, TimeUnit.SECONDS);
        w = new WarpBox(new RotatableRectangle(500, 0, 500, GameFrame.FRAME_HEIGHT,0),0,3);
        w2 = new WarpBox(new RotatableRectangle(1200, 0, 500, GameFrame.FRAME_HEIGHT,30),.5f);
        
        Body.register(w);
        Body.register(w2);
        w2.activate();
        w.activate();
        Body.register(line2);
        
        Body.register(line3);
        Body.register(line4);
        Dialog.register(d); 
        
        final String[] options = {"A health pack.", "An ammo pack.", "Your life. LOL."};
        final Runnable[] runs = {new Runnable() {
            @Override
            public void run() {
                Item.register(Item.createHealthItem(Player.getDefaultPlayer().getPosition().subtract(0, 250)));
            }
        }, new Runnable() {
            @Override
            public void run() {
                Item.register(Item.createAmmoItem(Player.getDefaultPlayer().getPosition().subtract(0, 250), ProjectileType.BULLET));
            }
        }, new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<5;i++)
                    Body.register(new Crawler("Zombie_" + Zombie.getList().size(), Player.getDefaultPlayer().getPosition().subtract(ZM.RANDOM.nextInt(50), 450), Crawler.getDefaultIdleSprite(), 100f));
            }
        }};
        final String intro = "Ahoy, freshwater scallywags!\nYarr be eyein' for treasurrres?! "
                + "Dis be a piece o' map ye got'derr? Yarr got a treasurr map derr? "
                + "Nay, nay, dun' be shy. Le'me have an eye on dis treasurr derr, aye?. "
                + "Dis be a nice piece o' paperr. Aye, I'd be tradin' dis if yerr willin'!\n"
                + "Arr, wha' would yerr be exchangin' dis map ferr?";

        Body.register(new NPC("Pirate Flabbyguns", Zombie.getDefaultSprite(), 100f, new Vector2D(500, 200), 100) {
            
            @Override
            public void initMovementPattern() {
                AwesomeTimer.addAction(new AwesomeAction() {
                    @Override
                    public synchronized void run() {
                        int n = ZM.RANDOM.nextInt(3)-1;
                        Vx = n;
                    }
                }, 2, true, TimeUnit.SECONDS);
                AIManager.register(this);
            }
            
            @Override
            public void initNodes() {
                this.mainTree = new DialogTree(new InteractibleDialog(AssetManager.loadImage("resources/face_pirate.png"), intro, Color.BLACK, 80, 
                    new Font(Font.DIALOG_INPUT, Font.PLAIN, 20), true, options, runs));
                this.mainTree.add(
                        new Dialog(AssetManager.loadImage("resources/face_pirate.png"), "Aye. Darr'd be a good deal! Herr it be, young lad! Take me olde blue pills (a.k.a. Viagra). Cya later, shitlords!",
                            Color.BLACK,80,new Font(Font.DIALOG_INPUT, Font.PLAIN, 20)),
                        new Dialog(AssetManager.loadImage("resources/face_pirate.png"), "Aye. Darr'd be a good deal! Herr it be, young lad! Take me olde silverr teeth, therr be quite good for the ol' shootin'. Cya later, shitlords!",
                            Color.BLACK,80,new Font(Font.DIALOG_INPUT, Font.PLAIN, 20)),
                        new Dialog(AssetManager.loadImage("resources/face_pirate.png"), "Yarr be a joker, arr? Well, yarr be in trouble now, freshwater scallywag. It's raining Zombies, Hallelujah! Cya later, shitlords!",
                            Color.BLACK,80,new Font(Font.DIALOG_INPUT, Font.PLAIN, 20)));
            
                this.secondaryTree = new DialogTree(new Dialog(AssetManager.loadImage("resources/face_pirate.png"), "Cya later, shitlords!",
                        Color.BLACK,80,new Font(Font.DIALOG_INPUT, Font.PLAIN, 20)));
                this.secondaryTree.add(new Dialog(AssetManager.loadImage("resources/face_pirate.png"), "Let x=LO, prod(a, b, c) a product of 'a' times itself from 'b' to 'c' and h->infinite: f(x)=TRO*prod(x, 0, h). Derive that, bitch.",
                        Color.BLACK,80,new Font(Font.DIALOG_INPUT, Font.PLAIN, 20)));
                this.secondaryTree.setRepeats(true);
            }
            
            @Override
            public void interact() {
                if(mainTree.isActive())
                    DialogTree.checkTrees();

                if(!mainTree.wasUsed())
                    this.mainTree.enable();
                else
                    this.secondaryTree.enable();

                DialogTree.checkTrees();
            }

            @Override
            protected boolean manageCollision(Body b) {return false;}

            @Override
            public void handleMovement() {}
            @Override
            public void handleAttack() {}
            @Override
            public void handleIdling() {}
            @Override
            public boolean checkMovementsRange() {return true;}
            @Override
            public boolean checkWeaponsRange() {return false;}
            @Override
            public void attack() {}
            @Override
            public void handleBefore() {}

            @Override
            public Body copy() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            
            
        });
        
        ArrayList<Line2D> lines = ImageManipulator.getBounds(AssetManager.loadImage("resources/backgrounds/test_level.png"));
        
        for(Line2D bounds : lines) {
            bounds.setLine(bounds.getX1(), bounds.getY1() + (260+80), bounds.getX2(), bounds.getY2() + (260+80));
            Body.register(new Bound.Simple(bounds, null, true));
        }
        
        MutableImage landBack = AssetManager.loadImage("resources/backgrounds/test_level.png");
        MutableImage skyBack = AssetManager.loadImage("resources/backgrounds/sky_city_burn_big.png");

        Dialog.start();
        
        Scene mainScene = new Scene("Main Scene");
        mainScene.addLayer(new SceneLayer("Sky", skyBack, true, false, Vector2D.TOP_LEFT.copy(), .4f));
        mainScene.addLayer(new SceneLayer("Land", landBack, true, false, Vector2D.BOTTOM_LEFT.subtract(0, 260), 1f, true));
        this.setScene(mainScene);
        
        GameFrame.velocity = 1;
    }

    @Override
    public void update() {
        if(line == null && Player.getDefaultPlayer().getPosition().getX()>500) {
            line = new Bound.Simple(new Line2D.Double(250,500,500,500), null, true);
            Body.register(line);
        }
        if(d2 == null && Player.getDefaultPlayer().getPosition().getX()>1300) {
            d2 = Dialog.getDialog(Dialog.Predefined.TARMA,"Oh I see you've walked a lot.");
            Dialog.register(d2);
        }
    }

    @Override
    public void close() {
        if(line!=null) line.destroy();
        line2.destroy();
        line3.destroy();
        line4.destroy();
        w.destroy();
        w2.destroy();
    }
}
