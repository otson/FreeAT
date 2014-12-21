/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

/**
 *
 * @author otso
 */
public class Route {
    
    Node destination;
    int price;
    
    Route(Node destination, int price){
        this.destination = destination;
        this.price = price;
    }
}
