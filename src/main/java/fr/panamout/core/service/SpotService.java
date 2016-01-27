package fr.panamout.core.service;

import com.google.inject.Inject;
import fr.panamout.core.dao.SpotDao;
import fr.panamout.domain.Spot;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by yann on 9/27/15.
 */
public class SpotService {


    @Inject
    private DataSource ds;

    public SpotService() {

    }

    public List<Spot> getSpot(int offset, int count) throws IOException {
        try (Connection connection = ds.getConnection()) {
            return new SpotDao(connection).getSpots(offset, count);
        } catch (SQLException e) {
            throw new IOException("Error while getting a connection from the DB", e);
        }
    }

    public Spot getSpotByName(String name) throws IOException {
        try (Connection connection = ds.getConnection()) {
            return new SpotDao(connection).getSpotByName(name);
        } catch (SQLException e) {
            throw new IOException("Error while getting a connection from the DB", e);
        }
    }
}
