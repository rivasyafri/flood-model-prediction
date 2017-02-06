package com.mofp.model.moving;

import com.mofp.model.Cell;
import com.mofp.model.support.MovingState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@Entity
@Table(name = "cell_state")
public class CellState extends MovingState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cell_state_id_seq")
    @SequenceGenerator(name="cell_state_id_seq", sequenceName="cell_state_id_seq", allocationSize=1)
    @Getter @Setter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cellId", nullable = false)
    @Getter @Setter
    private Cell cell;
}
