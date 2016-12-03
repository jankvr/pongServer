/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.game;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import pongserver.players.PlayerThread;

/**
 *
 * @author User
 */
public class Game implements Runnable {
    private PlayerThread player1;//hráč na ľavej strane
    private PlayerThread player2;//hráč na pravej strane
    private Ball ball;
    private Score score;
    private PlayerThread winner = null;
    private static final int WIDTH = 800; 
    private static final int HEIGHT = WIDTH/4*3;
    private static final double SCALE = 1;
    public static final int MAP_WIDTH = WIDTH;
    public static final int MAP_HEIGHT = HEIGHT;

    public Game(PlayerThread player1, PlayerThread player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.ball = new Ball(this);
        this.score = new Score(this);
    }
    
//    public void setWinner(Player winner){
//        this.winner = winner;
//    }
    
    public PlayerThread getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerThread player1) {
        this.player1 = player1;
    }

    public PlayerThread getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerThread player2) {
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

    public void setSides() {
        try {
            player1.getOutputStream().writeUTF("RIGHT");
            Thread.sleep(1000);
            player2.getOutputStream().writeUTF("LEFT");
            Thread.sleep(100);
            player1.setGame(this);
            player2.setGame(this);
            Thread.sleep(100);
            player1.getOutputStream().writeUTF("START");
            player2.getOutputStream().writeUTF("START");
            
            
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            setSides();
            //sendStartMessage();
            
            //priebeh hry ak sú obaja hráči prítomní
            //while(player1!=null && player2!=null) {
            new AnimationTimer() {
                @Override
                public void handle(long currentNanoTime) {
                    try {

                        String p1 = player1.getInputStream().readUTF();
                        player2.getOutputStream().writeUTF(p1);
                        
                        String p2 = player2.getInputStream().readUTF();
                        player1.getOutputStream().writeUTF(p2);
                        
                        //ak lopta je za hráčovim pádlom
                        if(player1.getxPosition()>ball.getxPosition()){
                            //zvýšim mu skóre pokiaľ nie je maximálne, a resetujem loptu
                            if(score.increaseRec2(1)){
                                ball.reset();
                            }
                            //ukončím celý cyklus while v prípade že už sa mu nedá zvýšiť skóre, teda dosiahlo maximum
                            else{
                                //break;
                            }
                        }
                        
                        //to isté aj pre hráča 2
                        if(player2.getxPosition()<ball.getxPosition()){
                            if(score.increaseRec2(1)){
                                ball.reset();
                            }
                            else{
                                //winner = player1;
//                        System.out.println("Player1 is the winner");
//break;
                            }
                        }   } catch (IOException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }

                
            }
            };
            //hra skončila, buď jeden hráč dosiahol maximálne skóre
            //alebo už jeden hráč nie je prítomný
           
            System.out.println("Game is over. The winner is "
                    +score.getWinnerInfo());  
            
            
            
        } catch(Exception e){
            
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getName() {
        return player1.getName() + "_" + player2.getName();
    }
}
