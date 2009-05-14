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

package rpiplanner.validation;

import rpiplanner.model.Course;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

public class SubjectRequirement implements IDegreeRequirement
{

    String name;
    String description;
    int numCredits;
    int numCourses;
    ArrayList<Course> reqCourse;
    ArrayList<Subject> subjectList;

    public String getName()
	{
		return name;
	}

    public String getDescription()
    {
        return description;
    }

    public DegreeSection validate(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList) {
        DegreeSection newSection = new DegreeSection();
        newSection.name = name;
        newSection.description = description;

        //Check for specific courses required in the requirement
        for (Course course : reqCourse)
        {
                if (courseMap.get(course) > 0)
                {
                    newSection.appliedCourses.add(course);
                    int newNum = courseMap.get(course);
                    newNum--;
                    courseMap.put(course,newNum);
                } else {
                    newSection.missingCourses.add(course);
                    newSection.potentialCourses.add(course);
                }

        }
        //Get courses that apply to the subjects
        //If they exist check to see if requirement is fufilled
        //Return the section

        ArrayList<Course> applicableCourses = new ArrayList<Course>();


        return newSection;
    }
}
