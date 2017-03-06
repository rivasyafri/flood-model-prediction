package com.mofp.service.method.support;

import com.mofp.model.Cell;
import com.mofp.model.Variable;
import com.mofp.model.data.Weather;
import com.mofp.service.method.formula.Drainage;
import com.mofp.service.method.formula.Evapotranspiration;
import com.mofp.service.method.formula.WaterBalance;
import com.mofp.util.UnitTemperature;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 */
public abstract class FloodModel {

    public double calculate(Variable variable, AtomicReference<Cell> cellReference, List<Weather> weathers,
                            long timeElapsed, int timeStep) {
        Cell cell = cellReference.get();
        long time = getDeltaTime(cell, timeElapsed, timeStep);
        double precipitation = getPrecipitationMinusEvapotranspiration(weathers, cell.getHeight(), variable, timeElapsed);
        double runOff = calculateRunOff(variable, cell, precipitation, time);
        double waterBalance = WaterBalance.calculate(cell.getWaterBalanceBefore(), precipitation, runOff, timeElapsed);
        cell.setWaterBalanceAfter(waterBalance);
        cellReference.set(cell);
        return runOff;
    }

    /**
     * Global calculation to calculate run off
     * @param precipitation
     * @param variable saving the variable for evapotranspiration and drainage
     * @return runoff = precipitation - saving - evapotranspiration
     */
    protected double calculateRunOff(Variable variable, Cell cell, double precipitation, long time) {
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
        return calculateInfiltration(cell, time) + calculateDrainage(variable, time);
    }

    protected double calculateDrainage(Variable variable, long time) {
        if (variable.isUsingDrainage()) {
            if (!variable.isDrainageByData()) {
                return variable.getDrainageValue();
            } else {
                return Drainage.calculate(variable.getDischarge(), time, variable.getSide());
            }
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

    private long getDeltaTime(@NonNull Cell cell, long timeElapsed, int timeStep) throws NullPointerException {
        long time = 0;
        if (cell.getTimeStartFlooded() != 0) {
            time = timeElapsed - cell.getTimeStartFlooded();
        }
        time += timeStep;
        return time/60;
    }

    private double getPrecipitationMinusEvapotranspiration(List<Weather> weathers, double height, Variable variable, long startTime) {
        double evapotranspiration = 0;
        double rain = 0;
        boolean usingEvapotranspiration = variable.isUsingEvapotranspiration();
        boolean evapotranspirationByVariableFromUser = variable.isEvapotranspirationByData();
        if (weathers != null) {
            for (Weather weather: weathers) {
                Double rainDouble = weather.getRain();
                if (rainDouble != null) {
                    long startTimeData = weather.getStartTime();
                    long endTimeData = weather.getEndTime();
                    long intervalData = endTimeData - startTimeData;
                    long intervalNeeded = intervalData - (startTime - startTimeData);
                    rain += rainDouble * (intervalNeeded / intervalData);
                }
                if (usingEvapotranspiration && !evapotranspirationByVariableFromUser) {
                    Double radiation = weather.getRadiation();
                    Double geothermal = weather.getGeothermal();
                    Double maxTemperature = weather.getMaxTemperature();
                    Double minTemperature = weather.getMinTemperature();
                    Double humidity = weather.getRelativeHumidity();
                    Double windSpeed = weather.getWindSpeed();
                    Double waterVapor = weather.getWaterVapor();
                    if (radiation != null && geothermal != null && maxTemperature != null && minTemperature != null &&
                            humidity != null && windSpeed != null && waterVapor!= null ) {
                        long startTimeData = weather.getStartTime();
                        long endTimeData = weather.getEndTime();
                        long intervalData = endTimeData - startTimeData;
                        double hour = intervalData / (3600);
                        double cn = 37 * hour; // still hardcode, use short plant 3 is hour
                        double cd = 0.24 * hour; // still hardcode, use short plant. 3 is hour
                        double calculationResult = Evapotranspiration.calculateWithRhMean(radiation, geothermal, cn, cd, windSpeed,
                                minTemperature, maxTemperature, UnitTemperature.CELCIUS, height, humidity);
                        long intervalNeeded = intervalData - (startTime - startTimeData);
                        evapotranspiration += calculationResult * (intervalNeeded / intervalData);
                    }
                }
            }
        }
        return rain - evapotranspiration;
    }

}
