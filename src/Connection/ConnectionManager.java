package Connection;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectionManager {
    private static ConnectionManager instance;
    private Connection connection;

    private ConnectionManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/bibliotheque", "root", "");
            System.out.println("Connected!!");
        } catch (Exception e) {
            System.out.println("Connected not yet!!");
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
