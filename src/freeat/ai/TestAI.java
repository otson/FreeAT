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

    public TestAI() {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("TestAI");
        count = (int) (Math.random() * 2 + 1);
        //count++;
    }

    @Override
    public void act(Controller c) {
        while (!c.isEndTurn()) {
            if (!distancesSet) {
                distancesSet = true;
                long start = System.nanoTime();
                initDistances(c);
                System.out.println("Time to calculate all distances: " + (System.nanoTime() - start) / 1000000 + " ms.");
                distances.checkRoutes();
                
                System.exit(0);
            }

        }
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
        return "ReittiOtso";
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
