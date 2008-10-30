package rpiplanner.model;

import java.util.ArrayList;

import rpiplanner.SchoolInformation;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("plan")
public class PlanOfStudy {
	protected String fullname;
	protected String school;
	protected String studentID;
	protected int startingYear;
	protected ArrayList<Degree> degrees;

	@XStreamImplicit
	protected ArrayList<Term> terms;
	
	public PlanOfStudy(){
		initializeTerms();
	}

	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public int getStartingYear() {
		return startingYear;
	}
	public void setStartingYear(int startingYear) {
		this.startingYear = startingYear;
	}
	public Term getTerm(int index){
		return terms.get(index);
	}
	public ArrayList<Degree> getDegrees() {
		if(degrees == null)
			degrees = new ArrayList<Degree>();
		return degrees;
	}
	public void setDegrees(ArrayList<Degree> degrees) {
		this.degrees = degrees;
	}

	public void rebuildTerms(){
		terms.get(0).setYear(startingYear);
		for(int i = 1; i < SchoolInformation.DEFAULT_NUM_SEMESTERS/2; i++){
			terms.get(2*i).setYear(startingYear+i);
			terms.get(2*i-1).setYear(startingYear+i);
		}
		terms.get(terms.size()-1).setYear(startingYear+SchoolInformation.DEFAULT_NUM_SEMESTERS/2);
	}
	
	/**
	 * Initialize terms according to school. For now this is a simple
	 * prototype that uses RPI's semester-based term system
	 */
	private void initializeTerms(){
		terms = new ArrayList<Term>(SchoolInformation.DEFAULT_NUM_SEMESTERS);
		Term firstTerm = new Term();
		firstTerm.setYear(startingYear);
		firstTerm.setTerm(YearPart.FALL);

		for(int i = 0; i < SchoolInformation.DEFAULT_NUM_SEMESTERS/2; i++){
			Term spring = new Term();
			spring.setYear(startingYear+i);
			spring.setTerm(YearPart.SPRING);

			terms.add(spring);
			Term fall = new Term();
			fall.setYear(startingYear+i);
			fall.setTerm(YearPart.SPRING);
			terms.add(fall);
		}
		Term lastTerm = new Term();
		lastTerm.setYear(startingYear + SchoolInformation.DEFAULT_NUM_SEMESTERS/2);
		lastTerm.setTerm(YearPart.SPRING);
	}
}
