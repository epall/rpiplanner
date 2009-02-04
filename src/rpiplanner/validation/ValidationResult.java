package rpiplanner.validation;

public interface ValidationResult {
	public int percentComplete();
	public Section getSectionResults(String name);
	
	public interface Section {
		public String[] missingCourses();
		public String[] appliedCourses();
		public String[] messages();
		public String[] potentialCourse();
	}
}
