package rpiplanner.view;

import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import rpiplanner.model.CourseDatabase;

public class CourseListModel implements ListModel {
	private CourseDatabase database;
	private ArrayList<ListDataListener> listeners = new ArrayList<ListDataListener>(1);

	public CourseListModel(CourseDatabase courseDatabase) {
		this.database = courseDatabase;
	}

	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	public Object getElementAt(int index) {
		return database.toArray()[index];
	}

	public int getSize() {
		return database.size();
	}

	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
}
