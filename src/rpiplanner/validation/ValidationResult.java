package rpiplanner.validation;

import rpiplanner.model.Course;

/**
 * A {@link DegreeValidator}'s <code>validate()</code> method will produce a
 * ValidationResult that can be queried for detailed results. Each
 * ValidationResult contains details for each section such as which courses
 * were missing, which courses were applied, which courses could possibly
 * be applied, and any other messages the validation caused.
 */
public interface ValidationResult {
    /**
     * An implementation-specific percentage of completion for progress
     * indication in the GUI. Currently implemented in Ruby as # of successful
     * sections / total # of sections * 100. Not perfect, but reasonable.
     * @return A number between 0 and 100 representing the level of completion.
     */
	public int percentComplete();

    /**
     * Get the results for a specific section.
     * @param name A name of a section in the <code>getSections()</code> array of
     * {@link DegreeValidator}.
     * @return The results for the requested section, or <code>null</code>.
     * if it does not exist.
     */
	public Section getSectionResults(String name);
	
	public interface Section {
		public Course[] missingCourses();
		public Course[] appliedCourses();
		public String[] messages();
		public Course[] potentialCourses();
		public boolean isSuccess();
	}
}
