/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class PublicInformation {

    public static final int PLAYER_COUNT = 2;

    private static int[] cashBalance = new int[PLAYER_COUNT];
    private static int[] location = new int[PLAYER_COUNT];
    private static boolean[] hasStar = new boolean[PLAYER_COUNT];
    private static boolean[] foundCapeTown = new boolean[PLAYER_COUNT];
    private static boolean[] hasHorseshoeAfterStar = new boolean[PLAYER_COUNT];
    private static boolean[] inSahara = new boolean[PLAYER_COUNT];
    private static boolean[] inPirates = new boolean[PLAYER_COUNT];
    private static boolean[] isWinner = new boolean[PLAYER_COUNT];

    private static int horseShoesTotal;
    private static int horseShoesLeft;
    private static int rybyTotal;
    private static int rubiesLeft;
    private static int topazTotal;
    private static int topazesLeft;
    private static int emeraldTotal;
    private static int emeraldsLeft;
    private static int robberTotal;
    private static int robberLeft;
    private static int emptyLeft;
    private static int emptyTotal;
    
    private static int unOpenedLeft;
    
    private static int treasureTotal;
    private static int treasuresLeft;

    static {
        cashBalance = new int[PLAYER_COUNT];
        location = new int[PLAYER_COUNT];
        hasStar = new boolean[PLAYER_COUNT];
        hasHorseshoeAfterStar = new boolean[PLAYER_COUNT];
        foundCapeTown = new boolean[PLAYER_COUNT];
    }

    public static int getBalance(int ID) {
        return cashBalance[ID];
    }

    public static int getLocation(int ID) {
        return location[ID];
    }

    public static boolean hasStar(int ID) {
        return hasStar[ID];
    }

    public static boolean hasHorseshoeAfterStar(int ID) {
        return hasHorseshoeAfterStar[ID];
    }

    public static void updateInformation(ArrayList<Player> players) {
        for (Player player : players) {
            cashBalance[player.ID] = player.getCashBalance();
            location[player.ID] = player.getLocation();
            hasStar[player.ID] = player.isHasStar();
            hasHorseshoeAfterStar[player.ID] = player.isHasHorseshoeAfterStar();
            inPirates[player.ID] = player.isInPirates();
            inSahara[player.ID] = player.isInSahara();
            foundCapeTown[player.ID] = player.isFoundCapeTown();
            isWinner[player.ID] = player.isWinner();
        }
    }

    public static boolean isStarFound() {
        for (int i = 0; i < hasStar.length; i++) {
            if (hasStar[i]) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCapeTownBonus() {
        for (int i = 0; i < foundCapeTown.length; i++) {
            if (foundCapeTown[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWinner() {
        for (int i = 0; i < isWinner.length; i++) {
            if (isWinner[i]) {
                return true;
            }
        }
        return false;
    }

    public static int getWinner() {
        for (int i = 0; i < isWinner.length; i++) {
            if (isWinner[i]) {
                return i;
            }
        }
        System.out.println("No winner! false");
        return -1;
    }

    public static void reset() {
        cashBalance = new int[PLAYER_COUNT];
        location = new int[PLAYER_COUNT];
        hasStar = new boolean[PLAYER_COUNT];
        foundCapeTown = new boolean[PLAYER_COUNT];
        hasHorseshoeAfterStar = new boolean[PLAYER_COUNT];
        inSahara = new boolean[PLAYER_COUNT];
        inPirates = new boolean[PLAYER_COUNT];
        isWinner = new boolean[PLAYER_COUNT];
    }

    public static int getHorseShoesTotal() {
        return horseShoesTotal;
    }

    public static int getHorseShoesLeft() {
        return horseShoesLeft;
    }

    public static int getRybyTotal() {
        return rybyTotal;
    }

    public static int getRubiesLeft() {
        return rubiesLeft;
    }

    public static int getTopazTotal() {
        return topazTotal;
    }

    public static int getTopazesLeft() {
        return topazesLeft;
    }

    public static int getEmeraldTotal() {
        return emeraldTotal;
    }

    public static int getEmeraldsLeft() {
        return emeraldsLeft;
    }

    public static int getRobberTotal() {
        return robberTotal;
    }

    public static int getRobberLeft() {
        return robberLeft;
    }

    public static void setHorseShoesTotal(int horseShoesTotal) {
        PublicInformation.horseShoesTotal = horseShoesTotal;
    }

    public static void setRybyTotal(int rybyTotal) {
        PublicInformation.rybyTotal = rybyTotal;
    }

    public static void setTopazTotal(int topazTotal) {
        PublicInformation.topazTotal = topazTotal;
    }

    public static void setEmeraldTotal(int emeraldTotal) {
        PublicInformation.emeraldTotal = emeraldTotal;
    }

    public static void setRobberTotal(int robberTotal) {
        PublicInformation.robberTotal = robberTotal;
    }

    public int getTreasuresLeft() {
        return treasuresLeft;
    }

    public void setTreasuresLeft(int treasuresLeft) {
        PublicInformation.treasuresLeft = treasuresLeft;
    }

    public static void setTreasureTotal(int treasureTotal) {
        PublicInformation.treasureTotal = treasureTotal;
    }
    
    public static void removeEmerald(){
        emeraldsLeft--;
    }
    
    public static void removeRobber(){
        robberLeft--;
    }
    
    public static void removeTopaz(){
        topazesLeft--;
    }
    
    public static void removeRuby(){
        rubiesLeft--;
    }
    
    public static void removeHorseShoe(){
        horseShoesLeft--;
    }

    public static int getTreasureTotal() {
        return treasureTotal;
    }

    public static int getEmptyLeft() {
        return emptyLeft;
    }

    public static int getEmptyTotal() {
        return emptyTotal;
    }

    public static int getUnOpenedLeft() {
        return unOpenedLeft;
    }
    
    public static void removeUnopened(){
        unOpenedLeft--;
    }
    
    public static void removeEmpty(){
        emptyLeft--;
    }

    public static void setEmptyLeft(int emptyLeft) {
        PublicInformation.emptyLeft = emptyLeft;
    }

    public static void setEmptyTotal(int emptyTotal) {
        PublicInformation.emptyTotal = emptyTotal;
        emptyLeft = emptyTotal;
    }

    public static void setUnOpenedLeft(int unOpenedLeft) {
        PublicInformation.unOpenedLeft = unOpenedLeft;
    }
    
    
    

}
