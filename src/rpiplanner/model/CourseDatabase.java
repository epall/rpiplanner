package rpiplanner.model;

import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("courses")
public class CourseDatabase implements ListModel {
	@XStreamImplicit
	private ArrayList<Course> courses = new ArrayList<Course>();
	
	@XStreamOmitField
	private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>(1);

	public void add(Course newCourse) {
		courses.add(newCourse);
		for(ListDataListener l : listeners){
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, courses.size()-1, courses.size()-1);
			l.intervalAdded(e);
		}
	}

	public void addListDataListener(ListDataListener l) {
		if(listeners == null) // because XStream will null it
			listeners = new ArrayList<ListDataListener>(1);

		listeners.add(l);
	}

	public Object getElementAt(int index) {
		return courses.toArray()[index];
	}

	public int getSize() {
		return courses.size();
	}

	public void removeListDataListener(ListDataListener l) {
		if(listeners == null) // because XStream will null it
			listeners = new ArrayList<ListDataListener>(1);
		listeners.remove(l);
	}
}
