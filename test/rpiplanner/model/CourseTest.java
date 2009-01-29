/* RPI Planner - Customized plans of study for RPI students.
 *
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package rpiplanner.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for rpiplanner.model.Course
 * @author Eric Allen
 *
 */
public class CourseTest {
	@Test
	public void testGetTitle() {
		Course foo = new Course();
		foo.getTitle();
	}

	@Test
	public void testCompareTo() {
		Course a = new Course();
		a.setCatalogNumber("MATH-1010");
		a.setTitle("Calculus X");
		Course b = new Course();
		b.setCatalogNumber("MATH-1020");

		// alphabetical by catalog number
		assertEquals(a.compareTo(b), a.getCatalogNumber().compareTo(b.getCatalogNumber()));
	}

	@Test
	public void testEqualsObject() {
		Course a = new Course();
		a.setCatalogNumber("MATH-1010");
		a.setTitle("Calculus X");
		Course b = new Course();
		b.setCatalogNumber("MATH-1020");
		Course c = new Course();
		c.setCatalogNumber("MATH-1010");
		c.setTitle("Calculus I");

		assertFalse(a.equals(b));
		assertTrue(a.equals(c));
	}

}
