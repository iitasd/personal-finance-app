package lk.ac.iit.finance.app.dao;

import lk.ac.iit.finance.app.model.AuthenticatedUser;
import lk.ac.iit.finance.app.model.User;
import lk.ac.iit.finance.app.util.FinanceDataSource;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is used to do the user operations in the DB.
 */
public class UserDAO {

    private static final UserDAO INSTANCE = new UserDAO();

    private UserDAO() {

    }

    public static UserDAO getInstance() {

        return INSTANCE;
    }

    /**
     * Register a new user
     * @param user user.
     */
    public void registerUser(User user) {

        user.setUserId(UUID.randomUUID().toString());

        try (Connection connection = FinanceDataSource.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO PFM_USER (UUID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, DigestUtils.sha256Hex(String.valueOf(user.getPassword())));
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error while registering: " + ex);
        }
    }

    /**
     * Authenticate a user.
     * @param username username
     * @param password password
     * @return Authenticated user
     */
    public Optional<AuthenticatedUser> authenticate(String username, char[] password) {

        try (Connection connection = FinanceDataSource.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT *  FROM PFM_USER WHERE USERNAME = ?");
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getString("PASSWORD").equals(DigestUtils.sha256Hex(String.valueOf(password)))) {
                        AuthenticatedUser authenticatedUser = new AuthenticatedUser(resultSet.getString("UUID"),
                                resultSet.getString("USERNAME"), resultSet.getString("FIRST_NAME"),
                                resultSet.getString("LAST_NAME"));
                        return Optional.of(authenticatedUser);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error while authenticating: " + ex);
        }
        return Optional.empty();
    }
}
