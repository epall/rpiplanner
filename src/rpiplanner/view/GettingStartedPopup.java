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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import rpiplanner.POSController;
import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;

public class GettingStartedPopup extends JDialog {
	private JComboBox majorComboBox;
	private JComboBox startingYearComboBox;
	private JTextArea descriptionTextArea;
	private JTextArea textArea;
	private POSController controller;

	// for Swing Designer
	public GettingStartedPopup() {
		setTitle("Getting Started");

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setAutoscrolls(false);
		descriptionTextArea
				.setText("Welcome to RPI Planner. Please enter your starting year and degree to get started.");
		getContentPane().add(descriptionTextArea, BorderLayout.NORTH);
		getContentPane().setLayout(new BorderLayout());

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.SOUTH);

		final JButton nextButton = new JButton();
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				save();
				setVisible(false);
			}
		});
		nextButton.setText("Next >>");
		panel.add(nextButton);

		final JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("4dlu:grow(1.0)"),
				ColumnSpec.decode("right:default"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("4dlu:grow(1.0)")},
			new RowSpec[] {
				RowSpec.decode("default:grow(1.0)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow(1.0)")}));
		getContentPane().add(inputPanel, BorderLayout.CENTER);

		final JLabel startingYearLabel = new JLabel();
		startingYearLabel.setText("Starting Year:");
		inputPanel.add(startingYearLabel, new CellConstraints(2, 3));

		Integer[] years = { 2006, 2007, 2008, 2009, 2010, 2011 };
		startingYearComboBox = new JComboBox(years);
		inputPanel.add(startingYearComboBox, new CellConstraints(4, 3));

		final JLabel majorLabel = new JLabel();
		majorLabel.setText("Major:");
		inputPanel.add(majorLabel, new CellConstraints(2, 5));

		majorComboBox = new JComboBox();
		inputPanel.add(majorComboBox, new CellConstraints(4, 5));
	}

	public GettingStartedPopup(POSController controller){
		this();
		this.controller = controller;
		majorComboBox.setModel(controller.getDegreeListModel());
		if (controller.getPlan().getDegrees() != null
				&& controller.getPlan().getDegrees().size() > 0) {
			Degree major = controller.getPlan().getDegrees().get(0);
			majorComboBox.setSelectedItem(major);
		}
		PlanOfStudy plan = controller.getPlan();
		BeanProperty<JComboBox, String> selectedItem = BeanProperty.create("selectedItem");

		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, plan,
				BeanProperty.create("startingYear"), startingYearComboBox,
				selectedItem).bind();
	}

	private void save(){
		controller.initializeTerms((Integer)startingYearComboBox.getSelectedItem());
		ArrayList<Degree> degrees = controller.getPlan().getDegrees();
		if(degrees.contains(majorComboBox.getSelectedItem()) || majorComboBox.getSelectedItem() == null)
			;
		else
			controller.addDegree((Degree)majorComboBox.getSelectedItem());
		controller.validatePlan();
	}
}
