package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import config.CONF;
public class Conn {
    private Connection conn;

    public Conn() throws ClassNotFoundException, SQLException{
        Class.forName(CONF.DRIVER);
        conn = DriverManager.getConnection(CONF.CONNECTION, CONF.USERNAME, CONF.PW);
        


        
    }

}
