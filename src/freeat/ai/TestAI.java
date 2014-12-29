/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Node;
import freeat.Route;
import freeat.ai.normalaiNode.DistanceListList;
import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class TestAI extends AI
{

    public static int count = 1;//(int) (Math.random() * 2 + 1);
    private static DistanceListList distances;
    private static boolean distancesSet = false;
    private int targetNode;

    public TestAI()
    {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("TestAI");
        //count = (int) (Math.random() * 2 + 1);
        targetNode = 120;
        count++;
    }

    @Override
    public void act()
    {
        while (!c.isEndTurn())
        {
            //turnCount++;
            if (!distancesSet)
            {
                distancesSet = true;
                long start = System.nanoTime();
                initDistances(c);
                System.out.println("Time to calculate all distances: " + (System.nanoTime() - start) / 1000000 + " ms.");
                distances.checkRoutes();
//                distances.printDistance(114, 243);
//                distances.printDistance(114, 110);
//                //distances.printDistance(1, 108);
//
//                System.exit(0);
            }
            if (c.getCurrentNode().hasTreasure())
            {
                c.decideTryToken();
            } else
            {
                c.decideToUseLandOrSeaRoute();
                ArrayList<Route> test = c.getAvailableRoutes(c.getNode(523), 200, 2);
                System.out.println("Routes from 523: ");
                for(Route route : test){
                    System.out.println(route.getString());
                }
                System.exit(0);
                
                Route route;
                if (!c.isEligibleForWin())
                {
                    route = getRouteToTreasure(0);
                } else
                {
                    route = getRouteTo(1, 0);
                }

                c.moveTo(route);
//                if (c.isEligibleForWin() && c.getCurrentNode().ID == 1)
//                {
//                    System.out.println("Turns to win: " + turnCount);
//                }
                if (canBuyTreasure())
                {
                    c.buyToken();
                } else
                {
                    c.endTurn();
                }

                // at cape town, head to Cairo
                if (c.getCurrentNode().ID == targetNode)
                {
                    if (targetNode == 120)
                    {
                        targetNode = 1;
                    } else if (targetNode == 1)
                    {
                        targetNode = 120;
                    }
                }
            }
        }
    }

    public boolean canBuyTreasure()
    {
        return c.getMyBalance() >= 100 && c.getCurrentNode().hasTreasure();
    }

    private Route getRouteToTreasure(int maxPrice)
    {
        ArrayList<Node> treasures = c.getRemainingTreasures();
        // find the closest node with treasure
        // find first accessible node with treasure
        Node nodeCandidate = null;
        for (Node node : treasures)
        {

            // if found a route longer than -1, it's valid candidate
            if (distances.getDistance(c.getCurrentNode().ID, node.ID) > -1)
            {
                nodeCandidate = node;
                break;
            }
        }

        // If no routes available to any treasures
        if (nodeCandidate == null)
        {
            return getRouteTo(targetNode, 0);
        } else
        {
            int candidateDistance = distances.getDistance(c.getCurrentNode().ID, nodeCandidate.ID);
            
            // find the route with shortest distance
            for (Node node : treasures)
            {
                // distance is shorter than the candidate and longer than -1, it becomes the new candidate
                if (distances.getDistance(c.getCurrentNode().ID, node.ID) > -1 &&  distances.getDistance(c.getCurrentNode().ID, node.ID) < candidateDistance)
                {
                    nodeCandidate = node;
                    candidateDistance = distances.getDistance(c.getCurrentNode().ID, node.ID);
                }
            }

            return getRouteTo(nodeCandidate.ID, maxPrice);
        }

    }

    private Route getRouteToStart(int maxPrice)
    {
        int distanceToCairo = distances.getDistance(c.getCurrentNode().ID, 1);
        int distanceToTangier = distances.getDistance(c.getCurrentNode().ID, 2);
        if (distanceToCairo < distanceToTangier)
        {
            return getRouteTo(2, maxPrice);
        } else
        {
            return getRouteTo(1, maxPrice);
        }
    }

    private Route getRouteTo(int to, int maxPrice)
    {
        c.setDebugString(" Heading towards: " + c.getNodeList().get(to).getName());
        if (c.isEligibleForWin())
        {
            c.concatDebugString(" with the Star or HorseShoe.");
        }
        ArrayList<Route> routes = c.getAvailableRoutes(c.getCurrentNode(), maxPrice, c.getDice());
        if (routes == null)
        {
            System.out.println("No routes: null");
        }
        // check if direct route is available
        for (Route route : routes)
        {
            if (route.getDestination().ID == to)
            {
                return route;
            }
        }

        // Use the first route as the first candidate
        Route routeCandidate = routes.get(0);

        // distance from route's destination to the wanted to node
        int distanceCandidate = distances.getDistance(routeCandidate.getDestination().ID, to);

        for (Route route : routes)
        {
            // check if the the route in the list has shorter distance to target
            if (distances.getDistance(route.getDestination().ID, to) < distanceCandidate)
            {
                routeCandidate = route;
                distanceCandidate = distances.getDistance(route.getDestination().ID, to);
            }
        }
        //System.out.println("Selected route distance: "+distanceCandidate);
        return routeCandidate;

    }

    private void initDistances(Controller c)
    {
        distances = new DistanceListList(c.getNodeList());

    }

    @Override
    public float getR()
    {
        return 1;
    }

    @Override
    public float getG()
    {
        return 1f;
    }

    @Override
    public float getB()
    {
        return 0f;
    }

    @Override
    public String getName()
    {
        return "ReittiTest";
    }

    private boolean containsNode(ArrayList<Route> possibleRoutes, Node selectedNode)
    {
        for (Route route : possibleRoutes)
        {
            if (route.getDestination() == selectedNode)
            {
                return true;
            }
        }
        return false;
    }

    private Route getRoute(ArrayList<Route> possibleRoutes, Node selectedNode)
    {
        for (Route route : possibleRoutes)
        {
            if (route.getDestination() == selectedNode)
            {
                return route;
            }
        }
        System.out.println("route did not contain given node");
        return null;
    }

}
