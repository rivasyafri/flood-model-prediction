package com.mofp.model.moving;

import com.mofp.model.Cell;
import com.mofp.model.Project;
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

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    @Getter @Setter
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellState cellState = (CellState) o;

        return id != null ? id.equals(cellState.id) : cellState.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
