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
    public static final int SINGLE_DICE_MIN_VALUE = 1;
    public static final int SINGLE_DICE_MAX_VALUE = 6;

    public static final int DICE_COUNT = 1;

    // Treasure counts
    public static final int STAR_OF_AFRICA_COUNT = 1;
    public static final int RUBY_COUNT = 2;
    public static final int EMERALD_COUNT = 3;
    public static final int TOPAZ_COUNT = 4;
    public static final int ROBBER_COUNT = 3;
    public static final int HORSESHOE_COUNT = 5;

    public static final int NUMBER_OF_TREASURE_TYPES = 7;

    // Treasure values
    public static final int TOPAZ_VALUE = 3;
    public static final int RUBY_VALUE = 5;
    public static final int EMERALD_VALUE = 10;

    // route prices
    public static final int SEA_ROUTE_PRICE = 1;
    public static final int PLANE_ROUTE_PRICE = 3;

    // treasure buying price
    public static final int TREASURE_BUYING_PRICE = 1;

    // Free sea movement amount
    public static final int FREE_SEA_MOVEMENT_LENGTH = 1;

    // Place specific globals
    public static final int GOLD_COAST_FACTOR = 2;
    public static final int SLAVE_COAST_WAIT_TIME = 3;
    public static final int CAPE_TOWN_BONUS = 5;

    // Values based on set Global values, do not modify
    // Dice
    public static final int DICE_SIZE = SINGLE_DICE_MAX_VALUE - SINGLE_DICE_MIN_VALUE + 1;
    public static final int MIN_DICE_VALUE = DICE_COUNT * SINGLE_DICE_MIN_VALUE;
    public static final int MAX_DICE_VALUE = DICE_COUNT * SINGLE_DICE_MAX_VALUE;

    // mean of dice value.
    public static final float MEAN_DICE_VALUE = DICE_COUNT * ((float) (SINGLE_DICE_MIN_VALUE + SINGLE_DICE_MAX_VALUE) / 2);

    private static float computeDiceSD()
    {
        // compute the standard deviation of dice value.
        float sumOfSquaredDeviations = 0;
        for (float i = SINGLE_DICE_MIN_VALUE; i <= SINGLE_DICE_MAX_VALUE; i++)
        {
            sumOfSquaredDeviations += Math.pow((i - MEAN_DICE_VALUE), 2);
        }
        return DICE_COUNT * (sumOfSquaredDeviations / DICE_SIZE);
    }

    public static final float SD_DICE_VALUE = computeDiceSD();

    // treasure opening with dice minimum value
    public static final int DICE_VALUE_TO_OPEN_TOKEN = (int) (MAX_DICE_VALUE * 0.75);

    // Sea movement price maximum price
    public static final int MAX_SEA_MOVEMENT_COST = (int) Math.ceil(MAX_DICE_VALUE * SEA_ROUTE_PRICE / 2);

    public static final int MAX_ROUTE_COST = 3;

}
