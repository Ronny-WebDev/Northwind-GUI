package northwindapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection 
{
	public static Connection connect()
	{
		try
		{
			Class.forName("org.mariadb.jdbc.Driver");
            String url = String.format("%s://%s:%s/%s", System.getenv("dvdrental_DB_PROTO"), System.getenv("dvdrental_DB_HOST"), System.getenv("dvdrental_DB_PORT"), System.getenv("dvdrental_DB_NAME"));
			return DriverManager.getConnection(url, System.getenv("dvdrental_DB_USERNAME"), System.getenv("dvdrental_DB_PASSWORD"));
		}
		catch(ClassNotFoundException | SQLException e)
		{
			System.out.println("Database connection failed");
			e.printStackTrace();
			return null;
		}
	}
}
