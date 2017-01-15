package com.mofp.ca.model;

import java.util.ArrayList;

/**
 * @author taufiqurrahman
 */
public class Neighborhood {

    // Neighborhood position
    int[] northwest = {-1, -1};
    int[] north = {0, -1};
    int[] northeast = {1, -1};
    int[] west = {-1, 0};
    int[] east = {1, 0};
    int[] southwest = {-1, 1};
    int[] south = {0, 1};
    int[] southeast = {1, 1};
    String currentlyUse = "";

    public String getCurrentlyUse() {
        return currentlyUse;
    }

    public int[] getNorthwest() {
        return northwest;
    }

    public int[] getNorth() {
        return north;
    }

    public int[] getNortheast() {
        return northeast;
    }

    public int[] getWest() {
        return west;
    }

    public int[] getEast() {
        return east;
    }

    public int[] getSouthwest() {
        return southwest;
    }

    public int[] getSouth() {
        return south;
    }

    public int[] getSoutheast() {
        return southeast;
    }

    ArrayList<int[][]> _moore = new ArrayList<>();
    ArrayList<int[][]> _neumann = new ArrayList<>();
    int[][] moore = {northwest, north, northeast, east, southeast, south, southwest, west};
    int[][] neumann = {north, west, east, south};

    public Neighborhood() {
        _moore.add(moore);
        _neumann.add(neumann);
    }

    public ArrayList<int[][]> neumann() {
        return _neumann;
    }

    public ArrayList<int[][]> moore() {
        return _moore;
    }

    public ArrayList<int[][]> use(String neighboorName) {
        if (neighboorName.equals("moore")) {
            currentlyUse = "moore";
            return moore();
        } else if (neighboorName.equals("neumann")) {
            currentlyUse = "neumann";
            return neumann();
        } else {
            return null;
        }
    }


}
