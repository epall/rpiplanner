package rpiplanner.model;

import java.util.HashSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("courses")
public class CourseDatabase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7592879999900432026L;
	
	private HashSet<Course> courses = new HashSet<Course>();

	public int size() {
		return courses.size();
	}

	public Course[] toArray() {
		return courses.toArray(new Course[1]);
	}

	public void add(Course newCourse) {
		courses.add(newCourse);
	}

}
