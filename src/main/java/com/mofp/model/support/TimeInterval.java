package com.mofp.model.support;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class TimeInterval {

    @Column(nullable = false)
    @Getter @Setter
    protected Long startTime;

    @Column
    @Getter @Setter
    protected Long endTime;
}

