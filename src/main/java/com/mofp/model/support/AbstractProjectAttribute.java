package com.mofp.model.support;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mofp.model.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractProjectAttribute {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    @Getter @Setter
    @JsonBackReference
    protected Project project;
}
