package rpiplanner.model;

public class EditableCourse extends Course {
	public EditableCourse(Course toEdit) {
		this.title = toEdit.title;
		this.description = toEdit.description;
		this.department = toEdit.department;
		this.catalogNumber = toEdit.catalogNumber;
		this.credits = toEdit.credits;
		if(toEdit.prerequisites != null)
			this.prerequisites = toEdit.prerequisites.clone();
		if(toEdit.corequisites != null)
			this.corequisites = toEdit.corequisites.clone();
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
	public void setPrerequisites(Course[] prerequisites) {
		this.prerequisites = prerequisites;
	}
	public void setCorequisites(Course[] corequisites) {
		this.corequisites = corequisites;
	}
	public void setAvailableTerms(YearPart[] availableTerms) {
		this.availableTerms = availableTerms;
	}
}
