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
    private final Game game;

    public Score(Game game) {
        this.game=game;
    }
    
    /**
     * Zvýši skôre hráča 1 o daný počet bodov
     * @param points
     * @return informáciu o tom, či sa podarilo zvýšiť počet bodov. Ak sa už 
     * nedá zvýšiť počet bodov, vráti false, inak vráti true.
     */
    public boolean increaseRec1(int points){
        rec1=rec1+points;
        return rec1 != max;
    }
    
        /**
     * Zvýši skôre hráča 2 o daný počet bodov
     * @param points
     * @return informáciu o tom, či sa podarilo zvýšiť počet bodov. Ak sa už 
     * nedá zvýšiť počet bodov, vráti false, inak vráti true.
     */
    public boolean increaseRec2(int points){
        rec2=rec2+points;
        return rec2 != max;
    }
    
    public String getWinnerInfo(){
        if(rec1>rec2) {
            return game.getPlayer1().getName()+". The score is "+rec1+":"+rec2;
        }
        if(rec1<rec2) {
            return game.getPlayer1().getName()+". The score is "+rec1+":"+rec2;
        }
        else {
            return "nobody, it's a tie. The score is "+rec1+":"+rec2;
        }
    }
    
    public int getRec1() {
        return rec1;
    }

    public void setRec1(int rec1) {
        this.rec1 = rec1;
    }

    public int getRec2() {
        return rec2;
    }

    public void setRec2(int rec2) {
        this.rec2 = rec2;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }    
    
    public boolean isMax(){
        return rec1==max || rec2==max;
    }
    
    public void reset(){
        rec1=0;
        rec2=0;
    }
    
    public String getScoreInfo(){
        return rec1 +" "+rec2;
    }
}
