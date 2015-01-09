// Paranormal node class for ParanormalAI.
package freeat.ai.paranormalai;

import java.util.ArrayList;

import freeat.Node;
import freeat.Globals;
import freeat.Controller;
import static freeat.ai.ParanormalAI.*;

public class ParanormalNode
{

    private Node node;
    private ArrayList<Integer> neighbors;
    private boolean hasAirport;

    // constructors.
    public ParanormalNode(Node node, ArrayList<Integer> neighbors, Controller c)
    {
        this.node = node;
        this.neighbors = neighbors;
        this.hasAirport = node.getPlaneConnections().size() > 0;
    }

    public ParanormalNode(Node node, Controller c)
    {
        this.node = node;
        this.neighbors = new ArrayList<>();
        this.hasAirport = node.getPlaneConnections().size() > 0;
    }

    // setters.
    public void setNode(Node node)
    {
        this.node = node;
        this.hasAirport = this.node.getPlaneConnections().size() > 0;
    }

    public void setDistanceToTarget(int targetNodeID, int currentMaxTotalPrice, int distanceToTarget)
    {
        distanceToTargetWithoutFreeSearoutesHashMap.put(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode(), distanceToTarget);
    }

    public void setDistanceToTarget(Node targetNode, int currentMaxTotalPrice, int distanceToTarget)
    {
        setDistanceToTarget(targetNode.ID, currentMaxTotalPrice, distanceToTarget);
    }

    public void setPriceToTarget(
        int targetNodeID,
        int currentMaxTotalPrice,
        int priceToTarget)
    {
        priceToTargetWithoutFreeSearoutesHashMap.put(new Key3(
            this.getNode().ID,
            targetNodeID,
            currentMaxTotalPrice).hashCode(), priceToTarget);
    }

    public void setPriceToTarget(Node targetNode, int currentMaxTotalPrice, int PriceToTarget)
    {
        setPriceToTarget(targetNode.ID, currentMaxTotalPrice, PriceToTarget);
    }

    public void setNeighbors(ArrayList<Integer> neighbors)
    {
        this.neighbors = neighbors;
    }

    // updaters.
    public boolean updateDistanceAndPrice(int targetNodeID, int currentMaxTotalPrice, int newDistanceToTarget, int newPriceToTarget)
    {
        boolean hasSomeBenefit = false;

        if ((newDistanceToTarget < 0) || (newPriceToTarget < 0))
        {
            // TODO: report error! Updated distance must a real distance, and update price must be a real price!
            return false;
        }
        else if (paranormalNodeHashMap.get(this.node.ID).node.ID == targetNodeID)
        {
            // OK, were updating the distance to the same node.
            // If newDistanceToTarget != 0 , there might be a bug somewhere.
            // If newPriceToTarget != 0, there might be a somewhere.
            if (newDistanceToTarget > 0)
            {
                // TODO: report error!
            }
            if (newPriceToTarget > 0)
            {
                // TODO: report error!
            }

            if ((distanceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode()) != 0)
                || (priceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode()) != 0))
            {
                // This is probably not the most elegant solution here...
                distanceToTargetWithoutFreeSearoutesHashMap.put(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode(), 0);
                priceToTargetWithoutFreeSearoutesHashMap.put(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode(), 0);
                hasSomeBenefit = true;
            }
        }
        else if ((distanceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode()) < 0)
                 && (newPriceToTarget <= currentMaxTotalPrice))
        {
            distanceToTargetWithoutFreeSearoutesHashMap.put(new Key3(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice).hashCode(),
                newDistanceToTarget);
            priceToTargetWithoutFreeSearoutesHashMap.put(new Key3(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice).hashCode(),
                newPriceToTarget);
            hasSomeBenefit = true;
        }
        else if ((newDistanceToTarget < distanceToTargetWithoutFreeSearoutesHashMap.get(new Key3(
            this.getNode().ID,
            targetNodeID,
            currentMaxTotalPrice).hashCode()))
                 && (newPriceToTarget <= currentMaxTotalPrice))
        {
            distanceToTargetWithoutFreeSearoutesHashMap.put(new Key3(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice).hashCode(),
                newDistanceToTarget);
            priceToTargetWithoutFreeSearoutesHashMap.put(new Key3(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice).hashCode(),
                newPriceToTarget);
            hasSomeBenefit = true;
        }
        else if ((newDistanceToTarget == distanceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode()))
                 && (newPriceToTarget >= 0)
                 && (newPriceToTarget < priceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, currentMaxTotalPrice).hashCode())))
        {
            distanceToTargetWithoutFreeSearoutesHashMap.put(new Key3(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice).hashCode(),
                newDistanceToTarget);
            priceToTargetWithoutFreeSearoutesHashMap.put(
                new Key3(this.getNode().ID,
                    targetNodeID,
                    currentMaxTotalPrice).hashCode(),
                newPriceToTarget);
            hasSomeBenefit = true;
        }
        return hasSomeBenefit;
    }

    public boolean updateDistanceAndPrice(Node targetNode, int currentMaxTotalPrice, int newDistanceToTarget, int newPriceToTarget)
    {
        return updateDistanceAndPrice(targetNode.ID, currentMaxTotalPrice, newDistanceToTarget, newPriceToTarget);
    }

    public boolean updateDistanceAndPriceForCurrentAndHigherPrices(int targetNodeID,
        int currentMaxTotalPrice,
        int newDistanceToTarget,
        int newPriceToTarget)
    {
        boolean isChanged = updateDistanceAndPrice(targetNodeID, currentMaxTotalPrice, newDistanceToTarget, newPriceToTarget);

        for (int i = currentMaxTotalPrice + 1; currentMaxTotalPrice <= Globals.MAX_SEA_MOVEMENT_COST; i++)
        {
            updateDistanceAndPrice(targetNodeID, i, newDistanceToTarget, newPriceToTarget);
        }
        return isChanged;
    }

    public boolean updateDistanceAndPriceForCurrentAndHigherPrices(Node targetNode,
        int currentMaxTotalPrice,
        int newDistanceToTarget,
        int newPriceToTarget)
    {
        boolean isChanged = updateDistanceAndPrice(targetNode.ID, currentMaxTotalPrice, newDistanceToTarget, newPriceToTarget);

        for (int i = currentMaxTotalPrice + 1; currentMaxTotalPrice <= Globals.MAX_SEA_MOVEMENT_COST; i++)
        {
            updateDistanceAndPrice(targetNode.ID, i, newDistanceToTarget, newPriceToTarget);
        }
        return isChanged;
    }

    // getters.
    public Node getNode()
    {
        return this.node;
    }

    public int getDistanceToTarget(int targetNodeID, int currentMaxTotalPrice)
    {
        if (currentMaxTotalPrice < 0)
        {
            return -1;
        }
        else
        {
            return distanceToTargetWithoutFreeSearoutesHashMap.get(
                new Key3(
                    this.getNode().ID,
                    targetNodeID,
                    Math.min(currentMaxTotalPrice, Globals.MAX_SEA_MOVEMENT_COST)).hashCode());
        }
    }

    public int getDistanceToTarget(Node targetNode, int currentMaxTotalPrice)
    {
        return getDistanceToTarget(targetNode.ID, currentMaxTotalPrice);
    }

    public int getPriceToTarget(int targetNodeID, int currentMaxTotalPrice)
    {
        if (currentMaxTotalPrice < 0)
        {
            return -1;
        }
        else
        {
            return priceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, Math.min(currentMaxTotalPrice, Globals.MAX_SEA_MOVEMENT_COST)).hashCode());
        }
    }

    public int getPriceToTarget(Node targetNode, int currentMaxTotalPrice)
    {
        return getPriceToTarget(targetNode.ID, currentMaxTotalPrice);
    }

    public ArrayList<Integer> getNeighbors()
    {
        return this.neighbors;
    }

    public boolean getHasAiport()
    {
        return this.hasAirport;
    }

    public float getMaxTimeToTarget(int targetNodeID, int currentMaxTotalPrice)
    {
        return (float) distanceToTargetWithoutFreeSearoutesHashMap.get(new Key3(this.getNode().ID, targetNodeID, Globals.MAX_SEA_MOVEMENT_COST).hashCode());
    }

    public float getMaxTimeToTarget(Node targetNode, int currentMaxTotalPrice)
    {
        return getMaxTimeToTarget(targetNode.ID, currentMaxTotalPrice);
    }
}
