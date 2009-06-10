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

package rpiplanner.model;

import java.util.ArrayList;
import rpiplanner.validation.degree.Degree;
import rpiplanner.validation.requirements.CoreRequirement;
import rpiplanner.validation.requirements.HumanitiesRequirement;
import rpiplanner.validation.requirements.FreeElectiveRequirement;
import rpiplanner.validation.requirements.RestrictedRequirement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DegreeDatabase")
public class DegreeDatabase {
    ArrayList<Degree> database = new ArrayList<Degree>();

    public DegreeDatabase() {
        createTestDegree();
        createAnotherTestDegree();
    }

    private void createTestDegree() {
        Degree testDegree = new Degree("Test Degree 2", 128);
        String name = "Core Requirement";
        String desc = "Testing the Core Requirement Validator";
        CoreRequirement coreMathScienceReq = new CoreRequirement(name, desc);

        coreMathScienceReq.addCourse(Course.get("CSCI","1100"));
        coreMathScienceReq.addCourse(Course.get("CSCI","1200"));
        coreMathScienceReq.addCourse(Course.get("CSCI","2300"));
        coreMathScienceReq.addCourse(Course.get("MATH","1010"));
        coreMathScienceReq.addCourse(Course.get("MATH","1020"));
        coreMathScienceReq.addCourse(Course.get("MATH","2400"));
        coreMathScienceReq.addCourse(Course.get("MATH","2800"));
        coreMathScienceReq.addCourse(Course.get("PHYS","1100"));
        coreMathScienceReq.addCourse(Course.get("PHYS","1200"));
        coreMathScienceReq.addCourse(Course.get("CHEM","1100"));

        coreMathScienceReq.addReplacementCourse(Course.get("MATH","1010"), Course.get("ARTS","1010"));

        testDegree.addRequirement(coreMathScienceReq);

        HumanitiesRequirement humReq = new HumanitiesRequirement();
        testDegree.addRequirement(humReq);

        FreeElectiveRequirement freeReq = new FreeElectiveRequirement(12);
        testDegree.addRequirement(freeReq);

        database.add(testDegree);
    }

    private void createAnotherTestDegree() {
        Degree newDegree = new Degree("Computer & Systems Engineering", 128);
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
		coreMathScienceReq.addCourse(Course.get("PHYS","1200"));
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
		coreMajorReq.addCourse(Course.get("ECSE","2410"));
		coreMajorReq.addCourse(Course.get("ECSE","4500"));
		coreMajorReq.addReplacementCourse(Course.get("ECSE","4500"),
				Course.get("ENGR","2600"));
		
		newDegree.addRequirement(coreMajorReq);
		
		RestrictedRequirement newRestReq;

		String restReqDesc = "Choose one of the following courses.";
		String restReqName = "Multidisciplinary Engineering Elective";
		int restReqNumClasses = 1;
		int restReqNumCredits = 4;
		newRestReq = new RestrictedRequirement(restReqName,
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

        HumanitiesRequirement humReq = new HumanitiesRequirement();
        newDegree.addRequirement(humReq);

        FreeElectiveRequirement freeReq = new FreeElectiveRequirement(16);
        newDegree.addRequirement(humReq);

        database.add(newDegree);
    }

    public Degree[] listDegrees() {
        return database.toArray(new Degree[0]);  //To change body of created methods use File | Settings | File Templates.
    }
}
