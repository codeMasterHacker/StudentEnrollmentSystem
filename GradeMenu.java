import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GradeMenu
{
	private JFrame main;
	
	private Font consolasBold84;
	
	private Enrollment enrollmentFound;
	
	private JMenuItem manageGradeMenuItem;
	
	private JTextField enrollmentIDTextField;
	
	private String[] grades = {"A", "B", "C", "D", "F"};
	
	private JComboBox<String> gradesComboBox;
	
	public JMenuItem getManageGradeMenuItem() {return manageGradeMenuItem;}
	
	public GradeMenu(JFrame parent)
	{
		main = parent;
		
		consolasBold84 = new Font("Consolas", Font.BOLD, 84);
		
		manageGradeMenuItem = new JMenuItem("Manage Grade");
		manageGradeMenuItem.addActionListener(new manageGradeMenuItemListener());
	}
	
	private class manageGradeMenuItemListener implements ActionListener
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
	
	private class editGradeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			main.getContentPane().removeAll();
			main.add(editGradePanel());
			main.setVisible(true);
		}
	}
	
	private class updateGradeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			enrollmentFound.setGrade((String)gradesComboBox.getSelectedItem());
			
			if (EnrollmentDatabase.editGrade(enrollmentFound))
			{
				main.getContentPane().removeAll();
				main.add(searchEnrollmentPanel());
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "The grade has been successfully updated.");
			}
			else
				JOptionPane.showMessageDialog(null, "ERROR: Couldn't update grade.");
		}
	}
	
	private JPanel searchEnrollmentPanel()
	{
		JLabel enrollmentIDLabel = new JLabel("Enrollment ID:");
		enrollmentIDLabel.setFont(consolasBold84);
		enrollmentIDLabel.setForeground(Color.ORANGE);
		enrollmentIDLabel.setBackground(Color.BLACK);
		
		enrollmentIDTextField = new JTextField(4);
		enrollmentIDTextField.setFont(consolasBold84);
		
		JPanel studentIDLabelAndTextFieldPanel = new JPanel();
		studentIDLabelAndTextFieldPanel.setBackground(Color.BLACK);
		studentIDLabelAndTextFieldPanel.add(enrollmentIDLabel);
		studentIDLabelAndTextFieldPanel.add(enrollmentIDTextField);
		
		JButton searchStudentIDForEnrollmentButton = new JButton("Search Enrollemnt");
		searchStudentIDForEnrollmentButton.setFont(consolasBold84);
		searchStudentIDForEnrollmentButton.setForeground(Color.BLACK);
		searchStudentIDForEnrollmentButton.addActionListener(new searchEnrollmentButtonListener());
		
		JPanel searchStudentIDForEnrollmentButtonPanel = new JPanel();
		searchStudentIDForEnrollmentButtonPanel.setBackground(Color.BLACK);
		searchStudentIDForEnrollmentButtonPanel.add(searchStudentIDForEnrollmentButton);
		
		JPanel searchEnrollmentPanel = new JPanel();	
		searchEnrollmentPanel.setBackground(Color.BLACK);
		searchEnrollmentPanel.setLayout(new BorderLayout());
		searchEnrollmentPanel.add(studentIDLabelAndTextFieldPanel, BorderLayout.NORTH);
		searchEnrollmentPanel.add(searchStudentIDForEnrollmentButtonPanel, BorderLayout.CENTER);
		
		return searchEnrollmentPanel;
	}
	
	private JPanel foundEnrollmentPanel()
	{
		JLabel studentNameLabel = new JLabel("Student Name:");
		studentNameLabel.setFont(consolasBold84);
		studentNameLabel.setForeground(Color.ORANGE);
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
		courseNameLabel.setForeground(Color.ORANGE);
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
		professorNameLabel.setForeground(Color.ORANGE);
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
		semesterLabel.setForeground(Color.ORANGE);
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
		yearLabel.setForeground(Color.ORANGE);
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
		
		JLabel gradeLabel = new JLabel("Grade:");
		gradeLabel.setFont(consolasBold84);
		gradeLabel.setBackground(Color.BLACK);
		gradeLabel.setForeground(Color.ORANGE);
		
		JPanel gradeLabelPanel = new JPanel();
		gradeLabelPanel.setBackground(Color.BLACK);
		gradeLabelPanel.add(gradeLabel);
		
		JTextField gradeTextField = new JTextField(1);
		gradeTextField.setText(enrollmentFound.getGrade());
		gradeTextField.setEditable(false);
		gradeTextField.setFont(consolasBold84);
		
		JPanel gradeTextFieldPanel = new JPanel();
		gradeTextFieldPanel.setBackground(Color.BLACK);
		gradeTextFieldPanel.add(gradeTextField);
		
		JPanel gradePanel = new JPanel();
		gradePanel.setBackground(Color.BLACK);
		gradePanel.add(gradeLabelPanel);
		gradePanel.add(gradeTextFieldPanel);
		
		JButton editGradeButton = new JButton("Edit Grade");
		editGradeButton.setFont(consolasBold84);
		editGradeButton.setForeground(Color.BLACK);
		editGradeButton.addActionListener(new editGradeButtonListener());
		
		JPanel editGradeButtonPanel = new JPanel();
		editGradeButtonPanel.setBackground(Color.BLACK);
		editGradeButtonPanel.add(editGradeButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.setLayout(new GridLayout(6,1));
		centerPanel.add(studentPanel);
		centerPanel.add(professorPanel);
		centerPanel.add(coursePanel);
		centerPanel.add(semesterPanel);
		centerPanel.add(yearPanel);
		centerPanel.add(gradePanel);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(centerPanel, BorderLayout.CENTER);
		returnPanel.add(editGradeButtonPanel, BorderLayout.SOUTH);
		
		return returnPanel;
	}
	
	private JPanel editGradePanel()
	{
		JLabel studentNameLabel = new JLabel("Student Name:");
		studentNameLabel.setFont(consolasBold84);
		studentNameLabel.setForeground(Color.ORANGE);
		studentNameLabel.setBackground(Color.BLACK);
		
		JPanel studentNameLabelPanel = new JPanel();
		studentNameLabelPanel.setBackground(Color.BLACK);
		studentNameLabelPanel.add(studentNameLabel);
		
		JTextField studentNameTextField = new JTextField(40);
		studentNameTextField.setText(enrollmentFound.getStudent().getFirstName() + " " + enrollmentFound.getStudent().getLastName());
		studentNameTextField.setEnabled(false);
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
		courseNameLabel.setForeground(Color.ORANGE);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JTextField courseNameTextField = new JTextField(20);
		courseNameTextField.setText(enrollmentFound.getCourse().getCourseName());
		courseNameTextField.setEnabled(false);
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
		professorNameLabel.setForeground(Color.ORANGE);
		professorNameLabel.setBackground(Color.BLACK);
		
		JPanel professorNameLabelPanel = new JPanel();
		professorNameLabelPanel.setBackground(Color.BLACK);
		professorNameLabelPanel.add(professorNameLabel);
		
		JTextField professorNameTextField = new JTextField(40);
		professorNameTextField.setText(enrollmentFound.getCourse().getProfessor().getFirstName() + " " + enrollmentFound.getCourse().getProfessor().getLastName());
		professorNameTextField.setEnabled(false);
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
		semesterLabel.setForeground(Color.ORANGE);
		semesterLabel.setBackground(Color.BLACK);
		
		JPanel semesterLabelPanel = new JPanel();
		semesterLabelPanel.setBackground(Color.BLACK);
		semesterLabelPanel.add(semesterLabel);
		
		JTextField semesterTextField = new JTextField(6);
		semesterTextField.setText(enrollmentFound.getSemester());
		semesterTextField.setEnabled(false);
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
		yearLabel.setForeground(Color.ORANGE);
		yearLabel.setBackground(Color.BLACK);
		
		JPanel yearLabelPanel = new JPanel();
		yearLabelPanel.setBackground(Color.BLACK);
		yearLabelPanel.add(yearLabel);
		
		JTextField yearTextField = new JTextField(4);
		yearTextField.setText(Integer.toString(enrollmentFound.getAcademicYear()));
		yearTextField.setEnabled(false);
		yearTextField.setFont(consolasBold84);
		
		JPanel yearTextFieldPanel = new JPanel();
		yearTextFieldPanel.setBackground(Color.BLACK);
		yearTextFieldPanel.add(yearTextField);
		
		JPanel yearPanel = new JPanel();
		yearPanel.setBackground(Color.BLACK);
		yearPanel.add(yearLabelPanel);
		yearPanel.add(yearTextFieldPanel);
		
		JLabel gradeLabel = new JLabel("Grade:");
		gradeLabel.setFont(consolasBold84);
		gradeLabel.setBackground(Color.BLACK);
		gradeLabel.setForeground(Color.ORANGE);
		
		JPanel gradeLabelPanel = new JPanel();
		gradeLabelPanel.setBackground(Color.BLACK);
		gradeLabelPanel.add(gradeLabel);
		
		gradesComboBox = new JComboBox<String>(grades);
		gradesComboBox.setFont(consolasBold84);
		
		JPanel gradeComboBoxPanel = new JPanel();
		gradeComboBoxPanel.setBackground(Color.BLACK);
		gradeComboBoxPanel.add(gradesComboBox);
		
		JPanel gradePanel = new JPanel();
		gradePanel.setBackground(Color.BLACK);
		gradePanel.add(gradeLabelPanel);
		gradePanel.add(gradeComboBoxPanel);
		
		JButton updateGradeButton = new JButton("Update Grade");
		updateGradeButton.setFont(consolasBold84);
		updateGradeButton.setForeground(Color.BLACK);
		updateGradeButton.addActionListener(new updateGradeButtonListener());
		
		JPanel updateGradeButtonPanel = new JPanel();
		updateGradeButtonPanel.setBackground(Color.BLACK);
		updateGradeButtonPanel.add(updateGradeButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.setLayout(new GridLayout(6,1));
		centerPanel.add(studentPanel);
		centerPanel.add(professorPanel);
		centerPanel.add(coursePanel);
		centerPanel.add(semesterPanel);
		centerPanel.add(yearPanel);
		centerPanel.add(gradePanel);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(centerPanel, BorderLayout.CENTER);
		returnPanel.add(updateGradeButtonPanel, BorderLayout.SOUTH);
		
		return returnPanel;
	}
}