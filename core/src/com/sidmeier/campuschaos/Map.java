package com.sidmeier.campuschaos;

import javafx.util.Pair;
import java.util.HashMap;

public class Map {

    HashMap<Sector,Pair<Integer,Integer>> sectorLocations;
    Pair<Integer, Integer> pvcLocation;

    public Map() {
        this.sectorLocations = null;
        this.pvcLocation = null;
    }

    public HashMap<Sector, Pair<Integer, Integer>> getSectorLocations() {
        return sectorLocations;
    }

    public Pair<Integer, Integer> getPvcLocation() {
        return pvcLocation;
    }

    public void setPvcLocation(Pair<Integer, Integer> pvcLocation) {
        this.pvcLocation = pvcLocation;
    }

    public void addSector(Sector sector, Pair<Integer, Integer> coord) {

    }
}
