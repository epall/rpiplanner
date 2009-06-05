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

    @Test
    public void testStruff () {

        Degree newDegree = new Degree("B.S. Computer and Systems Engineering", 128);

		initializeDegree(newDegree);


		XStream xstream = new XStream();

		xstream.processAnnotations(Degree.class);
		xstream.processAnnotations(CoreRequirement.class);
		xstream.processAnnotations(RestrictedRequirement.class);
		xstream.processAnnotations(SubjectRequirement.class);
        xstream.processAnnotations(HumanitiesRequirement.class);
        xstream.processAnnotations(FreeElectiveRequirement.class);
		xstream.processAnnotations(Course.class);
		String xml = xstream.toXML(newDegree);

		System.out.println(xml);

		Degree duplicateDegree = (Degree) xstream.fromXML(xml);
    }
    static void initializeDegree (Degree newDegree)
	{
		CoreRequirement coreMathScienceReq = new CoreRequirement("Math and Science Core",
		"Must fufill all course requirements below.");

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

		newDegree.addRequirement(coreMathScienceReq);

		CoreRequirement coreEngineeringReq = new CoreRequirement("Core Engineering Courses",
				"Must fufill all course requirements below.");

		coreEngineeringReq.addCourse(Course.get("ENGR","1100"));
		coreEngineeringReq.addCourse(Course.get("ENGR","2350"));
		coreEngineeringReq.addCourse(Course.get("ENGR","1200"));
		coreEngineeringReq.addCourse(Course.get("ENGR","1310"));
		coreEngineeringReq.addCourse(Course.get("ENGR","2050"));
		coreEngineeringReq.addCourse(Course.get("ENGR","4010"));

		newDegree.addRequirement(coreEngineeringReq);

		CoreRequirement coreMajorReq = new CoreRequirement("Required Major Courses",
				"Choose either ECSE 4500 or ENGR 2600 (with advisor's approval).");

		coreMajorReq.addCourse(Course.get("ECSE","2610"));
		coreMajorReq.addCourse(Course.get("ECSE","2660"));
		coreMajorReq.addCourse(Course.get("ECSE","2010"));
		coreMajorReq.addCourse(Course.get("ECSE","2410"));
		coreMajorReq.addCourse(Course.get("ECSE","4500"));
		coreMajorReq.addReplacementCourse(Course.get("ECSE","4500"),
				Course.get("ENGR","2600"));

		newDegree.addRequirement(coreMajorReq);


		String restReqDesc = "Choose one of the following courses.";
		String restReqName = "Multidisciplinary Engineering Elective";
		int restReqNumClasses = 1;
		int restReqNumCredits = 4;
		RestrictedRequirement newRestReq = new RestrictedRequirement(restReqName,
				restReqDesc);
        newRestReq.setNumCourses(restReqNumClasses);
        newRestReq.setNumCredits(restReqNumCredits);
		newRestReq.addCourse(Course.get("ENGR","1600"));
		newRestReq.addCourse(Course.get("ENGR","2090"));
		newRestReq.addCourse(Course.get("ENGR","2550"));
		newRestReq.addCourse(Course.get("ENGR","2530"));


		newDegree.addRequirement(newRestReq);

		restReqName = "Software Engineering Elective";
		restReqDesc = "Choose one of the following courses.";
		restReqNumClasses = 1;
		restReqNumCredits = 3;
		newRestReq = new RestrictedRequirement(restReqName,
				restReqDesc);
        newRestReq.setNumCourses(restReqNumClasses);
        newRestReq.setNumCredits(restReqNumCredits);
		newRestReq.addCourse(Course.get("ECSE","4690"));
		newRestReq.addCourse(Course.get("ECSE","4750"));
		newRestReq.addCourse(Course.get("CSCI","4380"));
		newRestReq.addCourse(Course.get("CSCI","4440"));
		newRestReq.addCourse(Course.get("CSCI","4600"));

        newDegree.addRequirement(newRestReq);


		restReqName = "Design Elective";
		restReqDesc = "Choose one of the following courses.";
		restReqNumClasses = 1;
		restReqNumCredits = 3;
		newRestReq = new RestrictedRequirement(restReqName,
				restReqDesc);
        newRestReq.setNumCourses(restReqNumClasses);
        newRestReq.setNumCredits(restReqNumCredits);
		newRestReq.addCourse(Course.get("ECSE","4780"));
		newRestReq.addCourse(Course.get("ECSE","4900"));
		newRestReq.addCourse(Course.get("MANE","4220"));
		newRestReq.addCourse(Course.get("EPOW","4850"));

        newDegree.addRequirement(newRestReq);

        FreeElectiveRequirement freeElec = new FreeElectiveRequirement(16);
        newDegree.addRequirement(freeElec);

        HumanitiesRequirement humReq = new HumanitiesRequirement();
        newDegree.addRequirement(humReq);



	}
    

}
