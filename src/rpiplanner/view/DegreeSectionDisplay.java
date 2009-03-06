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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXCollapsiblePane.Direction;

import rpiplanner.POSController;
import rpiplanner.model.Course;
import rpiplanner.validation.ValidationResult.Section;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.swtdesigner.SwingResourceManager;

public class DegreeSectionDisplay extends JPanel {

	private JLabel passFail;
	private JLabel statusLabel;
	private JLabel nameLabel;
	private JXCollapsiblePane detailsPane;
	private POSController controller;
	private JButton searchButton;
	private Section result;
	
	/**
	 * Create the panel
	 */
	public DegreeSectionDisplay() {
		super();
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("30px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC},
			new RowSpec[] {
				RowSpec.decode("30px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow(1.0)")}));

		nameLabel = new JLabel();
		nameLabel.setText("Section");
		add(nameLabel, new CellConstraints("3, 1, 1, 1"));

		final JToggleButton detailsToggle = new JToggleButton();
		detailsToggle.setPressedIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/arrow_down.gif"));
		detailsToggle.setIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/arrow_right.gif"));
		detailsToggle.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				detailsPane.setCollapsed(!detailsToggle.getModel().isSelected());
				if(detailsToggle.getModel().isSelected())
					detailsToggle.setIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/arrow_down.gif"));
				else
					detailsToggle.setIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/arrow_right.gif"));
			}
		});
		add(detailsToggle, new CellConstraints());
		
		detailsPane = new JXCollapsiblePane(Direction.DOWN);
		detailsPane.setLayout(new BoxLayout(detailsPane.getContentPane(), BoxLayout.Y_AXIS));
		add(detailsPane, new CellConstraints("1, 3, 5, 1"));
		detailsPane.setCollapsed(true);

		statusLabel = new JLabel();
		statusLabel.setText("Status");
		detailsPane.add(statusLabel, BorderLayout.NORTH);

		passFail = new JLabel();
		passFail.setIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/fail.png"));
		add(passFail, new CellConstraints(5, 1));
		
		searchButton = new JButton("Search for more");
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				controller.setSearchCourses(result.potentialCourses());
			}
		});
		//
	}

	public DegreeSectionDisplay(POSController controller) {
		this();
		this.controller = controller;
	}

	public void setValidationResult(Section result){
		this.result = result;
		if(result.isSuccess())
			passFail.setIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/pass.png"));
		else
			passFail.setIcon(SwingResourceManager.getIcon(DegreeSectionDisplay.class, "resources/fail.png"));
		
		detailsPane.removeAll();
		for(Course c : result.appliedCourses()){
			CourseValidationStatus cvs = new CourseValidationStatus(controller, c);
			cvs.setStatus(CourseValidationStatus.Status.PASS);
			detailsPane.add(cvs);
		}
		for(Course c : result.missingCourses()){
			CourseValidationStatus cvs = new CourseValidationStatus(controller, c);
			cvs.setStatus(CourseValidationStatus.Status.FAIL);
			detailsPane.add(cvs);
		}
		
		for(String s : result.messages()){
			detailsPane.add(new JLabel(new String(s)));
		}
		
		if(result.potentialCourses() != null)
			detailsPane.add(searchButton);
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		nameLabel.setText(name);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
}
