package com.mofp.ca.service.impl;

import com.mofp.ca.ca.*;
import com.mofp.ca.model.Neighborhood;
import com.mofp.ca.service.CellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Move method from Cell CA by Taufiqurrahman
 * With edited version
 *
 * @author rivasyafri
 */
@Service
public class DefaultCellServiceImpl implements CellService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Neighborhood neighborhood = new Neighborhood();
    private General GLOBAL;

    public boolean windProbability(int val) {
        Random random = new Random();
        if (val > random.nextInt(100)) {
            return true;
        } else {
            return false;
        }
    }

    public int countNeighborhood(Cell currCell, String stateName, String neighborhoodType, int dist) {
        ArrayList<int[][]> neighbor = neighborhood.use(neighborhoodType);
        int dx = currCell.getX();
        int dy = currCell.getY();
        int Nb = 0;
        int _x, _y;
        for (int[][] i : neighbor) { //1 loop
            for (int[] j : i) {
                for (int k = 1; k <= dist; k++) {
                    _x = dx + (k * j[0]);
                    _y = dy + (k * j[1]);
                    if (((_x >= 0) && (_x < GLOBAL.GRIDX)) && ((_y >= 0) && (_y < GLOBAL.GRIDY))) {
                        if (GLOBAL.cellspace.getCellProperties(_x, _y).getState().getName().equals(stateName)) {
                            Nb++;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return Nb;
    }

    public void run(int x, int y) {
        int _x, _y;
        dist = 1;
        for (int[][] i : GLOBAL.neighboor) {
            for (int[] j : i) {
                _x = j[0] + x;
                _y = j[1] + y;
                rule(_x, _y);
            }
        }
        dist = GLOBAL.getModelproperties().getWind().getDistance();
        if (GLOBAL.getModelproperties().getWind().isUseWind()) {

            for (int k = 2; k <= dist; k++) {
                windCalculation(x, y, dist);
            }
        }
        rule(x, y);
    }

    public void windCalculation(int x, int y, int dist) {
        String use = GLOBAL.neighborhood.getCurrentlyUse();
        if (use.equals("moore")) {
            int SouthEast = GLOBAL.getModelproperties().getWind().getSouthEast();
            int SouthWest = GLOBAL.getModelproperties().getWind().getSouthWest();
            int NorthEast = GLOBAL.getModelproperties().getWind().getNorthEast();
            int NorthWest = GLOBAL.getModelproperties().getWind().getNorthWest();
            if (windProbability(SouthEast)) {
                int[] arr = GLOBAL.neighborhood.getSoutheast();
                rule(x + (dist * arr[0]), y + (dist * arr[1]));
            }
            if (windProbability(SouthWest)) {
                int[] arr = GLOBAL.neighborhood.getSouthwest();
                rule(x + (dist * arr[0]), y + (dist * arr[1]));
            }
            if (windProbability(NorthEast)) {
                int[] arr = GLOBAL.neighborhood.getNortheast();
                rule(x + (dist * arr[0]), y + (dist * arr[1]));
            }
            if (windProbability(NorthWest)) {
                int[] arr = GLOBAL.neighborhood.getNorthwest();
                rule(x + (dist * arr[0]), y + (dist * arr[1]));
            }
        }
        int South = GLOBAL.getModelproperties().getWind().getSouth();
        int West = GLOBAL.getModelproperties().getWind().getWest();
        int East = GLOBAL.getModelproperties().getWind().getEast();
        int North = GLOBAL.getModelproperties().getWind().getNorth();

        if (windProbability(South)) {
            int[] arr = GLOBAL.neighborhood.getSouth();
            rule(x + (dist * arr[0]), y + (dist * arr[1]));
        }
        if (windProbability(East)) {
            int[] arr = GLOBAL.neighborhood.getEast();
            rule(x + (dist * arr[0]), y + (dist * arr[1]));
        }
        if (windProbability(West)) {
            int[] arr = GLOBAL.neighborhood.getWest();
            rule(x + (dist * arr[0]), y + (dist * arr[1]));
            //System.out.println("West");
        }
        if (windProbability(North)) {
            int[] arr = GLOBAL.neighborhood.getNorth();
            rule(x + (dist * arr[0]), y + (dist * arr[1]));
            // System.out.println("North");
        }

    }

    public void rule(int dx, int dy) {
        if (((dx >= 0) && (dx < GLOBAL.GRIDX)) && ((dy >= 0) && (dy < GLOBAL.GRIDY))) {
            if (!GLOBAL.cellspace.getCellProperties(dx, dy).process) {
                if (GLOBAL.useRule == true) {
                    ruleFromUser(dx, dy);
                } else {
                    innerRule(dx, dy);
                }
            }
        }
    }

    public void ruleFromUser(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        currCell.setDist(dist);
        Cell st;
        boolean active = false;
        st = GLOBAL.rule.summonClass(currCell);
        if (st.getState().isActive()) {
            addActiveCell(currCell);
            active = true;
        }
        if (!currCell.getProperty().get("Name").equals(st.getProperty().get("Name"))) {
            st.getState().setActive(active);
            currCell.process = true;

        } else {
            currCell.process = false;
        }
        this.NewCellProperties(st);
    }

    public void innerRule(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        Cell st = quartieri(dx, dy);

        if (st.getState().isActive()) {
            addActiveCell(currCell);
        }
        // if(!currCell.getProperty().get("Name").equals(st.getProperty().get("Name"))){
        currCell.process = true;
        // }else{
        // currCell.process=false;
        //  }
        this.NewCellProperties(st);
    }

    public Cell ObjectSpreading(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        if (currCell.getProperty().get("Name").equals("Forest")) {
            pro = (Property) GLOBAL.cellProperties.get("onFire");
            newstate = ((State) pro.getCellProperties().get("Relation"));
            return new Cell(currCell.getX(), currCell.getY(), newstate, pro.getCellProperties());
        }
        if (currCell.getProperty().get("Name").equals("onFire")) {
            pro = (Property) GLOBAL.cellProperties.get("Burnt");
            newstate = ((State) pro.getCellProperties().get("Relation"));
            return new Cell(currCell.getX(), currCell.getY(), newstate, pro.getCellProperties());
        }
        return currCell;
    }

    public Cell quartieri(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        if (currCell.getProperty().get("Name").equals("Forest")) {
            Nb = countNeighborhood(currCell, "Burn");
            if (Nb >= 1) {
                if (Nb >= GLOBAL.random.nextInt(8)) {
                    return newCell(currCell, "onFire");
                }
            }
        }
        if (currCell.getProperty().get("Name").equals("onFire")) {
            return newCell(currCell, "Burnt");
        }
        return currCell;
    }

    public Cell Bodrozic(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        if (currCell.getProperty().get("Name").equals("Forest")) {
            Nb = countNeighborhood(currCell, "Burn");
            if (Nb >= 1) {
                if (probability(Integer.parseInt(cellProperties("Forest", "Probability")))) {
                    return newCell(currCell, "onFire");
                }
            }
        }
        if (currCell.getProperty().get("Name").equals("onFire")) {
            return newCell(currCell, "Burnt");
        }
        return currCell;
    }

    public Cell ruleAr(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        Nb = countNeighborhood(currCell, "Burn");
        if (Nb >= 1) {
            if (currCell.getProperty().get("Name").equals("Kayu")) {
                if (probability(Integer.parseInt(cellProperties("Kayu", "Probability")))) {
                    return newCell(currCell, "Fire", "Time", "1");
                }
            }
        }

        if (currCell.getProperty().get("Name").equals("Fire")) {

            int time = Integer.parseInt((String) currCell.getProperty().get("Time"));
            if (time != 0) {
                // System.out.println(time);
                time--;
                return newCell(currCell, "Fire", "Time", Integer.toString(time));
            } else {
                return newCell(currCell, "Burnt");
            }
        }

        return currCell;
    }

    public Cell almeida(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        Double B = 0.5;
        Double I = 0.5;
        if (currCell.getProperty().get("Name").equals("Forest")) {
            Nb = countNeighborhood(currCell, "Burn");
            if (Nb > 0) {
                if (I > GLOBAL.random.nextDouble()) {
                    return newCell(currCell, "onFire");
                }
            }
        }
        if (currCell.getProperty().get("Name").equals("onFire")) {
            if (B > GLOBAL.random.nextDouble()) {
                return newCell(currCell, "Burnt");
            } else {
                return newCell(currCell, "onFire");
            }
        }
        return currCell;
    }

    public Cell alchemy(int dx, int dy) {
        currCell = GLOBAL.cellspace.getCellProperties(dx, dy);
        if (currCell.getProperty().get("Name").equals("Excited")) {
            return newCell(currCell, "Refractory");
        } else if (currCell.getProperty().get("Name").equals("Refractory")) {
            return newCell(currCell, "Resting");
        } else {
            Nb = countNeighborhood(currCell, "Excited");
            if (Nb >= 1) {
                return newCell(currCell, "Excited");
            } else {
                return currCell;
            }
        }
    }

    public Cell newCell(Cell currCell, String val) {
        pro = (Property) GLOBAL.cellProperties.get(val);
        newstate = (State) pro.getCellProperties().get("Relation");
        return new Cell(currCell.getX(), currCell.getY(), newstate, pro.getCellProperties());
    }

    public Cell newCell(Cell currCell, String val, String key, String keyVal) {
        pro = (Property) GLOBAL.cellProperties.get(val);
        pro.getCellProperties().replace(key, keyVal);
        newstate = (State) pro.getCellProperties().get("Relation");
        return new Cell(currCell.getX(), currCell.getY(), newstate, pro.getCellProperties());
    }

    public String cellProperties(String property, String key) {
        pro = (Property) GLOBAL.cellProperties.get(property);
        return (String) pro.getCellProperties().get(key);
    }

    public void addActiveCell(Cell act) {
        act.process = false;
        GLOBAL.newActiveCell.add(act);
    }

    /* Membuat Grid dari file cellspace*/
    void drawGrid(DataCell[][] dataCell){
        for(int x=0;x<GLOBAL.GRIDX;x++){
            for(int y=0;y<GLOBAL.GRIDY;y++){
                cellGrid[x][y]=new Cell(parent,GLOBAL,dataCell[x][y].getX(),dataCell[x][y].getY(),dataCell[x][y].getSt(),dataCell[x][y].getProperty());
                if(cellGrid[x][y].getState()!=null){
                    //System.out.print(cellGrid[x][y].getState().getName());
                    if(cellGrid[x][y].getState().isActive()){
                        cellGrid[x][y].addActiveCell(cellGrid[x][y]);
                    }
                }
                parent.fill((int) cellGrid[x][y].getProperty().get("Color"),127);
                parent.rect(x*GLOBAL.CELLSIZE,y*GLOBAL.CELLSIZE,GLOBAL.CELLSIZE,GLOBAL.CELLSIZE);
            }
        }
        GLOBAL.printListofCellProperties();
    }

    /* Membuat grid secara random */
    void drawGrid(int probability){ // draw grid and initialize each grid
        HashMap property=new HashMap();
        State state;
        GLOBAL.createDefaultState();
        GLOBAL.createDefaultProperties();
        Set set=GLOBAL.cellProperties.entrySet();
        Iterator it=set.iterator();
        Map.Entry me=(Map.Entry)it.next();
        Property i=(Property)me.getValue();
        int color=(int)i.getCellProperties().get("Color");
        state=(State)i.getCellProperties().get("Relation");
        for(int x=0; x<GLOBAL.GRIDX;x++){
            for(int y=0;y<GLOBAL.GRIDY;y++){
                if(GLOBAL.probability(probability)){
                    parent.fill(color);
                    GLOBAL.registerNewCellProperties(i.getCellProperties(),1);
                    cellGrid[x][y]=new Cell(parent,GLOBAL,x,y,state,i.getCellProperties());
                    parent.rect(x*GLOBAL.CELLSIZE,y*GLOBAL.CELLSIZE,GLOBAL.CELLSIZE,GLOBAL.CELLSIZE);
                }else{
                    parent.fill(GLOBAL.BLACK);
                    state=new State("obstacle",false);
                    property.put("Name", state.getName());
                    property.put("Color", GLOBAL.BLACK);
                    cellGrid[x][y]=new Cell(parent,GLOBAL,x,y,state,property);
                    GLOBAL.registerNewCellProperties(property,1);
                    parent.rect(x*GLOBAL.CELLSIZE,y*GLOBAL.CELLSIZE,GLOBAL.CELLSIZE,GLOBAL.CELLSIZE);
                }
            }
        }
    }

    /* Membuat grid dari image */
    void drawGrid(MapLoader Map){ // draw grid and initialize each grid
        int count=0;
        int[][] colorMap=Map.getMap();
        Map.createProperties();
        HashMap listProperty=Map.getProperties();
        HashMap property=new HashMap();
        for(int x=0; x<GLOBAL.GRIDX;x++){
            for(int y=0;y<GLOBAL.GRIDY;y++){
                if(y>=colorMap[0].length || x>=colorMap.length){
                    parent.fill(GLOBAL.BLACK);
                }else{
                    if(listProperty.containsKey(colorMap[x][y])){
                        property=(HashMap)listProperty.get(colorMap[x][y]);
                    }
                    parent.fill(colorMap[x][y]);
                }
                cellGrid[x][y]=new Cell(parent,GLOBAL,x,y,null,property);
                GLOBAL.registerNewCellProperties(property,1);
                parent.rect(x*GLOBAL.CELLSIZE,y*GLOBAL.CELLSIZE,GLOBAL.CELLSIZE,GLOBAL.CELLSIZE);
                count++;
            }
        }
        System.out.println("Jumlah Cell :" + count);
    }

    /* buat grid kosong :  digunakan oleh cellspace editor */
    public void createGrid(){
        int cellSize=GLOBAL.getCELLSIZE();
        State state;
        state=new State("blank",false);
        HashMap val=new HashMap();
        val.put("State", state);
        val.put("Count",1);
        GLOBAL.states.put("blank", val);
        HashMap property=new HashMap();
        property.put("Name", "Blank");
        property.put("Color", GLOBAL.WHITE);
        property.put("Relation",state);
        for(int x=0; x<GLOBAL.getGRIDX();x++){
            for(int y=0;y<GLOBAL.getGRIDY();y++){
                parent.noFill();
                parent.noStroke();
                parent.rect(x*cellSize,y*cellSize,cellSize,cellSize);
                cellGrid[x][y]=new Cell(parent,GLOBAL,x,y,state,property);
                GLOBAL.registerNewCellProperties(property, 1);
            }
        }
    }

    public void redrawGrid(){
        int cellSize=GLOBAL.getCELLSIZE();
        for(int x=0;x<GLOBAL.getGRIDX();x++){
            for(int y=0;y<GLOBAL.getGRIDY();y++){
                if(cellGrid[x][y].getProperty().get("Color").equals(GLOBAL.WHITE)){
                    parent.noFill();
                }else{
                    parent.fill((int)cellGrid[x][y].getProperty().get("Color"));
                }
                parent.noStroke();
                parent.rect(x*cellSize,y*cellSize,cellSize,cellSize);
            }
        }
    }


    public Cell getCellProperties(int x,int y){
        return cellGrid[x][y];
    }

    public void setCellProperties(int x,int y,Cell newCell){
        this.cellGrid[x][y]=newCell;
    }


    public Cell[][] getGrid(){
        return cellGrid;
    }

    public void setGrid(Cell[][] Grid){
        this.cellGrid=Grid;
    }
}
