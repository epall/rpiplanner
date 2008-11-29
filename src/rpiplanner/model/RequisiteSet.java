package rpiplanner.model;

import java.util.ArrayList;
import java.util.List;

public class RequisiteSet extends ArrayList<Course> {
	protected boolean required = true;
	protected boolean pickOne = false;
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isPickOne() {
		return pickOne;
	}
	public void setPickOne(boolean pickOne) {
		this.pickOne = pickOne;
	}
	public List<ValidationError> check(PlanOfStudy plan, int termEnd, boolean prereq){
		ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
		if (pickOne) {
			boolean found = false;
			
			for (Course req : this) {
				for (int j = 0; j <= termEnd; j++) {
					Term testTerm = plan.getTerm(j);
					for (Course c : testTerm.getCourses()) {
						if (req.equals(c))
							found = true;
					}
				}
			}
			if (!found) {
				StringBuilder b = new StringBuilder();
				for(int i = 0; i < size(); i++){
					b.append(get(i).catalogNumber);
					if(i < size()-1)
						b.append(", ");
				}
				ValidationError err = new ValidationError(
						required ? ValidationError.Type.ERROR
								: ValidationError.Type.WARNING, (prereq ? "Pre"
								: "Co")
								+ "requisite not satisfied: one of "
								+ b.toString());
				errors.add(err);
			}
		} else {
			for (Course req : this) {
				boolean found = false;
				for (int j = 0; j <= termEnd; j++) {
					Term testTerm = plan.getTerm(j);
					for (Course c : testTerm.getCourses()) {
						if (req.equals(c))
							found = true;
					}
				}
				if (!found) {
					ValidationError err = new ValidationError(
							required ? ValidationError.Type.ERROR
									: ValidationError.Type.WARNING,
							(prereq ? "Pre" : "Co")
									+ "requisite not satisfied: "
									+ req.toString());
					errors.add(err);
				}
			}
		}
		return errors;
	}
}
