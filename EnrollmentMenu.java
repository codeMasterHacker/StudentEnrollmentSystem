import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EnrollmentMenu
{
	private JFrame main;
	
	private Font consolasBold84;
	
	private Integer[] years = {2021, 2020, 2019, 2018};
	private String[] semesters = {"Fall", "Winter", "Spring", "Summer"};
	
	private Student studentFound;
	private Course courseFound;
	private Enrollment enrollmentFound;
	
	private JMenuItem addEnrollmentMenuItem;
	private JMenuItem searchEnrollmentMenuItem;
	private JMenuItem displayEnrollmentsMenuItem;
	
	private JTextField studentIDTextField;
	private JTextField enrollmentIDTextField;
	
	private JComboBox<String> departmentsComboBox;
	private JComboBox<String> coursesComboBox;
	private JComboBox<Integer> yearsComboBox;
	private JComboBox<String> semestersComboBox;
	
	int studentID;
	int academicYear;
	String semester;
	
	
	public JMenuItem getAddEnrollmentMenuItem() { return addEnrollmentMenuItem; }
	
	public JMenuItem getSearchEnrollmentMenuItem() { return searchEnrollmentMenuItem; }
	
	public JMenuItem getDisplayEnrollmentsMenuItem() { return displayEnrollmentsMenuItem; }
	
	public EnrollmentMenu(JFrame parent)
	{
		main = parent;
		
		consolasBold84 = new Font("Consolas", Font.BOLD, 84);
		
		addEnrollmentMenuItem = new JMenuItem("Enroll a Student");
		addEnrollmentMenuItem.addActionListener(new addEnrollmentMenuItemListener());
		
		searchEnrollmentMenuItem = new JMenuItem("Search for an Enrollment");
		searchEnrollmentMenuItem.addActionListener(new searchEnrollmentMenuItemListener());
		
		displayEnrollmentsMenuItem = new JMenuItem("Display a Student's Enrollments");
		displayEnrollmentsMenuItem.addActionListener(new displayEnrollmentsMenuItemListener());
	}
	
	private class addEnrollmentMenuItemListener implements ActionListener //1st
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (StudentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no students in the database. Students are a prerequisite to enroll. Add students to the database first.");
			else if (CourseDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no courses in the database. Courses are a prerequisite to enroll. Add courses to the database first.");
			else if (EnrollmentDatabase.departmentCoursesEmpty())
				JOptionPane.showMessageDialog(null, "There is a department with no courses. Add courses to the empty department.");
			else
			{
				main.getContentPane().removeAll();
				main.add(selectStudentPanel());
				main.setVisible(true);
			}
		}
	}
	
	private class searchEnrollmentMenuItemListener implements ActionListener //will delete a SINGLE enrollment
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (EnrollmentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no students enrolled. Enroll a student to a course.");
			else
			{
				main.getContentPane().removeAll();
				main.add(searchEnrollmentPanel());
				main.setVisible(true);
			}
		}
	}
	
	private class displayEnrollmentsMenuItemListener implements ActionListener //1st, will delete multiple enrollments
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (EnrollmentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no students enrolled. Enroll a student to a course.");
			else
			{
				main.getContentPane().removeAll();
				main.add(studentSemester_YearPanel());
				main.setVisible(true);
			}
		}	
	}
	
	private class displayEnrollmentsButtonListener implements ActionListener //2nd
	{
		public void actionPerformed(ActionEvent e) 
		{
			String studentIDnum = studentIDTextField.getText().trim();
			semester = (String)semestersComboBox.getSelectedItem();
			academicYear = (Integer)yearsComboBox.getSelectedItem();
			
			if (studentIDnum.isEmpty())
				JOptionPane.showMessageDialog(null, "Textfield is empty. Re-enter.");
			else if (studentIDnum.matches(".*[^0-9].*"))
				JOptionPane.showMessageDialog(null, "This is not a number. Re-enter.");
			else
			{
				studentID = Integer.parseInt(studentIDnum);
				JTable enrollmentJTable = EnrollmentDatabase.getEnrollmentJTable(studentID, semester, academicYear);
				
				if (enrollmentJTable != null)
				{
					main.getContentPane().removeAll();
					main.add(displayEnrollmentsPanel(enrollmentJTable));
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "The student with Student ID: " + studentID + " is not enrolled in any classes for the " + academicYear + " " + semester + " semester");
			}
		}	
	}
	
	private class selectStudentButtonListener implements ActionListener //2nd
	{
		public void actionPerformed(ActionEvent e) 
		{
			String studentIDnum = studentIDTextField.getText().trim();
			
			if (studentIDnum.isEmpty())
				JOptionPane.showMessageDialog(null, "Textfield is empty. Re-enter.");
			else if (studentIDnum.matches(".*[^0-9].*"))
				JOptionPane.showMessageDialog(null, "This is not a number. Re-enter.");
			else
			{
				int studentID = Integer.parseInt(studentIDnum);
				studentFound = StudentDatabase.readStudent(studentID);
				
				if (studentFound != null)
				{
					departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
					departmentsComboBox.setFont(consolasBold84);
					departmentsComboBox.addActionListener(new departmentsComboBoxListener());
					
					coursesComboBox = new JComboBox<String>(CourseDatabase.getCourseNames(DepartmentDatabase.getDepartmentNames()[0]));
					coursesComboBox.setFont(consolasBold84);
					
					main.getContentPane().removeAll();
					main.add(selectCoursePanel());
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Couldn't find the student with Student ID: " + studentID);
			}
		}
	}
	
	private class selectCourseButtonListener implements ActionListener //3rd
	{
		public void actionPerformed(ActionEvent e) 
		{
			String courseName = (String)coursesComboBox.getSelectedItem();
			courseFound = CourseDatabase.readCourse(courseName);
			
			if (courseFound != null)
			{
				main.getContentPane().removeAll();
				main.add(addEnrollmentPanel());
				main.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "Couldn't find the Course: " + courseName);
		}
	}
	
	private class enrollButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			Enrollment enrollment = new Enrollment(studentFound, courseFound, (String)semestersComboBox.getSelectedItem(), (Integer)yearsComboBox.getSelectedItem());
			
			if (EnrollmentDatabase.duplicateEnrollment(enrollment))
				JOptionPane.showMessageDialog(null, "The student with student ID: " + studentFound.getStudentID() + " is already enrolled to " + courseFound.getCourseName() + " for the " + enrollment.getSemester() + " " + enrollment.getAcademicYear() + " semester.\nStudent can't be enrolled to the same course for the same semester and year.");
			else
			{
				if (EnrollmentDatabase.writeEnrollment(enrollment))
				{
					main.getContentPane().removeAll();
					main.add(selectStudentPanel());
					main.setVisible(true);
					
					JOptionPane.showMessageDialog(null, "The student has been successfully enrolled.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't write enrollment to database.");
			}
		}
	}
	
	private class searchEnrollmentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String enrollID = enrollmentIDTextField.getText().trim();
			
			if (enrollID.isEmpty())
				JOptionPane.showMessageDialog(null, "Textfield is empty. Re-enter.");
			else if (enrollID.matches(".*[^0-9].*"))
				JOptionPane.showMessageDialog(null, "This is not a number. Re-enter.");
			else
			{
				int enrollmentID = Integer.parseInt(enrollID);
				Enrollment enrollment = EnrollmentDatabase.readEnrollment(enrollmentID);
				
				if (enrollment != null)
				{
					enrollmentFound = enrollment;
					
					main.getContentPane().removeAll();
					main.add(foundEnrollmentPanel());
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Couldn't find the enrollment with enrollment ID: " + enrollmentID);
			}
		}		
	}
	
	private class dropEnrollmentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int enrollmentID = enrollmentFound.getEnrollmentID();
			
			if (EnrollmentDatabase.deleteEnrollment(enrollmentID))
			{
				main.getContentPane().removeAll();
				main.add(searchEnrollmentPanel());
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, enrollmentFound.getStudent().getFirstName() + " " + enrollmentFound.getStudent().getLastName() + " has successfully dropped " + enrollmentFound.getCourse().getCourseName() + " for the " + enrollmentFound.getAcademicYear() + " " + enrollmentFound.getSemester() + " semester.");
			}
			else
				JOptionPane.showMessageDialog(null, "Couldn't find the enrollment with enrollment ID: " + enrollmentID);
		}
	}
	
	private class dropEnrollmentsButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (EnrollmentDatabase.deleteEnrollments(studentID, semester, academicYear))
			{
				main.getContentPane().removeAll();
				main.add(studentSemester_YearPanel());
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "The student has dropped all his/her classes for the " + academicYear + " " + semester + " semester.");
			}
			else
				JOptionPane.showMessageDialog(null, "ERROR! Couldn't delete enrollments.");
		}	
	}
	
	private class departmentsComboBoxListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			coursesComboBox = new JComboBox<String>(CourseDatabase.getCourseNames((String)departmentsComboBox.getSelectedItem()));
			coursesComboBox.setFont(consolasBold84);
			
			main.getContentPane().removeAll();
			main.add(selectCoursePanel());
			main.setVisible(true);
		}
	}
	
	private JPanel searchEnrollmentPanel()
	{
		enrollmentIDTextField = new JTextField(4);
		enrollmentIDTextField.setFont(consolasBold84);
		
		JLabel enrollmentIDLabel = new JLabel("Enrollment ID:");
		enrollmentIDLabel.setFont(consolasBold84);
		enrollmentIDLabel.setForeground(Color.MAGENTA);
		enrollmentIDLabel.setBackground(Color.BLACK);
		
		JPanel enrollmentIDLabelPanel = new JPanel();
		enrollmentIDLabelPanel.setBackground(Color.BLACK);
		enrollmentIDLabelPanel.add(enrollmentIDLabel);
		
		JPanel enrollmentIDTextFieldPanel = new JPanel();
		enrollmentIDTextFieldPanel.setBackground(Color.BLACK);
		enrollmentIDTextFieldPanel.add(enrollmentIDTextField);
		
		JPanel enrollmentIDPanel = new JPanel();
		enrollmentIDPanel.setBackground(Color.BLACK);
		enrollmentIDPanel.add(enrollmentIDLabelPanel);
		enrollmentIDPanel.add(enrollmentIDTextFieldPanel);
		
		JButton searchEnrollmentButton = new JButton("Search Enrollemnt");
		searchEnrollmentButton.setFont(consolasBold84);
		searchEnrollmentButton.setForeground(Color.MAGENTA);
		searchEnrollmentButton.addActionListener(new searchEnrollmentButtonListener());
		
		JPanel searchEnrollmentButtonPanel = new JPanel();
		searchEnrollmentButtonPanel.setBackground(Color.BLACK);
		searchEnrollmentButtonPanel.add(searchEnrollmentButton);
		
		JPanel returnPanel = new JPanel();	
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(enrollmentIDPanel, BorderLayout.NORTH);
		returnPanel.add(searchEnrollmentButtonPanel, BorderLayout.CENTER);
		
		return returnPanel;
	}
	
	private JPanel studentSemester_YearPanel()
	{
		studentIDTextField = new JTextField(4);
		studentIDTextField.setFont(consolasBold84);
		
		semestersComboBox = new JComboBox<String>(semesters);
		semestersComboBox.setFont(consolasBold84);
		
		yearsComboBox = new JComboBox<Integer>(years);
		yearsComboBox.setFont(consolasBold84);
		
		JLabel studentIDLabel = new JLabel("Student ID:");
		studentIDLabel.setFont(consolasBold84);
		studentIDLabel.setForeground(Color.MAGENTA);
		studentIDLabel.setBackground(Color.BLACK);
		
		JPanel studentIDLabelPanel = new JPanel();
		studentIDLabelPanel.setBackground(Color.BLACK);
		studentIDLabelPanel.add(studentIDLabel);
		
		JPanel studentIDTextFieldPanel = new JPanel();
		studentIDTextFieldPanel.setBackground(Color.BLACK);
		studentIDTextFieldPanel.add(studentIDTextField);
		
		JPanel studentIDPanel = new JPanel();
		studentIDPanel.setBackground(Color.BLACK);
		studentIDPanel.add(studentIDLabelPanel);
		studentIDPanel.add(studentIDTextFieldPanel);
		
		JLabel semestersComboBoxLabel = new JLabel("Semester:");
		semestersComboBoxLabel.setFont(consolasBold84);
		semestersComboBoxLabel.setForeground(Color.MAGENTA);
		semestersComboBoxLabel.setBackground(Color.BLACK);
		
		JPanel semestersComboBoxLabelPanel = new JPanel();
		semestersComboBoxLabelPanel.setBackground(Color.BLACK);
		semestersComboBoxLabelPanel.add(semestersComboBoxLabel);
		
		JPanel semestersComboBoxPanel = new JPanel();
		semestersComboBoxPanel.setBackground(Color.BLACK);
		semestersComboBoxPanel.add(semestersComboBox);
		
		JPanel semesterPanel = new JPanel();
		semesterPanel.setBackground(Color.BLACK);
		semesterPanel.add(semestersComboBoxLabelPanel);
		semesterPanel.add(semestersComboBoxPanel);
		
		JLabel yearsComboBoxLabel = new JLabel("Year:");
		yearsComboBoxLabel.setFont(consolasBold84);
		yearsComboBoxLabel.setForeground(Color.MAGENTA);
		yearsComboBoxLabel.setBackground(Color.BLACK);
		
		JPanel yearsComboBoxLabelPanel = new JPanel();
		yearsComboBoxLabelPanel.setBackground(Color.BLACK);
		yearsComboBoxLabelPanel.add(yearsComboBoxLabel);
		
		JPanel yearsComboBoxPanel = new JPanel();
		yearsComboBoxPanel.setBackground(Color.BLACK);
		yearsComboBoxPanel.add(yearsComboBox);
		
		JPanel yearPanel = new JPanel();
		yearPanel.setBackground(Color.BLACK);
		yearPanel.add(yearsComboBoxLabelPanel);
		yearPanel.add(yearsComboBoxPanel);
		
		JButton displayEnrollmentsButton = new JButton("Display Enrollments");
		displayEnrollmentsButton.setFont(consolasBold84);
		displayEnrollmentsButton.setForeground(Color.MAGENTA);
		displayEnrollmentsButton.addActionListener(new displayEnrollmentsButtonListener());
		
		JPanel displayEnrollmentsButtonPanel = new JPanel();
		displayEnrollmentsButtonPanel.setBackground(Color.BLACK);
		displayEnrollmentsButtonPanel.add(displayEnrollmentsButton);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new GridLayout(4,1));
		returnPanel.add(studentIDPanel);
		returnPanel.add(semesterPanel);
		returnPanel.add(yearPanel);
		returnPanel.add(displayEnrollmentsButtonPanel);
		
		return returnPanel;
	}
	
	private JPanel displayEnrollmentsPanel(JTable enrollmentJTable)
	{
		enrollmentJTable.setEnabled(false);
		enrollmentJTable.setFont(new Font("Consolas", Font.BOLD, 48));
		enrollmentJTable.setRowHeight(50);
		
		JButton dropEnrollmentsButton = new JButton("Drop Enrollments");
		dropEnrollmentsButton.setFont(consolasBold84);
		dropEnrollmentsButton.setForeground(Color.MAGENTA);
		dropEnrollmentsButton.addActionListener(new dropEnrollmentsButtonListener());
		
		JPanel dropEnrollmentsButtonPanel = new JPanel();
		dropEnrollmentsButtonPanel.add(dropEnrollmentsButton);
		
		JPanel displayEnrollmentsPanel = new JPanel();
		displayEnrollmentsPanel.setBackground(Color.BLACK);
		displayEnrollmentsPanel.setLayout(new BorderLayout());
		displayEnrollmentsPanel.add(new JScrollPane(enrollmentJTable), BorderLayout.CENTER);
		displayEnrollmentsPanel.add(dropEnrollmentsButtonPanel, BorderLayout.SOUTH);
		
		return displayEnrollmentsPanel;
	}
	
	private JPanel selectStudentPanel()
	{
		studentIDTextField = new JTextField(4);
		studentIDTextField.setFont(consolasBold84);
		
		JLabel selectStudentLabel = new JLabel("Select a Student to Enroll");
		selectStudentLabel.setFont(consolasBold84);
		selectStudentLabel.setForeground(Color.MAGENTA);
		selectStudentLabel.setBackground(Color.BLACK);
		
		JPanel selectStudentLabelPanel = new JPanel();
		selectStudentLabelPanel.setBackground(Color.BLACK);
		selectStudentLabelPanel.add(selectStudentLabel);
		
		JLabel studentIDLabel = new JLabel("Student ID:");
		studentIDLabel.setFont(consolasBold84);
		studentIDLabel.setForeground(Color.MAGENTA);
		studentIDLabel.setBackground(Color.BLACK);
		
		JPanel studentIDLabelPanel = new JPanel();
		studentIDLabelPanel.setBackground(Color.BLACK);
		studentIDLabelPanel.add(studentIDLabel);
		
		JPanel studentIDTextFieldPanel = new JPanel();
		studentIDTextFieldPanel.setBackground(Color.BLACK);
		studentIDTextFieldPanel.add(studentIDTextField);
		
		JPanel studentIDPanel = new JPanel();
		studentIDPanel.setBackground(Color.BLACK);
		studentIDPanel.add(studentIDLabelPanel);
		studentIDPanel.add(studentIDTextFieldPanel);
		
		JButton selectStudentButton = new JButton("Select Student");
		selectStudentButton.setFont(consolasBold84);
		selectStudentButton.setForeground(Color.MAGENTA);
		selectStudentButton.addActionListener(new selectStudentButtonListener());
		
		JPanel selectStudentButtonPanel = new JPanel();
		selectStudentButtonPanel.setBackground(Color.BLACK);
		selectStudentButtonPanel.add(selectStudentButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(studentIDPanel, BorderLayout.NORTH);
		centerPanel.add(selectStudentButtonPanel, BorderLayout.CENTER);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(selectStudentLabelPanel, BorderLayout.NORTH);
		returnPanel.add(centerPanel, BorderLayout.CENTER);
		
		return returnPanel;
	}
	
	private JPanel selectCoursePanel()
	{
		JLabel selectCourseLabel = new JLabel("Select a Course to Enroll " + studentFound.getFirstName() + " " + studentFound.getLastName());
		selectCourseLabel.setFont(consolasBold84);
		selectCourseLabel.setForeground(Color.MAGENTA);
		selectCourseLabel.setBackground(Color.BLACK);
		
		JPanel selectCourseLabelPanel = new JPanel();
		selectCourseLabelPanel.setBackground(Color.BLACK);
		selectCourseLabelPanel.add(selectCourseLabel);
		
		JLabel departmentLabel = new JLabel("Department:");
		departmentLabel.setFont(consolasBold84);
		departmentLabel.setForeground(Color.MAGENTA);
		departmentLabel.setBackground(Color.BLACK);
		
		JPanel departmentLabelPanel = new JPanel();
		departmentLabelPanel.setBackground(Color.BLACK);
		departmentLabelPanel.add(departmentLabel);
		
		JPanel departmentsComboBoxPanel = new JPanel();
		departmentsComboBoxPanel.setBackground(Color.BLACK);
		departmentsComboBoxPanel.add(departmentsComboBox);
		
		JLabel courseNameLabel = new JLabel("Course Name:");
		courseNameLabel.setFont(consolasBold84);
		courseNameLabel.setForeground(Color.MAGENTA);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JPanel coursesComboBoxPanel = new JPanel();
		coursesComboBoxPanel.setBackground(Color.BLACK);
		coursesComboBoxPanel.add(coursesComboBox);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2,2));
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(departmentLabelPanel);
		centerPanel.add(departmentsComboBoxPanel);
		centerPanel.add(courseNameLabelPanel);
		centerPanel.add(coursesComboBoxPanel);
		
		JButton selectCourseButton = new JButton("Select Course");
		selectCourseButton.setFont(consolasBold84);
		selectCourseButton.setForeground(Color.MAGENTA);
		selectCourseButton.addActionListener(new selectCourseButtonListener());
		
		JPanel selectCourseButtonPanel = new JPanel();
		selectCourseButtonPanel.setBackground(Color.BLACK);
		selectCourseButtonPanel.add(selectCourseButton);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(selectCourseLabelPanel, BorderLayout.NORTH);
		returnPanel.add(centerPanel, BorderLayout.CENTER);
		returnPanel.add(selectCourseButtonPanel, BorderLayout.SOUTH);
		
		return returnPanel;
	}
	
	private JPanel addEnrollmentPanel()
	{
		
		JLabel studentIDLabel = new JLabel("Student ID:");
		studentIDLabel.setFont(consolasBold84);
		studentIDLabel.setForeground(Color.MAGENTA);
		studentIDLabel.setBackground(Color.BLACK);
		
		JPanel studentIDLabelPanel = new JPanel();
		studentIDLabelPanel.setBackground(Color.BLACK);
		studentIDLabelPanel.add(studentIDLabel);
		
		JTextField studentIDTextField = new JTextField(4);
		studentIDTextField.setText(Integer.toString(studentFound.getStudentID()));
		studentIDTextField.setEnabled(false);
		studentIDTextField.setFont(consolasBold84);
		
		JPanel studentIDTextFieldPanel = new JPanel();
		studentIDTextFieldPanel.setBackground(Color.BLACK);
		studentIDTextFieldPanel.add(studentIDTextField);
		
		JLabel studentNameLabel = new JLabel("Student Name:");
		studentNameLabel.setFont(consolasBold84);
		studentNameLabel.setForeground(Color.MAGENTA);
		studentNameLabel.setBackground(Color.BLACK);
		
		JPanel studentNameLabelPanel = new JPanel();
		studentNameLabelPanel.setBackground(Color.BLACK);
		studentNameLabelPanel.add(studentNameLabel);
		
		JTextField studentNameTextField = new JTextField(40);
		studentNameTextField.setText(studentFound.getFirstName() + " " + studentFound.getLastName());
		studentNameTextField.setEnabled(false);
		studentNameTextField.setFont(consolasBold84);
		
		JPanel studentNameTextFieldPanel = new JPanel();
		studentNameTextFieldPanel.setBackground(Color.BLACK);
		studentNameTextFieldPanel.add(studentNameTextField);
		
		JPanel studentIDPanel = new JPanel();
		studentIDPanel.setBackground(Color.BLACK);
		studentIDPanel.add(studentIDLabelPanel);
		studentIDPanel.add(studentIDTextFieldPanel);
		
		JPanel studentNamePanel = new JPanel();
		studentNamePanel.setBackground(Color.BLACK);
		studentNamePanel.add(studentNameLabelPanel);
		studentNamePanel.add(studentNameTextFieldPanel);
		
		JPanel studentPanel = new JPanel();
		studentPanel.setBackground(Color.BLACK);
		studentPanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 6));
		studentPanel.setLayout(new GridLayout(2,1));
		studentPanel.add(studentIDPanel);
		studentPanel.add(studentNamePanel);
		
		JLabel courseNameLabel = new JLabel("Course Name:");
		courseNameLabel.setFont(consolasBold84);
		courseNameLabel.setForeground(Color.MAGENTA);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JTextField courseNameTextField = new JTextField(20);
		courseNameTextField.setText(courseFound.getCourseName());
		courseNameTextField.setEnabled(false);
		courseNameTextField.setFont(consolasBold84);
		
		JPanel courseNameTextFieldPanel = new JPanel();
		courseNameTextFieldPanel.setBackground(Color.BLACK);
		courseNameTextFieldPanel.add(courseNameTextField);
		
		JLabel professorNameLabel = new JLabel("Professor Name:");
		professorNameLabel.setFont(consolasBold84);
		professorNameLabel.setForeground(Color.MAGENTA);
		professorNameLabel.setBackground(Color.BLACK);
		
		JPanel professorNameLabelPanel = new JPanel();
		professorNameLabelPanel.setBackground(Color.BLACK);
		professorNameLabelPanel.add(professorNameLabel);
		
		JTextField professorNameTextField = new JTextField(40);
		professorNameTextField.setText(courseFound.getProfessor().getFirstName() + " " + courseFound.getProfessor().getLastName());
		professorNameTextField.setEnabled(false);
		professorNameTextField.setFont(new Font("Consolas", Font.BOLD, 80));
		
		JPanel professorNameTextFieldPanel = new JPanel();
		professorNameTextFieldPanel.setBackground(Color.BLACK);
		professorNameTextFieldPanel.add(professorNameTextField);
		
		JPanel courseNamePanel = new JPanel();
		courseNamePanel.setBackground(Color.BLACK);
		courseNamePanel.add(courseNameLabelPanel);
		courseNamePanel.add(courseNameTextFieldPanel);
		
		JPanel professorNamePanel = new JPanel();
		professorNamePanel.setBackground(Color.BLACK);
		professorNamePanel.add(professorNameLabelPanel);
		professorNamePanel.add(professorNameTextFieldPanel);
		
		JPanel coursePanel = new JPanel();
		coursePanel.setBackground(Color.BLACK);
		coursePanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 6));
		coursePanel.setLayout(new GridLayout(2,1));
		coursePanel.add(courseNamePanel);
		coursePanel.add(professorNamePanel);
		
		JLabel semesterLabel = new JLabel("Semester:");
		semesterLabel.setFont(consolasBold84);
		semesterLabel.setForeground(Color.MAGENTA);
		semesterLabel.setBackground(Color.BLACK);
		
		JPanel semesterLabelPanel = new JPanel();
		semesterLabelPanel.setBackground(Color.BLACK);
		semesterLabelPanel.add(semesterLabel);
		
		semestersComboBox = new JComboBox<String>(semesters);
		semestersComboBox.setFont(consolasBold84);
		
		JPanel semestersComboBoxPanel = new JPanel();
		semestersComboBoxPanel.setBackground(Color.BLACK);
		semestersComboBoxPanel.add(semestersComboBox);
		
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setFont(consolasBold84);
		yearLabel.setForeground(Color.MAGENTA);
		yearLabel.setBackground(Color.BLACK);
		
		JPanel yearLabelPanel = new JPanel();
		yearLabelPanel.setBackground(Color.BLACK);
		yearLabelPanel.add(yearLabel);
		
		yearsComboBox = new JComboBox<Integer>(years);
		yearsComboBox.setFont(consolasBold84);
		
		JPanel yearsComboBoxPanel = new JPanel();
		yearsComboBoxPanel.setBackground(Color.BLACK);
		yearsComboBoxPanel.add(yearsComboBox);
		
		JPanel semesterPanel = new JPanel();
		semesterPanel.setBackground(Color.BLACK);
		semesterPanel.add(semesterLabelPanel);
		semesterPanel.add(semestersComboBoxPanel);
		
		JPanel yearPanel = new JPanel();
		yearPanel.setBackground(Color.BLACK);
		yearPanel.add(yearLabelPanel);
		yearPanel.add(yearsComboBoxPanel);
		
		JPanel timePanel = new JPanel();
		timePanel.setBackground(Color.BLACK);
		timePanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 6));
		timePanel.setLayout(new GridLayout(2,1));
		timePanel.add(semesterPanel);
		timePanel.add(yearPanel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.setLayout(new GridLayout(3,1));
		centerPanel.add(studentPanel);
		centerPanel.add(coursePanel);
		centerPanel.add(timePanel);
		
		JButton enrollButton = new JButton("Enroll Student");
		enrollButton.setFont(consolasBold84);
		enrollButton.setForeground(Color.MAGENTA);
		enrollButton.addActionListener(new enrollButtonListener());
		
		JPanel enrollButtonPanel = new JPanel();
		enrollButtonPanel.setBackground(Color.BLACK);
		enrollButtonPanel.add(enrollButton);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(centerPanel, BorderLayout.CENTER);
		returnPanel.add(enrollButtonPanel, BorderLayout.SOUTH);
		
		return returnPanel;
	}
	
	private JPanel foundEnrollmentPanel()
	{
		JLabel studentNameLabel = new JLabel("Student Name:");
		studentNameLabel.setFont(consolasBold84);
		studentNameLabel.setForeground(Color.MAGENTA);
		studentNameLabel.setBackground(Color.BLACK);
		
		JPanel studentNameLabelPanel = new JPanel();
		studentNameLabelPanel.setBackground(Color.BLACK);
		studentNameLabelPanel.add(studentNameLabel);
		
		JTextField studentNameTextField = new JTextField(40);
		studentNameTextField.setText(enrollmentFound.getStudent().getFirstName() + " " + enrollmentFound.getStudent().getLastName());
		studentNameTextField.setEditable(false);
		studentNameTextField.setFont(consolasBold84);
		
		JPanel studentNameTextFieldPanel = new JPanel();
		studentNameTextFieldPanel.setBackground(Color.BLACK);
		studentNameTextFieldPanel.add(studentNameTextField);
		
		JPanel studentPanel = new JPanel();
		studentPanel.setBackground(Color.BLACK);
		studentPanel.add(studentNameLabelPanel);
		studentPanel.add(studentNameTextFieldPanel);
		
		JLabel courseNameLabel = new JLabel("Course Name:");
		courseNameLabel.setFont(consolasBold84);
		courseNameLabel.setForeground(Color.MAGENTA);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JTextField courseNameTextField = new JTextField(20);
		courseNameTextField.setText(enrollmentFound.getCourse().getCourseName());
		courseNameTextField.setEditable(false);
		courseNameTextField.setFont(consolasBold84);
		
		JPanel courseNameTextFieldPanel = new JPanel();
		courseNameTextFieldPanel.setBackground(Color.BLACK);
		courseNameTextFieldPanel.add(courseNameTextField);
		
		JPanel coursePanel = new JPanel();
		coursePanel.setBackground(Color.BLACK);
		coursePanel.add(courseNameLabelPanel);
		coursePanel.add(courseNameTextFieldPanel);
		
		JLabel professorNameLabel = new JLabel("Professor Name:");
		professorNameLabel.setFont(consolasBold84);
		professorNameLabel.setForeground(Color.MAGENTA);
		professorNameLabel.setBackground(Color.BLACK);
		
		JPanel professorNameLabelPanel = new JPanel();
		professorNameLabelPanel.setBackground(Color.BLACK);
		professorNameLabelPanel.add(professorNameLabel);
		
		JTextField professorNameTextField = new JTextField(40);
		professorNameTextField.setText(enrollmentFound.getCourse().getProfessor().getFirstName() + " " + enrollmentFound.getCourse().getProfessor().getLastName());
		professorNameTextField.setEditable(false);
		professorNameTextField.setFont(new Font("Consolas", Font.BOLD, 80));
		
		JPanel professorNameTextFieldPanel = new JPanel();
		professorNameTextFieldPanel.setBackground(Color.BLACK);
		professorNameTextFieldPanel.add(professorNameTextField);
		
		JPanel professorPanel = new JPanel();
		professorPanel.setBackground(Color.BLACK);
		professorPanel.add(professorNameLabelPanel);
		professorPanel.add(professorNameTextFieldPanel);
		
		JLabel semesterLabel = new JLabel("Semester:");
		semesterLabel.setFont(consolasBold84);
		semesterLabel.setForeground(Color.MAGENTA);
		semesterLabel.setBackground(Color.BLACK);
		
		JPanel semesterLabelPanel = new JPanel();
		semesterLabelPanel.setBackground(Color.BLACK);
		semesterLabelPanel.add(semesterLabel);
		
		JTextField semesterTextField = new JTextField(6);
		semesterTextField.setText(enrollmentFound.getSemester());
		semesterTextField.setEditable(false);
		semesterTextField.setFont(consolasBold84);
		
		JPanel semesterTextFieldPanel = new JPanel();
		semesterTextFieldPanel.setBackground(Color.BLACK);
		semesterTextFieldPanel.add(semesterTextField);
		
		JPanel semesterPanel = new JPanel();
		semesterPanel.setBackground(Color.BLACK);
		semesterPanel.add(semesterLabelPanel);
		semesterPanel.add(semesterTextFieldPanel);
		
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setFont(consolasBold84);
		yearLabel.setForeground(Color.MAGENTA);
		yearLabel.setBackground(Color.BLACK);
		
		JPanel yearLabelPanel = new JPanel();
		yearLabelPanel.setBackground(Color.BLACK);
		yearLabelPanel.add(yearLabel);
		
		JTextField yearTextField = new JTextField(4);
		yearTextField.setText(Integer.toString(enrollmentFound.getAcademicYear()));
		yearTextField.setEditable(false);
		yearTextField.setFont(consolasBold84);
		
		JPanel yearTextFieldPanel = new JPanel();
		yearTextFieldPanel.setBackground(Color.BLACK);
		yearTextFieldPanel.add(yearTextField);
		
		JPanel yearPanel = new JPanel();
		yearPanel.setBackground(Color.BLACK);
		yearPanel.add(yearLabelPanel);
		yearPanel.add(yearTextFieldPanel);
		
		JButton dropEnrollmentButton = new JButton("Drop Enrollment");
		dropEnrollmentButton.setFont(consolasBold84);
		dropEnrollmentButton.setForeground(Color.MAGENTA);
		dropEnrollmentButton.addActionListener(new dropEnrollmentButtonListener());
		
		JPanel dropEnrollmentButtonPanel = new JPanel();
		dropEnrollmentButtonPanel.setBackground(Color.BLACK);
		dropEnrollmentButtonPanel.add(dropEnrollmentButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.setLayout(new GridLayout(5,1));
		centerPanel.add(studentPanel);
		centerPanel.add(professorPanel);
		centerPanel.add(coursePanel);
		centerPanel.add(semesterPanel);
		centerPanel.add(yearPanel);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(centerPanel, BorderLayout.CENTER);
		returnPanel.add(dropEnrollmentButtonPanel, BorderLayout.SOUTH);
		
		return returnPanel;
	}
}