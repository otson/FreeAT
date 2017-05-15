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
import freeat.Route;
import freeat.ai.normalaiNode.DistanceListList;
import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class RouteAI extends AI
{

    static int count = (int) (Math.random() * 2 + 1);
    private static DistanceListList distances;
    private static boolean distancesSet = false;

    public RouteAI()
    {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("RouteAI");
        count = (int) (Math.random() * 2 + 1);
        //count++;
    }

    @Override
    public void act()
    {
        while (!c.isEndTurn())
        {
            if (!distancesSet)
            {
                distancesSet = true;
                long start = System.nanoTime();
                initDistances(c);
                System.out.println("Time to calculate all distances: " + (System.nanoTime() - start) / 1000000 + " ms.");

                distances.checkRoutes();
                //System.exit(0);
            }

            if (c.isEligibleForWin())
            {
                // go towards a starting town
                int tangierDist = distances.getDistance(c.getCurrentNode().ID, 1);
                int cairoDist = distances.getDistance(c.getCurrentNode().ID, 2);
                int target;
                if (tangierDist > cairoDist)
                {
                    target = 1;
                } else
                {
                    target = 2;
                }

                c.decideToUseLandOrSeaRoute();
                
                
                
                ArrayList<Route> possibleRoutes = c.getMyAvailableFreeRoutes();
                Route selectedRoute = possibleRoutes.get(0);
                int selectedRouteDist = distances.getDistance(selectedRoute.getDestination().ID, target);
                for (Route tempRoute : possibleRoutes)
                {
                    int tempDist = distances.getDistance(tempRoute.getDestination().ID, target);
                    if (tempDist < selectedRouteDist)
                    {
                        selectedRoute = tempRoute;
                        selectedRouteDist = tempDist;
                    }
                }
                c.moveTo(selectedRoute);
                c.endTurn();
            } // check if can try to win treasure
            else if (c.getCurrentNode().hasTreasure())
            {
                c.decideTryToken();
            } else
            {
                c.decideToUseLandOrSeaRoute();
                // go towards nearest treasure

                // if there are remaining treasures
                if (!c.getRemainingTreasures().isEmpty())
                {
                    ArrayList<Node> treasureCities = c.getRemainingTreasures();
                    Node selectedNode = treasureCities.get(0);
                    int selectedNodeDist = distances.getDistance(c.getCurrentNode().ID, selectedNode.ID);

                    if (selectedNodeDist > c.getDice())
                    {
                        // find the nearest treasure
                        for (Node tempNode : treasureCities)
                        {
                            int tempDist = distances.getDistance(c.getCurrentNode().ID, tempNode.ID);
                            if (tempDist < selectedNodeDist)
                            {
                                selectedNode = tempNode;
                                selectedNodeDist = tempDist;
                                if (selectedNodeDist == c.getDice())
                                {
                                    break;
                                }
                            }
                        }
                    }

                    // find the best route
                    ArrayList<Route> possibleRoutes = c.getMyAvailableFreeRoutes();
                    Route selectedRoute = possibleRoutes.get(0);
                    if (containsNode(possibleRoutes, selectedNode))
                    {
                        selectedRoute = getRoute(possibleRoutes, selectedNode);
                    } else
                    {
                        int selectedRouteDist = distances.getDistance(selectedRoute.getDestination().ID, selectedNode.ID);
                        for (Route tempRoute : possibleRoutes)
                        {
                            int tempDist = distances.getDistance(tempRoute.getDestination().ID, selectedNode.ID);
                            if (tempDist < selectedRouteDist)
                            {
                                selectedRoute = tempRoute;
                                selectedRouteDist = tempDist;
                            }

                        }
                    }
                    //System.out.println("From: "+c.getCurrentNodeName()+ " to "+selectedNode.getName()+" distance: "+selectedNodeDist);
                    c.moveTo(selectedRoute);
                    if (c.getMyBalance() >= 100 && c.getCurrentNode().hasTreasure())
                    {
                        c.buyToken();
                    } else
                    {
                        c.endTurn();
                    }

                } // wander aimlessly
                else
                {
                    int target = c.getMyAvailableRoutes().size();
                    target = (int) (target * Math.random());
                    if (!c.isEndTurn())
                    {
                        c.moveTo(c.getMyAvailableRoutes().get(target));
                    }
                    c.endTurn();
                }
            }
        }
    }

    private void initDistances(Controller c)
    {
        distances = new DistanceListList(c.getNodeList());

    }

    @Override
    public float getR()
    {
        return 1;
    }

    @Override
    public float getG()
    {
        return 1f;
    }

    @Override
    public float getB()
    {
        return 0f;
    }

    @Override
    public String getName()
    {
        return "ReittiOtso";
    }

    private boolean containsNode(ArrayList<Route> possibleRoutes, Node selectedNode)
    {
        for (Route route : possibleRoutes)
        {
            if (route.getDestination() == selectedNode)
            {
                return true;
            }
        }
        return false;
    }

    private Route getRoute(ArrayList<Route> possibleRoutes, Node selectedNode)
    {
        for (Route route : possibleRoutes)
        {
            if (route.getDestination() == selectedNode)
            {
                return route;
            }
        }
        System.out.println("route did not contain given node");
        return null;
    }

}
