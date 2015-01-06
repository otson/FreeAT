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
    public static final int MAXIMUM_ROUNDS = 150;
    public static final int DISPLAY_FRAME_RATE = 60;
    public static final boolean RENDER = false;
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
    public static final int RUBY_VALUE = 5;
    public static final int EMERALD_VALUE = 10;

    // route prices
    public static final int SEA_ROUTE_PRICE = 1;
    public static final int PLANE_ROUTE_PRICE = 3;

    // treasure buying price
    public static final int TREASURE_BUYING_PRICE = 1;

    // treasure opening with dice minimum value
    public static final int DICE_VALUE_TO_OPEN_TOKEN = (int) (DICE_SIZE * DICE_COUNT * 0.75);

    // Free sea movement amount
    public static final int FREE_SEA_MOVEMENT_LENGTH = 1;

    // Place specific globals
    public static final int GOLD_COAST_FACTOR = 2;
    public static final int SLAVE_COAST_WAIT_TIME = 3;
    public static final int CAPE_TOWN_BONUS = 5;

    // Values based on set Global values, do not modify
    // Dice
    public static final int MAX_DICE_VALUE = DICE_COUNT * DICE_SIZE;

    // Sea movement price maximum price
    public static final int MAX_SEA_MOVEMENT_COST = (int) Math.ceil(MAX_DICE_VALUE * SEA_ROUTE_PRICE / 2);
    
    public static final int MAX_ROUTE_COST = 3;

}
