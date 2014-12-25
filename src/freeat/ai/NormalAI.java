/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat.ai;

import freeat.Controller;
import freeat.Route;
import freeat.ai.normalaiNode.DistanceListList;
import java.util.ArrayList;

/**
 *
 * @author otso
 */
public class NormalAI extends AI
{

    static int count = (int) (Math.random() * 2 + 1);
    private static DistanceListList distances;
    private static boolean distancesSet = false;

    public NormalAI()
    {
        super(count); // set the preferred start city (1 or 2)
        AI.AIIdentifications.add("NormalAI");
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
                int startNode = 2;
                int target = 120;
                int distance = distances.getDistance(startNode, target);
                System.out.println("Distance from Tangier to Tunis: " + distance);
            }
            clearDraw();
            addDraw(0, 1, 1, 120);
            c.setDebugString(" I am at: " + c.getCurrentNodeName());
            if (c.getMyBalance() < 100 && c.getCurrentNode().hasTreasure())
            {
                c.decideTryToken();
            } else
            {
                if (c.getMyBalance() >= 400 && c.isAvailablePlanesFromCurrentNode() && (c.hasStar() || c.hasHorseShoeAfterStar()))
                {
                    c.decidetoUsePlane();
                    ArrayList<Route> routeList = c.getMyAvailableRoutes();
                    for (Route route : routeList)
                    {
                        //System.out.println("Current: " + c.getCurrentNode().ID + " route: " + route.getDestination().ID + " price: " + route.getPrice());
                        if (route.getDestination().isStartCity())
                        {
                            c.moveTo(route);
                            c.endTurn();
                        }
                    }
                    if (!c.isEndTurn())
                    {
                        for (Route route : routeList)
                        {
                            if (route.getDestination().hasTreasure())
                            {
                                c.moveTo(route);
                                c.buyToken();
                            }
                        }
                    }

                    if (!c.isEndTurn())
                    {
                        for (Route route : routeList)
                        {
                            c.moveTo(route);
                            c.endTurn();
                        }
                    }

                } else
                {
                    c.decideToUseLandOrSeaRoute();
                    ArrayList<Route> routeList = c.getMyAvailableRoutes();

                    for (Route route : routeList)
                    {
                        if (c.hasStar() && c.hasHorseShoeAfterStar())
                        {
                            if (route.getDestination().isStartCity() && !c.isEndTurn())
                            {
                                c.moveTo(route);
                                if (c.getMyBalance() >= 100)
                                {
                                    c.buyToken();
                                } else
                                {
                                    c.endTurn();
                                }
                            }
                        } else
                        {
                            if (route.getDestination().isStartCity() && !c.isEndTurn())
                            {
                                c.moveTo(route);
                                if (c.getMyBalance() >= 100)
                                {
                                    c.buyToken();
                                } else
                                {
                                    c.endTurn();
                                }
                            }
                        }
                    }

                    if (!c.isEndTurn())
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
        return 0.2f;
    }

    @Override
    public String getName()
    {
        return "Otso";
    }

    private void removeRoutesIfNoTreasure(ArrayList<Route> list)
    {
        for (Route route : list)
        {
            if (!route.getDestination().hasTreasure())
            {
                list.remove(route);
            }
        }
    }

    private void removeRouteIfCostEqualOrMore(ArrayList<Route> list, int cost)
    {
        for (Route route : list)
        {
            if (route.getPrice() >= cost)
            {
                list.remove(route);
            }
        }
    }

    private void removeRouteIfCostEqualOrLess(ArrayList<Route> list, int cost)
    {
        for (Route route : list)
        {
            if (route.getPrice() <= cost)
            {
                list.remove(route);

            }
        }
    }

    private void initDistances(Controller c)
    {
        distances = new DistanceListList(c.getNodeList());

    }
}
