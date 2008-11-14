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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Eric Allen
 */

@XStreamAlias("course")
public class Course implements Comparable<Course> {
    protected String title;
    protected String description;
    protected String department;
    protected String catalogNumber;
    protected int credits;
    protected Course[] prerequisites;
    protected Course[] corequisites;
    protected YearPart[] availableTerms;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCatalogNumber() {
		return catalogNumber;
	}
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public Course[] getPrerequisites() {
		return prerequisites;
	}
	public void setPrerequisites(Course[] prerequisites) {
		this.prerequisites = prerequisites;
	}
	public Course[] getCorequisites() {
		return corequisites;
	}
	public void setCorequisites(Course[] corequisites) {
		this.corequisites = corequisites;
	}
	public YearPart[] getAvailableTerms() {
		return availableTerms;
	}
	public void setAvailableTerms(YearPart[] availableTerms) {
		this.availableTerms = availableTerms;
	}
	public String toString(){
		return catalogNumber + " - " + title;
	}
	public int compareTo(Course o) {
		return toString().compareTo(o.toString());
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Course)
			return catalogNumber == ((Course)obj).catalogNumber;
		return false;
	}
}
