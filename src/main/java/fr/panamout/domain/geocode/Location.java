package fr.panamout.domain.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/8/15.
 */
public class Location {
    public Location() {
    }

    @JsonProperty("lat")
    public String lat;
    @JsonProperty("lng")
    public String lng;
}
