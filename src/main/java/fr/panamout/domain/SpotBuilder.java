package fr.panamout.domain;

import com.google.common.base.Strings;
import fr.panamout.domain.geocode.GeoCodeResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yann on 5/7/15.
 */
public class SpotBuilder {

    String name;
    SpotCategory category;
    String street;
    Map<String, String> metas;
    Integer district;
    String details;
    String url;
    Double lat;
    Double lng;

    public SpotBuilder named(String name) {
        this.name = name;
        return this;
    }

    public SpotBuilder located (String street) {
        this.street = street;
        return this;
    }

    public SpotBuilder district (Integer district) {
        this.district = district;
        return this;
    }

    public SpotBuilder category(SpotCategory category) {
        this.category = category;
        return this;
    }

    public SpotBuilder withMetas(Map<String, String> metas) {
        this.metas = metas;
        return this;
    }

    public SpotBuilder addMetas(Map<String, String> metas) {
        if (metas == null) {
            metas = new HashMap<>();
        }
        metas.putAll(metas);
        return this;
    }

    public SpotBuilder withDetails(String details) {
        this.details = details;
        return this;
    }

    public SpotBuilder lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public SpotBuilder lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public SpotBuilder url(String url) {
        this.url = url;
        return this;
    }


    public static Spot build(Spot spot, GeoCodeResponse response) throws IOException {
        Spot completedSpot = new Spot();
        completedSpot.name = spot.name;
        completedSpot.category = spot.category;
        completedSpot.street = spot.street;
        completedSpot.metas = spot.metas;
        completedSpot.district = spot.district;
        completedSpot.details = spot.details;
        completedSpot.lat = Double.valueOf(response.results[0].geometry.location.lat);
        completedSpot.lng = Double.valueOf(response.results[0].geometry.location.lng);
        return completedSpot;
    }

    public Spot build() {
        Spot completedSpot = new Spot();
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Name can not be null for spot creation");
        }
        completedSpot.name = name;
        completedSpot.category = category;
        if (Strings.isNullOrEmpty(street)) {
            throw new IllegalArgumentException("Street can not be null for spot creation");
        }
        completedSpot.street = street;
        completedSpot.metas = metas;
        if (null == district) {
            throw new IllegalArgumentException("District can not be null for spot creation");
        }
        completedSpot.district = district;
        completedSpot.details = details;
        completedSpot.lat = lat;
        completedSpot.lng = lng;
        if (Strings.isNullOrEmpty(street)) {
            throw new IllegalArgumentException("Url can not be null for spot creation");
        }
        completedSpot.url = url;
        return completedSpot;
    }
}
