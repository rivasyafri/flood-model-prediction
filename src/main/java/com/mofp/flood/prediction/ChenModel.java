package com.mofp.flood.prediction;

import com.mofp.flood.prediction.support.AbstractFloodModel;

/**
 * @author rivasyafri
 */
public class ChenModel extends AbstractFloodModel {

    /**
     * Calculate infiltration based on Chen's method by reducing it with water proof percentage of the area
     * @param infiltration maximum infiltration can occurs
     * @param waterProofPercentage persentage of which is calculated by NDVI
     * @return infiltration * omega
     */
    public double calculateInfiltration(double infiltration, double waterProofPercentage) {
        double omega = 1 - waterProofPercentage;
        return infiltration * omega;
    }

    @Override
    public String toString() {
        return "Chen Model";
    }
}
