/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.util.ArrayList;
import java.util.HashMap;
import freeat.Route;
import java.util.Collection;

/**
 *
 * @author otso
 */
public class Controller
{

    private Player player;
    private HashMap<Integer, Node> locations;

    public final int ID;

    public Controller(Player player, HashMap<Integer, Node> locations)
    {
        this.player = player;
        this.locations = locations;
        this.ID = player.ID;

    }

    public int getMyBalance()
    {
        return player.getCashBalance();
    }

    public boolean isAvailablePlanesFromCurrentNode()
    {
        return !player.getCurrentNode().getPlaneConnections().isEmpty();
    }

    public int getBalance(int playedID)
    {
        return PublicInformation.getBalance(playedID);
    }

    public int getLocation(int playedID)
    {
        return PublicInformation.getLocation(playedID);
    }

    public boolean hasStar(int playedID)
    {
        return PublicInformation.hasStar(playedID);
    }

    public boolean hasHorseShoeAfterStar(int playerID)
    {
        return PublicInformation.hasHorseshoeAfterStar(playerID);
    }

    public boolean isStarFound()
    {
        return PublicInformation.isStarFound();
    }

    public boolean isCapeTownBonus()
    {
        return PublicInformation.isCapeTownBonus();
    }

    public Node getCurrentNode()
    {
        return player.getCurrentNode();
    }

    public Node getNode(int nodeID)
    {
        return player.getNode(nodeID);
    }

    public int totalTreasures()
    {
        return PublicInformation.getTreasureTotal();
    }

    public int rubiesLeft()
    {
        return PublicInformation.getRubiesLeft();
    }

    public int topazesLeft()
    {
        return PublicInformation.getTopazesLeft();
    }

    public int robbersLeft()
    {
        return PublicInformation.getRobberLeft();
    }

    public int emeraldsLeft()
    {
        return PublicInformation.getEmeraldsLeft();
    }

    public int horseShoesLeft()
    {
        return PublicInformation.getHorseShoesLeft();
    }

    public int emptyLeft()
    {
        return PublicInformation.getEmptyLeft();
    }

    public int unopenedLeft()
    {
        return PublicInformation.getUnOpenedLeft();
    }

    public boolean isEndTurn()
    {
        return player.isEndTurn();
    }

    public int getMyID()
    {
        return player.ID;
    }

    public boolean hasStar()
    {
        return player.isHasStar();

    }

    public boolean hasHorseShoeAfterStar()
    {
        return player.isHasHorseshoeAfterStar();
    }

    public ArrayList<Route> getMyAvailableRoutes()
    {
        if (player.isUsePlane())
        {
            return getAvailableRoutes(player.getCurrentNode(), 300, 1);
        }

        if (getAvailableRoutes(player.getCurrentNode(), player.getCashBalance(), player.getDice()).isEmpty())
        {
            player.forceEndTurn();
        }
        return getAvailableRoutes(player.getCurrentNode(), player.getCashBalance(), player.getDice());
    }

    public ArrayList<Route> getAvailableRoutes(Node start, int cash, int dice)
    {
        if (cash < 100)
        {
            switch (dice)
            {
                case 1:
                    return locations.get(start.ID).getDistance1Cost0();
                case 2:
                    return locations.get(start.ID).getDistance2Cost0();
                case 3:
                    return locations.get(start.ID).getDistance3Cost0();
                case 4:
                    return locations.get(start.ID).getDistance4Cost0();
                case 5:
                    return locations.get(start.ID).getDistance5Cost0();
                case 6:
                    return locations.get(start.ID).getDistance6Cost0();
            }
        } else if (cash < 200)
        {
            switch (dice)
            {
                case 1:
                    return locations.get(start.ID).getDistance1Cost100();
                case 2:
                    return locations.get(start.ID).getDistance2Cost100();
                case 3:
                    return locations.get(start.ID).getDistance3Cost100();
                case 4:
                    return locations.get(start.ID).getDistance4Cost100();
                case 5:
                    return locations.get(start.ID).getDistance5Cost100();
                case 6:
                    return locations.get(start.ID).getDistance6Cost100();
            }
        } else if (cash < 300)
        {
            switch (dice)
            {
                case 1:
                    return locations.get(start.ID).getDistance1Cost200();
                case 2:
                    return locations.get(start.ID).getDistance2Cost200();
                case 3:
                    return locations.get(start.ID).getDistance3Cost200();
                case 4:
                    return locations.get(start.ID).getDistance4Cost200();
                case 5:
                    return locations.get(start.ID).getDistance5Cost200();
                case 6:
                    return locations.get(start.ID).getDistance6Cost200();
            }
        } else if (cash >= 300)
        {
            switch (dice)
            {
                case 1:
                    return locations.get(start.ID).getDistance1Cost300();
                case 2:
                    return locations.get(start.ID).getDistance2Cost300();
                case 3:
                    return locations.get(start.ID).getDistance3Cost300();
                case 4:
                    return locations.get(start.ID).getDistance4Cost300();
                case 5:
                    return locations.get(start.ID).getDistance5Cost300();
                case 6:
                    return locations.get(start.ID).getDistance6Cost300();
            }
        }

        System.out.println("No destinations found, error...");
        return null;

    }

    public void decidetoUsePlane()
    {
        player.decidetoUsePlane();
    }

    public void decideTryToken()
    {
        player.decideToTryToken();
    }

    public void decideToUseLandOrSeaRoute()
    {
        player.decideToUseLandOrSeaRoute();
    }

    public void moveTo(Route destination)
    {
        player.moveTo(destination);
    }

    public void endTurn()
    {
        player.endTurn();
    }

    public void buyToken()
    {
        player.buyToken();
    }

    public int getPlayerCount()
    {
        return PublicInformation.PLAYER_COUNT;
    }

    public boolean hasMoved()
    {
        return player.hasMoved();

    }

    public int getDice()
    {
        return player.getDice();
    }

    public void setDebugString(String debugString)
    {
        player.setDebugString(debugString);
    }

    public void resetDebugString()
    {
        player.resetDebugString();
    }

    public void concatDebugString(String debugString)
    {
        player.concatDebugString(debugString);
    }

    public String getCurrentNodeName()
    {
        return getCurrentNode().getName();
    }

    public HashMap<Integer, Node> getNodeList()
    {
        return player.getCurrentNode().getLocations();

    }

    public boolean isEligibleForWinning()
    {
        return player.isHasStar() || player.isHasHorseshoeAfterStar();
    }

    public ArrayList<Node> getRemainingTreasures()
    {
        int size = getNodeList().size();
        ArrayList<Node> valuesList = new ArrayList<>(getNodeList().values());
        for (int i = 0; i < valuesList.size(); i++)
        {
            if (!valuesList.get(i).hasTreasure())
            {
                valuesList.remove(i);
                i--;
            }
        }
        if (size != getNodeList().size())
        {
            System.out.println("ERROR");
        }
        return valuesList;
    }

    public ArrayList<Node> getAllCities()
    {
        int size = getNodeList().size();
        ArrayList<Node> valuesList = new ArrayList<>(getNodeList().values());
        for (int i = 0; i < valuesList.size(); i++)
        {
            if (!valuesList.get(i).isCity())
            {
                valuesList.remove(i);
                i--;
            }
        }
        if (size != getNodeList().size())
        {
            System.out.println("ERROR");
        }
        return valuesList;
    }

    public ArrayList<Node> getAllRedCities()
    {
        int size = getNodeList().size();
        ArrayList<Node> valuesList = new ArrayList<>(getNodeList().values());
        for (int i = 0; i < valuesList.size(); i++)
        {
            if (!valuesList.get(i).isCity() || valuesList.get(i).isStartCity())
            {
                valuesList.remove(i);
                i--;
            }
        }
        if (size != getNodeList().size())
        {
            System.out.println("ERROR");
        }
        return valuesList;
    }

    public ArrayList<Node> getAllPirates()
    {
        int size = getNodeList().size();
        ArrayList<Node> valuesList = new ArrayList<>(getNodeList().values());
        for (int i = 0; i < valuesList.size(); i++)
        {
            if (!valuesList.get(i).isPirate())
            {
                valuesList.remove(i);
                i--;
            }
        }
        if (size != getNodeList().size())
        {
            System.out.println("ERROR");
        }
        return valuesList;
    }

    public ArrayList<Node> getAllSaharas()
    {
        int size = getNodeList().size();
        ArrayList<Node> valuesList = new ArrayList<>(getNodeList().values());
        for (int i = 0; i < valuesList.size(); i++)
        {
            if (!valuesList.get(i).isSahara())
            {
                valuesList.remove(i);
                i--;
            }
        }
        if (size != getNodeList().size())
        {
            System.out.println("ERROR");
        }
        return valuesList;
    }

}
