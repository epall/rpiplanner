package rpiplanner.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rpiplanner.Main;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class NewCourseDialog extends JDialog {
	private JTextArea descriptionTextArea;
	private JTextField catalogField;
	private JTextField titleField;
	private JTextField departmentField;
	/**
	 * Create the dialog
	 */
	public NewCourseDialog() {
		super();
		getContentPane().setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow(1.0)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC}));
		setTitle("New Course");
		setBounds(100, 100, 461, 205);

		final JLabel courseTitleLabel = new JLabel();
		courseTitleLabel.setText("Department");
		getContentPane().add(courseTitleLabel, new CellConstraints(2, 2));

		final JLabel titleLabel = new JLabel();
		titleLabel.setText("Title");
		getContentPane().add(titleLabel, new CellConstraints(2, 4));

		final JLabel catalogNumberLabel = new JLabel();
		catalogNumberLabel.setText("Catalog number");
		getContentPane().add(catalogNumberLabel, new CellConstraints(2, 6));

		final JLabel descriptionLabel = new JLabel();
		descriptionLabel.setText("Description");
		getContentPane().add(descriptionLabel, new CellConstraints(2, 8));

		final JButton saveButton = new JButton();
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				createCourse();
				setVisible(false);
			}
		});
		saveButton.setText("Save");
		getContentPane().add(saveButton, new CellConstraints(2, 10));

		final JButton cancelButton = new JButton();
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
			}
		});
		cancelButton.setText("Cancel");
		getContentPane().add(cancelButton, new CellConstraints(4, 10));

		departmentField = new JTextField();
		getContentPane().add(departmentField, new CellConstraints(4, 2));

		titleField = new JTextField();
		getContentPane().add(titleField, new CellConstraints(4, 4));

		catalogField = new JTextField();
		getContentPane().add(catalogField, new CellConstraints(4, 6));

		descriptionTextArea = new JTextArea();
		getContentPane().add(descriptionTextArea, new CellConstraints(4, 8));
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//
	}
	
	protected void createCourse() {
		rpiplanner.model.Course newCourse = new rpiplanner.model.Course();
		newCourse.setDepartment(departmentField.getText());
		newCourse.setTitle(titleField.getText());
		newCourse.setCatalogNumber(catalogField.getText());
		newCourse.setDescription(descriptionTextArea.getText());
		Main.getCourseDatabase().add(newCourse);
	}

}
