/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import freeat.ai.AI;
import freeat.ai.NormalAI;
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
    public final int ID;
    private HashMap<Integer, Node> locations;

    private int skipTurns = 0;

    public Player(HashMap<Integer, Node> locations) {
        ID = idCount;
        idCount++;
        this.locations = locations;
        cashBalance = 300;
        hasStar = false;
        hasHorseshoeAfterStar = false;
        foundCapeTown = false;
        inSahara = false;
        inPirates = true;
        isWinner = false;
        ai = new NormalAI();
        ai.setLocations(locations);
        this.location = ai.START;

    }
    
    public Node getCurrentNode(){
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
                ai.act(this);
            } else {
                skipTurns--;
            }
        }
    }

    public void buyToken() {
        if (cashBalance >= 100) {
            cashBalance -= 100;
            openToken();
        }
    }

    private void openToken() {
        Node temp = locations.get(location);
        if (temp.getTreasure() != TreasureType.OPENED) {
            int type = temp.getTreasure();
            temp.removeTreasure();
            if (type == TreasureType.EMERALD) {
                cashBalance += 500;
                if (temp.TYPE == NodeType.GOLD_COAST) {
                    cashBalance += 500;
                }
            } else if (type == TreasureType.RUBY) {
                cashBalance += 1000;
                if (temp.TYPE == NodeType.GOLD_COAST) {
                    cashBalance += 1000;
                }
            } else if (type == TreasureType.TOPAZ) {
                cashBalance += 300;
                if (temp.TYPE == NodeType.GOLD_COAST) {
                    cashBalance += 300;
                }
            } else if (type == TreasureType.ROBBER) {
                cashBalance = 0;
            } else if (type == TreasureType.HORSESHOE) {
                if (PublicInformation.isStarFound()) {
                    hasHorseshoeAfterStar = true;
                }
            } else if (type == TreasureType.STAR_OF_AFRICA) {
                hasStar = true;
            } else if (type == TreasureType.EMPTY) {
                if (temp.TYPE == NodeType.SLAVE_COAST) {
                    skipTurns = 3;
                }
            }

        }
    }

    public void stayInCity() {
        stayInCity = true;
    }

    public void tryToWinToken() {
        if (stayInCity) {
            if ((int) (Math.random() * 6 + 1) > 3) {
                openToken();
            }
        }
    }

    public void flyTo(int location) {
        Node current = locations.get(this.location);
        if (current.hasPlaneConnection(location)) {
            if (cashBalance >= 300) {
                cashBalance -= 300;
                Node target = locations.get(location);
                if (target.TYPE == NodeType.CITY) {
                    this.location = location;
                }
                if (target.TYPE == NodeType.CAPE_TOWN) {
                    this.location = location;
                    if (PublicInformation.isCapeTownBonus()) {
                        cashBalance += 500;
                        foundCapeTown = true;
                    }
                }
                if (target.TYPE == NodeType.CAIRO) {
                    this.location = location;
                    if (location != ai.START && (hasHorseshoeAfterStar || hasStar)) {
                        isWinner = true;
                    }
                }
                if (target.TYPE == NodeType.TANGIR) {
                    this.location = location;
                    if (location != ai.START && (hasHorseshoeAfterStar || hasStar)) {
                        isWinner = true;
                    }
                }
                if (target.TYPE == NodeType.GOLD_COAST) {
                    this.location = location;
                }
                if (target.TYPE == NodeType.SLAVE_COAST) {
                    this.location = location;
                }
            }
        }
    }

    public void moveTo(int location) {
        Node current = locations.get(this.location);
        if (current.hasConnection(location)) {
            Node target = locations.get(location);
            if (target.TYPE == NodeType.ROUTE) {
                this.location = location;
            }
            if (target.TYPE == NodeType.CITY) {
                this.location = location;
            }
            if (target.TYPE == NodeType.CAPE_TOWN) {
                this.location = location;
                if (PublicInformation.isCapeTownBonus()) {
                    cashBalance += 500;
                    foundCapeTown = true;
                }
            }
            if (target.TYPE == NodeType.CAIRO) {
                this.location = location;
                if (location != ai.START && (hasHorseshoeAfterStar || hasStar)) {
                    isWinner = true;
                }

            }
            if (target.TYPE == NodeType.TANGIR) {
                this.location = location;
                if (location != ai.START && (hasHorseshoeAfterStar || hasStar)) {
                    isWinner = true;
                }
            }
            if (target.TYPE == NodeType.GOLD_COAST) {
                this.location = location;
            }
            if (target.TYPE == NodeType.SLAVE_COAST) {
                this.location = location;
            }
            if (target.TYPE == NodeType.SEA_ROUTE) {
                if (current.TYPE != NodeType.SEA_ROUTE || current.TYPE != NodeType.PIRATES) {
                    if (cashBalance >= 100) {
                        cashBalance -= 100;
                        this.location = location;
                    }
                } else {
                    this.location = location;
                }
            }
            if (target.TYPE == NodeType.SAHARA) {
                inSahara = true;
            }
            if (target.TYPE == NodeType.PIRATES) {
                inPirates = true;
            }
        } else {
            System.out.println("Illegal move: no connection");
        }
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

}
