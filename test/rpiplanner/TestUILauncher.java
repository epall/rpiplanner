package rpiplanner;

import org.uispec4j.Trigger;
import org.uispec4j.UISpecAdapter;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowInterceptor;

public class TestUILauncher implements UISpecAdapter {
	private static Main application = null;
	private static Window applicationWindow = null;
	
	public Window getMainWindow() {
		if (applicationWindow == null) {
			applicationWindow = WindowInterceptor.run(new Trigger() {
				public void run() throws Exception {
					Main.main(null);
				}
			});
		}
		application = Main.getInstance(Main.class);
		return applicationWindow;
	}
	
	public static POSController getController(){
		return application.getController();
	}
}
