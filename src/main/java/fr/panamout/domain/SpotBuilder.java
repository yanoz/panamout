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
        completedSpot.type = spot.type;
        completedSpot.address = spot.address;
        completedSpot.metas = spot.metas;
        completedSpot.coordinates = new SpotCoordinate();
        completedSpot.coordinates.lat = response.results[0].geometry.location.lat;
        completedSpot.coordinates.lng = response.results[0].geometry.location.lng;
        return completedSpot;
    }
}
