/*
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package rpiplanner.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import rpiplanner.validation.degree.Degree;

@XStreamAlias("courses")
public class DefaultCourseDatabase implements CourseDatabase {
	@XStreamImplicit
	private ArrayList<Course> courses = new ArrayList<Course>();
	
	@XStreamImplicit
	private ArrayList<rpiplanner.model.Degree> degrees = new ArrayList<rpiplanner.model.Degree>();

	@XStreamOmitField
	private ArrayList<String> departments;

    @XStreamOmitField
    private DegreeDatabase degreeDatabase = new DegreeDatabase();

	
    protected DefaultCourseDatabase(){}

	public int getNumCourses() {
		return courses.size();
	}

	public Course[] search(String text) {
		TreeSet<Course> found = new TreeSet<Course>();
		for(Course c : courses){
			if(c.toString().toLowerCase().contains(text.toLowerCase()))
				found.add(c);
		}
		return found.toArray(new Course[0]);
	}

	public Course[] listCourses() {
		if(courses == null)
			courses = new ArrayList<Course>();
		Course[] temp = new Course[courses.size()];
		courses.toArray(temp);
		Arrays.sort(temp);
		return temp;
	}
	
	public List<String> getDepartments(){
		if(departments == null){
			departments = new ArrayList<String>();
			for(Course c : courses){
				departments.add(c.getDepartment());
			}
		}
		return departments;
	}

	public Course getCourse(String catalogNumber) {
		for(Course c : courses){
			if(c.getCatalogNumber().equals(catalogNumber))
				return c;
		}
		return new Course(catalogNumber);
	}

	public Degree[] listDegrees() {
        if(degreeDatabase == null)
			degreeDatabase = new DegreeDatabase();
		return degreeDatabase.listDegrees();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener){
		throw new Error("Immutable course database");
	}
	public void addOrUpdate(Course updated) {
		throw new Error("Immutable course database");
	}
	public void add(Course newCourse) {
		throw new Error("Immutable course database");
	}

	public Collection<Course> getCourses() {
		return courses;
	}

	public Degree getDegree(long id) {
		//TODO: CLEAN UP
        //for(Degree d : degrees){
		//	if(d.getID() == id)
		//		return d;
		//
        // }
		return null;
	}

    public ArrayList<Course> getCourseBetween(String prefix, int lowerNumber, int upperNumber) {
        ArrayList<Course> courseList = new ArrayList<Course>();
        for (Course course : courses) {
            if (course.getNumber() >= lowerNumber &&
                    course.getNumber() < upperNumber) {
                courseList.add(course);
            }
        }
        return courseList;
    }
}
