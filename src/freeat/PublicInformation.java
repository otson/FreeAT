/* 
 * Copyright (C) 2017 Otso Nuortimo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package freeat;

import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class PublicInformation
{

    public static final int PLAYER_COUNT = 2;

    private static int[] cashBalance = new int[PLAYER_COUNT];
    private static int[] location = new int[PLAYER_COUNT];
    private static boolean[] hasStar = new boolean[PLAYER_COUNT];
    private static boolean[] foundCapeTown = new boolean[PLAYER_COUNT];
    private static boolean[] hasHorseshoeAfterStar = new boolean[PLAYER_COUNT];
    private static boolean[] inSahara = new boolean[PLAYER_COUNT];
    private static boolean[] inPirates = new boolean[PLAYER_COUNT];
    private static boolean[] isWinner = new boolean[PLAYER_COUNT];
    private static boolean[] isUsingFreeSeaRoute = new boolean[PLAYER_COUNT];

    private static String[] playerNames = new String[PLAYER_COUNT];

    private static int horseShoesTotal;
    private static int horseShoesLeft;
    private static int rubyTotal;
    private static int rubiesLeft;
    private static int topazTotal;
    private static int topazesLeft;
    private static int emeraldTotal;
    private static int emeraldsLeft;
    private static int robberTotal;
    private static int robberLeft;
    private static int emptyLeft;
    private static int emptyTotal;

    private static int unOpenedLeft;

    private static int treasureTotal;
    private static int treasuresLeft;

    static
    {
        cashBalance = new int[PLAYER_COUNT];
        location = new int[PLAYER_COUNT];
        hasStar = new boolean[PLAYER_COUNT];
        hasHorseshoeAfterStar = new boolean[PLAYER_COUNT];
        foundCapeTown = new boolean[PLAYER_COUNT];
    }

    public static int getBalance(int ID)
    {
        return cashBalance[ID];
    }

    public static int getLocation(int ID)
    {
        return location[ID];
    }

    public static boolean hasStar(int ID)
    {
        return hasStar[ID];
    }

    public static boolean hasHorseshoeAfterStar(int ID)
    {
        return hasHorseshoeAfterStar[ID];
    }

    public static void updateInformation(ArrayList<Player> players)
    {
        for (Player player : players)
        {
            cashBalance[player.ID] = player.getCashBalance();
            location[player.ID] = player.getLocation();
            hasStar[player.ID] = player.isHasStar();
            hasHorseshoeAfterStar[player.ID] = player.isHasHorseshoeAfterStar();
            inPirates[player.ID] = player.isInPirates();
            inSahara[player.ID] = player.isInSahara();
            foundCapeTown[player.ID] = player.isFoundCapeTown();
            isWinner[player.ID] = player.isWinner();
            playerNames[player.ID] = player.getName();
            isUsingFreeSeaRoute[player.ID] = player.isUsingFreeSeaRoute();
        }
    }

    public static boolean isStarFound()
    {
        for (int i = 0; i < hasStar.length; i++)
        {
            if (hasStar[i])
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isCapeTownBonus()
    {
        for (int i = 0; i < foundCapeTown.length; i++)
        {
            if (foundCapeTown[i])
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isWinner()
    {
        for (int i = 0; i < isWinner.length; i++)
        {
            if (isWinner[i])
            {
                return true;
            }
        }
        return false;
    }

    public static int getWinner()
    {
        for (int i = 0; i < isWinner.length; i++)
        {
            if (isWinner[i])
            {
                return i;
            }
        }
        System.out.println("No winner! false");
        return -1;
    }

    public static void reset()
    {
        cashBalance = new int[PLAYER_COUNT];
        location = new int[PLAYER_COUNT];
        hasStar = new boolean[PLAYER_COUNT];
        foundCapeTown = new boolean[PLAYER_COUNT];
        hasHorseshoeAfterStar = new boolean[PLAYER_COUNT];
        inSahara = new boolean[PLAYER_COUNT];
        inPirates = new boolean[PLAYER_COUNT];
        isWinner = new boolean[PLAYER_COUNT];
        emeraldsLeft = emeraldTotal;
        topazesLeft = topazTotal;
        rubiesLeft = rubyTotal;
        emptyLeft = emptyTotal;
        robberLeft = robberTotal;
        horseShoesLeft = horseShoesTotal;
        
        
    }

    public static int getHorseShoesTotal()
    {
        return horseShoesTotal;
    }

    public static int getHorseShoesLeft()
    {
        return horseShoesLeft;
    }

    public static int getRubyTotal()
    {
        return rubyTotal;
    }

    public static int getRubiesLeft()
    {
        return rubiesLeft;
    }

    public static int getTopazTotal()
    {
        return topazTotal;
    }

    public static int getTopazesLeft()
    {
        return topazesLeft;
    }

    public static int getEmeraldTotal()
    {
        return emeraldTotal;
    }

    public static int getEmeraldsLeft()
    {
        return emeraldsLeft;
    }

    public static int getRobberTotal()
    {
        return robberTotal;
    }

    public static int getRobberLeft()
    {
        return robberLeft;
    }

    public static void setHorseShoesTotal(int horseShoesTotal)
    {
        PublicInformation.horseShoesTotal = horseShoesTotal;
    }

    public static void setRubyTotal(int rubyTotal)
    {
        PublicInformation.rubyTotal = rubyTotal;
    }

    public static void setTopazTotal(int topazTotal)
    {
        PublicInformation.topazTotal = topazTotal;
    }

    public static void setEmeraldTotal(int emeraldTotal)
    {
        PublicInformation.emeraldTotal = emeraldTotal;
    }

    public static void setRobberTotal(int robberTotal)
    {
        PublicInformation.robberTotal = robberTotal;
    }

    static boolean isUsingFreeSeaRoute(int playerID)
    {
        return isUsingFreeSeaRoute[playerID];
    }

    public int getTreasuresLeft()
    {
        return treasuresLeft;
    }

    public void setTreasuresLeft(int treasuresLeft)
    {
        PublicInformation.treasuresLeft = treasuresLeft;
    }

    public static void setTreasureTotal(int treasureTotal)
    {
        PublicInformation.treasureTotal = treasureTotal;
    }

    public static void removeEmerald()
    {
        emeraldsLeft--;
    }

    public static void removeRobber()
    {
        robberLeft--;
    }

    public static void removeTopaz()
    {
        topazesLeft--;
    }

    public static void removeRuby()
    {
        rubiesLeft--;
    }

    public static void removeHorseShoe()
    {
        horseShoesLeft--;
    }

    public static int getTreasureTotal()
    {
        return treasureTotal;
    }

    public static int getEmptyLeft()
    {
        return emptyLeft;
    }

    public static int getEmptyTotal()
    {
        return emptyTotal;
    }

    public static int getUnOpenedLeft()
    {
        return unOpenedLeft;
    }

    public static void removeUnopened()
    {
        unOpenedLeft--;
    }

    public static void removeEmpty()
    {
        emptyLeft--;
    }

    public static void setEmptyLeft(int emptyLeft)
    {
        PublicInformation.emptyLeft = emptyLeft;
    }

    public static void setEmptyTotal(int emptyTotal)
    {
        PublicInformation.emptyTotal = emptyTotal;
        emptyLeft = emptyTotal;
    }

    public static void setUnOpenedLeft(int unOpenedLeft)
    {
        PublicInformation.unOpenedLeft = unOpenedLeft;
    }

    public static String getName(int id)
    {
        return playerNames[id];
    }

}
