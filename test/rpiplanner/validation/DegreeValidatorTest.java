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

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import rpiplanner.Fixtures;
import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.model.DefaultCourseDatabase;
import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.model.ShadowCourseDatabase;
import rpiplanner.model.YearPart;
import rpiplanner.validation.ValidationResult.Section;
import rpiplanner.xml.RequisiteSetConverter;

import com.thoughtworks.xstream.XStream;


public class DegreeValidatorTest {

    @BeforeClass
    public static void setUpBeforeClass(){
        Fixtures.getCourseDatabase();
    }
    
	@Test
	public void testValidate() {
		Degree csys = Fixtures.getCSYS();
		PlanOfStudy plan = new PlanOfStudy();
		plan.getTerm(0).add(Course.get("CSCI-1100"));

		DegreeValidator validator = csys.getValidator();
		ValidationResult validationOutput = validator.validate(plan);
		assertNotNull(validationOutput);

		Section sectionResults = validationOutput
				.getSectionResults("Math & Science");

		boolean found = false;
		for (Course c : sectionResults.missingCourses()) {
			if ("CSCI-1100".equals(c.getCatalogNumber()))
				found = true;
		}
		assertFalse(found);

		found = false;
		for (Course c : sectionResults.appliedCourses()) {
			if ("CSCI-1100".equals(c.getCatalogNumber()))
				found = true;
		}
		assertTrue(found);

		found = false;
		for (Course c : sectionResults.missingCourses()) {
			if ("CSCI-1200".equals(c.getCatalogNumber()))
				found = true;
		}
		assertTrue(found);
	}
	
	@Test
	public void testOneOf(){
		Degree csys = Fixtures.getCSYS();
		PlanOfStudy plan = new PlanOfStudy();
		plan.getTerm(0).add(Course.get("ECSE-4690"));
		ValidationResult validationOutput = csys.getValidator().validate(plan);
		Section softEng = validationOutput.getSectionResults("Software Engineering Elective");
		

		boolean found = false;
		for (Course c : softEng.appliedCourses()) {
			if ("ECSE-4690".equals(c.getCatalogNumber()))
				found = true;
		}
		assertTrue(found);
		
		assertEquals(0, softEng.missingCourses().length);
	}
}
