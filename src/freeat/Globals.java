/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.util.ArrayList;
import java.util.Collections;

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

    static int N_DICE_EVENTS = (int) Math.pow(DICE_SIZE, DICE_COUNT);

    // long startTime = System.nanoTime();
    public static final int[] OUTCOMES_ARRAY = throwManyDice();
    public static final int[] OCCURRENCES_ARRAY = countOccurrences(OUTCOMES_ARRAY);
    public static final float[] PROBABILITIES_ARRAY = computeProbabilities(OCCURRENCES_ARRAY);
    public static final float[] SAME_OR_LOWER_PROBABILITIES_ARRAY = computeCumulativeProbabilitiesUp(PROBABILITIES_ARRAY);
    public static final float[] SAMER_OR_HIGHER_PROBABILITIES_ARRAY = computeCumulativeProbabilitiesDown(PROBABILITIES_ARRAY);

    public static final float SD_DICE_VALUE = computeDiceSD();

    private static int[] throwManyDice()
    {
        // long startTime = System.nanoTime();
        int outcomesArray[] = new int[N_DICE_EVENTS];

        // initialize outcomesArray.
        for (int i = 0; i < outcomesArray.length; i++)
        {
            outcomesArray[i] = 0;
        }

        for (int diceIndex = 0; diceIndex < DICE_COUNT; diceIndex++)
        {
            int arrayIndex = 0;

            while (arrayIndex < N_DICE_EVENTS)
            {
                for (int diceOutcome = SINGLE_DICE_MIN_VALUE; diceOutcome <= SINGLE_DICE_MAX_VALUE; diceOutcome++)
                {
                    for (int repeatCount = 0; repeatCount < Math.pow(DICE_SIZE, diceIndex); repeatCount++)
                    {
                        outcomesArray[arrayIndex++] += diceOutcome;
                    }
                }
            }
        }

        // printOutcomes(outcomesArray);
        // System.out.println("Time to compute all dice outcomes: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
        return outcomesArray;
    }

    private static int[] countOccurrences(int[] outcomesArray)
    {
        // long startTime = System.nanoTime();

        int occurrencesArray[] = new int[MAX_DICE_VALUE + 1];

        // initialize occurrencesArray.
        for (int i = 0; i < occurrencesArray.length; i++)
        {
            occurrencesArray[i] = 0;
        }

        for (int i = 0; i < outcomesArray.length; i++)
        {
            int outcome = outcomesArray[i];
            occurrencesArray[outcome]++;
        }

        // printOccurrences(occurrencesArray);
        // System.exit(0);
        // ArrayList<Integer> numberOfOccurrences = new ArrayList<>(Collections.nCopies(N_DICE_EVENTS, 0));
        // System.out.println("Time to compute all dice outcomes: " + (System.nanoTime() - startTime) / 1000000 + " ms.");
        return occurrencesArray;
    }

    private static float[] computeProbabilities(int[] occurrencesArray)
    {
        float probabilitiesArray[] = new float[occurrencesArray.length];

        for (int i = 0; i < probabilitiesArray.length; i++)
        {
            probabilitiesArray[i] = (float) occurrencesArray[i] / N_DICE_EVENTS;
        }

        // printProbabilities(probabilitiesArray);
        // System.exit(0);
        return probabilitiesArray;
    }

    private static float[] computeCumulativeProbabilitiesUp(float[] probabilitiesArray)
    {
        float[] sameOrLowerProbabilitiesArray = new float[probabilitiesArray.length];
        float cumulativeSum = 0;

        for (int i = 0; i < sameOrLowerProbabilitiesArray.length; i++)
        {
            cumulativeSum += probabilitiesArray[i];
            sameOrLowerProbabilitiesArray[i] = cumulativeSum;
        }

        // printProbabilities(sameOrLowerProbabilitiesArray);
        return sameOrLowerProbabilitiesArray;
    }

    private static float[] computeCumulativeProbabilitiesDown(float[] probabilitiesArray)
    {
        float[] sameOrHigherProbabilitiesArray = new float[probabilitiesArray.length];
        float cumulativeSum = 0;

        for (int i = sameOrHigherProbabilitiesArray.length - 1; i >= 0; i--)
        {
            cumulativeSum += probabilitiesArray[i];
            sameOrHigherProbabilitiesArray[i] = cumulativeSum;
        }

        // printProbabilities(sameOrHigherProbabilitiesArray);
        return sameOrHigherProbabilitiesArray;
    }

    private static void printOutcomes(int[] outcomesArray)
    {
        for (int i = 0; i < outcomesArray.length; i++)
        {
            System.out.println("outcome (" + i + "): " + outcomesArray[i]);
        }
    }

    private static void printOccurrences(int[] occurrencesArray)
    {
        for (int i = 0; i < occurrencesArray.length; i++)
        {
            System.out.println("outcome " + i + " occurs " + occurrencesArray[i] + " times.");
        }
    }

    private static void printProbabilities(float[] probabilitiesArray)
    {
        for (int i = 0; i < probabilitiesArray.length; i++)
        {
            System.out.println("outcome " + i + " probability is " + probabilitiesArray[i] + " .");
        }
    }

    //System.out.println("Time to compute connections hash map: " + (System.nanoTime() - startTime) / 1000000) + " ms.");
    //System.exit(0);
    private static float computeDiceSD()
    {
        int diceCount = 3;
        int diceMinValue = 1;
        int diceMax = 6;
        ArrayList<Integer> list = new ArrayList<Integer>(Collections.nCopies(diceCount, diceMinValue));
        boolean throwingDice = true;
        while (throwingDice)
        {
            throwingDice = false;
            int result = 0;
            for (int i = 0; i < diceCount; i++)
            {
                result += list.get(i);
            }
            // System.out.println("Result: " + result);

            //increase one dice size here, go through all possible dice combinations
            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i) < diceMax)
                {
                    list.set(i, list.get(i) + 1);
                    throwingDice = true;
                    break;
                }
            }
        }

        // System.exit(0);
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
