package com.mofp.flood.model.support;

import com.mofp.ca.model.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractProjectAttribute {

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    @Getter @Setter
    private Project project;
}
