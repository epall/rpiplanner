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

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import rpiplanner.view.GettingStartedPopup;
import rpiplanner.view.PlanOfStudyEditor;

public class MainFrame extends JFrame {

	private JComboBox majorComboBox;
	private JComboBox comboBox;
	private PlanOfStudyEditor planCard;
	private JTextField studentIDfield;
	private JTextField schoolField;
	private JTextField nameField;
	private POSController controller;

	public MainFrame() {
		super();
		getContentPane().setLayout(new CardLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = new Dimension(1200, 800);
		if(windowSize.width > screenSize.width-200)
			windowSize.width = screenSize.width-200;
		setBounds((screenSize.width-windowSize.width)/2, (screenSize.height-windowSize.height)/2, 
				windowSize.width, windowSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		planCard = new PlanOfStudyEditor();
		planCard.setName("plan");
		getContentPane().add(planCard, planCard.getName());
		setTitle("RPI Planner");
		//
	}
	public PlanOfStudyEditor getPlanCard() {
		return planCard;
	}

	public void setController(POSController controller) {
		this.controller = controller;
	}
	
	public void gettingStarted() {
		GettingStartedPopup popup = new GettingStartedPopup(controller);
		Rectangle bounds = new Rectangle();
		bounds.x = getBounds().x + getBounds().width/4;
		bounds.y = getBounds().y + getBounds().height/4;
		bounds.width = getBounds().width/2;
		bounds.height = getBounds().height/2;
		popup.setBounds(bounds);
		popup.setVisible(true);
	}
}
