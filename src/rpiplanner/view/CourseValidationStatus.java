package rpiplanner.view;

import javax.swing.JLabel;

import com.jgoodies.forms.layout.CellConstraints;
import com.swtdesigner.SwingResourceManager;

import rpiplanner.POSController;
import rpiplanner.model.Course;

public class CourseValidationStatus extends CourseDisplay {
	public enum Status {PASS, FAIL};
	
	private JLabel passFail;

	public CourseValidationStatus(POSController controller) {
		super(controller);
		initialize();
	}

	public CourseValidationStatus(POSController controller, Course course) {
		super(controller, course);
		initialize();
	}

	private void initialize(){
		this.remove(xButton);
		passFail = new JLabel();
		passFail.setIcon(SwingResourceManager.getIcon(getClass(), "resources/fail.png"));
		add(passFail, new CellConstraints(3, 1));
	}
	
	public void setStatus(Status newStatus){
		if(newStatus == Status.PASS)
			passFail.setIcon(SwingResourceManager.getIcon(getClass(), "resources/pass.png"));
		else
			passFail.setIcon(SwingResourceManager.getIcon(getClass(), "resources/fail.png"));
	}
}
