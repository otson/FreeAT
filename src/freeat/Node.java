/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.util.ArrayList;
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
public class Node {

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

    //Routes that cost 0
    //Distance of 1
    private ArrayList<Route> distance1Cost0;

    //Routes that cost 0
    //Distance of 2
    private ArrayList<Route> distance2Cost0;

    //Routes that cost 0
    //Distance of 3
    private ArrayList<Route> distance3Cost0;

    //Routes that cost 0
    //Distance of 4
    private ArrayList<Route> distance4Cost0;

    //Routes that cost 0
    //Distance of 5
    private ArrayList<Route> distance5Cost0;

    //Routes that cost 0
    //Distance of 6
    private ArrayList<Route> distance6Cost0;

    //Routes that cost 100 or less
    //Distance of 1
    private ArrayList<Route> distance1Cost100;

    //Routes that cost 100 or less
    //Distance of 2
    private ArrayList<Route> distance2Cost100;

    //Routes that cost 100 or less
    //Distance of 3
    private ArrayList<Route> distance3Cost100;

    //Routes that cost 100 or less
    //Distance of 4
    private ArrayList<Route> distance4Cost100;

    //Routes that cost 100 or less
    //Distance of 5
    private ArrayList<Route> distance5Cost100;

    //Routes that cost 100 or less
    //Distance of 6
    private ArrayList<Route> distance6Cost100;

    //Routes that cost 200 or less
    //Distance of 1
    private ArrayList<Route> distance1Cost200;

    //Routes that cost 200 or less
    //Distance of 2
    private ArrayList<Route> distance2Cost200;

    //Routes that cost 200 or less
    //Distance of 3
    private ArrayList<Route> distance3Cost200;

    //Routes that cost 200 or less
    //Distance of 4
    private ArrayList<Route> distance4Cost200;

    //Routes that cost 200 or less
    //Distance of 5
    private ArrayList<Route> distance5Cost200;

    //Routes that cost 200 or less
    //Distance of 6
    private ArrayList<Route> distance6Cost200;

    //Routes that cost 300 or less
    //Distance of 1
    private ArrayList<Route> distance1Cost300;

    //Routes that cost 300 or less
    //Distance of 2
    private ArrayList<Route> distance2Cost300;

    //Routes that cost 300 or less
    //Distance of 3
    private ArrayList<Route> distance3Cost300;

    //Routes that cost 300 or less
    //Distance of 4
    private ArrayList<Route> distance4Cost300;

    //Routes that cost 300 or less
    //Distance of 5
    private ArrayList<Route> distance5Cost300;

    //Routes that cost 300 or less
    //Distance of 6
    private ArrayList<Route> distance6Cost300;

    public Node(int id, int x, int y, HashMap<Integer, Node> locations) {
        this.ID = id;
        this.x = x;
        this.y = Main.WINDOW_HEIGHT - y;
        this.locations = locations;
        connections = new ArrayList<>();
        if (ID == 1) {
            TYPE = NodeType.CAIRO;
        }
        if (ID == 2) {
            TYPE = NodeType.TANGIR;
        }
        if (ID > 100 && ID < 200) {
            TYPE = NodeType.CITY;
            CITY_COUNT++;
        }
        if (ID > 200 && ID < 400) {
            TYPE = NodeType.ROUTE;
        }
        if (ID == 400) {
            TYPE = NodeType.SAHARA;
        }
        if (ID > 500 && ID < 600) {
            TYPE = NodeType.SEA_ROUTE;
        }

        if (ID == 123) {
            TYPE = NodeType.SLAVE_COAST;
        }
        if (ID == 125) {
            TYPE = NodeType.GOLD_COAST;
        }
        if (ID == 120) {
            TYPE = NodeType.CAPE_TOWN;
        }
    }

    public void addConnection(Integer connection) {
        if (locations.containsKey(connection)) {
            connections.add(connection);
        }
    }

    public void addPlaneConnection(Integer connection) {
        if (locations.containsKey(connection)) {
            planeConnections.add(connection);
        }
    }

    public int getTreasure() {
        return treasure;
    }

    public void setTreasure(int type) {
        treasure = type;
        hasTreasure = true;
    }

    public void removeTreasure() {
        treasure = TreasureType.OPENED;
        hasTreasure = false;
    }

    public void print() {
        System.out.println("Id: " + ID + " x: " + x + " y: " + y + " connections: " + connectionsToString());
    }

    public boolean hasTreasure() {
        return hasTreasure;
    }

    public int getTYPE() {
        return TYPE;
    }

    public boolean hasConnection(Integer connection) {
        return connections.contains(connection);
    }

    public boolean hasPlaneConnection(Integer connection) {
        return planeConnections.contains(connection);
    }

    private String connectionsToString() {
        String string = "";
        for (Integer id : connections) {
            string = string.concat(locations.get(id).ID + " ");
        }
        return string;
    }

    public void draw() {
        float r = 0;
        float g = 0;
        float b = 0;
        glDisable(GL_TEXTURE_2D);
        if (ID > 100 && ID < 200) {
            if (treasure == TreasureType.EMPTY) {
                r = 0.8f;
                g = 0.8f;
                b = 0.9f;
            } else if (treasure == TreasureType.EMERALD) {
                r = 0;
                g = 1;
                b = 0;
            } else if (treasure == TreasureType.RUBY) {
                r = 1;
                g = 0;
                b = 0;
            } else if (treasure == TreasureType.TOPAZ) {
                r = 0;
                g = 0;
                b = 1;
            } else if (treasure == TreasureType.STAR_OF_AFRICA) {
                r = 1;
                g = 1;
                b = 0;
            } else if (treasure == TreasureType.HORSESHOE) {
                r = 0.4f;
                g = 0.4f;
                b = 0.4f;
            } else if (treasure == TreasureType.ROBBER) {
                r = 0.0f;
                g = 0.0f;
                b = 0.0f;
            } else if (treasure == TreasureType.OPENED) {
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

    public ArrayList<Integer> getConnections() {
        return connections;
    }

    public ArrayList<Integer> getPlaneConnections() {
        return planeConnections;
    }

    public ArrayList<Route> getDistance1Cost0() {
        return distance1Cost0;
    }

    public ArrayList<Route> getDistance2Cost0() {
        return distance2Cost0;
    }

    public ArrayList<Route> getDistance3Cost0() {
        return distance3Cost0;
    }

    public ArrayList<Route> getDistance4Cost0() {
        return distance4Cost0;
    }

    public ArrayList<Route> getDistance5Cost0() {
        return distance5Cost0;
    }

    public ArrayList<Route> getDistance6Cost0() {
        return distance6Cost0;
    }

    public ArrayList<Route> getDistance1Cost100() {
        return distance1Cost100;
    }

    public ArrayList<Route> getDistance2Cost100() {
        return distance2Cost100;
    }

    public ArrayList<Route> getDistance3Cost100() {
        return distance3Cost100;
    }

    public ArrayList<Route> getDistance4Cost100() {
        return distance4Cost100;
    }

    public ArrayList<Route> getDistance5Cost100() {
        return distance5Cost100;
    }

    public ArrayList<Route> getDistance6Cost100() {
        return distance6Cost100;
    }

    public ArrayList<Route> getDistance1Cost200() {
        return distance1Cost200;
    }

    public ArrayList<Route> getDistance2Cost200() {
        return distance2Cost200;
    }

    public ArrayList<Route> getDistance3Cost200() {
        return distance3Cost200;
    }

    public ArrayList<Route> getDistance4Cost200() {
        return distance4Cost200;
    }

    public ArrayList<Route> getDistance5Cost200() {
        return distance5Cost200;
    }

    public ArrayList<Route> getDistance6Cost200() {
        return distance6Cost200;
    }

    public ArrayList<Route> getDistance1Cost300() {
        return distance1Cost300;
    }

    public ArrayList<Route> getDistance2Cost300() {
        return distance2Cost300;
    }

    public ArrayList<Route> getDistance3Cost300() {
        return distance3Cost300;
    }

    public ArrayList<Route> getDistance4Cost300() {
        return distance4Cost300;
    }

    public ArrayList<Route> getDistance5Cost300() {
        return distance5Cost300;
    }

    public ArrayList<Route> getDistance6Cost300() {
        return distance6Cost300;
    }

}
