// Paranormal node class for ParanormalAI.
package freeat.ai.paranormalai;

import java.util.ArrayList;

import freeat.Controller;

public class Landmass
{

    private int landmassID = 1;
    private final ArrayList<ParanormalNode> treasureCitiesArrayList; // all treasures cities of this landmass.
    private final ArrayList<ParanormalNode> metropolsArrayList;      // all metropols of this landmass.
    private final ArrayList<ParanormalNode> seaportsArrayList;       // all seaports of this landmass.
    private final ArrayList<ParanormalNode> airportsArrayList;       // all airports of this landmass.
    private final ArrayList<Integer> seaConnectionsArrayList;        // landmass ID's of other landmasses with direct sea routes from this landmass.
    private final ArrayList<Integer> airConnectionsArrayList;        // landmass ID's of other landmasses with direct flights from this landmass.
    private final int nTreasureCities;                               // total number of treasure cities on this landmass.
    private final int nMetropols;                                    // total number of metropols on this landmass.
    private final boolean isTreasureCityLandmass;                    // has this landmass any treasure cities?
    private final boolean isMetropolLandmass;                        // has this landmass any metropols?
    private boolean isContinent;                                     // is this a landmass with highest number of treasure cities of any landmass?
    private boolean isIsland;                                        // is this a landmass with less than highest number of treasure cities of any landmass?

    // constructors.
    public Landmass()
    {
        this.landmassID = landmassID++;
        this.treasureCitiesArrayList = new ArrayList<>();
        this.metropolsArrayList = new ArrayList<>();
        this.seaportsArrayList = new ArrayList<>();
        this.airportsArrayList = new ArrayList<>();
        this.seaConnectionsArrayList = new ArrayList<>();
        this.airConnectionsArrayList = new ArrayList<>();
        this.nTreasureCities = -1;
        this.nMetropols = -1;
        this.isTreasureCityLandmass = false;
        this.isMetropolLandmass = false;
        this.isContinent = false;
        this.isIsland = false;
    }

    // setters.
    public void setIsContinent(boolean isContinent)
    {
        this.isContinent = isContinent;
    }

    public void setIsIsland(boolean isIsland)
    {
        this.isIsland = isIsland;
    }

    // updaters.
    public void addCity(ParanormalNode pNode, Controller c)
    {
        this.treasureCitiesArrayList.add(pNode);
    }

    // getters.
    public int getLandmassID()
    {
        return this.landmassID;
    }

    public ArrayList<ParanormalNode> getTreasureCitiesArrayList()
    {
        return this.treasureCitiesArrayList;
    }

    public ArrayList<ParanormalNode> getMetropolsArrayList()
    {
        return this.metropolsArrayList;
    }

    public ArrayList<ParanormalNode> getSeaportsArrayList()
    {
        return this.seaportsArrayList;
    }

    public ArrayList<ParanormalNode> getAirportsArrayList()
    {
        return this.airportsArrayList;
    }

    public ArrayList<Integer> getSeaConnectionsArrayList()
    {
        return this.seaConnectionsArrayList;
    }

    public ArrayList<Integer> getAirConnectionsArrayList()
    {
        return this.airConnectionsArrayList;
    }

    public Integer getNTreasureCitise()
    {
        return this.nTreasureCities;
    }

    public Integer getNMetropols()
    {
        return this.nMetropols;
    }

    public boolean getIsTreasureCityLandmass()
    {
        return this.isTreasureCityLandmass;
    }

    public boolean getIsMetropolLandmass()
    {
        return this.isMetropolLandmass;
    }

    public boolean getIsContinent()
    {
        return this.isContinent;
    }

    public boolean getIsIsland()
    {
        return this.isIsland;
    }
}
