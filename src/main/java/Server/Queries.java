package Server;


import java.security.NoSuchAlgorithmException;
import java.sql.*;

public abstract class Queries {

    public static void login (String body) throws SQLException, NoSuchAlgorithmException {
        Connection connection = Database.getConnection();
        String inputUser = body.split(" ")[0];
        String inputPassword = body.split(" ")[1];

    }

    public static void signUp (String firstName, String lastName, String password, String type, String nationalID, String dateOfBirth) throws SQLException {
        double interest = 0.05f;
        if ( type.equals("client")){
            interest = 0;

        }
        Connection connection = Database.getConnection();
        String sql = "CALL sign_up(?, ?, ?, ?, ?, ?, ?)";
        CallableStatement statement = connection.prepareCall(sql);
        statement.setString(1, password);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, nationalID);
        statement.setString(5, dateOfBirth);
        statement.setString(6, type);
        statement.setDouble(7, interest);
        statement.execute();
        connection.close();
    }

    public static void login (String username, String password) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "CALL login(?, ?)";
        CallableStatement statement = connection.prepareCall(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        statement.execute();
        connection.close();
    }

    public static void deposit (double amount) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "CALL deposit(?)";
        CallableStatement statement = connection.prepareCall(sql);
        statement.setDouble(1, amount);
        statement.execute();
        connection.close();
    }

    public static void withdraw (double amount) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "CALL withdraw(?)";
        CallableStatement statement = connection.prepareCall(sql);
        statement.setDouble(1, amount);
        statement.execute();
        connection.close();
    }

    public static void transfer (String to, double amount) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "CALL transfer(?, ?)";
        CallableStatement statement = connection.prepareCall(sql);
        statement.setString(1, to);
        statement.setDouble(2, amount);
        statement.execute();
        connection.close();
    }

    public static void payInterest () throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "CALL interest_payment()";
        CallableStatement statement = connection.prepareCall(sql);
        statement.execute();
        connection.close();
    }

    public static String checkBalance () throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "CALL interest_payment()";
        CallableStatement statement = connection.prepareCall(sql);
        statement.registerOutParameter(2, Types.FLOAT);
        statement.execute();
        String balance = statement.getDouble(1) + "";
        connection.close();
        return balance;
    }
}
