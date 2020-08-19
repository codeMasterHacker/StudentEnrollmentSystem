import java.sql.*;
import javax.swing.*;

public class ConnectDatabase
{
	private Connection connection = null;
	private Statement statement = null;
	
	public ConnectDatabase()
	{
		try
		{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			
			final String databasePath = "jdbc:ucanaccess://C:/Users/Enrique Munoz/Desktop/studentEnrollment_Database.accdb";
			
			connection = DriverManager.getConnection(databasePath);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,  ResultSet.CONCUR_READ_ONLY);
		}
		catch (ClassNotFoundException e) { JOptionPane.showMessageDialog(null, "Problem in loading or registering MS Access JDBC driver. Error: " + e.getMessage()); e.printStackTrace(); }
		catch (SQLException e) { JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage()); e.printStackTrace(); }
	}
	
	public void close()
	{
		try
		{
			if (connection != null)
			{
				statement.close();
				connection.close();
			}
			else
				JOptionPane.showMessageDialog(null, "ERROR: Connetion was never made");
		}
		catch (SQLException e) { JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage()); e.printStackTrace();}
	}
	
	public Statement getStatement() { return statement; }
}