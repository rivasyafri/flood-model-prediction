package com.mofp.model.support.json;

import com.mofp.model.Project;
import com.mofp.model.Variable;
import com.mofp.model.moving.CellState;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;

/**
 * @author rivasyafri
 */
@Projection(name = "inlineVariable", types = {Project.class})
public interface inlineVariable {

    Long getId();
    String getName();
    String getDescription();
    Integer getCellSize();
    Integer getTimeStep();
    Date getStartTime();
    boolean isDone();
    String getModel();
    Variable getVariable();
    List<CellState> getCellStates();
}
