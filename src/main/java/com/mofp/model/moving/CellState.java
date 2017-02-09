package com.mofp.model.moving;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.support.MovingState;
import com.mofp.model.support.json.CellSerializer;
import com.mofp.model.support.json.ProjectSerializer;
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

    @JsonSerialize(using = CellSerializer.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cellId", nullable = false)
    @Getter @Setter
    private Cell cell;

    @JsonSerialize(using = ProjectSerializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    @Getter @Setter
    @JsonBackReference
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
