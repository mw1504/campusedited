package com.sidmeier.campuschaos.utils;

import com.sidmeier.campuschaos.Map;
import com.sidmeier.campuschaos.Sector;
import javafx.util.Pair;

import java.util.HashMap;

public class UnitAllocation {

    private Map map;
    private String currentPlayer;

    public UnitAllocation(Map map, String currentPlayer){
        this.map = map;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Handles allocation of new gang members
     */
    // Will possibly take Player object as parameter
    private int getWeights(String player) {
        int weights = 0;
        HashMap<Pair<Integer, Integer>, Sector> sectorHashMap = map.getSectorLocations();
        for(Pair<Integer, Integer> coord : sectorHashMap.keySet()) {
            if(sectorHashMap.get(coord).getAffiliation() == currentPlayer) {
                weights += sectorHashMap.get(coord).getBonus();
            }
        }
        return weights;
    }


    private int getAllocation() {
        int weights = getWeights(currentPlayer);
        int bonus = 0; //Exists to allow for bonus feature later in development
        int troops = Constants.BASE_TROOPS + (weights * Constants.SECTOR_SCALAR) + bonus;
        return troops;
    }
}
