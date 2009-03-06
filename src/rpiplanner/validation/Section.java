package rpiplanner.validation;

import rpiplanner.model.Course;

/**
 * Created by IntelliJ IDEA.
 * User: Matt Murphy
 * Date: Mar 5, 2009
 */
public class Section
{
        String name;
        String description;
    
		public Course[] missingCourses()
        {
            return null;
        }
		public Course[] appliedCourses()
        {
            return null;
        }
		public String[] messages()
        {
            return null;
        }
		public Course[] potentialCourses()
        {
            return null;
        }
		public boolean isSuccess()
        {
            return false;
        }
}
