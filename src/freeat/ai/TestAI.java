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
public class TestAI extends AI {

    static int count = 2;//(int) (Math.random() * 2 + 1);
    private static DistanceListList distances;
    private static boolean distancesSet = false;
    private Controller c;

    private int targetNode;

    public TestAI() {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("TestAI");
        //count = (int) (Math.random() * 2 + 1);
        targetNode = 120;
        //count++;
    }

    @Override
    public void act(Controller c) {
        this.c = c;
        while (!c.isEndTurn()) {
            if (!distancesSet) {
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
            if (c.getCurrentNode().hasTreasure()) {
                System.out.println("Trying to win treasure");
                c.decideTryToken();
            } else {
                c.decideToUseLandOrSeaRoute();
                Route route = getRouteToTreasure(0);

                c.moveTo(route);
                c.endTurn();

                // at cape town, head to Cairo
                if (c.getCurrentNode().ID == targetNode) {
                    if (targetNode == 120) {
                        targetNode = 1;
                    } else if (targetNode == 1) {
                        targetNode = 120;
                    }
                }
            }
        }
    }

    private Route getRouteToTreasure(int maxPrice) {
        ArrayList<Node> treasures = c.getRemainingTreasures();
        System.out.println("Remaining treasures: "+treasures.size());
        // find the closest node with treasure
        // find first accessible node with treasure
        Node nodeCandidate = null;
        for (Node node : treasures) {

            // if found a route longer than -1, it's valid candidate
            if (distances.getDistance(c.getCurrentNode().ID, node.ID) > -1) {
                nodeCandidate = node;
                break;
            }
        }

        // If no routes available to any treasures
        if (nodeCandidate == null) {
            return getRouteTo(targetNode, 0);
        } else {
            return getRouteTo(nodeCandidate.ID, maxPrice);
        }

    }

    private Route getRouteTo(int to, int maxPrice) {
        ArrayList<Route> routes = c.getAvailableRoutes(c.getCurrentNode(), maxPrice, c.getDice());
        if (routes == null) {
            System.out.println("No routes: null");
        }
        // check if direct route is available
        for (Route route : routes) {
            if (route.getDestination().ID == to) {
                return route;
            }
        }

        // Use the first route as the first candidate
        Route routeCandidate = routes.get(0);

        // distance from route's destination to the wanted to node
        int distanceCandidate = distances.getDistance(routeCandidate.getDestination().ID, to);

        for (Route route : routes) {
            // check if the the route in the list has shorter distance to target
            if (distances.getDistance(route.getDestination().ID, to) < distanceCandidate) {
                routeCandidate = route;
                distanceCandidate = distances.getDistance(route.getDestination().ID, to);
            }
        }
        System.out.println("Selected route distance: "+distanceCandidate);
        return routeCandidate;

    }

    private void initDistances(Controller c) {
        distances = new DistanceListList(c.getNodeList());

    }

    @Override
    public float getR() {
        return 1;
    }

    @Override
    public float getG() {
        return 1f;
    }

    @Override
    public float getB() {
        return 0f;
    }

    @Override
    public String getName() {
        return "ReittiTest";
    }

    private boolean containsNode(ArrayList<Route> possibleRoutes, Node selectedNode) {
        for (Route route : possibleRoutes) {
            if (route.getDestination() == selectedNode) {
                return true;
            }
        }
        return false;
    }

    private Route getRoute(ArrayList<Route> possibleRoutes, Node selectedNode) {
        for (Route route : possibleRoutes) {
            if (route.getDestination() == selectedNode) {
                return route;
            }
        }
        System.out.println("route did not contain given node");
        return null;
    }

}
