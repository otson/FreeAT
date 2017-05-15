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
                ArrayList<Route> routes = (ArrayList<Route>) c.getMyAvailableRoutes().clone();
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
