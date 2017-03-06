package com.mofp.service.impl;

import com.mofp.dao.StateRepository;
import com.mofp.model.State;
import com.mofp.service.StateService;
import com.mofp.service.support.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author rivasyafri
 */
@Service
public class DefaultStateServiceImpl extends DefaultBaseServiceImpl<StateRepository> implements StateService {

    private State WET_STATE = null;
    private State DRY_STATE = null;

    @Override
    public State findAndCreateWetState() {
        if (WET_STATE != null) {
            return WET_STATE;
        }
        WET_STATE = repository.findOneByName("WET");
        if (WET_STATE == null) {
            WET_STATE = new State("WET", true);
            WET_STATE = repository.saveAndFlush(WET_STATE);
        }
        return WET_STATE;
    }

    @Override
    public State findAndCreateDryState() {
        if (DRY_STATE != null) {
            return DRY_STATE;
        }
        DRY_STATE = repository.findOneByName("DRY");
        if (DRY_STATE == null) {
            DRY_STATE = new State("DRY", false);
            DRY_STATE = repository.saveAndFlush(DRY_STATE);
        }
        return DRY_STATE;
    }
}
