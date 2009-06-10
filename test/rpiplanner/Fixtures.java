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

package rpiplanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.model.DefaultCourseDatabase;
import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.model.ShadowCourseDatabase;
import rpiplanner.model.YearPart;
import rpiplanner.xml.RequisiteSetConverter;

public class Fixtures {
	private static CourseDatabase db = null;
	
	public static CourseDatabase getCourseDatabase(){
		if(db == null){
			InputStream databaseStream;
			try {
				databaseStream = new FileInputStream("course_database.xml");
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			DefaultCourseDatabase mainDB = (DefaultCourseDatabase) XML.readDefaultCourseDatabase(databaseStream);

			ShadowCourseDatabase shadow = new ShadowCourseDatabase();
			shadow.shadow(mainDB);
			
			db = shadow;
			
			RubyEnvironment.getInstance().setCourseDatabase(db);
		}
		return db;
	}

	public static Degree getCSYS() {
		//TODO: CLEAN UP
        return null; //getCourseDatabase().getDegree(1);
	}
}
