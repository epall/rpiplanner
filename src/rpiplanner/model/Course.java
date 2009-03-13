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

import java.util.Stack;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("course")
public class Course implements Comparable<Course> {
    protected String title;
    protected String description;
    protected String department;
    protected String catalogNumber;
    protected int credits;
    protected RequisiteSet prerequisites;
    protected RequisiteSet corequisites;
    protected YearPart[] availableTerms;
    protected int DFSpost;
    
    
    public int getDFSpost() {
    	return DFSpost;
    }
    
    private void setDFSpost(int num) {
    	DFSpost = num;
    }
    
    public void DFS() {
		Stack<Course> dfs = new Stack<Course>();
		dfs.push(this);
		int DFSnum = 0;
		Course tmp = this;
		while (!dfs.isEmpty()) {
			for (int i = 0; i < tmp.getPrerequisites().size(); i++) {
				dfs.push(tmp.getPrerequisites().get(i));
			}
			
			tmp = dfs.pop();
			DFSnum++;
			tmp.setDFSpost(DFSnum);
		}
	}
    
	public String getTitle() {
		return title;
	}
	void setTitle(String title){
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public String getDepartment() {
		return department;
	}
	public String getCatalogNumber() {
		return catalogNumber;
	}
	void setCatalogNumber(String catalogNumber){
		this.catalogNumber = catalogNumber;
	}
	public int getCredits() {
		return credits;
	}
	public RequisiteSet getPrerequisites() {
		if(prerequisites == null)
			prerequisites = new RequisiteSet();
		return prerequisites;
	}
	public RequisiteSet getCorequisites() {
		if(corequisites == null)
			corequisites = new RequisiteSet();
		return corequisites;
	}
	public YearPart[] getAvailableTerms() {
		return availableTerms;
	}
	public String toString(){
		if(title == null)
			return catalogNumber;
		return catalogNumber + " - " + title;
	}
	public int compareTo(Course o) {
		return toString().compareTo(o.toString());
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Course)
			return catalogNumber.equals(((Course)obj).catalogNumber);
		return false;
	}
	@Override
	public int hashCode() {
        return catalogNumber.hashCode();
	}
}
