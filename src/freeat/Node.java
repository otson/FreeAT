/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 *
 * @author otso
 */
public class Node
{

    public static int CITY_COUNT = 0;
    private static int width = 30;
    public int TYPE;
    public final int x;
    public final int y;
    public final int ID;
    private int treasure;
    private boolean hasTreasure;
    private ArrayList<Integer> connections;
    private ArrayList<Integer> planeConnections;
    private HashMap<Integer, Node> locations;

    private String name;

    // new variables to make the game more scalable, not yet in use
    private ArrayList<Route> planeRoutes = new ArrayList<>();
    private ArrayList<Route> freeSeaRoutes = new ArrayList<>();
    private HashMap<Integer, ArrayList<Route>> nonPlaneRoutes = new HashMap<>();

    // new methods, not yet in use
    // set plane routes
    public void setPlaneRoutes()
    {
        for (Integer id : planeConnections)
        {
            planeRoutes.add(new Route(locations.get(id), Globals.PLANE_ROUTE_PRICE));
        }
    }

    // set free sea routes
    public void setFreeSeaRoutes()
    {
        for (Integer id : connections)
        {
            Node targetNode = locations.get(id);
            if ((isCity() && targetNode.isSea()) || (isSea() && targetNode.isCity()) || ((isSea() && targetNode.isSea())))
            {
                freeSeaRoutes.add(new Route(targetNode, 0));
            }
        }
    }

    public Node(int id, int x, int y, HashMap<Integer, Node> locations)
    {

        this.name = String.valueOf(id);

        this.ID = id;
        this.x = x;
        this.y = Main.WINDOW_HEIGHT - y;
        this.locations = locations;
        connections = new ArrayList<>();
        planeConnections = new ArrayList<>();
        if (ID == 1)
        {
            TYPE = NodeType.CAIRO;
        }
        if (ID == 2)
        {
            TYPE = NodeType.TANGIER;
        }
        if (ID > 100 && ID < 200)
        {
            TYPE = NodeType.CITY;
            CITY_COUNT++;
        }
        if (ID > 200 && ID < 400)
        {
            TYPE = NodeType.ROUTE;
        }
        if (ID == 400)
        {
            TYPE = NodeType.SAHARA;
        }
        if (ID > 500 && ID < 600)
        {
            TYPE = NodeType.SEA_ROUTE;
        }

        if (ID == 601 || ID == 602)
        {
            TYPE = NodeType.PIRATES;
        }

        if (ID == 123)
        {
            TYPE = NodeType.SLAVE_COAST;
        }
        if (ID == 125)
        {
            TYPE = NodeType.GOLD_COAST;
        }
        if (ID == 120)
        {
            TYPE = NodeType.CAPE_TOWN;
        }
    }

    public void addConnection(Integer connection)
    {
        if (locations.containsKey(connection))
        {
            connections.add(connection);
        }
    }

    public void addPlaneConnection(Integer connection)
    {
        if (locations.containsKey(connection))
        {
            planeConnections.add(connection);
        }
    }

    public int getTreasure()
    {
        return treasure;
    }

    public void setTreasure(int type)
    {
        treasure = type;
        hasTreasure = true;
    }

    public void removeTreasure()
    {
        treasure = TreasureType.OPENED;
        hasTreasure = false;
    }

    public void print()
    {
        System.out.println("Id: " + ID + " x: " + x + " y: " + y + " connections: " + connectionsToString());
    }

    public boolean hasTreasure()
    {
        return hasTreasure;
    }

    public int getTYPE()
    {
        return TYPE;
    }

    public boolean hasConnection(Integer connection)
    {
        return connections.contains(connection);
    }

    public boolean hasPlaneConnection(Integer connection)
    {
        return planeConnections.contains(connection);
    }

    private String connectionsToString()
    {
        String string = "";
        for (Integer id : connections)
        {
            string = string.concat(locations.get(id).ID + " ");
        }
        return string;
    }

    public void draw()
    {
        float r = 0;
        float g = 0;
        float b = 0;
        glDisable(GL_TEXTURE_2D);
        if (ID > 100 && ID < 200)
        {
            if (treasure == TreasureType.EMPTY)
            {
                r = 0.8f;
                g = 0.8f;
                b = 0.9f;
            } else if (treasure == TreasureType.EMERALD)
            {
                r = 0;
                g = 1;
                b = 0;
            } else if (treasure == TreasureType.RUBY)
            {
                r = 1;
                g = 0;
                b = 0;
            } else if (treasure == TreasureType.TOPAZ)
            {
                r = 0;
                g = 0;
                b = 1;
            } else if (treasure == TreasureType.STAR_OF_AFRICA)
            {
                r = 1;
                g = 1;
                b = 0;
            } else if (treasure == TreasureType.HORSESHOE)
            {
                r = 0.4f;
                g = 0.4f;
                b = 0.4f;
            } else if (treasure == TreasureType.ROBBER)
            {
                r = 0.0f;
                g = 0.0f;
                b = 0.0f;
            } else if (treasure == TreasureType.OPENED)
            {
                glEnable(GL_TEXTURE_2D);
                return;
            }

            glBegin(GL_QUADS);
            GL11.glColor3f(r, g, b);
            glVertex2f(-width / 2f + x, -width / 2f + y);
            glVertex2f(width / 2f + x, -width / 2f + y);
            glVertex2f(width / 2f + x, width / 2f + y);
            glVertex2f(-width / 2f + x, width / 2f + y);
            glEnd();
        }
        glEnable(GL_TEXTURE_2D);
    }

    public ArrayList<Integer> getConnections()
    {
        return connections;
    }

    public ArrayList<Integer> getPlaneConnections()
    {
        return planeConnections;
    }

    public boolean isCity()
    {
        return ID == NodeType.CAIRO || ID == NodeType.TANGIER || ID == NodeType.CITY;
    }

    public boolean isSea()
    {
        return ID == NodeType.SEA_ROUTE || ID == NodeType.PIRATES;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isStartCity()
    {
        return TYPE == NodeType.CAIRO || TYPE == NodeType.TANGIER;
    }

    public HashMap<Integer, Node> getLocations()
    {
        return locations;
    }

    public boolean isPirate()
    {
        return this.TYPE == NodeType.PIRATES;
    }

    public boolean isSahara()
    {
        return this.TYPE == NodeType.SAHARA;
    }

    public HashMap<Integer, ArrayList<Route>> getNonPlaneRoutes()
    {
        return nonPlaneRoutes;
    }

    public ArrayList<Route> getPlaneRoutes()
    {
        return planeRoutes;
    }

    public ArrayList<Route> getFreeSeaRoutes()
    {
        return freeSeaRoutes;
    }

    public void shuffleArrays()
    {
        Collections.shuffle(planeRoutes);
        for (ArrayList<Route> array : nonPlaneRoutes.values())
        {
            Collections.shuffle(array);
        }
        Collections.shuffle(freeSeaRoutes);
    }

    public void setType(String type)
    {
        switch (type)
        {
            case "CAIRO":
                this.TYPE = NodeType.CAIRO;
                break;
            case "TANGIER":
                this.TYPE = NodeType.TANGIER;
                break;
            case "CITY":
                this.TYPE = NodeType.CITY;
                CITY_COUNT++;
                break;
            case "CAPETOWN":
                this.TYPE = NodeType.CAPE_TOWN;
                break;
            case "SLAVECOAST":
                this.TYPE = NodeType.SLAVE_COAST;
                break;
            case "GOLDCOAST":
                this.TYPE = NodeType.GOLD_COAST;
                break;
            case "ROUTE":
                this.TYPE = NodeType.ROUTE;
                break;
            case "SEAROUTE":
                this.TYPE = NodeType.SEA_ROUTE;
                break;
            case "SAHARA":
                this.TYPE = NodeType.SAHARA;
                break;
            case "PIRATES":
                this.TYPE = NodeType.PIRATES;
                break;

        }
    }

}
