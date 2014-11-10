/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import freeat.ai.AI;
import freeat.ai.NormalAI;

/**
 *
 * @author otso
 */
public class Player {
    
    private int cash;
    private boolean hasStar;
    private AI ai;
    
    public Player(){
        cash = 300;
        hasStar = false;
        ai = new NormalAI();
    }
    
    public void act(){
        ai.act();
    }
}
