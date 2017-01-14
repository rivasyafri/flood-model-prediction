package com.mofp.flood.util;

/**
 * @author rivasyafri
 */
public enum State {

    DRY(0),
    WET(1);

    public int state;

    State(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String toString(State state) {
        if (State.DRY == state) {
            return "DRY";
        } else if (State.WET == state) {
            return "WET";
        } else {
            return null;
        }
    }
}
