package rpiplanner.view;

import javax.swing.AbstractListModel;

import rpiplanner.model.Course;

public class CourseListModel extends AbstractListModel {

	protected Course[] visibleCourses;

	protected CourseListModel() { }

	public CourseListModel(Course[] visibleCourses){
		if(visibleCourses == null)
			this.visibleCourses = new Course[0];
		else
			this.visibleCourses = visibleCourses;
	}

	public Object getElementAt(int index) {
		if(index < visibleCourses.length)
			return visibleCourses[index];
		return null;
	}

	public int getSize() {
		return visibleCourses.length;
	}

	public Course[] getCourses(){
		return visibleCourses.clone();
	}
}