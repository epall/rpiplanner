package rpiplanner;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.model.Degree;
import rpiplanner.model.Term;
import rpiplanner.model.ValidationError;
import rpiplanner.view.CourseDatabaseFilter;
import rpiplanner.view.CourseDisplay;
import rpiplanner.view.CourseTransferHandler;
import rpiplanner.view.DegreeListModel;
import rpiplanner.view.PlanOfStudyEditor;

public class POSController {
	protected rpiplanner.model.PlanOfStudy plan;
	protected rpiplanner.view.PlanOfStudyEditor view;
	private ArrayList<JPanel> semesterPanels;
	private CourseDatabase courseDatabase;
	private CourseDatabaseFilter courseDatabaseModel;
	private JPanel courseDetailsPanel;
	private DegreeListModel degreeListModel;
	private DegreeListModel planDegreeListModel;
	
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
		this.courseDatabaseModel = new CourseDatabaseFilter(courseDatabase);
		courseDatabase.addPropertyChangeListener(courseDatabaseModel);
	}
	
	public CourseDatabase getCourseDatabase(){
		return courseDatabase;
	}

	public rpiplanner.model.PlanOfStudy getPlan() {
		return plan;
	}
	
	public void setPlan(rpiplanner.model.PlanOfStudy plan){
		this.plan = plan;
	}

	public ListModel getCourseListModel() {
		return courseDatabaseModel;
	}

	public void setView(PlanOfStudyEditor planCard) {
		this.view = planCard;
		view.setController(this);
	}

	public void addCourse(int term, Course toAdd) {
		Term toModify = plan.getTerm(term);
		toModify.getCourses().add(toAdd);
		updateSemesterPanel(term, toModify);
		validatePlan();
	}

	private void updateSemesterPanel(int term, Term model) {
		ArrayList<Course> courses = model.getCourses();

		JPanel semesterPanel = semesterPanels.get(term);
		semesterPanel.removeAll();
		int numCourses = Math.max(SchoolInformation.DEFAULT_COURSES_PER_SEMESTER, courses.size());
		
		semesterPanel.setBorder(new TitledBorder(model.getTerm().toString() + " " + String.valueOf(model.getYear())));
		
		for(int i = 0; i < numCourses; i++){
			try{
				semesterPanel.add(new CourseDisplay(this, courses.get(i)));
			} catch (IndexOutOfBoundsException e){ // no course there yet
				semesterPanel.add(new CourseDisplay(this));
			}
		}
		semesterPanel.revalidate();
	}

	public void initializeTerms(int startingYear) {
		plan.setStartingYear(startingYear);
		plan.rebuildTerms();
	}

	public void removeCourse(Container semesterPanel, CourseDisplay course) {
		int semester = 0;
		int courseIndex = 0;
		// find the semester
		while(semesterPanels.get(semester) != semesterPanel)
			semester++;
		
		// find course
		while(semesterPanel.getComponent(courseIndex) != course)
			courseIndex++;

		ArrayList<Course> courses = plan.getTerm(semester).getCourses();
		courses.remove(courseIndex);
		updateSemesterPanel(semester, plan.getTerm(semester));
		validatePlan();
	}

	/**
	 * Show the details for the specified course in the course details panel
	 * @param course
	 */
	public void setDetailDisplay(Course course) {
		if(course == null)
			course = new Course();
		for(Component c : courseDetailsPanel.getComponents()){
			if(c.getName() == "department"){
				((JTextField)c).setText(course.getDepartment());
			}
			if(c.getName() == "title"){
				((JTextField)c).setText(course.getTitle());
			}
			if(c.getName() == "catalogNumber"){
				((JTextField)c).setText(course.getCatalogNumber());
			}
			if(c.getName() == "description"){
				((JTextArea)c).setText(course.getDescription());
			}
		}
	}

	public void setCourseDetailsPanel(JPanel courseDetailsPanel) {
		this.courseDetailsPanel = courseDetailsPanel;
	}

	public void searchTextChanged(String text) {
		courseDatabaseModel.setSearchText(text);
	}

	public void updatePlanFromDatabase() {
		for(int i = 0; i < SchoolInformation.DEFAULT_NUM_SEMESTERS; i++){
			Term t = plan.getTerm(i);
			ArrayList<Course> courses = t.getCourses();
			for(int j = 0; j < courses.size(); j++){
				courses.set(j, courseDatabase.getCourse(courses.get(j).getCatalogNumber()));
			}
		}
	}

	public ListModel getDegreeListModel() {
		if(degreeListModel == null){
			degreeListModel = new DegreeListModel(courseDatabase);
		}
		return degreeListModel;
	}

	public void addDegree(Degree toAdd) {
		plan.getDegrees().add(toAdd);
		planDegreeListModel.degreeAdded();
		validatePlan();
	}

	public void removeDegree(Degree selectedValue) {
		plan.getDegrees().remove(selectedValue);
		planDegreeListModel.degreeRemoved();
		validatePlan();
	}
	
	public void validatePlan(){
		for(Degree degree : plan.getDegrees()){
			ValidationError[] errors = PlanValidator.getInstance().validate(plan, degree);
			if(errors.length > 0){
				JOptionPane.showMessageDialog(null, "Degree "+degree.getName()+" not satisfied: "+errors[0].getMessage());
			}
		}
	}

	public DegreeListModel getPlanDegreeListModel() {
		if(planDegreeListModel == null)
			planDegreeListModel = new DegreeListModel(plan);
		return planDegreeListModel;
	}
}
