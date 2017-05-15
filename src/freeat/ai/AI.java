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

import freeat.Controller;
import freeat.Node;
import freeat.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author otso
 */
public abstract class AI
{

    public final int START;
    protected HashMap<Integer, Node> locations;
    // public static ArrayList<String> AIIdentifications = new ArrayList();
    public static Set<String> AIIdentifications = new HashSet<>();
    private ArrayList<DrawNode> drawList = new ArrayList<>();
    protected Controller c;

    public AI(int start)
    {
        START = start;
    }

    public void setLocations(HashMap<Integer, Node> locations)
    {
        this.locations = locations;
    }

    public abstract void act();

    public float getR()
    {
        return 0;
    }

    public float getG()
    {
        return 1;
    }

    public float getB()
    {
        return 0;
    }

    public String getName()
    {
        return "default";
    }

    public ArrayList<DrawNode> getDrawList()
    {
        return drawList;
    }

    protected void addDraw(int r, int g, int b, int nodeID)
    {
        drawList.add(new DrawNode(r, g, b, nodeID));
    }

    protected void clearDraw()
    {
        drawList.clear();
    }

    public void setController(Controller controller)
    {
        this.c = controller;
    }

}
