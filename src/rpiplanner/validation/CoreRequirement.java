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

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import rpiplanner.model.Course;

import java.util.ArrayList;
import java.util.HashMap;


public class CoreRequirement
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
}
