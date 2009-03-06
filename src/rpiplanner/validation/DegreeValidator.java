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

package rpiplanner.validation;

import rpiplanner.model.PlanOfStudy;

/**
 * Validates a PlanOfStudy to a Degree when asked.
 * @see rpiplanner.model.Degree
 * @see rpiplanner.model.PlanOfStudy
 */
public interface DegreeValidator {
    /**
     * Each degree has a set of sections, such as "Math & Science"
     * "Communication Requirement", "Free Electives", and so on. Each section
     * is validated separately, and the GUI needs to display the set of
     * sections for the user to see.
     * @return A list of section names
     */
	public String[] getSectionNames();

    /**
     * Every Degree in RPI Planner has a unique internal ID that ties it
     * to its degree validator. This is a convenience method to get that
     * internal ID.
     * @return an id that corresponds to a Degree in the database
     */
	public long getID();

    /**
     * Validate the given PlanOfStudy according to the degree this
     * DegreeValidator represents. Returns a complex ValidationResult
     * object for inspection and display. Can be called as often as
     * needed.
     * @param plan a PlanOfStudy to validate
     * @return The results of the validation
     */
	public ValidationResult validate(PlanOfStudy plan);
}
