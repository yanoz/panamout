package fr.panamout.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by yann on 5/7/15.
 */
public class Spot {


    @JsonProperty("name")
    public String name;
    @JsonProperty("category")
    public SpotCategory category;
    @JsonProperty("street")
    public String street;
    @JsonProperty("details")
    public String details;
    @JsonProperty("district")
    public Integer district;
    @JsonProperty("metas")
    public Map<String, String> metas;
    @JsonProperty("lat")
    public Double lat;
    @JsonProperty("lng")
    public Double lng;
    @JsonProperty("url")
    public String url;


    public String getAddress() {
        return String.format("%s, %s Paris, France", name, street);
    }
}
