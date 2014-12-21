/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author otso
 */
public class Controller {
    
    private Player player;
    private HashMap<Integer, Node> locations;
    public final int ID;
    
    
    public Controller(Player player, HashMap<Integer, Node> locations) {
        this.player = player;
        this.locations = locations;
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
    
    public int emptyLeft(){
        return PublicInformation.getEmptyLeft();
    }
    
    public int unopenedLeft(){
        return PublicInformation.getUnOpenedLeft();
    }
    
    //public ArrayList<Route> getAvailableRoutes(Node start, int cash, int dice) {
    //    return 
    //}
    
    public boolean canFly(){
        return !player.isHasFlown();
    }

    public boolean isEndTurn() {
        return player.isEndTurn();
    }
    
    public ArrayList<Route> getMyAvailableRoutes(){
        return getAvailableRoutes(player.getCurrentNode(), player.getCashBalance(), 6);
    }
    
    public ArrayList<Route> getAvailableRoutes(Node start, int cash, int dice) {
        if (cash < 100) {
            switch (dice) {
                case 1:
                    return locations.get(start.ID).getDistance1Cost0();
                case 2:
                    return locations.get(start.ID).getDistance2Cost0();
                case 3:
                    return locations.get(start.ID).getDistance3Cost0();
                case 4:
                    return locations.get(start.ID).getDistance4Cost0();
                case 5:
                    return locations.get(start.ID).getDistance5Cost0();
                case 6:
                    return locations.get(start.ID).getDistance6Cost0();
            }
        } else if (cash < 200) {
            switch (dice) {
                case 1:
                    return locations.get(start.ID).getDistance1Cost100();
                case 2:
                    return locations.get(start.ID).getDistance2Cost100();
                case 3:
                    return locations.get(start.ID).getDistance3Cost100();
                case 4:
                    return locations.get(start.ID).getDistance4Cost100();
                case 5:
                    return locations.get(start.ID).getDistance5Cost100();
                case 6:
                    return locations.get(start.ID).getDistance6Cost100();
            }
        } else if (cash < 300) {
            switch (dice) {
                case 1:
                    return locations.get(start.ID).getDistance1Cost200();
                case 2:
                    return locations.get(start.ID).getDistance2Cost200();
                case 3:
                    return locations.get(start.ID).getDistance3Cost200();
                case 4:
                    return locations.get(start.ID).getDistance4Cost200();
                case 5:
                    return locations.get(start.ID).getDistance5Cost200();
                case 6:
                    return locations.get(start.ID).getDistance6Cost200();
            }
        } else if (cash >= 300) {
            switch (dice) {
                case 1:
                    return locations.get(start.ID).getDistance1Cost300();
                case 2:
                    return locations.get(start.ID).getDistance2Cost300();
                case 3:
                    return locations.get(start.ID).getDistance3Cost300();
                case 4:
                    return locations.get(start.ID).getDistance4Cost300();
                case 5:
                    return locations.get(start.ID).getDistance5Cost300();
                case 6:
                    return locations.get(start.ID).getDistance6Cost300();
            }
        }
        
        System.out.println("No destinations found, error...");
        return null;

    }

    
    
}
