/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.players;

import pongserver.game.Ball;

/**
 *
 * @author User
 */
public class Player {
    private int xPosition;
    private int yPosition;
    private int halfLength=50;
    private String name;

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
    
    public boolean meets(Ball ball){
        if (ball.getyPosition()<this.yPosition+halfLength && 
                ball.getyPosition()>this.yPosition-halfLength){
            return true;
        }
        else{
            return false;
        }
    }
}
