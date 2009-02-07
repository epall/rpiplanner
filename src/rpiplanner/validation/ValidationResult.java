package rpiplanner.validation;

import rpiplanner.model.Course;

public interface ValidationResult {
	public int percentComplete();
	public Section getSectionResults(String name);
	
	public interface Section {
		public Course[] missingCourses();
		public Course[] appliedCourses();
		public String[] messages();
		public Course[] potentialCourses();
		public boolean isSuccess();
	}
}
