package rpiplanner;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.view.PlanOfStudyEditor;

public class POSController {
	protected rpiplanner.model.PlanOfStudy plan;
	protected rpiplanner.view.PlanOfStudyEditor view;
	private ArrayList<JPanel> semesterPanels;
	private CourseDatabase courseDatabase;
	
	public POSController(){
		plan = new rpiplanner.model.PlanOfStudy();
	}

	public void updateUserInfo(String name, String school, String studentID) {
		plan.setFullname(name);
		plan.setSchool(school);
		plan.setStudentID(studentID);
	}

	public void setSemesterPanels(ArrayList<JPanel> semesterPanels) {
		this.semesterPanels = semesterPanels;
		for(JPanel p : semesterPanels){
			for(int i = 0; i < SchoolInformation.DEFAULT_COURSES_PER_SEMESTER; i++){
				p.add(new JLabel("ENGR-2350 Embeded Control"));
			}
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

	public void setView(PlanOfStudyEditor planCard) {
		this.view = planCard;
		view.setController(this);
	}
}
