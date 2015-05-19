package fr.panamout.domain.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yann on 5/8/15.
 */
public class AddressComponent {
    public AddressComponent() {
    }

    @JsonProperty("long_name")
    public String longName;
    @JsonProperty("short_name")
    public String shortName;
    @JsonProperty("types")
    public String[] types;

}
