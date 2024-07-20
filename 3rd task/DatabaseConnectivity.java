import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectivity {

    
        public static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","BSF21#F@ll23");
    }

}
