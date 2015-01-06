// Paranormal node class for ParanormalAI.
package freeat.ai.paranormalai;

import java.util.ArrayList;

import freeat.Controller;
import freeat.Node;
import static freeat.ai.ParanormalAI.*;

public class ParanormalNode
{

    private Node node;
    private ArrayList<Integer> neighbors;
    private boolean hasAirport;
    private boolean isIslandTreasureCity;      // non-city nodes are never island cities.
    private boolean isContinentalTreasureCity; // non-city nodes are never continental cities.
    private boolean isIslandMetropol;          // a metropol located a not located in the landmass with most treasure cities.
    private boolean isContinentalMetropol;     // a metropol located in the landmass with most treasure cities.
    private boolean isMetropolLandmass;        // is the node located in a landmass with 1 or more metropols?
    private int nTreasureCitiesOnLandmass;     // total number of treasure cities on the same landmass (including the node itself).
    private int landmassID;                    // landmass ID number (used for internal purposes).

    // constructors.
    public ParanormalNode(Node node, ArrayList<Integer> neighbors, Controller c)
    {
        this.node = node;
        this.neighbors = neighbors;
        this.hasAirport = node.getPlaneConnections().size() > 0;
        this.isIslandTreasureCity = false;
        this.isContinentalTreasureCity = false;
        this.isIslandMetropol = false;
        this.isContinentalMetropol = false;
        this.isMetropolLandmass = false;
        this.nTreasureCitiesOnLandmass = 0;
        this.landmassID = -1;
    }

    public ParanormalNode(Node node, Controller c)
    {
        this.node = node;
        this.neighbors = new ArrayList<>();
        this.hasAirport = node.getPlaneConnections().size() > 0;
        this.isIslandTreasureCity = false;
        this.isContinentalTreasureCity = false;
        this.isIslandMetropol = false;
        this.isContinentalMetropol = false;
        this.isMetropolLandmass = false;
        this.nTreasureCitiesOnLandmass = 0;
        this.landmassID = -1;
    }

    // setters.
    public void setNode(Node node)
    {
        this.node = node;
        this.hasAirport = this.node.getPlaneConnections().size() > 0;
    }

    public void setDistanceToTarget(int targetNodeID, int currentMaxTotalPrice, int distanceToTarget)
    {
        distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, distanceToTarget);
    }

    public void setDistanceToTarget(Node targetNode, int currentMaxTotalPrice, int distanceToTarget)
    {
        setDistanceToTarget(targetNode.ID, currentMaxTotalPrice, distanceToTarget);
    }

    public void setPriceToTarget(int targetNodeID, int currentMaxTotalPrice, int priceToTarget)
    {
        priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, priceToTarget);

        if (priceToTarget == 0)
        {
            int lowerLandmassID = -1;

            if ((this.landmassID > 0) && (paranormalNodeHashMap.get(targetNodeID).getLandmassID() > 0))
            {
                lowerLandmassID = Math.min(this.landmassID, paranormalNodeHashMap.get(targetNodeID).getLandmassID());
            }
            else if (this.landmassID > 0)
            {
                lowerLandmassID = this.landmassID;
            }
            else if (paranormalNodeHashMap.get(targetNodeID).getLandmassID() > 0)
            {
                lowerLandmassID = paranormalNodeHashMap.get(targetNodeID).getLandmassID();
            }

            if (lowerLandmassID > 0)
            {
                this.landmassID = lowerLandmassID;
                // paranormalNodeHashMap.get(targetNodeID).setLandmassID(lowerLandmassID);
            }
        }
    }

    public void setPriceToTarget(Node targetNode, int currentMaxTotalPrice, int PriceToTarget)
    {
        setPriceToTarget(targetNode.ID, currentMaxTotalPrice, PriceToTarget);
    }

    public void setNeighbors(ArrayList<Integer> neighbors)
    {
        this.neighbors = neighbors;
    }

    public void isIslandTreasureCity(boolean isIslandTreasureCity)
    {
        this.isIslandTreasureCity = isIslandTreasureCity;
    }

    public void setIsContinentalTreasureCity(boolean setIsContinentalTreasureCity)
    {
        this.isContinentalTreasureCity = setIsContinentalTreasureCity;
    }

    public void setIsIslandMetropol(boolean isIslandMetropol)
    {
        this.isIslandMetropol = isIslandMetropol;
    }

    public void setIsContinentalMetropol(boolean isContinentalMetropol)
    {
        this.isContinentalMetropol = isContinentalMetropol;
    }

    public void setLandmassID(int landmassID)
    {
        this.landmassID = landmassID;
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

            if ((distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).get(this.getNode().ID) != 0)
                || (priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).get(this.getNode().ID) != 0))
            {
                // This is probably not the most elegant solution here...
                distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, 0);
                priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, 0);
                hasSomeBenefit = true;
            }
        }
        else if ((distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).get(this.getNode().ID) < 0)
                 && (newPriceToTarget <= currentMaxTotalPrice))
        {
            distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, newDistanceToTarget);
            priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, newPriceToTarget);
            hasSomeBenefit = true;
        }
        else if ((newDistanceToTarget < distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).get(this.getNode().ID)) && (newPriceToTarget <= currentMaxTotalPrice))
        {
            distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, newDistanceToTarget);
            priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, newPriceToTarget);
            hasSomeBenefit = true;
        }
        else if ((newDistanceToTarget == distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).get(this.getNode().ID))
                 && (newPriceToTarget >= 0)
                 && (newPriceToTarget < priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).get(this.getNode().ID)))
        {
            distanceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, newDistanceToTarget);
            priceToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, newPriceToTarget);
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

        for (int i = currentMaxTotalPrice + 1; currentMaxTotalPrice <= MAX_LAND_SEA_TOTAL_PRICE; i++)
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

        for (int i = currentMaxTotalPrice + 1; currentMaxTotalPrice <= MAX_LAND_SEA_TOTAL_PRICE; i++)
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
            return distanceToTargetHashMap.get(Math.min(currentMaxTotalPrice, MAX_LAND_SEA_TOTAL_PRICE)).get(targetNodeID).get(this.getNode().ID);
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
            return priceToTargetHashMap.get(Math.min(currentMaxTotalPrice, MAX_LAND_SEA_TOTAL_PRICE)).get(targetNodeID).get(this.getNode().ID);
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

    public boolean getIsIslandTreasureCity()
    {
        return this.isIslandTreasureCity;
    }

    public boolean getIsContinentalTreasureCity()
    {
        return this.isContinentalTreasureCity;
    }

    public boolean getIsIslandMetropol()
    {
        return this.isIslandMetropol;
    }

    public boolean getIsContinentalMetropol()
    {
        return this.isContinentalMetropol;
    }

    public int getLandmassID()
    {
        return this.landmassID;
    }
}
