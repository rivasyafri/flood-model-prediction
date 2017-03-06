package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.Weather;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "weather", path = "weather")
public interface WeatherRepository extends JpaSpecificationRepository<Weather, Long> {

    Weather findOneByDistrictIdAndStartTime(Long districtId, Long startTime);

    List<Weather> findByDistrictIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(Long districtId, Long startTime, Long endTime);
}
