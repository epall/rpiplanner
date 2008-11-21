/* RPI Planner - Customized plans of study for RPI students.
 *
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package rpiplanner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

import rpiplanner.model.Course;
import rpiplanner.model.CourseDatabase;
import rpiplanner.model.PlanOfStudy;

import com.thoughtworks.xstream.XStream;

// OSX imports

import com.apple.eawt.*;

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
        planControl = new POSController();

        loadFromXML();
        PlanValidator.getInstance(); // boot up ruby environment
        planControl.setCourseDatabase(courseDatabase);
        mainFrame = new MainFrame();
        planControl.setView(mainFrame.getPlanCard());
        mainFrame.setController(planControl);
        
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame.setVisible(false);
                exit();
            }
        });
        
        initializeMenuBar();

        try {
        	initializeOSXExtensions();
        } catch (ClassNotFoundException e){
        	// silently ignore because we're not on a mac
        } catch (NullPointerException e){
        	e.printStackTrace();
        }
        mainFrame.setVisible(true);
    }
    
    private javax.swing.Action getAction(String actionName) {
        ApplicationContext ac = this.getContext();
        return ac.getActionMap(getClass(), this).get(actionName);
    }
    
	private void initializeMenuBar() {
		final JMenuBar menu = new JMenuBar();
		final JMenu fileMenu = new JMenu("Plan");
		
		final JMenuItem fileSave = new JMenuItem("Save");
		fileSave.setAction(getAction("savePlan"));
		fileMenu.add(fileSave);

		final JMenuItem fileOpen = new JMenuItem();
		fileOpen.setAction(getAction("loadPlan"));
		fileMenu.add(fileOpen);

		final JMenuItem fileUpdate = new JMenuItem("Update from course database");
		fileUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				planControl.updatePlanFromDatabase();
			}
		});
		fileMenu.add(fileUpdate);

		menu.add(fileMenu);
		mainFrame.setJMenuBar(menu);
	}

	protected void loadFromXML(){
		File localStorageDir = getContext().getLocalStorage().getDirectory();
    	try {
			courseDatabase = (CourseDatabase) xs.fromXML(new FileInputStream(new File(localStorageDir, "course_database.xml")));
		} catch (FileNotFoundException e) {
			try{
				courseDatabase = (CourseDatabase) xs.fromXML(getClass().getResourceAsStream("/course_database.xml"));
			} catch(NullPointerException e1){
				courseDatabase = new CourseDatabase();
			}
		}
		try {
			planControl.setPlan((PlanOfStudy) xs.fromXML(new FileInputStream(new File(localStorageDir, "default_pos.xml"))));
		} catch (FileNotFoundException e) {
			planControl.setPlan(new PlanOfStudy());
		}
    }
	
	private void initializeOSXExtensions() throws ClassNotFoundException {
		com.apple.eawt.Application fApplication = com.apple.eawt.Application.getApplication();
		fApplication.setEnabledPreferencesMenu(false);
		fApplication.setEnabledAboutMenu(false);
		fApplication.addApplicationListener(new com.apple.eawt.ApplicationAdapter() {
			public void handleAbout(ApplicationEvent e) {
			}
			public void handleOpenApplication(ApplicationEvent e) {
			}
			public void handleOpenFile(ApplicationEvent e) {
			}
			public void handlePreferences(ApplicationEvent e) {
			}
			public void handlePrintFile(ApplicationEvent e) {
			}
			public void handleQuit(ApplicationEvent e) {
				quit(null);
			}
		});
	}
	
    @Override
    protected void shutdown() {
    	try {
    		File localStorageDir = getContext().getLocalStorage().getDirectory();
    		localStorageDir.mkdir();

    		File cdbFile = new File(localStorageDir, "course_database.xml");
       		File planFile = new File(localStorageDir, "default_pos.xml");
       		
			xs.toXML(courseDatabase, new FileOutputStream(cdbFile));
			xs.toXML(planControl.getPlan(), new FileOutputStream(planFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static CourseDatabase getCourseDatabase() {
		return courseDatabase;
	}
    
    @Action
    public void savePlan(){
	    JFileChooser chooser = new JFileChooser();
	    
        int returnVal = chooser.showSaveDialog(mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if(!file.getName().endsWith(".plan")){
            	file = new File(file.getPath()+".plan");
            }
    		try {
				xs.toXML(planControl.getPlan(), new FileOutputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    @Action
    public void loadPlan(){
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".plan");
			}

			@Override
			public String getDescription() {
				return "Plan of Study file";
			}
	    	
	    });
	    int option = chooser.showOpenDialog(mainFrame);
	    if (option == JFileChooser.APPROVE_OPTION) {
	    	File file = chooser.getSelectedFile();
			try {
				planControl.setPlan((PlanOfStudy) xs.fromXML(new FileInputStream(file)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }

	public static void main(String[] args) {
		xs = new XStream();
		xs.processAnnotations(PlanOfStudy.class);
		xs.processAnnotations(Course.class);
		xs.processAnnotations(CourseDatabase.class);
        Application.launch(Main.class, args);
    }
}
