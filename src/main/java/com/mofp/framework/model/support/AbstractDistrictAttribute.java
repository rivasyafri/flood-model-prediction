package com.mofp.framework.model.support;

import com.mofp.framework.model.District;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class AbstractDistrictAttribute extends MovingObject<Double> {

    @ManyToOne
    @JoinColumn(name = "districtId", nullable = false)
    @Getter @Setter
    private District district;
}
