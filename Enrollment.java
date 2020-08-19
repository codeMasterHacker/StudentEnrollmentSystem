public class Enrollment 
{
	private int academicYear, enrollmentID;
	private String semester, grade;
	private Student student;
	private Course course;
	
	public Enrollment(Student student, Course course, String semester, int academicYear)
	{
		this.enrollmentID = 0;
		this.student = student;
		this.course = course;
		this.semester = semester;
		this.academicYear = academicYear;
		this.grade = "E";
	}
	
	public Enrollment(int enrollmentID, Student student, Course course, String semester, int academicYear, String grade)
	{
		this.enrollmentID = enrollmentID;
		this.student = student;
		this.course = course;
		this.semester = semester;
		this.academicYear = academicYear;
		this.grade = grade;
	}
	
	public int getEnrollmentID() { return enrollmentID; }
	public Student getStudent() { return student; }
	public Course getCourse() { return course; }
	public String getSemester() { return semester; }
	public int getAcademicYear() { return academicYear; }
	public String getGrade() { return grade; }
	
	public void setGrade(String grade) { this.grade = grade; }	
}