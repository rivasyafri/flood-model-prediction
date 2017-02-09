package com.mofp.spring;

import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.Variable;
import com.mofp.model.moving.CellState;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Project.class);
        config.exposeIdsFor(Variable.class);
        config.exposeIdsFor(Cell.class);
        config.exposeIdsFor(CellState.class);
    }
}
