// Paranormal node class for ParanormalAI.

package freeat.ai.paranormalai;

import java.util.ArrayList;
import freeat.Node;

public class ParanormalNode
{
    public Node node;
    public int distanceToTarget;
    public int priceFromTarget;
    public ArrayList<Integer> neighbors;
    public boolean hasAirport;

    // constructors.

    public ParanormalNode(Node node, int distanceToTarget, int priceFromTarget, ArrayList<Integer> neighbors)
    {
        this.node = node;
        this.distanceToTarget = distanceToTarget;
        this.priceFromTarget = priceFromTarget;
        this.neighbors = neighbors;

        if (node.getPlaneConnections().size() > 0)
        {
            this.hasAirport = true;
        }
        else
        {
            this.hasAirport = false;
        }
    }

    public ParanormalNode(Node node)
    {
        this.node = node;
        this.distanceToTarget = -1;
        this.priceFromTarget = -1;
        this.neighbors = new ArrayList<>();
        this.hasAirport = false;
    }

    // setters.

    public void setNode(Node node)
    {
        this.node = node;
        if (this.node.getPlaneConnections().size() > 0)
        {
            this.hasAirport = true;
        }
        else
        {
            this.hasAirport = false;
        }
    }

    public void setDistanceToTarget(int distanceToTarget)
    {
        this.distanceToTarget = distanceToTarget;
    }

    public void setPriceFromTarget(int priceFromTarget)
    {
        this.priceFromTarget = priceFromTarget;
    }

    public void setNeighbors(ArrayList<Integer> neighbors)
    {
        this.neighbors = neighbors;
    }

    public int getDistanceToTarget()
    {
        return this.distanceToTarget;
    }

    public int getPriceFromTarget()
    {
        return this.priceFromTarget;
    }

    public ArrayList<Integer> setNeighbors()
    {
        return this.neighbors;
    }
}
