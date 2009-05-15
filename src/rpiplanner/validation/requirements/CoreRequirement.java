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

package rpiplanner.validation.requirements;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import rpiplanner.model.Course;
import rpiplanner.validation.interfaces.Validatable;
import rpiplanner.validation.degree.DegreeSection;

import java.util.ArrayList;
import java.util.HashMap;


public class CoreRequirement implements Validatable
{
    String name;
	String description;

	@XStreamImplicit(itemFieldName="course")
	ArrayList<Course> reqCourse;
	

	HashMap<Course, ArrayList<Course>> replacementCourses;


	CoreRequirement(String name, String description)
	{
		this.name = name;
		this.description = description;

		reqCourse = new ArrayList<Course>();
		replacementCourses = new HashMap<Course, ArrayList<Course> >();

	}

	public String getName()
	{
		return name;
	}

	public int getNumCourses ()
	{
		return reqCourse.size();
	}

	public void addCourse (Course course)
	{
		reqCourse.add(course);
	}

	public void addReplacementCourse (Course originalCourse,Course replacementCourse)
	{
		if (!replacementCourses.containsKey(originalCourse))
		{
			replacementCourses.put(originalCourse, new ArrayList<Course>());
		}
		replacementCourses.get(originalCourse).add(replacementCourse);
	}

	public String getDescription()
    {
		return description;
	}

    public Boolean hasReplacementCourse(Course course)
    {
        return replacementCourses.containsKey(course);
    }

    public Course[] getCourses() {
        Course[] tmp = new Course[reqCourse.size()];
        reqCourse.toArray(tmp);
        return tmp;
    }

    public Course[] getReplacementCourses(Course course) {
        if (replacementCourses.containsKey(course))
            return replacementCourses.get(course).toArray(new Course[replacementCourses.get(course).size()]);  //To change body of created methods use File | Settings | File Templates.
        else
            return null;
    }

    public DegreeSection validate(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList) {
         DegreeSection newSection = new DegreeSection();
            newSection.name = this.getName();
            newSection.description = this.getDescription();


                //TODO:Create fuction for this block
                for (Course course : this.getCourses())
                {
                    Boolean found = false;
                    //Check to see if we have taken this course;
                    if (courseMap.containsKey(course))
                    {
                        if (courseMap.get(course) > 0)
                        {
                            newSection.appliedCourses.add(course);
                            newSection.credits += course.getCredits();
                            int number = courseMap.get(course);
                            number--;
                            courseMap.put(course,number);
                            found = true;

                        }
                    }
                    if (this.hasReplacementCourse(course) && !found)
                    {
                        //Check for replacement courses for the current course.
                        for (Course repCourse : getReplacementCourses(course))
                        {
                            if (courseMap.containsKey(repCourse))
                            {   //TODO: Do we want the applied course to show original course or rep course?
                                newSection.appliedCourses.add(repCourse);
                                newSection.credits += course.getCredits();
                                int number = courseMap.get(repCourse);
                                number--;
                                courseMap.put(repCourse,number);
                                found = true;
                            }
                        }
                    }
                    //We didn't find the course so it must be missing.
                   if (!found)
                    {
                        //TODO:Check to see if we want the replacement course to be in missing courses also.
                        newSection.missingCourses.add(course);
                        newSection.potentialCourses.add(course);
                        if (hasReplacementCourse(course))
                        {
                            for (Course repCourse : getReplacementCourses(course))
                            {
                                if (courseMap.containsKey(repCourse))
                                {
                                    newSection.potentialCourses.add(repCourse);
                                }
                            }
                        }

                    }
                }
        return newSection;
    }
}