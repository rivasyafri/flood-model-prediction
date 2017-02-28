package com.mofp.model.data.google;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * @author rivasyafri
 */
@MappedSuperclass
public abstract class GoogleResponse {

    @Getter
    @Setter
    private String status;
}
