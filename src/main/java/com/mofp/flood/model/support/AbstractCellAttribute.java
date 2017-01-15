package com.mofp.flood.model.support;

import com.mofp.ca.model.Cell;
import com.mofp.framework.model.support.MovingObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author rivasyafri
 */
public abstract class AbstractCellAttribute<T> extends MovingObject<T> {

    @ManyToOne
    @JoinColumn(name = "cellId", nullable = false)
    @Getter @Setter
    private Cell cell;
}
