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
import rpiplanner.validation.degree.Degree;

import rpiplanner.validation.degree.*;
import rpiplanner.validation.degree.Degree;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;

public interface CourseDatabase {
	public Course[] search(String text);

	public Course[] listCourses();

	public Degree[] listDegrees();

    @Deprecated
	public Course getCourse(String name);

	public List<String> getDepartments();

	public void addPropertyChangeListener(PropertyChangeListener listener);

	public void addOrUpdate(Course updated);

	public Degree getDegree(long id);

    ArrayList<Course> getCourseBetween(String prefix, int lowerNumber, int upperNumber);
}
