package geometryzombiesmayhem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * Awesome Timer does awesome stuff. Just don't send any actions that take a long long time and you'll be fine
 * @author Yan
 */
public class AwesomeTimer {
   private static final AwesomeTimer main = new AwesomeTimer();
    
   private final List<UpdateAction> ua = new ArrayList<>();
   private final List<AwesomeAction> aa = new ArrayList<>();
  
    
    private final Thread mainThread = new Thread("Awesome Thread") {

        @Override
        public void run() {
            
            while(true) {
                try {
                    synchronized(aa) {
                        for(ListIterator<AwesomeAction> i = aa.listIterator();i.hasNext();) {
                            AwesomeAction a = i.next();
                            if(a==null) continue;
                            long aux2 = a.timeToSet;
                            
                            if(a.timeAffected) {
                                float mult = 1;
                                if(a.timeBody!= null) {
                                    mult = a.timeBody.getWarpVelocity();
                                }
                                mult*=GameFrame.velocity;
                                aux2 /= Math.abs(mult);
                                if(Math.abs(mult) < .0000001f) continue;
                                a.onReverse = mult < 0;
                            }
                            if(System.nanoTime() - a.timeAdded > aux2) {
                                if(a.repeats) a.timeAdded = a.timeAdded + aux2;
                                a.run();
                                if(!a.repeats) i.remove();
                            }
                        }
                    }
                    synchronized(ua) {
                        for(UpdateAction a : ua) {
                            a.times--;
                            if(a.times>0) continue;
                            a.times = a.timesMax;
                            long aux;
                            a.update((aux = System.nanoTime()) - a.lastTime);
                            a.lastTime = aux;
                        }
                    }
            }catch(Exception e) { e.fillInStackTrace();}
            }
        }
        
    };
    
    {
        mainThread.setPriority(Thread.MAX_PRIORITY);
        mainThread.start();
    }
    
    public void add(AwesomeAction action, long nanoTime,boolean repeat) {
        action.timeAdded = System.nanoTime(); action.timeToSet = nanoTime;action.repeats = repeat;
        synchronized(aa) {
            aa.add(action);
        }
    }
    
    public void add(AwesomeAction action, long time,boolean repeat, TimeUnit tu) {
        action.timeAdded = System.nanoTime(); action.timeToSet = tu.toNanos(time);action.repeats = repeat;
        synchronized(aa) {
            aa.add(action);
        }
    }
    
    public void add(UpdateAction action) {
        add(action,1);
    }
    
    public void add(UpdateAction action,int times) {
        action.times = action.timesMax = (byte) times;
        action.lastTime = System.nanoTime();
        synchronized(ua) {
            ua.add(action);
        }
    }
    
    public static void addAction(AwesomeAction action, long nanoTime,boolean repeat) {
        if(action==null) {
            System.out.println("dafuq");
            return;
        }
        main.add(action, nanoTime, repeat);
    }
    public static void addAction(AwesomeAction action, long time,boolean repeat, TimeUnit tu) {
        if(action==null) {
            System.out.println("dafuq");
            return;
        }
        main.add(action, time, repeat,tu);
    }
    
    public void runAndDestroy(AwesomeAction action) {
        action.run();
        synchronized(aa) {
            aa.remove(action);
        }
    }
    
    public void destroy(AwesomeAction action) {
        synchronized(aa) {
            aa.remove(action);
        }
    }
    
    public void destroy(UpdateAction action) {
        synchronized(ua) {
            ua.remove(action);
        }
    }
    
    public static void setTimeAffected(AwesomeAction a,boolean ta) {
        a.timeAffected = ta;
    }
    
    public static void setWarpTime(AwesomeAction a, WarpAffected b) {
        a.timeBody = b;
    }
    
    public static void addUpdateAction(UpdateAction action) {
        addUpdateAction(action,1);
    }
    
    public static void addUpdateAction(UpdateAction action,int times) {
        main.add(action, times);
    }
    
    public static void executeAndRemove(AwesomeAction action) {
        main.runAndDestroy(action);
    }
    
    public static void remove(AwesomeAction action) {
        main.destroy(action);
    }
    
    public static void remove(UpdateAction action) {
        main.destroy(action);
    }
    
}
