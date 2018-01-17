package com.sidmeier.campuschaos;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Map {

    private HashMap<Pair<Integer,Integer>,Sector> sectorLocations;
    private Pair<Integer, Integer> pvcLocation;

    public Map() {
        this.sectorLocations = new HashMap<Pair<Integer, Integer>, Sector>();
        this.pvcLocation = new Pair<Integer, Integer>(null, null);

        BufferedReader myReader = null;
        String file = "core/assets/Sectors.txt";
        try {
            this.readSectorsFromFile(myReader, file);
        } catch (IOException e) {
            if (myReader == null) {
                System.out.println("File: Sectors.txt not found");
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    private void readSectorsFromFile(BufferedReader myReader, String file) throws IOException {
        myReader = new BufferedReader(new FileReader(file));
        String lineRead;
        while ((lineRead = myReader.readLine()) != null) {
            String[] sectorParts = lineRead.split(", ");
            if (sectorParts.length > 3) {
                throw new IOException("File is corrupted.");
            }
            System.out.println(sectorParts[0] + ", " + sectorParts[1] + ", " + sectorParts[2]);
            this.addSector(new Pair<Integer,Integer>(Integer.parseInt(sectorParts[1]),Integer.parseInt(sectorParts[2])), new Sector(sectorParts[0]));
        }
    }

    public HashMap<Pair<Integer, Integer>, Sector> getSectorLocations() {
        return this.sectorLocations;
    }

    public Pair<Integer, Integer> getPvcLocation() {
        return this.pvcLocation;
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
