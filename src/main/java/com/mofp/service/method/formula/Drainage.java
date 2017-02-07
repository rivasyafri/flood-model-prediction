package com.mofp.service.method.formula;

/**
 * @author rivasyafri
 */
public class Drainage {

    /**
     * Calculate the volume handled by drainage
     * @param discharge maximum discharge of water by time
     * @param time time interval (must be the same type with discharge)
     * @param area area of drainage (must be the same type with discharge)
     * @return discharge * time * area;
     */
    public static double calculate(double discharge, double time, double area) {
        return discharge * time * area;
    }
}
