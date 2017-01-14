package com.mofp.flood.service;

/**
 * @author rivasyafri
 */
public class HortonEquation {

    /**
     * Calculate the infiltration using Horton equation
     * @param fc constant infiltration capacity
     * @param f0 initial infiltration capacity
     * @param k k value
     * @param minute minute elapsed
     * @return fc + (f0 - fc) * Math.exp(-1 * k * minute);
     */
    public static double calculate(double fc, double f0, double k, double minute) {
        return fc + (f0 - fc) * Math.exp(-1 * k * minute);
    }
}
