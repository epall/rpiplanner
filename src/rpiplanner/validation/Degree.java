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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.model.Course;

import java.util.ArrayList;
import java.util.HashMap;

public class Degree
{
    @XStreamAlias("DegreeName")
    String name;

    ArrayList<SpecialDesignationRequirement>  specialReq;
    ArrayList<CoreRequirement> coreReq;
    ArrayList<SubjectRequirement> subjReq;
    ArrayList<RestrictedRequirement> restReq;




    //TODO: Supposed to take PlanOfStudy pos as argument
    public DegreeValidationResult validate (ArrayList<Course> pos)
    {
        DegreeValidationResult result = new DegreeValidationResult();
         HashMap<Course,Integer> courseMap = createHash(pos);

        for (SpecialDesignationRequirement currentReq : specialReq)
        {
            DegreeSection newSection = new DegreeSection();
            newSection.name = currentReq.getName();
            newSection.description = currentReq.getDescription();

            result.addSection(newSection);
        }

        for (CoreRequirement currentReq : coreReq)
        {
                DegreeSection newSection = new DegreeSection();
                newSection.name = currentReq.getName();
                newSection.description = currentReq.getDescription();

                //TODO:Decrement HashMap when Course used
                for (Course course : currentReq.getCourses())
                {
                    //Check to see if we have taken this course;
                    if (courseMap.containsKey(course))
                    {
                        newSection.appliedCourses.add(course);
                    }
                    else if (currentReq.hasReplacementCourse(course))
                    {
                        //Check for replacement courses for the current course.
                        for (Course repCourse : currentReq.getReplacementCourses(course))
                        {
                            if (courseMap.containsKey(repCourse))
                            {
                                newSection.appliedCourses.add(repCourse);
                            }
                        }
                    }
                    //We didn't find the course so it must be missing.
                    else
                    {
                        //TODO:Check to see if we want the replacement course to be in missing courses also.
                        newSection.missingCourses.add(course);
                        newSection.potentialCourses.add(course);
                        if (currentReq.hasReplacementCourse(course))
                        {
                            for (Course repCourse : currentReq.getReplacementCourses(course))
                            {
                                if (courseMap.containsKey(repCourse))
                                {
                                    newSection.potentialCourses.add(repCourse);
                                }
                            }
                        }

                    }
                result.addSection(newSection);
			}
        }

        for (RestrictedRequirement currentReq : restReq)
			{
                DegreeSection newSection = new DegreeSection();
                newSection.name = currentReq.getName();
                newSection.description = currentReq.getDescription();

                result.addSection(newSection);
			}

        for (SubjectRequirement currentReq : subjReq)
			{
                DegreeSection newSection = new DegreeSection();
                newSection.name = currentReq.getName();
                newSection.description = currentReq.getDescription();

                result.addSection(newSection);
			}
        return result;
    }


    //TODO: Add PlanOfStudy pos as argument
    private HashMap <Course,Integer> createHash (ArrayList<Course> pos)
    {
        HashMap <Course,Integer> courseMap = new HashMap <Course,Integer>();
        //pos.getCourses() for PoS
        for (Course course : pos)
        {
            if (courseMap.containsKey(course))
            {
                int num = courseMap.get(course);
                num++;
                courseMap.put(course,num);
            }
            else
            {
                courseMap.put(course,1);
            }
        }

        return courseMap;
    }

    public void addCoreRequirement(CoreRequirement req) {
        coreReq.add(req);
    }

    public void addRestrictedRequirement(RestrictedRequirement req) {
        restReq.add(req);
    }
}
