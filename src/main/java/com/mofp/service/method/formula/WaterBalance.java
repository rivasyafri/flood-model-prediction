package com.mofp.service.method.formula;

/**
 * @author rivasyafri
 */
public class WaterBalance {

    /**
     * Calculate final water balance
     * @param w1min initial water balance
     * @param p precipitation on certain time
     * @param qd runoff
     * @param deltaTime interval
     * @return final water balance
     */
    public static double calculate(double w1min, double p, double qd, double deltaTime) {
        return calculate(w1min, p, qd, 0, 0, deltaTime);
    }

    /**
     * Calculate final water balance
     * @param w1min initial water balance
     * @param p precipitation on certain time
     * @param qd runoff
     * @param q12 runoff from first layer to second layer
     * @param e1 evapotranspiration in layer 1
     * @param deltaTime interval
     * @return final water balance
     */
    public static double calculate(double w1min, double p, double qd, double q12, double e1, double deltaTime) {
        return w1min + (p - qd - q12 - e1) * deltaTime;
    }
}
