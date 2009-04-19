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
import java.util.Collections;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import rpiplanner.POSController;
import rpiplanner.RequisiteFiller;
import rpiplanner.model.Course;

import rpiplanner.model.CourseComparator;
import rpiplanner.model.DummyPOSComparator;
import rpiplanner.model.RequisiteSet;
import rpiplanner.model.Term;
import rpiplanner.model.Pair;
import rpiplanner.model.ValidationError;


import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CourseDisplay extends JPanel {
	protected JLabel xButton;
	protected Course course;
	protected POSController controller;
	protected CourseTransferHandler handler;
	
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
					if (course != null && getBackground() == Color.red) {
						JPopupMenu contextMenu = new JPopupMenu();
						
						contextMenu.add("Fill Requisites").addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								new RequisiteFiller(controller, course);
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

    public void setErrors(List<ValidationError> errors) {
        StringBuilder tooltip = new StringBuilder();
        // now set its properties
        ValidationError worstError = new ValidationError(ValidationError.Type.NONE, "");
        for(ValidationError e : errors){
            if(e.compareTo(worstError) > 0)
                worstError = e;
            tooltip.append(e.getMessage());
            tooltip.append("\n");
        }
        if(worstError.getType() == ValidationError.Type.ERROR)
            this.setBackground(Color.red);
        else if(worstError.getType() == ValidationError.Type.WARNING)
            this.setBackground(Color.yellow);
        else
            this.setBackground(null);
        this.setToolTipText(tooltip.toString());
    }
}
