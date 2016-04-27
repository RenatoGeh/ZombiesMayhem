/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem;

/**
 *
 * @author Yan
 */
public abstract class UpdateAction {
    public long lastTime;
    public byte times,timesMax;
    public abstract void update(long dt);
}
