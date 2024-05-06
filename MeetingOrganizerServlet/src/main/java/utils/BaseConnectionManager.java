package utils;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseConnectionManager {
    Connection getConnection() throws SQLException;
}
