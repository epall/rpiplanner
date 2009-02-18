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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import rpiplanner.POSController;
import rpiplanner.model.Course;
import rpiplanner.model.RequisiteSet;
import rpiplanner.model.Term;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CourseDisplay extends JPanel {
	private JLabel xButton;
	private Course course;
	private POSController controller;
	private CourseTransferHandler handler;
	
	public CourseDisplay(POSController controller){
		this.controller = controller;
		setOpaque(true);
		initialize();
		setText("Drop course here");
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
				ColumnSpec.decode("right:min")},
			new RowSpec[] {
				FormFactory.MIN_ROWSPEC}));

		JLabel label = new JLabel("Add Course...");
		this.add(label, new CellConstraints(1, 1));

		handler = new CourseTransferHandler(controller);
		setTransferHandler(handler);
		
		xButton = new JLabel("X");
		xButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.removeCourse(getParent(), CourseDisplay.this);
			}
		});
		add(xButton, new CellConstraints(3, 1));

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(final MouseEvent e) {
				handler.exportAsDrag(CourseDisplay.this, e, TransferHandler.MOVE);
			}
		});
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					if (course != null) {
						JPopupMenu contextMenu = new JPopupMenu();
						
						contextMenu.add("Fill Requisites").addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								fillRequisites(course, controller.getTerm(course));
							}
						});
						
						contextMenu.show(getParent(), e.getX() + 10, e.getY() + 30);
					}
				}
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
	
	@Override
	protected void printChildren(Graphics g) {
		Component text = getComponent(0);
		Dimension oldSize = text.getSize();
		text.setSize(this.getWidth(), this.getHeight());
		getComponent(0).paint(g);
		text.setSize(oldSize);
	}
	
	private void fillRequisites(Course fillCourse, int term) {
		// for some reason, fillCourse.getPrerequisites() didnt work, but it WOULD work
		// if i set fillCourse to controller.getCourseDatabase().getCourse(fillCourse.getCatalogNumber())
		// no idea why
		// for some courses, controller.getCourseDatabase().getCourse(fillCourse.getCatalogNumber()) returns null
		// dont know why this happens, either
		fillCourse = controller.getCourseDatabase().getCourse(fillCourse.getCatalogNumber());	
		RequisiteSet reqs = fillCourse.getPrerequisites();
		
		for (int i = 0; i < reqs.size(); i++) {
			// if the course isnt offered in both spring and fall, and we arent in the term that its offered in,
			// go back one term so that we are in the term its offered in
			if ((fillCourse.getAvailableTerms().length < 2) && (fillCourse.getAvailableTerms()[0] != controller.getPlan().getTerm(term).getTerm())) {
				term--;
			}
			
			// make sure theres still terms
			// the ap/transfer term is term 0,
			// if we dont want to use that then make it (term - 1 >= 1)
			if (term - 1 >= 0) {
				fillRequisites(reqs.get(i), term - 1);
			}
			
			// ran out of terms for this line of prereqs
			else {
				return;
			}
		}
		
		// this looks to see if the requisite class were trying to add
		// has already been added.
		// not sure if theres an easier way to check or not
		ArrayList<Term> dupes = controller.getPlan().getTerms();
		boolean dupeCourse = false;
		for (int t = 0; t < dupes.size(); t++) {
			ArrayList<Course> dupeCourses = dupes.get(t).getCourses();
			
			for (int dc = 0; dc < dupeCourses.size(); dc++) {
				if (dupeCourses.get(dc).equals(fillCourse)) {
					dupeCourse = true;
					break;
				}
			}
		}
		
		// add course if no duplicate found
		if (!dupeCourse) {
			controller.addCourse(term, fillCourse);
		}
		
		// now do corequisites and their requisites
		RequisiteSet coreqs = fillCourse.getCorequisites();
		for (int k = 0; k < coreqs.size(); k++) {
			fillRequisites(coreqs.get(k), term);
		}
	}
}
