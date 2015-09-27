package fr.panamout.core.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import fr.panamout.domain.Spot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by yann on 5/8/15.
 */
public class Persister extends AbstractActor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Persister.class);
    private static final String INSERT_SPOT = "INSERT INTO spot(name, category, street, details, district, lat, lng, url) VALUES (?, ?::category, ?, ?, ?, ?, ?, ?);";
    private static final String INSERT_METAS = "INSERT INTO metas(spot_id, metas) VALUES (?, ?::jsonb);";

    @Inject
    private static DataSource ds;

    private void persist(Spot s) {
        try (Connection connection = ds.getConnection();) {
            connection.setAutoCommit(false);
            long spotId = saveSpot(s, connection);
            saveMetas(s, spotId, connection);
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error(String.format("Error while saving spot %s", s.toString()), e);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error during meta serialization while saving spot %s", s.toString()), e);
        }
        LOGGER.info(s.toString());
    }

    private long saveSpot(Spot s, Connection connection) throws SQLException {
        try ( PreparedStatement psSpot = connection.prepareStatement(INSERT_SPOT, Statement.RETURN_GENERATED_KEYS);) {
            psSpot.setString(1, s.name);
            if (s.category != null) {
                psSpot.setString(2, s.category.toString());
            } else {
                psSpot.setString(2, null);
            }
            psSpot.setString(3, s.street);
            psSpot.setString(4, s.details);
            psSpot.setInt(5, s.district);
            psSpot.setDouble(6, s.lat);
            psSpot.setDouble(7, s.lng);
            psSpot.setString(8, s.url);
            psSpot.execute();
            try (ResultSet resultSet = psSpot.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        }

    }

    private void saveMetas(Spot s, long spotId, Connection connection) throws SQLException, JsonProcessingException {
        try ( PreparedStatement psMetas = connection.prepareStatement(INSERT_METAS);) {
            psMetas.setLong(1, spotId);
            psMetas.setString(2, new ObjectMapper().writeValueAsString(s.metas));
            psMetas.execute();
        }
    }

    public Persister() {
        receive(ReceiveBuilder.match(Spot.class, s -> {persist(s);}).build());
    }


}
