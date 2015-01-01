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
    static boolean isLoggingInUse = false;
    static boolean shouldConnectionHashMapGenerationBeLogged = false;

    static String paranormalAIStartDateString;
    static boolean isParanormalAIStartDateStringReady = false;
    static boolean isParanormalNodeHashMapReady = false;
    static boolean isDistanceDataReady = false;
    static boolean isLeaderIDDefined = false;
    boolean isFilenameDefined = false;

    static DateFormat dateFormat = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
    static Date date = new Date();
    String filename;
    String absFilename;
    static String logDirectory = "/home/antti/paranormalai_log/";
    BufferedWriter bw;

    static int startingCity = (int) (Math.random() * 2 + 1); // random starting city for 1st object of this class.

    // needed for updating ParanormalAI's internal turn number.
    static int leaderID;
    static int turnNumber;

    // zeitgebers.
    static Node lastLeaderNode;
    static boolean isStarFound;
    static int rubiesLeft;
    static int emeraldsLeft;
    static int topazesLeft;
    static int horseShoesLeft;
    static int emptyLeft;
    static int robbersLeft;
    static int unopenedLeft;

    // pink, cyan, red, green, blue, purple.
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
    public static final int MONEY_SCALE = 100;

    // minimum land & sea road price.
    public static final int MIN_ROAD_PRICE = 0;

    // minimum land road price.
    public static final int MAX_LAND_ROAD_PRICE = 0;

    // maximum land & sea road price as used by ParanormalAI.
    public static final int MAX_ROAD_PRICE = 1;

    // maximum land & sea road price as used by FreeAT.
    public static final int TRUE_MAX_ROAD_PRICE = MONEY_SCALE * MAX_ROAD_PRICE;

    // flight price as used by ParanormalAI.
    public static final int FLIGHT_PRICE = 3;

    // true flight price as used by FreeAT.
    public static final int TRUE_FLIGHT_PRICE = MONEY_SCALE * FLIGHT_PRICE;

    // distance to a neighbor node.
    public static final int NEIGHBOR_DISTANCE = 1;

    // maximum price for land & sea routes as used by ParanormalAI.
    public static final int MAX_LAND_SEA_TOTAL_PRICE = 6;

    // maximum price for land & sea routes as used by FreeAT.
    public static final int TRUE_MAX_LAND_SEA_TOTAL_PRICE = MONEY_SCALE * MAX_LAND_SEA_TOTAL_PRICE;

    // initial distance from starting node (targetNode).
    public static final int INITIAL_DISTANCE = 0;

    // initial price from starting node (targetNode).
    public static final int INITIAL_PRICE = 0;

    // price to buy token for ParanormalAI.
    public static final int TOKEN_PRICE = 1;

    // price to buy token for FreeAT.
    public static final int TRUE_TOKEN_PRICE = MONEY_SCALE * TOKEN_PRICE;

    // minimum roll of 1d6.
    public static final float DICE_MIN = 1.0f;

    // maximum roll of 1d6.
    public static final float DICE_MAX = 6.0f;

    // average roll of 1d6.
    public static final float DICE_AVG = (DICE_MIN + DICE_MAX) / 2;

    // Cairo node ID.
    public static final int CAIRO_NODE_ID = 1;

    // Tangier node ID.
    public static final int TANGIER_NODE_ID = 2;

    // metropols.
    public static int[] METROPOLS_ARRAY =
    {
        CAIRO_NODE_ID, TANGIER_NODE_ID
    };

    public static HashMap<Integer, ParanormalNode> paranormalNodeHashMap = new HashMap<>();
    public static HashMap<Integer, HashMap<Integer, HashMap<Integer, ParanormalNode>>> connectionsHashMap = new HashMap<>();

    int destinationNodeID;

    int targetMetropolID = METROPOLS_ARRAY[(int) (Math.random())];

    public ParanormalAI()
    {
        super(startingCity); // Call the constructor and set the random start city (1 or 2).
        AI.AIIdentifications.add("ParanormalAI");
        startingCity = (int) (Math.random() * 2 + 1); // Random starting city for next object of this class (must be done here).
    }

    @Override
    public void act()
    {
        if (!(isLeaderIDDefined))
        {
            leaderID = c.getMyID();
            turnNumber = 0;
            isLeaderIDDefined = true;
        }

        if (isNewGame())
        {
            turnNumber = 0;
        }

        if (c.getMyID() == leaderID)
        {
            turnNumber++; // gets updated from 0 to 1 immediately.
        }

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

        if (isLoggingInUse)
        {
            try
            {
                bw = new BufferedWriter(new FileWriter(absFilename, true));
            }
            catch (IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
            writeTextAndNewlineToLog("log file " + absFilename + " is opened.");
        }

        while (!(c.isEndTurn()))
        {
            if (!(isParanormalNodeHashMapReady))
            {
                for (int originNodeID : c.getNodeList().keySet())
                {
                    paranormalNodeHashMap.put(originNodeID, new ParanormalNode(c.getNode(originNodeID), c));
                }
                isParanormalNodeHashMapReady = true;
            }

            if (!(isDistanceDataReady))
            {
                long startTime = System.nanoTime();

                for (int currentMaxTotalPrice = 0; currentMaxTotalPrice <= MAX_LAND_SEA_TOTAL_PRICE; currentMaxTotalPrice++)
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
            // #3 No one is eligible for win and I do have money.
            // #4 No one is eligible for win and I have no money.
            // #5 Hopeless situation: There are no treasures.
            if (c.isEligibleForWin())
            {
                writeTextAndNewlineToLog("situation #1: I am eligible for win.");

                // Situation #1.
                // Great, I have Africa's star or a horse shoe found after Africa's star.
                // Check which is closest land & sea destination, Cairo or Tangier.
                int shortestDistanceToCairoWithCurrentMoney = getShortestDistanceWithCurrentMoney(
                    c.getCurrentNode(),
                    c.getNode(CAIRO_NODE_ID));
                int shortestDistanceToTangierWithCurrentMoney = getShortestDistanceWithCurrentMoney(
                    c.getCurrentNode(),
                    c.getNode(TANGIER_NODE_ID));

                if (shortestDistanceToCairoWithCurrentMoney < shortestDistanceToTangierWithCurrentMoney)
                {
                    targetMetropolID = CAIRO_NODE_ID;   // Target metropol is Cairo.
                }
                else if (shortestDistanceToCairoWithCurrentMoney > shortestDistanceToTangierWithCurrentMoney)
                {
                    targetMetropolID = TANGIER_NODE_ID; // Target metropol is Tangier.
                }
                else if (shortestDistanceToCairoWithCurrentMoney == shortestDistanceToTangierWithCurrentMoney)
                {
                    targetMetropolID = METROPOLS_ARRAY[(int) (Math.random())]; // Target metropol is randomly chosen between Cairo and Tangier.
                }
                else
                {
                    writeTextAndNewlineToLogAndDebug("error in Cairo-Tangier distance check.");
                    ArrayList<Route> routesArrayList;
                    // routesArrayList = c.getAvailableRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), 100), c.getDice());
                    routesArrayList = c.getMyAvailableRoutes();
                    if (routesArrayList.isEmpty())
                    {
                        doEndTurn();
                    }
                    else
                    {
                        executeRoute(routesArrayList.get(0));
                        doEndTurn();
                    }
                }

                doLandTravelTowards(targetMetropolID);
                buyTokenIfItMayBeUseful();
                doEndTurn();
            }
            else if (isAnyOpponentEligibleForWin())
            {
                writeTextAndNewlineToLog("situation #2: some else is eligible for win but not me.");

                // Situation #2.
                // Someone else is eligible for win and I am not.
                // Play using best-case scenario for me.
                doUsefulLandMovement();
                buyTokenIfItMayBeUseful();
                doEndTurn();
            }
            else if (getCash() >= TOKEN_PRICE)
            {
                writeTextAndNewlineToLog("situation #3: No one is eligible for win and I do have money.");

                // Situation #3.
                // No one is eligible for win and I do have money.
                doUsefulLandMovement();
                buyTokenIfItMayBeUseful();
                doEndTurn();
            }
            else
            {
                writeTextAndNewlineToLog("situation #4: no one is eligible for treasure and I have no cash.");

                // Situation #4.
                // No one is eligible for win and I have no cash.
                // Check if I am on top of a token.
                if (c.getCurrentNode().hasTreasure())
                {
                    writeTextAndNewlineToLog("situation #4 continues: my node has a treasure!");
                    c.decideTryToken();
                    doEndTurn();
                }
                else if (c.totalTreasures() >= 1)
                {
                    writeTextAndNewlineToLog("situation #4 continues: there are treasures available!");
                    moveTowardsClosestTreasure();
                    doEndTurn();
                }
                else
                {
                    // Situation #5.
                    // Hopeless situation: There are no treasures.
                    writeTextAndNewlineToLog("situation #5: hopeless situation!");

                    // Hopeless situation: There are no treasures.
                    // Win is not possible, so just do some random land movement.
                    doRandomLandMovement();
                    doEndTurn();
                }
            }
            c.endTurn();
        }

        updateZeitgebers();
        writeTextAndNewlineToLog("log file " + absFilename + " will be closed next.");

        if (isLoggingInUse)
        {
            try
            {
                bw.close();
            }
            catch (IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }
    }

    /*------------------------------------------------------------------------*/
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

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Route data generation methods.                                          |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void createConnectionsHashMapWithAccumulatedPrice(
        Node originNode,
        Node targetNode,
        int cumulativeDistance,
        int cumulativePrice,
        int currentMaxTotalPrice)
    {
        if (shouldConnectionHashMapGenerationBeLogged)
        {
            writeTextAndNewlineToLog(
                "originNode: " + originNode.getName()
                + ", targetNode: " + targetNode.getName()
                + ", cumulativeDistance: " + cumulativeDistance
                + ", cumulativePrice: " + cumulativePrice
                + ", currentMaxTotalPrice: " + currentMaxTotalPrice);
        }

        if (!(connectionsHashMap.containsKey(originNode.ID)))
        {
            connectionsHashMap.put(originNode.ID, new HashMap<>());
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

        ParanormalNode pNode;
        pNode = paranormalNodeHashMap.get(originNode.ID);

        // 0: start from node, cumulative price = 0.
        //    call recursively each neighboring node if distance there is negative (not yet defined) or greater than current distance.
        ArrayList<Route> routesArrayList = c.getAvailableRoutes(
            originNode,
            2 * TRUE_MAX_ROAD_PRICE,
            NEIGHBOR_DISTANCE); // ArrayList of neighboring nodeID's.

        cumulativeDistance++;

        for (Route route : routesArrayList)
        {
            Node neighborNode = route.getDestination();
            ParanormalNode neighborParanormalNode = getParanormalNode(neighborNode);

            Route linkRoute = getLinkRoute(neighborNode, originNode);

            if (linkRoute == null)
            {
                System.out.println("error: linkRoute from " + neighborNode.getName() + " to " + originNode.getName() + " is null!");
            }

            int newCumulativePrice = (cumulativePrice + (linkRoute.getPrice() / MONEY_SCALE));

            boolean isRecursiveCallNeeded;
            isRecursiveCallNeeded = neighborParanormalNode.updateDistanceAndPrice(
                targetNode,
                currentMaxTotalPrice,
                cumulativeDistance,
                newCumulativePrice);

            if (isRecursiveCallNeeded)
            {
                createConnectionsHashMapWithAccumulatedPrice( // call recursively.
                    neighborNode, // neighbor node as the new origin node.
                    targetNode, // keep the same target node.
                    cumulativeDistance, // cumulative distance has already been incremented.
                    newCumulativePrice, // new cumulative price.
                    currentMaxTotalPrice); // keep original max total price.
            }
        }
    }

    /*------------------------------------------------------------------------*/
    private void createConnectionsHashMap(
        Node targetNode,
        int currentMaxTotalPrice)
    {
        createConnectionsHashMapWithAccumulatedPrice(
            targetNode,
            targetNode,
            INITIAL_DISTANCE,
            INITIAL_PRICE,
            currentMaxTotalPrice);
    }

    /*------------------------------------------------------------------------*/
    private void createConnectionsHashMap(
        int targetNodeID,
        int currentMaxTotalPrice)
    {
        createConnectionsHashMap(c.getNode(targetNodeID), currentMaxTotalPrice);
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Route data handling methods.                                            |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private ParanormalNode getParanormalNode(Node node)
    {
        return paranormalNodeHashMap.get(node.ID);
    }

    /*------------------------------------------------------------------------*/
    private ParanormalNode getParanormalNode(int nodeID)
    {
        return paranormalNodeHashMap.get(nodeID);
    }

    /*------------------------------------------------------------------------*/
    private Route getLinkRoute(Node originNode, Node targetNode)
    {
        ArrayList<Route> routesArrayList;
        routesArrayList = c.getAvailableRoutes(
            originNode,
            TRUE_MAX_ROAD_PRICE,
            NEIGHBOR_DISTANCE); // ArrayList of neighboring nodeID's.

        Route chosenRoute = null;

        // TODO: add check for land & sea link routes!
        for (Route route : routesArrayList)
        {
            if (route.getDestination() == targetNode)
            {
                chosenRoute = route;
            }
        }
        return chosenRoute;
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithMoney(Node originNode, Node targetNode, int cash)
    // Read the distance from populated ArrayList connectionsHashMap<ParanormalNode>[][]
    {
        return paranormalNodeHashMap.get(originNode.ID).getDistanceToTarget(targetNode, cash);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithCurrentMoney(Node originNode, Node targetNode)
    // shortest distance with current cash between two nodes.
    {
        return paranormalNodeHashMap.get(originNode.ID).getDistanceToTarget(targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithCurrentMoney(Node targetNode)
    // shortest distance with current cash from _current_ node to _target_ node.
    {
        return getShortestDistanceWithMoney(c.getCurrentNode(), targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandDistance(Node originNode, Node targetNode)
    // shortest only-land distance between two nodes.
    {
        return paranormalNodeHashMap.get(originNode.ID).getDistanceToTarget(targetNode, MAX_LAND_ROAD_PRICE);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandDistance(int originNodeID, int targetNodeID)
    // shortest only-land distance between two nodes.
    {
        return paranormalNodeHashMap.get(originNodeID).getDistanceToTarget(c.getNode(targetNodeID), MAX_LAND_ROAD_PRICE);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandSeaDistance(Node originNode, Node targetNode)
    // shortest land & sea distance between two nodes.
    {
        return paranormalNodeHashMap.get(originNode.ID).getDistanceToTarget(targetNode, MAX_LAND_SEA_TOTAL_PRICE);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandSeaDistance(int originNodeID, int targetNodeID)
    // shortest land & sea distance between two nodes.
    {
        return paranormalNodeHashMap.get(originNodeID).getDistanceToTarget(c.getNode(targetNodeID), MAX_LAND_SEA_TOTAL_PRICE);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node originNode, Node targetNode)
    // shortest only-flight distance between two nodes.
    // TODO: write the code!
    {
        return -1;
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandDistance(Node targetNode)
    // shortest only-land distance from current node to node.
    {
        return getShortestLandDistance(c.getCurrentNode(), targetNode);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node targetNode)
    // shortest only-land distance from current node to node.
    {
        return getShortestAirDistance(c.getCurrentNode(), targetNode);
    }

    /*------------------------------------------------------------------------*/
    private int getCash()
    {
        return c.getMyBalance() / MONEY_SCALE;
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Global game state reasoning methods.                                    |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void updateZeitgebers()
    {
        if ((c.getMyID() == leaderID))
        {
            lastLeaderNode = c.getCurrentNode();
            isStarFound = c.isStarFound();
            rubiesLeft = c.rubiesLeft();
            emeraldsLeft = c.emeraldsLeft();
            topazesLeft = c.topazesLeft();
            horseShoesLeft = c.horseShoesLeft();
            emptyLeft = c.emptyLeft();
            robbersLeft = c.robbersLeft();
            unopenedLeft = c.unopenedLeft();
        }
    }
    /*------------------------------------------------------------------------*/

    private boolean isNewGame()
    {
        if (c.getMyID() != leaderID)
        {
            return false;
        }
        else
        {
            return ((lastLeaderNode != c.getCurrentNode())
                    || (isStarFound && (!(c.isStarFound())))
                    || (rubiesLeft > c.rubiesLeft())
                    || (emeraldsLeft > c.emeraldsLeft())
                    || (topazesLeft > c.topazesLeft())
                    || (horseShoesLeft > c.horseShoesLeft())
                    || (emptyLeft > c.emptyLeft())
                    || (robbersLeft > c.robbersLeft())
                    || (unopenedLeft > c.unopenedLeft()));
        }
    }

    /*------------------------------------------------------------------------*/
    private boolean isAnyOpponentEligibleForWin()
    {
        for (int playerID = 0; playerID < PublicInformation.PLAYER_COUNT; playerID++)
        {
            if ((playerID != c.getMyID()) && (c.isEligibleForWin(playerID)))
            {
                return true;
            }
        }
        return false;
    }

    /*------------------------------------------------------------------------*/
    public boolean isAnyoneEligibleForWin()
    {
        return (c.isEligibleForWin() || isAnyOpponentEligibleForWin());
    }
    /*------------------------------------------------------------------------*/

    private boolean areThereValuableTreasuresLeft()
    {
        return ((c.rubiesLeft() + c.emeraldsLeft() + c.topazesLeft()) > 0);
    }

    /*-------------------------------------------------------------------------\
     | Movement methods.                                                       |
     |-------------------------------------------------------------------------|
     |                                                                         |
     | doUsefulMovement()                                                      |
     | +-> doUsefulLandMovement()                                              |
     | +-> doUsefulSeaMovement() TODO!                                         |
     | +-> doUsefulAirMovement() TODO!                                         |
     | ...                                                                     |
     |     +-> moveTowardsClosestTreasure()                                    |
     |     +-> doLandTravelTowards()                                           |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void doUsefulLandMovement()
    {
        moveTowardsClosestTreasure();
    }

    /*------------------------------------------------------------------------*/
    private void moveTowardsClosestTreasure()
    {
        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList;

        // routesArrayList = c.getAvailableRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), 2 * TRUE_MAX_ROAD_PRICE), c.getDice());
        routesArrayList = c.getMyAvailableRoutes();

        ArrayList<Node> treasureCitiesArrayList;
        treasureCitiesArrayList = c.getRemainingTreasures();

        Route chosenRoute = null;
        Node chosenTreasureCity = null;
        int shortestDistanceToTreasureCity = -1;
        int chosenPrice = -1;

        for (Route route : routesArrayList)
        {
            for (Node treasureCity : treasureCitiesArrayList)
            {
                int distanceFromDestinationToTreasureCity = getShortestLandDistance(route.getDestination(), treasureCity);
                int routePrice = route.getPrice();
                boolean isUpdateNeeded = false;

                writeTextAndNewlineToLog("distance from " + route.getDestination().getName()
                                         + " to " + treasureCity.getName()
                                         + " is " + distanceFromDestinationToTreasureCity
                                         + " links.");

                if (distanceFromDestinationToTreasureCity >= 0)
                {
                    if (shortestDistanceToTreasureCity < 0)
                    {
                        isUpdateNeeded = true;
                    }
                    else if (shortestDistanceToTreasureCity > distanceFromDestinationToTreasureCity)
                    {
                        isUpdateNeeded = true;
                    }
                    else if ((shortestDistanceToTreasureCity == distanceFromDestinationToTreasureCity)
                             && (chosenPrice > routePrice))
                    {
                        isUpdateNeeded = true;
                    }
                    else
                    {
                        isUpdateNeeded = false;
                    }
                }

                if (isUpdateNeeded)
                {
                    chosenRoute = route;
                    chosenPrice = routePrice;
                    chosenTreasureCity = treasureCity;
                    shortestDistanceToTreasureCity = distanceFromDestinationToTreasureCity;
                }
            }
        }

        if ((chosenRoute == null) || (chosenTreasureCity == null))
        {
            writeTextAndNewlineToLog("chosenRoute is null, I'll do random land movement!");
            doRandomLandMovement();
        }
        else
        {
            writeTextAndNewlineToLogAndDebug("I'm taking chosenRoute to " + chosenRoute.getDestination().getName()
                                             + ", en route to " + chosenTreasureCity.getName() + ", price " + chosenPrice + " pounds.");
            executeRoute(chosenRoute);
        }
    }

    /*------------------------------------------------------------------------*/
    private void doRandomLandMovement()
    {
        c.decideToUseLandOrSeaRoute();

        if (c.getMyAvailableRoutes().isEmpty())
        {
            writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName() + " and I have no available routes.");
        }
        else
        {
            ArrayList<Route> routesArrayList;
            // routesArrayList = c.getAvailableRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), MAX_LAND_ROAD_PRICE), c.getDice());
            routesArrayList = c.getMyAvailableRoutes();
            Route route;
            route = routesArrayList.get(0);
            writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName() + " and I take the first available route to " + route.getDestination().getName() + ".");
            executeRoute(route);
        }
    }

    /*------------------------------------------------------------------------*/
    private void doLandTravelTowards(Node targetNode)
    {
        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList;
        // routesArrayList = c.getAvailableRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), MAX_LAND_ROAD_PRICE), c.getDice());
        routesArrayList = c.getMyAvailableRoutes();

        if (!(routesArrayList.isEmpty()))
        {
            Route chosenRoute = null;
            int landDistanceFromChosenNodeToTreasureCity = -1;

            for (Route currentRoute : routesArrayList)
            {
                Node routeNode;
                routeNode = currentRoute.getDestination();

                int landDistanceFromDestinationToTreasureCity;
                landDistanceFromDestinationToTreasureCity = getShortestLandDistance(routeNode, targetNode);

                writeTextAndNewlineToLog("Land distance from destination " + routeNode.getName()
                                         + " to " + targetNode.getName()
                                         + " is " + landDistanceFromDestinationToTreasureCity
                                         + " links.");

                boolean isUpdateNeeded = false;

                if (landDistanceFromDestinationToTreasureCity >= 0)
                {
                    if (landDistanceFromChosenNodeToTreasureCity < 0)
                    {
                        isUpdateNeeded = true;
                    }
                    else
                    {
                        isUpdateNeeded = (landDistanceFromChosenNodeToTreasureCity > landDistanceFromDestinationToTreasureCity);
                    }
                }

                if (isUpdateNeeded)
                {
                    chosenRoute = currentRoute;
                    landDistanceFromChosenNodeToTreasureCity = landDistanceFromDestinationToTreasureCity;
                    writeTextAndNewlineToLog("The destination node of chosenRoute is " + chosenRoute.getDestination().getName() + ".");
                    writeTextAndNewlineToLog("landDistanceFromChosenNodeToTreasureCity is " + landDistanceFromChosenNodeToTreasureCity + ".");
                }
            }

            if (landDistanceFromChosenNodeToTreasureCity < 0)
            {
                writeTextAndNewlineToLog("I'm trying to do land travel towards " + targetNode.getName()
                                         + ", but landDistanceFromChosenNodeToTreasureCity is " + landDistanceFromChosenNodeToTreasureCity
                                         + "! Doing random land move!");
                doRandomLandMovement();
            }
            else if (chosenRoute == null)
            {
                writeTextAndNewlineToLog("I'm trying to do land travel towards " + targetNode.getName() + ", but chosenRoute is null! Doing random land move!");
                doRandomLandMovement();
            }
            else
            {
                writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName()
                                                 + ", I'm going to " + chosenRoute.getDestination().getName()
                                                 + ", en route to " + targetNode.getName() + ".");
                executeRoute(chosenRoute); // Do the movement.
            }
        }
        else
        {

            writeTextAndNewlineToLog("I'm trying to do land travel towards " + targetNode.getName() + ", but routesArrayList is empty! Doing random land move!");
            doRandomLandMovement();
        }
    }

    /*------------------------------------------------------------------------*/
    private void doLandTravelTowards(int targetNodeID)
    {
        doLandTravelTowards(c.getNode(targetNodeID));
    }

    /*------------------------------------------------------------------------*/
    private void doEndTurn()
    {
        writeTextAndNewlineToLog("Doing endTurn now");
        c.endTurn();
    }

    /*------------------------------------------------------------------------*/
    private void executeRoute(Route route)
    {
        String string;
        string = "Executing route to " + route.getDestination().getName() + ".";
        writeTextAndNewlineToLog(string);
        c.moveTo(route);
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Token buying methods.                                                   |
     |                                                                         |
     \------------------------------------------------------------------------*/
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

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Logging and reporting methods.                                          |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void writeTextToLog(String text)
    {
        if (isLoggingInUse)
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
    }

    /*------------------------------------------------------------------------*/
    private void writeTextAndNewlineToLog(String text)
    {
        String string = turnNumber + ": " + text + "\n";
        writeTextToLog(string);
    }

    /*------------------------------------------------------------------------*/
    private void writeTextAndNewlineToLogAndDebug(String text)
    {
        writeTextAndNewlineToLog(text);
        String string = turnNumber + ": " + text;
        c.setDebugString(string);
    }

    /*------------------------------------------------------------------------*/
    private void printMyItineraryToLogAndDebug(Node oldNode, Node newNode, Node destinationNode)
    {
        String string = "coming from " + oldNode.getName()
                        + ", going to " + newNode.getName()
                        + ", en route to " + destinationNode.getName() + ".";
        writeTextAndNewlineToLogAndDebug(string);
    }

    /*------------------------------------------------------------------------*/
    private void printMyItineraryToLogAndDebug(int oldNodeID, int newNodeID, int destinationNodeID)
    {
        printMyItineraryToLogAndDebug(c.getNode(oldNodeID), c.getNode(newNodeID), c.getNode(destinationNodeID));
    }

    /*------------------------------------------------------------------------*/
    private void printMyItineraryToLogAndDebug(Node oldNode, Node newNode)
    {
        String string = "coming from " + oldNode.getName()
                        + ", going to " + newNode.getName() + ".";
        writeTextAndNewlineToLogAndDebug(string);
    }

    /*------------------------------------------------------------------------*/
    private void printMyItineraryToLogAndDebug(int oldNodeID, int newNodeID)
    {
        printMyItineraryToLogAndDebug(c.getNode(oldNodeID), c.getNode(newNodeID));
    }

    /*------------------------------------------------------------------------*/
}
