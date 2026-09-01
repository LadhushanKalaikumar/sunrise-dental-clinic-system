import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcTest {

    public static void main(String[] args) {

        String url = "jdbc:mysql://127.0.0.1:3306/sunrise_dental_clinic";
        String user = "root";
        String password = "";

        try {
            Connection connection =
                    DriverManager.getConnection(url, user, password);

            System.out.println("SUCCESS: Java connected to MySQL!");
            connection.close();

        } catch (Exception e) {
            System.out.println("FAILED:");
            e.printStackTrace();
        }
    }
}
