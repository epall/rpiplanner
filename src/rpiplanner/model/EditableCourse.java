package rpiplanner.model;

import java.util.List;

public class EditableCourse extends Course {
	public EditableCourse(Course toEdit) {
		this.title = toEdit.title;
		this.description = toEdit.description;
		this.department = toEdit.department;
		this.catalogNumber = toEdit.catalogNumber;
		this.credits = toEdit.credits;
		if(toEdit.prerequisites != null)
			this.prerequisites = (RequisiteSet)toEdit.prerequisites.clone();
		if(toEdit.corequisites != null)
			this.corequisites = (RequisiteSet)toEdit.corequisites.clone();
		if(toEdit.availableTerms != null)
			this.availableTerms = toEdit.availableTerms.clone();
	}
	public EditableCourse() {}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public void setPrerequisites(List<Course> prerequisites) {
		if(this.prerequisites == null)
			this.prerequisites = new RequisiteSet();
		this.prerequisites.addAll(prerequisites);
	}
	public void setCorequisites(List<Course> corequisites) {
		if(this.corequisites == null)
			this.corequisites = new RequisiteSet();
		this.corequisites.addAll(corequisites);
	}
	public void setAvailableTerms(YearPart[] availableTerms) {
		this.availableTerms = availableTerms;
	}
}
