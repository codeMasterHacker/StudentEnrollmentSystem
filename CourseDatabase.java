import java.sql.*;
import javax.swing.*;

public class CourseDatabase 
{
	public static boolean writeCourse(Course course)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("INSERT INTO Course (courseName, campus, department, professorID) VALUES ('" + course.getCourseName() + "', '" + course.getCampus() + "', '" + course.getDepartment().getDepartmentName() + "', '" + course.getProfessor().getProfessorID() + "')");
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
	
	public static Course readCourse(String courseName)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet result = database.getStatement().executeQuery("SELECT * FROM Course WHERE courseName = '" + courseName + "'");
			result.next();
			database.close();
			return new Course(courseName, result.getString("campus"), DepartmentDatabase.readDepartment(result.getString("department")), ProfessorDatabase.readProfessor(result.getInt("professorID")));
		}
		catch (SQLException e)
		{
			database.close();
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean editCourse(Course course)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("UPDATE Course SET campus = '" + course.getCampus() + "', department = '" + course.getDepartment().getDepartmentName() + "', professorID = '" + course.getProfessor().getProfessorID() + "' WHERE courseName = '" + course.getCourseName() + "'");
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
	
	public static boolean deleteCourse(String courseName)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			database.getStatement().executeUpdate("DELETE FROM Course WHERE courseName = '" + courseName + "'");
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
	
	public static JTable getCourseJTable(String department)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Course WHERE department = '" + department + "' ORDER BY courseName");
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.first();
			
			if (numRows == 0)
				throw new SQLException();
			 
			ResultSetMetaData meta = resultSet.getMetaData();
			String[] colNames = new String[meta.getColumnCount()];
			colNames[0] = "Course    Name"; colNames[1] = "Campus"; colNames[2] = "Department"; colNames[3] = "Professor    ID";
			 
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
	
	public static String[] getCourseNames(String department)
	{
		ConnectDatabase database = new ConnectDatabase();
		
		try
		{
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Course WHERE department = '" + department + "' ORDER BY courseName");
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.beforeFirst();
			
			if (numRows == 0)
				throw new SQLException();
			
			String[] courseNames = new String[numRows];
			
			for (int i = 0; resultSet.next(); i++)
				courseNames[i] = resultSet.getString("courseName");
			
			database.close();
			return courseNames;
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
			ResultSet resultSet = database.getStatement().executeQuery("SELECT * FROM Course");
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