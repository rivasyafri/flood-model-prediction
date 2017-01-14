package com.mofp.flood.model;

import com.mofp.flood.model.support.AbstractCellAtribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell_state")
public class CellState extends AbstractCellAtribute<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_state_id_seq")
    @SequenceGenerator(name="cell_state_id_seq", sequenceName="cell_state_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;
}
