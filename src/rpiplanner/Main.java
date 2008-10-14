/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpiplanner;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFrame;

import org.jdesktop.application.Application;

import rpiplanner.model.CourseDatabase;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Eric Allen
 */
public class Main extends Application {
    private MainFrame mainFrame;
    private static POSController planControl;
    private static CourseDatabase courseDatabase;
	private static XStream xs;

    @Override
    protected void startup() {
    	loadCourseDatabase();
        mainFrame = new MainFrame();
        planControl = new POSController(mainFrame.getPlanCard());
        planControl.setCourseDatabase(courseDatabase);
        mainFrame.setController(planControl);
        
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame.setVisible(false);
                exit();
            }
        });
        mainFrame.setVisible(true);
    }
    
	protected void loadCourseDatabase(){
    	try {
			courseDatabase = (CourseDatabase) xs.fromXML(new FileInputStream("course_database.xml"));
		} catch (FileNotFoundException e) {
			courseDatabase = new CourseDatabase();
		}
    }
    
    @Override
    protected void shutdown() {
    	try {
			xs.toXML(courseDatabase, new FileOutputStream("course_database.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static CourseDatabase getCourseDatabase() {
		return courseDatabase;
	}

	public static void main(String[] args) {
		xs = new XStream();
        Application.launch(Main.class, args);
    }
}