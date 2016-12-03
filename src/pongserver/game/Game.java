/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.game;

import pongserver.players.Player;

/**
 *
 * @author User
 */
public class Game implements Runnable {
    private Player player1;//hráč na ľavej strane
    private Player player2;//hráč na pravej strane
    private Ball ball;
    private Score score;
    private Player winner = null;
    private static final int WIDTH = 640; 
    private static final int HEIGHT = WIDTH/4*3;
    private static final double SCALE = 1;
    public static final int MAP_WIDTH = WIDTH;
    public static final int MAP_HEIGHT = HEIGHT;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player1.setName("Player 1");
        this.player2 = player2;
        this.player2.setName("Player 2");
        this.ball = new Ball(this);
        this.score = new Score();
    }
    
//    public void setWinner(Player winner){
//        this.winner = winner;
//    }
    
    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @Override
    public void run() {
        try{
            //priebeh hry ak sú obaja hráči prítomní
            while(player1!=null && player2!=null){
                //ak lopta je za hráčovim pádlom
                if(player1.getxPosition()>ball.getxPosition()){
                    //zvýšim mu skóre pokiaľ nie je maximálne, a resetujem loptu
                    if(score.increaseRec2(1)){
                        this.ball.reset();
                    }
                    //ukončím celý cyklus while v prípade že už sa mu nedá zvýšiť skóre, teda dosiahlo maximum
                    else{
                        break;
                    }
                }
                
                //to isté aj pre hráča 2
                if(player2.getxPosition()<ball.getxPosition()){
                    if(score.increaseRec2(1)){
                        this.ball.reset();
                    }
                    else{
                        //winner = player1;
//                        System.out.println("Player1 is the winner");
                        break;
                    }
                }
            }
            //hra skončila, buď jeden hráč dosiahol maximálne skóre
            //alebo už jeden hráč nie je prítomný
           
            System.out.println("Game is over. The winner is "
                    +score.getWinnerInfo());  
            
            
            
        } catch(Exception e){
            
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
