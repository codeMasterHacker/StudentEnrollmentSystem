public class Student
{
	private int studentID;
	private String firstName, lastName, birthday;
	
	public Student(int studentID, String firstName, String lastName, String birthday)
	{
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
	}
	
	public Student(String firstName, String lastName, String birthday)
	{
		studentID = 0;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
	}
	
	public int getStudentID() { return studentID; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getBirthday() { return birthday; }
	
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setBirthday(String birthday) { this.birthday = birthday; }
}