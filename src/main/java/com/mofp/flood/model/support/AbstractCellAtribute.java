package com.mofp.flood.model.support;

import com.mofp.flood.model.Cell;
import com.mofp.framework.model.support.MovingObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by rivas on 1/14/2017.
 */
public abstract class AbstractCellAtribute<T> extends MovingObject<T> {

    @ManyToOne
    @JoinColumn(name = "cellId", nullable = false)
    @Getter @Setter
    private Cell cell;
}
