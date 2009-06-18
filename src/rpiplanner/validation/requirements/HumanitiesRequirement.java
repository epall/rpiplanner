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

package rpiplanner.validation.requirements;

import rpiplanner.model.Course;
import rpiplanner.validation.degree.DegreeSection;
import rpiplanner.validation.interfaces.Requirement;

import java.util.ArrayList;
import java.util.HashMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HumanitiesRequirement")
public class HumanitiesRequirement extends Requirement {
    public String getName() {
        return "Humanities and Social Sciences";
    }

    public DegreeSection validate(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList) {
        DegreeSection humSSSection = new DegreeSection();
        humSSSection.setName("Humanities and Social Sciences");
        humSSSection.setDescription("Humanities and Social Sciences Core Requirement");

        ArrayList<Course> humSSCourses = createHumanitiesOrSSList(courseList, courseMap);


        boolean pd2ReqMet = checkPD2(courseMap, courseList, humSSSection);
        boolean depthReqMet = depthRequirement(courseMap, humSSCourses);
        boolean levelReqMet = LevelReq(humSSSection, humSSCourses, courseMap);
        boolean mainReq = mainReq(humSSSection,courseMap,humSSCourses);

        if (pd2ReqMet && depthReqMet && levelReqMet &&  mainReq) {
            humSSSection.succeeded();
            humSSSection.clearMessages();
        }

        return humSSSection;
    }

    private boolean mainReq(DegreeSection section, HashMap<Course, Integer> courseMap,
        ArrayList<Course> humSSCourses) {
        //TODO: Add courses to potential
        int totalSocCredits = 0;
        int totalHumCredits = 0;
        int total1000Level = 0;
        for (Course course : section.appliedCourses()) {
            if (isHumanities(course)) {
                if (course.getLevel() == "1000") {
                    total1000Level++;
                }
                totalHumCredits += course.getCredits();
            }
            if (isSS(course)) {
                if (course.getLevel() == "1000") {
                    total1000Level++;
                }
                totalSocCredits += course.getCredits();
            }
        }

        for (Course course : humSSCourses) {
            if (!isApplied(section,course)) {
                if (isHumanities(course)) {
                    if (course.getLevel() == "1000") total1000Level++;
                        if ((total1000Level <= 3 || course.getLevel() != "1000") && courseMap.get(course) > 0) {
                            courseMap.put(course,courseMap.get(course) - 1);
                            totalSocCredits += course.getCredits();
                            section.addAppliedCourse(course);
                        }
                }
                if (isSS(course)) {
                    if (course.getLevel() == "1000") total1000Level++;
                        if ((total1000Level <= 3 || course.getLevel() != "1000") && courseMap.get(course) > 0) {
                            courseMap.put(course,courseMap.get(course) - 1);
                            totalHumCredits += course.getCredits();
                            section.addAppliedCourse(course);
                        }

               }
            }
            if (totalHumCredits + totalSocCredits >= 20) return true;
        }
        if (totalSocCredits < 8) section.addMessage("Need More Social Sciences Courses");
        if (totalHumCredits < 8) section.addMessage("Need More Humanities Courses");
        if (totalSocCredits + totalHumCredits <= 20) section.addMessage("Minimum of 20 credits."); 
        return false;
    }

    private boolean isApplied(DegreeSection section, Course _course) {
        for (Course course : section.appliedCourses()) {
            if (course == _course) return true;
        }
        return false;
    }


    private boolean depthRequirement(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList) {
        ArrayList<String> lowPrefixList = new ArrayList<String>();
        ArrayList<String> upperPrefixList = new ArrayList<String>();
        for (Course course : courseList) {
            if (courseMap.get(course) > 0) {
                if (course.getNumber() < 2000) {
                    if (course.getPrefix() == "STSH" || course.getPrefix() == "STSS") {
                        lowPrefixList.add("STSH");
                        lowPrefixList.add("STSS");
                    }
                    lowPrefixList.add(course.getPrefix());
                }
                else
                {
                    if (course.getPrefix() == "STSH" || course.getPrefix() == "STSS") {
                        upperPrefixList.add("STSH");
                        upperPrefixList.add("STSS");
                    }
                    upperPrefixList.add(course.getPrefix());
                }
            }
        }
        for (String prefix : lowPrefixList) {
            if (upperPrefixList.contains(prefix)) return true;
        }
        return false;
    }

    private boolean LevelReq(DegreeSection humSSSection, ArrayList<Course> humCourses, HashMap<Course, Integer> courseMap) {
        if (!met4000LevelReq(humSSSection,humCourses, courseMap)) {
            add4000LevelCourses(humSSSection, humCourses);
            humSSSection.addMessage("You need a 4000 Level Course");
            return false;
        }
        return true;
    }

    private void add4000LevelCourses(DegreeSection humSSSection, ArrayList<Course> humCourses) {
        ArrayList<String> prefixes = new ArrayList<String>();
        fillPrefixes(prefixes);
        ArrayList<Course> courses = new ArrayList<Course>();
        for (String prefix : prefixes) {
            courses.addAll(Course.getAllBetween(prefix, 4000, 5000));
        }
        humSSSection.addPotentialCourse(courses);
    }

    private void fillPrefixes(ArrayList<String> prefixes) {
        prefixes.add("LANG");
        prefixes.add("LITR");
        prefixes.add("COMM");
        prefixes.add("WRIT");
        prefixes.add("ARTS");
        prefixes.add("PHIL");
        prefixes.add("STSH");
        prefixes.add("IHSS");
        prefixes.add("ECON");
        prefixes.add("STSS");
        prefixes.add("PSYC");
    }

    private Boolean met4000LevelReq(DegreeSection humSSSection, ArrayList<Course> humCourses, HashMap<Course, Integer> courseMap) {
        for (Course course : humCourses) {
            if (course.getNumber() > 4000 && course.getNumber() < 5000 && courseMap.get(course) > 0 &&
                    course != Course.get("STSS", "4840") && course != Course.get("PSYC", "4170")) {
                humSSSection.addAppliedCourse(course);
                courseMap.put(course, courseMap.get(course) - 1);
                return true;
            }
        }
        return false;
    }

    private ArrayList<Course> createHumanitiesOrSSList(ArrayList<Course> courseList, HashMap<Course, Integer> courseMap) {
        ArrayList<Course> newList = new ArrayList<Course>();
        for (Course course : courseList) {
            if (courseMap.get(course) > 0) {
                if (isHumanities(course) || isSS(course)) {
                    newList.add(course);
                }
            }
        }
        return newList;
    }

    private boolean isHumanities(Course course) {
        if (course.getPrefix().equals("LANG") || course.getPrefix().equals("LITR") || course.getPrefix().equals("COMM")
                || course.getPrefix().equals("WRIT") || course.getPrefix().equals("ARTS") || course.getPrefix().equals("PHIL")
                || course.getPrefix().equals("STSH") || course.getPrefix().equals("IHSS")) {
            return true;
        }
        return false;
    }

    private boolean isSS(Course course) {
        if (course.getPrefix().equals("ECON") || course.getPrefix().equals("STSS")
                || course.getPrefix().equals("PSYC")) return true;
        return false;
    }


    private boolean checkPD2(HashMap<Course, Integer> courseMap, ArrayList<Course> courseList, DegreeSection humSSSection) {
        Boolean pdReq;//Professional Development II
        if (courseMap.containsKey(Course.get("PSYC", "4170")) || courseMap.containsKey(Course.get("STSS", "4840"))) {
            if (courseMap.containsKey(Course.get("PSYC", "4170"))) {
                courseMap.put(Course.get("PSYC", "4170"), 0);
                humSSSection.addAppliedCourse(Course.get("PSYC", "4170"));
                pdReq = true;
                courseList.remove(Course.get("PSYC", "4170"));
            }
            if (courseMap.containsKey(Course.get("STSS", "4840"))) {
                courseMap.put(Course.get("STSS", "4840"), 0);
                humSSSection.addAppliedCourse(Course.get("STSS", "4840"));
                pdReq = true;
                courseList.remove(Course.get("STSS", "4840"));
            }
            return true;
        } else {
            humSSSection.addMissingCourse(Course.get("PSYC", "4170"));
            humSSSection.addMissingCourse(Course.get("STSS", "4840"));
            humSSSection.addPotentialCourse(Course.get("PSYC", "4170"));
            humSSSection.addPotentialCourse(Course.get("STSS", "4840"));
            return false;
        }
    }
}
