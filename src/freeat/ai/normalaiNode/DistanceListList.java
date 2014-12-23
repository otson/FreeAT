package freeat.ai.normalaiNode;

import freeat.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author otso
 */
public class DistanceListList {

    private HashMap<Integer, DistanceList> distances = new HashMap<>();

    public DistanceListList(HashMap<Integer, Node> nodeList) {

        // Give all the nodes in the list a list of all the other nodes, with a starting distance of -1 (not reachable)
        for (Node node : nodeList.values()) {
            HashMap<Integer, Integer> distancesForNode = new HashMap<>();
            for (Node node2 : nodeList.values()) {
                if (node.ID != node2.ID) {
                    distancesForNode.put(node2.ID, -1);
                }
            }
            distances.put(node.ID, new DistanceList(node.ID, distancesForNode, nodeList));
        }

        // set free routes with a distance of 1
        for (DistanceList nodesList : distances.values()) {
            HashMap<Integer, Integer> nodeDistances = nodesList.getDistances();
            Node currentNode = nodeList.get(nodesList.ID);
            ArrayList<Integer> connections = currentNode.getConnections();
            for (Integer destination : connections) {
                Node destNode = nodeList.get(destination);
                // check that the route is free
                if (!(currentNode.isCity() && destNode.isSea())) {
                    nodeDistances.put(destination, 1);
                }

            }

        }
        
        // calculate rest of the distances in the Distancelist of method
        for (DistanceList nodesList : distances.values()) {
            nodesList.calculateDistances();
        }

    }
    
    public int getDistance(int from, int to){
        int distance = distances.get(from).getDistances().get(to);
        
        return distance;
    }

}
