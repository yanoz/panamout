package fr.panamout.domain.geocode;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoCodeResponse {
    @JsonProperty("status")
    public String status;

    @JsonProperty("results")
    public Result[] results;

    public GeoCodeResponse() {

    }
}