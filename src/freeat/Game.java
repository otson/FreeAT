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

import freeat.ai.AI;
import freeat.ai.TestAI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 *
 * @author otso
 */
public class Game
{

    private static final int STAR_OF_AFRICA_COUNT = 1;
    private static final int RUBY_COUNT = 2;
    private static final int EMERALD_COUNT = 3;
    private static final int TOPAZ_COUNT = 4;
    private static final int ROBBER_COUNT = 3;
    private static final int HORSESHOE_COUNT = 5;

    public static HashMap<Integer, Node> locations;
    private ArrayList<Player> players;
    private boolean running = true;
    private int[] winCount = new int[PublicInformation.PLAYER_COUNT];
    private int[] avgTurnsToWin = new int[PublicInformation.PLAYER_COUNT];
    private long[] totalTurnsToWin = new long[PublicInformation.PLAYER_COUNT];
    private int turnCount = 0;
    private int totalWins = 0;
    private int totalDraws = 0;
    private boolean calculateActTime = false;

    public Game()
    {

        locations = new HashMap<>();
        players = new ArrayList<>();
        getLocations();
        setNodeTypes();
        getConnections();
        getPlaneConnections();
        setTreasures();
        setNodeNames();
        setPlayers();
        setAllRoutes();

        // print all possible routes from all nodes
//        int totalRoutes = 0;
//        for (Node node : locations.values())
//        {
//            //System.out.println("---Going through all routes for node: "+node.ID+" ---");
//            for (ArrayList<Route> array : node.getNonPlaneRoutes().values())
//            {
//                totalRoutes+=array.size();
//                //System.out.println("---Going through one array---");
//                for(Route route : array){
//                    //System.out.println("Route Start: "+node.ID+" "+route.getString());
//                }
//            }
//        }
//        System.out.println("Total routes: "+totalRoutes);
//        System.exit(0);
    }

    public final void setAllRoutes()
    {
        int distance = 1;
        int currentPrice = 0;
        for (Node node : locations.values())
        {

            getNext(node, null, distance, currentPrice, node.getNonPlaneRoutes());
        }
        //plane routes, free sea routes
        for (Node node : locations.values())
        {
            node.setPlaneRoutes();
            node.setFreeSeaRoutes();
        }
    }

    private void getNext(Node previous, Node previousPrevious, int distance, int currentPrice, HashMap<Integer, ArrayList<Route>> routes)
    {
        for (int i = 0; i < previous.getConnections().size(); i++)
        {
            int tempPrice = currentPrice;
            Integer integer = previous.getConnections().get(i);
            Node current = locations.get(integer);
            if (current != previousPrevious)
            {
                if (current.isCity())
                {
                    for (int j = distance; j <= Globals.MAX_DICE_VALUE; j++)
                    {
                        for (int x = tempPrice; x <= Globals.MAX_SEA_MOVEMENT_COST; x++)
                        {
                            //list[j][x].add(new Route(current, tempPrice * 100));
                            if (routes.get(new Key(j, x).hashCode()) == null)
                            {
                                routes.put(new Key(j, x).hashCode(), new ArrayList<>());
                            }
                            routes.get(new Key(j, x).hashCode()).add(new Route(locations.get(current.ID), tempPrice));
                        }
                    }
                } else
                {
                    if (current.isSea() && !previous.isSea())
                    {
                        tempPrice++;
                    }
                    for (int x = tempPrice; x <= Globals.MAX_SEA_MOVEMENT_COST; x++)
                    {
                        //list[distance][x].add(new Route(current, tempPrice * 100));
                        if (routes.get(new Key(distance, x).hashCode()) == null)
                        {
                            routes.put(new Key(distance, x).hashCode(), new ArrayList<>());
                        }

                        routes.get(new Key(distance, x).hashCode()).add(new Route(locations.get(current.ID), tempPrice));
                    }
                }
                if (distance < Globals.MAX_DICE_VALUE)
                {
                    getNext(current, previous, distance + 1, tempPrice, routes);
                }
            }

        }
    }

    private static void getLocations()
    {
        BufferedReader br = null;
        try
        {
            String filepath = Main.class.getResource("/res/coordinates/"+Main.LOCATIONS_FILE).getPath();
            br = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("res/coordinates/"+Main.LOCATIONS_FILE)));
            //br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();
            while (line != null)
            {
                String[] values = line.split(" ");
                int[] iValues = new int[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    iValues[i] = Integer.parseInt(values[i].trim());
                }
                // add new node to array with the id, x and y coordinates
                locations.put(iValues[0], new Node(iValues[0], iValues[1], iValues[2], locations));
                line = br.readLine();
            }
        } catch (FileNotFoundException ex)
        {
            System.out.println("file not found exception");
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            System.out.println("io exception");
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                br.close();
            } catch (IOException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static void setNodeNames()
    {
        BufferedReader br = null;
        try
        {
            String filepath = Main.class.getResource("/res/coordinates/"+Main.NODE_NAMES).getPath();
            br = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("res/coordinates/"+Main.NODE_NAMES)));
            //br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();
            while (line != null)
            {
                String[] values = line.split(" ");
                if (values.length == 2)
                {
                    locations.get(Integer.parseInt(values[0].trim())).setName(values[1].replace("_", " "));
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                br.close();
            } catch (IOException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private static void setNodeTypes()
    {
        BufferedReader br = null;
        try
        {
            String filepath = Main.class.getResource("/res/coordinates/"+Main.NODE_TYPES).getPath();
            br = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("res/coordinates/"+Main.NODE_TYPES)));
            //br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();
            while (line != null)
            {
                String[] values = line.split(" ");
                if (values.length == 2)
                {
                    locations.get(Integer.parseInt(values[0].trim())).setType(values[1]);
                    //System.out.println("Setting node "+locations.get(Integer.parseInt(values[0].trim())).ID+" to type: "+values[1]);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                br.close();
            } catch (IOException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void getConnections()
    {
        BufferedReader br = null;
        try
        {
            String filepath = Main.class.getResource("/res/coordinates/"+Main.CONNECTIONS_FILE).getPath();
            br = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("res/coordinates/"+Main.CONNECTIONS_FILE)));
            //br = new BufferedReader(new FileReader(filepath)); // at 120
            String line = br.readLine();
            while (line != null)
            {
                String[] values = line.split(" ");
                int[] iValues = new int[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    iValues[i] = Integer.parseInt(values[i].trim());
                }
                // add new node to array with the id, x and y coordinates
                Node temp = locations.get(iValues[0]);
                for (int i = 1; i < values.length; i++)
                {
                    temp.addConnection(iValues[i]);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                br.close();
            } catch (IOException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (Node node : locations.values())
        {
            if (node.getConnections().contains(node.ID))
            {
                System.out.println("Node " + node.ID + " contains route to itself.");
            }
        }
        //System.exit(0);
    }

    private void getPlaneConnections()
    {
        BufferedReader br = null;
        try
        {
            String filepath = Main.class.getResource("/res/coordinates/"+Main.PLANE_CONNECTIONS_FILE).getPath();
            br = new BufferedReader(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("res/coordinates/"+Main.PLANE_CONNECTIONS_FILE)));
            //br = new BufferedReader(new FileReader(filepath)); // at 120
            String line = br.readLine();
            while (line != null)
            {
                String[] values = line.split(" ");
                int[] iValues = new int[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    iValues[i] = Integer.parseInt(values[i].trim());
                }
                // add new node to array with the id, x and y coordinates
                Node temp = locations.get(iValues[0]);
                for (int i = 1; i < values.length; i++)
                {
                    temp.addPlaneConnection(iValues[i]);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                br.close();
            } catch (IOException ex)
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void renderTreasures()
    {
        for (Node node : locations.values())
        {
            node.draw();
        }
    }

    public void renderPlayers()
    {
        for (Player player : players)
        {
            int x;
            int y;
            int size = 25;
            Node temp = locations.get(player.getLocation());
            x = temp.x;
            y = temp.y;
            glDisable(GL_TEXTURE_2D);
            glBegin(GL_TRIANGLES);
            glColor3f(player.getR(), player.getG(), player.getB());
            glVertex2f(x - size / 2f, y + size / 2f);
            glVertex2f(x + size / 2f, y + size / 2f);
            glVertex2f(x, y - size / 2f);
            glEnd();
            glColor3f(0, 0, 0);
            glBegin(GL_LINES);
            glVertex2f(x - size / 2f, y + size / 2f);
            glVertex2f(x + size / 2f, y + size / 2f);

            glVertex2f(x + size / 2f, y + size / 2f);
            glVertex2f(x, y - size / 2f);

            glVertex2f(x, y - size / 2f);
            glVertex2f(x - size / 2f, y + size / 2f);
            glEnd();
            glEnable(GL_TEXTURE_2D);
        }
    }

    private void setTreasures()
    {
        int totalTreasures = Node.CITY_COUNT;
        int emeralds = (int) (totalTreasures * Globals.EMERALD_PERCENTAGE + 0.5f);
        int horseShoes = (int) (totalTreasures * Globals.HORSESHOE_PERCENTAGE + 0.5f);
        int topazes = (int) (totalTreasures * Globals.TOPAZ_PERCENTAGE + 0.5f);
        int rubies = (int) (totalTreasures * Globals.RUBY_PERCENTAGE + 0.5f);
        int robbers = (int) (totalTreasures * Globals.ROBBER_PERCENTAGE + 0.5f);

        PublicInformation.setEmeraldTotal(emeralds);
        PublicInformation.setHorseShoesTotal(horseShoes);
        PublicInformation.setRobberTotal(robbers);
        PublicInformation.setRubyTotal(rubies);
        PublicInformation.setTopazTotal(topazes);
        PublicInformation.setUnOpenedLeft(Node.CITY_COUNT);
        PublicInformation.setEmptyTotal(Node.CITY_COUNT - emeralds - horseShoes - topazes - rubies - robbers - Globals.STAR_OF_AFRICA_COUNT);

        PublicInformation.setTreasureTotal(Node.CITY_COUNT);

        int[] treasures = new int[Node.CITY_COUNT];
        int current = 0;
        for (int i = 0; i < Globals.STAR_OF_AFRICA_COUNT; i++)
        {
            treasures[current] = TreasureType.STAR_OF_AFRICA;
            current++;
        }
        for (int i = 0; i < rubies; i++)
        {
            treasures[current] = TreasureType.RUBY;
            current++;
        }
        for (int i = 0; i < emeralds; i++)
        {
            treasures[current] = TreasureType.EMERALD;
            current++;
        }
        for (int i = 0; i < topazes; i++)
        {
            treasures[current] = TreasureType.TOPAZ;
            current++;
        }
        for (int i = 0; i < robbers; i++)
        {
            treasures[current] = TreasureType.ROBBER;
            current++;
        }
        for (int i = 0; i < horseShoes; i++)
        {
            treasures[current] = TreasureType.HORSESHOE;
            current++;
        }

        treasures = ShuffleArray(treasures);
        ArrayList<Node> valuesList = new ArrayList<>(locations.values());
        for (int i = 0; i < valuesList.size(); i++)
        {
            if (!valuesList.get(i).isTreasureCity())
            {
                valuesList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < treasures.length; i++)
        {
            Node temp = valuesList.get(i);
            temp.setTreasure(treasures[i]);
        }
    }

    private int[] ShuffleArray(int[] array)
    {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }

    private void setPlayers()
    {
        Player.resetID();
        for (int i = 0; i < PublicInformation.PLAYER_COUNT; i++)
        {
            players.add(new Player(locations));
        }
        PublicInformation.updateInformation(players);
        Collections.shuffle(players);
    }

    public void processTurn()
    {
        if (!PublicInformation.isWinner())
        {
            players.stream().forEach((player) ->
            {
                if (calculateActTime)
                {
                    long start = System.nanoTime();
                    //player.resetCash();
                    player.act();
                    System.out.println("Act time for " + player.getName() + ": " + (System.nanoTime() - start) / 1000 + " microseconds.");
                } else
                {
                    player.act();
                }
            });
        }
        turnCount++;
        PublicInformation.updateInformation(players);
        if (running)
        {
            if (PublicInformation.isWinner())
            {
                winCount[PublicInformation.getWinner()]++;
                totalTurnsToWin[PublicInformation.getWinner()] += turnCount;
                totalWins++;
                if (winCount[PublicInformation.getWinner()] % 1 == 0)
                {
                    for (int i = 0; i < PublicInformation.PLAYER_COUNT; i++)
                    {
                        System.out.print("Player " + i + " " + PublicInformation.getName(i) + " wins: " + winCount[i] + " (" + (float) winCount[i] * 100 / totalWins + "%)");
                        if (winCount[i] > 0)
                        {
                            System.out.print(" avg turns to win: " + (float) totalTurnsToWin[i] / winCount[i] + ". \t");
                        }
                    }
                    System.out.print("Draws: " + totalDraws + " (" + (float) totalDraws * 100 / totalWins + "%) ");
                    System.out.println("");
                }

                resetGame();
            } else if (turnCount > 250)
            {
                totalDraws++;
                resetGame();
            }

        }
    }

    public void resetGame()
    {
        //locations = new HashMap<>();
        PublicInformation.reset();
        AI.AIIdentifications.clear();
        players = new ArrayList<>();
        turnCount = 0;
        setPlayers();
        setTreasures();
        TestAI.count = 1;
        if (Globals.SHUFFLE_ROUTE_ARRAYS)
        {
            shuffleRouteOrder();
        }

        running = true;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    void renderAINodes()
    {
        for (Player player : players)
        {
            player.draw();
        }
    }

    private void shuffleRouteOrder()
    {
        for (Node node : locations.values())
        {
            node.shuffleArrays();
        }
    }

}
