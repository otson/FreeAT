/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Route;
import freeat.ai.AI;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author otso
 */
public class Test2AI extends AI
{

    public Test2AI()
    {
        super(1);
    }

    @Override
    public void act()
    {
        while (!c.isEndTurn())
        {
            if (c.getCurrentNode().hasTreasure())
            {
                c.decideTryToken();
            } else
            {
                c.decideToUseLandOrSeaRoute();
                ArrayList<Route> routes = (ArrayList<Route>) c.getMyAvailableRoutes_NOT_YET_IMPLEMENTED().clone();
                Collections.shuffle(routes);
                boolean moved = false;
                for (Route route : routes)
                {
                    c.moveTo(route);
                    break;
        
                }
                c.endTurn();
            }

        }
    }

    @Override
    public float getR()
    {
        return 1;
    }

    @Override
    public float getG()
    {
        return 0f;
    }

    @Override
    public float getB()
    {
        return 0f;
    }

    @Override
    public String getName()
    {
        return "Reitti2Test";
    }

}
