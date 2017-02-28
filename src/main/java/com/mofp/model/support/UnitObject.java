package com.mofp.model.support;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class UnitObject<T> extends TimeInterval {

    @Column(nullable = false)
    @Getter @Setter
    protected T value;
}

