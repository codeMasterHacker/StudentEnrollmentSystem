public class Department 
{
	private String departmentName, departmentHead;
	
	public Department(String departmentName, String departmentHead)
	{
		this.departmentName = departmentName;
		this.departmentHead = departmentHead;
	}
	
	public Department(String departmentName)
	{
		this.departmentName = departmentName;
		this.departmentHead = "";
	}
	
	public String getDepartmentName() { return departmentName; }
	
	public String getDepartmentHead() { return departmentHead; }
	
	public void setDepartmentHead(String departmentHead) { this.departmentHead = departmentHead; }
}