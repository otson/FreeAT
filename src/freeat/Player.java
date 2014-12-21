/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import freeat.ai.AI;
import freeat.ai.NormalAI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author otso
 */
public class Player {

    private static int idCount = 0;
    private AI ai;
    private int location;
    private int cashBalance;
    private boolean hasStar;
    private boolean hasHorseshoeAfterStar;
    private boolean foundCapeTown;
    private boolean inSahara;
    private boolean inPirates;
    private boolean stayInCity = false;
    private boolean isWinner;
    private boolean hasFlown;
    public final int ID;
    private HashMap<Integer, Node> locations;
    private ArrayList<Integer> visitedThisTurn;

    private int movesLeft = 0;

    private int skipTurns = 0;
    private Controller controller;

    private boolean endTurn;

    //decision booleans
    private boolean tryToWinToken;
    private boolean usePlane;
    private boolean useLandOrSea;
    private boolean moved;
    private int dice;

    public Player(HashMap<Integer, Node> locations) {
        ID = idCount;
        idCount++;
        this.locations = locations;
        cashBalance = 300;
        hasStar = false;
        hasHorseshoeAfterStar = false;
        hasFlown = false;
        foundCapeTown = false;
        inSahara = false;
        inPirates = true;
        isWinner = false;
        ai = new NormalAI();
        ai.setLocations(locations);
        this.location = ai.START;
        controller = new Controller(this, locations);
        visitedThisTurn = new ArrayList();

    }

    public Node getCurrentNode() {
        return locations.get(location);
    }

    public void act() {
        if (inSahara || inPirates) {
            if ((int) (Math.random() * 6 + 1) < 3) {
                inSahara = false;
                inPirates = false;
            }
        }
        if (!inSahara && !inPirates) {
            if (skipTurns == 0) {
                throwDice();
                ai.act(this);
            } else {
                skipTurns--;
            }
        }
    }

    private void throwDice() {
        dice = 1 + (int) (Math.random() * 5);
    }

    private void openToken() {
        Node temp = locations.get(location);
        if (temp.hasTreasure()) {
            if (temp.getTreasure() != TreasureType.OPENED) {
                int type = temp.getTreasure();
                temp.removeTreasure();
                if (type == TreasureType.EMERALD) {
                    PublicInformation.removeEmerald();
                    PublicInformation.removeUnopened();
                    cashBalance += 500;
                    endTurn = true;
                    if (temp.TYPE == NodeType.GOLD_COAST) {
                        cashBalance += 500;
                    }
                } else if (type == TreasureType.RUBY) {
                    PublicInformation.removeRuby();
                    PublicInformation.removeUnopened();
                    cashBalance += 1000;
                    endTurn = true;
                    if (temp.TYPE == NodeType.GOLD_COAST) {
                        cashBalance += 1000;
                    }
                } else if (type == TreasureType.TOPAZ) {
                    PublicInformation.removeTopaz();
                    PublicInformation.removeUnopened();
                    cashBalance += 300;
                    endTurn = true;
                    if (temp.TYPE == NodeType.GOLD_COAST) {
                        cashBalance += 300;
                    }
                } else if (type == TreasureType.ROBBER) {
                    PublicInformation.removeRobber();
                    PublicInformation.removeUnopened();
                    cashBalance = 0;
                    endTurn = true;
                } else if (type == TreasureType.HORSESHOE) {
                    PublicInformation.removeHorseShoe();
                    PublicInformation.removeUnopened();
                    endTurn = true;
                    if (PublicInformation.isStarFound()) {
                        hasHorseshoeAfterStar = true;
                    }
                } else if (type == TreasureType.STAR_OF_AFRICA) {
                    PublicInformation.removeUnopened();
                    hasStar = true;
                    endTurn = true;
                } else if (type == TreasureType.EMPTY) {
                    endTurn = true;
                    PublicInformation.removeEmpty();
                    PublicInformation.removeUnopened();
                    if (temp.TYPE == NodeType.SLAVE_COAST) {
                        skipTurns = 3;
                    }
                }
            }
        }
    }

    private void tryToWinToken() {
        Node temp = locations.get(location);
        if (temp.hasTreasure()) {
            if ((int) (Math.random() * 6 + 1) > 3) {
                openToken();
            }
        }
        endTurn = true;
    }

    public static void resetID() {
        idCount = 0;
    }

    public int getLocation() {
        return location;
    }

    public int getCashBalance() {
        return cashBalance;
    }

    public boolean isHasStar() {
        return hasStar;
    }

    public boolean isHasHorseshoeAfterStar() {
        return hasHorseshoeAfterStar;
    }

    public boolean isInSahara() {
        return inSahara;
    }

    public boolean isInPirates() {
        return inPirates;
    }

    public boolean isFoundCapeTown() {
        return foundCapeTown;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public boolean isStayInCity() {
        return stayInCity;
    }

    public int getDice() {
        if (useLandOrSea) {
            return dice;
        } else {
            return 0;
        }
    }

    private boolean inCity() {
        Node current = locations.get(this.location);
        return (current.getTYPE() == NodeType.CITY
                || current.getTYPE() == NodeType.CAIRO
                || current.getTYPE() == NodeType.TANGIR
                || current.getTYPE() == NodeType.GOLD_COAST
                || current.getTYPE() == NodeType.SLAVE_COAST
                || current.getTYPE() == NodeType.CAPE_TOWN);
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public boolean isValidMove(int targetID) {
        return !visitedThisTurn.contains(targetID);
    }

    public Node getNode(int nodeID) {
        return locations.get(nodeID);
    }

    // Decision
    public void decideToTryToken() {
        if (getCurrentNode().hasTreasure()) {
            if (!usePlane && !useLandOrSea) {
                tryToWinToken = true;
                tryToWinToken();
            }
        }
    }

    // Decision
    public void decidetoUsePlane() {
        if (!getCurrentNode().getPlaneConnections().isEmpty() && cashBalance >= 300) {
            if (!tryToWinToken && !useLandOrSea) {
                usePlane = true;
            }
        }
    }

    // Decision
    public void decideToUseLandOrSeaRoute() {
        if (!tryToWinToken && !usePlane) {
            useLandOrSea = true;
        }
    }

    // Land, sea, or plane
    public void moveTo(Route destination) {
        if (useLandOrSea || usePlane) {
            if (!moved) {
                if (cashBalance >= destination.getPrice() && getCurrentNode().getAllLists()[dice][destination.getPrice()].contains(destination)) {
                    Node target = destination.getDestination();
                    cashBalance -= destination.getPrice();
                    if (target.TYPE == NodeType.ROUTE) {
                        this.location = destination.getDestination().ID;
                        moved = true;
                    }
                    if (target.TYPE == NodeType.CITY) {
                        this.location = destination.getDestination().ID;
                        moved = true;

                    }
                    if (target.TYPE == NodeType.CAPE_TOWN) {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        if (PublicInformation.isCapeTownBonus()) {
                            cashBalance += 500;
                            foundCapeTown = true;
                        }
                    }
                    if (target.TYPE == NodeType.CAIRO) {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        if (location != ai.START && (hasHorseshoeAfterStar || hasStar)) {
                            isWinner = true;
                        }

                    }
                    if (target.TYPE == NodeType.TANGIR) {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        if (location != ai.START && (hasHorseshoeAfterStar || hasStar)) {
                            isWinner = true;
                        }
                    }
                    if (target.TYPE == NodeType.GOLD_COAST) {
                        this.location = destination.getDestination().ID;
                        moved = true;

                    }
                    if (target.TYPE == NodeType.SLAVE_COAST) {
                        this.location = destination.getDestination().ID;
                        moved = true;
                    }
                    if (target.TYPE == NodeType.SEA_ROUTE) {
                        this.location = destination.getDestination().ID;
                        moved = true;
                    }
                    if (target.TYPE == NodeType.SAHARA) {
                        inSahara = true;
                        moved = true;
                    }
                    if (target.TYPE == NodeType.PIRATES) {
                        inPirates = true;
                        moved = true;
                    }
                } else {
                    System.out.println("No place to move to, bug.");

                }
            }
        }
    }

    public void buyToken() {
        if (!endTurn) {
            Node temp = locations.get(location);
            if (temp.hasTreasure()) {
                if (cashBalance >= 100) {
                    cashBalance -= 100;
                    openToken();
                } else {
                    System.out.println("Not enough money to buy a token.");
                }
            } else {
                System.out.println("Not possible to buy token: no token available.");
            }
        }
    }

    public void endTurn() {
        if (moved) {
            endTurn = true;
        }
    }

}
