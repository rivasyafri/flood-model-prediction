package com.mofp.model.support;

import com.mofp.model.State;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class MovingState {

    @ManyToOne
    @JoinColumn(name = "stateId", nullable = false)
    @Getter @Setter
    private State value;

    @Column(nullable = false)
    @Getter @Setter
    private Integer startTime;

    @Column
    @Getter @Setter
    private Integer endTime;
}

