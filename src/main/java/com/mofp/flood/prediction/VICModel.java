package com.mofp.flood.prediction;

import com.mofp.flood.prediction.support.AbstractFloodModel;

/**
 * @author rivasyafri
 */
public class VICModel extends AbstractFloodModel {

    public double getMaxWaterBalance(double bi, double im) {
        return im / (1 + bi);
    }

    public double calculateRunOff(double p, double bi, double w1min,
                                  double i0, double im, double deltaTime) {
        double w1c = getMaxWaterBalance(bi, im);
        if (i0 + p * deltaTime >= im) {
            return p * deltaTime - w1c + w1min;
        } else {
            return p * deltaTime - w1c + w1min + w1c * (1 - (i0 + p * deltaTime) / im);
        }
    }

    public static String getModelName() {
        return "VIC";
    }
}