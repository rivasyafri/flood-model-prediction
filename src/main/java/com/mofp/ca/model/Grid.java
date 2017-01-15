package com.mofp.ca.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author taufiqurrahman
 * Edited by Riva Syafri
 */
public class Grid {

    @Getter @Setter
    Cell[][] cells;

    public Grid(int x, int y) {
        cells = new Cell[x][y];
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }
}
