/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import freeat.ai.AI;
import freeat.ai.TestAI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private int turnCount = 0;
    private int totalWins = 0;
    private int totalDraws = 0;
    private boolean calculateActTime = false;

    public Game()
    {
        locations = new HashMap<>();
        players = new ArrayList<>();
        getLocations();
        getConnections();
        getPlaneConnections();
        setTreasures();
        setNodeNames();
        setPlayers();
        setAllRoutes();
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

    private void getLocations()
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(Main.LOCATIONS_FILE));
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

    private static void setNodeNames()
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(Main.NODE_NAMES));
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

    private void getConnections()
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(Main.CONNECTIONS_FILE)); // at 120
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
            br = new BufferedReader(new FileReader(Main.PLANE_CONNECTIONS_FILE)); // at 120
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
        PublicInformation.setEmeraldTotal(EMERALD_COUNT);
        PublicInformation.setHorseShoesTotal(HORSESHOE_COUNT);
        PublicInformation.setRobberTotal(ROBBER_COUNT);
        PublicInformation.setRubyTotal(RUBY_COUNT);
        PublicInformation.setTopazTotal(TOPAZ_COUNT);
        PublicInformation.setUnOpenedLeft(Node.CITY_COUNT);
        PublicInformation.setEmptyTotal(Node.CITY_COUNT - EMERALD_COUNT - HORSESHOE_COUNT - ROBBER_COUNT - RUBY_COUNT - TOPAZ_COUNT - STAR_OF_AFRICA_COUNT);

        PublicInformation.setTreasureTotal(Node.CITY_COUNT);

        int[] treasures = new int[Node.CITY_COUNT];
        int current = 0;
        for (int i = 0; i < STAR_OF_AFRICA_COUNT; i++)
        {
            treasures[current] = TreasureType.STAR_OF_AFRICA;
            current++;
        }
        for (int i = 0; i < RUBY_COUNT; i++)
        {
            treasures[current] = TreasureType.RUBY;
            current++;
        }
        for (int i = 0; i < EMERALD_COUNT; i++)
        {
            treasures[current] = TreasureType.EMERALD;
            current++;
        }
        for (int i = 0; i < TOPAZ_COUNT; i++)
        {
            treasures[current] = TreasureType.TOPAZ;
            current++;
        }
        for (int i = 0; i < ROBBER_COUNT; i++)
        {
            treasures[current] = TreasureType.ROBBER;
            current++;
        }
        for (int i = 0; i < HORSESHOE_COUNT; i++)
        {
            treasures[current] = TreasureType.HORSESHOE;
            current++;
        }

        treasures = ShuffleArray(treasures);
        for (int i = 0; i < treasures.length; i++)
        {
            Node temp = locations.get(i + 101);
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
                totalWins++;
                if (winCount[PublicInformation.getWinner()] % 1 == 0)
                {
                    for (int i = 0; i < PublicInformation.PLAYER_COUNT; i++)
                    {
                        System.out.print("Player " + i + " " + PublicInformation.getName(i) + " wins: " + winCount[i] + " (" + (float) winCount[i] * 100 / totalWins + "%) ");
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

        running = true;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    private void printRoutes()
    {

        for (Node node : locations.values())
        {
            for (int i = 1; i < 7; i++)
            {
                for (Route route : node.getAllLists()[i][0])
                {
                    System.out.println("Route from: " + node.ID + " to " + route.getDestination().ID + ". Price: " + route.getPrice() + " length: " + i);
                }
            }
        }
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
        for(Node node: locations.values()){
            node.shuffleArrays();
        }
    }

}
