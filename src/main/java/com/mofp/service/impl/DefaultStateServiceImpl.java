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

    @Override
    public State findAndCreateWetState() {
        State state = repository.findOneByName("WET");
        if (state == null) {
            state = new State("WET", true);
            state = repository.saveAndFlush(state);
        }
        return state;
    }

    @Override
    public State findAndCreateDryState() {
        State state = repository.findOneByName("DRY");
        if (state == null) {
            state = new State("DRY", false);
            state = repository.saveAndFlush(state);
        }
        return state;
    }
}
