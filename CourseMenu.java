import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CourseMenu
{
private JFrame main;
	
	private Course courseFound;
	
	private Font consolasBold84;
	
	private JMenuItem addCourseMenuItem;
	private JMenuItem searchCourseMenuItem;
	private JMenuItem displayCoursesMenuItem;
	
	private JTextField courseNameTextField;
	
	private String[] campuses = {
			"West Los Angeles College",
			"East Los Angeles College",
			"Los Angeles City College",
			"Los Angeles Harbor College", 
			"Los Angeles Mission College", 
			"Los Angeles Pierce College", 
			"Los Angeles Southwest College", 
			"Los Angeles Trade-Tech College", 
			"Los Angeles Valley College", 
			};
	
	
	private JComboBox<String> campusComboBox;
	private JComboBox<String> departmentsComboBox;
	private JComboBox<String> professorsComboBox;
	
	private JPanel addCoursePanel;
	private JPanel departmentSelectorPanel;
	private JPanel searchCoursePanel;
	private JPanel foundCoursePanel;
	private JPanel editCoursePanel;
	
	public JMenuItem getAddCourseMenuItem() { return addCourseMenuItem; }
	public JMenuItem getSearchCourseMenuItem() { return searchCourseMenuItem; }	
	public JMenuItem getDisplayCoursesMenuItem() { return displayCoursesMenuItem; }
	
	public CourseMenu(JFrame parent)
	{
		main = parent;
		
		consolasBold84 = new Font("Consolas", Font.BOLD, 84);
		
		addCourseMenuItem = new JMenuItem("Add Course");
		addCourseMenuItem.addActionListener(new addCourseMenuItemListener());
		
		searchCourseMenuItem = new JMenuItem("Search Course");
		searchCourseMenuItem.addActionListener(new searchCourseMenuItemListener());
		
		displayCoursesMenuItem = new JMenuItem("Display Courses");
		displayCoursesMenuItem.addActionListener(new displayCoursesMenuItemListener());
	}
	
	private class addCourseMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (DepartmentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no departments in the database yet. Departments are a prerequisite. You need departments to add courses. Add departments to the database.");
			else if (ProfessorDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no professors in the database yet. Professors are a prerequisite. You need professors to add courses. Add profesors to the database.");
			else
			{
				departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
				departmentsComboBox.setFont(consolasBold84);
				departmentsComboBox.addActionListener(new departmentsComboBoxListener());
				
				addCoursePanel();
				
				main.getContentPane().removeAll();
				main.add(addCoursePanel);
				main.setVisible(true);
			}
		}
	}
	
	private class searchCourseMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (CourseDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no courses in the database yet. Add courses to the database.");
			else
			{
				searchCoursePanel();
				
				main.getContentPane().removeAll();
				main.add(searchCoursePanel);
				main.setVisible(true);
			}
		}		
	}
	
	private class displayCoursesMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (CourseDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no courses in the databae yet. Add courses to the database.");
			else
			{
				departmentSelectorPanel();
				
				main.getContentPane().removeAll();
				main.add(departmentSelectorPanel);
				main.setVisible(true);
			}
		}
	}
	
	private class displayCoursesButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JTable courseJTable = CourseDatabase.getCourseJTable((String)departmentsComboBox.getSelectedItem());
			
			if (courseJTable != null)
			{
				courseJTable.setEnabled(false);
				courseJTable.setFont(new Font("Consolas", Font.BOLD, 48));
				courseJTable.setRowHeight(50);
				
				main.getContentPane().removeAll();
				main.add(new JScrollPane(courseJTable));
				main.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "There are no courses that belong to that department in the database yet. Add courses to the database.");
		}
	}
	
	private class addCourseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String professorInfo = (String)professorsComboBox.getSelectedItem();
			int index = professorInfo.indexOf(" ");
			int professorID = Integer.parseInt(professorInfo.substring(0, index));
			
			String departmentName = (String)departmentsComboBox.getSelectedItem();
			String courseName = courseNameTextField.getText().trim();
			String campus = (String)campusComboBox.getSelectedItem();
			Department department = DepartmentDatabase.readDepartment(departmentName);	
			Professor professor =  ProfessorDatabase.readProfessor(professorID);
			
			if (courseName.isEmpty())
				JOptionPane.showMessageDialog(null, "Course Name field is empty.\nFill in the empty field.");
			else
			{
				if (CourseDatabase.writeCourse(new Course(courseName, campus, department, professor)))
				{
					departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
					departmentsComboBox.setFont(consolasBold84);
					departmentsComboBox.addActionListener(new departmentsComboBoxListener());
					departmentsComboBox.setSelectedItem(departmentName);
					
					String[] professorsInfo = ProfessorDatabase.getProfessorsInfo((String)departmentsComboBox.getSelectedItem());
					professorsComboBox = new JComboBox<String>(professorsInfo);
					professorsComboBox.setFont(consolasBold84);
					professorsComboBox.setSelectedItem(professorInfo);
					
					campusComboBox = new JComboBox<String>(campuses);
					campusComboBox.setFont(new Font("Consolas", Font.BOLD, 72));
					campusComboBox.setSelectedItem(campus);
					
					main.getContentPane().removeAll();
					main.add(afterAddCoursePanel());
					main.setVisible(true);
					
					JOptionPane.showMessageDialog(null, "The course has been successfully added.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't write course to database.");
			}
		}
	}
	
	private class searchCourseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String courseName = courseNameTextField.getText().trim();
			
			if (courseName.isEmpty())
				JOptionPane.showMessageDialog(null, "Textfield is empty. Re-enter.");
			else
			{
				Course course = CourseDatabase.readCourse(courseName);
				
				if (course != null)
				{
					courseFound = course;
					
					foundCoursePanel();
					
					main.getContentPane().removeAll();
					main.add(foundCoursePanel);
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Couldn't find the course with Course Name: " + courseName);
			}
		}
	}
	
	private class deleteCourseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String courseName = courseFound.getCourseName();
			
			if(CourseDatabase.deleteCourse(courseName))
			{
				searchCoursePanel();
				
				main.getContentPane().removeAll();
				main.add(searchCoursePanel);
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "The course has been successfully deleted.");
			}
			else
				JOptionPane.showMessageDialog(null, "Couldn't find the course with Course Name: " + courseName);
		}
	}
	
	private class editCourseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			editCoursePanel(false);
			
			main.getContentPane().removeAll();
			main.add(editCoursePanel);
			main.setVisible(true);
		}
	}
	
	private class saveChangesCourseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String professorInfo = (String)professorsComboBox.getSelectedItem();
			int index = professorInfo.indexOf(" ");
			int professorID = Integer.parseInt(professorInfo.substring(0, index));
			
			String campus = (String)campusComboBox.getSelectedItem();
			Department department = DepartmentDatabase.readDepartment((String)departmentsComboBox.getSelectedItem());	
			Professor professor =  ProfessorDatabase.readProfessor(professorID);
			
			courseFound.setCampus(campus);
			courseFound.setDepartment(department);
			courseFound.setProfessor(professor);
			
			if (CourseDatabase.editCourse(courseFound))
			{
				searchCoursePanel();
				
				main.getContentPane().removeAll();
				main.add(searchCoursePanel);
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "Course has been successfully updated.");
			}
			else
				JOptionPane.showMessageDialog(null, "ERROR: Couldn't update course to database.");
		}
	}
	
	private class departmentComboBoxListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			editCoursePanel(true);
			
			main.getContentPane().removeAll();
			main.add(editCoursePanel);
			main.setVisible(true);
		}
	}
	
	private class departmentsComboBoxListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			addCoursePanel();
			
			main.getContentPane().removeAll();
			main.add(addCoursePanel);
			main.setVisible(true);
		}	
	}
	
	private void addCoursePanel()
	{
		String[] professorsInfo = ProfessorDatabase.getProfessorsInfo((String)departmentsComboBox.getSelectedItem());
		professorsComboBox = new JComboBox<String>(professorsInfo);
		professorsComboBox.setFont(consolasBold84);
		
		courseNameTextField = new JTextField(20);
		courseNameTextField.setFont(consolasBold84);
		
		campusComboBox = new JComboBox<String>(campuses);
		campusComboBox.setFont(new Font("Consolas", Font.BOLD, 72));
		
		JLabel courseDepartmentNameLabel = new JLabel("Department:");
		courseDepartmentNameLabel.setFont(consolasBold84);
		courseDepartmentNameLabel.setForeground(Color.CYAN);
		courseDepartmentNameLabel.setBackground(Color.BLACK);

		JPanel courseDepartmentNameLabelPanel = new JPanel();
		courseDepartmentNameLabelPanel.setBackground(Color.BLACK);
		courseDepartmentNameLabelPanel.add(courseDepartmentNameLabel);
		
		JPanel courseDepartmentNameComboBoxesPanel = new JPanel();
		courseDepartmentNameComboBoxesPanel.setBackground(Color.BLACK);
		courseDepartmentNameComboBoxesPanel.add(departmentsComboBox);
		
		JLabel courseNameLabel = new JLabel("Course Name:");
		courseNameLabel.setFont(consolasBold84);
		courseNameLabel.setForeground(Color.CYAN);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JPanel courseNameTextFieldPanel = new JPanel();
		courseNameTextFieldPanel.setBackground(Color.BLACK);
		courseNameTextFieldPanel.add(courseNameTextField);
		
		JLabel courseCampusNameLabel = new JLabel("Campus:");
		courseCampusNameLabel.setFont(consolasBold84);
		courseCampusNameLabel.setForeground(Color.CYAN);
		courseCampusNameLabel.setBackground(Color.BLACK);
		
		JPanel courseCampusNameLabelPanel = new JPanel();
		courseCampusNameLabelPanel.setBackground(Color.BLACK);
		courseCampusNameLabelPanel.add(courseCampusNameLabel);
		
		JPanel courseCampusComboBoxPanel = new JPanel();
		courseCampusComboBoxPanel.setBackground(Color.BLACK);
		courseCampusComboBoxPanel.add(campusComboBox);
		
		JLabel courseInstructorNameLabel = new JLabel("Professor:");
		courseInstructorNameLabel.setFont(consolasBold84);
		courseInstructorNameLabel.setForeground(Color.CYAN);
		courseInstructorNameLabel.setBackground(Color.BLACK);
		
		JPanel courseInstructorNameLabelPanel = new JPanel();
		courseInstructorNameLabelPanel.setBackground(Color.BLACK);
		courseInstructorNameLabelPanel.add(courseInstructorNameLabel);
		
		JPanel courseInstructorNameComboBoxesPanel = new JPanel();
		courseInstructorNameComboBoxesPanel.setBackground(Color.BLACK);
		courseInstructorNameComboBoxesPanel.add(professorsComboBox);
		
		JPanel addCourseLabelsAndComboBoxesPanel = new JPanel();
		addCourseLabelsAndComboBoxesPanel.setBackground(Color.BLACK);
		addCourseLabelsAndComboBoxesPanel.setLayout(new GridLayout(4,2));
		addCourseLabelsAndComboBoxesPanel.add(courseDepartmentNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseDepartmentNameComboBoxesPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseNameTextFieldPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseCampusNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseCampusComboBoxPanel);	
		addCourseLabelsAndComboBoxesPanel.add(courseInstructorNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseInstructorNameComboBoxesPanel);
		
		JButton addCourseButton = new JButton("Add Course");
		addCourseButton.setFont(consolasBold84);
		addCourseButton.setForeground(Color.CYAN);
		addCourseButton.addActionListener(new addCourseButtonListener());
		
		JPanel addCourseButtonPanel = new JPanel();
		addCourseButtonPanel.setBackground(Color.BLACK);
		addCourseButtonPanel.add(addCourseButton);
		
		addCoursePanel = new JPanel();
		addCoursePanel.setBackground(Color.BLACK);
		addCoursePanel.setLayout(new BorderLayout());
		addCoursePanel.add(addCourseLabelsAndComboBoxesPanel, BorderLayout.CENTER);
		addCoursePanel.add(addCourseButtonPanel, BorderLayout.SOUTH);
	}
	
	private JPanel afterAddCoursePanel()
	{
		courseNameTextField = new JTextField(20);
		courseNameTextField.setFont(consolasBold84);
		
		JLabel courseDepartmentNameLabel = new JLabel("Department:");
		courseDepartmentNameLabel.setFont(consolasBold84);
		courseDepartmentNameLabel.setForeground(Color.CYAN);
		courseDepartmentNameLabel.setBackground(Color.BLACK);

		JPanel courseDepartmentNameLabelPanel = new JPanel();
		courseDepartmentNameLabelPanel.setBackground(Color.BLACK);
		courseDepartmentNameLabelPanel.add(courseDepartmentNameLabel);
		
		JPanel courseDepartmentNameComboBoxesPanel = new JPanel();
		courseDepartmentNameComboBoxesPanel.setBackground(Color.BLACK);
		courseDepartmentNameComboBoxesPanel.add(departmentsComboBox);
		
		JLabel courseNameLabel = new JLabel("Course Name:");
		courseNameLabel.setFont(consolasBold84);
		courseNameLabel.setForeground(Color.CYAN);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JPanel courseNameTextFieldPanel = new JPanel();
		courseNameTextFieldPanel.setBackground(Color.BLACK);
		courseNameTextFieldPanel.add(courseNameTextField);
		
		JLabel courseCampusNameLabel = new JLabel("Campus:");
		courseCampusNameLabel.setFont(consolasBold84);
		courseCampusNameLabel.setForeground(Color.CYAN);
		courseCampusNameLabel.setBackground(Color.BLACK);
		
		JPanel courseCampusNameLabelPanel = new JPanel();
		courseCampusNameLabelPanel.setBackground(Color.BLACK);
		courseCampusNameLabelPanel.add(courseCampusNameLabel);
		
		JPanel courseCampusComboBoxPanel = new JPanel();
		courseCampusComboBoxPanel.setBackground(Color.BLACK);
		courseCampusComboBoxPanel.add(campusComboBox);
		
		JLabel courseInstructorNameLabel = new JLabel("Professor:");
		courseInstructorNameLabel.setFont(consolasBold84);
		courseInstructorNameLabel.setForeground(Color.CYAN);
		courseInstructorNameLabel.setBackground(Color.BLACK);
		
		JPanel courseInstructorNameLabelPanel = new JPanel();
		courseInstructorNameLabelPanel.setBackground(Color.BLACK);
		courseInstructorNameLabelPanel.add(courseInstructorNameLabel);
		
		JPanel courseInstructorNameComboBoxesPanel = new JPanel();
		courseInstructorNameComboBoxesPanel.setBackground(Color.BLACK);
		courseInstructorNameComboBoxesPanel.add(professorsComboBox);
		
		JPanel addCourseLabelsAndComboBoxesPanel = new JPanel();
		addCourseLabelsAndComboBoxesPanel.setBackground(Color.BLACK);
		addCourseLabelsAndComboBoxesPanel.setLayout(new GridLayout(4,2));
		addCourseLabelsAndComboBoxesPanel.add(courseDepartmentNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseDepartmentNameComboBoxesPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseNameTextFieldPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseCampusNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseCampusComboBoxPanel);	
		addCourseLabelsAndComboBoxesPanel.add(courseInstructorNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseInstructorNameComboBoxesPanel);
		
		JButton addCourseButton = new JButton("Add Course");
		addCourseButton.setFont(consolasBold84);
		addCourseButton.setForeground(Color.CYAN);
		addCourseButton.addActionListener(new addCourseButtonListener());
		
		JPanel addCourseButtonPanel = new JPanel();
		addCourseButtonPanel.setBackground(Color.BLACK);
		addCourseButtonPanel.add(addCourseButton);
		
		JPanel returnPanel = new JPanel();
		returnPanel.setBackground(Color.BLACK);
		returnPanel.setLayout(new BorderLayout());
		returnPanel.add(addCourseLabelsAndComboBoxesPanel, BorderLayout.CENTER);
		returnPanel.add(addCourseButtonPanel, BorderLayout.SOUTH);
		
		return returnPanel;
	}
	
	private void searchCoursePanel()
	{
		courseNameTextField = new JTextField(20);
		courseNameTextField.setFont(consolasBold84);
		
		JLabel searchCourseLabel = new JLabel("Course Name:");
		searchCourseLabel.setFont(consolasBold84);
		searchCourseLabel.setBackground(Color.BLACK);
		searchCourseLabel.setForeground(Color.CYAN);
		
		JPanel searchCourseLabelAndTextFieldPanel = new JPanel();
		searchCourseLabelAndTextFieldPanel.setBackground(Color.BLACK);
		searchCourseLabelAndTextFieldPanel.add(searchCourseLabel);
		searchCourseLabelAndTextFieldPanel.add(courseNameTextField);
		
		JButton searchCourseButton = new JButton("Search Course");
		searchCourseButton.setFont(consolasBold84);
		searchCourseButton.setForeground(Color.CYAN);
		searchCourseButton.addActionListener(new searchCourseButtonListener());
		
		JPanel searchCourseButtonPanel = new JPanel();
		searchCourseButtonPanel.setBackground(Color.BLACK);
		searchCourseButtonPanel.add(searchCourseButton);
		
		searchCoursePanel = new JPanel();
		searchCoursePanel.setBackground(Color.BLACK);
		searchCoursePanel.setLayout(new BorderLayout());
		searchCoursePanel.add(searchCourseLabelAndTextFieldPanel, BorderLayout.NORTH);
		searchCoursePanel.add(searchCourseButtonPanel, BorderLayout.CENTER);
	}
	
	private void departmentSelectorPanel()
	{
		departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
		departmentsComboBox.setFont(consolasBold84);
		
		JLabel displayProfessorsLabel = new JLabel();
		displayProfessorsLabel.setText("Display Courses by Department");
		displayProfessorsLabel.setFont(consolasBold84);
		displayProfessorsLabel.setBackground(Color.BLACK);
		displayProfessorsLabel.setForeground(Color.CYAN);
		
		JPanel displayProfessorsLabelPanel = new JPanel();
		displayProfessorsLabelPanel.setBackground(Color.BLACK);
		displayProfessorsLabelPanel.add(displayProfessorsLabel);
		
		JLabel departmentLabel = new JLabel("Select Department:");
		departmentLabel.setFont(consolasBold84);
		departmentLabel.setBackground(Color.BLACK);
		departmentLabel.setForeground(Color.CYAN);
		
		JPanel departmentLabelPanel = new JPanel();
		departmentLabelPanel.setBackground(Color.BLACK);
		departmentLabelPanel.add(departmentLabel);
		
		JPanel departmentsComboBoxPanel = new JPanel();
		departmentsComboBoxPanel.setBackground(Color.BLACK);
		departmentsComboBoxPanel.add(departmentsComboBox);
		
		JButton displayProfessorsButton = new JButton();
		displayProfessorsButton.setText("Display");
		displayProfessorsButton.addActionListener(new displayCoursesButtonListener());
		displayProfessorsButton.setFont(consolasBold84);
		displayProfessorsButton.setForeground(Color.CYAN);
		
		JPanel displayProfessorsButtonPanel = new JPanel();
		displayProfessorsButtonPanel.setBackground(Color.BLACK);
		displayProfessorsButtonPanel.add(displayProfessorsButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(departmentLabelPanel);
		centerPanel.add(departmentsComboBoxPanel);
		centerPanel.add(displayProfessorsButtonPanel);
		
		departmentSelectorPanel = new JPanel();
		departmentSelectorPanel.setBackground(Color.BLACK);
		departmentSelectorPanel.setLayout(new BorderLayout());
		departmentSelectorPanel.add(displayProfessorsLabelPanel, BorderLayout.NORTH);
		departmentSelectorPanel.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void foundCoursePanel()
	{
		JTextField foundCourseNameTextField = new JTextField(20);
		foundCourseNameTextField.setText(courseFound.getCourseName());
		foundCourseNameTextField.setEditable(false);
		foundCourseNameTextField.setFont(consolasBold84);
		
		JTextField foundCourseCampusTextField = new JTextField(30);
		foundCourseCampusTextField.setText(courseFound.getCampus());
		foundCourseCampusTextField.setEditable(false);
		foundCourseCampusTextField.setFont(new Font("Consolas", Font.BOLD, 72));
		
		JTextField foundCourseDepartmentTextField = new JTextField(20);
		foundCourseDepartmentTextField.setText(courseFound.getDepartment().getDepartmentName());
		foundCourseDepartmentTextField.setEditable(false);
		foundCourseDepartmentTextField.setFont(consolasBold84);
		
		JTextField foundCourseProfessorTextField = new JTextField(40);
		foundCourseProfessorTextField.setText(courseFound.getProfessor().getProfessorID() + " " + courseFound.getProfessor().getFirstName() + " " + courseFound.getProfessor().getLastName());
		foundCourseProfessorTextField.setEditable(false);
		foundCourseProfessorTextField.setFont(consolasBold84);
		
		JLabel foundCourseNameLabel = new JLabel("Course:");
		foundCourseNameLabel.setFont(consolasBold84);
		foundCourseNameLabel.setBackground(Color.BLACK);
		foundCourseNameLabel.setForeground(Color.CYAN);
		
		JPanel foundCourseNameLabelPanel = new JPanel();
		foundCourseNameLabelPanel.setBackground(Color.BLACK);
		foundCourseNameLabelPanel.add(foundCourseNameLabel);
		
		JPanel foundCourseNameTextFieldPanel = new JPanel();
		foundCourseNameTextFieldPanel.setBackground(Color.BLACK);
		foundCourseNameTextFieldPanel.add(foundCourseNameTextField);
		
		JLabel foundCourseInstructorLabel = new JLabel("Professor:");
		foundCourseInstructorLabel.setFont(consolasBold84);
		foundCourseInstructorLabel.setBackground(Color.BLACK);
		foundCourseInstructorLabel.setForeground(Color.CYAN);
		
		JPanel foundCourseInstructorLabelPanel = new JPanel();
		foundCourseInstructorLabelPanel.setBackground(Color.BLACK);
		foundCourseInstructorLabelPanel.add(foundCourseInstructorLabel);
		
		JPanel foundCourseInstructorTextFieldPanel = new JPanel();
		foundCourseInstructorTextFieldPanel.setBackground(Color.BLACK);
		foundCourseInstructorTextFieldPanel.add(foundCourseProfessorTextField);
		
		JLabel foundCourseCampusLabel = new JLabel("Campus:");
		foundCourseCampusLabel.setFont(consolasBold84);
		foundCourseCampusLabel.setBackground(Color.BLACK);
		foundCourseCampusLabel.setForeground(Color.CYAN);
		
		JPanel foundCourseCampusLabelPanel = new JPanel();
		foundCourseCampusLabelPanel.setBackground(Color.BLACK);
		foundCourseCampusLabelPanel.add(foundCourseCampusLabel);
		
		JPanel foundCourseCampusTextFieldPanel = new JPanel();
		foundCourseCampusTextFieldPanel.setBackground(Color.BLACK);
		foundCourseCampusTextFieldPanel.add(foundCourseCampusTextField);
		
		JLabel foundCourseDepartmentLabel = new JLabel("Department:");
		foundCourseDepartmentLabel.setFont(consolasBold84);
		foundCourseDepartmentLabel.setBackground(Color.BLACK);
		foundCourseDepartmentLabel.setForeground(Color.CYAN);
		
		JPanel foundCourseDepartmentLabelPanel = new JPanel();
		foundCourseDepartmentLabelPanel.setBackground(Color.BLACK);
		foundCourseDepartmentLabelPanel.add(foundCourseDepartmentLabel);
		
		JPanel foundCourseDepartmentTextFieldPanel = new JPanel();
		foundCourseDepartmentTextFieldPanel.setBackground(Color.BLACK);
		foundCourseDepartmentTextFieldPanel.add(foundCourseDepartmentTextField);
		
		JPanel coursePanel = new JPanel();
		coursePanel.setBackground(Color.BLACK);
		coursePanel.add(foundCourseNameLabelPanel);
		coursePanel.add(foundCourseNameTextFieldPanel);
		
		JPanel campusPanel = new JPanel();
		campusPanel.setBackground(Color.BLACK);
		campusPanel.add(foundCourseCampusLabelPanel);
		campusPanel.add(foundCourseCampusTextFieldPanel);
		
		JPanel departmentPanel = new JPanel();
		departmentPanel.setBackground(Color.BLACK);
		departmentPanel.add(foundCourseDepartmentLabelPanel);
		departmentPanel.add(foundCourseDepartmentTextFieldPanel);
		
		JPanel professorPanel = new JPanel();
		professorPanel.setBackground(Color.BLACK);
		professorPanel.add(foundCourseInstructorLabelPanel);
		professorPanel.add(foundCourseInstructorTextFieldPanel);
		
		JPanel foundCourseLabelsAndTextFields = new JPanel();
		foundCourseLabelsAndTextFields.setBackground(Color.BLACK);
		foundCourseLabelsAndTextFields.setLayout(new GridLayout(4,1));
		foundCourseLabelsAndTextFields.add(coursePanel);
		foundCourseLabelsAndTextFields.add(campusPanel);
		foundCourseLabelsAndTextFields.add(departmentPanel);
		foundCourseLabelsAndTextFields.add(professorPanel);
		
		JButton editCourseButton = new JButton("Edit Course");
		editCourseButton.setFont(consolasBold84);
		editCourseButton.setForeground(Color.CYAN);
		editCourseButton.addActionListener(new editCourseButtonListener());
		
		JButton deleteCourseButton = new JButton("Delete Course");
		deleteCourseButton.setFont(consolasBold84);
		deleteCourseButton.setForeground(Color.CYAN);
		deleteCourseButton.addActionListener(new deleteCourseButtonListener());
		
		JPanel foundCourseEditButtonPanel = new JPanel();
		foundCourseEditButtonPanel.setBackground(Color.BLACK);
		foundCourseEditButtonPanel.add(editCourseButton);
		foundCourseEditButtonPanel.add(deleteCourseButton);
		
		foundCoursePanel = new JPanel();
		foundCoursePanel.setBackground(Color.BLACK);
		foundCoursePanel.setLayout(new BorderLayout());
		foundCoursePanel.add(foundCourseLabelsAndTextFields, BorderLayout.CENTER);
		foundCoursePanel.add(foundCourseEditButtonPanel, BorderLayout.SOUTH);
	}
	
	private void editCoursePanel(boolean bool)
	{
		String department = null;
		if (bool)
			department = (String)departmentsComboBox.getSelectedItem();
		else
			department = courseFound.getDepartment().getDepartmentName();
		
		courseNameTextField = new JTextField(20);
		courseNameTextField.setText(courseFound.getCourseName());
		courseNameTextField.setEnabled(false);
		courseNameTextField.setFont(consolasBold84);
		
		campusComboBox = new JComboBox<String>(campuses);
		campusComboBox.setFont(new Font("Consolas", Font.BOLD, 72));
		campusComboBox.setSelectedItem(courseFound.getCampus());
		
		departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
		departmentsComboBox.setFont(consolasBold84);
		departmentsComboBox.setSelectedItem(department);
		departmentsComboBox.addActionListener(new departmentComboBoxListener());
		
		String[] professorsInfo = ProfessorDatabase.getProfessorsInfo(department);
		professorsComboBox = new JComboBox<String>(professorsInfo);
		professorsComboBox.setFont(consolasBold84);
		professorsComboBox.setSelectedItem(courseFound.getProfessor().getProfessorID() + " " + courseFound.getProfessor().getFirstName() + " " + courseFound.getProfessor().getLastName());
		
		JLabel courseNameLabel = new JLabel("Course Name:");
		courseNameLabel.setFont(consolasBold84);
		courseNameLabel.setForeground(Color.CYAN);
		courseNameLabel.setBackground(Color.BLACK);
		
		JPanel courseNameLabelPanel = new JPanel();
		courseNameLabelPanel.setBackground(Color.BLACK);
		courseNameLabelPanel.add(courseNameLabel);
		
		JPanel courseNameTextFieldPanel = new JPanel();
		courseNameTextFieldPanel.setBackground(Color.BLACK);
		courseNameTextFieldPanel.add(courseNameTextField);
		
		JLabel courseCampusNameLabel = new JLabel("Campus:");
		courseCampusNameLabel.setFont(consolasBold84);
		courseCampusNameLabel.setForeground(Color.CYAN);
		courseCampusNameLabel.setBackground(Color.BLACK);
		
		JPanel courseCampusNameLabelPanel = new JPanel();
		courseCampusNameLabelPanel.setBackground(Color.BLACK);
		courseCampusNameLabelPanel.add(courseCampusNameLabel);
		
		JPanel courseCampusComboBoxPanel = new JPanel();
		courseCampusComboBoxPanel.setBackground(Color.BLACK);
		courseCampusComboBoxPanel.add(campusComboBox);
		
		JLabel courseDepartmentNameLabel = new JLabel("Department:");
		courseDepartmentNameLabel.setFont(consolasBold84);
		courseDepartmentNameLabel.setForeground(Color.CYAN);
		courseDepartmentNameLabel.setBackground(Color.BLACK);

		JPanel courseDepartmentNameLabelPanel = new JPanel();
		courseDepartmentNameLabelPanel.setBackground(Color.BLACK);
		courseDepartmentNameLabelPanel.add(courseDepartmentNameLabel);
		
		JPanel courseDepartmentNameComboBoxesPanel = new JPanel();
		courseDepartmentNameComboBoxesPanel.setBackground(Color.BLACK);
		courseDepartmentNameComboBoxesPanel.add(departmentsComboBox);
		
		JLabel courseInstructorNameLabel = new JLabel("Professor:");
		courseInstructorNameLabel.setFont(consolasBold84);
		courseInstructorNameLabel.setForeground(Color.CYAN);
		courseInstructorNameLabel.setBackground(Color.BLACK);
		
		JPanel courseInstructorNameLabelPanel = new JPanel();
		courseInstructorNameLabelPanel.setBackground(Color.BLACK);
		courseInstructorNameLabelPanel.add(courseInstructorNameLabel);
		
		JPanel courseInstructorNameComboBoxesPanel = new JPanel();
		courseInstructorNameComboBoxesPanel.setBackground(Color.BLACK);
		courseInstructorNameComboBoxesPanel.add(professorsComboBox);
		
		JPanel addCourseLabelsAndComboBoxesPanel = new JPanel();
		addCourseLabelsAndComboBoxesPanel.setBackground(Color.BLACK);
		addCourseLabelsAndComboBoxesPanel.setLayout(new GridLayout(4,2));
		addCourseLabelsAndComboBoxesPanel.add(courseNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseNameTextFieldPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseCampusNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseCampusComboBoxPanel);	
		addCourseLabelsAndComboBoxesPanel.add(courseDepartmentNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseDepartmentNameComboBoxesPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseInstructorNameLabelPanel);
		addCourseLabelsAndComboBoxesPanel.add(courseInstructorNameComboBoxesPanel);
		
		JButton editCourseButton = new JButton("Save Changes");
		editCourseButton.setFont(consolasBold84);
		editCourseButton.setForeground(Color.CYAN);
		editCourseButton.addActionListener(new saveChangesCourseButtonListener());
		
		JPanel editCourseButtonPanel = new JPanel();
		editCourseButtonPanel.setBackground(Color.BLACK);
		editCourseButtonPanel.add(editCourseButton);
		
		editCoursePanel = new JPanel();
		editCoursePanel.setBackground(Color.BLACK);
		editCoursePanel.setLayout(new BorderLayout());
		editCoursePanel.add(addCourseLabelsAndComboBoxesPanel, BorderLayout.CENTER);
		editCoursePanel.add(editCourseButtonPanel, BorderLayout.SOUTH);
	}
}