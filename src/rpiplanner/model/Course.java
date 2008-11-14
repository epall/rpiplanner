package rpiplanner.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Eric Allen
 */

@XStreamAlias("course")
public class Course implements Comparable<Course> {
    protected String title;
    protected String description;
    protected String department;
    protected String catalogNumber;
    protected int credits;
    protected Course[] prerequisites;
    protected Course[] corequisites;
    protected YearPart[] availableTerms;
    
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
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public Course[] getPrerequisites() {
		return prerequisites;
	}
	public void setPrerequisites(Course[] prerequisites) {
		this.prerequisites = prerequisites;
	}
	public Course[] getCorequisites() {
		return corequisites;
	}
	public void setCorequisites(Course[] corequisites) {
		this.corequisites = corequisites;
	}
	public YearPart[] getAvailableTerms() {
		return availableTerms;
	}
	public void setAvailableTerms(YearPart[] availableTerms) {
		this.availableTerms = availableTerms;
	}
	public String toString(){
		return catalogNumber + " - " + title;
	}
	public int compareTo(Course o) {
		return toString().compareTo(o.toString());
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Course)
			return catalogNumber == ((Course)obj).catalogNumber;
		return false;
	}
}
