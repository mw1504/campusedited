package com.sidmeier.campuschaos.utils;
import java.util.Random;
public class ConflictResolution {

    public static void main(String[] args){
        System.out.print("TEST");
        ConflictResolution myResolution = new ConflictResolution();
        myResolution.resolveConflict();
    }
    Random rand = new Random();
    // GET PARAMETERS
    // GET TROOP AMOUNT
    // GET DEFEND OR ATTACK
    // RANDOM
    private int defendingUnits = 10;
    private int attackingUnits = 23;
    private int sum;
    private int multiplier = 10;
    private boolean conflictfinished = false;

    public String resolveConflict() {
        System.out.println("TEST 2");
        while (conflictfinished == false) {
            sum = (defendingUnits * multiplier) + (attackingUnits * multiplier);
            System.out.println("Sum of Int: " +sum);
            int n = rand.nextInt(sum) + 1;
            System.out.println("Random Int: " +n);
            if (n <= (defendingUnits * multiplier)) {
                attackingUnits = attackingUnits - 1;
                System.out.println("New amount of attacking units: " +attackingUnits);
                if (attackingUnits == 0) {
                    System.out.println("attack");
                    return "attack";
                }
            } else {
                defendingUnits = defendingUnits - 1;
                System.out.println("New amount of defending units: " +defendingUnits);
                if (defendingUnits == 0) {
                    System.out.println("attack");
                    return "defend";
                }
            }
        }
    return null;
    }

}
