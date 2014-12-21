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
    public void act(Player player) {
//        int dice = (int) (Math.random() * 6 + 1);
//        for (int i = 0; i < dice; i++) {
//            //System.out.println("balance: " + player.getCashBalance());
//            Node current = player.getCurrentNode();
//            if (player.isStayInCity()) {
//                player.tryToWinToken();
//                return;
//            }
//            int targetID = (int) (Math.random() * current.getConnections().size());
//            int target = current.getConnections().get(targetID);
//            Node targetNode = locations.get(target);
//            if (!targetNode.getConnections().isEmpty()) {
//                player.moveTo(target);
//                if (targetNode.hasTreasure()) {
//                    if (player.getCashBalance() >= 100) {
//                        player.buyToken();
//
//                    } else {
//                        player.stayInCity();
//                        return;
//                    }
//                }
//
//            }
//        }
    }

    @Override
    public void act(Controller c) {
        // loop ends when there are no moves left and 
        // the boolean to end the turn has been set
        //while (!c.isEndTurn()) {
        
        if (c.getMyBalance() < 100 && c.getCurrentNode().hasTreasure()) {
            c.decideTryToken();
        } else {
//            if (c.getMyBalance() >= 400 && c.isAvailablePlanesFromCurrentNode()) {
//                c.decidetoUsePlane();
//                ArrayList<Route> routes = c.getAvailableRoutes(c.getCurrentNode(), 300, 1);
//                for (Route route : routes) {
//                    if (route.getPrice() == 300) {
//                        if (route.getDestination().hasTreasure()) {
//                            if (!c.hasMoved()) {
//                                c.moveTo(route);
//                                c.buyToken();
//                            }
//                        }
//                    }
//                }
//                if (!c.hasMoved()) {
//                    c.moveTo(routes.get(1));
//                    c.endTurn();
//                }
//            } else {
            c.decideToUseLandOrSeaRoute();
            ArrayList<Route> routes = c.getAvailableRoutes(c.getCurrentNode(), 0, c.getDice());
            for (Route route : routes) {
                if (!c.hasMoved() && route.getDestination().hasTreasure()) {
                    c.moveTo(route);
                    if (c.getMyBalance() >= 100) {
                        c.buyToken();
                    }
                }
            }
            if (!c.hasMoved()) {
                int target = (int) (Math.random()*routes.size());
                c.moveTo(routes.get(target));
            }
        }
        //}
        //}
    }
}
