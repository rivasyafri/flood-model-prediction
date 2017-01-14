package com.mofp.flood.service.support;

/**
 * @author rivasyafri
 */
public abstract class AbstractFloodModel {

    /**
     * General calculation (without evapotranspiration) to calculate run off
     * @param precipitation
     * @param saving
     * @return runoff = precipitation - saving
     */
    public double calculateRunOff(double precipitation, double saving) {
        return calculateRunOff(precipitation, saving, 0);
    }

    /**
     * General calculation to calculate run off
     * @param precipitation
     * @param saving
     * @param evapotranspiration
     * @return runoff = precipitation - saving - evapotranspiration
     */
    public double calculateRunOff(double precipitation, double saving, double evapotranspiration) {
        return precipitation - saving - evapotranspiration;
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
