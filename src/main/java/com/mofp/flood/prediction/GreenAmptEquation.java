package com.mofp.flood.prediction;

/**
 * @author rivasyafri
 */
public class GreenAmptEquation {

    /**
     * Calculate infiltration using Green-Ampt function
     * @param k hydraulic constant
     * @param psi wetting front soil suction head
     * @param initialWaterContent
     * @param finalWaterContent
     * @param t time elapsed
     * @return k * ((psi * deltaWaterContent)/Ft + 1);
     */
    public static double calculate(double k, double psi,
                                    double initialWaterContent, double finalWaterContent,
                                    double t) {
        double deltaWaterContent = finalWaterContent - initialWaterContent;
        double Ft = Math.sqrt(2 * psi * deltaWaterContent * k * t);
        return k * ((psi * deltaWaterContent)/Ft + 1);
    }
}
