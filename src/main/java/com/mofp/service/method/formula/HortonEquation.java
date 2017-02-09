package com.mofp.service.method.formula;

/**
 * @author rivasyafri
 */
public class HortonEquation {

    /**
     * Calculate the infiltration using Horton equation
     * @param fc constant infiltration capacity
     * @param f0 initial infiltration capacity
     * @param k k value
     * @param minuteElapsed minuteElapsed elapsed
     * @return fc + (f0 - fc) * Math.exp(-1 * k * minuteElapsed);
     */
    public static double calculate(double fc, double f0, double k, double minuteElapsed) {
        return fc + (f0 - fc) * Math.exp(-1 * k * minuteElapsed);
    }
}
