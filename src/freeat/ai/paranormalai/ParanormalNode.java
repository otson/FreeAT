// Paranormal node class for ParanormalAI.
package freeat.ai.paranormalai;

import java.util.ArrayList;
import java.util.HashMap;

import freeat.Controller;
import freeat.Node;
import static freeat.ai.ParanormalAI.*;

public class ParanormalNode
{

	private Node node;
	private final HashMap<Integer, HashMap<Integer, Integer>> distanceToTargetHashMap;
	private final HashMap<Integer, HashMap<Integer, Integer>> priceToTargetHashMap;
	private ArrayList<Integer> neighbors;
	private boolean hasAirport;

	// constructors.
	public ParanormalNode(Node node, HashMap<Integer, HashMap<Integer, Integer>> distanceToTargetHashMap, HashMap<Integer, HashMap<Integer, Integer>> PriceToTargetHashMap, ArrayList<Integer> neighbors, Controller c)
	{
		this.node = node;
		this.distanceToTargetHashMap = distanceToTargetHashMap;
		this.priceToTargetHashMap = PriceToTargetHashMap;
		this.neighbors = neighbors;
		this.hasAirport = node.getPlaneConnections().size() > 0;

		this.fillHashMaps(c);
	}

	public ParanormalNode(Node node, ArrayList<Integer> neighbors, Controller c)
	{
		this.node = node;
		this.distanceToTargetHashMap = new HashMap<>();
		this.priceToTargetHashMap = new HashMap<>();
		this.neighbors = neighbors;
		this.hasAirport = node.getPlaneConnections().size() > 0;

		this.fillHashMaps(c);
	}

	public ParanormalNode(Node node, Controller c)
	{
		this.node = node;
		this.distanceToTargetHashMap = new HashMap<>();
		this.priceToTargetHashMap = new HashMap<>();
		this.neighbors = new ArrayList<>();
		this.hasAirport = node.getPlaneConnections().size() > 0;

		this.fillHashMaps(c);
	}

	// setters.
	public void setNode(Node node)
	{
		this.node = node;
		this.hasAirport = this.node.getPlaneConnections().size() > 0;
	}

	public void setDistanceToTarget(Node targetNode, int currentMaxTotalPrice, int distanceToTarget)
	{
		this.distanceToTargetHashMap.get(targetNode.ID).put(currentMaxTotalPrice, distanceToTarget);
	}

	public void setDistanceToTarget(int targetNodeID, int currentMaxTotalPrice, int distanceToTarget)
	{
		this.distanceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, distanceToTarget);
	}

	public void setPriceToTarget(Node targetNode, int currentMaxTotalPrice, int PriceToTarget)
	{
		this.priceToTargetHashMap.get(targetNode.ID).put(currentMaxTotalPrice, PriceToTarget);
	}

	public void setPriceToTarget(int targetNodeID, int currentMaxTotalPrice, int PriceToTarget)
	{
		this.priceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, PriceToTarget);
	}

	public void setNeighbors(ArrayList<Integer> neighbors)
	{
		this.neighbors = neighbors;
	}

	// fillers.
	private void fillHashMaps(Controller c)
	{
		for (int targetNodeID : c.getNodeList().keySet())
		{
			this.distanceToTargetHashMap.put(targetNodeID, new HashMap<>());
			this.priceToTargetHashMap.put(targetNodeID, new HashMap<>());

			for (int currentMaxTotalPrice = 0; currentMaxTotalPrice <= MAX_LAND_SEA_TOTAL_PRICE; currentMaxTotalPrice++)
			{
				this.distanceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, -1);
				this.priceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, -1);
			}
		}
	}

	// updaters.
	public boolean updateDistanceAndPrice(int targetNodeID, int currentMaxTotalPrice, int newDistanceToTarget, int newPriceToTarget)
	{
		boolean hasSomeBenefit = false;

		if ((this.distanceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice) < 0)
			&& (newPriceToTarget >= 0)
			&& (newPriceToTarget <= currentMaxTotalPrice))
		{
			this.distanceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, newDistanceToTarget);
			this.priceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, newPriceToTarget);
			hasSomeBenefit = true;
		}
		else if ((newDistanceToTarget < this.distanceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice)) && (newPriceToTarget >= 0) && (newPriceToTarget <= currentMaxTotalPrice))
		{
			this.distanceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, newDistanceToTarget);
			this.priceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, newPriceToTarget);
			hasSomeBenefit = true;
		}
		else if ((newDistanceToTarget == this.distanceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice))
				 && (newPriceToTarget >= 0)
				 && (newPriceToTarget < this.priceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice)))
		{
			this.distanceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, newDistanceToTarget);
			this.priceToTargetHashMap.get(targetNodeID).put(currentMaxTotalPrice, newPriceToTarget);
			hasSomeBenefit = true;
		}
		//else if (newPriceToTarget < this.priceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice))
		//{
		//	hasSomeBenefit = true;
		//}
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

	public int getDistanceToTarget(Node targetNode, int currentMaxTotalPrice)
	{
		return this.distanceToTargetHashMap.get(targetNode.ID).get(currentMaxTotalPrice);
	}

	public int getDistanceToTarget(int targetNodeID, int currentMaxTotalPrice)
	{
		return this.distanceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice);
	}

	public int getPriceToTarget(Node targetNode, int currentMaxTotalPrice)
	{
		return this.priceToTargetHashMap.get(targetNode.ID).get(currentMaxTotalPrice);
	}

	public int getPriceToTarget(int targetNodeID, int currentMaxTotalPrice)
	{
		return this.priceToTargetHashMap.get(targetNodeID).get(currentMaxTotalPrice);
	}

	public ArrayList<Integer> getNeighbors()
	{
		return this.neighbors;
	}
}
