package fr.panamout.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/7/15.
 */
public class SpotCoordinate {

    @JsonProperty("lat")
    public String lat;
    @JsonProperty("lng")
    public String lng;
}
