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

import rpiplanner.model.Course;
import rpiplanner.validation.DegreeValidationResult;

/**
 * A {@link DegreeValidator}'s <code>validate()</code> method will produce a
 * ValidationResult that can be queried for detailed results. Each
 * ValidationResult contains details for each section such as which courses
 * were missing, which courses were applied, which courses could possibly
 * be applied, and any other messages the validation caused.
 */
public interface ValidationResult {
    /**
     * An implementation-specific percentage of completion for progress
     * indication in the GUI. Currently implemented in Ruby as # of successful
     * sections / total # of sections * 100. Not perfect, but reasonable.
     * @return A number between 0 and 100 representing the level of completion.
     */
	public int percentComplete();

    /**
     * Get the results for a specific section.
     * @param name A name of a section in the <code>getSections()</code> array of
     * {@link rpiplanner.validation.DegreeValidator}.
     * @return The results for the requested section, or <code>null</code>.
     * if it does not exist.
     */
    public Section getSectionResults(String name);

     /**
     * Each degree has a set of sections, such as "Math & Science"
     * "Communication Requirement", "Free Electives", and so on. Each section
     * is validated separately, and the GUI needs to display the set of
     * sections for the user to see.
     * @return A list of section names
     */
	public String[] getSectionNames();
	
	public interface Section {
		public Course[] missingCourses();
		public Course[] appliedCourses();
		public String[] messages();
		public Course[] potentialCourses();
		public boolean isSuccess();
	}
}
