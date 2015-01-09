/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Globals;
import freeat.Node;
import freeat.Route;
import freeat.TreasureType;
import freeat.ai.normalaiNode.DistanceListList;
import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class TestAI extends AI
{

    public static int count = (int) (Math.random() * 2 + 1);
    private static DistanceListList distances;
    private static boolean distancesSet = false;
    private int targetNode;

    public TestAI()
    {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("TestAI");
        count = (int) (Math.random() * 2 + 1);
        targetNode = 120;
    }

    @Override
    public void act()
    {
        while (!c.isEndTurn())
        {
            if (!distancesSet)
            {
                distancesSet = true;
                long start = System.nanoTime();
                initDistances(c);
                System.out.println("Time to calculate all distances: " + (System.nanoTime() - start) / 1000000 + " ms.");
                distances.checkRoutes();
            }
            if (c.getCurrentNode().hasTreasure())
            {
                c.decideTryToken();
            } else
            {

                Route route;

                if (!c.isEligibleForWin())
                {
                    c.decideToUseLandOrSeaRoute();
                    if (isTreasuresOnMainLand())
                    {
                        route = getRouteToTreasure(0);
                    } else
                    {
                        route = getRouteToTreasure(1);
                    }
                } else
                {
                    if (shouldFlyToStart())
                    {
                        c.decidetoUsePlane();
                        route = getPlaneRouteToStart();

                    } else
                    {
                        c.decideToUseLandOrSeaRoute();
                        route = getRouteToStart(1);
                    }

                }

                c.moveTo(route);
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
        return c.getMyBalance() >= Globals.TREASURE_BUYING_PRICE && c.getCurrentNode().hasTreasure();
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
            if (distances.dist(c.getCurrentNode().ID, node.ID, maxPrice) > -1)
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
            int candidateDistance = distances.dist(c.getCurrentNode().ID, nodeCandidate.ID, maxPrice);

            // find the route with shortest distance
            for (Node node : treasures)
            {
                // distance is shorter than the candidate and longer than -1, it becomes the new candidate
                if (distances.dist(c.getCurrentNode().ID, node.ID, maxPrice) > -1 && distances.dist(c.getCurrentNode().ID, node.ID, maxPrice) < candidateDistance)
                {
                    nodeCandidate = node;
                    candidateDistance = distances.dist(c.getCurrentNode().ID, node.ID, maxPrice);
                }
            }

            return getRouteTo(nodeCandidate.ID, maxPrice);
        }

    }

    private Route getRouteToStart(int maxPrice)
    {

        int distanceToCairo = distances.dist(c.getCurrentNode().ID, 1, maxPrice);
        int distanceToTangier = distances.dist(c.getCurrentNode().ID, 2, maxPrice);
        if (distanceToCairo < distanceToTangier)
        {
            return getRouteTo(1, maxPrice);
        } else
        {
            return getRouteTo(2, maxPrice);
        }
    }

    private Route getRouteTo(int to, int maxPrice)
    {
        //System.out.println("At: " + c.getCurrentNodeName());
        c.setDebugString(" Heading towards: " + c.getNodeList().get(to).getName());
        if (c.isEligibleForWin())
        {
            c.concatDebugString(" with the Star or HorseShoe.");
        }
        //ArrayList<Route> routes = c.getAllRoutes(c.getCurrentNode(), maxPrice, c.getDice());
        ArrayList<Route> routes;
        if (maxPrice == 0)
        {
            routes = c.getMyAvailableFreeRoutesNoSea();
        } else
        {
            routes = c.getMyAvailableRoutes();
        }

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
        Route routeCandidate;
        if (maxPrice == 0)
        {
            if (!routes.isEmpty())
            {

                routeCandidate = routes.get(0);

                // distance from route's destination to the wanted to node
                int distanceCandidate = distances.distNoSea(routeCandidate.getDestination().ID, to);

                for (Route route : routes)
                {
                    // check if the the route in the list has shorter distance to target
                    if (distances.distNoSea(route.getDestination().ID, to) < distanceCandidate)
                    {
                        routeCandidate = route;
                        distanceCandidate = distances.distNoSea(route.getDestination().ID, to);
                    }
                }
            } else
            {
                routeCandidate = c.getMyAvailableRoutes().get(0);
            }
        } else
        {
            if (!routes.isEmpty())
            {

                routeCandidate = routes.get(0);

                // distance from route's destination to the wanted to node
                int distanceCandidate = distances.dist(routeCandidate.getDestination().ID, to);

                for (Route route : routes)
                {
                    // check if the the route in the list has shorter distance to target
                    if (distances.dist(route.getDestination().ID, to) < distanceCandidate)
                    {
                        routeCandidate = route;
                        distanceCandidate = distances.dist(route.getDestination().ID, to);
                    }
                }
            } else
            {
                routeCandidate = c.getMyAvailableRoutes().get(0);
            }
        }
        //System.out.println("Selected route distance: "+distanceCandidate);
        return routeCandidate;

    }

    public boolean isTreasuresOnMainLand()
    {
        if (!c.getRemainingTreasures().isEmpty())
        {
            for (Node node : c.getRemainingTreasures())
            {
                if (distances.distNoSea(node.ID, 1) > -1)
                {
                    //System.out.println("Treasure left");
                    return true;
                }
            }
        }
        //System.out.println("No treasure left");
        return false;
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

    // return boolean if the parameter node has a land connection to either of the start cities
    private int getExpectedTreasureValue()
    {
        int sum = 0;
        return sum;
    }

    private boolean shouldFlyToStart()
    {
        if (c.getCurrentNode().getPlaneRoutes().isEmpty() || c.getMyBalance() < Globals.PLANE_ROUTE_PRICE)
        {
            return false;
        }
        if (GetPlaneAdvantageToStart() < Globals.MAX_DICE_VALUE)
        {
            return false;
        }
        return true;
    }

    private Route getPlaneRouteToStart()
    {
        Route route = c.getCurrentNode().getPlaneRoutes().get(0);
        int distanceToStart = distances.dist(route.getDestination().ID, 1, 1);
        if (distances.dist(route.getDestination().ID, 2, 1) < distanceToStart)
        {
            distanceToStart = distances.dist(route.getDestination().ID, 1, 2);
        }
        for (Route plane : c.getCurrentNode().getPlaneRoutes())
        {
            if (distances.dist(plane.getDestination().ID, 1, 1) < distanceToStart)
            {
                route = plane;
                distanceToStart = distances.dist(plane.getDestination().ID, 1, 1);
            }
            if (distances.dist(plane.getDestination().ID, 2, 1) < distanceToStart)
            {
                route = plane;
                distanceToStart = distances.dist(plane.getDestination().ID, 2, 1);
            }

        }
        return route;
    }

    private int GetPlaneAdvantageToStart()
    {
        Route route = c.getCurrentNode().getPlaneRoutes().get(0);
        int distanceGained = distances.dist(route.getDestination().ID, c.getCurrentNode().ID, 1);

        for (Route plane : c.getCurrentNode().getPlaneRoutes())
        {
            if (distances.dist(plane.getDestination().ID, 1, 1) < distanceGained)
            {
                distanceGained = distances.dist(plane.getDestination().ID, c.getCurrentNode().ID, 1);
            }

        }
        return distanceGained;
    }

}
