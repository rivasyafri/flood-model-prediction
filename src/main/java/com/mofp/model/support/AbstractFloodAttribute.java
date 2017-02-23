package com.mofp.model.support;

import com.mofp.model.Flood;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractFloodAttribute<T> extends UnitObject<T> {

    @ManyToOne
    @JoinColumn(name = "floodId", nullable = false)
    @Getter @Setter
    protected Flood flood;
}
