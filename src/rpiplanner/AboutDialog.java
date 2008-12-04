package rpiplanner;

import javax.swing.JDialog;

public class AboutDialog extends JDialog {
	private static AboutDialog instance = null;
	
	public static void showDialog(){
		if(instance == null)
			instance = new AboutDialog();
		instance.setVisible(true);
	}
	
	public AboutDialog() {
		super();
		setTitle("About RPI Planner");
	}
	
}
