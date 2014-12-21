/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Node;
import freeat.Player;

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
    public void act(Controller c){
        // loop ends when there are no moves left and 
        // the boolean to end the turn has been set
        while(!c.isEndTurn()){
            
        }
    }
}
