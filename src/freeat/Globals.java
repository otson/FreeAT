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
public class Globals
{
    // player variables
    public static final int START_CASH = 3;
    
    // game variables
    public static final int MAXIMUM_ROUNDS = 200;
    public static final int DISPLAY_FRAME_RATE = 60;
    public static final boolean RENDER = true;
    public static final boolean OUTPUT_WINS_TO_CONSOLE = true;

    // dice
    public static final int DICE_SIZE = 6;
    public static final int DICE_COUNT = 1;

    // Treasure counts
    public static final int STAR_OF_AFRICA_COUNT = 1;
    public static final int RUBY_COUNT = 2;
    public static final int EMERALD_COUNT = 3;
    public static final int TOPAZ_COUNT = 4;
    public static final int ROBBER_COUNT = 3;
    public static final int HORSESHOE_COUNT = 5;

    // Treasure values
    public static final int TOPAZ_VALUE = 3;
    public static final int RUBY_VALUE = 10;
    public static final int EMERALD_VALUE = 5;

    // route prices
    public static final int SEA_ROUTE_PRICE = 1;
    public static final int PLANE_ROUTE_PRICE = 3;

    // treasure buying price
    public static final int TREASURE_BUYING_PRICE = 1;
    
    // treasure opening with dice minimum value
    public static final int DICE_VALUE_TO_OPEN_TOKEN = (int) (DICE_SIZE * DICE_COUNT * 0.75);

    // Free sea movement amount
    public static final int FREE_SEA_MOVEMENT_LENGTH = 1;

}
