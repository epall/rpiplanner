package rpiplanner.view;

import javax.swing.JProgressBar;

import org.junit.BeforeClass;
import org.junit.Test;
import org.uispec4j.Panel;
import org.uispec4j.UISpec4J;
import org.uispec4j.assertion.UISpecAssert;

import rpiplanner.Fixtures;
import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;

public class DegreeProgressPanelTest {
	@BeforeClass
	public static void setUp(){
		UISpec4J.init();
	}
	
	@Test
	public void testProgress(){
		PlanOfStudy plan = new PlanOfStudy();
		Degree csys = Fixtures.getCourseDatabase().getDegree(2);
		DegreeProgressPanel panel = new DegreeProgressPanel(plan, csys);
		
		Panel panelWrapper = new Panel(panel);
		UISpecAssert.assertThat(panelWrapper.containsSwingComponent(JProgressBar.class));
		
		UISpecAssert.assertThat(panelWrapper.getProgressBar().completionEquals(50));
	}
}
