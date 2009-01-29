package rpiplanner;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import rpiplanner.model.Degree;
import rpiplanner.model.EditableCourse;
import rpiplanner.model.PlanOfStudy;

public class PlanValidatorTest {
	// TODO
	private PlanValidator validator = PlanValidator.getInstance();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvalCodeContainedInDegree(){
		Degree degree = new Degree("test degree", "$errors << 'fail'");
		PlanOfStudy plan = new PlanOfStudy();

		assertFalse(validator.satisfiesDegree(plan, degree));

		degree = new Degree("good degree", "true");
		assertTrue(validator.satisfiesDegree(plan, degree));
	}

	@Test
	public void testIterationOverCourses(){
		String validationCode = "each_course do |course|" +
				"$errors << 'foo'" +
				"end";
		Degree degree = new Degree("some stuff", validationCode);
		PlanOfStudy plan = new PlanOfStudy();
		EditableCourse course = new EditableCourse();
		course.setCatalogNumber("XXXX-1000");
		assertTrue(validator.satisfiesDegree(plan, degree));

		plan.getTerm(0).add(course);
		assertFalse(validator.satisfiesDegree(plan, degree));

		assertEquals("foo", validator.validate(plan, degree)[0].toString());
	}
}
