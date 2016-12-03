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
public class Ball {
    private int xPosition;
    private int yPosition;

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
    public Ball() {
        xPosition=50;
        yPosition=50;
    }
    
    

    
    
}
