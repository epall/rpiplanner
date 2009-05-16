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

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import rpiplanner.Fixtures;
import rpiplanner.validation.degree.DegreeValidationResult;
import rpiplanner.validation.interfaces.Section;
import rpiplanner.model.Course;

import java.util.ArrayList;
import java.util.HashMap;


public class CoreRequirementTest {
    private static final String NAME = "Core Requriement";
    private static final String DESCRIPTION = "Core Requriement";

    @BeforeClass
    public static void runBeforeClass() {
        Fixtures.getCourseDatabase();
    }

    @Test
    public void testGetName() {
        CoreRequirement requirement = new CoreRequirement(NAME,DESCRIPTION);
        assertEquals(requirement.getDescription(),DESCRIPTION);
    }

    @Test
    public void testGetNumCourses() {
        CoreRequirement requirement = new CoreRequirement(NAME,DESCRIPTION);
        assertEquals(requirement.getNumCourses(),0);
        requirement.addCourse(new Course("PHYS-1100"));
        assertEquals(requirement.getNumCourses(),1);

    }

    @Test
    public void testGetDescription() {
        CoreRequirement requirement = new CoreRequirement(NAME,DESCRIPTION);
        assertEquals(requirement.getDescription(),DESCRIPTION);
    }

    @Test
    public void testHasReplacementCourse() {
        CoreRequirement requirement = new CoreRequirement(NAME,DESCRIPTION);
        requirement.addReplacementCourse(Course.get("CSCI","1100"),Course.get("CSCI","1200"));
        assertTrue(requirement.hasReplacementCourse(Course.get("CSCI","1100")));
    }

    @Test
    public void testGetCourses() {
        CoreRequirement requirement = new CoreRequirement(NAME, DESCRIPTION);
        ArrayList<Course> courseList = new ArrayList<Course>();

        assertEquals(requirement.getCourses().size(),0);

		requirement.addCourse(Course.get("CSCI","1100"));
		requirement.addCourse(Course.get("CSCI","1200"));

        courseList.add(Course.get("CSCI","1100"));
        courseList.add(Course.get("CSCI","1200"));

        assertEquals(requirement.getCourses(),courseList);
    }

    @Test
    public void testValidate() {
        CoreRequirement requirement = new CoreRequirement(NAME, DESCRIPTION);

		requirement.addCourse(Course.get("CSCI","1100"));
		requirement.addCourse(Course.get("CSCI","1200"));
		requirement.addCourse(Course.get("CSCI","2300"));
		requirement.addCourse(Course.get("MATH","1010"));
		requirement.addCourse(Course.get("MATH","1020"));
		requirement.addCourse(Course.get("MATH","2400"));
		requirement.addCourse(Course.get("MATH","2800"));
		requirement.addCourse(Course.get("PHYS","1100"));
		requirement.addCourse(Course.get("PHYS","2400"));
		requirement.addCourse(Course.get("CHEM","1100"));

        ArrayList<Course> courseList = new ArrayList<Course>();
        HashMap<Course,Integer> courseMap = new HashMap<Course,Integer>();

        courseList.add(Course.get("CSCI","1100"));
        courseMap.put(Course.get("CSCI","1100"),1);
        courseList.add(Course.get("CSCI","1200"));
        courseMap.put(Course.get("CSCI","1200"),1);
        courseList.add(Course.get("MATH","1010"));
        courseMap.put(Course.get("MATH","1010"),1);

        ArrayList<Course> missingCourseList = new ArrayList<Course>();

        missingCourseList.add(Course.get("CSCI","2300"));
        missingCourseList.add(Course.get("MATH","1020"));
        missingCourseList.add(Course.get("MATH","2400"));
        missingCourseList.add(Course.get("MATH","2800"));
        missingCourseList.add(Course.get("PHYS","1100"));
        missingCourseList.add(Course.get("PHYS","2400"));
        missingCourseList.add(Course.get("CHEM","1100"));


        Section result = requirement.validate(courseMap,courseList);

        assertEquals(courseList, result.appliedCourses());
        assertEquals(missingCourseList, result.missingCourses());
        assertFalse(result.isSuccess());

    }
}
