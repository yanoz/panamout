package fr.panamout.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by yann on 5/7/15.
 */
public class Spot {

    @Override
    public String toString() {
        return "Spot{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", address=" + address +
                ", coordinates=" + coordinates +
                ", metas=" + metas +
                '}';
    }

    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    SpotType type;
    @JsonProperty("address")
    SpotAddress address;
    @JsonProperty("metas")
    Map<String, String> metas;
    @JsonIgnore
    SpotCoordinate coordinates;

    public String getAddress() {
        return address.toString();
    }


}
