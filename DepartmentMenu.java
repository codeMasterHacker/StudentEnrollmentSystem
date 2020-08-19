import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DepartmentMenu 
{
	private JFrame main;
	
	private Department departmentFound;
	
	private Font consolasBold84;
	
	private JMenuItem addDepartmentMenuItem;
	private JMenuItem searchDepartmentMenuItem;
	private JMenuItem displayDepartmentsMenuItem;
	
	private JTextField departmentNameTextField;
	
	private JComboBox<String> departmentHeadComboBox;
	
	private JPanel addDepartmentPanel;
	private JPanel searchDepartmentPanel;
	private JPanel foundDepartmentPanel;
	private JPanel setDepartmentHeadPanel;
	
	
	public JMenuItem getAddDepartmentMenuItem() { return addDepartmentMenuItem; }
	public JMenuItem getSearchDepartmentMenuItem() { return searchDepartmentMenuItem; }	
	public JMenuItem getDisplayDepartmentsMenuItem() { return displayDepartmentsMenuItem; }
	
	public DepartmentMenu(JFrame parent)
	{
		main = parent;
		
		consolasBold84 = new Font("Consolas", Font.BOLD, 84);
		
		addDepartmentMenuItem = new JMenuItem("Add Department");
		addDepartmentMenuItem.addActionListener(new addDepartmentMenuItemListener());
		
		searchDepartmentMenuItem = new JMenuItem("Search Department");
		searchDepartmentMenuItem.addActionListener(new searchDepartmentMenuItemListener());
		
		displayDepartmentsMenuItem = new JMenuItem("Display Departments");
		displayDepartmentsMenuItem.addActionListener(new displayDepartmentsMenuItemListener());
	}
	
	private class addDepartmentMenuItemListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e)
		{
			addDepartmentPanel();
			
			main.getContentPane().removeAll();
			main.add(addDepartmentPanel);
			main.setVisible(true);
		}
	}
	
	private class searchDepartmentMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (DepartmentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no departments in the database yet. Add departments to the database.");
			else
			{
				searchDepartmentPanel();
				
				main.getContentPane().removeAll();
				main.add(searchDepartmentPanel);
				main.setVisible(true);
			}
		}
	}
	
	private class displayDepartmentsMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			JTable departmentJTable = DepartmentDatabase.getDepartmentJTable();
			
			if (departmentJTable != null)
			{
				departmentJTable.setEnabled(false);
				departmentJTable.setFont(new Font("Consolas", Font.BOLD, 48));
				departmentJTable.setRowHeight(50);
				
				main.getContentPane().removeAll();
				main.add(new JScrollPane(departmentJTable));
				main.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "There are no departments in the database yet. Add departments to the database.");
		}
	}
	
	private class addDepartmentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String departmentName = departmentNameTextField.getText().trim();
			
			if (departmentName.isEmpty())
				JOptionPane.showMessageDialog(null, "Department Name field is empty.\nFill in the empty field.");
			else if (departmentName.matches(".*[0-9].*"))
				JOptionPane.showMessageDialog(null, "Names don't contain numbers. Re-enter name.");
			else
			{
				if (DepartmentDatabase.writeDepartment(departmentName))
				{
					departmentNameTextField.setText("");
					JOptionPane.showMessageDialog(null,"The department was successfully added.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't write department to database.");
			}
		}
	}
	
	private class searchDepartmentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String departmentName = departmentNameTextField.getText().trim();
			
			if (departmentName.isEmpty())
				JOptionPane.showMessageDialog(null, "Department Name field is empty.\nFill in the empty field.");
			else if (departmentName.matches(".*[0-9].*"))
				JOptionPane.showMessageDialog(null, "Names don't contain numbers. Re-enter name.");
			else
			{
				Department department = DepartmentDatabase.readDepartment(departmentName);
				
				if (department != null)
				{
					departmentFound = department;
					
					foundDepartmentPanel();
					
					main.getContentPane().removeAll();
					main.add(foundDepartmentPanel);
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Couldn't find the department with Department Name: " + departmentName);
			}
		}
	}
	
	private class deleteDepartmentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String departmentName = departmentFound.getDepartmentName();
			
			if(DepartmentDatabase.deleteDepartment(departmentName))
			{
				searchDepartmentPanel();
				
				main.getContentPane().removeAll();
				main.add(searchDepartmentPanel);
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "The department has been successfully deleted.");
			}
			else
				JOptionPane.showMessageDialog(null, "Couldn't find the department with Department Name: " + departmentName);
		}
	}
	
	private class setDepartmentHeadButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			setDepartmentHeadPanel();
			
			main.getContentPane().removeAll();
			main.add(setDepartmentHeadPanel);
			main.setVisible(true);
		}
	}
	
	private class saveChangesButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String departmentInfo = (String)departmentHeadComboBox.getSelectedItem();
			int index = departmentInfo.indexOf(" ");
			String departmentHead = departmentInfo.substring(index+1, departmentInfo.length());
			
			departmentFound.setDepartmentHead(departmentHead);
			
			if (DepartmentDatabase.editDepartment(departmentFound))
			{
				searchDepartmentPanel();
				
				main.getContentPane().removeAll();
				main.add(searchDepartmentPanel);
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "Department has been successfully updated.");
			}
		}
	}
	
	private void addDepartmentPanel()
	{
		departmentNameTextField = new JTextField(30);
		departmentNameTextField.setFont(consolasBold84);
		
		JLabel departmentNameLabel = new JLabel("Department Name:");
		departmentNameLabel.setFont(consolasBold84);
		departmentNameLabel.setBackground(Color.BLACK);
		departmentNameLabel.setForeground(Color.YELLOW);
		
		JPanel departmentNameLabelPanel = new JPanel();
		departmentNameLabelPanel.setBackground(Color.BLACK);
		departmentNameLabelPanel.add(departmentNameLabel);
		
		JPanel departmentNameTextFieldPanel = new JPanel();
		departmentNameTextFieldPanel.setBackground(Color.BLACK);
		departmentNameTextFieldPanel.add(departmentNameTextField);
		
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.BLACK);
		northPanel.add(departmentNameLabelPanel);
		northPanel.add(departmentNameTextFieldPanel);
		
		JButton addDepartmentButton = new JButton("Add Department");
		addDepartmentButton.setFont(consolasBold84);
		addDepartmentButton.setForeground(Color.BLACK);
		addDepartmentButton.addActionListener(new addDepartmentButtonListener());
		
		JPanel addDepartmentButtonPanel = new JPanel();
		addDepartmentButtonPanel.setBackground(Color.BLACK);
		addDepartmentButtonPanel.add(addDepartmentButton);
		
		addDepartmentPanel = new JPanel();
		addDepartmentPanel.setBackground(Color.BLACK);
		addDepartmentPanel.setLayout(new BorderLayout());
		addDepartmentPanel.add(northPanel, BorderLayout.NORTH);
		addDepartmentPanel.add(addDepartmentButtonPanel, BorderLayout.CENTER);
	}
	
	private void searchDepartmentPanel()
	{
		departmentNameTextField = new JTextField(30);
		departmentNameTextField.setFont(consolasBold84);
		
		JLabel departmentNameLabel = new JLabel("Department Name:");
		departmentNameLabel.setFont(consolasBold84);
		departmentNameLabel.setBackground(Color.BLACK);
		departmentNameLabel.setForeground(Color.YELLOW);
		
		JPanel departmentNameLabelPanel = new JPanel();
		departmentNameLabelPanel.setBackground(Color.BLACK);
		departmentNameLabelPanel.add(departmentNameLabel);
		
		JPanel departmentNameTextFieldPanel = new JPanel();
		departmentNameTextFieldPanel.setBackground(Color.BLACK);
		departmentNameTextFieldPanel.add(departmentNameTextField);
		
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.BLACK);
		northPanel.add(departmentNameLabelPanel);
		northPanel.add(departmentNameTextFieldPanel);
		
		JButton searchDepartmentButton = new JButton("Search Department");
		searchDepartmentButton.setFont(consolasBold84);
		searchDepartmentButton.setForeground(Color.BLACK);
		searchDepartmentButton.addActionListener(new searchDepartmentButtonListener());
		
		JPanel searchDepartmentButtonPanel = new JPanel();
		searchDepartmentButtonPanel.setBackground(Color.BLACK);
		searchDepartmentButtonPanel.add(searchDepartmentButton);
		
		searchDepartmentPanel = new JPanel();
		searchDepartmentPanel.setBackground(Color.BLACK);
		searchDepartmentPanel.setLayout(new BorderLayout());
		searchDepartmentPanel.add(northPanel, BorderLayout.NORTH);
		searchDepartmentPanel.add(searchDepartmentButtonPanel, BorderLayout.CENTER);
	}
	
	private void foundDepartmentPanel()
	{
		JLabel departmentNAMElabel = new JLabel("Department Name:");
		departmentNAMElabel.setFont(consolasBold84);
		departmentNAMElabel.setBackground(Color.BLACK);
		departmentNAMElabel.setForeground(Color.YELLOW);
		
		JPanel departmentNAMElabelPanel = new JPanel();
		departmentNAMElabelPanel.setBackground(Color.BLACK);
		departmentNAMElabelPanel.add(departmentNAMElabel);
		
		JTextField departmentNameTextField = new JTextField(30);
		departmentNameTextField.setFont(consolasBold84);
		departmentNameTextField.setText(departmentFound.getDepartmentName());
		departmentNameTextField.setEditable(false);
		
		JPanel departmentNameTextFieldPanel = new JPanel();
		departmentNameTextFieldPanel.setBackground(Color.BLACK);
		departmentNameTextFieldPanel.add(departmentNameTextField);
		
		JPanel departmentNamePanel = new JPanel();
		departmentNamePanel.setBackground(Color.BLACK);
		departmentNamePanel.add(departmentNAMElabelPanel);
		departmentNamePanel.add(departmentNameTextFieldPanel);
		
		JLabel departmentHEADlabel = new JLabel("Department Head:");
		departmentHEADlabel.setFont(consolasBold84);
		departmentHEADlabel.setBackground(Color.BLACK);
		departmentHEADlabel.setForeground(Color.YELLOW);
		
		JPanel departmentHEADlabelPanel = new JPanel();
		departmentHEADlabelPanel.setBackground(Color.BLACK);
		departmentHEADlabelPanel.add(departmentHEADlabel);
		
		String departmentHead = departmentFound.getDepartmentHead();
		JTextField departmentHeadTextField = new JTextField(38);
		if (departmentHead != null)
			departmentHeadTextField.setText(departmentHead);
		else
			departmentHeadTextField.setText("No department head");
		departmentHeadTextField.setEditable(false);
		departmentHeadTextField.setFont(consolasBold84);
		
		JPanel departmentHeadTextFieldPanel = new JPanel();
		departmentHeadTextFieldPanel.setBackground(Color.BLACK);
		departmentHeadTextFieldPanel.add(departmentHeadTextField);
		
		JPanel departmentHeadPanel = new JPanel();
		departmentHeadPanel.setBackground(Color.BLACK);
		departmentHeadPanel.add(departmentHEADlabelPanel);
		departmentHeadPanel.add(departmentHeadTextFieldPanel);
		
		JButton setDepartmentHeadButton = new JButton("Set Department Head");
		setDepartmentHeadButton.setFont(consolasBold84);
		setDepartmentHeadButton.setForeground(Color.BLACK);
		setDepartmentHeadButton.addActionListener(new setDepartmentHeadButtonListener());
		
		JPanel setDepartmentHeadButtonPanel = new JPanel();
		setDepartmentHeadButtonPanel.setBackground(Color.BLACK);
		setDepartmentHeadButtonPanel.add(setDepartmentHeadButton);
		
		JButton deleteDepartmentButton = new JButton("Delete Department");
		deleteDepartmentButton.setFont(consolasBold84);
		deleteDepartmentButton.setForeground(Color.BLACK);
		deleteDepartmentButton.addActionListener(new deleteDepartmentButtonListener());
		
		JPanel deleteDepartmentButtonPanel = new JPanel();
		deleteDepartmentButtonPanel.setBackground(Color.BLACK);
		deleteDepartmentButtonPanel.add(deleteDepartmentButton);
		
		JPanel departmentButtonsPanel = new JPanel();
		departmentButtonsPanel.setBackground(Color.BLACK);
		departmentButtonsPanel.add(deleteDepartmentButtonPanel);
		if (!ProfessorDatabase.isEmpty())
			departmentButtonsPanel.add(setDepartmentHeadButtonPanel);
		
		foundDepartmentPanel = new JPanel();
		foundDepartmentPanel.setBackground(Color.BLACK);
		foundDepartmentPanel.setLayout(new GridLayout(3,1));
		foundDepartmentPanel.add(departmentNamePanel);
		foundDepartmentPanel.add(departmentHeadPanel);
		foundDepartmentPanel.add(departmentButtonsPanel);
	}
	
	private void setDepartmentHeadPanel()
	{
		String departmentName = departmentFound.getDepartmentName();
		
		JTextField departmentNameTextField = new JTextField(30);
		departmentNameTextField.setFont(consolasBold84);
		departmentNameTextField.setText(departmentName);
		departmentNameTextField.setEnabled(false);
		
		String[] professorsInfo = ProfessorDatabase.getProfessorsInfo(departmentName);
		departmentHeadComboBox = new JComboBox<String>(professorsInfo);
		departmentHeadComboBox.setFont(consolasBold84);
		
		JLabel departmentNAMElabel = new JLabel("Department Name:");
		departmentNAMElabel.setFont(consolasBold84);
		departmentNAMElabel.setBackground(Color.BLACK);
		departmentNAMElabel.setForeground(Color.YELLOW);
		
		JPanel departmentNAMElabelPanel = new JPanel();
		departmentNAMElabelPanel.setBackground(Color.BLACK);
		departmentNAMElabelPanel.add(departmentNAMElabel);
		
		JPanel departmentNameTextFieldPanel = new JPanel();
		departmentNameTextFieldPanel.setBackground(Color.BLACK);
		departmentNameTextFieldPanel.add(departmentNameTextField);
		
		JPanel deptNamePanel = new JPanel();
		deptNamePanel.setBackground(Color.BLACK);
		deptNamePanel.add(departmentNAMElabelPanel);
		deptNamePanel.add(departmentNameTextFieldPanel);
		
		JLabel departmentHEADlabel = new JLabel("Department Head:");
		departmentHEADlabel.setFont(consolasBold84);
		departmentHEADlabel.setBackground(Color.BLACK);
		departmentHEADlabel.setForeground(Color.YELLOW);
		
		JPanel departmentHEADlabelPanel = new JPanel();
		departmentHEADlabelPanel.setBackground(Color.BLACK);
		departmentHEADlabelPanel.add(departmentHEADlabel);
		
		JPanel departmentHeadComboBoxPanel = new JPanel();
		departmentHeadComboBoxPanel.setBackground(Color.BLACK);
		departmentHeadComboBoxPanel.add(departmentHeadComboBox);
		
		JPanel deptHeadPanel = new JPanel();
		deptHeadPanel.setBackground(Color.BLACK);
		deptHeadPanel.add(departmentHEADlabelPanel);
		deptHeadPanel.add(departmentHeadComboBoxPanel);
		
		JPanel displayDepartmentPanel = new JPanel();
		displayDepartmentPanel.setBackground(Color.BLACK);
		displayDepartmentPanel.setLayout(new GridLayout(2,1));
		displayDepartmentPanel.add(deptNamePanel);
		displayDepartmentPanel.add(deptHeadPanel);
		
		JButton saveChangesToDepartmentButton = new JButton("Save Changes");
		saveChangesToDepartmentButton.setFont(consolasBold84);
		saveChangesToDepartmentButton.setForeground(Color.BLACK);
		saveChangesToDepartmentButton.addActionListener(new saveChangesButtonListener());
		
		JPanel editDepartmentButtonPanel = new JPanel();
		editDepartmentButtonPanel.setBackground(Color.BLACK);
		editDepartmentButtonPanel.add(saveChangesToDepartmentButton);
		
		setDepartmentHeadPanel = new JPanel();
		setDepartmentHeadPanel.setBackground(Color.BLACK);
		setDepartmentHeadPanel.setLayout(new BorderLayout());
		setDepartmentHeadPanel.add(displayDepartmentPanel, BorderLayout.CENTER);
		setDepartmentHeadPanel.add(editDepartmentButtonPanel, BorderLayout.SOUTH);
	}
}