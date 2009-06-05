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

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import rpiplanner.model.Course;
import rpiplanner.validation.requirements.*;
import rpiplanner.validation.degree.Degree;
import rpiplanner.validation.degree.DegreeValidationResult;
import rpiplanner.Fixtures;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

public class DegreeTest
{
    @Before
    public void setUp()
    {
        Fixtures.getCourseDatabase();
    }

    @After
    public void tearDown()
    {
        // Add your code here
    }

    @Test
    public void testValidateCoreRequirement()
    {
        Degree testDegree = new Degree("Test Degree", 128);
        String name = "Test Requirement";
        String desc = "Testing the Core Requirement Validator";
        CoreRequirement coreMathScienceReq = new CoreRequirement(name, desc);

        assertNotNull(Course.get("CSCI","1100"));

		coreMathScienceReq.addCourse(Course.get("CSCI","1100"));
		coreMathScienceReq.addCourse(Course.get("CSCI","1200"));
		coreMathScienceReq.addCourse(Course.get("CSCI","2300"));
		coreMathScienceReq.addCourse(Course.get("MATH","1010"));
		coreMathScienceReq.addCourse(Course.get("MATH","1020"));
		coreMathScienceReq.addCourse(Course.get("MATH","2400"));
		coreMathScienceReq.addCourse(Course.get("MATH","2800"));
		coreMathScienceReq.addCourse(Course.get("PHYS","1100"));
		coreMathScienceReq.addCourse(Course.get("PHYS","2400"));
		coreMathScienceReq.addCourse(Course.get("CHEM","1100"));

        coreMathScienceReq.addReplacementCourse(Course.get("MATH","1010"), Course.get("ARTS","1010"));

        testDegree.addRequirement(coreMathScienceReq);

        ArrayList<Course> courseList = new ArrayList<Course>();

        courseList.add(Course.get("CSCI","1100"));
        courseList.add(Course.get("CSCI","1200"));
        courseList.add(Course.get("MATH","1010"));

        ArrayList<Course> missingCourseList = new ArrayList<Course>();

        missingCourseList.add(Course.get("CSCI","2300"));
        missingCourseList.add(Course.get("MATH","1020"));
        missingCourseList.add(Course.get("MATH","2400"));
        missingCourseList.add(Course.get("MATH","2800"));
        missingCourseList.add(Course.get("PHYS","1100"));
        missingCourseList.add(Course.get("PHYS","2400"));
        missingCourseList.add(Course.get("CHEM","1100"));


        DegreeValidationResult result = testDegree.validate(courseList);

        result.getSectionResults("Test Requirement");

        assertEquals(courseList, result.getSectionResults("Test Requirement").appliedCourses());
        assertEquals(missingCourseList, result.getSectionResults("Test Requirement").missingCourses());
        assertFalse(result.getSectionResults("Test Requirement").isSuccess());

    }
    @Test
    public void testValidateRestrictedElective ()
    {
        Degree testDegree = new Degree("Test Degree", 128);
        String name = "Test Requirement";
        String desc = "Testing the Restricted Requirement Validator";
        RestrictedRequirement multiReq = new RestrictedRequirement(name, desc);
        multiReq.setNumCredits(4);
        multiReq.setNumCourses(1);

		multiReq.addCourse(Course.get("ENGR","1600"));
		multiReq.addCourse(Course.get("ENGR","2090"));
		multiReq.addCourse(Course.get("ENGR","2250"));
		multiReq.addCourse(Course.get("ENGR","2530"));

        multiReq.addReplacementCourse(Course.get("MATH","1010"), Course.get("ARTS","1010"));

        testDegree.addRequirement(multiReq);

        ArrayList<Course> courseList = new ArrayList<Course>();

        courseList.add(Course.get("CSCI","1100"));
        courseList.add(Course.get("CSCI","1200"));
        courseList.add(Course.get("ARTS","1010"));
        courseList.add(Course.get("ENGR","2530"));


        DegreeValidationResult result = testDegree.validate(courseList);

        result.getSectionResults("Test Requirement");
        assertTrue(result.getSectionResults("Test Requirement").isSuccess());

        //Shows what I need to fix
        courseList.add(Course.get("ENGR","2250"));
        result = testDegree.validate(courseList);
        assertTrue(result.getSectionResults("Test Requirement").isSuccess());

    }
    

}
