/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai.normalaiNode;

import freeat.Node;

/**
 *
 * @author otso
 */
public class DistanceNode
{

    private Node node;
    private int distance;
    private int price;

    public DistanceNode(Node node)
    {
        this.node = node;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public Node getNode()
    {
        return node;
    }

}
