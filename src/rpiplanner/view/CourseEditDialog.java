package rpiplanner.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import rpiplanner.Main;
import rpiplanner.POSController;
import rpiplanner.model.Course;
import rpiplanner.model.Term;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CourseEditDialog extends JDialog {
	private JPanel offeredDuringPanel;
	private JComboBox creditsComboBox;
	private JTextArea descriptionTextArea;
	private JTextField catalogField;
	private JTextField titleField;
	private JTextField departmentField;
	private Course toEdit;
	private boolean newCourse = true;
	/**
	 * Create the dialog
	 */
	public CourseEditDialog() {
		super();
		this.toEdit = new Course();
		initialize();
		bind();
	}

	protected void initialize() {
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow(1.0)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC}));
		setTitle("New Course");
		setBounds(100, 100, 447, 281);

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
		getContentPane().add(descriptionLabel, new CellConstraints(2, 12));

		final JButton saveButton = new JButton();
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if(newCourse)
					createCourse();
				else
					updateCourse();
				setVisible(false);
			}
		});
		saveButton.setText("Save");
		getContentPane().add(saveButton, new CellConstraints(2, 14));

		final JButton cancelButton = new JButton();
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
			}
		});
		cancelButton.setText("Cancel");
		getContentPane().add(cancelButton, new CellConstraints(4, 14));

		departmentField = new JTextField();
		getContentPane().add(departmentField, new CellConstraints(4, 2));

		titleField = new JTextField();
		getContentPane().add(titleField, new CellConstraints(4, 4));

		catalogField = new JTextField();
		getContentPane().add(catalogField, new CellConstraints(4, 6));

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, new CellConstraints(4, 12));

		descriptionTextArea = new JTextArea();
		scrollPane.setViewportView(descriptionTextArea);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		final JLabel creditsLabel = new JLabel();
		creditsLabel.setText("Credits");
		getContentPane().add(creditsLabel, new CellConstraints(2, 8));

		final JLabel offeredDuringLabel = new JLabel();
		offeredDuringLabel.setText("Offered during");
		getContentPane().add(offeredDuringLabel, new CellConstraints(2, 10));

		creditsComboBox = new JComboBox(new Integer[] {1, 2, 3, 4});
		creditsComboBox.setSelectedIndex(3);
		getContentPane().add(creditsComboBox, new CellConstraints(4, 8));

		offeredDuringPanel = new JPanel();
		for(Term.YearPart p : Term.YearPart.values()){
			final JCheckBox termBox = new JCheckBox(p.toString().toLowerCase());
			termBox.setSelected(true);
			offeredDuringPanel.add(termBox);
		}
		
		getContentPane().add(offeredDuringPanel, new CellConstraints(4, 10));
		//
	}
	
	public CourseEditDialog(Course toEdit){
		newCourse = false;
		this.toEdit = toEdit;
		initialize();
		bind();
	}
	
	protected void bind(){
		BeanProperty<JTextField, String> text = BeanProperty.create("text");
		BeanProperty<JTextArea, String> textArea = BeanProperty.create("text");
		BeanProperty<JComboBox, String> selectedItem = BeanProperty.create("selectedItem");
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, toEdit, BeanProperty.create("department"), departmentField, text).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, toEdit, BeanProperty.create("title"), titleField, text).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, toEdit, BeanProperty.create("catalogNumber"), catalogField, text).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, toEdit, BeanProperty.create("description"), descriptionTextArea, textArea).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, toEdit, BeanProperty.create("credits"), creditsComboBox, selectedItem).bind();
	}
	
	protected void createCourse() {
		updateCourse();
		Main.getCourseDatabase().add(toEdit);
	}
	
	protected void updateCourse() {
		
		
		ArrayList<Term.YearPart> offeredDuring = new ArrayList<Term.YearPart>();
		for(Component c : offeredDuringPanel.getComponents()){
			JCheckBox box = (JCheckBox)c;
			offeredDuring.add(Term.YearPart.valueOf(box.getText().toUpperCase()));
		}
		toEdit.setAvailableTerms(offeredDuring.toArray(new Term.YearPart[0]));
	}
	
	public void setController(POSController controller){
		AutoCompleteDecorator.decorate(departmentField, controller.getCourseDatabase().getDepartments(), false);
	}
}
