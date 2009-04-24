package rpiplanner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.GlobalVariable;
import org.jruby.runtime.builtin.IRubyObject;

import rpiplanner.model.CourseDatabase;
import rpiplanner.model.Degree;
import rpiplanner.model.Course;
import rpiplanner.validation.DegreeValidator;

public class RubyEnvironment {
	private Ruby rubyEnvironment;
	private RubyHash degrees;
	private CourseDatabase courseDatabase;
	
	private static RubyEnvironment instance;
	
	public static RubyEnvironment getInstance(){
		if(instance == null)
			instance = new RubyEnvironment();
		return instance;
	}
	
	protected RubyEnvironment() {
		rubyEnvironment = Ruby.newInstance();
		try {
			rubyEnvironment.executeScript(readFileAsString("plan_validation_2.rb"), "plan_validation_2.rb");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		degrees = RubyHash.newHash(rubyEnvironment);
		GlobalVariable degreesGlob = new GlobalVariable(rubyEnvironment, "$degrees", degrees);
		rubyEnvironment.defineVariable(degreesGlob);
	}
	
	private static String readFileAsString(String filePath)
	throws java.io.IOException{
	    StringBuffer fileData = new StringBuffer(1000);
	    InputStream fileStream = RubyEnvironment.class.getResourceAsStream("/"+filePath);
	    if(fileStream == null){
	    	try{
	    		fileStream = new FileInputStream(filePath);
	    	} catch (FileNotFoundException e){
	    		System.err.println("Critical file not found: "+filePath);
	    		return "";
	    	}
	    }
	    BufferedReader reader = new BufferedReader(
	            new InputStreamReader(fileStream));
	    char[] buf = new char[1024];
	    int numRead=0;
	    while((numRead=reader.read(buf)) != -1){
	        fileData.append(buf, 0, numRead);
	    }
	    reader.close();
	    return fileData.toString();
	}
	
	public void setCourseDatabase(CourseDatabase courseDatabase) {
		this.courseDatabase = courseDatabase;
		GlobalVariable databaseGlob = new GlobalVariable(rubyEnvironment,
				"$courseDatabase", JavaEmbedUtils.javaToRuby(rubyEnvironment,
						courseDatabase));
		rubyEnvironment.defineVariable(databaseGlob);
	}

    public Course[] convertAPCourses(ArrayList<String> tests, ArrayList<Integer> scores){
        try {
            rubyEnvironment.executeScript(readFileAsString("apcredit.rb"), "apcredit.rb");
        } catch (IOException e) {
            return null;
        }
        IRubyObject instance = rubyEnvironment.getClass("Apcredit").newInstance(rubyEnvironment.getCurrentContext(), new IRubyObject[0], null);
        IRubyObject[] args = new IRubyObject[2];
        args[0] = JavaEmbedUtils.javaToRuby(rubyEnvironment, tests);
        args[1] = JavaEmbedUtils.javaToRuby(rubyEnvironment, scores);
        return (Course[])JavaEmbedUtils.rubyToJava(rubyEnvironment, instance.callMethod(rubyEnvironment.getCurrentContext(), "getcourse", args), Course[].class);
    }

	public DegreeValidator getDegreeDescriptor(Degree degree) {
		DegreeValidator desc = (DegreeValidator) degrees.get(degree.getID());

		// no descriptor cached
		if(desc == null){
			rubyEnvironment.executeScript(courseDatabase.getDegree(
					degree.getID()).getValidationCode(), degree.getName());
		}

		desc = (DegreeValidator)degrees.get(degree.getID());
		
		if(desc == null)// nothing found. Yikes!
			throw new RuntimeException("Validation code for degree "+degree.getName()+" failed to initialize");
		
		return desc;
	}


}
