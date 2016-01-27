package fr.panamout.core.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.panamout.domain.Spot;
import fr.panamout.domain.SpotBuilder;
import fr.panamout.domain.SpotCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yann on 9/27/15.
 */
public class SpotDao {

    private static final Logger LOGGER = LoggerFactory.getLogger("SpotDao");

    private static String GET_SPOTS = "SELECT s.name, s.street, s.category, s.district, s.lat, s.lng, s.url, s.details, m.metas FROM spot s INNER JOIN metas m ON s.id=m.spot_id LIMIT ? OFFSET ?";

    private static String GET_SPOT_BY_NAME = "SELECT s.name, s.street, s.category, s.district, s.lat, s.lng, s.url, s.details, m.metas FROM spot s INNER JOIN metas m ON s.id=m.spot_id WHERE s.name = ?";

    private final Connection connection;

    public SpotDao(Connection connection) {
        this.connection = connection;
    }

    public List<Spot> getSpots(int offset, int count) throws IOException {
        List<Spot> spots = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_SPOTS)) {
            statement.setInt(1, count);
            statement.setInt(2, offset);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    spots.add(buildSpot(rs));
                }
                return spots;
            }
        } catch (SQLException e) {
            throw new IOException(String.format("Error while getting %d spots from page %d", count, offset), e);
        }
    }

    public Spot getSpotByName(String name) throws IOException {
        try (PreparedStatement statement = connection.prepareStatement(GET_SPOT_BY_NAME)) {
            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return buildSpot(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IOException(String.format("Error while getting spot with name %s", name), e);
        }
    }

    private Spot buildSpot(ResultSet rs) throws SQLException, IOException {
        SpotBuilder builder = new SpotBuilder();
        builder.named(rs.getString("name"))//
                .located(rs.getString("street"))//
                .category(SpotCategory.valueOf(rs.getString("category")))//
                .district(rs.getInt("district"))//
                .lat(rs.getDouble("lat"))//
                .lng(rs.getDouble("lng"))//
                .url(rs.getString("url"))//
                .withDetails(rs.getString("details"))//
                .withMetas(new ObjectMapper().readValue(rs.getString("metas"), new TypeReference<Map<String, String>>() {
                }));
        return builder.build();
    }
}
