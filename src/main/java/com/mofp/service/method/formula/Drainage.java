package com.mofp.service.method.formula;

/**
 * @author rivasyafri
 */
public class Drainage {

    /**
     * Calculate the volume handled by drainage
     * @param discharge maximum discharge of water by time
     * @param time time interval (must be the same type with discharge)
     * @param side side of drainage (must be the same type with discharge)
     * @return discharge * time * area;
     */
    public static double calculate(double discharge, double time, double side) {
        return discharge * time * Drainage.calculateArea(side);
    }

    /**
     * Calculate area of drainage, asuming that the area is square
     * @param side side of drainage
     * @return area of drainage
     */
    public static double calculateArea(double side) { return side * side; }
}
