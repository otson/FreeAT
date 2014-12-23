/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai.normalaiNode;

import java.util.HashMap;

/**
 *
 * @author otso
 */
public class DistanceList {
    // key is target nodeID, value is distance
    private HashMap<Integer, Integer> distances;
    public int ID;

    public DistanceList() {

    }

    DistanceList(int id,HashMap<Integer, Integer> distancesForNode) {
        this.ID = id;
        this.distances = distancesForNode;
    }

    public HashMap<Integer, Integer> getDistances() {
        return distances;
    }
    
    
    
}
