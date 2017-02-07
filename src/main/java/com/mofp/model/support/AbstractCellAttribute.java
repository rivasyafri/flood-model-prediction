package com.mofp.model.support;

import com.mofp.model.Cell;
import com.mofp.model.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractCellAttribute<T> extends MovingObject<T> {

    @ManyToOne
    @JoinColumn(name = "cellId", nullable = false)
    @Getter @Setter
    private Cell cell;

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    @Getter @Setter
    private Project project;
}
