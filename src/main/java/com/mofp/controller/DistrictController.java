package com.mofp.controller;

import com.mofp.model.data.District;
import com.mofp.model.data.Weather;
import com.mofp.service.data.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author rivasyafri
 */
@Controller
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @RequestMapping(value = "/district/pullData", method = RequestMethod.GET)
    public @ResponseBody District pullData(@RequestParam(value = "latitude") double latitude,
                                           @RequestParam(value = "longitude") double longitude) {
        District district = districtService.pullDataOfDistrict(latitude, longitude);
        if (district != null) {
            return district;
        }
        return null;
    }

    @RequestMapping(value = "/district/getData", method = RequestMethod.GET)
    public @ResponseBody List<Weather> getData(@RequestParam(value = "id") long districtId,
                          @RequestParam(value = "startTime") long startTime,
                          @RequestParam(value = "endTime") long endTime) {
        District district = districtService.getRepository().getOne(districtId);
        if (district != null) {
            return districtService.getDataOfWeatherByDistrictAndTime(district, startTime, endTime);
        }
        return null;
    }
}
