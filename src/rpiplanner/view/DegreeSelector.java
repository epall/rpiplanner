package rpiplanner.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;

import rpiplanner.POSController;
import rpiplanner.model.Degree;

public class DegreeSelector extends JDialog {
	private JList list;
	private POSController controller;
	/**
	 * Create the dialog
	 */
	public DegreeSelector() {
		super();
		setBounds(100, 100, 500, 375);

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		list = new JList();
		scrollPane.setViewportView(list);

		final JButton addButton = new JButton();
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Degree toAdd = (Degree)list.getSelectedValue();
				controller.addDegree(toAdd);
				setVisible(false);
			}
		});
		addButton.setText("Add");
		getContentPane().add(addButton, BorderLayout.SOUTH);
		//
	}
	
	public DegreeSelector(final POSController controller){
		this();
		this.controller = controller;
		list.setModel(new AbstractListModel(){
			private Degree[] degrees = controller.getCourseDatabase().listDegrees();

			public Object getElementAt(int index) {
				return degrees[index];
			}

			public int getSize() {
				return degrees.length;
			}
		});
	}
}
