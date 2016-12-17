/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.game;

/**
 * Trieda lopta spravuje pohyb lopty na obrazovke, odrážanie a iné
 * @author Jaroslav Fedorčák, Jan Kovář
 */
public class Ball {
    private double xPosition;
    private double yPosition;
    private final double defaultX = Game.MAP_WIDTH/2;
    private final double defaultY = Game.MAP_HEIGHT/2;
    public double directionX, directionY;
    private final Game game;
    private final double height=20;
    private final double width=20;
    
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
    
    /**
     * vracia pozíciu lopty na x-ovej osi
     * @return pozícia lopty na x-ovej osi
     */
    public double getxPosition() {
        return xPosition;
    }

    /**
     * vracia pozíciu lopty na y-ovej osi
     * @return pozícia lopty na y-ovej osi
     */
    public double getyPosition() {
        return yPosition;
    }

    /**
     * nastavuje pozíciu lopty na x-ovej osi
     * @param xPosition pozícia lopty na x-ovej osi
     */
    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }
    
    /**
     * nastavuje pozíciu lopty na y-ovej osi
     * @param yPosition pozícia lopty a y-ovej osi
     */
    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }
 
    /**
     * vracia informáciu o tom, či je lopta v kolízii v odrážacou plochou hráča
     * @return true, pokiaľ je lopta v kolízii v odrážacou plochou hráča
     */
    private boolean isCollisionWithPod() {
        return (this.game.getPlayer1().meets(this) ||
                this.game.getPlayer2().meets(this));
    }
    
    /**
     * Posunutie lopty v jednom kroku aplikácie
     * @throws InterruptedException 
     */
    public void move() throws InterruptedException {
        if (!this.canMove) {
            return;
        }
        
        // kolize s ploskou
        if (isCollisionWithPod()) {
            this.directionX *= -1;//lopta sa odrazila
            
            double podLength = this.game.getPlayer1().getLength();
        
            double positionOnY = this.yPosition - this.game.getPlayer1().getyPosition();
            

            if(positionOnY+this.height<=podLength/3){
                if(this.directionY>0){this.directionY*=-1;}
                else {this.directionY*=1;}
            } //lopta odrazená hornou tretinou sa odráža vždy dohora
            else if (positionOnY > 2*podLength/3){
                
                     if(this.directionY<0){this.directionY*=-1;}
                else {this.directionY*=1;}
            }//lopta odrazená spodnou tretinou sa vždy odráža nadol
            else {
                this.directionX *= 1;
            }  
        }

        if (this.yPosition < 0) {
            this.directionY *= -1;
        }//odrazenie z horného okraja
        else if ((this.yPosition + this.width) > Game.MAP_HEIGHT) {
            this.directionY *= -1; 
        }//odrazenie od spodného okraja
        // toto je jen kolize s pravou stranou, od te se nebude odrazet
        else if ((this.xPosition + this.height) > Game.MAP_WIDTH) {
            //this.directionX *= -1;
            //System.out.println("bod pro leveho");
            game.getScore().increaseRec1(1);
            this.canMove = false;
            this.reset(); 
        }
        
        // toto je kolize s levou stranou
        else if (this.xPosition < 0) {
            //System.out.println("bod pro praveho");
            game.getScore().increaseRec2(1);
            this.canMove = false;
            this.reset();
        }
        
        this.xPosition = this.xPosition + (this.directionX * SPEED);
        this.yPosition = this.yPosition + (this.directionY * SPEED);        
    }
    
    /**
     * vracia informáciu o tom, či sa lopta vie hýbať
     * @return true, pokiaľ sa lopta vie hýbať
     */
    public boolean canMove() {
        return this.canMove;
    }    
    
    /**
     * nastavenie východzej polohy a stavu lopty uprostred hracej plochy
     */
    public void reset(){
        xPosition = defaultX;
        yPosition = defaultY;
        this.canMove = true;
    }
    
    /**
     * podáva informáciu o svojej pozícii, spracovateľnú parserom
     * @return informácia o svojej pozícii
     */
    public String getCurrentPosition() {
        //System.out.println("BALL " + this.xPosition + " " + this.yPosition);
        return "BALL " + this.xPosition + " " + this.yPosition;
    }

}
