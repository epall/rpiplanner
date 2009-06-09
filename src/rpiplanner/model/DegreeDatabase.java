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

import rpiplanner.validation.degree.Degree;
import rpiplanner.validation.requirements.RestrictedRequirement;

import java.util.ArrayList;

public class DegreeDatabase {
    ArrayList<Degree> database = new ArrayList<Degree>();

    public DegreeDatabase() {
        rpiplanner.validation.degree.Degree testDegree = new rpiplanner.validation.degree.Degree("Test Degree", 128);
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

        database.add(testDegree);  
    }

    public Degree[] listDegrees() {
        Degree[] list = new Degree[database.size()];
        database.toArray(list);
        return list;
    }
}
