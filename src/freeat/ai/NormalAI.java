/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Node;
import freeat.Player;
import freeat.Route;
import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class NormalAI extends AI {

    static int count = 1;

    public NormalAI() {
        super(count); // set the preferred start city (1 or 2)
        //count++;
    }

    @Override
    public void act(Controller c) {
        // loop ends when there are no moves left and 
        // the boolean to end the turn has been set
        while (!c.isEndTurn()) {
            c.setDebugString("jee");
            if (c.getMyBalance() < 100 && c.getCurrentNode().hasTreasure()) {
                c.decideTryToken();
            } else {
//                if (c.getMyBalance() >= 400 && c.isAvailablePlanesFromCurrentNode()) {
//                    c.decidetoUsePlane();
//                    ArrayList<Route> routes = c.getAvailableRoutes(c.getCurrentNode(), 300, 1);
//                    for (Route route : routes) {
//                        if (route.getPrice() == 300) {
//                            if (route.getDestination().hasTreasure()) {
//                                if (!c.hasMoved()) {
//                                    c.moveTo(route);
//                                    if (c.getCurrentNode().hasTreasure()) {
//                                        c.buyToken();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (!c.hasMoved()) {
//                        for (Route route : routes) {
//                            if (!c.hasMoved() && route.getPrice() == 300) {
//                                c.moveTo(route);
//                            }
//                        }
//                    }
//                } 
//                else {
                c.decideToUseLandOrSeaRoute();
                ArrayList<Route> routes0 = c.getAvailableRoutes(c.getCurrentNode(), 100, c.getDice());
                for (Route route : routes0) {
                    if (!c.hasMoved() && route.getDestination().hasTreasure()) {
                        c.moveTo(route);
                        if (c.getMyBalance() >= 100) {
                            c.buyToken();
                        }
                        c.endTurn();
                    }
                }
                ArrayList<Route> routes100 = c.getAvailableRoutes(c.getCurrentNode(), 0, c.getDice());
                for (Route route : routes100) {
                    if (!c.hasMoved() && route.getDestination().hasTreasure()) {
                        c.moveTo(route);
                        if (c.getMyBalance() >= 100) {
                            c.buyToken();
                        }
                        c.endTurn();
                    }
                }
                if (!c.hasMoved()) {
                    int target = (int) (Math.random() * routes0.size());
                    c.moveTo(routes0.get(target));
                    c.endTurn();
                }
                if (!c.hasMoved()) {
                    int target = (int) (Math.random() * routes100.size());
                    c.moveTo(routes100.get(target));
                    c.endTurn();
                }
            }
        }
        
    }
}
