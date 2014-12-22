/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Route;
import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class NormalAI extends AI {

    static int count = (int) (Math.random() * 2 + 1);

    public NormalAI() {
        super(count); // set the preferred start city (1 or 2)
        count = (int) (Math.random() * 2 + 1);
        //count++;
    }

    @Override
    public void act(Controller c) {
        while (!c.isEndTurn()) {
            c.setDebugString(" I am at: "+c.getCurrentNodeName());
            if (c.getMyBalance() < 100 && c.getCurrentNode().hasTreasure()) {
                c.decideTryToken();
            } else {
                c.decideToUseLandOrSeaRoute();
                ArrayList<Route> routeList = c.getMyAvailableRoutes();
                for (Route route : routeList) {
                    //System.out.println("Current: " + c.getCurrentNode().ID + " route: " + route.getDestination().ID + " price: " + route.getPrice());
                    if (route.getDestination().hasTreasure() && !c.isEndTurn()) {
                        c.moveTo(route);
                        if (c.getMyBalance() >= 100) {
                            c.buyToken();
                        } else {
                            c.endTurn();
                        }
                    }
                }
                if (!c.isEndTurn()) {
                    int target = c.getMyAvailableRoutes().size();
                    target = (int) (target*Math.random());
                    if(!c.isEndTurn())
                        c.moveTo(c.getMyAvailableRoutes().get(target));
                    c.endTurn();
                }
            }
        }
    }
    
    @Override
    public float getR() {
        return 1;
    }

    @Override
    public float getG() {
        return 1f;
    }

    @Override
    public float getB() {
        return 0.2f;
    }
    
    @Override
    public String getName(){
        return "Otso";
    }
}
