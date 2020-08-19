import java.sql.*;
import javax.swing.*;

public class DepartmentDatabase 
{	
	public static boolean writeDepartment(String departmentName)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("INSERT INTO Department (departmentName) VALUES ('" + departmentName + "')");
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
	
	public static Department readDepartment(String departmentName)
	{	
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet result = database.getStatement().executeQuery("SELECT * FROM Department WHERE departmentName = '" + departmentName + "'");
			result.next();
			database.close();
			return new Department(departmentName, result.getString("departmentHead"));
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean editDepartment(Department department)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("UPDATE Department SET departmentHead = '" + department.getDepartmentHead() + "' WHERE departmentName = '" + department.getDepartmentName() + "'");
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
	
	public static boolean deleteDepartment(String departmentName)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("DELETE FROM Department WHERE departmentName = '" + departmentName + "'");
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
	
	public static JTable getDepartmentJTable()
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Department");
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.first();
			
			if (numRows == 0)
				throw new SQLException();
			 
			ResultSetMetaData meta = resultSet.getMetaData();
			String[] colNames = new String[meta.getColumnCount()];
			colNames[0] = "Department    Name"; colNames[1] = "Department    Head";
			
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
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Department");
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
	
	public static String[] getDepartmentNames()
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Department");
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.beforeFirst();
			
			if (numRows == 0)
				throw new SQLException();
			
			String[] departmentNames = new String[numRows];
			
			for (int i = 0; resultSet.next(); i++)
				departmentNames[i] = resultSet.getString("departmentName");
			
			database.close();
			return departmentNames;
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return null;
		}
	}
}