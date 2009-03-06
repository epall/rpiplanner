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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import rpiplanner.BrowserLauncher;
import rpiplanner.Main;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AboutDialog extends JDialog {
	private static AboutDialog instance = null;
	private JScrollPane scrollPane;
	
	public static void showDialog(JFrame mainFrame){
		if(instance == null)
			instance = new AboutDialog(mainFrame);
		instance.setVisible(true);
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				instance.scrollPane.getViewport().setViewPosition(new Point(0,0));
			}
		});
	}
	
	public AboutDialog(JFrame parent) {
		super(parent);
		getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("left:1in:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:1in:grow(1.0)")},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow(1.0)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC}));
		setTitle("About RPI Planner");

		final JLabel rpiPlannerLabel = new JLabel();
		rpiPlannerLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
		rpiPlannerLabel.setText("RPI Planner");
		getContentPane().add(rpiPlannerLabel, new CellConstraints(3, 1));

		final JButton okButton = new JButton();
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
			}
		});
		okButton.setText("OK");
		getContentPane().add(okButton, new CellConstraints(3, 5));

		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane, new CellConstraints(1, 3, 5, 1));

		final JEditorPane fooEditorPane = new JEditorPane();
		fooEditorPane.setContentType("text/html");
		fooEditorPane.setText(Main.getInstance().getContext().getResourceMap().getString("aboutDescription"));
		fooEditorPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(final HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
					BrowserLauncher.openURL(e.getURL().toString());
				}
			}
		});
		fooEditorPane.setEditable(false);
		scrollPane.setViewportView(fooEditorPane);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = new Dimension(600, 500);
		if(windowSize.width > screenSize.width-200)
			windowSize.width = screenSize.width-200;
		setBounds((screenSize.width-windowSize.width)/2, (screenSize.height-windowSize.height)/2, 
				windowSize.width, windowSize.height);

		final JLabel label = new JLabel();
		label.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/opensource-75x65.png"))));
		getContentPane().add(label, new CellConstraints(5, 1));
	}	
}
