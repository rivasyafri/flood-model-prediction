package com.mofp.service.method;

import com.mofp.model.Cell;
import com.mofp.service.method.formula.GreenAmptEquation;
import com.mofp.service.method.support.FloodModel;

/**
 * @author rivasyafri
 */
public class ChenModel extends FloodModel {

    /**
     * Calculate infiltration based on Chen's method by reducing it with water proof percentage of the area.
     * Will use Green Ampt Equation
     * @return infiltration * omega
     */
    @Override
    protected double calculateInfiltration(Cell cell, long time) {
        double infiltration = GreenAmptEquation.calculate(cell.getKValue(), cell.getPsiOrBi(),
                cell.getWaterBalanceBefore(), cell.getWaterBalanceAfter(), time);
        double omega = 1 - cell.getWaterProofPercentage();
        return infiltration * omega;
    }

    public static String getModelName() {
        return "Chen";
    }
}
