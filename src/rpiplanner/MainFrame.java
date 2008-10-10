package rpiplanner;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import rpiplanner.view.PlanOfStudy;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainFrame extends JFrame {

	private JPanel introCard;
	private PlanOfStudy planCard;
	private JTextField textField_2;
	private JTextField rensselaerPolytechnicInstituteTextField;
	private JTextField textField;
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
		
		setBounds(200, 100, 800, 600);
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
				RowSpec.decode("fill:default:grow(1.0)"),
				RowSpec.decode("bottom:default")}));
		introCard.setName("introCard");
		getContentPane().add(introCard, introCard.getName());
		
		planCard = new PlanOfStudy();
		planCard.setName("plan");
		getContentPane().add(planCard, planCard.getName());

		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				controller.updateUserInfo(textField.getText(), rensselaerPolytechnicInstituteTextField.getText(), textField_2.getText());
				CardLayout l = (CardLayout)getContentPane().getLayout();
				l.next(getContentPane());
			}
		});
		button.setText("Continue");
		introCard.add(button, new CellConstraints("1, 7, 4, 1, fill, fill"));

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

		textField = new JTextField();
		introCard.add(textField, new CellConstraints(4, 3));

		rensselaerPolytechnicInstituteTextField = new JTextField();
		rensselaerPolytechnicInstituteTextField.setText("Rensselaer Polytechnic Institute");
		introCard.add(rensselaerPolytechnicInstituteTextField, new CellConstraints(4, 4));

		textField_2 = new JTextField();
		introCard.add(textField_2, new CellConstraints(4, 5));
		
		final JMenuBar menu = new JMenuBar();
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem fileSave = new JMenuItem("Save");
		fileSave.setEnabled(false);
		fileMenu.add(fileSave);
		menu.add(fileMenu);
		this.setJMenuBar(menu);
		//
	}
	public PlanOfStudy getPlanCard() {
		return planCard;
	}
	public JPanel getIntroPanel() {
		return introCard;
	}

	public void setController(POSController controller) {
		this.controller = controller;
	}

	public POSController getController() {
		return controller;
	}

}
