package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=agenda;encrypt=false;trustServerCertificate=true;";
    private static final String USER = "urbano";
    private static final String PASSWORD = "dragona";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
