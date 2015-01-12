// Paranormal node class for ParanormalAI.
package freeat.ai.paranormalai;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import freeat.Node;
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

    public void setTimeToTarget(
        int targetNodeID,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int timeToTarget,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        timeHashMap.
            put(new Key4(this.getNode().ID, targetNodeID, currentMaxTotalPrice, isUsingFreeSearoute).hashCode(), timeToTarget);
    }

    public void setTimeToTarget(
        Node targetNode,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int timeToTarget,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        setTimeToTarget(targetNode.ID, currentMaxTotalPrice, isUsingFreeSearoute, timeToTarget, timeHashMap);
    }

    public void setPriceToTarget(
        int targetNodeID,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int priceToTarget,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        priceHashMap.put(new Key4(
            this.getNode().ID,
            targetNodeID,
            currentMaxTotalPrice,
            isUsingFreeSearoute).hashCode(), priceToTarget);
    }

    public void setPriceToTarget(
        Node targetNode,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int PriceToTarget,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        setPriceToTarget(targetNode.ID, currentMaxTotalPrice, isUsingFreeSearoute, PriceToTarget, priceHashMap);
    }

    public void setNeighbors(ArrayList<Integer> neighbors)
    {
        this.neighbors = neighbors;
    }

    // updaters.
    public boolean updateTimeAndPrice(
        int targetNodeID,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int newTimeToTarget,
        int newPriceToTarget,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        boolean hasSomeBenefit = false;

        if ((newTimeToTarget < 0)
            || (newPriceToTarget < 0)
            || (newPriceToTarget > currentMaxTotalPrice))
        {
            // Invalid values (time and price must be non-negative and
            // price must be less or equal to currentMaxTotalPrice).
            return false;
        }
        else if (paranormalNodeHashMap.get(this.node.ID).node.ID == targetNodeID)
        {
            // OK, were updating the distance to the same node.
            // If newDistanceToTarget != 0 , there might be a bug somewhere.
            // If newPriceToTarget != 0, there might be a somewhere.
            if (newTimeToTarget > 0)
            {
                // TODO: report error!
            }
            if (newPriceToTarget > 0)
            {
                // TODO: report error!
            }

            if ((timeHashMap.get(new Key4(this.getNode().ID, targetNodeID, currentMaxTotalPrice, isUsingFreeSearoute).hashCode()) != 0)
                || (priceHashMap.
                get(new Key4(this.getNode().ID, targetNodeID, currentMaxTotalPrice, isUsingFreeSearoute).hashCode()) != 0))
            {
                // This is probably not the most elegant solution here...
                timeHashMap.
                    put(new Key4(
                            this.getNode().ID,
                            targetNodeID,
                            currentMaxTotalPrice,
                            isUsingFreeSearoute).hashCode(), 0);
                priceHashMap.
                    put(new Key4(
                            this.getNode().ID,
                            targetNodeID,
                            currentMaxTotalPrice,
                            isUsingFreeSearoute).hashCode(), 0);
                hasSomeBenefit = true;
            }
        }
        else if (newTimeToTarget <= 0)
        {
            // 0 is valid time only if origin and target are the same node.
            // Negatime value is never accepted for time
            // (it would mean traveling backwards in time).
            return false;
        }
        else if (timeHashMap.get(
            new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode()) < 0)
        {
            // OK, there was no time from origin to target.
            // Any value greater than 0 is accepted.
            timeHashMap.put(new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode(),
                newTimeToTarget);
            priceHashMap.put(new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode(),
                newPriceToTarget);
            hasSomeBenefit = true;
        }
        else if (newTimeToTarget < timeHashMap.get(
            new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode()))
        {
            // OK, new time is shorter than old time.
            timeHashMap.put(new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode(),
                newTimeToTarget);
            priceHashMap.put(new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode(),
                newPriceToTarget);
            hasSomeBenefit = true;
        }
        else if ((newTimeToTarget == timeHashMap.get(
            new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode()))
                 && (newPriceToTarget == priceHashMap.get(
                new Key4(
                    this.getNode().ID,
                    targetNodeID,
                    currentMaxTotalPrice,
                    isUsingFreeSearoute).hashCode())))
        {
            // OK, new time is the same as old one but
            // new price is cheaper.
            timeHashMap.put(new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode(),
                newTimeToTarget);
            priceHashMap.put(new Key4(
                this.getNode().ID,
                targetNodeID,
                currentMaxTotalPrice,
                isUsingFreeSearoute).hashCode(),
                newPriceToTarget);
            hasSomeBenefit = true;
        }
        return hasSomeBenefit;
    }

    public boolean updateTimeAndPrice(
        Node targetNode,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int newTimeToTarget,
        int newPriceToTarget,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)

    {
        return ParanormalNode.this.updateTimeAndPrice(
            targetNode.ID,
            currentMaxTotalPrice,
            isUsingFreeSearoute,
            newTimeToTarget,
            newPriceToTarget,
            timeHashMap,
            priceHashMap);
    }

    public boolean updateTimeAndPriceForCurrentAndHigherPrices(
        int targetNodeID,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int newTimeToTarget,
        int newPriceToTarget,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        boolean isChanged = ParanormalNode.this.updateTimeAndPrice(
            targetNodeID,
            currentMaxTotalPrice,
            isUsingFreeSearoute,
            newTimeToTarget,
            newPriceToTarget,
            timeHashMap,
            priceHashMap);

        for (int i = currentMaxTotalPrice + 1; currentMaxTotalPrice <= MAX_ROUTING_PRICE; i++)
        {
            ParanormalNode.this.updateTimeAndPrice(
                targetNodeID,
                i,
                isUsingFreeSearoute,
                newTimeToTarget,
                newPriceToTarget,
                timeHashMap,
                priceHashMap);
        }
        return isChanged;
    }

    public boolean updateTimeAndPriceForCurrentAndHigherPrices(
        Node targetNode,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        int newTimeToTarget,
        int newPriceToTarget,
        ConcurrentHashMap<Integer, Integer> timeHashMap,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        boolean isChanged = ParanormalNode.this.updateTimeAndPrice(
            targetNode.ID,
            currentMaxTotalPrice,
            isUsingFreeSearoute,
            newTimeToTarget,
            newPriceToTarget,
            timeHashMap,
            priceHashMap);

        for (int i = currentMaxTotalPrice + 1; currentMaxTotalPrice <= MAX_ROUTING_PRICE; i++)
        {
            ParanormalNode.this.updateTimeAndPrice(
                targetNode.ID,
                i,
                isUsingFreeSearoute,
                newTimeToTarget,
                newPriceToTarget,
                timeHashMap,
                priceHashMap);
        }
        return isChanged;
    }

    // getters.
    public Node getNode()
    {
        return this.node;
    }

    public int getTimeToTarget(
        int targetNodeID,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        if (currentMaxTotalPrice < 0)
        {
            return -1;
        }
        else
        {
            return timeHashMap.get(
                new Key4(
                    this.getNode().ID,
                    targetNodeID,
                    Math.min(currentMaxTotalPrice, MAX_ROUTING_PRICE),
                    isUsingFreeSearoute).hashCode());
        }
    }

    public int getTimeToTarget(
        Node targetNode,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> timeHashMap)
    {
        return getTimeToTarget(targetNode.ID, currentMaxTotalPrice, isUsingFreeSearoute, timeHashMap);
    }

    public int getPriceToTarget(
        int targetNodeID,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        if (currentMaxTotalPrice < 0)
        {
            return -1;
        }
        else
        {
            return priceHashMap.
                get(new Key4(
                        this.getNode().ID,
                        targetNodeID,
                        Math.min(currentMaxTotalPrice, MAX_ROUTING_PRICE),
                        isUsingFreeSearoute).hashCode());
        }
    }

    public int getPriceToTarget(
        Node targetNode,
        int currentMaxTotalPrice,
        boolean isUsingFreeSearoute,
        ConcurrentHashMap<Integer, Integer> priceHashMap)
    {
        return getPriceToTarget(targetNode.ID, currentMaxTotalPrice, isUsingFreeSearoute, priceHashMap);
    }

    public ArrayList<Integer> getNeighbors()
    {
        return this.neighbors;
    }

    public boolean getHasAiport()
    {
        return this.hasAirport;
    }
}
