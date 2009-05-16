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
import rpiplanner.validation.interfaces.Requirement;

import java.util.ArrayList;
import java.util.HashMap;

public class HumanitiesRequirement extends Requirement
{
    public DegreeSection validate(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList) {
        DegreeSection humSSSection = new DegreeSection();
        humSSSection.setName("Humanities and Social Sciences");
        humSSSection.setDescription("Humanities and Social Sciences Stuff");

        Boolean pdReq = false;
        Boolean depthReq = false;

        int numHum = 0;
        int numIHSS = 0;
        int numSS = 0;

        ArrayList<Course> humCourses = new ArrayList<Course>();
        checkPD2(courseMap, courseList, humSSSection);


        //TODO: Check Depth Requirement
        //TODO: Check 4000 level requirement
        //TODO: Check Humanities
        //TODO: Check Social Sciences


        return humSSSection;
    }

    private void checkPD2(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList, DegreeSection humSSSection) {
        Boolean pdReq;//Professional Development II
        if (courseMap.containsKey(Course.get("PSYC", "4170")) || courseMap.containsKey(Course.get("STSS", "4840"))) {
            if (courseMap.containsKey(Course.get("PSYC", "4170"))) {
                courseMap.put(Course.get("PSYC", "4170"), 0);
                humSSSection.addAppliedCourse(Course.get("PSYC", "4170"));
                pdReq = true;
                courseList.remove(Course.get("PSYC", "4170"));
            }
            if (courseMap.containsKey(Course.get("STSS", "4840"))) {
                courseMap.put(Course.get("STSS", "4840"), 0);
                humSSSection.addAppliedCourse(Course.get("STSS", "4840"));
                pdReq = true;
                courseList.remove(Course.get("STSS", "4840"));
            }
        } else {
            humSSSection.addMissingCourse(Course.get("PSYC", "4170"));
            humSSSection.addMissingCourse(Course.get("STSS", "4840"));
            humSSSection.addPotentialCourse(Course.get("PSYC", "4170"));
            humSSSection.addPotentialCourse(Course.get("STSS", "4840"));
        }
    }
}
