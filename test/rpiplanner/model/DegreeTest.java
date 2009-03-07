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
