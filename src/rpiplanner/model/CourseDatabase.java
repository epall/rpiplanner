package rpiplanner.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("courses")
public class CourseDatabase {
	@XStreamImplicit
	private ArrayList<Course> courses = new ArrayList<Course>();
	
	@XStreamOmitField
	private ArrayList<String> departments;
	
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
		TreeSet<Course> found = new TreeSet<Course>();
		for(Course c : courses){
			if(c.toString().toLowerCase().contains(text.toLowerCase()))
				found.add(c);
		}
		return found.toArray(new Course[0]);
	}

	public Course[] listAll() {
		Course[] temp = new Course[courses.size()];
		courses.toArray(temp);
		Arrays.sort(temp);
		return temp;
	}
	
	public List<String> getDepartments(){
		if(departments == null){
			departments = new ArrayList<String>();
			for(Course c : courses){
				departments.add(c.getDepartment());
			}
		}
		return departments;
	}

	public Course getCourse(String catalogNumber) {
		for(Course c : courses){
			if(c.getCatalogNumber().equals(catalogNumber))
				return c;
		}
		return null;
	}
}
