package fr.panamout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import fr.panamout.domain.geocode.GeoCodeResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by yann on 3/29/15.
 */
public class CleanFiles {



    public static void main(String args[]) throws IOException {
        String json = Files.toString(new File("/home/yann/Desktop/google.json"), Charset.defaultCharset());
        GeoCodeResponse response = new ObjectMapper().readValue(json, GeoCodeResponse.class);
        System.out.println(response.toString());
    }
}
