package config;

/**
 * CONF
 */
public class Config {

    public static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static int PORT = 20006;

    public static String DB_NAME = "anime_jupyter";
    public static int DB_PORT = 3306;

    public static String URL_CONNECTION = "jdbc:mysql://127.0.0.1:"+DB_PORT+"/"+DB_NAME;
    public static String USERNAME = "root";
    public static String PW = "";

}