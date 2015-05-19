package fr.panamout.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yann on 5/7/15.
 */
public class SpotAddressBuilder {

    public static SpotAddress build(String addressString) throws IOException {
        if (Strings.isNullOrEmpty(addressString)) {
            throw new IOException("Spot address is null or empty");
        }
        Map<String, String> addressMap = new ObjectMapper().readValue(addressString, Map.class);
        SpotAddress address = new SpotAddress();
        if (Strings.isNullOrEmpty(addressMap.get("street"))) {
            throw new IOException("Spot street address is null or empty");
        }
        if (Strings.isNullOrEmpty(addressMap.get("district"))) {
            throw new IOException("Spot district address is null or empty");
        }
        address.street = addressMap.get("street");
        address.district = addressMap.get("district");
        address.details = addressMap.get("details");

        return address;
    }
}
