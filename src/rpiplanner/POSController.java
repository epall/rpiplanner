package rpiplanner;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.model.Term;
import rpiplanner.view.CourseTransferHandler;
import rpiplanner.view.PlanOfStudyEditor;

public class POSController {
	protected rpiplanner.model.PlanOfStudy plan;
	protected rpiplanner.view.PlanOfStudyEditor view;
	private ArrayList<JPanel> semesterPanels;
	private CourseDatabase courseDatabase;
	
	public POSController(){
		plan = new rpiplanner.model.PlanOfStudy();
	}

	public void setSemesterPanels(ArrayList<JPanel> semesterPanels) {
		this.semesterPanels = semesterPanels;
		for(int i = 0; i < semesterPanels.size(); i++){
			JPanel p = semesterPanels.get(i);
			p.setTransferHandler(new CourseTransferHandler(this));
			updateSemesterPanel(i, plan.getTerm(i));
		}
	}

	public void setCourseDatabase(CourseDatabase courseDatabase) {
		this.courseDatabase = courseDatabase;
	}

	public rpiplanner.model.PlanOfStudy getPlan() {
		return plan;
	}
	
	public void setPlan(rpiplanner.model.PlanOfStudy plan){
		this.plan = plan;
	}

	public ListModel getCourseListModel() {
		return courseDatabase;
	}

	public void setView(PlanOfStudyEditor planCard) {
		this.view = planCard;
		view.setController(this);
	}

	public void addCourse(int term, Course toAdd) {
		Term toModify = plan.getTerm(term);
		toModify.getCourses().add(toAdd);
		updateSemesterPanel(term, toModify);
	}

	private void updateSemesterPanel(int term, Term model) {
		ArrayList<Course> courses = model.getCourses();

		JPanel semesterPanel = semesterPanels.get(term);
		semesterPanel.removeAll();
		for(int i = 0; i < SchoolInformation.DEFAULT_COURSES_PER_SEMESTER; i++){
			try{
				semesterPanel.add(new JLabel(courses.get(i).toString()));
			} catch (IndexOutOfBoundsException e){ // no course there yet
				semesterPanel.add(new JLabel("Add Course..."));
			}
		}
		semesterPanel.validate();
	}

	public void initializeTerms(int startingYear) {
		plan.setStartingYear(startingYear);
		plan.rebuildTerms();
	}
}
