/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import freeat.Globals;
import freeat.ai.AI;
import freeat.ai.DrawNode;
import freeat.ai.LoomAI;
import freeat.ai.DumbAI;
import freeat.ai.NormalAI;
import freeat.ai.ParanormalAI;
import freeat.ai.RouteAI;
import freeat.ai.Test2AI;
import freeat.ai.TestAI;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 *
 * @author otso
 */
public class Player
{

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
    private String debugString;
    private boolean usingFreeSeaRoute = false;

    private int skipTurns = 0;
    private Controller controller;

    private boolean endTurn;

    //decision booleans
    private boolean tryToWinToken;
    private boolean usePlane;
    private boolean useLandOrSea;
    private boolean moved;
    private int dice;

    private float r, g, b;
    private String name;

    public Player(HashMap<Integer, Node> locations)
    {

        ID = idCount;
        idCount++;
        this.locations = locations;
        cashBalance = Globals.START_CASH;
        hasStar = false;
        hasHorseshoeAfterStar = false;
        foundCapeTown = false;
        inSahara = false;
        inPirates = true;
        isWinner = false;

        int nAITypes, AIType;
        nAITypes = AI.AIIdentifications.size();
        //System.out.println("Size: "+AI.AIIdentifications.size());
        if (nAITypes == 0)
        {
            AIType = 1;
        } else
        {
            AIType = (ID % nAITypes);
        }

        switch (AIType)
        {
            case 0:
                ai = new TestAI();
                break;
            case 1:
                ai = new ParanormalAI();
                break;
            default:
                System.out.println("number of AIIdentifications (" + nAITypes + ") is greater number of cases in Player.java\n"
                        + "Creating a new NormalAI as default action. Please fix Player.java");
                ai = new LoomAI(); // create default AI.
                break;
        }

        this.location = ai.START;
        controller = new Controller(this, locations);
        ai.setController(controller);
        r = ai.getR();
        g = ai.getG();
        b = ai.getB();
        name = ai.getName();

        ai.setLocations(locations);
        debugString = "Test";

    }

    public Node getCurrentNode()
    {
        return locations.get(location);
    }

    public void act()
    {
        if (inSahara || inPirates)
        {
            if ((int) (Math.random() * 6 + 1) < 3)
            {
                inSahara = false;
                inPirates = false;
            }
        }
        if (!inSahara && !inPirates)
        {
            if (skipTurns == 0)
            {
                throwDice();
                endTurn = false;
                moved = false;
                tryToWinToken = false;
                useLandOrSea = false;
                usePlane = false;
                ai.act();
            } else
            {
                skipTurns--;
            }
        }
    }

    private void throwDice()
    {
        dice = 0;
        for (int i = 0; i < Globals.DICE_COUNT; i++)
        {
            dice += Globals.SINGLE_DICE_MIN_VALUE + (int) (Math.random() * Globals.DICE_SIZE); // after full implementation of Globals.
        }
    }

    private void openToken()
    {
        Node temp = locations.get(location);

        if (temp.hasTreasure())
        {
            if (temp.getTreasure() != TreasureType.OPENED)
            {
                int type = temp.getTreasure();
                temp.removeTreasure();
                if (type == TreasureType.EMERALD)
                {
                    PublicInformation.removeEmerald();
                    PublicInformation.removeUnopened();
                    cashBalance += Globals.EMERALD_VALUE;
                    endTurn = true;
                    if (temp.TYPE == NodeType.GOLD_COAST)
                    {
                        for (int i = 0; i < Globals.GOLD_COAST_FACTOR; i++)
                        {
                            cashBalance += Globals.EMERALD_VALUE;
                        }
                    }
                } else if (type == TreasureType.RUBY)
                {
                    PublicInformation.removeRuby();
                    PublicInformation.removeUnopened();
                    cashBalance += Globals.RUBY_VALUE;
                    endTurn = true;
                    if (temp.TYPE == NodeType.GOLD_COAST)
                    {
                        for (int i = 0; i < Globals.GOLD_COAST_FACTOR; i++)
                        {
                            cashBalance += Globals.RUBY_VALUE;
                        }
                    }
                } else if (type == TreasureType.TOPAZ)
                {
                    PublicInformation.removeTopaz();
                    PublicInformation.removeUnopened();
                    cashBalance += Globals.TOPAZ_VALUE;
                    endTurn = true;
                    if (temp.TYPE == NodeType.GOLD_COAST)
                    {
                        for (int i = 0; i < Globals.GOLD_COAST_FACTOR; i++)
                        {
                            cashBalance += Globals.TOPAZ_VALUE;
                        }
                    }
                } else if (type == TreasureType.ROBBER)
                {
                    PublicInformation.removeRobber();
                    PublicInformation.removeUnopened();
                    cashBalance = 0;
                    endTurn = true;
                } else if (type == TreasureType.HORSESHOE)
                {
                    PublicInformation.removeHorseShoe();
                    PublicInformation.removeUnopened();
                    endTurn = true;
                    if (PublicInformation.isStarFound())
                    {
                        hasHorseshoeAfterStar = true;
                    }
                } else if (type == TreasureType.STAR_OF_AFRICA)
                {
                    PublicInformation.removeUnopened();
                    hasStar = true;
                    endTurn = true;
                } else if (type == TreasureType.EMPTY)
                {
                    endTurn = true;
                    PublicInformation.removeEmpty();
                    PublicInformation.removeUnopened();
                    if (temp.TYPE == NodeType.SLAVE_COAST)
                    {
                        skipTurns = Globals.SLAVE_COAST_WAIT_TIME;
                    }
                }
            }
        }
    }

    private void tryToWinToken()
    {
        Node temp = locations.get(location);
        if (temp.hasTreasure())
        {
            if ((int) (Math.random() * 6 + 1) > 3)
            {
                openToken();
            }
        }
        endTurn = true;
    }

    public static void resetID()
    {
        idCount = 0;
    }

    public int getLocation()
    {
        return location;
    }

    public int getCashBalance()
    {
        return cashBalance;
    }

    public boolean isHasStar()
    {
        return hasStar;
    }

    public boolean isHasHorseshoeAfterStar()
    {
        return hasHorseshoeAfterStar;
    }

    public boolean isInSahara()
    {
        return inSahara;
    }

    public boolean isInPirates()
    {
        return inPirates;
    }

    public boolean isFoundCapeTown()
    {
        return foundCapeTown;
    }

    public boolean isWinner()
    {
        return isWinner;
    }

    public boolean isStayInCity()
    {
        return stayInCity;
    }

    public int getDice()
    {
        if (useLandOrSea)
        {
            return dice;
        } else
        {
            return 0;
        }
    }

    private boolean inCity()
    {
        Node current = locations.get(this.location);
        return (current.getTYPE() == NodeType.CITY
                || current.getTYPE() == NodeType.CAIRO
                || current.getTYPE() == NodeType.TANGIER
                || current.getTYPE() == NodeType.GOLD_COAST
                || current.getTYPE() == NodeType.SLAVE_COAST
                || current.getTYPE() == NodeType.CAPE_TOWN);
    }

    public boolean isEndTurn()
    {
        return endTurn;
    }

    public Node getNode(int nodeID)
    {
        return locations.get(nodeID);
    }

    // Decision
    public void decideToTryToken()
    {
        if (getCurrentNode().hasTreasure())
        {
            if (!usePlane && !useLandOrSea)
            {
                tryToWinToken = true;
                tryToWinToken();
            }
        }
    }

    // Decision
    public void decidetoUsePlane()
    {
        if (!getCurrentNode().getPlaneConnections().isEmpty() && cashBalance >= Globals.PLANE_ROUTE_PRICE)
        {
            if (!tryToWinToken && !useLandOrSea)
            {
                usePlane = true;
            }
        }
    }

    // Decision
    public void decideToUseLandOrSeaRoute()
    {
        if (!tryToWinToken && !usePlane)
        {
            useLandOrSea = true;
        }
    }

    // Land, sea, or plane
    public void moveTo(Route destination)
    {
        if (useLandOrSea || usePlane)
        {

            if (!moved)
            {
                if (usePlane)
                {
                    if (!getCurrentNode().getPlaneRoutes().contains(destination))
                    {
                        System.out.println("Decided to use plane, tried to use non-plane route. Returning...");
                        return;
                    }
                }
                if (useLandOrSea)
                {
                    if (getCurrentNode().getPlaneRoutes().contains(destination))
                    {
                        System.out.println("Decided to use landOrSea, tried to use plane route. Returning...");
                        return;
                    }
                }

                if (controller.getMyAvailableRoutes().contains(destination) || controller.getMyAvailableFreeRoutes().contains(destination))
                {

                    if (getCurrentNode().getFreeSeaRoutes() != null)
                    {
                        if (getCurrentNode().getFreeSeaRoutes().contains(destination))
                        {
                            usingFreeSeaRoute = true;
                        }
                    }

                    Node target = destination.getDestination();
                    cashBalance -= destination.getPrice();
                    if (target.TYPE == NodeType.ROUTE)
                    {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        usingFreeSeaRoute = false;
                    }
                    if (target.TYPE == NodeType.CITY)
                    {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        usingFreeSeaRoute = false;

                    }
                    if (target.TYPE == NodeType.CAPE_TOWN)
                    {
                        this.location = destination.getDestination().ID;
                        usingFreeSeaRoute = false;
                        moved = true;
                        if (PublicInformation.isCapeTownBonus())
                        {
                            cashBalance += Globals.CAPE_TOWN_BONUS;;
                            foundCapeTown = true;
                        }
                    }
                    if (target.TYPE == NodeType.CAIRO)
                    {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        usingFreeSeaRoute = false;
                        if (hasHorseshoeAfterStar || hasStar)
                        {
                            isWinner = true;
                        }

                    }
                    if (target.TYPE == NodeType.TANGIER)
                    {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        usingFreeSeaRoute = false;
                        if (hasHorseshoeAfterStar || hasStar)
                        {
                            isWinner = true;
                        }
                    }
                    if (target.TYPE == NodeType.GOLD_COAST)
                    {
                        this.location = destination.getDestination().ID;
                        usingFreeSeaRoute = false;
                        moved = true;

                    }
                    if (target.TYPE == NodeType.SLAVE_COAST)
                    {
                        this.location = destination.getDestination().ID;
                        moved = true;
                        usingFreeSeaRoute = false;
                    }
                    if (target.TYPE == NodeType.SEA_ROUTE)
                    {
                        this.location = destination.getDestination().ID;
                        moved = true;
                    }
                    if (target.TYPE == NodeType.SAHARA)
                    {
                        this.location = destination.getDestination().ID;
                        inSahara = true;
                        usingFreeSeaRoute = false;
                        moved = true;
                    }
                    if (target.TYPE == NodeType.PIRATES)
                    {
                        this.location = destination.getDestination().ID;
                        inPirates = true;
                        moved = true;
                    }
                } else
                {

                    System.out.println("Did not move in the moveTo Method.");
                }
            }
        }
    }

    public void buyToken()
    {
        if (!endTurn && (!tryToWinToken && !usePlane && !useLandOrSea) || moved)
        {
            Node temp = locations.get(location);
            if (temp.hasTreasure())
            {
                if (cashBalance >= Globals.TREASURE_BUYING_PRICE)
                {
                    cashBalance -= Globals.TREASURE_BUYING_PRICE;
                    openToken();
                } else
                {
                    //  System.out.println("Not enough money to buy a token.");
                }
            } else
            {
                //System.out.println("Not possible to buy token: no token available.");
            }
        }
    }

    public void endTurn()
    {
        if (moved)
        {
            endTurn = true;
        }
    }

    boolean hasMoved()
    {
        return moved;
    }

    public void setDebugString(String debugString)
    {
        this.debugString = debugString;
    }

    public void resetDebugString()
    {
        debugString = "";
    }

    public void concatDebugString(String debugString)
    {
        this.debugString = this.debugString.concat(debugString);
    }

    public String getDebugString()
    {
        return debugString;
    }

    public void forceEndTurn()
    {
        endTurn = true;
    }

    public float getR()
    {
        return r;
    }

    public float getG()
    {
        return g;
    }

    public float getB()
    {
        return b;
    }

    public String getName()
    {
        return name;
    }

    public boolean isTryToWinToken()
    {
        return tryToWinToken;
    }

    public boolean isUsePlane()
    {
        return usePlane;
    }

    public boolean isUseLandOrSea()
    {
        return useLandOrSea;
    }

    public void draw()
    {
        int size = 20;
        for (DrawNode drawNode : ai.getDrawList())
        {
            Node node = locations.get(drawNode.nodeID);
            glBegin(GL_QUADS);
            GL11.glColor3f(drawNode.r, drawNode.g, drawNode.b);
            glVertex2f(-size / 2f + node.x, -size / 2f + node.y);
            glVertex2f(size / 2f + node.x, -size / 2f + node.y);
            glVertex2f(size / 2f + node.x, size / 2f + node.y);
            glVertex2f(-size / 2f + node.x, size / 2f + node.y);
            glEnd();
        }
        glEnable(GL_TEXTURE_2D);
    }

    public void resetCash()
    {
        cashBalance = 0;
    }

    public boolean isUsingFreeSeaRoute()
    {
        return usingFreeSeaRoute;
    }


}
