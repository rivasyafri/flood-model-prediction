package com.mofp.util;

/**
 * @author rivasyafri
 */
public enum UnitTemperature {
    CELCIUS('C'),
    KELVIN('K'),
    FARENHEIT('F'),
    REAUMUR('R');

    public char unit;

    UnitTemperature(char unit) {
        this.unit = unit;
    }

    public char getUnit() {
        return unit;
    }
}
