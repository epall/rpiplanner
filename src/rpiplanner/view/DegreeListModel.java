package rpiplanner.view;

import javax.swing.AbstractListModel;

import rpiplanner.model.CourseDatabase;
import rpiplanner.model.PlanOfStudy;

public class DegreeListModel extends AbstractListModel {
	private CourseDatabase database;
	private PlanOfStudy plan;
	
	public DegreeListModel(CourseDatabase database){
		this.database = database;
	}

	public DegreeListModel(PlanOfStudy plan) {
		this.plan = plan;
	}

	public Object getElementAt(int index) {
		if(database != null)
			return database.listDegrees()[index];
		else
			return plan.getDegrees().get(index);
	}

	public int getSize() {
		if(database != null)
			return database.listDegrees().length;
		else
			return plan.getDegrees().size();
	}
	
	public void degreeAdded(){
		fireIntervalAdded(this, getSize()-1, getSize()-1);
	}
	
	public void degreeRemoved(){
		fireContentsChanged(this, 0, getSize());
	}

	public void newPlan(PlanOfStudy plan2) {
		this.plan = plan2;
		fireContentsChanged(this, 0, getSize());
	}
}
