package com.mofp.flood.model.support;

import com.mofp.flood.model.Flood;
import com.mofp.framework.model.support.MovingObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractFloodAttribute<T> extends MovingObject<T> {

    @ManyToOne
    @JoinColumn(name = "floodId", nullable = false)
    @Getter @Setter
    private Flood flood;
}
