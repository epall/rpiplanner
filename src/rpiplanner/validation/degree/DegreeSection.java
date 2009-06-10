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

import rpiplanner.model.Course;
import rpiplanner.validation.interfaces.IDegreeValidationResult;
import rpiplanner.validation.ValidationResult;

import java.util.ArrayList;

public class DegreeSection implements ValidationResult.Section
{
    String name;
    String description;
    int credits;
    boolean isSuccess = false;

    ArrayList<Course> missingCourses = new ArrayList<Course>();
    ArrayList<Course> appliedCourses = new ArrayList<Course>();
    ArrayList<Course> potentialCourses = new ArrayList<Course>();

    ArrayList <String> messages = new ArrayList<String>();

    public String getName(){
        return name;
    }

    public void addMissingCourse(Course course) {
        missingCourses.add(course);
    }
    public void addMissingCourse(ArrayList<Course> course) {
        missingCourses.addAll(course);
    }
    public void addAppliedCourse(Course course){
        appliedCourses.add(course);
    }
    public void addPotentialCourse(Course course) {
        potentialCourses.add(course);
    }
    public void addPotentialCourse(ArrayList<Course> course) {
        potentialCourses.addAll(course);
    }
    public void addCredits(int num) {
        credits += num;
    }

    public int getNumCredits() {
        return credits;
    }

    public boolean isSuccess()
    {
       return isSuccess;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void succeeded() {
        isSuccess = true;
        missingCourses.clear();
        potentialCourses.clear();
    }

    public Course[] missingCourses()
    {
        return missingCourses.toArray(new Course[0]);
    }
    public Course[] appliedCourses()
    {
        return appliedCourses.toArray(new Course[0]);
    }
    public Course[] potentialCourses()
    {
        return potentialCourses.toArray(new Course[0]);
    }
    public String[] messages()
    {
        return messages.toArray(new String[0]);
    }

    public void addMessage(String s) {
        messages.add(s);
    }

    public void clearPotentialCourse() {
        potentialCourses.clear();
    }

    public void clearMissingCourse() {
        missingCourses.clear();
    }

    public void clearMessages() {
        messages.clear();
    }
}
