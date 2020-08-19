import java.awt.*;
import javax.swing.*;

public class Student_Enrollment_Program extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private Font consolasBold36;
	
	public Student_Enrollment_Program()
	{
		consolasBold36 = new Font("Consolas", Font.BOLD, 36);
		
		setTitle("Student Enrollment Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(2550, 1500);
		add(new JLabel(new ImageIcon("SES.jpg")));
		menuBar();
		setVisible(true);
	}
	
	private void menuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.BLACK);
		menuBar.add(studentMenu());
		menuBar.add(departmentMenu());
		menuBar.add(professorMenu());
		menuBar.add(courseMenu());
		menuBar.add(enrollmentMenu());
		menuBar.add(gradeMenu());
		
		setJMenuBar(menuBar);
	}
	
	private JMenu studentMenu()
	{
		StudentMenu student_menu = new StudentMenu(this);
		
		JMenuItem addStudent = student_menu.getAddStudentMenuItem();
		addStudent.setFont(consolasBold36);
		
		JMenuItem searchStudent = student_menu.getSearchStudentMenuItem();
		searchStudent.setFont(consolasBold36);
		
		JMenuItem displayStudents = student_menu.getDisplayStudentsMenuItem();
		displayStudents.setFont(consolasBold36);
		
		JMenu studentMenu = new JMenu("Student");
		studentMenu.setForeground(Color.GREEN);
		studentMenu.setFont(consolasBold36);
		studentMenu.add(addStudent);
		studentMenu.add(searchStudent);
		studentMenu.add(displayStudents);
		
		return studentMenu;
	}
	
	private JMenu departmentMenu()
	{
		DepartmentMenu department_menu = new DepartmentMenu(this);
		
		JMenuItem addDepartment = department_menu.getAddDepartmentMenuItem();
		addDepartment.setFont(consolasBold36);
		
		JMenuItem searchDepartment = department_menu.getSearchDepartmentMenuItem();
		searchDepartment.setFont(consolasBold36);
		
		JMenuItem displayDepartments = department_menu.getDisplayDepartmentsMenuItem();
		displayDepartments.setFont(consolasBold36);
		
		JMenu departmentMenu = new JMenu("Department");
		departmentMenu.setForeground(Color.YELLOW);
		departmentMenu.setFont(consolasBold36);
		departmentMenu.add(addDepartment);
		departmentMenu.add(searchDepartment);
		departmentMenu.add(displayDepartments);
		
		return departmentMenu;
	}
	
	private JMenu professorMenu()
	{
		ProfessorMenu professor_menu = new ProfessorMenu(this);
		
		JMenuItem addProfessor = professor_menu.getAddProfessorMenuItem();
		addProfessor.setFont(consolasBold36);
		
		JMenuItem searchProfessor = professor_menu.getSearchProfessorMenuItem();
		searchProfessor.setFont(consolasBold36);
		
		JMenuItem displayProfessors = professor_menu.getDisplayProfessorsMenuItem();
		displayProfessors.setFont(consolasBold36);
		
		JMenu professorMenu = new JMenu("Professor");
		professorMenu.setForeground(Color.RED);
		professorMenu.setFont(consolasBold36);
		professorMenu.add(addProfessor);
		professorMenu.add(searchProfessor);
		professorMenu.add(displayProfessors);
		
		return professorMenu;
	}
	
	private JMenu courseMenu()
	{
		CourseMenu course_menu = new CourseMenu(this);
		
		JMenuItem addCourse = course_menu.getAddCourseMenuItem();
		addCourse.setFont(consolasBold36);
		
		JMenuItem searchCourse = course_menu.getSearchCourseMenuItem();
		searchCourse.setFont(consolasBold36);
		
		JMenuItem displayCourses = course_menu.getDisplayCoursesMenuItem();
		displayCourses.setFont(consolasBold36);
		
		JMenu courseMenu = new JMenu("Course");
		courseMenu.setForeground(Color.CYAN);
		courseMenu.setFont(consolasBold36);
		courseMenu.add(addCourse);
		courseMenu.add(searchCourse);
		courseMenu.add(displayCourses);
		
		return courseMenu;
	}
	
	private JMenu enrollmentMenu()
	{
		EnrollmentMenu enrollment_menu = new EnrollmentMenu(this);
		
		JMenuItem addEnrollment = enrollment_menu.getAddEnrollmentMenuItem();
		addEnrollment.setFont(consolasBold36);
		
		JMenuItem searchEnrollment = enrollment_menu.getSearchEnrollmentMenuItem();
		searchEnrollment.setFont(consolasBold36);
		
		JMenuItem displayEnrollment = enrollment_menu.getDisplayEnrollmentsMenuItem();
		displayEnrollment.setFont(consolasBold36);
		
		JMenu enrollmentMenu = new JMenu("Enrollment");
		enrollmentMenu.setForeground(Color.MAGENTA);
		enrollmentMenu.setFont(consolasBold36);
		enrollmentMenu.add(addEnrollment);
		enrollmentMenu.add(searchEnrollment);
		enrollmentMenu.add(displayEnrollment);
		
		return enrollmentMenu;
	}
	
	private JMenu gradeMenu()
	{
		GradeMenu grade_menu = new GradeMenu(this);
		
		JMenuItem manageGrade = grade_menu.getManageGradeMenuItem();
		manageGrade.setFont(consolasBold36);
		
		JMenu gradeMenu = new JMenu("Grade");
		gradeMenu.setForeground(Color.ORANGE);
		gradeMenu.setFont(consolasBold36);
		gradeMenu.add(manageGrade);
		
		return gradeMenu;
	}

	public static void main(String[] args)
	{
		new Student_Enrollment_Program();
	}
}