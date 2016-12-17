/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.game;

/**
 * Trieda skóre na strane klienta poskytuje a udržuje všetky informácie o stave hry
 * @author Jaroslav Fedorčák
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
    

    /**
     * Vracia počet bodov ľavého hráča
     * @return počet bodov ľavého hráča
     */
    public int getRec1() {
        return rec1;
    }

    /**
     * nastavuje počet bodov ľavého hráča
     * @param rec1 počet bodov ľavého hráča
     */
    public void setRec1(int rec1) {
        this.rec1 = rec1;
    }

    /**
     * vracia počet bodov pravého hráča
     * @return počet bodov pravého hráča
     */
    public int getRec2() {
        return rec2;
    }

    /**
     * nastavuje počet bodov pravého hráča
     * @param rec2 počet bodov pravého hráča
     */
    public void setRec2(int rec2) {
        this.rec2 = rec2;
    }

    /**
     * vracia informáciu o počte bodov, ktorý je potrebné dosiahnuť na vyhratie
     * @return počet bodov na vyhratie
     */
    public int getMax() {
        return max;
    }

    /**
     * nastavuje počet bodov na vyhratie
     * @param max počet bodov na vyhratie
     */
    public void setMax(int max) {
        this.max = max;
    }    
    
    /**
     * informuje o tom, či jeden z hráčov dosiahol maximálny počet bodo, teda vyhral
     * @return true pokiaľ hráč dosiahol maximálny počet bodov
     */
    public boolean isMax(){
        return rec1==max || rec2==max;
    }
    
    /**
     * nastavuje defaultný stav skóre 0:0
     */
    public void reset(){
        rec1=0;
        rec2=0;
    }
    
    /**
     * vracia plnú informáciu o skóre
     * @return 
     */
    public String getScoreInfo(){
        return rec1 +" "+rec2;
    }
}
