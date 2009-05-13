/*
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

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
						if (req.equals(c) || c.doesCountAs(req))
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
						if (req.equals(c) || c.doesCountAs(req))
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
