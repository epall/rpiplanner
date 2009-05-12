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

public class DegreeSection implements ISection
{
    String name;
    String description;
    int credits;

    ArrayList<Course> missingCourses;
    ArrayList<Course> appliedCourses;
    ArrayList<Course> potentialCourses;

    public DegreeSection()
    {
        missingCourses = new ArrayList<Course>();
        appliedCourses = new ArrayList<Course>();
        potentialCourses = new ArrayList<Course>();
    }

    public boolean isSuccess()
    {
       return (missingCourses.size() == 0 && potentialCourses.size() == 0);
    }
    public ArrayList<Course> missingCourses()
    {
        return missingCourses;
    }
    public ArrayList<Course> appliedCourses()
    {
        return appliedCourses;
    }
    public String messages()
    {
        return description;
    }
    public ArrayList<Course> potentialCourses()
    {
        return potentialCourses;
    }
}
