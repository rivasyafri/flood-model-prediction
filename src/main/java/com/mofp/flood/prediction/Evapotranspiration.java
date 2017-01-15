package com.mofp.flood.prediction;

import com.mofp.framework.util.UnitTemperature;

/**
 * @author rivasyafri
 */
public class Evapotranspiration {

    /**
     * Calculate evapotranspiration with full value
     * @return value of evapotranspiration happens in a day
     */
    public static double calculate(double rn, double g, double cn, double cd, double u2,
                                   double tMin, double tMax, UnitTemperature unit,
                                   double height,
                                   double rhMax, double rhMin) {
        double tMean = getMeanTemperature(tMax, tMin, unit);
        double delta = getSlopeOfSaturatedWaterVaporCurve(tMean);
        double y = getPsychometricConstant(height);
        double es0 = getMeanSaturationVaporPressure(tMin, tMax, unit);
        double ea = getActualVaporPressure(tMin, tMax, unit, rhMax, rhMin);
        return calculate(rn, g, cn, cd, u2, delta, y, tMean, es0, ea);
    }

    /**
     * Full function of estimating evapotranspiration by Penman-Monteith
     * @param delta delta of saturated water vapor in Pa/K
     * @param rn radiation in MJ/m2d
     * @param g geothermal in MJ/m2d
     * @param pa air density in kg/m3
     * @param Cp specific air heat in kJ/kg K
     * @param es0 average of saturated water vapor pressure calculated by
     *            average of e0 in max and min of daily temperature in kPa
     * @param ea average of water vapor pressure in kPa
     * @param rav all surface resistance in s/m
     * @param rs canopy resistance in s/m
     * @param psychometricConstant psiychometric constant in PaK
     * @return
     */
    public static double calculate(double rn, double g, double cn, double cd, double u2,
                                   double delta, double y, double tMean, double es0, double ea) {
        return (0.408 * delta * (rn - g) + y * (cn / (tMean + 273)) * u2 * (es0 - ea))/
                (delta + y * (1 + cd * u2));
    }

    /**
     * Calculate evapotranspiration with errors in rhMin
     * @return value of evapotranspiration happens in a day
     */
    public static double calculateWithErrorsInRh(double rn, double g, double cn, double cd, double u2,
                                                 double tMin, double tMax, UnitTemperature unit,
                                                 double height,
                                                 double rh) {
        double tMean = getMeanTemperature(tMax, tMin, unit);
        double delta = getSlopeOfSaturatedWaterVaporCurve(tMean);
        double y = getPsychometricConstant(height);
        double es0 = getMeanSaturationVaporPressure(tMin, tMax, unit);
        double ea = getActualVaporPressure(tMin, unit, rh);
        return calculate(rn, g, cn, cd, u2, delta, y, tMean, es0, ea);
    }

    /**
     * Calculate evapotranspiration with errors in rhMin
     * @return value of evapotranspiration happens in a day
     */
    public static double calculateWithRhMean(double rn, double g, double cn, double cd, double u2,
                                             double tMin, double tMax, UnitTemperature unit,
                                             double height,
                                             double rh) {
        double tMean = getMeanTemperature(tMax, tMin, unit);
        double delta = getSlopeOfSaturatedWaterVaporCurve(tMean);
        double y = getPsychometricConstant(height);
        double es0 = getMeanSaturationVaporPressure(tMin, tMax, unit);
        double ea = getActualVaporPressure(tMin, tMax, unit, rh);
        return calculate(rn, g, cn, cd, u2, delta, y, tMean, es0, ea);
    }

    /**
     * Calculate mean temperature
     * @param tMax maximum temperature per time interval
     * @param tMin minimum temperature per time interval
     * @param unit type of temperature (Celcius, Farenheit, Kelvin, Reaumur)
     * @return mean temperature in Celcius
     */
    public static double getMeanTemperature(double tMax, double tMin, UnitTemperature unit) {
        double temperatureMax = convertTemperatureToCelcius(tMax, unit);
        double temperatureMin = convertTemperatureToCelcius(tMin, unit);
        return (temperatureMax + temperatureMin) / 2;
    }

    /**
     * Calculate slope of saturated water vapor curve
     * @param tMean mean of temperature
     * @return slope = (4098 * (0.6108 * Math.exp((17.27 * tMean)/(tMean + 237.3)))) / Math.pow(tMean + 237.3, 2);
     */
    public static double getSlopeOfSaturatedWaterVaporCurve(double tMean) {
        return (4098 * (0.6108 * Math.exp((17.27 * tMean)/(tMean + 237.3)))) / Math.pow(tMean + 237.3, 2);
    }

    /**
     * Calculate slope of saturated water vapor curve
     * @param tMax maximum temperature per time interval
     * @param tMin minimum temperature per time interval
     * @param unit type of temperature (Celcius, Farenheit, Kelvin, Reaumur)
     * @return slope of saturated water vapor curve
     */
    public static double getSlopeOfSaturatedWaterVaporCurve(double tMax, double tMin, UnitTemperature unit) {
        double tMean = getMeanTemperature(tMax, tMin, unit);
        return getSlopeOfSaturatedWaterVaporCurve(tMean);
    }

    /**
     * Calculate the atmospheric pressure of certain area
     * @param height elevation above sea surface level
     * @return P = 101.3 * Math.pow((293 - 0.0065 * height) / 293, 5.26);
     */
    public static double getAtmosphericPressure(double height) {
        return 101.3 * Math.pow((293 - 0.0065 * height) / 293, 5.26);
    }

    /**
     * Calculate the psychometric constant
     * @param p atmospheric pressure
     * @return y = 0.000665 * p;
     */
    public static double getPsychometricConstant(double p) {
        return 0.000665 * p;
    }

    /**
     * Calculate the psychometric constant
     * @param height height above sea surface level
     * @return y = 0.000665 * p;
     */
    public static double getPsychometricConstantWithHeight(double height) {
        double p = getAtmosphericPressure(height);
        return getPsychometricConstant(p);
    }

    /**
     * Calculate vapor pressure in certain temperature
     * @param t temperature
     * @param unit type temperature
     * @return et = 0.6108 * Math.exp((17.27 * t) / (t + 237.3))
     */
    public static double getVaporPressureInTemperature(double temperature, UnitTemperature unit) {
        double t = convertTemperatureToCelcius(temperature, unit);
        return 0.6108 * Math.exp((17.27 * t) / (t + 237.3));
    }

    /**
     * Calculate mean saturation vapor pressure
     * @param tMin minimum temperature in certain time
     * @param tMax maximum temperature in certain time
     * @param unit type temperature
     * @return mean saturation vapor pressure
     */
    public static double getMeanSaturationVaporPressure(double tMin, double tMax, UnitTemperature unit) {
        double temperatureMin = convertTemperatureToCelcius(tMin, unit);
        double temperatureMax = convertTemperatureToCelcius(tMax, unit);
        return (getVaporPressureInTemperature(temperatureMax, UnitTemperature.CELCIUS) +
                getVaporPressureInTemperature(temperatureMin, UnitTemperature.CELCIUS)) / 2;
    }

    /**
     * Calculate mean saturation vapor pressure
     * @param tMean minimum temperature in certain time
     * @param unit type temperature
     * @return mean saturation vapor pressure
     */
    public static double getMeanSaturationVaporPressure(double tMean, UnitTemperature unit) {
        double temperatureMean = convertTemperatureToCelcius(tMean, unit);
        return getVaporPressureInTemperature(temperatureMean, UnitTemperature.CELCIUS);
    }

    /**
     * Calculate actual vapor pressure with all data
     * @param tMin minimum temperature in certain time
     * @param tMax maximum temperature in certain time
     * @param unit type temperature
     * @param rhMax maximum relative humidity in certain time
     * @param rhMin maximum relative humidity in certain time
     * @return ea = (vaporPressureMin * rhMax / 100 + vaporPressureMax * rhMin / 100) / 2
     */
    public static double getActualVaporPressure(double tMin, double tMax, UnitTemperature unit,
                                                double rhMax, double rhMin) {
        double temperatureMin = convertTemperatureToCelcius(tMin, unit);
        double temperatureMax = convertTemperatureToCelcius(tMax, unit);
        double vaporPressureMax = getVaporPressureInTemperature(temperatureMax, UnitTemperature.CELCIUS);
        double vaporPressureMin = getVaporPressureInTemperature(temperatureMin, UnitTemperature.CELCIUS);
        return (vaporPressureMin * rhMax / 100 + vaporPressureMax * rhMin / 100) / 2;
    }

    /**
     * Calculate actual vapor pressure with error in rhMin
     * @param t minimum temperature in certain time
     * @param unit type temperature
     * @param rh maximum relative humidity in certain time
     * @return ea = (vaporPressureMin * rhMax / 100 + vaporPressureMax * rhMin / 100) / 2
     */
    public static double getActualVaporPressure(double t, UnitTemperature unit, double rh) {
        double temperature = convertTemperatureToCelcius(t, unit);
        double vaporPressure = getVaporPressureInTemperature(temperature, UnitTemperature.CELCIUS);
        return vaporPressure * rh / 100;
    }

    /**
     * Calculate actual vapor pressure with no data of rhMin and rhMax
     * @param tMin minimum temperature in certain time
     * @param tMax maximum temperature in certain time
     * @param unit type temperature
     * @param rhMean relative humidity in certain time
     * @return ea = (vaporPressureMin * rhMax / 100 + vaporPressureMax * rhMin / 100) / 2
     */
    public static double getActualVaporPressure(double tMin, double tMax, UnitTemperature unit,
                                                double rhMean) {
        double temperatureMin = convertTemperatureToCelcius(tMin, unit);
        double temperatureMax = convertTemperatureToCelcius(tMax, unit);
        double vaporPressureMax = getVaporPressureInTemperature(temperatureMax, UnitTemperature.CELCIUS);
        double vaporPressureMin = getVaporPressureInTemperature(temperatureMin, UnitTemperature.CELCIUS);
        return (rhMean / 100) * ((vaporPressureMin + vaporPressureMax) / 2);
    }

    /**
     * Convert temperature to Celcius
     * @param temperature
     * @param fromUnit from type (Kelvin, Farenheit, Reaumur)
     * @return
     */
    public static double convertTemperatureToCelcius(double temperature, UnitTemperature fromUnit) {
        double t = temperature;
        if (fromUnit.compareTo(UnitTemperature.KELVIN) == 0) {
            t -= 273.05;
        } else if (fromUnit.compareTo(UnitTemperature.FARENHEIT) == 0) {
            t = ((t - 32) * 5) / 9;
        } else if (fromUnit.compareTo(UnitTemperature.REAUMUR) == 0) {
            t = t * 1.25;
        }
        return t;
    }

    /**
     * Convert mean daily solar radiation from Watts per square meter per day to Megajoules per square meter per day
     * @param Rn mean daily solar radiation in Watts per square meter per day
     * @return mean daily solar radiation in Megajoules per square meter per day
     */
    public static double convertRadiation(double Rn) {
        return Rn * 0.0864;
    }

    /**
     * Convert wind speed if the given is not in height of 2 meters
     * @param h height above the ground surface
     * @param uh wind speed in h
     * @return wind speed in 2 meters above the ground surface
     */
    public static double convertWindSpeed(double h, double uh) {
        return uh * (4.87 / (Math.log1p(67.8 * h - 5.42)));
    }
}
