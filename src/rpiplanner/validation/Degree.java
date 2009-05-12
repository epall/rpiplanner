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

import java.util.*;

public class Degree {
    @XStreamAlias("DegreeName")
    String name;

    ArrayList<SpecialDesignationRequirement> specialReq = new ArrayList<SpecialDesignationRequirement>();
    ArrayList<CoreRequirement> coreReq = new ArrayList<CoreRequirement>();
    ArrayList<SubjectRequirement> subjReq = new ArrayList<SubjectRequirement>();
    ArrayList<RestrictedRequirement> restReq = new ArrayList<RestrictedRequirement>();


    //TODO: Supposed to take PlanOfStudy pos as argument
    //Have them return validtionresult and then run another function to update Hash
    public DegreeValidationResult validate(ArrayList<Course> pos) {
        ArrayList<Course> courseList = pos;
        DegreeValidationResult result = new DegreeValidationResult();
        HashMap<Course, Integer> courseMap = createHash(courseList);
        int totalCredits = 0;

        for (SpecialDesignationRequirement currentReq : specialReq) {
            DegreeSection newSection = new DegreeSection();
            newSection.name = currentReq.getName();
            newSection.description = currentReq.getDescription();

            result.addSection(newSection);
        }

        for (CoreRequirement currentReq : coreReq) {
            DegreeSection newSection = currentReq.validate(courseMap);
            result.addSection(newSection);
        }

        for (RestrictedRequirement currentReq : restReq) {
            DegreeSection newSection = currentReq.validate(courseMap);
            result.addSection(newSection);
        }

        for (SubjectRequirement currentReq : subjReq) {
            DegreeSection newSection = currentReq.validate(courseMap, courseList);
            result.addSection(newSection);
        }

        //Humanities
        DegreeSection humanitiesSocialSciencesResult = humanitiesValidate(courseMap,courseList);
        result.addSection(humanitiesSocialSciencesResult);

        //Free Electives
        DegreeSection freeElective = new DegreeSection();
        freeElective.name = "Free Electives";
        freeElective.description = "Courses that are not applied to any other requirements";
        for (Course course : courseList) {
            if (courseMap.get(course) > 0) {
                freeElective.appliedCourses.add(course);
                freeElective.credits += course.getCredits();
                int number = courseMap.get(course);
                number--;
                courseMap.put(course, number);
                if (courseMap.get(course) > 0 && course.isDoubleCount()) {
                    for (int i = 0; i < courseMap.get(course); i++) {
                        freeElective.appliedCourses.add(course);
                        freeElective.credits += course.getCredits();
                        totalCredits += course.getCredits();
                    }
                }
            }
        }
        result.addSection(freeElective);
        result.setTotalCredits(totalCredits);

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

    private DegreeSection humanitiesValidate(HashMap <Course,Integer> courseMap,ArrayList<Course> courseList)
    {
        DegreeSection humSSSection = new DegreeSection();
        humSSSection.name = "Humanities and Social Sciences";
        humSSSection.description = "Humanities and Social Sciences Stuff";

        Boolean pdReq = false;
        Boolean depthReq = false;

        int numHum = 0;
        int numIHSS = 0;
        int numSS = 0;

        ArrayList<Course> humCourses = new ArrayList<Course>();


        //Professional Development II
        if (courseMap.containsKey(Course.get("PSYC", "4170")) || courseMap.containsKey(Course.get("STSS", "4840"))) {
            if (courseMap.containsKey(Course.get("PSYC", "4170"))) {
                courseMap.put(Course.get("PSYC", "4170"), 0);
                humSSSection.appliedCourses.add(Course.get("PSYC", "4170"));
                pdReq = true;
                courseList.remove(Course.get("PSYC", "4170"));
            }
            if (courseMap.containsKey(Course.get("STSS", "4840"))) {
                courseMap.put(Course.get("STSS", "4840"), 0);
                humSSSection.appliedCourses.add(Course.get("STSS", "4840"));
                pdReq = true;
                courseList.remove(Course.get("STSS", "4840"));
            }
        } else {
            humSSSection.missingCourses.add(Course.get("PSYC", "4170"));
            humSSSection.missingCourses.add(Course.get("STSS", "4840"));
            humSSSection.potentialCourses.add(Course.get("PSYC", "4170"));
            humSSSection.potentialCourses.add(Course.get("STSS", "4840"));
        }
        
        //TODO: Check Depth Requirement
        //TODO: Check 4000 level requirement
        //TODO: Check Humanities
        //TODO: Check Social Sciences


        return humSSSection;
    }

    public void addCoreRequirement(CoreRequirement req) {
        coreReq.add(req);
    }

    public void addRestrictedRequirement(RestrictedRequirement req) {
        restReq.add(req);
    }
}
