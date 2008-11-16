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

package rpiplanner.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import rpiplanner.POSController;
import rpiplanner.model.Course;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CourseDisplay extends JPanel {
	private JButton xButton;
	private Course course;
	private POSController controller;
	private CourseTransferHandler handler;
	private boolean prerequisitesSatisfied = true;
	private boolean corequisitesSatisfied = true;
	
	public CourseDisplay(POSController controller){
		this.controller = controller;
		setOpaque(true);
		initialize();
		setText("Add Course...");
		xButton.setVisible(false);
	}
	public CourseDisplay(POSController controller, Course course){
		this.course = course;
		this.controller = controller;
		setOpaque(true);
		initialize();
		setText(course.toString());
	}
	
	private void initialize(){
		setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("left:0dlu:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:24px")},
			new RowSpec[] {
				RowSpec.decode("17px")}));

		JLabel label = new JLabel("Add Course...");
		this.add(label, new CellConstraints(1, 1));

		handler = new CourseTransferHandler(controller);
		setTransferHandler(handler);
		
		xButton = new JButton();
		xButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				controller.removeCourse(getParent(), CourseDisplay.this);
			}
		});
		xButton.setIcon(new ImageIcon("remove_course.png"));
		add(xButton, new CellConstraints(3, 1));
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(final MouseEvent e) {
				handler.exportAsDrag(CourseDisplay.this, e, TransferHandler.MOVE);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(final MouseEvent e) {
				controller.setDetailDisplay(course);
			}
			@Override
			public void mouseExited(final MouseEvent e) {
				controller.setDetailDisplay((Course)null);
			}
		});
		//
	}
	
	public void setText(String text){
		JLabel label = (JLabel) this.getComponent(0);
		label.setText(text);
	}
	
	public Course getCourse(){
		return course;
	}
	public void setPrerequisitesSatisfied(boolean prerequisitesSatisfied) {
		if(prerequisitesSatisfied != this.prerequisitesSatisfied){
			if(prerequisitesSatisfied)
				setBackground(null);
			else
				setBackground(Color.RED);
		}
		this.prerequisitesSatisfied = prerequisitesSatisfied;
	}
	public void setCorequisitesSatisfied(boolean corequisitesSatisfied) {
		if(corequisitesSatisfied != this.corequisitesSatisfied){
			if(corequisitesSatisfied)
				setBackground(null);
			else
				setBackground(Color.YELLOW);
		}
		this.corequisitesSatisfied = corequisitesSatisfied;
	}
}
