package com.sidmeier.campuschaos;

public class Sector {

    String name;
    // TODO Implement player class
    String affiliation;
    int amountOfUnits;
    // TODO Implement bonus system

    public Sector(String name) {
        this.name = name;
        this.affiliation = null;
        this.amountOfUnits = 0;
    }

    public String getName() {
        return this.name;
    }

    // TODO Change to use player class
    public String getAffiliation() {
        return this.affiliation;
    }

    public int getAmountOfUnits() {
        return this.amountOfUnits;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setAmountOfUnits(int amountOfUnits) {
        this.amountOfUnits = amountOfUnits;
    }

    public void addUnit() {
        this.amountOfUnits++;
    }
}
