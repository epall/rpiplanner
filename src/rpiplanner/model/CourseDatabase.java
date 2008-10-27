package rpiplanner.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("courses")
public class CourseDatabase {
	@XStreamImplicit
	private ArrayList<Course> courses = new ArrayList<Course>();
	
	@XStreamOmitField
	private PropertyChangeSupport support;
	
	public void add(Course newCourse) {
		if(courses == null)
			courses = new ArrayList<Course>();
		courses.add(newCourse);
		support.firePropertyChange("courses", null, courses);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		if(support == null)
			support = new PropertyChangeSupport(this);
		support.addPropertyChangeListener(listener);
	}

	public int getSize() {
		return courses.size();
	}

	public Course[] search(String text) {
		ArrayList<Course> found = new ArrayList<Course>(courses.size());
		for(Course c : courses){
			if(c.toString().toLowerCase().contains(text.toLowerCase()))
				found.add(c);
		}
		return found.toArray(new Course[0]);
	}

	public Course[] listAll() {
		return courses.toArray(new Course[0]);
	}
}
