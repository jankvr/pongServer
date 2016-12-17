/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.game;

import java.io.IOException;
import org.apache.log4j.Logger;
import pongserver.main.Login;
import pongserver.players.PlayerThread;

/**
 * Samotná hra medzi dvomi hráčmi.
 * @author Jan Kovář, Jaroslav Fedorčák
 */
public class Game implements Runnable {
    
    private static final Logger LOG = Logger.getLogger(Game.class.getName());
    
    private Ball ball;
    private Score score;

    private static final int WIDTH = 800; 
    private static final int HEIGHT = WIDTH/4*3;
    private static final int WAIT = 8;
    
    public static final int MAP_WIDTH = WIDTH;
    public static final int MAP_HEIGHT = HEIGHT;
    
    private Thread player1Thread;
    private Thread player2Thread;
    
//    private CmdParser parser;
    
    private PlayerThread player1;//hráč na ľavej strane
    private PlayerThread player2;//hráč na pravej strane
    
    private final Login login;

    public Game(PlayerThread player1, PlayerThread player2, Login login) {
        this.player1 = player1;
        this.player2 = player2;
        this.ball = new Ball(this);
        this.score = new Score(this);
        this.login = login;
//        this.parser = new CmdParser();
        
    }
    

    /**
     * vracia ľavého hráča
     * @return ľavý hráč
     */
    public PlayerThread getPlayer1() {
        return player1;
    }

    /**
     * nastavuje ľavého hráča
     * @param player1 ľavý hráč
     */
    public void setPlayer1(PlayerThread player1) {
        this.player1 = player1;
    }
 
   /**
    * vracia pravého hráča
    * @return praýv hráč
    */
    public PlayerThread getPlayer2() {
        return player2;
    }
    
    /**
     * nastavuje pravého hráča
     * @param player2 pravý hráč
     */

    public void setPlayer2(PlayerThread player2) {
        this.player2 = player2;
    }

    /**
     * vracia loptu danej hry
     * @return lopta
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * nastavuje loptu v danej hre
     * @param ball lopta
     */
    public void setBall(Ball ball) {
        this.ball = ball;
    }

    /**
     * vracia aktuálne skóre hry
     * @return skóre
     */
    public Score getScore() {
        return score;
    }

    /**
     * nastavuje skóre v hre
     * @param score skóre
     */
    public void setScore(Score score) {
        this.score = score;
    }

    /**
     * Nastavuje východzie rozpoloženie hry a spúšťa ju
     */
    public void setSidesAndStart() {
        try {
            player1.getOutputStream().writeUTF("RIGHT");
            player1.setxPosition(20);
            Thread.sleep(2000);
            player2.getOutputStream().writeUTF("LEFT");
            player2.setxPosition(760);
            Thread.sleep(1000);
            player1.setOpponent(this);
            player2.setOpponent(this);
            Thread.sleep(100);
            player1.getOutputStream().writeUTF("START");
            player2.getOutputStream().writeUTF("START");

            
        } catch (IOException | InterruptedException ex) {
            LOG.fatal(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // nastartuj vlakna
            player1Thread = new Thread(player1);
            player2Thread = new Thread(player2);
            
            player1Thread.start();
            player2Thread.start();
            
            // a hru
            setSidesAndStart();
            boolean willToContinue = true; //vôľa pokračovať
            while(willToContinue){
                while (!score.isMax()) {

    //###############################POSILANI_UDAJU_O_POLOZE_MICKU_A_SKORE######################
                    ball.move();
                    // tato zprava se paradoxne posila hraci c.2
//                    player1.sendData(ball.getCurrentPosition());  //ODKOMENTOVAŤ ak 134 135 blbnú
                    // a tahle naopak hraci 1 (to kvuli implementaci sendData v PlayerThread)
//                    player2.sendData(ball.getCurrentPosition());   //ODKOMENTOVAŤ ak 134 135 blbnú
                    
                    //System.out.println("BALLPOSITIONANDSCORE "+ball.getxPosition()+" "+ball.getyPosition()+" "+score.getScoreInfo());
                    player1.sendData("BALLPOSITIONANDSCORE "+ball.getxPosition()+" "+ball.getyPosition()+" "+score.getScoreInfo());
                    player2.sendData("BALLPOSITIONANDSCORE "+ball.getxPosition()+" "+ball.getyPosition()+" "+score.getScoreInfo());
                    
                    Thread.sleep(WAIT);

                }
                            
                
                
                willToContinue = true;
                score.reset();
                setSidesAndStart();
            }
            
                       
        } catch(IOException | InterruptedException e){
            LOG.fatal(e.getMessage());
        }
    }
  
    /**
     * vracia mená oboch hráčov
     * @return mená oboch hráčov oddelené medzerou
     */
    public String getName() {
        return player1.getName() + "_" + player2.getName();
    }

    
    /**
     * Login na to, aby sa hráč mohol pri odhlásení odobrať zo zoznamu prihlásených klientov
     * @return login
     */
    public Login getLogin() {
        return login;
    }
    
    
}
