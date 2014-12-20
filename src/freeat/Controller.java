/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

/**
 *
 * @author otso
 */
public class Controller {
    
    private Player player;
    public final int ID;
    
    public Controller(Player player) {
        this.player = player;
        this.ID = player.ID;
    }
    
    public void moveTo(int nodeID){
        player.moveTo(nodeID);
    }
    
    public void buyToken(){
        player.buyToken();
    }
    
    public void tryToWinToken(){
        player.tryToWinToken();
    }
    
    public void flyTo(int nodeID){
        player.flyTo(nodeID);
    }
    
    public void stayInCity(){
        player.stayInCity();
    }
    public boolean hasMovesLeft(){
        return player.getMovesLeft() > 0;
    }
    
    
    public int getBalance(int playedID){
        return PublicInformation.getBalance(playedID);
    }
    
    public int getLocation(int playedID){
        return PublicInformation.getLocation(playedID);
    }
    
    public boolean hasStar(int playedID){
        return PublicInformation.hasStar(playedID);
    }
    
    public boolean hasHorseShoeAfterStar(int playerID){
        return PublicInformation.hasHorseshoeAfterStar(playerID);
    }
    
    public boolean isStarFound(){
        return PublicInformation.isStarFound();
    }
    
    public boolean isCapeTownBonus(){
        return PublicInformation.isCapeTownBonus();
    }
    
    public Node getCurrentNode(){
        return player.getCurrentNode();
    }
    
    public Node getNode(int nodeID){
        return player.getNode(nodeID);
    }
    
    public int movesLeft(){
        if(!player.isStayInCity())
            return player.getMovesLeft();
        else{
            System.out.println("Staying in city, don't know how many moves left");
            return -1;   
        }
    }
    
    public boolean isValidMove(int targetID){
        return player.isValidMove(targetID);
    }
    
    public void leaveCity(){
        player.leaveCity();
    }

    public void endTurn() {
        player.endTurn();
    }
    
    public int totalTreasures(){
        return PublicInformation.getTreasureTotal();
    }
    
    public int rubiesLeft(){
        return PublicInformation.getRubiesLeft();
    }
    
    public int topazesLeft(){
        return PublicInformation.getTopazesLeft();
    }
    
    public int robbersLeft(){
        return PublicInformation.getRobberLeft();
    }
    
    public int emeraldsLeft(){
        return PublicInformation.getEmeraldsLeft();
    }
    
    public int horseShoesLeft(){
        return PublicInformation.getHorseShoesLeft();
    }
    
    public boolean canFly(){
        return !player.isHasFlown();
    }

    public boolean isEndTurn() {
        return player.isEndTurn();
    }
    
}
