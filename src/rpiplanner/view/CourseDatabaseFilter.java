package rpiplanner.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;

public class CourseDatabaseFilter extends AbstractListModel implements PropertyChangeListener {
	private Course[] visibleCourses;
	
	private CourseDatabase database;
	
	public CourseDatabaseFilter(CourseDatabase database){
		this.database = database;
		this.visibleCourses = database.listCourses();
	}

	public Object getElementAt(int index) {
		if(index < visibleCourses.length)
			return visibleCourses[index];
		return null;
	}

	public int getSize() {
		return visibleCourses.length;
	}

	public void setSearchText(String text) {
		if(text == null || text == "")
			visibleCourses = database.listCourses();
		else
			visibleCourses = database.search(text);
		fireContentsChanged(this, 0, visibleCourses.length-1);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName() == "courses"){
			setSearchText("");
		}
	}
}
