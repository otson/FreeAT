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
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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
    static boolean isMultithreadingInUse = true;
    static boolean isLoggingInUse = false;
    static boolean shouldConnectionHashMapGenerationBeLogged = true;

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

    // minimum land & sea road price.
    public static final int MIN_ROAD_PRICE = 0;

    // minimum land road price.
    public static final int MAX_LAND_ROAD_PRICE = 0;

    // maximum land & sea road price as used by FreeAT.
    public static final int MAX_ROAD_PRICE = Globals.SEA_ROUTE_PRICE;

    // distance to a neighbor node.
    public static final int NEIGHBOR_DISTANCE = 1;

    // maximum price for land & sea routes as used by ParanormalAI.
    public static final int MAX_LAND_SEA_TOTAL_PRICE = 6;

    // min dice value x 2.
    public static final int MIN_DICE_VALUE_X_2 = 2 * Globals.MIN_DICE_VALUE;

    // max dice value x 2.
    public static final int MAX_DICE_VALUE_X_2 = 2 * Globals.MAX_DICE_VALUE;

    // mean dice value x 2.
    public static final int MEAN_DICE_VALUE_X_2 = Globals.MIN_DICE_VALUE + Globals.MAX_DICE_VALUE;

    // least common multiple for 1/DICE_MAX and MEAN_DICE_VALUE, 42 for 1d6.
    // TODO: compute the LCM!
    public static final int DICE_LCM = MIN_DICE_VALUE_X_2 * MAX_DICE_VALUE_X_2 * MEAN_DICE_VALUE_X_2;

    // number of time units needed for one link in worst case (lowest dice outcome).
    public static final int ONE_LINK_TIME_UNITS_FOR_MIN_DICE = DICE_LCM / MIN_DICE_VALUE_X_2; // 2*12*7/2 = 84 time units for 1d6 (not real LCM).

    // number of time units needed for one link in best case (highest dice outcome).
    public static final int ONE_LINK_TIME_UNITS_FOR_MAX_DICE = DICE_LCM / MAX_DICE_VALUE_X_2; // 2*12*7/7 = 24 time units for 1d6 (not real LCM).

    // number of time units needed for one link in best case (highest dice outcome).
    public static final int ONE_LINK_TIME_UNITS_FOR_MEAN_DICE = DICE_LCM / MEAN_DICE_VALUE_X_2; // 2*12*7/12 = 14 time units for 1d6 (not real LCM).

    // initial distance from starting node (targetNode).
    public static final int INITIAL_DISTANCE = 0;

    // initial price from starting node (targetNode).
    public static final int INITIAL_PRICE = 0;

    // initial time from starting node (targetNode).
    public static final int INITIAL_TIME = 0;

    // Least common multiple for mean dice value and max dice value.
    // Mean dice value is a floating point number ending with .00 or .50 (half).
    // public int DICE_LCM = computeLCM(MAX_DICE_VALUE);
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
    public static ConcurrentHashMap<Integer, ParanormalNode> connectionsHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, Integer> minTimePriceToTargetHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, Integer> maxTimePriceToTargetHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, Integer> meanTimePriceToTargetHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, Integer> minTimeToTargetHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, Integer> maxTimeToTargetHashMap = new ConcurrentHashMap<>();

    /**
     *
     */
    public static ConcurrentHashMap<Integer, Integer> meanTimeToTargetHashMap = new ConcurrentHashMap<>();

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
                initializeDistancePriceAndTimeHashMaps(); // fill the hashmaps with minus 1's.
                long startTime = System.nanoTime();

                ArrayList<Thread> threadArrayList = new ArrayList<>();
                int threadCount = 0;

                for (int currentMaxTotalPrice = 0; currentMaxTotalPrice <= MAX_LAND_SEA_TOTAL_PRICE; currentMaxTotalPrice++)
                {
                    if (isMultithreadingInUse)
                    {
                        final int tempCurrentMaxTotalPrice = currentMaxTotalPrice;

                        for (int targetNodeID : c.getNodeList().keySet())
                        {
                            final int tempTargetNodeID = targetNodeID;

                            // Create a new thread for each targetNode (targetNodeID).
                            threadArrayList.add(new Thread(
                                () ->
                                {
                                    createConnectionsHashMap(tempTargetNodeID,
                                        tempCurrentMaxTotalPrice,
                                        minTimeToTargetHashMap,
                                        minTimePriceToTargetHashMap);
                                    createConnectionsHashMap(tempTargetNodeID,
                                        tempCurrentMaxTotalPrice,
                                        maxTimeToTargetHashMap,
                                        maxTimePriceToTargetHashMap);
                                    createConnectionsHashMap(tempTargetNodeID,
                                        tempCurrentMaxTotalPrice,
                                        meanTimeToTargetHashMap,
                                        meanTimePriceToTargetHashMap);
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
                    else
                    {
                        for (int targetNodeID : c.getNodeList().keySet())
                        {
                            createConnectionsHashMap(targetNodeID,
                                currentMaxTotalPrice,
                                minTimeToTargetHashMap,
                                minTimePriceToTargetHashMap);
                            createConnectionsHashMap(targetNodeID,
                                currentMaxTotalPrice,
                                maxTimeToTargetHashMap,
                                maxTimePriceToTargetHashMap);
                            createConnectionsHashMap(targetNodeID,
                                currentMaxTotalPrice,
                                meanTimeToTargetHashMap,
                                meanTimePriceToTargetHashMap);
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
            // #3 Someone else is eligible for win (not me), I have no money, and there are treasures left.
            // #4 No one is eligible for win and I do have money, and there are treasures left.
            // #5 No one is eligible for win and I have no money, and there are treasures left.
            // #6 Hopeless situation: There are no treasures left anywhere.
            //
            // Other situations may be hopeless too, but in situations listed as 'hopeless' above it's also impossible to try tokens to help allies.
            if (c.isEligibleForWin())
            {
                writeTextAndNewlineToLog(
                    "situation #1: Great, I am eligible for win (someone else may be eligible too).");
                // Situation #1.
                // Great, I am eligible for win (someone else may be eligible too).
                // Check which is closest land & sea destination, Cairo or Tangier.
                // Follow worst-case scenario.
                int maxTimeToCairoWithCurrentCash = getMaxTimeToTarget(c.getNode(CAIRO_NODE_ID), c.isUsingFreeSeaRoute());
                int maxTimeToTangierWithCurrentCash = getMaxTimeToTarget(c.getNode(TANGIER_NODE_ID), c.isUsingFreeSeaRoute());

                if (maxTimeToCairoWithCurrentCash < maxTimeToTangierWithCurrentCash)
                {
                    targetMetropolID = CAIRO_NODE_ID;   // Target metropol is Cairo.
                }
                else if (maxTimeToCairoWithCurrentCash > maxTimeToTangierWithCurrentCash)
                {
                    targetMetropolID = TANGIER_NODE_ID; // Target metropol is Tangier.
                }
                else if (maxTimeToCairoWithCurrentCash == maxTimeToTangierWithCurrentCash)
                {
                    targetMetropolID = METROPOLS_ARRAY[(int) (Math.random())]; // Target metropol is randomly chosen between Cairo and Tangier.
                }
                else
                {
                    writeTextAndNewlineToLogAndDebug("error in Cairo-Tangier travel time check.");
                    ArrayList<Route> routesArrayList;
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
                // Follow worst-case scenario.
                doLandSeaTravelTowards(targetMetropolID, winConditionMessage, maxTimeToTargetHashMap);
                if (isThereExcessCash())
                {
                    buyTokenIfItMayBeUseful();
                }
                doEndTurn();
            }
            else if (isAnyOpponentEligibleForWin() && (getCash() >= Globals.TREASURE_BUYING_PRICE) && areThereTreasuresLeft())
            {
                writeTextAndNewlineToLog(
                    "situation #2: Someone else is eligible for win (not me), and I do have money (" + getCash() + " GBP), and there are treasures left.");
                // Situation #2.
                // Someone else is eligible for win (not me), and I do have money, and there are treasures left.
                if (c.getCurrentNode().hasTreasure())
                {
                    c.buyToken();
                }
                else
                {
                    // Follow best-case scenario.
                    moveTowardsClosestTreasureInTime(
                        "#2: " + c.horseShoesLeft() + " horseshoes left! ",
                        minTimeToTargetHashMap);
                    buyTokenIfItMayBeUseful();
                }
                doEndTurn();
            }
            else if (isAnyOpponentEligibleForWin() && areThereTreasuresLeft())
            {
                writeTextAndNewlineToLog(
                    "situation #3: Someone else is eligible for win (not me), I have no money, and there are treasures left.");
                // Situation #3.
                // Someone else is eligible for win (not me), I have no money, and there are treasures left.
                // TODO: Play using best-case scenario for me.
                if (c.getCurrentNode().hasTreasure())
                {
                    writeTextAndNewlineToLogAndDebug("#3 I don't have any cash, so I try token.");
                    c.decideTryToken();
                }
                else
                {
                    // Follow best-case scenario.
                    searchForTreasureWithoutCash(
                        "#3: " + c.horseShoesLeft() + "horseshoes left! ",
                        minTimeToTargetHashMap);
                }
                doEndTurn();
            }
            else if ((getCash() >= Globals.TREASURE_BUYING_PRICE) && areThereTreasuresLeft())
            {
                writeTextAndNewlineToLog(
                    "situation #4: No one is eligible for win and I do have money (" + getCash() + " GBP), and there are treasures left.");
                // Situation #4.
                // No one is eligible for win and I do have money, and there are treasures left.
                if (c.getCurrentNode().hasTreasure())
                {
                    c.buyToken();
                }
                else
                {
                    // Follow average-case scenario.
                    moveTowardsClosestTreasureInTime(
                        "#4: " + c.robbersLeft() + " robbers left! ",
                        meanTimeToTargetHashMap);
                    buyTokenIfItMayBeUseful();
                }
                doEndTurn();
            }
            else if (areThereTreasuresLeft())
            {
                writeTextAndNewlineToLog(
                    "situation #5: No one is eligible for win and I have no money, and there are treasures left.");
                // Situation #5.
                // No one is eligible for win and I have no money, and there are treasures left.
                if (c.getCurrentNode().hasTreasure())
                {
                    writeTextAndNewlineToLogAndDebug("#5: I don't have any cash, so I try token.");
                    c.decideTryToken();
                }
                else
                {
                    // Follow average-case scenario.
                    searchForTreasureWithoutCash(
                        "#5: " + c.robbersLeft() + " robbers left! ",
                        meanTimeToTargetHashMap);
                }
                doEndTurn();
            }
            else
            {
                writeTextAndNewlineToLog(
                    "situation #6: Hopeless situation: There are no treasures left anywhere.");
                // #6 Hopeless situation: There are no treasures left anywhere.
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

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Route data generation methods.                                          |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void initializeDistancePriceAndTimeHashMaps()
    {
        for (int i : c.getNodeList().keySet())
        {
            for (int j : c.getNodeList().keySet())
            {
                for (int k = 0; k <= MAX_LAND_SEA_TOTAL_PRICE; k++)
                {
                    minTimePriceToTargetHashMap.put(new Key4(i, j, k, true).hashCode(), -1);
                    maxTimePriceToTargetHashMap.put(new Key4(i, j, k, true).hashCode(), -1);
                    meanTimePriceToTargetHashMap.put(new Key4(i, j, k, true).hashCode(), -1);
                    minTimeToTargetHashMap.put(new Key4(i, j, k, true).hashCode(), -1);
                    maxTimeToTargetHashMap.put(new Key4(i, j, k, true).hashCode(), -1);
                    meanTimeToTargetHashMap.put(new Key4(i, j, k, true).hashCode(), -1);

                    minTimePriceToTargetHashMap.put(new Key4(i, j, k, false).hashCode(), -1);
                    maxTimePriceToTargetHashMap.put(new Key4(i, j, k, false).hashCode(), -1);
                    meanTimePriceToTargetHashMap.put(new Key4(i, j, k, false).hashCode(), -1);
                    minTimeToTargetHashMap.put(new Key4(i, j, k, false).hashCode(), -1);
                    maxTimeToTargetHashMap.put(new Key4(i, j, k, false).hashCode(), -1);
                    meanTimeToTargetHashMap.put(new Key4(i, j, k, false).hashCode(), -1);
                }
            }
        }
    }

    /*------------------------------------------------------------------------*/
    private void createConnectionsHashMapWithAccumulatedPrice(
        Node originNode,
        Node targetNode,
        int cumulativeTime,
        int cumulativePrice,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        if (!(isMultithreadingInUse))
        {
            if (shouldConnectionHashMapGenerationBeLogged)
            {
                writeTextAndNewlineToLog(
                    "originNode: " + originNode.getName()
                    + ", targetNode: " + targetNode.getName()
                    + ", cumulativeTime: " + cumulativeTime
                    + ", cumulativePrice: " + cumulativePrice
                    + ", currentMaxTotalPrice: " + currentMaxTotalPrice);
            }
        }

        connectionsHashMap.putIfAbsent(new Key4(
            originNode.ID,
            targetNode.ID,
            currentMaxTotalPrice,
            isUsingFreeSearoute).hashCode(),
            paranormalNodeHashMap.get(originNode.ID));

        // 0: start from node, cumulative price = 0.
        //    call recursively each neighboring node if distance there is negative (not yet defined) or greater than current distance.
        ArrayList<Integer> neighborIDSArrayList = originNode.getConnections();

        for (int neighborNodeID : neighborIDSArrayList)
        {
            Node neighborNode = c.getNode(neighborNodeID);

            if (neighborNode != originNode)
            {
                ParanormalNode neighborParanormalNode = getParanormalNode(neighborNode);

                if (isSearouteStart(originNode, neighborNode))
                {
                    // OK, the route is a ship departure.
                    // Both free searoute and non-free searoute must be
                    // taken into account.

                    // First, try free departure.                    
                    {
                        int newCumulativeTime;
                        boolean newIsUsingFreeSearoute = true;

                        newCumulativeTime = cumulativeTime + 1;

                        boolean isRecursiveCallNeeded = neighborParanormalNode.updateTimeAndPrice(
                            targetNode,
                            currentMaxTotalPrice,
                            newIsUsingFreeSearoute,
                            newCumulativeTime,
                            cumulativePrice,
                            timeHashMap,
                            priceHashMap);
                        if (isRecursiveCallNeeded)
                        {
                            // call recursively.
                            // neighbor node as the new origin node.
                            // keep the same target node.
                            // cumulative distance has already been incremented.
                            // new cumulative price is in newCumulativePrice.
                            // keep the original maximum total price.
                            createConnectionsHashMapWithAccumulatedPrice(
                                neighborNode,
                                targetNode,
                                newCumulativeTime,
                                cumulativePrice,
                                currentMaxTotalPrice,
                                newIsUsingFreeSearoute,
                                timeHashMap,
                                priceHashMap);
                        }
                    }

                    // Then, try non-free departure.
                    {
                        int newCumulativeTime;
                        boolean newIsUsingFreeSearoute = false;

                        if (timeHashMap == minTimeToTargetHashMap)
                        {
                            newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MAX_DICE;
                        }
                        else if (timeHashMap == maxTimeToTargetHashMap)
                        {
                            newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MIN_DICE;
                        }
                        else if (timeHashMap == meanTimeToTargetHashMap)
                        {
                            newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MEAN_DICE;
                        }
                        else
                        {
                            // Crash!
                            timeHashMap = priceHashMap = null;
                            newCumulativeTime = -1;
                        }

                        int newCumulativePrice = cumulativePrice + Globals.SEA_ROUTE_PRICE;

                        boolean isRecursiveCallNeeded = neighborParanormalNode.updateTimeAndPrice(
                            targetNode,
                            currentMaxTotalPrice,
                            newIsUsingFreeSearoute,
                            newCumulativeTime,
                            newCumulativePrice,
                            timeHashMap,
                            priceHashMap);

                        if (isRecursiveCallNeeded)
                        {
                            // call recursively.
                            // neighbor node as the new origin node.
                            // keep the same target node.
                            // cumulative distance has already been incremented.
                            // new cumulative price is in newCumulativePrice.
                            // keep the original maximum total price.
                            createConnectionsHashMapWithAccumulatedPrice(
                                neighborNode,
                                targetNode,
                                newCumulativeTime,
                                newCumulativePrice,
                                currentMaxTotalPrice,
                                newIsUsingFreeSearoute,
                                timeHashMap,
                                priceHashMap);
                        }
                    }
                }
                else
                {
                    // OK, this is not a ship departure.

                    int newCumulativeTime;

                    if (isUsingFreeSearoute)
                    {
                        newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MIN_DICE;
                    }
                    else
                    {
                        if (timeHashMap == minTimeToTargetHashMap)
                        {
                            newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MAX_DICE;
                        }
                        else if (timeHashMap == maxTimeToTargetHashMap)
                        {
                            newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MIN_DICE;
                        }
                        else if (timeHashMap == meanTimeToTargetHashMap)
                        {
                            newCumulativeTime = cumulativeTime + ONE_LINK_TIME_UNITS_FOR_MEAN_DICE;
                        }
                        else
                        {
                            // Crash!
                            timeHashMap = priceHashMap = null;
                            newCumulativeTime = -1;
                        }
                    }

                    boolean newIsUsingFreeSearoute;

                    if (isSearouteEnd(originNode, neighborNode))
                    {
                        newIsUsingFreeSearoute = false;
                    }
                    else
                    {
                        newIsUsingFreeSearoute = isUsingFreeSearoute;
                    }

                    int linkRoutePrice = getLinkRoutePrice(neighborNode, originNode);
                    if (linkRoutePrice < 0)
                    {
                        System.out.println("error: linkRoute from " + neighborNode.getName() + " to " + originNode.getName() + " is null!");
                    }
                    int newCumulativePrice = cumulativePrice + linkRoutePrice;

                    boolean isRecursiveCallNeeded = neighborParanormalNode.updateTimeAndPrice(
                        targetNode,
                        currentMaxTotalPrice,
                        newIsUsingFreeSearoute,
                        newCumulativeTime,
                        newCumulativePrice,
                        timeHashMap,
                        priceHashMap);

                    if (isRecursiveCallNeeded)
                    {
                        // call recursively.
                        // neighbor node as the new origin node.
                        // keep the same target node.
                        // cumulative distance has already been incremented.
                        // new cumulative price is in newCumulativePrice.
                        // keep the original maximum total price.
                        createConnectionsHashMapWithAccumulatedPrice(
                            neighborNode,
                            targetNode,
                            newCumulativeTime,
                            newCumulativePrice,
                            currentMaxTotalPrice,
                            newIsUsingFreeSearoute,
                            timeHashMap,
                            priceHashMap);
                    }
                }
            }
        }
    }

    /*------------------------------------------------------------------------*/
    private void createConnectionsHashMap(
        Node targetNode,
        int currentMaxTotalPrice,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        boolean isUsingFreeSeaRoute = false;
        createConnectionsHashMapWithAccumulatedPrice(
            targetNode,
            targetNode,
            INITIAL_TIME,
            INITIAL_PRICE,
            currentMaxTotalPrice,
            isUsingFreeSeaRoute,
            timeHashMap,
            priceHashMap);
    }

    /*------------------------------------------------------------------------*/
    private void createConnectionsHashMap(
        int targetNodeID,
        int currentMaxTotalPrice,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        boolean isUsingFreeSeaRoute = false;
        createConnectionsHashMap(
            c.getNode(targetNodeID),
            currentMaxTotalPrice,
            timeHashMap,
            priceHashMap);
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
    private boolean isSearouteStart(Node originNode, Node targetNode)
    {
        return ((!(originNode.isSea())) && targetNode.isSea());
    }

    /*------------------------------------------------------------------------*/
    private boolean isSearouteEnd(Node originNode, Node targetNode)
    {
        return (originNode.isSea() && (!(targetNode.isSea())));
    }

    /*------------------------------------------------------------------------*/
    private int getLinkRoutePrice(Node originNode, Node targetNode)
    {
        if (originNode.hasConnection(targetNode.ID))
        {
            if ((!(originNode.isSea())) && targetNode.isSea())
            {
                return Globals.SEA_ROUTE_PRICE;
            }
            return MAX_LAND_ROAD_PRICE;
        }
        else if (originNode.hasPlaneConnection(targetNode.ID))
        {
            return Globals.PLANE_ROUTE_PRICE;
        }
        return -1;
    }

    /*------------------------------------------------------------------------*/
    private int getTimeToTarget(
        int originNodeID,
        int targetNodeID,
        int cash,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        return paranormalNodeHashMap.get(originNodeID).getTimeToTarget(targetNodeID, cash, isUsingFreeSearoute, timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getTimeToTarget(
        Node originNode,
        Node targetNode,
        int cash,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        return getTimeToTarget(originNode.ID, targetNode.ID, cash, isUsingFreeSearoute, timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getTimeToTarget(
        int targetNodeID,
        int cash,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        return getTimeToTarget(c.getCurrentNode().ID, targetNodeID, cash, isUsingFreeSearoute, timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getTimeToTarget(
        Node targetNode,
        int cash,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        return getTimeToTarget(targetNode.ID, cash, isUsingFreeSearoute, timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getMinTimeToTarget(int originNodeID, int targetNodeID, int cash, boolean isUsingFreeSearoute)
    // get minimum time with given cash from _origin_ node to _target_ node.
    {
        return getTimeToTarget(originNodeID, targetNodeID, cash, isUsingFreeSearoute, minTimeToTargetHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getMinTimeToTarget(Node originNode, Node targetNode, int cash, boolean isUsingFreeSearoute)
    // get minimum time with given cash from _origin_ node to _target_ node.
    {
        return getMinTimeToTarget(originNode.ID, targetNode.ID, cash, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMinTimeToTarget(Node originNode, Node targetNode, boolean isUsingFreeSearoute)
    // get minimum time with current cash from _origin_ node to _target_ node.
    {
        return getMinTimeToTarget(originNode, targetNode, getCash(), isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMinTimeToTarget(Node targetNode, int cash, boolean isUsingFreeSearoute)
    // get minimum time with given cash from _current_ node to _target_ node.
    {
        return getMinTimeToTarget(c.getCurrentNode().ID, targetNode.ID, cash, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMinTimeToTarget(Node targetNode, boolean isUsingFreeSearoute)
    // get minimum time with current cash from _current_ node to _target_ node.
    {
        return getMinTimeToTarget(c.getCurrentNode(), targetNode, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMaxTimeToTarget(int originNodeID, int targetNodeID, int cash, boolean isUsingFreeSearoute)
    // get maximum time with given cash from _origin_ node to _target_ node.
    {
        return getTimeToTarget(originNodeID, targetNodeID, cash, isUsingFreeSearoute, maxTimeToTargetHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getMaxTimeToTarget(Node originNode, Node targetNode, int cash, boolean isUsingFreeSearoute)
    // get maximum time with given cash from _origin_ node to _target_ node.
    {
        return getMaxTimeToTarget(originNode.ID, targetNode.ID, cash, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMaxTimeToTarget(Node originNode, Node targetNode, boolean isUsingFreeSearoute)
    // get maximum time with current cash from _origin_ node to _target_ node.
    {
        return getMaxTimeToTarget(originNode, targetNode, getCash(), isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMaxTimeToTarget(Node targetNode, int cash, boolean isUsingFreeSearoute)
    // get maximum time with given cash from _current_ node to _target_ node.
    {
        return getMaxTimeToTarget(c.getCurrentNode().ID, targetNode.ID, cash, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMaxTimeToTarget(Node targetNode, boolean isUsingFreeSearoute)
    // get maximum time with current cash from _current_ node to _target_ node.
    {
        return getMaxTimeToTarget(c.getCurrentNode(), targetNode, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMeanTimeToTarget(int originNodeID, int targetNodeID, int cash, boolean isUsingFreeSearoute)
    // get mean time with given cash from _origin_ node to _target_ node.
    {
        return getTimeToTarget(originNodeID, targetNodeID, cash, isUsingFreeSearoute, meanTimeToTargetHashMap);
    }

    /*------------------------------------------------------------------------*/
    private int getMeanTimeToTarget(Node originNode, Node targetNode, int cash, boolean isUsingFreeSearoute)
    // get mean time with given cash from _origin_ node to _target_ node.
    {
        return getMeanTimeToTarget(originNode.ID, targetNode.ID, cash, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMeanTimeToTarget(Node originNode, Node targetNode, boolean isUsingFreeSearoute)
    // get mean time with current cash from _origin_ node to _target_ node.
    {
        return getMeanTimeToTarget(originNode, targetNode, getCash(), isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMeanTimeToTarget(Node targetNode, int cash, boolean isUsingFreeSearoute)
    // get mean time with given cash from _current_ node to _target_ node.
    {
        return getMeanTimeToTarget(c.getCurrentNode().ID, targetNode.ID, cash, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    private int getMeanTimeToTarget(Node targetNode, boolean isUsingFreeSearoute)
    // get time with current cash from _current_ node to _target_ node.
    {
        return getMeanTimeToTarget(c.getCurrentNode(), targetNode, isUsingFreeSearoute);
    }

    /*------------------------------------------------------------------------*/
    // the price of the shortest route from originNode to any metropol.
    private int getPriceOfFastestRouteToMetropol(ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        int timeOfChosenRoute = -1;
        int priceOfChosenRoute = -1;

        for (int metropolNodeID : METROPOLS_ARRAY)
        {
            int timeOfCurrentRoute = paranormalNodeHashMap.get(c.getCurrentNode().ID).
                getTimeToTarget(
                    metropolNodeID,
                    getCash(),
                    c.isUsingFreeSeaRoute(),
                    timeHashMap);

            int priceOfCurrentRoute = paranormalNodeHashMap.get(c.getCurrentNode().ID).
                getPriceToTarget(
                    metropolNodeID,
                    getCash(),
                    c.isUsingFreeSeaRoute(),
                    timeHashMap);
            boolean isUpdateNeeded = false;

            if (priceOfCurrentRoute >= 0)
            {
                if (timeOfChosenRoute < 0)
                {
                    isUpdateNeeded = true;
                }
                else if ((timeOfChosenRoute == timeOfCurrentRoute) && (priceOfChosenRoute > priceOfCurrentRoute))
                {
                    isUpdateNeeded = true;
                }

                if (isUpdateNeeded)
                {
                    timeOfChosenRoute = timeOfCurrentRoute;
                    priceOfChosenRoute = priceOfCurrentRoute;
                }
            }
        }
        return priceOfChosenRoute;
    }

    /*------------------------------------------------------------------------*/
    private float convertTimeUnitsToTurns(float timeUnits)
    {
        return (float) timeUnits / DICE_LCM;
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Global game state reasoning methods.                                    |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private int getCash()
    {
        return c.getMyBalance();
    }

    /*------------------------------------------------------------------------*/
    private int getMinTreasureValue()
    {
        // If there are robbers left, the minimum treasure value is
        // (-1) * current cash.
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
        // The minimum treasure value without taking robbers into count,
        // should always be a non-negative integer.
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
            // Check zeitgebers.
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
        return route.getPrice();
    }

    /*------------------------------------------------------------------------*/
    private int getCashAfterRoute(Route route)
    {
        return getCash() - getRoutePrice(route);
    }

    /*------------------------------------------------------------------------*/
    private boolean areThereTreasuresLeft()
    {
        return (c.getRemainingTreasures().size() > 0);
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Combined movement & token-trying methods.                               |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void searchForTreasureWithoutCash(
        String messagePrefix,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
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
            moveTowardsClosestTreasureInTime(messagePrefix, timeHashMap);
            doEndTurn();
        }
        else
        {
            doRandomLandMovement();
        }
    }

    /*------------------------------------------------------------------------*/
    private void searchForTreasureWithoutCash(
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        searchForTreasureWithoutCash("", timeHashMap);
    }

    /*-------------------------------------------------------------------------\
     |                                                                         |
     | Movement methods.                                                       |
     |                                                                         |
     \------------------------------------------------------------------------*/
    private void moveTowardsClosestTreasureInTime(
        String messagePrefix,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        // TODO: implement travel to treasure cities also with flights.

        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList = c.getMyAvailableRoutes();
        ArrayList<Node> treasureCitiesArrayList = c.getRemainingTreasures();

        boolean randomChoiceInUse = false;

        ArrayList<Route> chosenRoutesArrayList = new ArrayList<>();
        ArrayList<Node> chosenTreasureCitiesArrayList = new ArrayList<>();

        Route chosenRoute = null;
        Node chosenTreasureCity = null;
        int timeFromChosenDestinationToTreasureCity = -1;
        int chosenRoutePrice = -1;

        for (Route route : routesArrayList)
        {
            for (Node treasureCity : treasureCitiesArrayList)
            {
                int timeFromCurrentDestinationToTreasureCity = getTimeToTarget(
                    route.getDestination(),
                    treasureCity,
                    getCashAfterRoute(route),
                    isFreeSearoute(route),
                    timeHashMap);
                int currentRoutePrice = route.getPrice();
                boolean isUpdateNeeded = false;
                boolean isBetterRoute = false;

                writeTextAndNewlineToLog("time from " + route.getDestination().getName()
                                         + " to " + treasureCity.getName()
                                         + " is " + timeFromCurrentDestinationToTreasureCity
                                         + " turns.");

                if (timeFromCurrentDestinationToTreasureCity >= 0)
                {
                    if (timeFromChosenDestinationToTreasureCity < 0)
                    {
                        // If chosen route is not valid,
                        // any valid route is better.
                        isUpdateNeeded = true;
                        isBetterRoute = true;
                    }
                    else if (timeFromCurrentDestinationToTreasureCity < timeFromChosenDestinationToTreasureCity)
                    {
                        // If current destination has shorter distance to
                        // treasure city than chosen destination, current
                        // route is better.
                        isUpdateNeeded = true;
                        isBetterRoute = true;
                    }
                    else if ((timeFromChosenDestinationToTreasureCity == timeFromCurrentDestinationToTreasureCity)
                             && (currentRoutePrice < chosenRoutePrice))
                    {
                        // If current destination and chosen destination
                        // have the same distance to nearest treasure city
                        // and current route is cheaper than chosen route,
                        // current route is better.
                        isUpdateNeeded = true;
                        isBetterRoute = true;
                    }
                    else if ((timeFromChosenDestinationToTreasureCity == timeFromCurrentDestinationToTreasureCity)
                             && (chosenRoutePrice == currentRoutePrice))
                    {
                        // If current destination and chosen destination
                        // have the same distance to nearest treasure city
                        // and both have the same price, they are equally
                        // good.
                        isUpdateNeeded = true;
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
                    chosenRoutePrice = currentRoutePrice;
                    timeFromChosenDestinationToTreasureCity = timeFromCurrentDestinationToTreasureCity;
                }
            }
        }

        if (randomChoiceInUse)
        {
            if ((!(chosenRoutesArrayList.isEmpty()))
                && (!(chosenTreasureCitiesArrayList.isEmpty()))
                && (chosenRoutesArrayList.size() == chosenTreasureCitiesArrayList.size()))
            {
                int randomIndex = rand.nextInt(chosenRoutesArrayList.size());
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
            float timeUnitsInTurns = convertTimeUnitsToTurns(timeFromChosenDestinationToTreasureCity);
            writeTextAndNewlineToLogAndDebug(messagePrefix + "I'm taking route to " + chosenRoute.getDestination().getName()
                                             + ", en route to " + chosenTreasureCity.getName()
                                             + " (" + timeFromChosenDestinationToTreasureCity
                                             + " timeUnits/" + timeUnitsInTurns + " turns remaining), price " + chosenRoutePrice + " GBP.");
            executeRoute(chosenRoute);
        }
    }

    /*------------------------------------------------------------------------*/
    private void moveTowardsClosestTreasureInTime(
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        moveTowardsClosestTreasureInTime("", timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private boolean isFreeSearoute(Node originNode, Node targetNode, int cash)
    {
        return ((cash == 0)
                && (!(originNode.isSea()))
                && targetNode.isSea());
    }

    /*------------------------------------------------------------------------*/
    private boolean isFreeSearoute(Node originNode, Node targetNode)
    {
        return isFreeSearoute(originNode, targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private boolean isFreeSearoute(Node targetNode, int cash)
    {
        return isFreeSearoute(c.getCurrentNode(), targetNode, cash);
    }

    /*------------------------------------------------------------------------*/
    private boolean isFreeSearoute(Node targetNode)
    {
        return isFreeSearoute(c.getCurrentNode(), targetNode, getCash());
    }

    /*------------------------------------------------------------------------*/
    private boolean isFreeSearoute(Node originNode, Route route)
    {
        return isFreeSearoute(originNode, route.getDestination());
    }

    /*------------------------------------------------------------------------*/
    private boolean isFreeSearoute(Route route)
    {
        return isFreeSearoute(c.getCurrentNode(), route);
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
    private Node chooseFarthestCity(Node originNode, int currentMaxTotalPrice, boolean isUsingFreeSeaRoute)
    {
        ArrayList<Node> allCitiesArrayList = c.getAllCities();
        int chosenDistance = -1;
        Node chosenNode = null;

        for (Node targetNode : allCitiesArrayList)
        {
            int timeFromCurrentNode = getTimeToTarget(
                originNode,
                targetNode,
                currentMaxTotalPrice,
                isUsingFreeSeaRoute,
                maxTimeToTargetHashMap);
            if (timeFromCurrentNode >= 0)
            {
                if ((chosenDistance < 0) || (chosenDistance < timeFromCurrentNode))
                {
                    chosenDistance = timeFromCurrentNode;
                    chosenNode = targetNode;
                }
            }
        }
        return chosenNode;
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity(Node originNode, boolean isUsingFreeSeaRoute)
    {
        return chooseFarthestCity(originNode, getCash(), isUsingFreeSeaRoute);
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity(int currentMaxTotalPrice, boolean isUsingFreeSeaRoute)
    {
        return chooseFarthestCity(c.getCurrentNode(), currentMaxTotalPrice, isUsingFreeSeaRoute);
    }

    /*------------------------------------------------------------------------*/
    private Node chooseFarthestCity()
    {
        return chooseFarthestCity(c.getCurrentNode(), getCash(), c.isUsingFreeSeaRoute());
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
                nextDestinationOfAfricaTour = chooseFarthestCity(MAX_LAND_ROAD_PRICE, c.isUsingFreeSeaRoute());
            }
            doLandSeaTravelTowards(
                nextDestinationOfAfricaTour,
                MAX_LAND_ROAD_PRICE,
                "I'm on Africa Tour en route to " + nextDestinationOfAfricaTour.getName() + ".",
                maxTimeToTargetHashMap);
        }
        else
        {
            Node chosenNode = chooseFarthestCity(MAX_LAND_ROAD_PRICE, c.isUsingFreeSeaRoute());

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
            writeTextAndNewlineToLogAndDebug(
                "I am at " + c.getCurrentNodeName()
                + " and I have no available routes.");
        }
        else
        {
            ArrayList<Route> routesArrayList = c.getMyAvailableRoutes();
            Route route = routesArrayList.get(0);
            writeTextAndNewlineToLogAndDebug(
                "I am at " + c.getCurrentNodeName()
                + " and I take the first available route to " + route.getDestination().getName() + ".");
            executeRoute(route);
        }
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(
        Node targetNode,
        int currentMaxTotalPrice,
        String messagePrefix,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        c.decideToUseLandOrSeaRoute();

        ArrayList<Route> routesArrayList;
        routesArrayList = c.getMyAvailableRoutes();

        if (!(routesArrayList.isEmpty()))
        {
            Route chosenRoute = null;
            int landSeaDistanceFromChosenNodeToTargetNode = -1;

            for (Route currentRoute : routesArrayList)
            {
                Node routeNode;
                routeNode = currentRoute.getDestination();

                int timeFromDestinationToTargetNode;
                boolean isFreeSeaRoute = isFreeSearoute(currentRoute);
                timeFromDestinationToTargetNode = getTimeToTarget(routeNode, targetNode, getCash(), isFreeSeaRoute, timeHashMap);

                float timeUnitsInTurns = convertTimeUnitsToTurns(timeFromDestinationToTargetNode);
                writeTextAndNewlineToLog("Time from destination " + routeNode.getName()
                                         + " to " + targetNode.getName()
                                         + " is " + timeFromDestinationToTargetNode
                                         + " time units (" + timeUnitsInTurns + " turns, price : " + currentRoute.getPrice() + " GBP)");

                boolean isUpdateNeeded = false;

                if (timeFromDestinationToTargetNode >= 0)
                {
                    if (landSeaDistanceFromChosenNodeToTargetNode < 0)
                    {
                        isUpdateNeeded = true;
                    }
                    else
                    {
                        isUpdateNeeded = (timeFromDestinationToTargetNode < landSeaDistanceFromChosenNodeToTargetNode);
                    }
                }

                if (isUpdateNeeded)
                {
                    chosenRoute = currentRoute;
                    landSeaDistanceFromChosenNodeToTargetNode = timeFromDestinationToTargetNode;
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
    private void doLandSeaTravelTowards(
        Node targetNode,
        int currentMaxTotalPrice,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        doLandSeaTravelTowards(targetNode, currentMaxTotalPrice, "", timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(
        Node targetNode,
        String messagePrefix,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        doLandSeaTravelTowards(targetNode, getCash(), messagePrefix, timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(
        Node targetNode,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        doLandSeaTravelTowards(targetNode, getCash(), timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(
        int targetNodeID,
        String messagePrefix,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        doLandSeaTravelTowards(c.getNode(targetNodeID), messagePrefix, timeHashMap);
    }

    /*------------------------------------------------------------------------*/
    private void doLandSeaTravelTowards(
        int targetNodeID,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        doLandSeaTravelTowards(targetNodeID, "", timeHashMap);
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
        if ((getPriceOfFastestRouteToMetropol(minTimeToTargetHashMap) >= 0)
            || (getPriceOfFastestRouteToMetropol(maxTimeToTargetHashMap) >= 0)
            || (getPriceOfFastestRouteToMetropol(maxTimeToTargetHashMap) >= 0))
        {
            return ((getMinCashAfterBuyingToken() >= getPriceOfFastestRouteToMetropol(minTimeToTargetHashMap))
                    && (getMinCashAfterBuyingToken() >= getPriceOfFastestRouteToMetropol(maxTimeToTargetHashMap))
                    && (getMinCashAfterBuyingToken() >= getPriceOfFastestRouteToMetropol(maxTimeToTargetHashMap)));
        }
        else
        {
            return false;
        }
    }

    /*------------------------------------------------------------------------*/
    private int computeLCM(int a, int b)
    {
        // This method computes least common divisor for 2 integers.
        //
        // TODO: write the code to compute least common.
        // For now, just return the product of the numbers.
        return a * b;
    }

    /*-------------------------------------------------------------------------*
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
