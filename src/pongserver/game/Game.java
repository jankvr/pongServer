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

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.ball = new Ball();
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
        
            while(player1!=null && player2!=null){
    //            player1.getPoint();
    //            player2.getPoint();
    //            ball.getPoint();
                if(player1.meets(ball)){
                    if(score.setRec2(1)){
                        this.ball=new Ball();
                    }
                    else{
                        winner = player2;
//                        System.out.println("Player2 is the winner");
                        break;
                    }
                }
                if(player2.meets(ball)){
                    if(score.setRec2(1)){
                        this.ball=new Ball();
                    }
                    else{
                        winner = player1;
//                        System.out.println("Player1 is the winner");
                        break;
                    }
                }
            }
            if(winner!=null){
                System.out.println(winner + "is the winner!");
            }
            else{
                System.out.println("Nobody won.");
            }
            
        } catch(Exception e){
            
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
