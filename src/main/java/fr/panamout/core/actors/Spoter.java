package fr.panamout.core.actors;

/**
 * Created by yann on 5/7/15.
 */

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import fr.panamout.domain.Spot;
import fr.panamout.domain.SpotBuilder;
import fr.panamout.domain.geocode.GeoCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;

public class Spoter extends AbstractActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Spoter.class);

    // URL prefix to the geocoder
    private static final String GEOCODER_REQUEST_PREFIX = "https://maps.googleapis.com/maps/api/geocode/json";

    private static final String API_KEY="AIzaSyBayVIWxSFSEzH1gzWpAmIpsSanPgvMVlo";

    private ActorSelection getPersister() {
        return getContext().actorSelection("/user/persister");
    }

    private Spot getCoordinate(Spot spot) throws IOException {
        Client client = Client.create();
        WebResource webResource = client
                .resource(String.format("%s?address=%s&key=%s", GEOCODER_REQUEST_PREFIX, URLEncoder.encode(spot.getAddress(), "UTF-8"), API_KEY));
        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);
        GeoCodeResponse googleResponse = new ObjectMapper().readValue(response.getEntity(String.class), GeoCodeResponse.class);
        if (!googleResponse.status.equals("OK")) {
            throw new IOException(String.format("Error while calling Geocode API : %s", googleResponse.status));
        }
        return SpotBuilder.build(spot, googleResponse);
    }


    public Spoter() {
        receive(ReceiveBuilder.match(Spot.class, s -> {getPersister().tell(getCoordinate(s), self());}).build());
    }
}
