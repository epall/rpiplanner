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

import rpiplanner.model.Course;
import rpiplanner.validation.degree.DegreeSection;
import rpiplanner.validation.interfaces.Validatable;

import java.util.ArrayList;
import java.util.HashMap;

public class FreeElectiveRequirement implements Validatable
{
    public DegreeSection validate(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList) {
        //Free Electives
        DegreeSection freeElective = new DegreeSection();
        freeElective.setName("Free Electives");
        freeElective.setDescription("Courses that are not applied to any other requirements");
        for (Course course : courseList) {
            if (courseMap.get(course) > 0) {
                freeElective.addAppliedCourse(course);
                freeElective.addCredits(course.getCredits());
                int number = courseMap.get(course);
                number--;
                courseMap.put(course, number);
                if (courseMap.get(course) > 0 && course.isDoubleCount()) {
                    for (int i = 0; i < courseMap.get(course); i++) {
                        freeElective.addAppliedCourse(course);
                        freeElective.addCredits(course.getCredits());
                    }
                }
            }
        }
        return freeElective;
    }
}
