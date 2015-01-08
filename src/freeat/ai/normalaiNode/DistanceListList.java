package freeat.ai.normalaiNode;

import freeat.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author otso
 */
public class DistanceListList
{

    private HashMap<Integer, DistanceList> distances = new HashMap<>();
    private HashMap<Integer, Node> nodeList;
    private HashMap<Integer, Integer> distancesWithKey = new HashMap<>();

    public DistanceListList(HashMap<Integer, Node> nodeList)
    {

        this.nodeList = nodeList;
        doFloydWarshallAlgorithm();

        // Give all the nodes in the list a list of all the other nodes, with a starting distance of -1 (not reachable)
        for (Node node : nodeList.values())
        {
            HashMap<Integer, Integer> distancesForNode = new HashMap<>();
            for (Node node2 : nodeList.values())
            {
                if (node.ID != node2.ID)
                {
                    distancesForNode.put(node2.ID, -1);

                }
            }
            distances.put(node.ID, new DistanceList(node.ID, distancesForNode, nodeList));
        }
        //

        //Collections.
        // set free routes with a distance of 1
        for (DistanceList nodesList : distances.values())
        {
            HashMap<Integer, Integer> nodeDistances = nodesList.getDistances();
            Node currentNode = nodeList.get(nodesList.ID);
            ArrayList<Integer> connections = currentNode.getConnections();
            for (Integer destination : connections)
            {
                Node destNode = nodeList.get(destination);
                // check that the route is free
                if (!(currentNode.isCity() && destNode.isSea()))
                {
                    //nodeDistances.put(destination, 1);

                    distancesWithKey.putIfAbsent(new Key(currentNode.ID, destNode.ID).hashCode(), 1);
                }

            }

        }

        // calculate rest of the distances in the Distancelist of method
        // ArrayList to hold all the thread objects
        ArrayList<Thread> threadList = new ArrayList<>();
        int threadCount = 0;

        // Make a new thread for each node
        for (DistanceList nodesList : distances.values())
        {
//            threadList.add(new Thread(
//                    new Runnable()
//                    {
//                        public void run()
//                        {
//            nodesList.calculateDistances(distancesWithKey);
//                        }
//                    })
//            );
//            // start the thread
//            threadList.get(threadCount).start();
//            threadCount++;
        }

        boolean finished = false;
        // While loop to let every thread finish
        while (!finished)
        {
            finished = true;
            for (Thread thread : threadList)
            {
                if (thread.isAlive())
                {
                    finished = false;
                }
            }
        }
        System.out.println("HashMap size: " + distancesWithKey.size());

        // All threads finished
    }

    public int getDistance(int from, int to)
    {

        //System.out.println("From: "+from+" to: "+to);
        if (from == to)
        {
            System.out.println("Distance from and to same value");
            System.out.println("From: " + from);
            System.out.println("To: " + to);

            int i = 0;
            // i /=i;
            return 0;
        }
        int distance = distances.get(from).getDistances().get(to);

        return distance;
    }

    public void checkRoutes()
    {
        for (DistanceList list : distances.values())
        {
            int from = list.ID;
            if (from > 100 && from <= 400 && from != 1 && from != 2 && from != 1 && from != 128 && from != 127 && from != 102 && from != 103 && from != 215 && from != 216 && from != 217)
            {

                for (Integer to : list.getDistances().keySet())
                {
                    int distance = list.getDistances().get(to);
                    if (to > 100 && to <= 400 && to != 1 && to != 2 && to != 1 && to != 128 && to != 127 && to != 102 && to != 103 && to != 215 && to != 216 && to != 217)
                    {
                        if (distance == -1)
                        {
                            System.out.println("Distance of " + distance + " from " + from + " to " + to);
                        }
                    }
                }
            }
        }
    }

    public void printDistance(int from, int to)
    {
        int distance = distances.get(from).getDistances().get(to);
        System.out.println("From: " + nodeList.get(from).getName() + " to: " + nodeList.get(to).getName() + " distance: " + distance);
    }

    public void printAll()
    {
        for (DistanceList distanceList : distances.values())
        {
            distanceList.print();
        }
    }

    private void doFloydWarshallAlgorithm()
    {
        long start = System.nanoTime();
        // init distances (9999 initial value)
        for (Integer integer : nodeList.keySet())
        {
            for (Integer integer2 : nodeList.keySet())
            {
                distancesWithKey.putIfAbsent(new Key(integer, integer2).hashCode(), 9999);
            }
        }

        // distance to self is 0
        for (Integer integer : nodeList.keySet())
        {
            distancesWithKey.put(new Key(integer, integer).hashCode(), 0);
        }

        for (Node node : nodeList.values())
        {
            for (Integer connection : node.getConnections())
            {
                distancesWithKey.put(new Key(node.ID, connection).hashCode(), 1);
            }
        }



        for (int k : nodeList.keySet()){
            
                for (int i : nodeList.keySet())
                {
                    for (int j : nodeList.keySet())
                    {
                        if (dist(i, j) > dist(i, k) + dist(k, j))
                        {
                            setDist(i, j, dist(i, k) + dist(k, j));
                        }
                    }
                
                }
        }

        System.out.println("Time: " + (System.nanoTime() - start) / 1000000 + " ms.");
        System.out.println("Distance from 2 to 120: " + dist(2, 120));
        System.out.println("Distance from 1 to 120: " + dist(2, 102));
        System.exit(0);
    }

    private int dist(int from, int to)
    {
        return distancesWithKey.get(new Key(from, to).hashCode());
    }

    private void setDist(int from, int to, int distance)
    {
        distancesWithKey.put(new Key(from, to).hashCode(), distance);
    }
}
