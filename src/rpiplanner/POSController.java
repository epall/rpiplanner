package rpiplanner;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;

public class POSController {
	protected rpiplanner.model.PlanOfStudy plan;
	protected rpiplanner.view.PlanOfStudy view;
	private ArrayList<JPanel> semesterPanels;
	private CourseDatabase courseDatabase;
	
	public POSController(rpiplanner.view.PlanOfStudy view){
		this.view = view;
		view.setController(this);
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
				p.add(new JLabel("Add Course"));
			}
		}
	}

	public void setCourseDatabase(CourseDatabase courseDatabase) {
		this.courseDatabase = courseDatabase;
	}
}
