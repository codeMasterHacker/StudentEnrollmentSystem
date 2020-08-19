import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ProfessorMenu 
{
	private JFrame main;
	
	private Professor professorFound;
	
	private Font consolasBold84;
	
	private JMenuItem addProfessorMenuItem;
	private JMenuItem searchProfessorMenuItem;
	private JMenuItem displayProfessorsMenuItem;
	
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField searchProfessorIDTextField;
	
	private JComboBox<String> departmentsComboBox;
	
	private JPanel addProfessorPanel;
	private JPanel displayProfessorsPanel;
	private JPanel searchProfessorPanel;
	private JPanel foundProfessorPanel;
	private JPanel editProfessorPanel;
	
	public JMenuItem getAddProfessorMenuItem() { return addProfessorMenuItem; }
	public JMenuItem getSearchProfessorMenuItem() { return searchProfessorMenuItem; }
	public JMenuItem getDisplayProfessorsMenuItem() { return displayProfessorsMenuItem; }
	
	public ProfessorMenu(JFrame parent)
	{
		main = parent;
		
		consolasBold84 = new Font("Consolas", Font.BOLD, 84);
		
		addProfessorMenuItem = new JMenuItem("Add Professor");
		addProfessorMenuItem.addActionListener(new addProfessorMenuItemListener());
		
		searchProfessorMenuItem = new JMenuItem("Search Professor");
		searchProfessorMenuItem.addActionListener(new searchProfessorMenuItemListener());
		
		displayProfessorsMenuItem = new JMenuItem("Display Professors");
		displayProfessorsMenuItem.addActionListener(new displayProfessorsMenuItemListener());
	}
	
	private class addProfessorMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (DepartmentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no departments in the databae yet. Departments are a prerequisite for professors. Add departments to the database first.");
			else
			{
				addProfessorPanel();
				
				main.getContentPane().removeAll();
				main.add(addProfessorPanel);
				main.setVisible(true);
			}
		}
	}
	
	private class resetProfessorButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			firstNameTextField.setText("");
			lastNameTextField.setText("");
			departmentsComboBox.setSelectedIndex(0);
		}
	}
	
	private class searchProfessorMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (ProfessorDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no professors in the databae yet. Add professors to the database.");
			else
			{
				searchProfessorPanel();
				
				main.getContentPane().removeAll();
				main.add(searchProfessorPanel);
				main.setVisible(true);
			}
		}
	}
	
	private class displayProfessorsMenuItemListener implements ActionListener //1st
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (ProfessorDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no professors in the databae yet. Add professors to the database.");
			else
			{
				displayProfessorsPanel();
				
				main.getContentPane().removeAll();
				main.add(displayProfessorsPanel);
				main.setVisible(true);
			}
		}
	}
	
	private class displayProfessorsButtonListener implements ActionListener //2nd
	{
		public void actionPerformed(ActionEvent e)
		{
			JTable professorJTable = ProfessorDatabase.getProfessorJTable((String)departmentsComboBox.getSelectedItem());
			
			if (professorJTable != null)
			{
				professorJTable.setEnabled(false);
				professorJTable.setFont(new Font("Consolas", Font.BOLD, 48));
				professorJTable.setRowHeight(50);
				
				main.getContentPane().removeAll();
				main.add(new JScrollPane(professorJTable));
				main.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "There are no professors that belong to that department in the database yet. Add professors to the database.");
		}
	}
	
	private class addProfessorButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String firstName = firstNameTextField.getText().trim();
			String lastName = lastNameTextField.getText().trim();
			String department = (String)departmentsComboBox.getSelectedItem();
			
			if (firstName.isEmpty() || lastName.isEmpty())
				JOptionPane.showMessageDialog(null, "First Name or Last Name fields may be empty.\nFill in the empty field(s).");
			else if (firstName.matches(".*[0-9].*") || lastName.matches(".*[0-9].*"))
				JOptionPane.showMessageDialog(null, "Names don't contain numbers. Re-enter name.");
			else
			{
				if (ProfessorDatabase.writeProfessor(new Professor(firstName, lastName, DepartmentDatabase.readDepartment(department))))
				{
					firstNameTextField.setText("");
					lastNameTextField.setText("");
					JOptionPane.showMessageDialog(null,"The professor was successfully added.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't write professor to database.");
			}
		}
	}
	
	private class searchProfessorButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String profID = searchProfessorIDTextField.getText().trim();
			
			if (profID.isEmpty())
				JOptionPane.showMessageDialog(null, "Textfield is empty. Re-enter.");
			else if (profID.matches(".*[a-zA-Z].*"))
				JOptionPane.showMessageDialog(null, "No letters in professor ID field. Re-enter.");
			else
			{
				int professorID = Integer.parseInt(profID);
				Professor professor = ProfessorDatabase.readProfessor(professorID);
				
				if (professor != null)
				{
					professorFound = professor;
					
					foundProfessorPanel();
					
					main.getContentPane().removeAll();
					main.add(foundProfessorPanel);
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Couldn't find the professor with Professor ID: " + professorID);
			}
		}
	}
	
	private class deleteProfessorButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int professorID = professorFound.getProfessorID();
			
			if(ProfessorDatabase.deleteProfessor(professorID))
			{
				searchProfessorPanel();
				
				main.getContentPane().removeAll();
				main.add(searchProfessorPanel);
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "The professor has been successfully deleted.");
			}
			else
				JOptionPane.showMessageDialog(null, "ERROR: Couldn't delete the professor with Professor ID: " + professorID);
		}
	}
	
	private class editProfessorButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			editProfessorPanel();
			
			main.getContentPane().removeAll();
			main.add(editProfessorPanel);
			main.setVisible(true);
		}
	}
	
	private class saveChangesButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String firstName = firstNameTextField.getText().trim();
			String lastName = lastNameTextField.getText().trim();
			String department = (String)departmentsComboBox.getSelectedItem();
			
			if (firstName.isEmpty() || lastName.isEmpty())
				JOptionPane.showMessageDialog(null, "First Name or Last Name fields may be empty.\nFill in the empty field(s).");
			else if (firstName.matches(".*[0-9].*") || lastName.matches(".*[0-9].*"))
				JOptionPane.showMessageDialog(null, "Names don't contain numbers. Re-enter name.");
			else
			{	
				professorFound.setFirstName(firstName);
				professorFound.setLastName(lastName);
				professorFound.setDepartment(DepartmentDatabase.readDepartment(department));
				
				if (ProfessorDatabase.editProfessor(professorFound))
				{		
					searchProfessorPanel();
					
					main.getContentPane().removeAll();
					main.add(searchProfessorPanel);
					main.setVisible(true);
					
					JOptionPane.showMessageDialog(null, "The professor has been successfully updated.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't update professor to database.");
			}
		}
	}
	
	private void addProfessorPanel()
	{
		firstNameTextField = new JTextField(20);
		firstNameTextField.setFont(consolasBold84);
		
		lastNameTextField = new JTextField(20);
		lastNameTextField.setFont(consolasBold84);
		
		departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
		departmentsComboBox.setFont(consolasBold84);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(consolasBold84);
		firstNameLabel.setBackground(Color.BLACK);
		firstNameLabel.setForeground(Color.RED);
		
		JPanel firstNameLabelPanel = new JPanel();
		firstNameLabelPanel.setBackground(Color.BLACK);
		firstNameLabelPanel.add(firstNameLabel);
		
		JPanel firstNameTextFieldPanel = new JPanel();
		firstNameTextFieldPanel.setBackground(Color.BLACK);
		firstNameTextFieldPanel.add(firstNameTextField);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(consolasBold84);
		lastNameLabel.setBackground(Color.BLACK);
		lastNameLabel.setForeground(Color.RED);
		
		JPanel lastNameLabelPanel = new JPanel();
		lastNameLabelPanel.setBackground(Color.BLACK);
		lastNameLabelPanel.add(lastNameLabel);
		
		JPanel lastNameTextFieldPanel = new JPanel();
		lastNameTextFieldPanel.setBackground(Color.BLACK);
		lastNameTextFieldPanel.add(lastNameTextField);
		
		JLabel departmentLabel = new JLabel("Department:");
		departmentLabel.setFont(consolasBold84);
		departmentLabel.setBackground(Color.BLACK);
		departmentLabel.setForeground(Color.RED);
		
		JPanel departmentLabelPanel = new JPanel();
		departmentLabelPanel.setBackground(Color.BLACK);
		departmentLabelPanel.add(departmentLabel);
		
		JPanel departmentsComboBoxPanel = new JPanel();
		departmentsComboBoxPanel.setBackground(Color.BLACK);
		departmentsComboBoxPanel.add(departmentsComboBox);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(3,2));
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(firstNameLabelPanel);
		centerPanel.add(firstNameTextFieldPanel);
		centerPanel.add(lastNameLabelPanel);
		centerPanel.add(lastNameTextFieldPanel);
		centerPanel.add(departmentLabelPanel);
		centerPanel.add(departmentsComboBoxPanel);
		
		JButton addProfessorButton = new JButton("Add Professor");
		addProfessorButton.setFont(consolasBold84);
		addProfessorButton.setForeground(Color.RED);
		addProfessorButton.addActionListener(new addProfessorButtonListener());
		
		JButton resetProfessorButton = new JButton("Clear Fields");
		resetProfessorButton.setFont(consolasBold84);
		resetProfessorButton.setForeground(Color.RED);
		resetProfessorButton.addActionListener(new resetProfessorButtonListener());
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.BLACK);
		buttonsPanel.add(addProfessorButton);
		buttonsPanel.add(resetProfessorButton);
		
		addProfessorPanel = new JPanel();
		addProfessorPanel.setBackground(Color.BLACK);
		addProfessorPanel.setLayout(new BorderLayout());
		addProfessorPanel.add(centerPanel, BorderLayout.CENTER);
		addProfessorPanel.add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	private void displayProfessorsPanel()
	{
		departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
		departmentsComboBox.setFont(consolasBold84);
		
		JLabel displayProfessorsLabel = new JLabel("Display Professors by Department");
		displayProfessorsLabel.setFont(consolasBold84);
		displayProfessorsLabel.setBackground(Color.BLACK);
		displayProfessorsLabel.setForeground(Color.RED);
		
		JPanel displayProfessorsLabelPanel = new JPanel();
		displayProfessorsLabelPanel.setBackground(Color.BLACK);
		displayProfessorsLabelPanel.add(displayProfessorsLabel);
		
		JLabel departmentLabel = new JLabel("Select Department:");
		departmentLabel.setFont(consolasBold84);
		departmentLabel.setBackground(Color.BLACK);
		departmentLabel.setForeground(Color.RED);
		
		JPanel departmentLabelPanel = new JPanel();
		departmentLabelPanel.setBackground(Color.BLACK);
		departmentLabelPanel.add(departmentLabel);
		
		JPanel departmentsComboBoxPanel = new JPanel();
		departmentsComboBoxPanel.setBackground(Color.BLACK);
		departmentsComboBoxPanel.add(departmentsComboBox);
		
		JButton displayProfessorsButton = new JButton("Display");
		displayProfessorsButton.setFont(consolasBold84);
		displayProfessorsButton.setForeground(Color.RED);
		displayProfessorsButton.addActionListener(new displayProfessorsButtonListener());
		
		JPanel displayProfessorsButtonPanel = new JPanel();
		displayProfessorsButtonPanel.setBackground(Color.BLACK);
		displayProfessorsButtonPanel.add(displayProfessorsButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(departmentLabelPanel);
		centerPanel.add(departmentsComboBoxPanel);
		centerPanel.add(displayProfessorsButtonPanel);
		
		displayProfessorsPanel = new JPanel();
		displayProfessorsPanel.setBackground(Color.BLACK);
		displayProfessorsPanel.setLayout(new BorderLayout());
		displayProfessorsPanel.add(displayProfessorsLabelPanel, BorderLayout.NORTH);
		displayProfessorsPanel.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void searchProfessorPanel()
	{
		searchProfessorIDTextField = new JTextField(4);
		searchProfessorIDTextField.setFont(consolasBold84);
		
		JLabel professorIDlabel = new JLabel("Professor ID:");
		professorIDlabel.setFont(consolasBold84);
		professorIDlabel.setBackground(Color.BLACK);
		professorIDlabel.setForeground(Color.RED);
		
		JPanel professorIDlabelPanel = new JPanel();
		professorIDlabelPanel.setBackground(Color.BLACK);
		professorIDlabelPanel.add(professorIDlabel);
		
		JPanel professorIDTextFieldPanel = new JPanel();
		professorIDTextFieldPanel.setBackground(Color.BLACK);
		professorIDTextFieldPanel.add(searchProfessorIDTextField);
		
		JButton searchProfessorButton = new JButton("Search Professor");
		searchProfessorButton.setFont(consolasBold84);
		searchProfessorButton.setForeground(Color.RED);
		searchProfessorButton.addActionListener(new searchProfessorButtonListener());
		
		JPanel searchProfessorButtonPanel = new JPanel();
		searchProfessorButtonPanel.setBackground(Color.BLACK);
		searchProfessorButtonPanel.add(searchProfessorButton);
		
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.BLACK);
		northPanel.add(professorIDlabelPanel);
		northPanel.add(professorIDTextFieldPanel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(searchProfessorButtonPanel);
		
		searchProfessorPanel = new JPanel();
		searchProfessorPanel.setBackground(Color.BLACK);
		searchProfessorPanel.setLayout(new BorderLayout());
		searchProfessorPanel.add(northPanel, BorderLayout.NORTH);
		searchProfessorPanel.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void foundProfessorPanel()
	{	
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(consolasBold84);
		firstNameLabel.setBackground(Color.BLACK);
		firstNameLabel.setForeground(Color.RED);
		
		JPanel firstNameLabelPanel = new JPanel();
		firstNameLabelPanel.setBackground(Color.BLACK);
		firstNameLabelPanel.add(firstNameLabel);
		
		JTextField firstNameTextField = new JTextField(20);
		firstNameTextField.setFont(consolasBold84);
		firstNameTextField.setText(professorFound.getFirstName());
		firstNameTextField.setEditable(false);
		
		JPanel firstNameTextFieldPanel = new JPanel();
		firstNameTextFieldPanel.setBackground(Color.BLACK);
		firstNameTextFieldPanel.add(firstNameTextField);
		
		JPanel firstNamePanel = new JPanel();
		firstNamePanel.setBackground(Color.BLACK);
		firstNamePanel.add(firstNameLabelPanel);
		firstNamePanel.add(firstNameTextFieldPanel);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(consolasBold84);
		lastNameLabel.setBackground(Color.BLACK);
		lastNameLabel.setForeground(Color.RED);
		
		JPanel lastNameLabelPanel = new JPanel();
		lastNameLabelPanel.setBackground(Color.BLACK);
		lastNameLabelPanel.add(lastNameLabel);
		
		JTextField lastNameTextField = new JTextField(20);
		lastNameTextField.setFont(consolasBold84);
		lastNameTextField.setText(professorFound.getLastName());
		lastNameTextField.setEditable(false);
		
		JPanel lastNameTextFieldPanel = new JPanel();
		lastNameTextFieldPanel.setBackground(Color.BLACK);
		lastNameTextFieldPanel.add(lastNameTextField);
		
		JPanel lastNamePanel = new JPanel();
		lastNamePanel.setBackground(Color.BLACK);
		lastNamePanel.add(lastNameLabelPanel);
		lastNamePanel.add(lastNameTextFieldPanel);
		
		JLabel professorDepartmentNameLabel = new JLabel("Department:");
		professorDepartmentNameLabel.setFont(consolasBold84);
		professorDepartmentNameLabel.setBackground(Color.BLACK);
		professorDepartmentNameLabel.setForeground(Color.RED);
		
		JPanel professorDepartmentNameLabelPanel = new JPanel();
		professorDepartmentNameLabelPanel.setBackground(Color.BLACK);
		professorDepartmentNameLabelPanel.add(professorDepartmentNameLabel);
		
		JTextField displayProfessorDepartmentNameTextField = new JTextField(20);
		displayProfessorDepartmentNameTextField.setFont(consolasBold84);
		displayProfessorDepartmentNameTextField.setText(professorFound.getDepartment().getDepartmentName());
		displayProfessorDepartmentNameTextField.setEditable(false);
		
		JPanel displayProfessorDepartmentNameTextFieldPanel = new JPanel();
		displayProfessorDepartmentNameTextFieldPanel.setBackground(Color.BLACK);
		displayProfessorDepartmentNameTextFieldPanel.add(displayProfessorDepartmentNameTextField);
		
		JPanel departmentPanel = new JPanel();
		departmentPanel.setBackground(Color.BLACK);
		departmentPanel.add(professorDepartmentNameLabelPanel);
		departmentPanel.add(displayProfessorDepartmentNameTextFieldPanel);
		
		JButton editProfessorButton = new JButton("Edit Professor");
		editProfessorButton.setFont(consolasBold84);
		editProfessorButton.setForeground(Color.RED);
		editProfessorButton.addActionListener(new editProfessorButtonListener());
		
		JPanel editProfessorButtonPanel = new JPanel();
		editProfessorButtonPanel.setBackground(Color.BLACK);
		editProfessorButtonPanel.add(editProfessorButton);
		
		JButton deleteProfessorButton = new JButton("Delete Professor");
		deleteProfessorButton.setFont(consolasBold84);
		deleteProfessorButton.setForeground(Color.RED);
		deleteProfessorButton.addActionListener(new deleteProfessorButtonListener());
		
		JPanel deleteProfessorButtonPanel = new JPanel();
		deleteProfessorButtonPanel.setBackground(Color.BLACK);
		deleteProfessorButtonPanel.add(deleteProfessorButton);
		
		JPanel professorButtonsPanel = new JPanel();
		professorButtonsPanel.setBackground(Color.BLACK);		
		professorButtonsPanel.add(editProfessorButtonPanel);
		professorButtonsPanel.add(deleteProfessorButtonPanel);
		
		foundProfessorPanel = new JPanel();
		foundProfessorPanel.setBackground(Color.BLACK);
		foundProfessorPanel.setLayout(new GridLayout(4,1));
		foundProfessorPanel.add(firstNamePanel);
		foundProfessorPanel.add(lastNamePanel);
		foundProfessorPanel.add(departmentPanel);
		foundProfessorPanel.add(professorButtonsPanel);
	}
	
	private void editProfessorPanel()
	{
		firstNameTextField = new JTextField(20);
		firstNameTextField.setFont(consolasBold84);
		firstNameTextField.setText(professorFound.getFirstName());
		
		lastNameTextField = new JTextField(20);
		lastNameTextField.setFont(consolasBold84);
		lastNameTextField.setText(professorFound.getLastName());
		
		departmentsComboBox = new JComboBox<String>(DepartmentDatabase.getDepartmentNames());
		departmentsComboBox.setFont(consolasBold84);
		departmentsComboBox.setSelectedItem(professorFound.getDepartment().getDepartmentName());
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(consolasBold84);
		firstNameLabel.setBackground(Color.BLACK);
		firstNameLabel.setForeground(Color.RED);
		
		JPanel firstNameLabelPanel = new JPanel();
		firstNameLabelPanel.setBackground(Color.BLACK);
		firstNameLabelPanel.add(firstNameLabel);
		
		JPanel firstNameTextFieldPanel = new JPanel();
		firstNameTextFieldPanel.setBackground(Color.BLACK);
		firstNameTextFieldPanel.add(firstNameTextField);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(consolasBold84);
		lastNameLabel.setBackground(Color.BLACK);
		lastNameLabel.setForeground(Color.RED);
		
		JPanel lastNameLabelPanel = new JPanel();
		lastNameLabelPanel.setBackground(Color.BLACK);
		lastNameLabelPanel.add(lastNameLabel);
		
		JPanel lastNameTextFieldPanel = new JPanel();
		lastNameTextFieldPanel.setBackground(Color.BLACK);
		lastNameTextFieldPanel.add(lastNameTextField);
		
		JLabel departmentLabel = new JLabel("Department:");
		departmentLabel.setFont(consolasBold84);
		departmentLabel.setBackground(Color.BLACK);
		departmentLabel.setForeground(Color.RED);
		
		JPanel departmentLabelPanel = new JPanel();
		departmentLabelPanel.setBackground(Color.BLACK);
		departmentLabelPanel.add(departmentLabel);
		
		JPanel departmentsComboBoxPanel = new JPanel();
		departmentsComboBoxPanel.setBackground(Color.BLACK);
		departmentsComboBoxPanel.add(departmentsComboBox);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(3,2));
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(firstNameLabelPanel);
		centerPanel.add(firstNameTextFieldPanel);
		centerPanel.add(lastNameLabelPanel);
		centerPanel.add(lastNameTextFieldPanel);
		centerPanel.add(departmentLabelPanel);
		centerPanel.add(departmentsComboBoxPanel);
		
		JButton saveChangesButton = new JButton("Save Changes");
		saveChangesButton.setFont(consolasBold84);
		saveChangesButton.setForeground(Color.RED);
		saveChangesButton.addActionListener(new saveChangesButtonListener());
		
		JPanel saveChangesButtonPanel = new JPanel();
		saveChangesButtonPanel.setBackground(Color.BLACK);
		saveChangesButtonPanel.add(saveChangesButton);
		
		editProfessorPanel = new JPanel();
		editProfessorPanel.setBackground(Color.BLACK);
		editProfessorPanel.setLayout(new BorderLayout());
		editProfessorPanel.add(centerPanel, BorderLayout.CENTER);
		editProfessorPanel.add(saveChangesButtonPanel, BorderLayout.SOUTH);
	}
}