package com.mofp.framework.model.support;

import com.mofp.ca.model.State;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

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
    private Date startTime;

    @Column(nullable = false)
    @Getter @Setter
    private Date endTime;
}

