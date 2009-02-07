package rpiplanner.model;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import rpiplanner.Fixtures;
import rpiplanner.xml.RequisiteSetConverter;

import com.thoughtworks.xstream.XStream;

import static org.junit.Assert.*;

public class DegreeTest {
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
	public void testListSections(){
		Degree csys = Fixtures.getCSYS();
		assertNotNull(csys);
		
		String[] sections = csys.getSectionNames();
		boolean found = false;
		for(String s : sections){
			if("Required Courses".equals(s))
				found = true;
		}
		assertTrue("section not found", found);
	}
}
