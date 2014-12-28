/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Node;
import freeat.Route;
import freeat.PublicInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

// Paranormal AI's own imports.
import freeat.ai.paranormalai.*;

/**
 *
 * @author antti
 */
public class ParanormalAI extends AI
{

    // class-level (static) variables.
    static String paranormalAIStartDateString;
    static boolean isParanormalAIStartDateStringReady = false;
    static boolean isParanormalNodeHashMapReady = false;
    static boolean isDistanceDataReady = false;
    boolean isFilenameDefined = false;

    static DateFormat dateFormat = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
    static Date date = new Date();
    String filename;
    String absFilename;
    static String logDirectory = "/home/antti/paranormalai_log/";
    // FileWriter fw;
    BufferedWriter bw;

    static int startingCity = (int) (Math.random() * 2 + 1); // random starting city for 1st object of this class.

    //                                        pink, cyan,  red, green, blue, purple.
    static final float[] REDVALUESARRAY =
    {
        1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f
    };
    static final float[] GREENVALUESARRAY =
    {
        0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f
    };
    static final float[] BLUEVALUESARRAY =
    {
        1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f
    };

    @Override
    public String getName()
    {
        return "ParanormalAI";
    }

    @Override
    public float getR()
    {
        if (c.getMyID() >= REDVALUESARRAY.length)
        {
            return REDVALUESARRAY[0];
        }
        else
        {
            return REDVALUESARRAY[c.getMyID()];
        }
    }

    @Override
    public float getG()
    {
        if (c.getMyID() >= GREENVALUESARRAY.length)
        {
            return GREENVALUESARRAY[0];
        }
        else
        {
            return GREENVALUESARRAY[c.getMyID()];
        }
    }

    @Override
    public float getB()
    {
        if (c.getMyID() >= BLUEVALUESARRAY.length)
        {
            return BLUEVALUESARRAY[0];
        }
        else
        {
            return BLUEVALUESARRAY[c.getMyID()];
        }
    }

    // ParanormalAI uses money as 0, 1, 2, 3, ...
    // FreetAT uses money as 0, 100, 200, 300, ...
    public static final int MONEYSCALE = 100;

    // minimum land & sea road price.
    public static final int MINROADPRICE = 0;

    // minimum land road price.
    public static final int MAXLANDROADPRICE = 0;

    // maximum land & sea road price as used by ParanormalAI.
    public static final int MAXROADPRICE = 1;

    // maximum land & sea road price as used by FreeAT.
    public static final int TRUEMAXROADPRICE = MONEYSCALE * MAXROADPRICE;

    // flight price as used by ParanormalAI.
    public static final int FLIGHTPRICE = 3;

    // true flight price as used by FreeAT.
    public static final int TRUEFLIGHTPRICE = MONEYSCALE * FLIGHTPRICE;

    // distance to a neighbor node.
    public static final int NEIGHBORDISTANCE = 1;

    // maximum price for land & sea routes as used by ParanormalAI.
    public static final int MAXLANDSEATOTALPRICE = 6;

    // maximum price for land & sea routes as used by FreeAT.
    public static final int TRUEMAXLANDSEATOTALPRICE = MONEYSCALE * MAXLANDSEATOTALPRICE;

    // initial distance from starting node (targetNode).
    public static final int INITIALDISTANCE = 0;

    // initial price from starting node (targetNode).
    public static final int INITIALPRICE = 0;

    // price to buy token for ParanormalAI.
    public static final int TOKENPRICE = 0;

    // price to buy token for FreeAT.
    public static final int TRUETOKENPRICE = MONEYSCALE * TOKENPRICE;

    // Cairo node ID.
    public static final int CAIRONODEID = 1;

    // Tangier node ID.
    public static final int TANGIERNODEID = 2;

    // metropols.
    public static int[] METROPOLSARRAY =
    {
        CAIRONODEID, TANGIERNODEID
    };

    public static HashMap<Integer, ParanormalNode> paranormalNodeHashMap = new HashMap<>();
    public static HashMap<Integer, HashMap<Integer, HashMap<Integer, ParanormalNode>>> connectionsHashMap = new HashMap<>();

    int destinationNodeID;
    int oldNodeID;
    int newNodeID;

    int targetMetropolID = METROPOLSARRAY[(int) (Math.random())];

    PrintWriter writer = null;

    public ParanormalAI()
    {
        super(startingCity); // Call the constructor and set the random start city (1 or 2).
        AI.AIIdentifications.add("ParanormalAI");
        startingCity = (int) (Math.random() * 2 + 1); // Random starting city for next object of this class (must be done here).
    }

    @Override
    public void act()
    {
        while (!(c.isEndTurn()))
        {
            if (!(isParanormalAIStartDateStringReady))
            {
                paranormalAIStartDateString = dateFormat.format(date); // eg. `2014_12_23_19_05_22`.
                isParanormalAIStartDateStringReady = true;             // Date string is defined once for each FreeAT execution.
            }

            if (!(isFilenameDefined))
            {
                filename = "ParanormalAI_log_id" + c.getMyID() + paranormalAIStartDateString + ".txt"; // eg. `ParanormalAI_log_id_1_2014_12_23_19_05_22.txt`.
                absFilename = logDirectory + filename;
                isFilenameDefined = true;
            }

            try
            {
                // fw = new FileWriter(absFilename, true);
                // fw = new FileWriter(new File(logDirectory, filename));
                bw = new BufferedWriter(new FileWriter(absFilename, true));
            }
            catch (IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }

            writeTextAndNewlineToLog("log file " + absFilename + " is opened.");

            if (!(isParanormalNodeHashMapReady))
            {
                for (int nodeID : c.getNodeList().keySet())
                {
                    paranormalNodeHashMap.put(nodeID, new ParanormalNode(c.getNode(nodeID)));
                }
                isParanormalNodeHashMapReady = true;
            }

            if (!(isDistanceDataReady))
            {
                long startTime = System.nanoTime();

                for (int currentMaxTotalPrice = 0; currentMaxTotalPrice <= MAXLANDSEATOTALPRICE; currentMaxTotalPrice++)
                {
                    for (int targetNodeID : c.getNodeList().keySet())
                    {
                        writeTextAndNewlineToLog(
                                "computing routes to " + c.getNode(targetNodeID).getName() + ".");
                        createConnectionsHashMap(targetNodeID, currentMaxTotalPrice);
                    }
                }
                writeTextAndNewlineToLog("Time to compute connections hash map: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
                isDistanceDataReady = true;
            }

            // Main game loop begins here.
            // The different situations that I as an AI need to handle:
            // #1 I am eligible for win (someone else may be eligible too).
            // #2 I am not eligible for win, but someone else is.
            // #3 No one is eligible for win and I have no money.
            // #4 No one is eligible for win and I do have money.
            // #5 Hopeless situation: I have no money and there are no treasures.
            if (c.isEligibleForWin())
            {
                writeTextAndNewlineToLog("situation #1: I am eligible for win.");

                // Situation #1.
                // Great, I have Africa's star or a horse shoe found after Africa's star.
                // Check which is closest land & sea destination, Cairo or Tangier.
                int shortestDistanceToCairoWithCurrentMoney = getShortestDistanceWithMoney(
                        c.getCurrentNode(),
                        c.getNode(CAIRONODEID),
                        c.getMyBalance());
                int shortestDistanceToTangierWithCurrentMoney = getShortestDistanceWithMoney(
                        c.getCurrentNode(),
                        c.getNode(TANGIERNODEID),
                        c.getMyBalance());

                if (shortestDistanceToCairoWithCurrentMoney < shortestDistanceToTangierWithCurrentMoney)
                {
                    targetMetropolID = CAIRONODEID;   // Target metropol is Cairo.
                }
                else if (shortestDistanceToCairoWithCurrentMoney > shortestDistanceToTangierWithCurrentMoney)
                {
                    targetMetropolID = TANGIERNODEID; // Target metropol is Tangier.
                }
                else if (shortestDistanceToCairoWithCurrentMoney == shortestDistanceToTangierWithCurrentMoney)
                {
                    targetMetropolID = METROPOLSARRAY[(int) (Math.random())]; // Target metropol is randomly chosen between Cairo and Tangier.
                }
                else
                {
                    writeTextAndNewlineToLogAndDebug("error in Cairo-Tangier distance check.");
                    ArrayList<Route> availableRoutes = c.getAvailableRoutes(c.getCurrentNode(), 100, c.getDice());
                }
                doLandTravelTowards(c.getNode(targetMetropolID), c.getDice());
            }
            else if (isAnyOpponentEligibleForWin())
            {
                writeTextAndNewlineToLog("situation #2: some else is eligible for win but not me.");
                // Situation #2.
                // Someone else is eligible for win and I am not.
                // Play using best-case scenario for me.
                doRandomUsefulLandMovement();
            }
            else if (getCash() < TOKENPRICE)
            {
                writeTextAndNewlineToLog("situation #3: no one is eligible for treasure and I have no cash.");

                // Situation #3.
                // No one is eligible for win and I have no cash.
                // Check if I am on top of a token.
                if (c.getCurrentNode().hasTreasure())
                {
                    writeTextAndNewlineToLog("situation #3 continues: my node has a treasure!");
                    c.decideTryToken();
                }
                else if (c.totalTreasures() >= 1)
                {
                    writeTextAndNewlineToLog("situation #3 continues: there are treasures available!");

                    int dice = c.getDice();

                    ArrayList<Node> treasuresArrayList = c.getRemainingTreasures();

                    int minimumDistanceToTreasure = -1;
                    int chosenTreasureNodeID = -1;

                    for (Node treasureNode : treasuresArrayList)
                    {
                        int shortestLandDistanceToCurrentTreasureNode = getShortestLandDistance(treasureNode);

                        if ((chosenTreasureNodeID < 0) || (minimumDistanceToTreasure > shortestLandDistanceToCurrentTreasureNode))
                        {
                            minimumDistanceToTreasure = shortestLandDistanceToCurrentTreasureNode;
                            chosenTreasureNodeID = treasureNode.ID;
                        }
                    }
                    doLandTravelTowards(c.getNode(chosenTreasureNodeID), dice);
                }
                else
                {
                    // Situation #5.
                    // Hopeless situation: I have no money and there are no treasures.
                    writeTextAndNewlineToLog("situation #5: hopeless situation!");

                    // Hopeless situation: I have no money and there are no treasures.
                    // Win is not possible, so just do some random land movement.
                    doRandomLandMovement();
                }
            }
            else
            {
                writeTextAndNewlineToLog("situation #4");

                // #4 No one is eligible for win and I do have money.
                doRandomUsefulLandMovement();
                buyTokenIfItMayBeUseful();
            }
            oldNodeID = newNodeID;

            c.endTurn();

            try
            {
                bw.close();
            }
            catch (IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }
        writeTextAndNewlineToLog("log file " + absFilename + " will be closed next.");

    }

    /*-------------------------------------------------------------------------------------------*/
    private void writeTextToLog(String text)
    {
        try
        {
            bw.write(text);
        }
        catch (IOException ioe)
        {
            System.err.println("IOexception: " + ioe.getMessage());
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    private void writeTextAndNewlineToLog(String text)
    {
        writeTextToLog(text + "\n");
    }

    /*-------------------------------------------------------------------------------------------*/
    private void writeTextAndNewlineToLogAndDebug(String text)
    {
        writeTextAndNewlineToLog(text);
        c.setDebugString(text);
    }

    /*-------------------------------------------------------------------------------------------*/
    private ArrayList<Route> removeRoutesIfNoTreasure(ArrayList<Route> routesArrayList)
    // Returns a shallow copy of `routesArrayList`.
    // The routes stored in the returned ArrayList are still the same as original,
    // so they must not be modified!!!
    {
        ArrayList<Route> newArrayList = (ArrayList<Route>) routesArrayList.clone(); // shallow copy!!!

        for (Route route : newArrayList)
        {
            if (!route.getDestination().hasTreasure())
            {
                routesArrayList.remove(route);
            }
        }
        return newArrayList; // return the new ArrayList.
    }

    /*-------------------------------------------------------------------------------------------*/
    private void createConnectionsHashMapWithAccumulatedPrice(
            Node originNode,
            Node targetNode,
            int cumulativeDistance,
            int cumulativePrice,
            int currentMaxTotalPrice)
    {
        writeTextAndNewlineToLog(
                "originNode: " + originNode.getName()
                + ", targetNode: " + targetNode.getName()
                + ", cumulativeDistance: " + cumulativeDistance
                + ", cumulativePrice: " + cumulativePrice
                + ", currentMaxTotalPrice: " + currentMaxTotalPrice);

        if (!(connectionsHashMap.containsKey(originNode.ID)))
        {
            connectionsHashMap.put(originNode.ID, new HashMap<Integer, HashMap<Integer, ParanormalNode>>());
        }

        if (!(connectionsHashMap.get(originNode.ID).containsKey(targetNode.ID)))
        {
            connectionsHashMap.get(originNode.ID).put(
                    targetNode.ID,
                    new HashMap<>());
        }

        if (!(connectionsHashMap.get(originNode.ID).get(targetNode.ID).containsKey(currentMaxTotalPrice)))
        {
            connectionsHashMap.get(originNode.ID).get(targetNode.ID).put(
                    currentMaxTotalPrice,
                    paranormalNodeHashMap.get(originNode.ID));
        }

        ParanormalNode pNode = connectionsHashMap.get(originNode.ID).get(targetNode.ID).get(currentMaxTotalPrice);
        pNode.setNode(originNode);
        pNode.setDistanceToTarget(cumulativeDistance); // distance from target city (such as Cairo or Tangier!).
        pNode.setPriceFromTarget(cumulativePrice);       // cumulative price from target city (such as Cairo or Tangier!).
        pNode.setNeighbors(originNode.getConnections()); // neighbor nodes.

        // side effect: populates `public static HashMap<Integer, HashMap<Integer, HashMap<Integer, ParanormalNode>>> connectionsHashMap`.
        //
        // first key:  origin node.
        // second key: destination node.
        // third key:  maximum price.
        //
        // 0: start from node, cumulative price = 0.
        //    call recursively each neighboring node if distance there is negative (not yet defined) or greater than current distance.
        ArrayList<Route> routesArrayList = c.getAvailableRoutes(
                originNode,
                TRUEMAXROADPRICE,
                NEIGHBORDISTANCE); // ArrayList of neighboring nodeID's.

        cumulativeDistance++;

        for (Route route : routesArrayList)
        {
            Node neighborNode = route.getDestination();
            ParanormalNode neighborParanormalNode = getParanormalNode(neighborNode);

            boolean isUpdateNeeded;
            int newCumulativePrice = cumulativePrice + (route.getPrice() / MONEYSCALE);

            if (neighborParanormalNode.getDistanceToTarget() < 0)
            {
                // if there is no distance, an update is needed.
                isUpdateNeeded = true;
            }
            else if (cumulativeDistance < neighborParanormalNode.getDistanceToTarget())
            {
                // if new route is shorter than old one, an update is needed.
                isUpdateNeeded = true;
            }
            else if ((cumulativeDistance == neighborParanormalNode.getDistanceToTarget()) && (newCumulativePrice < neighborParanormalNode.getPriceFromTarget()))
            {
                // if new route has same length as old one but is cheaper, an update is needed.
                isUpdateNeeded = true;
            }
            else
            {
                isUpdateNeeded = false;
            }

            // if (isUpdateNeeded && (newCumulativePrice <= currentMaxTotalPrice))
            if (isUpdateNeeded)
            {
                createConnectionsHashMapWithAccumulatedPrice( // call recursively.
                        neighborNode, // neighbor node as the new origin node.
                        targetNode, // keep the same target node.
                        cumulativeDistance, // cumulative distance has already been incremented.
                        newCumulativePrice, // new cumulative price.
                        currentMaxTotalPrice);                    // keep original max total price.
            }
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    private ParanormalNode getParanormalNode(Node node)
    {
        return paranormalNodeHashMap.get(node.ID);
    }

    /*-------------------------------------------------------------------------------------------*/
    private ParanormalNode getParanormalNode(int nodeID)
    {
        return paranormalNodeHashMap.get(nodeID);
    }

    /*-------------------------------------------------------------------------------------------*/
    private void createConnectionsHashMap(
            Node targetNode,
            int currentMaxTotalPrice)
    {
        createConnectionsHashMapWithAccumulatedPrice(
                targetNode,
                targetNode,
                INITIALDISTANCE,
                INITIALPRICE,
                currentMaxTotalPrice);
    }

    /*-------------------------------------------------------------------------------------------*/
    private void createConnectionsHashMap(
            int targetNodeID,
            int currentMaxTotalPrice)
    {
        createConnectionsHashMapWithAccumulatedPrice(
                c.getNode(targetNodeID),
                c.getNode(targetNodeID),
                INITIALDISTANCE,
                INITIALPRICE,
                currentMaxTotalPrice);
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestDistanceWithMoney(Node originNode, Node targetNode, int cash)
    // Read the distance from populated ArrayList connectionsHashMap<ParanormalNode>[][]
    {
        return connectionsHashMap.get(originNode.ID).get(targetNode.ID).get(cash).getDistanceToTarget();
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestDistanceWithCurrentMoney(Node originNode, Node targetNode)
    // shortest distance with current cash between two nodes.
    {
        return connectionsHashMap.get(originNode.ID).get(targetNode.ID).get(getCash()).getDistanceToTarget();
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestDistanceWithCurrentMoney(Node targetNode)
    // shortest distance with current cash from _current_ node to _target_ node.
    {
        return getShortestDistanceWithMoney(c.getCurrentNode(), targetNode, getCash());
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestLandDistance(Node originNode, Node targetNode)
    // shortest only-land distance between two nodes.
    {
        return connectionsHashMap.get(originNode.ID).get(targetNode.ID).get(MAXLANDROADPRICE).getDistanceToTarget();
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestLandSeaDistance(Node originNode, Node targetNode)
    // shortest land & sea distance between two nodes.
    {
        return connectionsHashMap.get(originNode.ID).get(targetNode.ID).get(MAXLANDSEATOTALPRICE).getDistanceToTarget();
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node originNode, Node targetNode)
    // shortest only-flight distance between two nodes.
    // TODO: write the code!
    {
        return -1;
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestLandDistance(Node targetNode)
    // shortest only-land distance from current node to node.
    {
        return getShortestLandDistance(c.getCurrentNode(), targetNode);
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node targetNode)
    // shortest only-land distance from current node to node.
    {
        return getShortestAirDistance(c.getCurrentNode(), targetNode);
    }

    /*-------------------------------------------------------------------------------------------*/
    private void printMyItinerary(int oldNodeID, int newNodeID, int destinationNodeID)
    {
        writeTextAndNewlineToLogAndDebug("coming from " + oldNodeID + ", going to " + newNodeID + ", en route to " + destinationNodeID + ".");
    }

    /*-------------------------------------------------------------------------------------------*/
    private void printMyItinerary(int oldNodeID, int newNodeID)
    {
        writeTextAndNewlineToLogAndDebug("coming from " + oldNodeID + ", going to " + newNodeID + ".");
    }

    /*-------------------------------------------------------------------------------------------*/
    private int getCash()
    {
        return c.getMyBalance() / MONEYSCALE;
    }

    /*-------------------------------------------------------------------------------------------*/
    private void doLandTravelTowards(Node targetNode, int dice)
    {
        ArrayList<Route> routesArrayList = c.getAvailableRoutes(c.getCurrentNode(), MAXLANDROADPRICE, dice);

        if (!(routesArrayList.isEmpty()))
        {
            Route chosenRoute = routesArrayList.get(0);
            int distanceToTargetFromChosenNode = -1;

            for (Route currentRoute : routesArrayList)
            {
                Node routeNode = currentRoute.getDestination();

                boolean isUpdateNeeded = false;

                if (distanceToTargetFromChosenNode < 0)
                {
                    isUpdateNeeded = true;
                }

                else if (distanceToTargetFromChosenNode > getShortestLandDistance(routeNode, targetNode))
                {
                    isUpdateNeeded = true;
                }

                if (isUpdateNeeded)
                {
                    chosenRoute = currentRoute;
                    distanceToTargetFromChosenNode = getShortestLandDistance(routeNode, targetNode);
                }
            }

            if (distanceToTargetFromChosenNode < 0)
            {
                doRandomLandMovement();
            }
            else
            {
                writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName()
                                                 + ", go to " + chosenRoute.getDestination().getName()
                                                 + ", en route to " + targetNode.getName() + ".");
                c.moveTo(chosenRoute); // Do the movement.
            }
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    private void doLandTravelTowards(int targetNodeID, int dice)
    {
        doLandTravelTowards(c.getNode(targetNodeID), dice);
    }

    /*-------------------------------------------------------------------------------------------*/
    private void doLandTravelTowardsClosestTreasure()
    {
    }

    /*-------------------------------------------------------------------------------------------*/
    private boolean isAnyOpponentEligibleForWin()
    {
        boolean anyOpponentEligibleForWin = false;

        for (int playerID = 0; playerID < PublicInformation.PLAYER_COUNT; playerID++)
        {
            if ((c.isEligibleForWin(playerID)) && (playerID != c.getMyID()))
            {
                anyOpponentEligibleForWin = true;
            }
        }
        return anyOpponentEligibleForWin;
    }

    /*-------------------------------------------------------------------------------------------*/
    public boolean isAnyoneEligibleForWin()
    {
        return (c.isEligibleForWin() || isAnyOpponentEligibleForWin());
    }

    /*-------------------------------------------------------------------------------------------*/
    private void doRandomLandMovement()
    {
        c.decideToUseLandOrSeaRoute();

        if (c.getMyAvailableRoutes().isEmpty())
        {
            writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName() + " and I have no available routes.");
        }
        else
        {
            Route route = c.getMyAvailableRoutes().get(0);
            writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName() + " and I take the first available route to " + route.getDestination().getName() + ".");
            c.moveTo(route);
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    private void moveTowardsClosestTreasure()
    {
        c.decideToUseLandOrSeaRoute();

        int dice = c.getDice();
        int shortestDistanceToTreasureCity = -1;

        ArrayList<Route> routesArrayList = c.getMyAvailableRoutes();
        ArrayList<Node> treasureCitiesArrayList = c.getRemainingTreasures();
        Route chosenRoute = null;

        for (Route route : routesArrayList)
        {
            for (Node treasureCity : treasureCitiesArrayList)
            {
                int distanceFromDestinationToTreasureCity = getShortestLandDistance(route.getDestination(), treasureCity);
                boolean isUpdateNeeded;

                if (shortestDistanceToTreasureCity < 0)
                {
                    isUpdateNeeded = true;
                }
                else if ((distanceFromDestinationToTreasureCity >= 0) && (shortestDistanceToTreasureCity > distanceFromDestinationToTreasureCity))
                {
                    isUpdateNeeded = true;
                }
                else
                {
                    isUpdateNeeded = false;
                }
                if (isUpdateNeeded)
                {
                    chosenRoute = route;
                    shortestDistanceToTreasureCity = distanceFromDestinationToTreasureCity;
                }
            }
        }

        if (chosenRoute == null)
        {
            doRandomUsefulLandMovement();
        }
        else
        {
            c.moveTo(chosenRoute);
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    private void doRandomUsefulLandMovement()
    {
        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList = c.getMyAvailableRoutes();

        if (!(routesArrayList.isEmpty()))
        {
            writeTextAndNewlineToLog("doing random useful land movement.");

            Route chosenRouteWithTreasure = null;

            for (Route route : routesArrayList)
            {
                if (route.getDestination().hasTreasure())
                {
                    chosenRouteWithTreasure = route;
                }
            }
            if (chosenRouteWithTreasure == null)
            {
                moveTowardsClosestTreasure();
            }
            else
            {
                c.moveTo(chosenRouteWithTreasure);
            }
        }
        else
        {
            doLandTravelTowards(c.getNode(targetMetropolID), c.getDice());
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    private boolean areThereValuableTreasuresLeft()
    {
        return ((c.rubiesLeft() + c.emeraldsLeft() + c.topazesLeft()) > 0);
    }

    /*-------------------------------------------------------------------------------------------*/
    private void buyTokenIfItMayBeUseful()
    {
        // Different situations in which buying a token may be useful:
        // #1 No one has Africa's star.
        // #2 I am eligible for win, and there are valuable treasures left.
        // #3 Some opponent has Africa's star, and there are horseshoes left.

        if (!(isAnyoneEligibleForWin()))
        {
            // Situation #1. No one has Africa's star.
            c.buyToken();
        }
        else if (c.isEligibleForWin())
        {
            // Situation #2 I am eligible for win, and there are valuable treasures left.
            if (areThereValuableTreasuresLeft())
            {
                c.buyToken();
            }
        }
        else if (isAnyOpponentEligibleForWin())
        {
            // Situation #3 Some opponent has Africa's star, and there are horseshoes left.
            if (c.horseShoesLeft() > 0)
            {
                c.buyToken();
            }
        }
    }

    /*-------------------------------------------------------------------------------------------*/
}
