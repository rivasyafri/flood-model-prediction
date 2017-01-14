package com.mofp.framework.model.support;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author rivasyafri
 */
public abstract class MovingObject<T>{

    @Column(nullable = false)
    @Getter @Setter
    private T value;

    @Column(nullable = false)
    @Getter @Setter
    private Date startTime;

    @Column(nullable = false)
    @Getter @Setter
    private Date endTime;
}

