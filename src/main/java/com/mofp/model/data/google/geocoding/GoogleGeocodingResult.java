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
public class GoogleGeocodingResult {

    @Getter
    @Setter
    @JsonProperty("address_components")
    private List<GoogleAddressComponent> addressComponents;

    @Getter
    @Setter
    @JsonProperty("formatted_address")
    private String formattedAddress;

    @Getter
    @Setter
    private GoogleGeometry geometry;

    @Getter
    @Setter
    @JsonProperty("place_id")
    private String placeId;

    @Getter
    @Setter
    private List<String> types;
}
