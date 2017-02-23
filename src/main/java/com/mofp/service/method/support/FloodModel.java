package com.mofp.service.method.support;

import com.mofp.model.Cell;
import com.mofp.model.Variable;
import com.mofp.model.data.Weather;
import com.mofp.service.method.formula.Evapotranspiration;
import com.mofp.util.UnitTemperature;
import lombok.NonNull;

import java.util.List;

/**
 * @author rivasyafri
 */
public abstract class FloodModel {

    /**
     * Global calculation to calculate run off
     * @param precipitation
     * @param variable saving the variable for evapotranspiration and drainage
     * @return runoff = precipitation - saving - evapotranspiration
     */
    public double calculateRunOff(Variable variable, Cell cell, double precipitation, long time) {
        double runOff = cell.getWaterHeight() +
                precipitation - calculateSaving(variable, cell, time);
        if (variable.isUsingEvapotranspiration() && variable.isEvapotranspirationByData()) {
            runOff -= calculateEvapotranspiration(variable);
        }
        if (runOff > 0) {
            return runOff;
        }
        return 0;
    }

    protected double calculateSaving(Variable variable, Cell cell, long time) {
        return calculateInfiltration(cell, time) + calculateDrainage(variable);
    }

    protected double calculateDrainage(Variable variable) {
        if (variable.isUsingDrainage()) {
            return variable.getDrainageValue();
        } else {
            return 0;
        }
    }

    protected double calculateEvapotranspiration(@NonNull Variable variable) {
        return Evapotranspiration.calculate(variable.getRadiation(),
                variable.getGeothermal(), variable.getCn(), variable.getCd(),
                variable.getWindSpeed(), variable.getDelta(), variable.getPsychometric(),
                variable.getMeanTemperature(), variable.getSaturatedWaterVapor(),
                variable.getWaterVapor());
    }

    protected abstract double calculateInfiltration(Cell cell, long time);
}
