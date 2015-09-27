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

    private static String GET_SPOTS = "SELECT * FROM spot s INNER JOIN metas ON s.id=metas.spot_id LIMIT ? OFFSET ?";

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
                    spots.add(buildSpot(rs, false));
                }
                return spots;
            }
        } catch (SQLException e) {
            throw new IOException(String.format("Error while getting %d spots from page %d", count, offset), e);
        }
    }

    private Spot buildSpot(ResultSet rs, boolean b) throws SQLException, IOException {
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
