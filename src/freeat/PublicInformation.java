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
    
    

}
