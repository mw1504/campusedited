package com.sidmeier.campuschaos;

import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class Map {

    HashMap<Pair<Integer,Integer>,Sector> sectorLocations;
    Pair<Integer, Integer> pvcLocation;

    public Map() {
        this.sectorLocations = new HashMap<Pair<Integer, Integer>, Sector>();
        this.pvcLocation = new Pair<Integer, Integer>(null, null);

        Sector ronCookeHub = new Sector("Ron Cooke Hub");
        Sector hesHall = new Sector("Heslington Hall");
        Sector centralHall = new Sector("Central Hall");
        Pair<Integer, Integer> ronCookeHubCoord = new Pair<Integer, Integer>(19,8);
        Pair<Integer, Integer> hesHallCoord = new Pair<Integer, Integer>(9,7);
        Pair<Integer, Integer> centralHallCoord = new Pair<Integer, Integer>(5,8);
        this.addSector(ronCookeHubCoord, ronCookeHub);
        this.addSector(hesHallCoord, hesHall);
        this.addSector(centralHallCoord, centralHall);
    }

/*    File sectorsFile = new File("core/assets/sectors.txt");
    String name;
    int x, y;

	    try {
        Scanner inputStream = new Scanner(sectorsFile);
        inputStream.useDelimiter(", ");

        while(inputStream.hasNext()) {
            name = inputStream.next();
            x = Integer.parseInt(inputStream.next());
            y = Integer.parseInt(inputStream.next());


        }
        inputStream.close();
    }
        catch (FileNotFoundException e) {
        e.printStackTrace();
    }*/

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
