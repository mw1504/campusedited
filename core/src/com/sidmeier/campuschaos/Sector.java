package com.sidmeier.campuschaos;

public class Sector {

    /**
     * The name of this sector.
     * This should be meaningful to what is contained within the sector.
     */
    private String name;
    /**
     * The name of the owning player's college.
     * This will be a single word being the college the player chose at the beginning of the game.
     */
    private String affiliation;
    /**
     * The amount of units occupying the sector.
     * This will change a lot depending on the actions of the user.
     * The amount with either increase or decrease by varying amounts.
     */
    private int amountOfUnits;
    /**
     * The bonus that the player gets for controlling this sector.
     * This will be a small bonus to give the player an advantage.
     * This value may change depending on how important the buildings the sector contains are.
     */
    private int bonus;

    /**
     * Sets up the starting values for the sector, which is usually nothing except for the bonus of the sector and the name.
     * @param name The name of this sector.
     */
    public Sector(String name) {
        this.name = name;
        this.affiliation = null;
        this.amountOfUnits = 0;
        this.bonus = 0;
    }

    /**
     * Gets the name assigned to the sector.
     * @return this sectors name.
     */
    public String getName() {
        return this.name;
    }

    // TODO Change to use player class

    /**
     * Gets the college of the owning player.
     * @return the college of the owning player.
     */
    public String getAffiliation() {
        return this.affiliation;
    }

    /**
     * Sets the college of the sector to the new owning player.
     * @param affiliation the players college.
     */
    public void setAffiliation(String affiliation) {
        if (affiliation != null) {
            this.affiliation = affiliation;
        }
    }

    /**
     * Gets the amount of units currently occupying the sector.
     * @return the amount of units on the sector.
     */
    public int getAmountOfUnits() {
        return this.amountOfUnits;
    }

    /**
     * Sets the amount of units on the sector to an new positive integer amount.
     * @param amountOfUnits This should be the amount of units on the sector.
     */
    public void setAmountOfUnits(int amountOfUnits) {
        if (amountOfUnits >= 0) {
            this.amountOfUnits = amountOfUnits;
        }
    }

    /**
     * Increments the amount of units on the sector.
     */
    public void addUnit() {
        this.amountOfUnits++;
    }

    /**
     * Gets the amount of bonus the sector gives to the player.
     * @return the bonus value of the sector.
     */
    public int getBonus() {
        return this.bonus;
    }
}
