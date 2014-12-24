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

    static int count = (int) (Math.random() * 2 + 1);
    private static DistanceListList distances;
    private static boolean distancesSet = false;
    private Controller c;

    private int targetNode;

    public TestAI() {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("TestAI");
        count = (int) (Math.random() * 2 + 1);
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
//                distances.printDistance(1, 108);
//                distances.printDistance(262, 108);
//                distances.printDistance(261, 108);
//                distances.printDistance(260, 108);
//                distances.printDistance(109, 108);
//                distances.printDistance(259, 108);
//                distances.printDistance(258, 108);
//                distances.printDistance(114, 108);
//                distances.printDistance(243, 108);
//                distances.printDistance(110, 108);
//                distances.printDistance(244, 108);
//                System.exit(0);
            }
            c.decideToUseLandOrSeaRoute();
            Route route = getRouteTo(targetNode, 0);
            c.moveTo(route);
            c.endTurn();
            // at cape town, head to Cairo
            if (c.getCurrentNode().ID == targetNode) {
                if(targetNode == 120){
                    targetNode = 1;
                }
                else if(targetNode == 1){
                    targetNode = 120;
                }
            }
        }
    }

    private Route getRouteTo(int to, int maxPrice) {
        ArrayList<Route> routes = c.getAvailableRoutes(c.getCurrentNode(), maxPrice, c.getDice());
        if(routes == null)
            System.out.println("No routes: null");
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
