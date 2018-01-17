package com.sidmeier.campuschaos.utils;

public class UnitAllocation {

    /**
     * Handles allocation of new gang members
     */
    // Will possibly take Player object as parameter
    private int getWeights(String player) {
        int weights = 0;
        // TODO finish implementation of gang member allocation
        return 0;
    }

    String currentPlayer = null;

    private int getAllocation() {
        int weights = getWeights(currentPlayer);
        int bonus = 0; //Exists to allow for bonus feature later in development
        int troops = Constants.BASE_TROOPS + (weights * Constants.SECTOR_SCALAR) + bonus;
        return troops;
    }
}
