package rpiplanner.model;

import java.util.ArrayList;

import rpiplanner.SchoolInformation;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("term")
public class Term {
	@XStreamImplicit
	private ArrayList<Course> courses = new ArrayList<Course>(SchoolInformation.DEFAULT_COURSES_PER_SEMESTER);
	
	private int year;
	private YearPart term;
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public YearPart getTerm() {
		return term;
	}

	public void setTerm(YearPart term) {
		this.term = term;
	}

	public ArrayList<Course> getCourses() {
		if(courses == null){
			courses = new ArrayList<Course>(SchoolInformation.DEFAULT_COURSES_PER_SEMESTER);
		}
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
}
