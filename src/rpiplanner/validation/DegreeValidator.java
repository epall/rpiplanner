package rpiplanner.validation;

import rpiplanner.model.PlanOfStudy;

public interface DegreeValidator {
	public String[] getSectionNames();
	public long getID();
	public ValidationResult validate(PlanOfStudy plan);
}
