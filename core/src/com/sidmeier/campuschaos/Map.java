package com.sidmeier.campuschaos;

import javafx.util.Pair;
import java.util.HashMap;

public class Map {

    HashMap<Pair<Integer,Integer>,Sector> sectorLocations;
    Pair<Integer, Integer> pvcLocation;

    public Map() {
        this.sectorLocations = new HashMap<Pair<Integer, Integer>, Sector>();
        this.pvcLocation = new Pair<Integer, Integer>(null, null);
    }

    public HashMap<Pair<Integer, Integer>, Sector> getSectorMap() {
        return sectorLocations;
    }

    public Pair<Integer, Integer> getPvcLocation() {
        return pvcLocation;
    }

    public void setPvcLocation(Pair<Integer, Integer> pvcLocation) {
        this.pvcLocation = pvcLocation;
    }

    public void addSector(Pair<Integer, Integer> coord, Sector sector) {
        this.sectorLocations.put(coord, sector);
    }

    public Sector getSector(Pair<Integer, Integer> coord) {
        return this.sectorLocations.get(coord);
    }

    public boolean sectorAtCoord(Pair<Integer, Integer> coord) {
        if(this.sectorLocations.containsKey(coord)) {
            return true;
        }
        else {
            return false;
        }
    }
}
