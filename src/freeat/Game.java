/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author otso
 */
public class Game {

    private static final int STAR_OF_AFRICA_COUNT = 1;
    private static final int RUBY_COUNT = 2;
    private static final int EMERALD_COUNT = 3;
    private static final int TOPAZ_COUNT = 4;
    private static final int ROBBER_COUNT = 3;
    private static final int HORSESHOE_COUNT = 5;

    private HashMap<Integer, Node> locations;

    public Game() {
        locations = new HashMap<>();
        getLocations();
        getConnections();
        setTreasures();
        for (Node node : locations.values()) {
            node.print();
        }
    }

    private void getLocations() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(Main.LOCATIONS_FILE));
            String line = br.readLine();
            while (line != null) {
                String[] values = line.split(" ");
                int[] iValues = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    iValues[i] = Integer.parseInt(values[i].trim());
                }
                // add new node to array with the id, x and y coordinates
                locations.put(iValues[0], new Node(iValues[0], iValues[1], iValues[2], locations));
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void getConnections() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(Main.CONNECTIONS_FILE)); // at 120
            String line = br.readLine();
            while (line != null) {
                String[] values = line.split(" ");
                int[] iValues = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    iValues[i] = Integer.parseInt(values[i].trim());
                }
                // add new node to array with the id, x and y coordinates
                Node temp = locations.get(iValues[0]);
                for (int i = 1; i < values.length; i++) {
                    temp.addConnection(iValues[i]);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void renderTreasures() {
        for (Node node : locations.values()) {
            node.draw();
        }
    }

    private void setTreasures() {

        int[] treasures = new int[Node.CITY_COUNT];
        int current = 0;
        for (int i = 0; i < STAR_OF_AFRICA_COUNT; i++) {
            treasures[current] = TreasureType.STAR_OF_AFRICA;
            current++;
        }
        for (int i = 0; i < RUBY_COUNT; i++) {
            treasures[current] = TreasureType.RUBY;
            current++;
        }
        for (int i = 0; i < EMERALD_COUNT; i++) {
            treasures[current] = TreasureType.EMERALD;
            current++;
        }
        for (int i = 0; i < TOPAZ_COUNT; i++) {
            treasures[current] = TreasureType.TOPAZ;
            current++;
        }
        for (int i = 0; i< ROBBER_COUNT; i++) {
            treasures[current] = TreasureType.ROBBER;
            current++;
        }
        for (int i = 0; i < HORSESHOE_COUNT; i++) {
            treasures[current] = TreasureType.HORSESHOE;
            current++;
        }
        
        treasures = ShuffleArray(treasures);
        for(int i = 0; i< treasures.length; i++){
            Node temp = locations.get(i+101);
            temp.setTreasure(treasures[i]);
        }
    }

    private int[] ShuffleArray(int[] array) {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }
}
