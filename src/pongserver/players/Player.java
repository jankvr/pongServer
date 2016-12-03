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
    private double xPosition;
    private double yPosition;
    private double halfLength=50;
    private String name;


    private static final int SPEED = 5;
    
    public double getLength(){
        return halfLength*2;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
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
