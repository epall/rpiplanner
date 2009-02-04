package rpiplanner.view;

import org.junit.Test;
import org.uispec4j.UISpecTestCase;

import rpiplanner.TestUILauncher;

/**
 * Integration tests to verify regressions / bugs
 * @author Eric Allen
 *
 */
public class UIRegressionTests extends UISpecTestCase {
	public void setUp() throws Exception{
		super.setUp();
		setAdapter(new TestUILauncher());
	}
	
	/**
	 * Simple example to demonstrate UISpec4j
	 */
	@Test public void testTitle(){
		assertEquals("RPI Planner", getMainWindow().getTitle());
	}
}
