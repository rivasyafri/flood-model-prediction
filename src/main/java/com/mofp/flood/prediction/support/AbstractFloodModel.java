package com.mofp.flood.prediction.support;

/**
 * @author rivasyafri
 */
public abstract class AbstractFloodModel {

    /**
     * Global calculation (without evapotranspiration) to calculate run off
     * @param precipitation
     * @param saving
     * @return runoff = precipitation - saving
     */
    public double calculateRunOff(double precipitation, double saving) {
        return calculateRunOff(precipitation, saving, 0);
    }

    /**
     * Global calculation to calculate run off
     * @param precipitation
     * @param saving
     * @param evapotranspiration
     * @return runoff = precipitation - saving - evapotranspiration
     */
    public double calculateRunOff(double precipitation, double saving, double evapotranspiration) {
        double runOff = precipitation - saving - evapotranspiration;
        if (runOff > 0) {
            return runOff;
        }
        return 0;
    }

    /**
     * Calculate the saving of water
     * @param infiltration volume that handled by infiltration
     * @param drainage volume that handled by drainage
     * @return infiltration + drainage;
     */
    public double calculateSaving(double infiltration, double drainage) {
        return infiltration + drainage;
    }
}
