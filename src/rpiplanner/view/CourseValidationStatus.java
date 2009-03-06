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
