package com.mofp.ca.ca;

import com.mofp.ca.model.Cell;
import com.mofp.ca.model.Grid;
import com.mofp.ca.model.Neighborhood;
import com.mofp.ca.util.Rule;
import com.mofp.fire.model.WindCondition;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author taufiqurrahman
 * @modifiedBy rivasyafri
 */
public class General {

    //// **************** CA Environment's Properties *************************** ////
    public int W;
    public int H;
    public int GRIDX;
    public int GRIDY;
    public int visualX;
    public int visualY;
    public int TIME_STEP;
    public boolean FLAG;

    //// ************************** Added by Riva ******************************** ////
    public int dist;

    ////*********************** Model Properties ******************************** ////
    public int CELL_SIZE;
    public int CURRENT_TIME_STEP;
    public WindCondition windCondition;

    //// ************************** FLAGS *************************************** ////
    public boolean useMap;
    public boolean useRule;
    public boolean useCellspace;
    public boolean useImageOverlay;
    public String MapPath = "None";
    public String OverlayPath = "None";
    public String RulePath = "None";
    public String CellspacePath = "None";
    public boolean executeOnce = false;
    public boolean renounce = false;
    public boolean useWindCondition;

    //// ************************** Rules *************************************** ////
    public String ruleString;
    public Rule rule;

    //// ************************** Neighborhood *************************************** ////
    public Neighborhood neighborhood;
    public ArrayList<int[][]> neighboor;

    //// ************************** Public Variables *************************************** ////
    public ArrayList<Cell> activeCell;
    public List<Cell> newActiveCell;
    public List<Cell> newStateCell;
    public Grid cellSpace;
    public Random random;

    public General() {

        //// ************************** Model Properties *************************************** ////
        windCondition = new WindCondition();
        TIME_STEP = 1001;
        CELL_SIZE = 4;

        //// ************************** FLAGS *************************************** ////
        FLAG = false;
        useMap = false;
        useRule = false;
        useWindCondition = false;

        //// ************************** Public Variables *************************************** ////
        neighborhood = new Neighborhood();
        neighboor = neighborhood.use("moore");
        rule = new Rule();
        activeCell = new ArrayList<>();
        newActiveCell = new ArrayList<>();
        newStateCell = new ArrayList<>();
        random = new Random();
        dist = 1;
    }

    public General(int timeStep, int cellSize) {
        //// ************************** Model Properties *************************************** ////
        windCondition = new WindCondition();
        TIME_STEP = timeStep;
        CELL_SIZE = cellSize;

        //// ************************** FLAGS *************************************** ////
        FLAG = false;
        useMap = false;
        useRule = false;
        useWindCondition = false;

        //// ************************** Public Variables *************************************** ////
        neighborhood = new Neighborhood();
        neighboor = neighborhood.use("moore");
        rule = new Rule();
        activeCell = new ArrayList<>();
        newActiveCell = new ArrayList<>();
        newStateCell = new ArrayList<>();
        random = new Random();
        dist = 1;
    }

    public boolean checkAdditionalState(HashMap currProperty, HashMap nextProperty) {
        int sum = 0;
        boolean doIOwnIt = false;
        if (cellProperties.containsKey(nextProperty.get("Name"))) {
            doIOwnIt = true;
            sum = ((Property) cellProperties.get(nextProperty.get("Name"))).getCount() + 1;
            ((Property) cellProperties.get(nextProperty.get("Name"))).setCount(sum);
        }
        if (cellProperties.containsKey(currProperty.get("Name"))) {
            sum = ((Property) cellProperties.get(currProperty.get("Name"))).getCount() - 1;
            ((Property) cellProperties.get(currProperty.get("Name"))).setCount(sum);
        }
        return doIOwnIt;
    }

    public void counterNewCellProperties(HashMap currProperty, HashMap nextProperty) {
        if (cellProperties.isEmpty()) {   //jika kosong tambahkan sebagai properti yg baru
            cellProperties.put(nextProperty.get("Name"), new Property(nextProperty, 1));
        } else {  // jika tidak cek
            if (!checkAdditionalState(currProperty, nextProperty)) {
                cellProperties.put(nextProperty.get("Name"), new Property(nextProperty, 1));
            }
        }

    }

    public void resetCountCellProperties() {
        Set set = cellProperties.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Property i = (Property) me.getValue();
            i.setCount(0);
        }
    }

    public void saveObject(String path, Object obj) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Object loadObject(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object result = ois.readObject();
            ois.close();
            return result;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void renounceGrid(String oldProperty, HashMap Property) {  // RENOUNCE GRID WITH NEW PROPERTY
        Cell[][] cellGrid = this.getCellspace().getGrid();
        for (int x = 0; x < GRIDX; x++) {
            for (int y = 0; y < GRIDY; y++) {
                if (cellGrid[x][y].getProperty().get("Name").equals(oldProperty)) {
                    cellGrid[x][y].setProperty(Property);
                    if ((State) Property.get("Relation") != null) {
                        cellGrid[x][y].setState((State) Property.get("Relation"));
                    }
                    registerNewCellProperties(Property, 1);
                }
            }
        }
    }

    public void renounceState(String oldState, State state) {
        Cell[][] cellGrid = this.getCellspace().getGrid();

        State newState = new State(state.getName(), state.isActive());

        for (int x = 0; x < GRIDX; x++) {
            for (int y = 0; y < GRIDY; y++) {
                if ((((State) cellGrid[x][y].getProperty().get("Relation")).getName()) != null) {
                    if ((((State) cellGrid[x][y].getProperty().get("Relation")).getName()).equals(oldState)) {
                        cellGrid[x][y].getProperty().remove("Relation");
                        cellGrid[x][y].getProperty().put("Relation", newState);
                        if ((State) cellGrid[x][y].getProperty().get("Relation") != null) {
                            cellGrid[x][y].setState((State) cellGrid[x][y].getProperty().get("Relation"));
                        }
                    }
                }
            }
        }
        Iterator it = cellProperties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Property i = (Property) me.getValue();
            if (((State) i.getCellProperties().get("Relation")).getName().equals(oldState)) {
                i.getCellProperties().put("Relation", newState);
            }

        }
    }

    public void renounceGrid() {  // RENOUNCE GRID WITH NEW PROPERTY
        Cell[][] cellGrid = this.getCellspace().getGrid();

        for (int x = 0; x < GRIDX; x++) {
            for (int y = 0; y < GRIDY; y++) {
                int color = (int) cellGrid[x][y].getProperty().get("Color");
                parent.fill(color);
                parent.rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
