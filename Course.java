public class Course
{
	private String courseName, campus;
	private Department department;
	private Professor professor;
	
	public Course(String courseName, String campus, Department department, Professor professor)
	{
		this.courseName = courseName;
		this.campus = campus;
		this.department = department;
		this.professor = professor;
	}
	
	public String getCourseName() { return courseName; }
	public String getCampus() { return campus; }
	public Department getDepartment() { return department; }
	public Professor getProfessor() { return professor; }
	
	public void setCampus(String campus) { this.campus = campus; }
	public void setDepartment(Department department) { this.department = department; }
	public void setProfessor(Professor professor) { this.professor = professor; }
}