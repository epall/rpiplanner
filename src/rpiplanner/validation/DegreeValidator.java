package rpiplanner.validation;

import rpiplanner.model.PlanOfStudy;

/**
 * Validates a PlanOfStudy to a Degree when asked.
 * @see rpiplanner.model.Degree
 * @see rpiplanner.model.PlanOfStudy
 */
public interface DegreeValidator {
    /**
     * Each degree has a set of sections, such as "Math & Science"
     * "Communication Requirement", "Free Electives", and so on. Each section
     * is validated separately, and the GUI needs to display the set of
     * sections for the user to see.
     * @return A list of section names
     */
	public String[] getSectionNames();

    /**
     * Every Degree in RPI Planner has a unique internal ID that ties it
     * to its degree validator. This is a convenience method to get that
     * internal ID.
     * @return an id that corresponds to a Degree in the database
     */
	public long getID();

    /**
     * Validate the given PlanOfStudy according to the degree this
     * DegreeValidator represents. Returns a complex ValidationResult
     * object for inspection and display. Can be called as often as
     * needed.
     * @param plan a PlanOfStudy to validate
     * @return The results of the validation
     */
	public ValidationResult validate(PlanOfStudy plan);
}
