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

public class Degree
{
    @XStreamAlias("DegreeName")
    String name;

    ArrayList<SpecialDesignationRequirement> specialReq = new ArrayList<SpecialDesignationRequirement>();
    ArrayList<CoreRequirement> coreReq = new ArrayList<CoreRequirement>();
    ArrayList<SubjectRequirement> subjReq = new ArrayList<SubjectRequirement>();
    ArrayList<RestrictedRequirement> restReq = new ArrayList<RestrictedRequirement>();


    //TODO: Supposed to take PlanOfStudy pos as argument
    public DegreeValidationResult validate (ArrayList<Course> pos)
    {
        ArrayList<Course> courseList = pos;
        DegreeValidationResult result = new DegreeValidationResult();
        HashMap<Course,Integer> courseMap = createHash(courseList);
        int totalCredits = 0;

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


                //TODO:Create fuction for this block
                for (Course course : currentReq.getCourses())
                {
                    Boolean found = false;
                    //Check to see if we have taken this course;
                    if (courseMap.containsKey(course))
                    {
                        if (courseMap.get(course) > 0)
                        {
                            newSection.appliedCourses.add(course);
                            newSection.credits += course.getCredits();
                            int number = courseMap.get(course);
                            number--;
                            courseMap.put(course,number);
                            found = true;
                            totalCredits += course.getCredits();
                        }
                    }
                    if (currentReq.hasReplacementCourse(course) && !found)
                    {
                        //Check for replacement courses for the current course.
                        for (Course repCourse : currentReq.getReplacementCourses(course))
                        {
                            if (courseMap.containsKey(repCourse))
                            {   //TODO: Do we want the applied course to show original course or rep course?
                                newSection.appliedCourses.add(repCourse);
                                newSection.credits += course.getCredits();
                                int number = courseMap.get(repCourse);
                                number--;
                                courseMap.put(repCourse,number);
                                found = true;
                                totalCredits += course.getCredits();
                            }
                        }
                    }
                    //We didn't find the course so it must be missing.
                   if (!found)
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
                }
            if (newSection.missingCourses.size() == 0) newSection.isSuccess = true;
            else newSection.isSuccess = false;
            result.addSection(newSection);
        }

        for (RestrictedRequirement currentReq : restReq)
			{
                DegreeSection newSection = new DegreeSection();
                newSection.name = currentReq.getName();
                newSection.description = currentReq.getDescription();
                for (Course course : currentReq.getCourses())
                {
                    if (courseMap.containsKey(course))
                    {
                        newSection.appliedCourses.add(course);
                        newSection.credits += course.getCredits();
                        int number = courseMap.get(course);
                        number--;
                        courseMap.put(course,number);
                        totalCredits += course.getCredits();
                    }
                    else if (currentReq.hasReplacementCourse(course))
                    {
                        //Check for replacement courses for the current course.
                        for (Course repCourse : currentReq.getReplacementCourses(course))
                            {
                                if (courseMap.containsKey(repCourse))
                                {   //TODO: Do we want the applied course to show original course or rep course?
                                    newSection.appliedCourses.add(repCourse);
                                    newSection.credits += course.getCredits();
                                    int number = courseMap.get(repCourse);
                                    number--;
                                    courseMap.put(repCourse,number);
                                    totalCredits += course.getCredits();
                                }
                            }
                    }
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
                }
                //TODO: Add support for if more than one course that meets the requirement is taken.
                if (newSection.appliedCourses.size() == currentReq.getNumCourses() && newSection.credits >= currentReq.getNumCredits())
                {
                    newSection.isSuccess = true;
                    newSection.missingCourses.clear();
                    newSection.potentialCourses.clear();
                }
                else newSection.isSuccess = false;
                result.addSection(newSection);
			}

        for (SubjectRequirement currentReq : subjReq)
			{
                DegreeSection newSection = new DegreeSection();
                newSection.name = currentReq.getName();
                newSection.description = currentReq.getDescription();

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
        String description = "To ensure that students have some depth in their H&SS core, students must take at least two courses within a single area prefix (STSH and STSS can be counted as a single area), at least one of which is taken at an advanced level (above 1000). No course within the depth sequence may be taken as Pass/No Credit.\n" +
                "\n" +
                "No more than three 1000-level H&SS courses may be applied toward the H&SS core requirement, no more than 6 credits may be taken as Pass/No credit and at least one course (4 credits) must be at the 4000 level.";
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

        for (Course course : courseList)
        {
            //Humanities
            if (course.getPrefix() == "LANG" || course.getPrefix() == "LITR" || course.getPrefix() == "WRIT" || course.getPrefix() == "COMM"
                    || course.getPrefix() == "ARTS" || course.getPrefix() == "PHIL" || course.getPrefix() == "STSH" || course.getPrefix() == "IHSS")
            {
                numHum++;
                if (course.getLevel() == "1000")
                {
                    num1000Hum++;
                    hum1000List.add(course);
                }
                if (course.getLevel() == "2000" || course.getLevel() == "4000" || course.getLevel() == "6000")
                {
                    numUpperHum++;
                    humUpperList.add(course);
                }
            }
            //Social Sciences
            else if (course.getPrefix() == "ECON" || course.getPrefix() == "STSS" || course.getPrefix() == "PSYC")
            {
                numSocSci++;
                if (course.getLevel() == "1000")
                {
                    num1000SocSci++;
                    ss1000List.add(course);
                }
                if (course.getLevel() == "2000" || course.getLevel() == "4000" || course.getLevel() == "6000")
                {
                    numUpperSocSci++;
                    ssUpperList.add(course);
                }
            }
        }
        humSSSection.isSuccess = false;
        result.addSection(humSSSection);

        //Free Electives
        DegreeSection freeElective = new DegreeSection();
        freeElective.name = "Free Electives";
        freeElective.description = "Courses that are not applied to any other requirements";
        for (Course course : courseList)
        {
            if (courseMap.get(course) > 0)
            {
                freeElective.appliedCourses.add(course);
                freeElective.credits += course.getCredits();
                int number = courseMap.get(course);
                number--;
                courseMap.put(course,number);
                if (courseMap.get(course) > 0 && course.isDoubleCount())
                {
                    for (int i = 0;i < courseMap.get(course);i++)
                    {
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
