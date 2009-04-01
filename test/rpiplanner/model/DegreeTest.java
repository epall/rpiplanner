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
