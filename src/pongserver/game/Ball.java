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
    private double xPosition;
    private double yPosition;
    private double defaultX = 50;
    private double defaultY = 50;
    public double directionX, directionY;
    private final Game game;
    //private final Pong ps;
    private double height=10;
    private double width=10;
    
    private boolean canMove;
    
    private static final int SPEED = 2;
   
    public Ball(Game game) {
        xPosition=defaultX;
        yPosition=defaultY;
        this.game=game;
        this.canMove = true;
        this.directionX = -1;
        this.directionY = -1 * (Math.random());        
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

    private boolean isCollisionWithPod() {
        // melo by se kontrolovat s tim, kdo ted bude odehravat
        return (this.game.getPlayer1().meets(this)||
                this.game.getPlayer2().meets(this));
    }
    
    public void move() throws InterruptedException {
        if (!this.canMove) {
            return;
        }
        
        // kolize s ploskou
        if (isCollisionWithPod()) {
            this.directionX *= -1;
            
            double podLength = this.game.getPlayer1().getLength();
        
            double positionOnY = this.yPosition - this.game.getPlayer1().getyPosition();
            
            if ((positionOnY + this.height <= podLength/3) 
                    || (positionOnY > 2*podLength/3)) {

                this.directionX *= 1;
                System.out.println("okraj");
            }
            
            else {
                this.directionX *= 1;
                
                System.out.println("stred");
            }
            
        }

        if (this.defaultY < 0) {
            this.directionY *= -1;
        }
        else if ((this.yPosition + this.width) > Game.MAP_HEIGHT) {
            this.directionY *= -1; 
        }
        // toto je jen kolize s pravou stranou, od te se nebude odrazet
        else if ((this.xPosition + this.height) > Game.MAP_WIDTH) {
            this.directionX *= -1;
//            System.out.println("bod pro leveho");
//            this.canMove = false;
//            this.resetBall();
        }
        
        // toto je kolize s levou stranou
        else if (this.xPosition < 0) {
            System.out.println("bod pro praveho");
            this.canMove = false;
            this.reset();
        }
        
        this.xPosition = this.xPosition + (this.directionX * SPEED);
        this.yPosition = this.yPosition + (this.directionY * SPEED);
        //this.sprite.setXY(position.x, position.y);
        
        
    }
    
    public boolean canMove() {
        return this.canMove;
    }    
    
    public void reset(){
        xPosition=defaultX;
        yPosition=defaultY;
        this.canMove=true;
    }
    
    

    
    
}
