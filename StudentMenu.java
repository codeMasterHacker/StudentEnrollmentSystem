import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StudentMenu
{
	private JFrame main;
	
	private Student studentFound;
	
	private Font consolasBold84;
	
	private JMenuItem addStudentMenuItem;
	private JMenuItem searchStudentMenuItem;
	private JMenuItem displayStudentsMenuItem;
	
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField searchStudentTextField;
	
	private Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	private String[] years = {"2002", "2001", "2000",
			"1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990",
			"1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980",
			"1979", "1978", "1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970",
			"1969", "1968", "1967", "1966", "1965", "1964", "1963", "1962", "1961", "1960",
			"1959", "1958", "1957", "1956", "1955", "1954", "1953", "1952", "1951", "1950",
			"1949", "1948", "1947", "1946", "1945", "1944", "1943", "1942", "1941", "1940",
			"1939", "1938", "1937", "1936", "1935", "1934", "1933", "1932", "1931", "1930",
			"1929", "1928", "1927", "1926", "1925", "1924", "1923", "1922", "1921", "1920"};
	
	private JComboBox<Integer> monthComboBox;
	private JComboBox<Integer> dayComboBox;
	private JComboBox<String> yearComboBox;
	
	private JPanel addStudentPanel;
	private JPanel searchStudentPanel;
	private JPanel foundStudentPanel;
	private JPanel editStudentPanel;
	
	public JMenuItem getAddStudentMenuItem() { return addStudentMenuItem; }
	public JMenuItem getSearchStudentMenuItem() { return searchStudentMenuItem; }
	public JMenuItem getDisplayStudentsMenuItem() { return displayStudentsMenuItem; }
	
	public StudentMenu(JFrame parent)
	{
		main = parent;
		
		consolasBold84 = new Font("Consolas", Font.BOLD, 84);
		
		addStudentMenuItem = new JMenuItem("Add Student");
		addStudentMenuItem.addActionListener(new addStudentMenuItemListener());
		
		searchStudentMenuItem = new JMenuItem("Search Student");
		searchStudentMenuItem.addActionListener(new searchStudentMenuItemListener());
		
		displayStudentsMenuItem = new JMenuItem("Display Students");
		displayStudentsMenuItem.addActionListener(new displayStudentsMenuItemListener());
	}
	
	private class addStudentMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
			
			firstNameTextField = new JTextField(20);
			firstNameTextField.setFont(consolasBold84);
			
			lastNameTextField = new JTextField(20);
			lastNameTextField.setFont(consolasBold84);
			
			monthComboBox = new JComboBox<Integer>(months);
			monthComboBox.setFont(consolasBold84);
			monthComboBox.addActionListener(new monthComboBox1Listener());
			
			dayComboBox = new JComboBox<Integer>(days);
			dayComboBox.setFont(consolasBold84);
			
			addStudentPanel();
			
			main.getContentPane().removeAll();
			main.add(addStudentPanel);
			main.setVisible(true);
		}
	}
	
	private class searchStudentMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (StudentDatabase.isEmpty())
				JOptionPane.showMessageDialog(null, "There are no students in the database yet. Add students to the database.");
			else
			{
				searchStudentPanel();
				
				main.getContentPane().removeAll();
				main.add(searchStudentPanel);
				main.setVisible(true);
			}
		}		
	}
	
	private class displayStudentsMenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JTable studentJTable = StudentDatabase.getStudentJTable();
			
			if (studentJTable != null)
			{
				studentJTable.setEnabled(false);
				studentJTable.setFont(new Font("Consolas", Font.BOLD, 48));
				studentJTable.setRowHeight(50);
				
				main.getContentPane().removeAll();
				main.add(new JScrollPane(studentJTable));
				main.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "There are no students in the database yet. Add students to the database.");
		}		
	}
	
	private class addStudentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String firstName = firstNameTextField.getText().trim();
			String lastName = lastNameTextField.getText().trim();
			int month = (int)monthComboBox.getSelectedItem();
			int day = (int)dayComboBox.getSelectedItem();
			String year = (String)yearComboBox.getSelectedItem();
			String birthday = month + "-" + day + "-" + year;
			
			if (firstName.isEmpty() || lastName.isEmpty())
				JOptionPane.showMessageDialog(null, "First Name field or Last Name field may be empty.\nFill in the appropriate empty field(s).");
			else if (firstName.matches(".*[0-9].*") || lastName.matches(".*[0-9].*"))
				JOptionPane.showMessageDialog(null, "Names don't contain numbers. Re-enter names.");
			else
			{
				if (StudentDatabase.writeStudent(new Student(firstName, lastName, birthday)))
				{
					firstNameTextField.setText("");
					lastNameTextField.setText("");
					monthComboBox.setSelectedIndex(0);
					dayComboBox.setSelectedIndex(0);
					yearComboBox.setSelectedIndex(0);
					JOptionPane.showMessageDialog(null, "Student has been successfully added.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't write student to database.");
			}
		}
	}
	
	private class resetStudentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			firstNameTextField.setText("");
			lastNameTextField.setText("");
			monthComboBox.setSelectedIndex(0);
			dayComboBox.setSelectedIndex(0);
			yearComboBox.setSelectedIndex(0);
		}
	}
	
	private class searchStudentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			String studentIDnum = searchStudentTextField.getText().trim();
			
			if (studentIDnum.isEmpty())
				JOptionPane.showMessageDialog(null, "Textfield is empty. Re-enter.");
			else if (studentIDnum.matches("^[a-zA-Z]+ [a-zA-Z]+$"))
			{
				int space = studentIDnum.indexOf(" ");
				String firstName = studentIDnum.substring(0, space);
				String lastName = studentIDnum.substring(space+1, studentIDnum.length());
				
				JTable studentJTable = StudentDatabase.getStudentJTable(firstName, lastName);
				
				if (studentJTable != null)
				{
					studentJTable.setEnabled(false);
					studentJTable.setFont(new Font("Consolas", Font.BOLD, 48));
					studentJTable.setRowHeight(50);
					
					main.getContentPane().removeAll();
					main.add(new JScrollPane(studentJTable));
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "There are no students in the database with that name.");
			}
			else if (studentIDnum.matches("^[0-9]+$"))
			{
				int studentID = Integer.parseInt(studentIDnum);
				Student student = StudentDatabase.readStudent(studentID);
				
				if (student != null)
				{
					studentFound = student;
					
					foundStudentPanel();
					
					main.getContentPane().removeAll();
					main.add(foundStudentPanel);
					main.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Couldn't find the student with Student ID: " + studentID);
			}
			else
				JOptionPane.showMessageDialog(null, "Incorrect format. Enter either a number or a full name(first and last name).");
		}		
	}
	
	private class deleteStudentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int studentID = studentFound.getStudentID();
			
			if(StudentDatabase.deleteStudent(studentID))
			{
				searchStudentPanel();
				
				main.getContentPane().removeAll();
				main.add(searchStudentPanel);
				main.setVisible(true);
				
				JOptionPane.showMessageDialog(null, "The student has been successfully deleted.");
			}
			else
				JOptionPane.showMessageDialog(null, "Couldn't find the student with Student ID: " + studentID);
		}
	}
	
	private class editStudentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int month = 0, day = 0;
			String year = "", birthday = studentFound.getBirthday();
			if (birthday.length() == 10)
			{
				 month = Integer.parseInt(birthday.substring(0, 2));
				 day = Integer.parseInt(birthday.substring(3, 5));
				 year = birthday.substring(6,10);
			}
			else if (birthday.length() == 9)
			{
				month = Integer.parseInt(birthday.substring(0, 1));
				day = Integer.parseInt(birthday.substring(2, 4));
				year = birthday.substring(5,9);
			}
			else if (birthday.length() == 8)
			{
				month = Integer.parseInt(birthday.substring(0, 1));
				day = Integer.parseInt(birthday.substring(2, 3));
				year = birthday.substring(4,8);
			}
			
			if (month == 2)
			{
				Integer[] days1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
				dayComboBox = new JComboBox<Integer>(days1);
			}
			else if (month == 4 || month == 6 || month == 9 || month == 11)
			{
				Integer[] days5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
				dayComboBox = new JComboBox<Integer>(days5);
			}
			else
			{
				Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
				dayComboBox = new JComboBox<Integer>(days);
			}
			dayComboBox.setFont(consolasBold84);
			dayComboBox.setSelectedItem(day);
			
			firstNameTextField = new JTextField(20);
			firstNameTextField.setFont(consolasBold84);
			firstNameTextField.setText(studentFound.getFirstName());
			
			lastNameTextField = new JTextField(20);
			lastNameTextField.setFont(consolasBold84);
			lastNameTextField.setText(studentFound.getLastName());
			
			monthComboBox = new JComboBox<Integer>(months);
			monthComboBox.setFont(consolasBold84);
			monthComboBox.setSelectedItem(month);
			monthComboBox.addActionListener(new monthComboBox2Listener());
			
			yearComboBox = new JComboBox<String>(years);
			yearComboBox.setFont(consolasBold84);
			yearComboBox.setSelectedItem(year);
			
			editStudentPanel();
			
			main.getContentPane().removeAll();
			main.add(editStudentPanel);
			main.setVisible(true);
		}		
	}
	
	private class saveChangesButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String firstName = firstNameTextField.getText().trim();
			String lastName = lastNameTextField.getText().trim();
			int month = (int)monthComboBox.getSelectedItem();
			int day = (int)dayComboBox.getSelectedItem();
			String year = (String)yearComboBox.getSelectedItem();
			String birthday = month + "-" + day + "-" + year;
			
			if (firstName.isEmpty() || lastName.isEmpty())
				JOptionPane.showMessageDialog(null, "First Name field or Last Name field may be empty.\nFill in the appropriate empty field(s).");
			else if (firstName.matches(".*[0-9].*") || lastName.matches(".*[0-9].*"))
				JOptionPane.showMessageDialog(null, "Names don't contain numbers. Re-enter names.");
			else
			{
				studentFound.setFirstName(firstName);
				studentFound.setLastName(lastName);
				studentFound.setBirthday(birthday);
				
				if (StudentDatabase.editStudent(studentFound))
				{
					searchStudentPanel();
					
					main.getContentPane().removeAll();
					main.add(searchStudentPanel);
					main.setVisible(true);
					
					JOptionPane.showMessageDialog(null, "Student has been successfully updated.");
				}
				else
					JOptionPane.showMessageDialog(null, "ERROR: Couldn't update student to database.");
			}
		}
	}
	
	private class monthComboBox1Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			int month = (Integer)monthComboBox.getSelectedItem();
			if (month == 2)
			{
				Integer[] days1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
				dayComboBox = new JComboBox<Integer>(days1);
			}
			else if (month == 4 || month == 6 || month == 9 || month == 11)
			{
				Integer[] days5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
				dayComboBox = new JComboBox<Integer>(days5);
			}
			else
			{
				Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
				dayComboBox = new JComboBox<Integer>(days);
			}
			dayComboBox.setFont(consolasBold84);
			
			String firstName = firstNameTextField.getText();
			if (!firstName.isEmpty())
				firstNameTextField.setText(firstName);
			
			String lastName = lastNameTextField.getText();
			if (!lastName.isEmpty())
				lastNameTextField.setText(lastName);
			
			addStudentPanel();
			
			main.getContentPane().removeAll();
			main.add(addStudentPanel);
			main.setVisible(true);
		}
	}
	
	private class monthComboBox2Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			int month = (Integer)monthComboBox.getSelectedItem();
			if (month == 2)
			{
				Integer[] days1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
				dayComboBox = new JComboBox<Integer>(days1);
			}
			else if (month == 4 || month == 6 || month == 9 || month == 11)
			{
				Integer[] days5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
				dayComboBox = new JComboBox<Integer>(days5);
			}
			else
			{
				Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
				dayComboBox = new JComboBox<Integer>(days);
			}
			dayComboBox.setFont(consolasBold84);
			
			editStudentPanel();
			
			main.getContentPane().removeAll();
			main.add(editStudentPanel);
			main.setVisible(true);
		}
	}
	
	private void addStudentPanel()
	{
		yearComboBox = new JComboBox<String>(years);
		yearComboBox.setFont(consolasBold84);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(consolasBold84);
		firstNameLabel.setForeground(Color.GREEN);
		firstNameLabel.setBackground(Color.BLACK);
		
		JPanel firstNameLabelPanel = new JPanel();
		firstNameLabelPanel.setBackground(Color.BLACK);
		firstNameLabelPanel.add(firstNameLabel);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(consolasBold84);
		lastNameLabel.setForeground(Color.GREEN);
		lastNameLabel.setBackground(Color.BLACK);
		
		JPanel lastNameLabelPanel = new JPanel();
		lastNameLabelPanel.setBackground(Color.BLACK);
		lastNameLabelPanel.add(lastNameLabel);
		
		JLabel birthdayLabel = new JLabel("Birthday:");
		birthdayLabel.setFont(consolasBold84);
		birthdayLabel.setForeground(Color.GREEN);
		birthdayLabel.setBackground(Color.BLACK);
		
		JPanel birthdayLabelPanel = new JPanel();
		birthdayLabelPanel.setBackground(Color.BLACK);
		birthdayLabelPanel.add(birthdayLabel);
		
		JLabel monthLabel = new JLabel("Month:");
		monthLabel.setFont(consolasBold84);
		monthLabel.setForeground(Color.GREEN);
		monthLabel.setBackground(Color.BLACK);
		
		JPanel monthLabelPanel = new JPanel();
		monthLabelPanel.setBackground(Color.BLACK);
		monthLabelPanel.add(monthLabel);
		
		JLabel dayLabel = new JLabel("Day:");
		dayLabel.setFont(consolasBold84);
		dayLabel.setForeground(Color.GREEN);
		dayLabel.setBackground(Color.BLACK);
		
		JPanel dayLabelPanel = new JPanel();
		dayLabelPanel.setBackground(Color.BLACK);
		dayLabelPanel.add(dayLabel);
		
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setFont(consolasBold84);
		yearLabel.setForeground(Color.GREEN);
		yearLabel.setBackground(Color.BLACK);
		
		JPanel yearLabelPanel = new JPanel();
		yearLabelPanel.setBackground(Color.BLACK);
		yearLabelPanel.add(yearLabel);
		
		JPanel firstNameTextFieldPanel = new JPanel();
		firstNameTextFieldPanel.setBackground(Color.BLACK);
		firstNameTextFieldPanel.add(firstNameTextField);
		
		JPanel lastNameTextFieldPanel = new JPanel();
		lastNameTextFieldPanel.setBackground(Color.BLACK);
		lastNameTextFieldPanel.add(lastNameTextField);
		
		JPanel birthdayPanel = new JPanel();
		birthdayPanel.setBackground(Color.BLACK);
		birthdayPanel.add(monthLabelPanel);
		birthdayPanel.add(monthComboBox);
		birthdayPanel.add(dayLabelPanel);
		birthdayPanel.add(dayComboBox);
		birthdayPanel.add(yearLabelPanel);
		birthdayPanel.add(yearComboBox);
		
		JPanel addStudentTextFieldsAndLabelsPanel = new JPanel();
		addStudentTextFieldsAndLabelsPanel.setLayout(new GridLayout(3,2));
		addStudentTextFieldsAndLabelsPanel.setBackground(Color.BLACK);
		addStudentTextFieldsAndLabelsPanel.add(firstNameLabelPanel);
		addStudentTextFieldsAndLabelsPanel.add(firstNameTextFieldPanel);
		addStudentTextFieldsAndLabelsPanel.add(lastNameLabelPanel);
		addStudentTextFieldsAndLabelsPanel.add(lastNameTextFieldPanel);
		addStudentTextFieldsAndLabelsPanel.add(birthdayLabelPanel);
		addStudentTextFieldsAndLabelsPanel.add(birthdayPanel);
		
		JButton addStudentButton = new JButton("Add Student");
		addStudentButton.setFont(consolasBold84);
		addStudentButton.setForeground(Color.GREEN);
		addStudentButton.addActionListener(new addStudentButtonListener());
		
		JButton resetStudentButton = new JButton("Clear Fields");	
		resetStudentButton.setFont(consolasBold84);
		resetStudentButton.setForeground(Color.GREEN);
		resetStudentButton.addActionListener(new resetStudentButtonListener());
		
		JPanel addStudentButtonsPanel = new JPanel();
		addStudentButtonsPanel.setBackground(Color.BLACK);
		addStudentButtonsPanel.add(addStudentButton);
		addStudentButtonsPanel.add(resetStudentButton);
		
		addStudentPanel = new JPanel();
		addStudentPanel.setBackground(Color.BLACK);
		addStudentPanel.setLayout(new BorderLayout());
		addStudentPanel.add(addStudentTextFieldsAndLabelsPanel, BorderLayout.CENTER);
		addStudentPanel.add(addStudentButtonsPanel, BorderLayout.SOUTH);
	}
	
	private void searchStudentPanel()
	{
		searchStudentTextField = new JTextField(40);
		searchStudentTextField.setFont(consolasBold84);
		
		JLabel searchStudentLabel = new JLabel("Full Name/ID:");
		searchStudentLabel.setFont(consolasBold84);
		searchStudentLabel.setBackground(Color.BLACK);
		searchStudentLabel.setForeground(Color.GREEN);
		
		JPanel searchStudentLabelAndTextFieldPanel = new JPanel();
		searchStudentLabelAndTextFieldPanel.setBackground(Color.BLACK);
		searchStudentLabelAndTextFieldPanel.add(searchStudentLabel);
		searchStudentLabelAndTextFieldPanel.add(searchStudentTextField);
		
		JButton searchStudentButton = new JButton("Search Student");
		searchStudentButton.setFont(consolasBold84);
		searchStudentButton.setForeground(Color.GREEN);
		searchStudentButton.addActionListener(new searchStudentButtonListener());
		
		JPanel searchStudentButtonPanel = new JPanel();
		searchStudentButtonPanel.setBackground(Color.BLACK);
		searchStudentButtonPanel.add(searchStudentButton);
		
		searchStudentPanel = new JPanel();
		searchStudentPanel.setBackground(Color.BLACK);
		searchStudentPanel.setLayout(new BorderLayout());
		searchStudentPanel.add(searchStudentLabelAndTextFieldPanel, BorderLayout.NORTH);
		searchStudentPanel.add(searchStudentButtonPanel, BorderLayout.CENTER);
	}
	
	private void foundStudentPanel()
	{	
		JLabel foundStudentFirstNameLabel = new JLabel("First Name:");
		foundStudentFirstNameLabel.setFont(consolasBold84);
		foundStudentFirstNameLabel.setForeground(Color.GREEN);
		foundStudentFirstNameLabel.setBackground(Color.BLACK);
		
		JTextField foundStudentFirstNameTextField = new JTextField(20);
		foundStudentFirstNameTextField.setFont(consolasBold84);
		foundStudentFirstNameTextField.setText(studentFound.getFirstName());
		foundStudentFirstNameTextField.setEditable(false);
		
		JPanel foundStudentFirstNamePanel = new JPanel();
		foundStudentFirstNamePanel.setBackground(Color.BLACK);
		foundStudentFirstNamePanel.add(foundStudentFirstNameLabel);
		foundStudentFirstNamePanel.add(foundStudentFirstNameTextField);
		
		JLabel foundStudentLastNameLabel = new JLabel("Last Name:");
		foundStudentLastNameLabel.setFont(consolasBold84);
		foundStudentLastNameLabel.setForeground(Color.GREEN);
		foundStudentLastNameLabel.setBackground(Color.BLACK);
		
		JTextField foundStudentLastNameTextField = new JTextField(20);
		foundStudentLastNameTextField.setFont(consolasBold84);
		foundStudentLastNameTextField.setText(studentFound.getLastName());
		foundStudentLastNameTextField.setEditable(false);
		
		JPanel foundStudentLastNamePanel = new JPanel();
		foundStudentLastNamePanel.setBackground(Color.BLACK);
		foundStudentLastNamePanel.add(foundStudentLastNameLabel);
		foundStudentLastNamePanel.add(foundStudentLastNameTextField);
		
		JLabel foundStudentBirthdayLabel = new JLabel("Birthday:");
		foundStudentBirthdayLabel.setFont(consolasBold84);
		foundStudentBirthdayLabel.setForeground(Color.GREEN);
		foundStudentBirthdayLabel.setBackground(Color.BLACK);
		
		JTextField foundStudentBirthdayTextField = new JTextField(10);
		foundStudentBirthdayTextField.setFont(consolasBold84);
		foundStudentBirthdayTextField.setText(studentFound.getBirthday());
		foundStudentBirthdayTextField.setEditable(false);
		
		JPanel foundStudentBirthdayPanel = new JPanel();
		foundStudentBirthdayPanel.setBackground(Color.BLACK);
		foundStudentBirthdayPanel.add(foundStudentBirthdayLabel);
		foundStudentBirthdayPanel.add(foundStudentBirthdayTextField);
		
		JButton editStudentButton = new JButton("Edit Student");
		editStudentButton.setFont(consolasBold84);
		editStudentButton.setForeground(Color.GREEN);
		editStudentButton.addActionListener(new editStudentButtonListener());
		
		JButton deleteStudentButton = new JButton("Delete Student");
		deleteStudentButton.setFont(consolasBold84);
		deleteStudentButton.setForeground(Color.GREEN);
		deleteStudentButton.addActionListener(new deleteStudentButtonListener());
		
		JPanel foundStudentButtonPanel = new JPanel();
		foundStudentButtonPanel.setBackground(Color.BLACK);
		foundStudentButtonPanel.add(editStudentButton);
		foundStudentButtonPanel.add(deleteStudentButton);
		
		foundStudentPanel = new JPanel();
		foundStudentPanel.setBackground(Color.BLACK);
		foundStudentPanel.setLayout(new GridLayout(4,1));
		foundStudentPanel.add(foundStudentFirstNamePanel);
		foundStudentPanel.add(foundStudentLastNamePanel);
		foundStudentPanel.add(foundStudentBirthdayPanel);
		foundStudentPanel.add(foundStudentButtonPanel);
	}
	
	private void editStudentPanel()
	{
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(consolasBold84);
		firstNameLabel.setForeground(Color.GREEN);
		firstNameLabel.setBackground(Color.BLACK);
		
		JPanel firstNameLabelPanel = new JPanel();
		firstNameLabelPanel.setBackground(Color.BLACK);
		firstNameLabelPanel.add(firstNameLabel);
		
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(consolasBold84);
		lastNameLabel.setForeground(Color.GREEN);
		lastNameLabel.setBackground(Color.BLACK);
		
		JPanel lastNameLabelPanel = new JPanel();
		lastNameLabelPanel.setBackground(Color.BLACK);
		lastNameLabelPanel.add(lastNameLabel);
		
		JLabel birthdayLabel = new JLabel("Birthday:");
		birthdayLabel.setFont(consolasBold84);
		birthdayLabel.setForeground(Color.GREEN);
		birthdayLabel.setBackground(Color.BLACK);
		
		JPanel birthdayLabelPanel = new JPanel();
		birthdayLabelPanel.setBackground(Color.BLACK);
		birthdayLabelPanel.add(birthdayLabel);
		
		JLabel monthLabel = new JLabel("Month:");
		monthLabel.setFont(consolasBold84);
		monthLabel.setForeground(Color.GREEN);
		monthLabel.setBackground(Color.BLACK);
		
		JPanel monthLabelPanel = new JPanel();
		monthLabelPanel.setBackground(Color.BLACK);
		monthLabelPanel.add(monthLabel);
		
		JLabel dayLabel = new JLabel("Day:");
		dayLabel.setFont(consolasBold84);
		dayLabel.setForeground(Color.GREEN);
		dayLabel.setBackground(Color.BLACK);
		
		JPanel dayLabelPanel = new JPanel();
		dayLabelPanel.setBackground(Color.BLACK);
		dayLabelPanel.add(dayLabel);
		
		JLabel yearLabel = new JLabel("Year:");
		yearLabel.setFont(consolasBold84);
		yearLabel.setForeground(Color.GREEN);
		yearLabel.setBackground(Color.BLACK);
		
		JPanel yearLabelPanel = new JPanel();
		yearLabelPanel.setBackground(Color.BLACK);
		yearLabelPanel.add(yearLabel);
		
		JPanel firstNameTextFieldPanel = new JPanel();
		firstNameTextFieldPanel.setBackground(Color.BLACK);
		firstNameTextFieldPanel.add(firstNameTextField);
		
		JPanel lastNameTextFieldPanel = new JPanel();
		lastNameTextFieldPanel.setBackground(Color.BLACK);
		lastNameTextFieldPanel.add(lastNameTextField);
		
		JPanel birthdayPanel = new JPanel();
		birthdayPanel.setBackground(Color.BLACK);
		birthdayPanel.add(monthLabelPanel);
		birthdayPanel.add(monthComboBox);
		birthdayPanel.add(dayLabelPanel);
		birthdayPanel.add(dayComboBox);
		birthdayPanel.add(yearLabelPanel);
		birthdayPanel.add(yearComboBox);
		
		JPanel addStudentTextFieldsAndLabelsPanel = new JPanel();
		addStudentTextFieldsAndLabelsPanel.setLayout(new GridLayout(3,2));
		addStudentTextFieldsAndLabelsPanel.setBackground(Color.BLACK);
		addStudentTextFieldsAndLabelsPanel.add(firstNameLabelPanel);
		addStudentTextFieldsAndLabelsPanel.add(firstNameTextFieldPanel);
		addStudentTextFieldsAndLabelsPanel.add(lastNameLabelPanel);
		addStudentTextFieldsAndLabelsPanel.add(lastNameTextFieldPanel);
		addStudentTextFieldsAndLabelsPanel.add(birthdayLabelPanel);
		addStudentTextFieldsAndLabelsPanel.add(birthdayPanel);
		
		JButton saveChangesButton = new JButton("Save Changes");
		saveChangesButton.setFont(consolasBold84);
		saveChangesButton.setForeground(Color.GREEN);
		saveChangesButton.addActionListener(new saveChangesButtonListener());
		
		JPanel saveChangesButtonPanel = new JPanel();
		saveChangesButtonPanel.setBackground(Color.BLACK);
		saveChangesButtonPanel.add(saveChangesButton);;
		
		editStudentPanel = new JPanel();
		editStudentPanel.setBackground(Color.BLACK);
		editStudentPanel.setLayout(new BorderLayout());
		editStudentPanel.add(addStudentTextFieldsAndLabelsPanel, BorderLayout.CENTER);
		editStudentPanel.add(saveChangesButtonPanel, BorderLayout.SOUTH);
	}
}