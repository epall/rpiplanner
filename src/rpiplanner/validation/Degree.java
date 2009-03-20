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
import java.util.Iterator;
import java.util.Set;

public class Degree {
    @XStreamAlias("DegreeName")
    String name;

    ArrayList<SpecialDesignationRequirement> specialReq = new ArrayList<SpecialDesignationRequirement>();
    ArrayList<CoreRequirement> coreReq = new ArrayList<CoreRequirement>();
    ArrayList<SubjectRequirement> subjReq = new ArrayList<SubjectRequirement>();
    ArrayList<RestrictedRequirement> restReq = new ArrayList<RestrictedRequirement>();


    //TODO: Supposed to take PlanOfStudy pos as argument
    //TODO: Put validate functions in respective requirements?
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
        /*
        Humanities and Social Sciences RPI Version
        Humanities: LANG LITR COMM WRIT ARTS PHIL STSH IHSS
        Social Sciences: ECON STSS PSYC
        STSS and STSH can count as one area for depth
        PD2 is a humanities requirement so 20 credits required
        Must take one class above 1000 level in the same prefix as you took a 1000 level
        Max 3 1000 level courses
        */
        String description = "Humanities and Social Sciences Stuff";
        DegreeSection humSSSection = new DegreeSection();
        humSSSection.name = "Humanities and Social Sciences";
        humSSSection.description = description;
        int numHum = 0;
        int numSocSci = 0;
        int num1000Hum = 0;
        int num1000SocSci = 0;
        int numUpperHum = 0;
        int numUpperSocSci = 0;
        Boolean depthSatisfied;
        ArrayList<Course> hum1000List = new ArrayList<Course>();
        ArrayList<Course> ss1000List = new ArrayList<Course>();
        ArrayList<Course> humUpperList = new ArrayList<Course>();
        ArrayList<Course> ssUpperList = new ArrayList<Course>();

        for (Course course : courseList) {
            //Humanities
            if (course.getPrefix() == "LANG" || course.getPrefix() == "LITR" || course.getPrefix() == "WRIT" || course.getPrefix() == "COMM"
                    || course.getPrefix() == "ARTS" || course.getPrefix() == "PHIL" || course.getPrefix() == "STSH" || course.getPrefix() == "IHSS") {
                numHum++;
                if (course.getLevel() == "1000") {
                    num1000Hum++;
                    hum1000List.add(course);
                }
                if (course.getLevel() == "2000" || course.getLevel() == "4000" || course.getLevel() == "6000") {
                    numUpperHum++;
                    humUpperList.add(course);
                }
            }
            //Social Sciences
            else if (course.getPrefix() == "ECON" || course.getPrefix() == "STSS" || course.getPrefix() == "PSYC") {
                numSocSci++;
                if (course.getLevel() == "1000") {
                    num1000SocSci++;
                    ss1000List.add(course);
                }
                if (course.getLevel() == "2000" || course.getLevel() == "4000" || course.getLevel() == "6000") {
                    numUpperSocSci++;
                    ssUpperList.add(course);
                }
            }
        }
        //Minimum 2 4 credit Courses in Humanities

        //minimum of 2 4-credit courses in the Social Sciences

        //one 4 credit course at the 4000 level

        //depth requirement
        
        //Professional Development II
        if (courseMap.containsKey(Course.get("PSYC", "4170")) || courseMap.containsKey(Course.get("STSS", "4840"))) {
            if (courseMap.containsKey(Course.get("PSYC", "4170"))) {
                courseMap.put(Course.get("PSYC", "4170"), 0);
                humSSSection.appliedCourses.add(Course.get("PSYC", "4170"));
            }
            if (courseMap.containsKey(Course.get("STSS", "4840"))) {
                courseMap.put(Course.get("STSS", "4840"), 0);
                humSSSection.appliedCourses.add(Course.get("STSS", "4840"));
            }
        } else {
            humSSSection.missingCourses.add(Course.get("PSYC", "4170"));
            humSSSection.missingCourses.add(Course.get("STSS", "4840"));
            humSSSection.potentialCourses.add(Course.get("PSYC", "4170"));
            humSSSection.potentialCourses.add(Course.get("STSS", "4840"));
        }

        humSSSection.isSuccess = false;
        result.addSection(humSSSection);

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

    public void addCoreRequirement(CoreRequirement req) {
        coreReq.add(req);
    }

    public void addRestrictedRequirement(RestrictedRequirement req) {
        restReq.add(req);
    }
}
