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

package rpiplanner.view;

import javax.swing.JProgressBar;

import org.junit.BeforeClass;
import org.junit.Ignore;
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
	
	@Test @Ignore
	public void testProgress(){
        //TODO: CLEAN UP
		PlanOfStudy plan = new PlanOfStudy();
		Degree csys = Fixtures.getCSYS();
		DegreeProgressPanel panel = null; //new DegreeProgressPanel(plan, csys);
		
		Panel panelWrapper = new Panel(panel);
		UISpecAssert.assertThat(panelWrapper.containsSwingComponent(JProgressBar.class));
		
		UISpecAssert.assertThat(panelWrapper.getProgressBar().completionEquals(50));
	}
}
