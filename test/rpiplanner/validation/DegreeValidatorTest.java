package rpiplanner.validation;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

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
	private static CourseDatabase courseDatabase;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		XStream xs = new XStream();
		xs.processAnnotations(PlanOfStudy.class);
		xs.processAnnotations(Course.class);
		xs.processAnnotations(DefaultCourseDatabase.class);
		xs.processAnnotations(ShadowCourseDatabase.class);
		xs.processAnnotations(YearPart.class);
		xs.registerConverter(new RequisiteSetConverter(xs.getMapper()));
		InputStream databaseStream = new FileInputStream("course_database.xml");
		DefaultCourseDatabase mainDB = (DefaultCourseDatabase) xs.fromXML(databaseStream);

		ShadowCourseDatabase shadow = new ShadowCourseDatabase();
		shadow.shadow(mainDB);
		
		courseDatabase = shadow;
	}

	@Test
	public void testValidate() {
		Degree csys = courseDatabase.getDegree(2);
		PlanOfStudy plan = new PlanOfStudy();
		plan.getTerm(0).add(courseDatabase.getCourse("CSCI-1100"));

		DegreeValidator validator = csys.getValidator();
		ValidationResult validationOutput = validator.validate(plan);
		assertNotNull(validationOutput);

		Section sectionResults = validationOutput
				.getSectionResults("Math & Science");

		boolean found = false;
		for (String o : sectionResults.missingCourses()) {
			if ("CSCI-1100".equals(o))
				found = true;
		}
		assertFalse(found);

		found = false;
		for (String o : sectionResults.appliedCourses()) {
			if ("CSCI-1100".equals(o))
				found = true;
		}
		assertTrue(found);

		found = false;
		for (String o : sectionResults.missingCourses()) {
			if ("CSCI-1200".equals(o))
				found = true;
		}
		assertTrue(found);
	}
}
