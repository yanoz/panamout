package fr.panamout.domain.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/8/15.
 */
public class Geometry {
    public Geometry() {
    }

    @JsonProperty("location_type")
    public String locationType;
    @JsonProperty("location")
    public Location location;
    @JsonProperty("viewport")
    public Viewport viewport;
}
