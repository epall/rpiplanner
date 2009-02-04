package rpiplanner.validation;

import org.jruby.RubyHash;

import rpiplanner.model.PlanOfStudy;

public interface DegreeValidator {
	public String[] getSectionNames();
	public long getID();
	public RubyHash validate(String section_name, PlanOfStudy plan);
}
