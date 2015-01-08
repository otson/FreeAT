// Paranormal node class for ParanormalAI.
package freeat.ai.dumbai;

import java.util.ArrayList;

import freeat.Node;
import freeat.Controller;
import static freeat.ai.DumbAI.*;

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

    public void setDistanceToTarget(int targetNodeID, int distanceToTarget)
    {
        distanceToTargetHashMap.get(targetNodeID).put(this.getNode().ID, distanceToTarget);
    }

    public void setDistanceToTarget(Node targetNode, int distanceToTarget)
    {
        setDistanceToTarget(targetNode.ID, distanceToTarget);
    }

    public void setMaxTimeToTarget(int targetNodeID, int currentMaxTotalPrice, float maxTimeToTarget)
    {
        maxTimeToTargetHashMap.get(currentMaxTotalPrice).get(targetNodeID).put(this.getNode().ID, maxTimeToTarget);
    }

    public void setMaxTimeToTarget(Node targetNode, int currentMaxTotalPrice, float maxTimeToTarget)
    {
        setMaxTimeToTarget(targetNode.ID, currentMaxTotalPrice, maxTimeToTarget);
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
    public boolean updateDistance(int targetNodeID, int newDistanceToTarget)
    {
        boolean hasSomeBenefit = false;

        if (newDistanceToTarget < 0)
        {
            // TODO: report error! Updated distance must a real distance, and update price must be a real price!
            return false;
        }
        else if (paranormalNodeHashMap.get(this.node.ID).node.ID == targetNodeID)
        {
            // OK, were updating the distance to the same node.
            // If newDistanceToTarget != 0 , there might be a bug somewhere.
            if (newDistanceToTarget > 0)
            {
                // This is probably not the most elegant solution here...
                distanceToTargetHashMap.get(targetNodeID).put(this.getNode().ID, 0);
                hasSomeBenefit = true;
            }
        }
        else if (distanceToTargetHashMap.get(targetNodeID).get(this.getNode().ID) < 0)
        {
            distanceToTargetHashMap.get(targetNodeID).put(this.getNode().ID, newDistanceToTarget);
            hasSomeBenefit = true;
        }
        else if (newDistanceToTarget < distanceToTargetHashMap.get(targetNodeID).get(this.getNode().ID))
        {
            distanceToTargetHashMap.get(targetNodeID).put(this.getNode().ID, newDistanceToTarget);
            hasSomeBenefit = true;
        }
        return hasSomeBenefit;
    }

    public boolean updateDistance(Node targetNode, int newDistanceToTarget)
    {
        return updateDistance(targetNode.ID, newDistanceToTarget);
    }

    // getters.
    public Node getNode()
    {
        return this.node;
    }

    public int getDistanceToTarget(int targetNodeID)
    {
        return distanceToTargetHashMap.get(targetNodeID).get(this.getNode().ID);
    }

    public int getDistanceToTarget(Node targetNode)
    {
        return getDistanceToTarget(targetNode.ID);
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

    public float getMaxTimeToTarget(int targetNodeID, int currentMaxTotalPrice)
    {
        return (float) distanceToTargetHashMap.get(targetNodeID).get(this.getNode().ID);
    }

    public float getMaxTimeToTarget(Node targetNode, int currentMaxTotalPrice)
    {
        return getMaxTimeToTarget(targetNode.ID, currentMaxTotalPrice);
    }
}
