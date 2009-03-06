package rpiplanner.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rpiplanner.Fixtures;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


public class PlanOfStudyTest {
	// TODO
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlanOfStudy() {
		fail("Not yet implemented");
	}

	@Test
	public void testRebuildTerms() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTerms() {
		fail("Not yet implemented");
	}

    @Test
    public void testGetCourses(){
        PlanOfStudy plan = new PlanOfStudy();
        Term t1 = new Term();
        t1.plan = plan;
        t1.add(Fixtures.getCourseDatabase().getCourse("CSCI-1100"));
        Term t2 = new Term();
        t2.plan = plan;
        t1.add(Fixtures.getCourseDatabase().getCourse("CSCI-1200"));

        ArrayList<Term> terms = new ArrayList<Term>();
        terms.add(t1);
        terms.add(t2);
        plan.setTerms(terms);

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(Fixtures.getCourseDatabase().getCourse("CSCI-1100"));
        courses.add(Fixtures.getCourseDatabase().getCourse("CSCI-1200"));

        assertEquals((List<Course>)courses, plan.getCourses());
    }
}
