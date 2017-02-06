package com.mofp.model.support;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class MovingObject<T>{

    @Column(nullable = false)
    @Getter @Setter
    private T value;

    @Column(nullable = false)
    @Getter @Setter
    private Integer startTime;

    @Column
    @Getter @Setter
    private Integer endTime;
}

