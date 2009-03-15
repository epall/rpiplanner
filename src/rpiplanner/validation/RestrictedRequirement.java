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

import java.util.ArrayList;
import java.util.HashMap;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RestrictedRequirement")
public class RestrictedRequirement
{
    String name;
	String description;

	int numCredits = 0;
	int numCourses = 0;


	@XStreamImplicit(itemFieldName="course")
	ArrayList<Course> reqCourse = new ArrayList<Course>();

	HashMap<Course, ArrayList<Course>> replacementCourses;


	RestrictedRequirement(String name,String description,int numCredits,int numCourses)
	{
		this.name = name;
		this.description = description;
		this.numCredits = numCredits;
		this.numCourses = numCourses;

		reqCourse = new ArrayList<Course>();
		replacementCourses = new HashMap<Course, ArrayList<Course> >();

	}

    public RestrictedRequirement(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public String getName()
	{
		return name;
	}

	public int getNumCourses()
	{
		return numCourses;
	}

	public int getNumCredits()
	{
		return numCredits;
	}
    public String getDescription()
    {
        return description;
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

    public ArrayList<Course> getCourses() {
        return reqCourse;
    }

    public boolean hasReplacementCourse(Course course) {
        return replacementCourses.containsKey(course);
    }

    public ArrayList<Course> getReplacementCourses(Course course) {
        return replacementCourses.get(course);
    }
}
