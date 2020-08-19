public class Professor
{
	private int professorID;
	private String firstName, lastName;
	private Department department;
	
	public Professor(int professorID, String firstName, String lastName, Department department)
	{
		this.professorID = professorID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
	}
	
	public Professor(String firstName, String lastName, Department department)
	{
		professorID = 0;
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
	}
	
	public int getProfessorID() { return professorID; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public Department getDepartment() { return department; }
	
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setDepartment(Department department) { this.department = department; }
}