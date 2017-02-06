package com.mofp.service.method;

import com.mofp.model.Cell;
import com.mofp.model.Variable;
import com.mofp.service.method.support.FloodModel;

/**
 * @author rivasyafri
 */
public class VICModel extends FloodModel {

    @Override
    public double calculateRunOff(Variable variable, Cell cell, double precipitation, long time) {
        double prep = precipitation * time;
        double saving = calculateSaving(variable, cell, time);
        double runOff = precipitation * time - saving - calculateEvapotranspiration(variable);
        if (cell.getConstantInfiltrationCapacity() + prep < cell.getInitialInfiltrationCapacity()) {
            double w1c = getMaxWaterBalance(cell.getPsiOrBi(), cell.getInitialInfiltrationCapacity());
            runOff += w1c * Math.pow((1 - (cell.getConstantInfiltrationCapacity() + prep) /
                    cell.getInitialInfiltrationCapacity()), 1 + cell.getPsiOrBi());
        }
        if (runOff > 0) {
            return runOff;
        }
        return 0;
    }

    @Override
    public double calculateInfiltration(Cell cell, long time) {
        double w1c = getMaxWaterBalance(cell.getPsiOrBi(), cell.getInitialInfiltrationCapacity());
        return w1c - cell.getWaterBalanceBefore();
    }

    public static String getModelName() {
        return "VIC";
    }

    private double getMaxWaterBalance(double bi, double im) {
        return im / (1 + bi);
    }
}
