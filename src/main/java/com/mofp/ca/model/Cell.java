package com.mofp.ca.model;

import com.mofp.flood.model.CellBalance;
import com.mofp.flood.model.CellHeightWater;
import com.mofp.flood.model.CellState;
import com.mofp.flood.model.support.AbstractProjectAttribute;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;
import org.postgresql.geometric.PGpolygon;

import javax.persistence.*;
import java.util.Random;
import java.util.Set;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell")
public class Cell extends AbstractProjectAttribute implements Comparable<Cell> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_id_seq")
    @SequenceGenerator(name="cell_id_seq", sequenceName="cell_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private Geometry area;

    // Flood = add more inferred variable here
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cell", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<CellBalance> cellBalanceSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cell", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<CellHeightWater> cellHeightWaterSet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cell", cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<CellState> cellStates;

    @Getter @Setter
    private int xArray;

    @Getter @Setter
    private int yArray;

    @Getter @Setter
    @Transient
    private transient State currentState;

    @Getter @Setter
    @Transient
    private transient double height;

    @Getter @Setter
    @Transient
    private transient double waterHeight;

    @Getter @Setter
    @Transient
    private transient double totalHeight;

    @Getter @Setter
    @Transient
    private transient double waterBalance;

    @Getter @Setter
    @Transient
    private transient double constantInfiltrationCapacity;

    @Getter @Setter
    @Transient
    private transient double initialInfiltrationCapacity;

    @Getter @Setter
    @Transient
    private transient double kValue;

    @Getter @Setter
    @Transient
    private transient int timeStartFlooded;

    public Cell(int xArray, int yArray, State state) {
        this.xArray = xArray;
        this.yArray = yArray;
        if (state != null) {
            this.currentState = new State(state.getName());
        } else {
            this.currentState = null;
        }
    }

    public void updateTotalHeight() {
        this.totalHeight = this.height + this.waterHeight;
    }

    @Override
    public int compareTo(Cell o) {
        if (this.totalHeight == o.getTotalHeight()) {
            return 0;
        } else if (this.totalHeight < o.getTotalHeight()) {
            return -1;
        } else {
            return 1;
        }
    }

    public void randomizeData() {
        Random randomGenerator = new Random();
        this.height = randomGenerator.nextDouble() * 10 + 10;
        this.constantInfiltrationCapacity = randomGenerator.nextDouble();
        this.initialInfiltrationCapacity = randomGenerator.nextDouble() * 15;
        this.kValue = randomGenerator.nextDouble();
        updateTotalHeight();
    }
}
