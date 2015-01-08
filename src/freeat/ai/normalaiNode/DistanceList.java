/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai.normalaiNode;

import freeat.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author otso
 */
public class DistanceList
{

    // key is target nodeID, value is distance
    private HashMap<Integer, Integer> distances;
    private HashMap<Integer, Node> nodeList;
    public int ID;
    public ArrayList<Integer>[] array;

    DistanceList(int ID, HashMap<Integer, Integer> distancesForNode, HashMap<Integer, Node> nodeList)
    {
        this.ID = ID;
        this.distances = distancesForNode;
        this.nodeList = nodeList;
    }

    public HashMap<Integer, Integer> getDistances()
    {
        return distances;
    }

    public void calculateDistances()
    {

        for (int i = 2; i < 43; i++)
        {
            for (int targetNodeID : distances.keySet())
            {

                // if sea, no route is currently set
                if (targetNodeID < 500)
                {
                    int distance = distances.get(targetNodeID);

                    if (distance == -1 || distance > i)
                    {

                        for (int tempTarget : distances.keySet())
                        {
                            int tempDist = distances.get(tempTarget);

                            if (tempDist == i - 1)
                            {
                                Node tempNode = nodeList.get(tempTarget);
                                // check if the node at distance i-1 has a direct connection to targetNode
                                if (tempNode.hasConnection(targetNodeID))
                                {
                                    //Node targetNode = nodeList.get(targetNodeID);

                                    // check that the route is free (not moving from city to sea)
                                    distances.put(targetNodeID, i);
                                }
                            }
                        }
                    }
                } // Shorter distance already found
                else
                {

                }
            }
        }
    }

    void print()
    {
        
    }

    void calculateDistances(ConcurrentHashMap<Integer, Integer> distancesWithKey)
    {
        for (int i = 2; i < 43; i++)
        {
            for (int targetNodeID : distances.keySet())
            {

                // if sea, no route is currently set
                if (targetNodeID < 500)
                {
                    int distance = distances.get(targetNodeID);

                    if (distance == -1 || distance > i)
                    {

                        for (int tempTarget : distances.keySet())
                        {
                            int tempDist = distances.get(tempTarget);

                            if (tempDist == i - 1)
                            {
                                Node tempNode = nodeList.get(tempTarget);
                                // check if the node at distance i-1 has a direct connection to targetNode
                                if (tempNode.hasConnection(targetNodeID))
                                {
                                    //Node targetNode = nodeList.get(targetNodeID);

                                    // check that the route is free (not moving from city to sea)
                                    distances.put(targetNodeID, i);
                                    distancesWithKey.putIfAbsent(new Key(this.ID, targetNodeID).hashCode(), i);
                                }
                            }
                        }
                    }
                } // Shorter distance already found
                else
                {

                }
            }
        }
    }

}
