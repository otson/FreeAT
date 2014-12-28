// Routing class for ParanormalAI.

package freeat.ai.paranormalai;

import java.util.ArrayList;
import freeat.Node;

public class Routing
{
    public ArrayList<ParanormalNode> routingArrayList;
    public int nRoadsOrFlights;

    public Routing(ArrayList<ParanormalNode> routingArrayList)
    {
        this.routingArrayList = routingArrayList;
        this.nRoadsOrFlights = routingArrayList.size();
    }
}
