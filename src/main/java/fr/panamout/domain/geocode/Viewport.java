package fr.panamout.domain.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/8/15.
 */
public class Viewport {
    public Viewport() {
    }

    @JsonProperty("northeast")
    public Location northeast;
    @JsonProperty("southwest")
    public Location southwest;

}
