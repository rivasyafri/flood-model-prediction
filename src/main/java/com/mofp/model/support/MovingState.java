package com.mofp.model.support;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mofp.model.State;
import com.mofp.model.support.json.StateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class MovingState {

    @JsonSerialize(using = StateSerializer.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stateId", nullable = false)
    @Getter @Setter
    @JsonManagedReference
    protected State value;

    @Column(nullable = false)
    @Getter @Setter
    protected Integer startTime;

    @Column
    @Getter @Setter
    protected Integer endTime;
}

