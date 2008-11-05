package rpiplanner;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import rpiplanner.model.PlanOfStudy;
import rpiplanner.view.PlanOfStudyEditor;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainFrame extends JFrame {

	private JComboBox comboBox;
	private JPanel introCard;
	private PlanOfStudyEditor planCard;
	private JTextField studentIDfield;
	private JTextField schoolField;
	private JTextField nameField;
	private POSController controller;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1829687916731988669L;

	/**
	 * Create the frame
	 */
	public MainFrame() {
		super();
		getContentPane().setLayout(new CardLayout());
		
		setBounds(200, 100, 900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		introCard = new JPanel();
		introCard.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("pref:grow(1.0)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow(1.0)")},
			new RowSpec[] {
				RowSpec.decode("top:default"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default"),
				RowSpec.decode("fill:default:grow(1.0)"),
				RowSpec.decode("bottom:default")}));
		introCard.setName("introCard");
		getContentPane().add(introCard, introCard.getName());
		
		planCard = new PlanOfStudyEditor();
		planCard.setName("plan");
		getContentPane().add(planCard, planCard.getName());

		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				controller.initializeTerms((Integer)comboBox.getSelectedItem());
				CardLayout l = (CardLayout)getContentPane().getLayout();
				l.next(getContentPane());
			}
		});
		button.setText("Continue");
		introCard.add(button, new CellConstraints("1, 8, 4, 1, fill, fill"));

		final JLabel welcomeToRpiLabel = new JLabel();
		welcomeToRpiLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeToRpiLabel.setText("Welcome to RPI Planner!");
		introCard.add(welcomeToRpiLabel, new CellConstraints("1, 1, 4, 1, fill, fill"));

		final JLabel pleaseEnterYourLabel = new JLabel();
		pleaseEnterYourLabel.setText("Please enter your information");
		introCard.add(pleaseEnterYourLabel, new CellConstraints(1, 2, 4, 1));
		
		final JLabel nameLabel = new JLabel();
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		nameLabel.setText("Name:");
		introCard.add(nameLabel, new CellConstraints(2, 3));

		final JLabel label_1 = new JLabel();
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setText("School:");
		introCard.add(label_1, new CellConstraints(2, 4));

		final JLabel studentIdLabel = new JLabel();
		studentIdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		studentIdLabel.setText("Student ID:");
		introCard.add(studentIdLabel, new CellConstraints(2, 5));

		nameField = new JTextField();
		introCard.add(nameField, new CellConstraints(4, 3));

		schoolField = new JTextField();
		schoolField.setText("Rensselaer Polytechnic Institute");
		introCard.add(schoolField, new CellConstraints(4, 4));

		studentIDfield = new JTextField();
		introCard.add(studentIDfield, new CellConstraints(4, 5));

		final JLabel startingYearLabel = new JLabel();
		startingYearLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		startingYearLabel.setText("Starting Year:");
		introCard.add(startingYearLabel, new CellConstraints(2, 6));

		Integer[] years = {2006, 2007, 2008, 2009, 2010, 2011};
		comboBox = new JComboBox(years);
		introCard.add(comboBox, new CellConstraints(4, 6));
		//
	}
	public PlanOfStudyEditor getPlanCard() {
		return planCard;
	}
	public JPanel getIntroPanel() {
		return introCard;
	}

	public void setController(POSController controller) {
		this.controller = controller;
		setupBindings();
	}

	private void setupBindings() {
		PlanOfStudy plan = controller.getPlan();
		BeanProperty<JTextField, String> text = BeanProperty.create("text");
		BeanProperty<JComboBox, String> selectedItem = BeanProperty.create("selectedItem");

		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, plan, BeanProperty.create("fullname"), nameField, text).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, plan, BeanProperty.create("school"), schoolField, text).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, plan, BeanProperty.create("studentID"), studentIDfield, text).bind();
		Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, plan, BeanProperty.create("startingYear"), comboBox, selectedItem).bind();
	}
	public POSController getController() {
		return controller;
	}

}
