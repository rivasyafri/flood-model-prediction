package com.mofp.service;

import com.mofp.dao.StateRepository;
import com.mofp.model.State;
import com.mofp.service.support.AbstractBaseService;

/**
 * @author rivasyafri
 */
public interface StateService extends AbstractBaseService<StateRepository> {

    /**
     * Create wet state if not exist in database then return the wet state
     * @return wet state object
     */
    State findAndCreateWetState();

    /**
     * Create dry state if not exist in database then return the dry state
     * @return dry state object
     */
    State findAndCreateDryState();
}
