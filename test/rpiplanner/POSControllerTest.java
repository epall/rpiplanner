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

package rpiplanner;

import static org.junit.Assert.*;
import org.junit.Test;
import rpiplanner.model.PlanOfStudy;

public class POSControllerTest {
    /**
     * Ensure that when loading a template the starting year
     * is persisted from the user's plan.
     */
    @Test
    public void testLoadTemplateYearPersist(){
        PlanOfStudy myPlan = new PlanOfStudy();
        myPlan.setStartingYear(2000);
        myPlan.rebuildTerms();
        POSController controller = new POSController();
        controller.setPlan(myPlan);
        myPlan.getDegrees().add(Fixtures.getCSYS());
        assertEquals(2000, myPlan.getStartingYear());
        controller.loadTemplate();
        assertEquals(2000, myPlan.getStartingYear());
    }

    /**
     * Ensure that templates get loaded. Generally from files, not from
     * JAR, for tests.
     */
    @Test
    public void testLoadTemplate(){
        PlanOfStudy myPlan = new PlanOfStudy();
        myPlan.setStartingYear(2000);
        myPlan.rebuildTerms();
        POSController controller = new POSController();
        controller.setPlan(myPlan);
        myPlan.getDegrees().add(Fixtures.getCSYS());
        controller.loadTemplate();

        assertNotSame("Expected non-zero number of courses in template.", 0, myPlan.getTerm(1).getCourses().size());
    }

    /**
     * Ensure that when loading a template the years for each
     * term are persisted from the user's plan.
     */
    @Test
    public void testLoadTemplateTermYearPersist(){
        PlanOfStudy myPlan = new PlanOfStudy();
        myPlan.setStartingYear(2000);
        myPlan.rebuildTerms();
        assertEquals(2000, myPlan.getTerm(1).getYear());
        POSController controller = new POSController();
        controller.setPlan(myPlan);
        myPlan.getDegrees().add(Fixtures.getCSYS());
        controller.loadTemplate();
        assertEquals(2000, myPlan.getTerm(1).getYear());
    }
}
