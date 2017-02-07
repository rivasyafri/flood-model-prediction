package com.mofp.model;

import com.mofp.model.moving.CellBalance;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.model.support.AbstractProjectAttribute;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.Geometry;

import javax.persistence.*;
import java.util.List;
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

    @Getter
    @Setter
    private Integer xArray;

    @Getter
    @Setter
    private Integer yArray;

    @Getter
    @Setter
    private Double height;

    @Getter @Setter
    @Transient
    private transient State currentState;

    @Getter @Setter
    @Transient
    private transient double waterHeight = 0;

    @Getter @Setter
    @Transient
    private transient double totalHeight;

    @Getter @Setter
    @Transient
    private transient double waterBalanceBefore = 0;

    @Getter @Setter
    @Transient
    private transient double waterBalanceAfter = 0;

    @Getter @Setter
    @Transient
    private transient double constantInfiltrationCapacity = 0;

    @Getter @Setter
    @Transient
    private transient double initialInfiltrationCapacity = 0;

    @Getter @Setter
    @Transient
    private transient double kValue = 0;

    @Getter @Setter
    @Transient
    private transient double waterProofPercentage = 0;

    @Getter @Setter
    @Transient
    private transient double psiOrBi = 0;

    @Getter @Setter
    @Transient
    private transient int timeStartFlooded = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cell", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<CellHeightWater> cellHeightWaters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cell", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<CellState> cellStates;

    public Cell() {}

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
        this.psiOrBi = randomGenerator.nextDouble();
        this.waterProofPercentage = randomGenerator.nextDouble();
        this.updateTotalHeight();
    }
}
