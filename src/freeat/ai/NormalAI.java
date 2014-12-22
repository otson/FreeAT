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

    static int count = 1;

    public NormalAI() {
        super(count); // set the preferred start city (1 or 2)
        //count++;
    }

    @Override
    public void act(Controller c) {
        /*
         while (Keyboard.next()) {

         if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
         glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
         }
         */
        while (!c.isEndTurn()) {
            c.setDebugString("fee");
            if (c.getMyBalance() < 100 && c.getCurrentNode().hasTreasure()) {
                c.decideTryToken();
            } else {
                c.decideToUseLandOrSeaRoute();
                System.out.println("Dice: "+6);
                System.out.println("Routes available from " + c.getCurrentNode().ID + " : " + c.getAvailableRoutes(c.getCurrentNode(), 100, 6).size());
                for (int i = 0; i < c.getAvailableRoutes(c.getCurrentNode(), 100, 6).size(); i++) {
                    System.out.println("To: " + c.getAvailableRoutes(c.getCurrentNode(), 100, 6).get(i).getDestination().ID);
                }
                ArrayList<Route> routeList = c.getMyAvailableRoutes();
                int routeListSize = routeList.size();
                for (Route route : routeList) {
                    //System.out.println("Current: " + c.getCurrentNode().ID + " route: " + route.getDestination().ID + " price: " + route.getPrice());
                    if (route.getDestination().hasTreasure() && !c.isEndTurn()) {
                        c.moveTo(route);
                        System.out.println("here");
                        if (c.getMyBalance() >= 100) {
                            c.buyToken();
                        } else {
                            c.endTurn();
                        }
                    }
                }
                if (!c.isEndTurn()) {
                    for (Route route : c.getMyAvailableRoutes()) {
                        if (!c.isEndTurn()) {
                            System.out.println("here");
                            c.moveTo(route);
                            c.endTurn();
                        }
                    }
                }
            }
        }
    }
}
