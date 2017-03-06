package com.mofp.model.data.google.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleAddressComponent {

    @Getter
    @Setter
    @JsonProperty("long_name")
    private String longName;

    @Getter
    @Setter
    @JsonProperty("short_name")
    private String shortName;

    @Getter
    @Setter
    private List<String> types;
}
