// flight path class for ParanormalAI.

package freeat.ai.paranormalai;

import java.util.ArrayList;
import java.util.Collections;

import freeat.Node;
import freeat.ai.ParanormalAI;

public class FlightPath
{
    public Node originNode;
    public Node targetNode;
    public ArrayList<Routing> routingArrayList;
    public ArrayList<Integer> routingLengths;
    public int minRoutingLength;
    public int minFlightPathPrice;
	public FlightPath(Node originNode, Node targetNode, ArrayList<ArrayList<Routing>> routingArrayList)
	{
	    this.originNode = originNode;
	    this.targetNode = targetNode;
        this.routingArrayList = new ArrayList<>();
        this.routingLengths = new ArrayList<>();
        this.minRoutingLength = -1;
        this.minFlightPathPrice = -1;
	}

    public void addRouting(Routing routing)
    {
        this.routingArrayList.add(routing);
        this.routingLengths.add(routing.nRoadsOrFlights);
        this.minRoutingLength = Collections.min(this.routingLengths);
        this.minFlightPathPrice = this.minRoutingLength * ParanormalAI.TRUEFLIGHTPRICE;
    }
}
