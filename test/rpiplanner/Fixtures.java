package rpiplanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.model.DefaultCourseDatabase;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.model.ShadowCourseDatabase;
import rpiplanner.model.YearPart;
import rpiplanner.xml.RequisiteSetConverter;

public class Fixtures {
	private static CourseDatabase db = null;
	
	public static CourseDatabase getCourseDatabase(){
		if(db == null){
			XStream xs = new XStream();
			xs.processAnnotations(PlanOfStudy.class);
			xs.processAnnotations(Course.class);
			xs.processAnnotations(DefaultCourseDatabase.class);
			xs.processAnnotations(ShadowCourseDatabase.class);
			xs.processAnnotations(YearPart.class);
			xs.registerConverter(new RequisiteSetConverter(xs.getMapper()));
			InputStream databaseStream;
			try {
				databaseStream = new FileInputStream("course_database.xml");
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			DefaultCourseDatabase mainDB = (DefaultCourseDatabase) xs.fromXML(databaseStream);

			ShadowCourseDatabase shadow = new ShadowCourseDatabase();
			shadow.shadow(mainDB);
			
			db = shadow;
		}
		return db;
	}
}
