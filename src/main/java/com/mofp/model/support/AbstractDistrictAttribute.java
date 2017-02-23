package com.mofp.model.support;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mofp.model.data.District;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractDistrictAttribute extends TimeInterval {

    @ManyToOne()
    @JoinColumn(name = "districtId", nullable = false)
    @Getter @Setter
    @JsonBackReference
    protected District district;
}
