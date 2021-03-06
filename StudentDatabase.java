import java.sql.*;
import javax.swing.*;

public class StudentDatabase
{	
	public static boolean writeStudent(Student student) //Database is preset. studentID field is of data type auto number, ergo the studentID will be auto generated by the database
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("INSERT INTO Student (firstName, lastName, birthday) VALUES ('" + student.getFirstName() + "', '" + student.getLastName() + "', '" + student.getBirthday() + "')");
			database.close();
			return true;
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return false;
		}
	}
	
	public static Student readStudent(int studentID)
	{	
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet result = database.getStatement().executeQuery("SELECT * FROM Student WHERE studentID = " + studentID);
			result.next();
			database.close();
			return new Student(studentID, result.getString("firstName"), result.getString("lastName"), result.getString("birthday"));
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean editStudent(Student student)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("UPDATE Student SET firstName = '" + student.getFirstName() + "', lastName = '" + student.getLastName() + "', birthday = '" + student.getBirthday() + "' WHERE studentID = " + student.getStudentID());
			database.close();
			return true;
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteStudent(int studentID)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("DELETE FROM Student WHERE studentID = " + studentID);
			database.close();
			return true;
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return false;
		}
	}
	
	public static JTable getStudentJTable()
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Student ORDER BY lastName");
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.first();
			
			if (numRows == 0)
				throw new SQLException();
			 
			ResultSetMetaData meta = resultSet.getMetaData();
			String[] colNames = new String[meta.getColumnCount()];
			colNames[0] = "Student    ID"; colNames[1] = "First    Name"; colNames[2] = "Last    Name"; colNames[3] = "Birthday";
			
			String[][] tableData = new String[numRows][meta.getColumnCount()];
			for (int row = 0; row < numRows; row++)
			{
				for (int col = 0; col < meta.getColumnCount(); col++)
					tableData[row][col] = resultSet.getString(col + 1);
				
				resultSet.next();
			}
			
			database.close();
			return new JTable(tableData, colNames);
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return null;
		}
	}
	
	public static JTable getStudentJTable(String firstName, String lastName)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Student WHERE firstName = '" + firstName + "' AND lastName = '" + lastName + "'");
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.first();
			
			if (numRows == 0)
				throw new SQLException();
			 
			ResultSetMetaData meta = resultSet.getMetaData();
			String[] colNames = new String[meta.getColumnCount()];
			colNames[0] = "Student ID"; colNames[1] = "First Name"; colNames[2] = "Last Name"; colNames[3] = "Birthday";
			 
			String[][] tableData = new String[numRows][meta.getColumnCount()];
			for (int row = 0; row < numRows; row++)
			{
				for (int col = 0; col < meta.getColumnCount(); col++)
					tableData[row][col] = resultSet.getString(col + 1);
				
				resultSet.next();
			}
			
			database.close();
			return new JTable(tableData, colNames);
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isEmpty()
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Student");
			resultSet.last();
			int numRows = resultSet.getRow();
			database.close();
			
			if (numRows == 0)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return true;
		}
	}
}