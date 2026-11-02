package lk.menara.smartcity360.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import lk.menara.smartcity360.dto.SelectOption;
import lk.menara.smartcity360.dto.UserDTO;
import lk.menara.smartcity360.exception.AppException;
import lk.menara.smartcity360.util.PasswordUtil;

public class AuthService {
    private static final String DATA_SOURCE_NAME = "jdbc/SmartCity";
    private static final String ROLE_RESIDENT = "RESIDENT";
    private static final String STATUS_ACTIVE = "ACTIVE";

    private static final String SELECT_USER_BY_EMAIL =
            "SELECT id, first_name, last_name, email, password_hash, role_id, assigned_zone_id "
                    + "FROM app_user WHERE email = ? AND status = ?";

    private static final String COUNT_BY_EMAIL = "SELECT COUNT(*) FROM app_user WHERE email = ?";

    private static final String INSERT_RESIDENT =
            "INSERT INTO app_user (first_name, last_name, email, password_hash, role_id, joined_at, "
                    + "building_id, status, must_change_password) "
                    + "VALUES (?, ?, ?, ?, (SELECT id FROM role WHERE name = ?), NOW(), ?, ?, 0)";

    private static final String SELECT_BUILDINGS = "SELECT id, name FROM building ORDER BY name";

    private DataSource getDataSource() throws NamingException {
        return (DataSource) new InitialContext().lookup(DATA_SOURCE_NAME);
    }

    public UserDTO authenticate(String email, String rawPassword) {
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {
            statement.setString(1, email);
            statement.setString(2, STATUS_ACTIVE);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && PasswordUtil.verify(rawPassword, resultSet.getString("password_hash"))) {
                    UserDTO user = new UserDTO();
                    user.setId(resultSet.getInt("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setRoleId(resultSet.getInt("role_id"));
                    int zoneId = resultSet.getInt("assigned_zone_id");
                    user.setAssignedZoneId(resultSet.wasNull() ? null : zoneId);
                    return user;
                }
            }
            return null;
        } catch (NamingException | SQLException e) {
            throw new AppException("Authentication error", e);
        }
    }

    public void registerResident(String firstName, String lastName, String email,
                                 String rawPassword, int buildingId) {
        try (Connection connection = getDataSource().getConnection()) {
            if (emailExists(connection, email)) {
                throw new IllegalArgumentException("Email already registered: " + email);
            }
            try (PreparedStatement statement = connection.prepareStatement(INSERT_RESIDENT)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, PasswordUtil.hash(rawPassword));
                statement.setString(5, ROLE_RESIDENT);
                statement.setInt(6, buildingId);
                statement.setString(7, STATUS_ACTIVE);
                statement.executeUpdate();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Email already registered: " + email);
        } catch (NamingException | SQLException e) {
            throw new AppException("Registration error", e);
        }
    }

    public List<SelectOption> getBuildings() {
        List<SelectOption> buildings = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BUILDINGS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                buildings.add(new SelectOption(String.valueOf(resultSet.getInt("id")), resultSet.getString("name")));
            }
            return buildings;
        } catch (NamingException | SQLException e) {
            throw new AppException("Error loading buildings", e);
        }
    }

    private boolean emailExists(Connection connection, String email) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(COUNT_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }
}