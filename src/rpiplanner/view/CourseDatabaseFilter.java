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

package rpiplanner.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;

public class CourseDatabaseFilter extends CourseListModel implements PropertyChangeListener {
	private CourseDatabase database;
	
	public CourseDatabaseFilter(CourseDatabase database){
		this.database = database;
		this.visibleCourses = database.listCourses();
	}

	public void setSearchText(String text) {
		if(text == null || text == "")
			visibleCourses = database.listCourses();
		else
			visibleCourses = database.search(text);
		fireContentsChanged(this, 0, visibleCourses.length-1);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName() == "courses"){
			setSearchText("");
		}
	}

	public void setShownCourses(Course[] potentialCourses) {
		visibleCourses = potentialCourses;
		fireContentsChanged(this, 0, visibleCourses.length-1);
	}
}
