package fr.panamout.domain.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/8/15.
 */
public class Result {
    public Result() {
    }

    @JsonProperty("formatted_address")
    public String formattedAddress;
    @JsonProperty("geometry")
    public Geometry geometry;
    @JsonProperty("types")
    public String[] types;
    @JsonProperty("place_id")
    public String placeId;
    @JsonProperty("address_components")
    public AddressComponent[] addressComponents;
}
