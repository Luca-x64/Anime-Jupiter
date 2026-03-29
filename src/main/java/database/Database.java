package database;

import config.Config;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

/**
 * Database
 */
public class Database implements AutoCloseable {
    private Connection conn;

    public Database(@NotNull String urlConnection, @NotNull String username, @NotNull String pw) {
        try {
            Class.forName(Config.DRIVER);
            conn = DriverManager.getConnection(urlConnection, username, pw);
        } catch (ClassNotFoundException e) {
            System.out.println("Cant connect to DB " + e.getMessage());
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("Cant establish connection with DataBase! "+e.getMessage());
            System.exit(3);
        }

    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in closing connection! " + e.getMessage());
            System.exit(2);
        }
    }

    public ResultSet ExecutePreparedQuery(String selectQuery, Object... params) {
        try {
            PreparedStatement stmt = conn.prepareStatement(selectQuery);
            for (int i = 1; i <= params.length; i++) {
                stmt.setObject(i, params[i - 1]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int executeQuery(String query, Object... params) {

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                stmt.setObject(i, params[i - 1]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}