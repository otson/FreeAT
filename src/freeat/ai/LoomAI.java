/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Route;
import java.util.ArrayList;

/**
 *
 * @author antti
 */
public class LoomAI extends AI
{
    static int count = (int) (Math.random() * 2 + 1); // random starting city for 1st object of this class.

    @Override
    public String getName()
    {
        return "LoomAI";
    }

    @Override
    public float getR()
    {
        return 1.0f;
    }

    @Override
    public float getG()
    {
        return 0.0f;
    }

    @Override
    public float getB()
    {
        return 1.0f;
    }

    int destinationNodeID;
    int oldNodeID;
    int newNodeID;
    int askMeAboutLoomStatus;

    public LoomAI()
    {
        super(count); // call the constructor and set the random start city (1 or 2).
        AI.AIIdentifications.add("LoomAI");
        count = (int) (Math.random() * 2 + 1); // random starting city for next object of this class (must be done here).
    }

    private String loomArrayOfStrings[] = {
        "You mean the latest masterpiece of fantasy storytelling from Lucasfilm's Brian Moriarty?",
        "Why it's an extraordinary adventure with an interface on magic...",
        "stunning, high-resolution, 3D landscapes...",
        "sophisticated score and musical effects.",
        "Not to mention the detailed animation and special effects, elegant point'n' click control of characters, objects, and magic spells.",
        "Beat the rush! Go out and buy Loom today!" };

    @Override
    public void act()
    {
        while (!(c.isEndTurn()))
        {
            if (destinationNodeID > 0)
            {
                c.setDebugString("current destination is " + destinationNodeID);
                // System.in.read();

                // TODO: go to the destination node, buy tokens on the way (if it's probably useful).

                // when destination is reached, set destination to 0 (no destination).
                if (c.getCurrentNode().ID == destinationNodeID)
                {
                    destinationNodeID = 0;
                }
                c.endTurn();
            }
            else
            {
                c.setDebugString("no current destination");
                // System.in.read();

                c.decideToUseLandOrSeaRoute();

                ArrayList<Route> routesArrayList = c.getMyAvailableRoutes();
                c.setDebugString("number of routes: " + routesArrayList.size());
                // System.in.read();

                int iTarget = (int) ((int) routesArrayList.size() * Math.random());
                Route route = routesArrayList.get(iTarget);
                newNodeID = route.getDestination().ID;
                c.moveTo(route);
                if (c.getMyBalance() >= 100)
                {
                    c.buyToken();
                }
            }

            // print my itinerary this turn.

            printMyItineraryAndWait(oldNodeID, newNodeID);
            oldNodeID = newNodeID;

            if (askMeAboutLoomStatus == 0)
            {
                askMeAboutLoomStatus = (c.getMyID() + 1);
                if (askMeAboutLoomStatus > loomArrayOfStrings.length)
                {
                    askMeAboutLoomStatus = 1;
                }
            }
            askMeAboutLoom();
            c.endTurn();
        }
    }

    private void printMyItinerary(int oldNodeID, int newNodeID)
    {
        c.setDebugString("coming from " + oldNodeID + " going to " + newNodeID);
        // System.in.read();
    }

    private void printMyItineraryAndWait(int oldNodeID, int newNodeID)
    {
        printMyItinerary(oldNodeID, newNodeID);
        // System.in.read();
    }

    public void askMeAboutLoom()
    {
        c.setDebugString(loomArrayOfStrings[(askMeAboutLoomStatus-1)]);
        // myController.setDebugString(loomArrayOfStrings[1]);
        // System.in.read();
        if (++askMeAboutLoomStatus > loomArrayOfStrings.length)
        {
            askMeAboutLoomStatus = 0;
        }
    }
}
