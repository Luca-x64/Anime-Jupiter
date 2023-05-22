package database;

import java.sql.*;
import config.Config;

/**
 * Database
 */
public class Database {
    private Connection conn;
    private Statement st;

    public Database()  {
        try {
            createConnection();
        } catch (ClassNotFoundException e) {
            System.out.println("Cant connect to DB "+e);
            System.exit(1);
        }
        
    }

    private void createConnection() throws ClassNotFoundException {
        Class.forName(Config.DRIVER);
        try {
            conn = DriverManager.getConnection(Config.URL_CONNECTION, Config.USERNAME, Config.PW);
        } catch (SQLException e) {
            System.err.println("Cant estabilish connection with DataBase!");
            System.exit(3);
        }

    }

    public void closeConnection()  {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error in closing connection!");
            System.exit(2);
        }
    }

    public Connection getConn() {
        return conn;
    }
}