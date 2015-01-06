/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Node;
import freeat.Route;
import freeat.Globals;
import freeat.PublicInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

// Paranormal AI's own imports.
import freeat.ai.paranormalai.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author antti
 */
public class ParanormalAI extends AI
{

    // class-level (static) variables.
    static boolean isLoggingInUse = false;
    static boolean shouldConnectionHashMapGenerationBeLogged = false;
    static boolean shouldLandMassHashMapGenerationBeLogged = false;

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
    Random rand = new Random();

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
    // public static final int MONEY_SCALE = 100;
    // minimum land & sea road price.
    public static final int MIN_ROAD_PRICE = 0;

    // minimum land road price.
    public static final int MAX_LAND_ROAD_PRICE = 0;

    // maximum land & sea road price as used by FreeAT.
    public static final int MAX_ROAD_PRICE = Globals.SEA_ROUTE_PRICE;

    // distance to a neighbor node.
    public static final int NEIGHBOR_DISTANCE = 1;

    // maximum price for land & sea routes as used by ParanormalAI.
    public static final int MAX_LAND_SEA_TOTAL_PRICE = Globals.MAX_DICE_VALUE;

    // initial distance from starting node (targetNode).
    public static final int INITIAL_DISTANCE = 0;

    // initial price from starting node (targetNode).
    public static final int INITIAL_PRICE = 0;

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

    /**
     *
     */
    public static ConcurrentHashMap<Integer, ParanormalNode> paranormalNodeHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ParanormalNode>>> connectionsHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> distanceToTargetHashMap;

    /**
     *
     */
    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> priceToTargetHashMap;

    /**
     *
     */
    public static HashMap<Integer, Landmass> landmassHashMap = new HashMap<>();

    int targetMetropolID = METROPOLS_ARRAY[(int) (Math.random())];
    boolean onAfricaTour = false;
    Node nextDestinationOfAfricaTour;

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
                initializeDistanceAndPriceHashMaps(); // fill the hashmaps with minus 1's.
                long startTime = System.nanoTime();

                ArrayList<Thread> threadArrayList;
                threadArrayList = new ArrayList<>();
                int threadCount;
                threadCount = 0;

                for (int currentMaxTotalPrice = 0; currentMaxTotalPrice <= MAX_LAND_SEA_TOTAL_PRICE; currentMaxTotalPrice++)
                {
                    if (!(connectionsHashMap.containsKey(currentMaxTotalPrice)))
                    {
                        connectionsHashMap.put(
                            currentMaxTotalPrice,
                            new ConcurrentHashMap<>());
                    }

                    final int tempCurrentMaxTotalPrice = currentMaxTotalPrice;

                    for (int targetNodeID : c.getNodeList().keySet())
                    {
                        final int tempTargetNodeID = targetNodeID;

                        // Create a new thread for each targetNode (targetNodeID).
                        threadArrayList.add(new Thread(
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    createConnectionsHashMap(tempTargetNodeID, tempCurrentMaxTotalPrice);
                                }
                            })
                        );
                        // Start the recently created thread.
                        threadArrayList.get(threadCount).start();
                        threadCount++;
                    }

                    boolean finished = false;
                    while (!(finished))
                    {
                        finished = true;
                        for (Thread thread : threadArrayList)
                        {
                            if (thread.isAlive())
                            {
                                finished = false;
                            }
                        }
                    }
                }
                writeTextAndNewlineToLog("Time to compute connections hash map: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
                System.out.println("Time to compute connections hash map: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
                isDistanceDataReady = true;
            }

            // Main game loop begins here.
            // The different situations that I as an AI need to handle:
            // #1 Great, I am eligible for win (someone else may be eligible too).
            // #2 Someone else is eligible for win (not me), and I do have money, and there are treasures left.
            // #3 Someone else is eligible for win (not me), I have no money, and there are treasures on this landmass.
            // #4 No one is eligible for win and I do have money, and there are treasures left.
            // #5 No one is eligible for win and I have no money, and there are treasures left on this landmass.
            // #6 Hopeless situation: I have no money, and there are no treasures on this landmass.
            // #7 Hopeless situation: There are no treasures left anywhere.
            //
            // Other situations may be hopeless too, but in situations listed as 'hopeless' above it's also impossible to try tokens to help allies.
            if (c.isEligibleForWin())
            {
                writeTextAndNewlineToLog(
                    "situation #1: Great, I am eligible for win (someone else may be eligible too).");
                // Situation #1.
                // Great, I am eligible for win (someone else may be eligible too).
                // Check which is closest land & sea destination, Cairo or Tangier.
                int shortestDistanceToCairoWithCurrentCash = getShortestDistanceWithCash(c.getNode(CAIRO_NODE_ID));
                int shortestDistanceToTangierWithCurrentCash = getShortestDistanceWithCash(c.getNode(TANGIER_NODE_ID));

                if (shortestDistanceToCairoWithCurrentCash < shortestDistanceToTangierWithCurrentCash)
                {
                    targetMetropolID = CAIRO_NODE_ID;   // Target metropol is Cairo.
                }
                else if (shortestDistanceToCairoWithCurrentCash > shortestDistanceToTangierWithCurrentCash)
                {
                    targetMetropolID = TANGIER_NODE_ID; // Target metropol is Tangier.
                }
                else if (shortestDistanceToCairoWithCurrentCash == shortestDistanceToTangierWithCurrentCash)
                {
                    targetMetropolID = METROPOLS_ARRAY[(int) (Math.random())]; // Target metropol is randomly chosen between Cairo and Tangier.
                }
                else
                {
                    writeTextAndNewlineToLogAndDebug("error in Cairo-Tangier distance check.");
                    ArrayList<Route> routesArrayList;
                    // routesArrayList = c.getAllRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), 100), c.getDice());
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

                String winConditionMessage;
                winConditionMessage = "";

                if (c.hasStar())
                {
                    winConditionMessage = "w/Star of Africa! ";
                }
                else if (c.hasHorseShoeAfterStar())
                {
                    winConditionMessage = "w/valid horseshoe! ";
                }
                doLandSeaTravelTowards(targetMetropolID, winConditionMessage);
                if (isThereExcessCash())
                {
                    buyTokenIfItMayBeUseful();
                }
                doEndTurn();
            }
            else if (isAnyOpponentEligibleForWin() && (getCash() >= Globals.TREASURE_BUYING_PRICE) && (areThereTreasuresLeft()))
            {
                writeTextAndNewlineToLog(
                    "situation #2: Someone else is eligible for win (not me), and I do have money, and there are treasures left.");
                // Situation #2.
                // Someone else is eligible for win (not me), and I do have money, and there are treasures left.
                // TODO: Play using best-case scenario for me.
                if (c.getCurrentNode().hasTreasure())
                {
                    if (isThereSpareCash())
                    {
                        writeTextAndNewlineToLogAndDebug("#2: I have spare cash (cash: " + getCash() + " GBP), I buy token.");
                        c.buyToken();
                    }
                    else
                    {
                        writeTextAndNewlineToLogAndDebug("#2: I don't have spare cash (cash: " + getCash() + " GBP), I try token.");
                        c.decideTryToken();
                    }
                }
                else
                {
                    moveTowardsClosestTreasure("#2: " + c.horseShoesLeft() + " horseshoes left! ");
                    if (isThereSpareCash())
                    {
                        buyTokenIfItMayBeUseful();
                    }
                }
                doEndTurn();
            }
            else if (isAnyOpponentEligibleForWin() && areThereTreasuresLeftOnLandmass())
            {
                writeTextAndNewlineToLog(
                    "situation #3: Someone else is eligible for win (not me), I have no money, and there are treasures on this landmass.");
                // Situation #3.
                // Someone else is eligible for win (not me), I have no money, and there are treasures on this landmass.
                // TODO: Play using best-case scenario for me.
                if (c.getCurrentNode().hasTreasure())
                {
                    writeTextAndNewlineToLogAndDebug("#3: I don't have spare cash (cash: " + getCash() + " GBP), I try token.");
                    c.decideTryToken();
                }
                else
                {
                    searchForTreasureWithoutCash("#3: " + c.horseShoesLeft() + "horseshoes left! ");
                }
                doEndTurn();
            }
            else if ((getCash() >= Globals.TREASURE_BUYING_PRICE) && (c.getRemainingTreasures().size() > 0))
            {
                writeTextAndNewlineToLog(
                    "situation #4: No one is eligible for win and I do have money, and there are treasures left.");
                // Situation #4.
                // No one is eligible for win and I do have money, and there are treasures left.
                if (c.getCurrentNode().hasTreasure())
                {
                    if (isThereSpareCash())
                    {
                        writeTextAndNewlineToLogAndDebug("#4: I have spare cash (cash: " + getCash() + " GBP), I buy token.");
                        c.buyToken();
                    }
                    else
                    {
                        writeTextAndNewlineToLogAndDebug("#4: I don't have spare cash (cash: " + getCash() + " GBP), I try token.");
                        c.decideTryToken();
                    }
                }
                else
                {
                    moveTowardsClosestTreasure("#4: " + c.robbersLeft() + " robbers left! ");
                    if (isThereSpareCash())
                    {
                        buyTokenIfItMayBeUseful();
                    }
                }
                doEndTurn();
            }
            else if (areThereTreasuresLeftOnLandmass())
            {
                writeTextAndNewlineToLog(
                    "situation #5: No one is eligible for win and I have no money, and there are treasures left on this landmass.");
                // Situation #5.
                // No one is eligible for win and I have no money, and there are treasures left on this landmass.
                if (c.getCurrentNode().hasTreasure())
                {
                    writeTextAndNewlineToLogAndDebug("#5: I don't have spare cash, I try token.");
                    c.decideTryToken();
                }
                else
                {
                    searchForTreasureWithoutCash("#5: " + c.robbersLeft() + " robbers left! ");
                }
                doEndTurn();
            }
            else if ((getCash() < Globals.TREASURE_BUYING_PRICE) && (!(areThereTreasuresLeftOnLandmass())))
            {
                writeTextAndNewlineToLog(
                    "situation #6 Hopeless situation: I have no money, and there are no treasures on this landmass.");
                // #6 Hopeless situation: I have no money, and there are no treasures on this landmass.
                doAfricaTour();
                doEndTurn();
            }
            else
            {
                writeTextAndNewlineToLog(
                    "situation #7: Hopeless situation: There are no treasures left anywhere.");
                // #7 Hopeless situation: There are no treasures left anywhere.
                doAfricaTour();
                doEndTurn();
            }

            doEndTurn();
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
    private void initializeDistanceAndPriceHashMaps()
    {
        distanceToTargetHashMap = new ConcurrentHashMap<>();
        priceToTargetHashMap = new ConcurrentHashMap<>();

        for (int i = 0; i <= MAX_LAND_SEA_TOTAL_PRICE; i++)
        {
            distanceToTargetHashMap.put(i, new ConcurrentHashMap<>());
            priceToTargetHashMap.put(i, new ConcurrentHashMap<>());

            for (int j : c.getNodeList().keySet())
            {
                distanceToTargetHashMap.get(i).put(j, new ConcurrentHashMap<>());
                priceToTargetHashMap.get(i).put(j, new ConcurrentHashMap<>());

                for (int k : c.getNodeList().keySet())
                {
                    distanceToTargetHashMap.get(i).get(j).put(k, -1);
                    priceToTargetHashMap.get(i).get(j).put(k, -1);
                }
            }
        }
    }

    /*------------------------------------------------------------------------*/
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

        connectionsHashMap.get(currentMaxTotalPrice).putIfAbsent(
            targetNode.ID,
            new ConcurrentHashMap<>());

        connectionsHashMap.get(currentMaxTotalPrice).get(targetNode.ID).putIfAbsent(
            originNode.ID,
            paranormalNodeHashMap.get(originNode.ID));

        ParanormalNode pNode;
        pNode = paranormalNodeHashMap.get(originNode.ID);

        // 0: start from node, cumulative price = 0.
        //    call recursively each neighboring node if distance there is negative (not yet defined) or greater than current distance.
        ArrayList<Route> routesArrayList = c.getAllRoutes(
            originNode,
            Globals.SEA_ROUTE_PRICE,
            NEIGHBOR_DISTANCE); // ArrayList of neighboring nodeID's.

        cumulativeDistance++;

        for (Route route : routesArrayList)
        {
            Node neighborNode = route.getDestination();

            if (neighborNode != originNode)
            {
                ParanormalNode neighborParanormalNode = getParanormalNode(neighborNode);

                Route linkRoute = getLinkRoute(neighborNode, originNode);

                if (linkRoute == null)
                {
                    System.out.println("error: linkRoute from " + neighborNode.getName() + " to " + originNode.getName() + " is null!");
                }

                // int newCumulativePrice = (cumulativePrice + (linkRoute.getPrice() / MONEY_SCALE));
                int newCumulativePrice = cumulativePrice + (linkRoute.getPrice());

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
        routesArrayList = c.getAllRoutes(
            originNode,
            Globals.SEA_ROUTE_PRICE,
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
    private int getShortestDistanceWithCash(int originNodeID, int targetNodeID, int cash)
    // shortest distance with given cash from _origin_ node to _target_ node.
    {
        return paranormalNodeHashMap.get(originNodeID).getDistanceToTarget(targetNodeID, cash);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithCash(Node originNode, Node targetNode, int cash)
    // shortest distance with given cash from _origin_ node to _target_ node.
    {
        return getShortestDistanceWithCash(originNode.ID, targetNode.ID, cash);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithCash(Node originNode, Node targetNode)
    // shortest distance with current cash from _origin_ node to _target_ node.
    {
        return getShortestDistanceWithCash(originNode, targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithCash(Node targetNode, int cash)
    // shortest distance with given cash from _current_ node to _target_ node.
    {
        return getShortestDistanceWithCash(c.getCurrentNode().ID, targetNode.ID, cash);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestDistanceWithCash(Node targetNode)
    // shortest distance with current cash from _current_ node to _target_ node.
    {
        return getShortestDistanceWithCash(c.getCurrentNode(), targetNode);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandDistance(Node originNode, Node targetNode)
    // shortest distance with 0 cash from _origin_ node to _target_ node.
    {
        return getShortestDistanceWithCash(originNode, targetNode, MAX_LAND_ROAD_PRICE);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestLandDistance(Node targetNode)
    // shortest distance with 0 cash from _current_ node to _target_ node.
    {
        return getShortestLandDistance(c.getCurrentNode(), targetNode);
    }
    /*------------------------------------------------------------------------*/

    private int getShortestAirDistance(int originNodeID, int targetNodeID, int cash)
    // shortest flight distance with given cash from _current_ node to _target_ node.
    // TODO: write the code!
    {
        return -1;
    }

    /*------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node originNode, Node C, int cash)
    // shortest flight distance with given cash from _current_ node to _target_ node.
    {
        return getShortestAirDistance(originNode.ID, originNode.ID, cash);
    }

    /*------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node originNode, Node targetNode)
    // shortest flight distance with current cash from _origin_ node to _target_ node.
    {
        return getShortestAirDistance(originNode, targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private int getShortestAirDistance(Node targetNode)
    // shortest flight distance with current cash from _current_ node to _target_ node.
    {
        return getShortestAirDistance(c.getCurrentNode(), targetNode);
    }

    /*------------------------------------------------------------------------*/
    private int getCheapestPriceToMetropol(Node originNode)
    // the price of the cheapest route from originNode to any metropol.
    {
        int priceOfChosenRoute;
        priceOfChosenRoute = -1;

        for (int currentPrice = 0; currentPrice <= MAX_LAND_SEA_TOTAL_PRICE; currentPrice++)
        {
            for (int metropolNodeID : METROPOLS_ARRAY)
            {
                int priceOfCurrentRoute;
                priceOfCurrentRoute = paranormalNodeHashMap.get(c.getCurrentNode().ID).getPriceToTarget(metropolNodeID, currentPrice);
                boolean isUpdateNeeded = false;

                if (paranormalNodeHashMap.get(originNode.ID).getPriceToTarget(metropolNodeID, currentPrice) >= 0);
                {
                    if (priceOfChosenRoute < 0)
                    {
                        isUpdateNeeded = true;
                    }
                    else if (priceOfChosenRoute > priceOfCurrentRoute)
                    {
                        isUpdateNeeded = true;
                    }

                    if (isUpdateNeeded)
                    {
                        priceOfChosenRoute = priceOfCurrentRoute;
                    }
                }
            }
        }
        return priceOfChosenRoute;
    }

    /*------------------------------------------------------------------------*/
    // the price of the shortest route from originNode to any metropol.
    private int getPriceOfFastestRouteToMetropol()
    {
        int lengthOfChosenRoute;
        lengthOfChosenRoute = -1;
        int priceOfChosenRoute;
        priceOfChosenRoute = -1;

        for (int metropolNodeID : METROPOLS_ARRAY)
        {
            int lengthOfCurrentRoute;
            int priceOfCurrentRoute;
            lengthOfCurrentRoute = getShortestDistanceWithCash(c.getNode(metropolNodeID), getCash());
            priceOfCurrentRoute = paranormalNodeHashMap.get(c.getCurrentNode().ID).getPriceToTarget(metropolNodeID, MAX_LAND_SEA_TOTAL_PRICE);
            boolean isUpdateNeeded = false;

            if (priceOfCurrentRoute >= 0)
            {
                if (lengthOfChosenRoute < 0)
                {
                    isUpdateNeeded = true;
                }
                else if ((lengthOfChosenRoute == lengthOfCurrentRoute) && (priceOfChosenRoute > priceOfCurrentRoute))
                {
                    isUpdateNeeded = true;
                }

                if (isUpdateNeeded)
                {
                    lengthOfChosenRoute = lengthOfCurrentRoute;
                    priceOfChosenRoute = priceOfCurrentRoute;
                }
            }
        }
        return priceOfChosenRoute;
    }
    /*------------------------------------------------------------------------*/

    private int getCheapestPriceToMetropol()
    // the price of the cheapest route from current node to any metropol.
    {
        return getCheapestPriceToMetropol(c.getCurrentNode());
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Global game state reasoning methods.                                    |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private int getCash()
    {
        // return c.getMyBalance() / MONEY_SCALE;
        return c.getMyBalance();
    }

    /*------------------------------------------------------------------------*/
    private int getMinTreasureValue()
    {
        if (c.robbersLeft() > 0)
        {
            return -1 * getCash();
        }
        else if ((c.emptyLeft() > 0) || (c.horseShoesLeft() > 0))
        {
            return 0;
        }
        else if (c.topazesLeft() > 0)
        {
            return Globals.TOPAZ_VALUE;
        }
        else if (c.emeraldsLeft() > 0)
        {
            return Globals.EMERALD_VALUE;
        }
        else if (c.rubiesLeft() > 0)
        {
            return Globals.RUBY_VALUE;
        }
        return 0;
    }

    /*------------------------------------------------------------------------*/
    private int getMinTreasureValueWithoutRobbers()
    {
        if ((c.emptyLeft() > 0) || (c.horseShoesLeft() > 0))
        {
            return 0;
        }
        else if (c.topazesLeft() > 0)
        {
            return Globals.TOPAZ_VALUE;
        }
        else if (c.emeraldsLeft() > 0)
        {
            return Globals.EMERALD_VALUE;
        }
        else if (c.rubiesLeft() > 0)
        {
            return Globals.RUBY_VALUE;
        }
        return 0;
    }

    /*------------------------------------------------------------------------*/
    private int getMinCashAfterBuyingToken()
    {
        return getCash() - Globals.TREASURE_BUYING_PRICE + getMinTreasureValue();
    }

    /*------------------------------------------------------------------------*/
    private int getMinProbableCashAfterBuyingToken()
    {
        return getCash() - Globals.TREASURE_BUYING_PRICE + getMinTreasureValueWithoutRobbers();
    }

    /*------------------------------------------------------------------------*/
    private int getMinCashAfterWinningTokenWithDice()
    {
        return getCash() + getMinTreasureValue();
    }

    /*------------------------------------------------------------------------*/
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

    private boolean areThereUsefulTreasuresLeft()
    {
        // Are there left any treasures, which could be useful for me, an opponent or a possible ally?
        // In other words: Can I make my situation better by buying or trying tokens?
        if (!(c.isStarFound()))
        {
            return true;
        }
        return ((c.horseShoesLeft() + c.rubiesLeft() + c.emeraldsLeft() + c.topazesLeft()) > 0);
    }

    /*------------------------------------------------------------------------*/
    private int getRoutePrice(Route route)
    {
        // return route.getPrice() / MONEY_SCALE;
        return route.getPrice();
    }

    /*------------------------------------------------------------------------*/
    private int getCashAfterRoute(Route route)
    {
        return getCash() - getRoutePrice(route);
    }

    /*------------------------------------------------------------------------*/
    private int getLandmassID(int nodeID)
    {
        return paranormalNodeHashMap.get(nodeID).getLandmassID();
    }

    /*------------------------------------------------------------------------*/
    private int getLandmassID(Node node)
    {
        return getLandmassID(node.ID);
    }

    /*------------------------------------------------------------------------*/
    private int getLandmassID()
    {
        return getLandmassID(c.getCurrentNode());
    }

    /*------------------------------------------------------------------------*/
    private boolean areThereTreasuresLeftOnLandmass(int landmassID)
    {
        // TODO: write the code!
        return true;
    }

    /*------------------------------------------------------------------------*/
    private boolean areThereTreasuresLeft()
    {
        return (c.getRemainingTreasures().size() > 0);
    }

    /*------------------------------------------------------------------------*/
    private boolean areThereTreasuresLeftOnLandmass()
    {
        return areThereTreasuresLeftOnLandmass(getLandmassID());
    }

    /*------------------------------------------------------------------------*/
    private ArrayList<Integer> getLandmassesWithMostTreasuresLeft()
    {
        ArrayList<Integer> landmassesWithMostTreasuresLeft;
        landmassesWithMostTreasuresLeft = new ArrayList<>();

        // TODO: write the code!
        return landmassesWithMostTreasuresLeft;
    }

    /*------------------------------------------------------------------------*/
    private ArrayList<Integer> getMetropolLandmasses()
    {
        ArrayList<Integer> metropolLandmasses;
        metropolLandmasses = new ArrayList<>();

        // TODO: write the code!
        return metropolLandmasses;
    }

    /*------------------------------------------------------------------------*/
    private ArrayList<Node> getRemainingTreasuresOnLandmass(int landmassID)
    {
        ArrayList<Node> treasureCitiesArrayList;
        treasureCitiesArrayList = new ArrayList<>();

        return treasureCitiesArrayList;
    }

    /*------------------------------------------------------------------------*/
    private ArrayList<Node> getRemainingTreasuresOnLandmass()
    {
        return getRemainingTreasuresOnLandmass(getLandmassID());
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Combined movement & token-trying methods.                               |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void searchForTreasureWithoutCash(String messagePrefix)
    {
        if (c.getCurrentNode().hasTreasure())
        {
            writeTextAndNewlineToLog("My node has a treasure!");
            c.decideTryToken();
            doEndTurn();
        }
        else if (c.getRemainingTreasures().size() >= 1)
        {
            writeTextAndNewlineToLog("There are treasures available somewhere!");
            moveTowardsClosestTreasure(messagePrefix);
            doEndTurn();
        }
        else
        {
            doRandomLandMovement();
        }
    }

    /*------------------------------------------------------------------------*/
    private void searchForTreasureWithoutCash()
    {
        searchForTreasureWithoutCash("");
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Movement methods.                                                       |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void moveTowardsClosestTreasure(String messagePrefix)
    {
        // TODO: implement travel to treasure cities also with flights.
        // TODO: prefer continental treasure cities to island treasure cities,
        //       until there are no robbers and I have enough money to return to
        //       a metropol landmass.
        // TODO: implement money counting and saving behavior for island trips.

        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList;

        // routesArrayList = c.getAllRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), 2 * Globals.SEA_ROUTE_PRICE), c.getDice());
        routesArrayList = c.getMyAvailableRoutes();

        ArrayList<Node> treasureCitiesArrayList;
        treasureCitiesArrayList = c.getRemainingTreasures();

        boolean randomChoiceInUse = false;

        ArrayList<Route> chosenRoutesArrayList;
        chosenRoutesArrayList = new ArrayList<>();

        ArrayList<Node> chosenTreasureCitiesArrayList;
        chosenTreasureCitiesArrayList = new ArrayList<>();

        Route chosenRoute = null;
        Node chosenTreasureCity = null;
        int shortestDistanceToTreasureCity;
        int chosenPrice;
        int priceFromChosenTreasureCityToMetropol;
        shortestDistanceToTreasureCity = chosenPrice = priceFromChosenTreasureCityToMetropol = -1;

        for (Route route : routesArrayList)
        {
            for (Node treasureCity : treasureCitiesArrayList)
            {
                int distanceFromDestinationToTreasureCity;
                int cheapestPriceToMetropolFromTreasureCity = getCheapestPriceToMetropol(treasureCity);
                distanceFromDestinationToTreasureCity = getShortestDistanceWithCash(
                    route.getDestination(),
                    treasureCity,
                    (getCashAfterRoute(route) - cheapestPriceToMetropolFromTreasureCity));
//                int cheapestPriceToMetropolFromTreasureCity = getCheapestPriceToMetropol(treasureCity);
//                distanceFromDestinationToTreasureCity = getShortestDistanceWithCash(
//                    route.getDestination(),
//                    treasureCity,
//                    getCash());
                int routePrice = route.getPrice();
                boolean isUpdateNeeded = false;
                boolean isBetterRoute = false;

                writeTextAndNewlineToLog("distance from " + route.getDestination().getName()
                                         + " to " + treasureCity.getName()
                                         + " is " + distanceFromDestinationToTreasureCity
                                         + " links.");
                writeTextAndNewlineToLog("cheapest price from " + treasureCity.getName()
                                         + " to any metropol is " + cheapestPriceToMetropolFromTreasureCity
                                         + " GBP.");

                if (false)
                // if (c.robbersLeft() > 0)
                {
                    // If there are robbers left, prefer
                    // metropol landmass destinations.
                    if ((distanceFromDestinationToTreasureCity >= 0) && (cheapestPriceToMetropolFromTreasureCity >= 0))
                    {
                        if (shortestDistanceToTreasureCity < 0)
                        {
                            isUpdateNeeded = isBetterRoute = true;
                        }
                        else if (priceFromChosenTreasureCityToMetropol > cheapestPriceToMetropolFromTreasureCity)
                        {
                            isUpdateNeeded = isBetterRoute = true;
                        }
                        else if ((priceFromChosenTreasureCityToMetropol == cheapestPriceToMetropolFromTreasureCity)
                                 && (shortestDistanceToTreasureCity > distanceFromDestinationToTreasureCity))
                        {
                            isUpdateNeeded = isBetterRoute = true;
                        }
                        else if ((priceFromChosenTreasureCityToMetropol == cheapestPriceToMetropolFromTreasureCity)
                                 && (shortestDistanceToTreasureCity == distanceFromDestinationToTreasureCity)
                                 && (chosenPrice > routePrice))
                        {
                            isUpdateNeeded = isBetterRoute = true;
                        }
                        else if ((priceFromChosenTreasureCityToMetropol == cheapestPriceToMetropolFromTreasureCity)
                                 && (shortestDistanceToTreasureCity == distanceFromDestinationToTreasureCity)
                                 && (chosenPrice == routePrice))
                        {
                            isUpdateNeeded = true;
                        }
                    }
                }
                else
                {
                    // If there are no robbers left, the
                    // landmass where the treasure city is located
                    // doesn't matter.
                    if (distanceFromDestinationToTreasureCity >= 0)
                    {
                        if (shortestDistanceToTreasureCity < 0)
                        {
                            isUpdateNeeded = true;
                            isBetterRoute = true;
                        }
                        else if (shortestDistanceToTreasureCity > distanceFromDestinationToTreasureCity)
                        {
                            isUpdateNeeded = true;
                            isBetterRoute = true;
                        }
                        else if ((shortestDistanceToTreasureCity == distanceFromDestinationToTreasureCity)
                                 && (chosenPrice > routePrice))
                        {
                            isUpdateNeeded = true;
                            isBetterRoute = true;
                        }
                        else if ((shortestDistanceToTreasureCity == distanceFromDestinationToTreasureCity)
                                 && (chosenPrice == routePrice))
                        {
                            isUpdateNeeded = true;
                        }
                    }
                }

                if (isUpdateNeeded)
                {
                    if (randomChoiceInUse)
                    {
                        if (isBetterRoute)
                        {
                            chosenRoutesArrayList.clear();
                            chosenTreasureCitiesArrayList.clear();
                        }
                        chosenRoutesArrayList.add(route);
                        chosenTreasureCitiesArrayList.add(treasureCity);
                    }
                    else
                    {
                        chosenRoute = route;
                        chosenTreasureCity = treasureCity;
                    }
                    chosenPrice = routePrice;
                    shortestDistanceToTreasureCity = distanceFromDestinationToTreasureCity;
                }
            }
        }

        if (randomChoiceInUse)
        {
            if ((!(chosenRoutesArrayList.isEmpty()))
                && (!(chosenTreasureCitiesArrayList.isEmpty()))
                && (chosenRoutesArrayList.size() == chosenTreasureCitiesArrayList.size()))
            {
                int randomIndex;
                randomIndex = rand.nextInt(chosenRoutesArrayList.size());
                chosenRoute = chosenRoutesArrayList.get(randomIndex);
                chosenTreasureCity = chosenTreasureCitiesArrayList.get(randomIndex);
            }
        }
        if ((chosenRoute == null) || (chosenTreasureCity == null))
        {
            writeTextAndNewlineToLog("chosenRoute or chosenTreasureCity is null, I'll do random land movement!");
            doRandomLandMovement();
        }
        else
        {
            writeTextAndNewlineToLogAndDebug(messagePrefix + "I'm taking route to " + chosenRoute.getDestination().getName()
                                             + ", en route to " + chosenTreasureCity.getName()
                                             + " (" + shortestDistanceToTreasureCity
                                             + " links remaining), price " + chosenPrice + " GBP.");
            executeRoute(chosenRoute);
        }
    }

    /*------------------------------------------------------------------------*/
    private void moveTowardsClosestTreasure()
    {
        moveTowardsClosestTreasure("");
    }

    /*------------------------------------------------------------------------*/
    private Node chooseRandomNodeFromList(ArrayList<Node> nodesArrayList)
    {
        if (nodesArrayList.isEmpty())
        {
            writeTextAndNewlineToLog("nodesArrayList is empty, returning null!");
            return null;
        }
        return nodesArrayList.get(rand.nextInt(nodesArrayList.size()));
    }

    /*------------------------------------------------------------------------*/
    private Route chooseRandomRouteFromList(ArrayList<Route> routesArrayList)
    {
        if (routesArrayList.isEmpty())
        {
            writeTextAndNewlineToLog("routesArrayList is empty, returning null!");
            return null;
        }
        else
        {
            return routesArrayList.get(rand.nextInt(routesArrayList.size()));
        }
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity(Node originNode, int currentMaxTotalPrice)
    {
        ArrayList<Node> allCitiesArrayList;
        allCitiesArrayList = c.getAllCities();
        int chosenDistance = -1;
        Node chosenNode = null;

        for (Node targetNode : allCitiesArrayList)
        {
            int distanceToCurrentNode;
            distanceToCurrentNode = getShortestDistanceWithCash(originNode, targetNode, currentMaxTotalPrice);

            if (distanceToCurrentNode >= 0)
            {
                if ((chosenDistance < 0) || (chosenDistance < distanceToCurrentNode))
                {
                    chosenDistance = distanceToCurrentNode;
                    chosenNode = targetNode;
                }
            }
        }
        return chosenNode;
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity(Node originNode)
    {
        return chooseFarthestCity(originNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity(int currentMaxTotalPrice)
    {
        return chooseFarthestCity(c.getCurrentNode(), currentMaxTotalPrice);
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity()
    {
        return chooseFarthestCity(c.getCurrentNode(), getCash());
    }

    /*------------------------------------------------------------------------*/
    private void doAfricaTour()
    {
        // TODO: Do the Africa tour.
        // 1. Choose the farthest city.
        // 2. Go there using only land movement.
        // 3. Repeat from 1 upon arrival.

        if (onAfricaTour)
        {
            if (c.getCurrentNode() == nextDestinationOfAfricaTour)
            {
                nextDestinationOfAfricaTour = chooseFarthestCity(MAX_LAND_ROAD_PRICE);
            }
            doLandSeaTravelTowards(
                nextDestinationOfAfricaTour,
                MAX_LAND_ROAD_PRICE,
                "I'm on Africa Tour en route to " + nextDestinationOfAfricaTour.getName() + ".");
        }
        else
        {
            Node chosenNode;
            chosenNode = chooseFarthestCity(MAX_LAND_ROAD_PRICE);

            if (chosenNode == null)
            {
                doRandomLandMovement();
            }
            else
            {
                nextDestinationOfAfricaTour = chosenNode;
                onAfricaTour = true;
                doAfricaTour();
            }
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
            // routesArrayList = c.getAllRoutes(c.getCurrentNode(), Math.min(c.getMyBalance(), MAX_LAND_ROAD_PRICE), c.getDice());
            routesArrayList = c.getMyAvailableRoutes();
            Route route;
            route = routesArrayList.get(0);
            writeTextAndNewlineToLogAndDebug("I am at " + c.getCurrentNodeName() + " and I take the first available route to " + route.getDestination().getName() + ".");
            executeRoute(route);
        }
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(Node targetNode, int currentMaxTotalPrice, String messagePrefix)
    {
        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList;
        // routesArrayList = c.getMyAvailableRoutes();
        routesArrayList = c.getAllRoutes(
            c.getCurrentNode(),
            // Math.min(MONEY_SCALE * currentMaxTotalPrice, 2 * Globals.SEA_ROUTE_PRICE),
            Math.min(currentMaxTotalPrice, 2 * Globals.SEA_ROUTE_PRICE),
            c.getDice());

        if (!(routesArrayList.isEmpty()))
        {
            Route chosenRoute = null;
            int landSeaDistanceFromChosenNodeToTargetNode = -1;

            for (Route currentRoute : routesArrayList)
            {
                Node routeNode;
                routeNode = currentRoute.getDestination();

                int landSeaDistanceFromDestinationToTargetNode;
                landSeaDistanceFromDestinationToTargetNode = getShortestDistanceWithCash(routeNode, targetNode);

                writeTextAndNewlineToLog("Land & sea distance from destination " + routeNode.getName()
                                         + " to " + targetNode.getName()
                                         + " is " + landSeaDistanceFromDestinationToTargetNode
                                         + " links (price : " + currentRoute.getPrice() + " GBP");

                boolean isUpdateNeeded = false;

                if (landSeaDistanceFromDestinationToTargetNode >= 0)
                {
                    if (landSeaDistanceFromChosenNodeToTargetNode < 0)
                    {
                        isUpdateNeeded = true;
                    }
                    else
                    {
                        isUpdateNeeded = (landSeaDistanceFromChosenNodeToTargetNode > landSeaDistanceFromDestinationToTargetNode);
                    }
                }

                if (isUpdateNeeded)
                {
                    chosenRoute = currentRoute;
                    landSeaDistanceFromChosenNodeToTargetNode = landSeaDistanceFromDestinationToTargetNode;
                    writeTextAndNewlineToLog("The destination node of chosenRoute is " + chosenRoute.getDestination().getName() + ".");
                    writeTextAndNewlineToLog("landDistanceFromChosenNodeToTreasureCity is " + landSeaDistanceFromChosenNodeToTargetNode + ".");
                }
            }

            if (landSeaDistanceFromChosenNodeToTargetNode < 0)
            {
                writeTextAndNewlineToLog("I'm trying to do land travel towards " + targetNode.getName()
                                         + ", but landDistanceFromChosenNodeToTreasureCity is " + landSeaDistanceFromChosenNodeToTargetNode
                                         + "! Cash: " + getCash()
                                         + " Doing random land move!");
                doRandomLandMovement();
            }
            else if (chosenRoute == null)
            {
                writeTextAndNewlineToLog("I'm trying to do land travel towards " + targetNode.getName()
                                         + ", but chosenRoute is null! Cash: " + getCash() + " Doing random land move!");
                doRandomLandMovement();
            }
            else
            {
                writeTextAndNewlineToLogAndDebug(messagePrefix + "I am at " + c.getCurrentNodeName()
                                                 + ", I'm going to " + chosenRoute.getDestination().getName()
                                                 + ", en route to " + targetNode.getName() + ".");
                executeRoute(chosenRoute);
            }
        }
        else
        {
            writeTextAndNewlineToLog("I'm trying to do land & sea travel towards " + targetNode.getName() + ", but routesArrayList is empty! Doing random land move!");
            doRandomLandMovement();
        }
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(Node targetNode, int currentMaxTotalPrice)
    {
        doLandSeaTravelTowards(targetNode, currentMaxTotalPrice, "");
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(Node targetNode, String messagePrefix)
    {
        doLandSeaTravelTowards(targetNode, getCash(), messagePrefix);
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(Node targetNode)
    {
        doLandSeaTravelTowards(targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(int targetNodeID, String messagePrefix)
    {
        doLandSeaTravelTowards(c.getNode(targetNodeID), messagePrefix);
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(int targetNodeID)
    {
        doLandSeaTravelTowards(targetNodeID, "");
    }

    /*------------------------------------------------------------------------*/
    private void doBudgetLandSeaTravelTowards(int targetNodeID)
    {
        // Travel to targetNode using the cheapest possible route.
        // TODO: write the code!
    }

    /*------------------------------------------------------------------------*/
    private void doBudgetLandSeaTravelTowards(Node targetNode)
    {
        // Travel to targetNode using the cheapest possible route.
        doBudgetLandSeaTravelTowards(targetNode.ID);
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
     | Token buying and trying methods.                                        |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void buyTokenIfItMayBeUseful()
    {
        // Different situations in which buying a token may be useful:
        // #1 No one has Africa's star.
        // #2 I am eligible for win, and there are valuable treasures left.
        // #3 Some opponent has Africa's star, and there are horseshoes left.

        if ((c.getCurrentNode().hasTreasure()) && areThereUsefulTreasuresLeft())
        {
            c.buyToken();
        }
    }

    /*------------------------------------------------------------------------*/
    private boolean isThereExcessCash()
    {
        if (getPriceOfFastestRouteToMetropol() >= 0)
        {
            return (getMinCashAfterBuyingToken() >= getPriceOfFastestRouteToMetropol());
        }
        else
        {
            return false;
        }
    }

    /*------------------------------------------------------------------------*/
    private boolean isThereSpareCash()
    {
        if (getCheapestPriceToMetropol() >= 0)
        {
            return (getMinProbableCashAfterBuyingToken() >= getCheapestPriceToMetropol());
        }
        else
        {
            return false;
        }
    }

    /*------------------------------------------------------------------------*/
    private void tryOrBuyToken()
    {
        if (c.getCurrentNode().hasTreasure())
        {
            if ((getCheapestPriceToMetropol() >= 0)
                && areThereUsefulTreasuresLeft()
                && (getMinCashAfterBuyingToken() >= getCheapestPriceToMetropol()))
            // some excess cash.
            {
                buyTokenIfItMayBeUseful();
            }
            else
            // no excess cash.
            {
                c.decideTryToken();
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
