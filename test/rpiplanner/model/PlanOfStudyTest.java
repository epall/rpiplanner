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

import static org.junit.Assert.*;

import org.junit.*;
import rpiplanner.Fixtures;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


public class PlanOfStudyTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        // ensure course database is loaded
        Fixtures.getCourseDatabase();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test @Ignore
	public void testPlanOfStudy() {
		fail("Not yet implemented");
	}

	@Test @Ignore
	public void testRebuildTerms() {
		fail("Not yet implemented");
	}

	@Test @Ignore
	public void testSetTerms() {
		fail("Not yet implemented");
	}

    @Test
    public void testGetCourses(){
        PlanOfStudy plan = new PlanOfStudy();
        Term t1 = new Term();
        t1.plan = plan;
        t1.add(Course.get("CSCI-1100"));
        Term t2 = new Term();
        t2.plan = plan;
        t1.add(Course.get("CSCI-1200"));

        ArrayList<Term> terms = new ArrayList<Term>();
        terms.add(t1);
        terms.add(t2);
        plan.setTerms(terms);

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(Course.get("CSCI-1100"));
        courses.add(Course.get("CSCI-1200"));

        assertEquals((List<Course>)courses, plan.getCourses());
    }
}
