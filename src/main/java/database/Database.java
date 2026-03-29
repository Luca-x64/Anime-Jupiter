package database;

import java.sql.*;
import config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Database
 */
public class Database /* implements  AutoCloseable*/ {
    private Connection conn;

    public Database(@NotNull String urlConnection, @NotNull String username, @NotNull String pw)  {
        try {
            Class.forName(Config.DRIVER);
            System.out.println(urlConnection);
            System.out.println(username);
            System.out.println(pw);
            conn = DriverManager.getConnection(urlConnection, username, pw);
        } catch (ClassNotFoundException e) {
            System.out.println("Cant connect to DB "+ e.getMessage());
            System.exit(1);
        }catch (SQLException e) {
            System.err.println("Cant establish connection with DataBase! " );
            System.out.println(e);
                System.exit(3);
        }

    }

    public void closeConnection()  {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in closing connection! " + e.getMessage());
            System.exit(2);
        }
    }

    public @NotNull Connection getConn() {
        return conn; //TODO fix ref. escaping
    }

    //TODO
//    @Override
//    public void close() throws Exception {
//        closeConnection();
//    }
}