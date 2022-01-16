package lk.ac.iit.finance.app.util;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FinanceDataSource {

    private static final FinanceDataSource INSTANCE = new FinanceDataSource();

    private static JdbcDataSource jdbcDataSource;

    private FinanceDataSource() {

        init();
    }

    public static FinanceDataSource getInstance() throws SQLException {

        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {

        return jdbcDataSource.getConnection();
    }

    private void init() {

        jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:" + System.getProperty("user.dir") + "/../webapps/personal-finance-app/"
                + "WEB-INF/FINANCE_DB");
        jdbcDataSource.setUser("sa");
        jdbcDataSource.setPassword("");

        //Initialize Database
        try (Connection connection = jdbcDataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS PFM_USER (UUID VARCHAR(255) NOT NULL, USERNAME VARCHAR(255) NOT NULL, "
                            + "PASSWORD VARCHAR(255) NOT NULL, FIRST_NAME VARCHAR(255) NOT NULL, LAST_NAME VARCHAR "
                            + "(255) NOT NULL, PRIMARY KEY (UUID), UNIQUE(USERNAME))");
            preparedStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
