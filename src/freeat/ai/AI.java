/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Node;
import freeat.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author otso
 */
public abstract class AI {

    public final int START;
    protected HashMap<Integer, Node> locations;
    // public static ArrayList<String> AIIdentifications = new ArrayList();
    public static Set<String> AIIdentifications = new HashSet<>();
    private ArrayList<DrawNode> drawList = new ArrayList<>();
    protected Controller c;
    
    
    
    public AI(int start) {
        START = start;
    }

    public void setLocations(HashMap<Integer, Node> locations) {
        this.locations = locations;
    }

    public abstract void act();

    public float getR() {
        return 0;
    }

    public float getG() {
        return 1;
    }

    public float getB() {
        return 0;
    }
    
    public String getName(){
        return "default";
    }

    public ArrayList<DrawNode> getDrawList() {
        return drawList;
    }
    
    protected void addDraw(int r, int g, int b, int nodeID){
        drawList.add(new DrawNode(r,g,b, nodeID));
    }
    
    protected void clearDraw(){
        drawList.clear();
    }

    public void setController(Controller controller) {
        this.c = controller;
    }

    
    
}
