package fr.panamout.web.resources;

import com.google.common.io.ByteStreams;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import fr.panamout.core.ImportSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

/**
 * Created by yann on 4/5/15.
 */
@Path("/import")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class ImportResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportResource.class);


    @GET
    @Path("info")
    public String getInfo() {
        return "Import resource";
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response load(@FormDataParam("file") InputStream stream, //
                         @FormDataParam("file") FormDataContentDisposition fileDetail, //
                         @FormDataParam("file") FormDataBodyPart body) {
        if (stream == null) {
            return Response.status(400).entity("file is a mandatory parameter. Cannot be null!").build();
        }
        try {
            ImportSystem.getInstance().importFile(writeStream(stream));
        } catch (IOException e) {
            LOGGER.info("Error while writing the file", e);
            return Response.serverError().entity("Error while writing the file").type(MediaType.TEXT_PLAIN).build();
        }
        LOGGER.info("New data import has been launched");
        return Response.status(201).entity("New data import has been launched").type(MediaType.TEXT_PLAIN).build();
    }

    protected File writeStream(InputStream stream) throws IOException {
        File tmpFile = File.createTempFile("sae", "upload");
        try (OutputStream os = new FileOutputStream(tmpFile)) {
            ByteStreams.copy(stream, os);
        }
        return tmpFile;
    }
}


