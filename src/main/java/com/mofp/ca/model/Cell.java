package com.mofp.ca.model;

import com.mofp.ca.model.State;
import com.mofp.flood.model.CellBalance;
import com.mofp.flood.model.CellHeightWater;
import com.mofp.flood.model.CellState;
import com.mofp.flood.model.support.AbstractProjectAttribute;
import lombok.Getter;
import lombok.Setter;
import org.postgresql.geometric.PGpolygon;

import javax.persistence.*;
import java.util.Set;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell")
public class Cell extends AbstractProjectAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_id_seq")
    @SequenceGenerator(name="cell_id_seq", sequenceName="cell_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private PGpolygon area;

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
    private State currentState;

    @Getter @Setter
    private double height;

    @Getter @Setter
    private double waterHeight;

    @Getter @Setter
    private double waterBalance;

    @Getter @Setter
    private double constantInfiltrationCapacity;

    @Getter @Setter
    private double initialInfiltrationCapacity;

    public Cell(int xArray, int yArray, State state) {
        this.xArray = xArray;
        this.yArray = yArray;
        if (state != null) {
            this.currentState = new State(state.getName());
        } else {
            this.currentState = null;
        }
    }
}
