package com.mofp.ca.service;

import com.mofp.ca.model.Cell;

/**
 * Move method from Cell CA by Taufiqurrahman
 * @author rivasyafri
 */
public interface CellService {

    boolean windProbability(int val);
    int countNeighborhood(Cell currCell,String stateName);
    void addActiveCell(Cell act);
    void run(int x, int y);
    void windCalculation(int x, int y, int dist);
    void rule(int dx,int dy);
    void ruleFromUser(int dx,int dy);
    void innerRule(int dx, int dy);
    Cell ObjectSpreading(int dx, int dy);
    Cell quartieri(int dx,int dy);
    Cell Bodrozic(int dx,int dy);
    Cell ruleAr(int dx, int dy);
    Cell almeida(int dx, int dy);
    Cell alchemy(int dx,int dy);
    Cell newCell(Cell currCell,String val);
    Cell newCell(Cell currCell,String val,String key,String keyVal);
}
