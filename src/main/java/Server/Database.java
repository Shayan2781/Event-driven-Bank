package Server;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String CONN = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "docker";
    protected static Connection getConnection () throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(CONN, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
