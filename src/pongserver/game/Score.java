/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.game;

/**
 *
 * @author User
 */
public class Score {
    private int rec1 = 0;//skóre hráča 1 (ľavého)
    private int rec2 = 0;//skóre hráča 2 (pravého)
    private int max = 10;//maximálne skóre 1 hráča
    
    /**
     * Zvýši skôre hráča 1 o daný počet bodov
     * @param points
     * @return informáciu o tom, či sa podarilo zvýšiť počet bodov. Ak sa už 
     * nedá zvýšiť počet bodov, vráti false, inak vráti true.
     */
    public boolean setRec1(int points){
        rec1=rec1+points;
        if(rec1==max){
            return false;
        }        
        return true;
    }
    
        /**
     * Zvýši skôre hráča 2 o daný počet bodov
     * @param points
     * @return informáciu o tom, či sa podarilo zvýšiť počet bodov. Ak sa už 
     * nedá zvýšiť počet bodov, vráti false, inak vráti true.
     */
    public boolean setRec2(int points){
        rec1=rec1+points;
        if(rec1==max){
            return false;
        }        
        return true;
    }
}
