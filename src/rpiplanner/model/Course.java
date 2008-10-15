package rpiplanner.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Eric Allen
 */

@XStreamAlias("course")
public class Course {
    protected String title;
    protected String description;
    protected String department;
    protected String catalogNumber;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCatalogNumber() {
		return catalogNumber;
	}
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	public String toString(){
		return catalogNumber + " - " + title;
	}
}
