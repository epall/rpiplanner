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
            XStream xs = Main.initializeXStream();

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
			
			RubyEnvironment.getInstance().setCourseDatabase(db);
		}
		return db;
	}

	public static Degree getCSYS() {
		return getCourseDatabase().getDegree(1);
	}
}
