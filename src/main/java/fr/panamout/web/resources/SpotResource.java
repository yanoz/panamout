package fr.panamout.web.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import fr.panamout.core.SpotSystem;
import fr.panamout.core.service.SpotService;
import fr.panamout.domain.Spot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by yann on 5/7/15.
 */
@Path("/spot")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpotResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpotResource.class);

    @Inject
    private SpotService spotService;

    @GET
    @Path("info")
    public String getInfo() {
        return "Import resource";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSpot(String spot) {
        if (Strings.isNullOrEmpty(spot)) {
            return Response.status(400).entity("JSON string is null or empty").type(MediaType.TEXT_PLAIN).build();
        }
        Spot validatedSpot;
        try {
            validatedSpot = new ObjectMapper().readValue(spot, Spot.class);
        } catch (IOException e) {
            LOGGER.info("Error with spot inputs", e);
            return Response.status(400).entity(String.format("Error with spot inputs : %s", e.getMessage())).type(MediaType.TEXT_PLAIN).build();
        }
        SpotSystem.getInstance().create(validatedSpot);
        LOGGER.info("New spot has been created");
        return Response.status(201).entity("New spot has been created").type(MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpots(@QueryParam("offset") Integer offset, @QueryParam("count") Integer count) {
        try {
            List<Spot> spots = spotService.getSpot(offset, count);
            return Response.status(200).entity(new ObjectMapper().writeValueAsString(spots)).type(MediaType.TEXT_PLAIN).build();
        } catch (IOException e) {
            LOGGER.info("Error while getting spots", e);
            return Response.status(500).entity(String.format("Error while getting spots :  %s", e.getMessage())).type(MediaType.TEXT_PLAIN).build();
        }
    }
}
