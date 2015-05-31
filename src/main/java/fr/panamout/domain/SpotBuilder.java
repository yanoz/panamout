package fr.panamout.domain;

import fr.panamout.domain.geocode.GeoCodeResponse;

import java.io.IOException;

/**
 * Created by yann on 5/7/15.
 */
public class SpotBuilder {

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
}
