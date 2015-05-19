package fr.panamout.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/7/15.
 */
public class SpotAddress {

    @Override
    public String toString() {
        return String.format("%s , Paris, France", street);
    }
    @JsonProperty("street")
    String street;
    @JsonProperty("details")
    String details;
    @JsonProperty("district")
    String district;
}
