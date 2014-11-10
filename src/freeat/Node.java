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
    //public final int TYPE;
    public final int x;
    public final int y;
    public final int ID;
    private int treasure;
    private ArrayList<Integer> connections;
    private HashMap<Integer, Node> locations;
    private int width = 30;

    public Node(int id, int x, int y, HashMap<Integer, Node> locations) {
        this.ID = id;
        this.x = x;
        this.y = Main.WINDOW_HEIGHT - y;
        this.locations = locations;
        connections = new ArrayList<>();
        if (ID > 100 && ID < 200) {
            CITY_COUNT++;
        }
    }

    public void addConnection(Integer connection) {
        if (locations.containsKey(connection)) {
            connections.add(connection);
        }
    }

    public int getTreasure() {
        return treasure;
    }

    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public void print() {
        if (treasure == TreasureType.EMPTY) {
            //System.out.println("Id: " + ID + " x: " + x + " y: " + y + " connections: " + connectionsToString());
        } else {
            System.out.println("Id: " + ID + " x: " + x + " y: " + y + " connections: " + connectionsToString() + " treasure: " + treasure);
        }

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
}
