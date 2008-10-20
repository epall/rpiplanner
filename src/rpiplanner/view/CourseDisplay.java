package rpiplanner.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import org.jruby.javasupport.JavaUtil.JavaConverter;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import rpiplanner.POSController;
import rpiplanner.model.Course;
import rpiplanner.model.Term;

public class CourseDisplay extends JPanel {
	private JButton xButton;
	private Course course;
	private POSController controller;
	
	public CourseDisplay(POSController controller){
		this.controller = controller;
		initialize();
		setText("Add Course...");
		xButton.setVisible(false);
	}
	public CourseDisplay(POSController controller, Course course){
		this.course = course;
		this.controller = controller;
		initialize();
		setText(course.toString());
	}
	
	private void initialize(){
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("4dlu:grow(1.0)"),
				ColumnSpec.decode("right:24px")},
			new RowSpec[] {
				RowSpec.decode("17px")}));

		JLabel label = new JLabel("Add Course...");
		this.add(label, new CellConstraints(1, 1));

		xButton = new JButton();
		xButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				controller.removeCourse(getParent(), CourseDisplay.this);
			}
		});
		xButton.setIcon(new ImageIcon("remove_course.png"));
		add(xButton, new CellConstraints(3, 1));
		//
	}
	
	public void setText(String text){
		JLabel label = (JLabel) this.getComponent(0);
		label.setText(text);
	}
}
