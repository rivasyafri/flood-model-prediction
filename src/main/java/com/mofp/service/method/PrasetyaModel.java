package com.mofp.service.method;

import com.mofp.model.Cell;
import com.mofp.service.method.formula.HortonEquation;
import com.mofp.service.method.support.FloodModel;

/**
 * @author rivasyafri
 */
public class PrasetyaModel extends FloodModel {

    @Override
    public double calculateInfiltration(Cell cell, long time) {
        return HortonEquation.calculate(cell.getConstantInfiltrationCapacity(),
                cell.getInitialInfiltrationCapacity(), cell.getKValue(), time);
    }

    public static String getModelName() {
        return "Prasetya";
    }
}
