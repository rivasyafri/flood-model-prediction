package com.mofp.model.support;

import com.mofp.model.Cell;
import com.mofp.model.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractCellAttribute<T> extends UnitObject<T> {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cellId", nullable = false)
    @Getter @Setter
    protected Cell cell;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    @Getter @Setter
    protected Project project;
}
