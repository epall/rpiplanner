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

package rpiplanner.validation.degree;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rpiplanner.model.Course;
import rpiplanner.validation.interfaces.Section;
import rpiplanner.validation.interfaces.Requirement;
import rpiplanner.validation.requirements.CoreRequirement;
import rpiplanner.validation.requirements.RestrictedRequirement;

import java.util.ArrayList;
import java.util.HashMap;

@XStreamAlias("Degree")
public class Degree {
    @XStreamAlias("DegreeName")
    String name;
    int numCredits;


    ArrayList<Requirement> requirements = new ArrayList<Requirement>();

    public Degree(String name, int numCredits) {
        this.name = name;
        this.numCredits = numCredits;
    }

    //TODO: Supposed to take PlanOfStudy pos as argument
    //Have them return validtionresult and then run another function to update Hash
    public DegreeValidationResult validate(ArrayList<Course> pos) {
        ArrayList<Course> courseList = pos;
        DegreeValidationResult result = new DegreeValidationResult();
        HashMap<Course, Integer> courseMap = createHash(courseList);
        result.setTotalCredits(numCredits);
        int totalCredits = 0;

        for (Requirement requirement : requirements){
            Section section = requirement.validate(courseMap, courseList);
            result.addSection(section);
        }

        return result;
    }


    private HashMap<Course, Integer> createHash(ArrayList<Course> pos) {
        HashMap<Course, Integer> courseMap = new HashMap<Course, Integer>();
        //pos.getCourses() for PoS
        for (Course course : pos) {
            if (courseMap.containsKey(course)) {
                int num = courseMap.get(course);
                num++;
                courseMap.put(course, num);
            } else {
                courseMap.put(course, 1);
            }
        }

        return courseMap;
    }

    @Deprecated
    public void addCoreRequirement(CoreRequirement req) {
        //coreReq.add(req);
    }
    @Deprecated
    public void addRestrictedRequirement(RestrictedRequirement req) {
        //restReq.add(req);
    }

    public void addRequirement(Requirement requirement){
        requirements.add(requirement);
    }
}
