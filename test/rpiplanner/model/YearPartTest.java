package rpiplanner.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class YearPartTest {
	@Test
	public void testToString() {
		assertEquals("Spring", YearPart.SPRING.toString());
		assertEquals("Fall", YearPart.FALL.toString());
	}
}
