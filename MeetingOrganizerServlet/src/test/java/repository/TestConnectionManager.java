package repository;

import utils.BaseConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnectionManager implements BaseConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    public Connection getConnection() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("test.properties"));
            Class.forName("org.postgresql.Driver");
            String url = properties.getProperty(URL_KEY);
            String username = properties.getProperty(USERNAME_KEY);
            String password = properties.getProperty(PASSWORD_KEY);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

