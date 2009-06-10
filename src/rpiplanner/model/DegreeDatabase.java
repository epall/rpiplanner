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


public class DegreeDatabase {
    ArrayList<Degree> database = new ArrayList<Degree>();

    public DegreeDatabase() {
        createTestDegree();
    }

    private void createTestDegree() {
        Degree testDegree = new Degree("Test Degree 2", 128);
        String name = "Test Requirement 2";
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

        database.add(testDegree);

        createAnotherTestDegree();
    }

    private void createAnotherTestDegree() {
        Degree testDegree = new Degree("Test Degree 1", 128);
        String name = "Test Requirement 1";
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
        coreMathScienceReq.addCourse(Course.get("PHYS","2400"));
        coreMathScienceReq.addCourse(Course.get("CHEM","1100"));

        coreMathScienceReq.addReplacementCourse(Course.get("MATH","1010"), Course.get("ARTS","1010"));

        testDegree.addRequirement(coreMathScienceReq);

        database.add(testDegree);
    }

    public Degree[] listDegrees() {
        return database.toArray(new Degree[0]);  //To change body of created methods use File | Settings | File Templates.
    }
}
