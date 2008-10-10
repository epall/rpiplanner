package rpiplanner.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import rpiplanner.Main;
import rpiplanner.POSController;
import rpiplanner.SchoolInformation;

public class PlanOfStudy extends JPanel {
	private JList courseList;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5679127864387757734L;
	private JTextField searchField;
	private ArrayList<JPanel> semesterPanels = new ArrayList<JPanel>(8);

	public PlanOfStudy() {
		super();
		setLayout(new BorderLayout());

		final JLabel realtimeStatusHereLabel = new JLabel();
		add(realtimeStatusHereLabel, BorderLayout.SOUTH);
		realtimeStatusHereLabel.setText("Real-time status here");

		final JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.NORTH);

		final JLabel rpiPlannerLabel = new JLabel();
		rpiPlannerLabel.setText("RPI Planner");
		titlePanel.add(rpiPlannerLabel);

		final JPanel planPanel = new JPanel();
		add(planPanel);

		for(int i = 0; i < SchoolInformation.DEFAULT_NUM_SEMESTERS; i++){
			final JPanel semesterPanel = new JPanel();
			semesterPanel.setBorder(new CompoundBorder(new EmptyBorder(5,5,5,5), new EtchedBorder()));
			semesterPanel.setLayout(new BoxLayout(semesterPanel, BoxLayout.Y_AXIS));
			planPanel.add(semesterPanel);
			semesterPanels.add(semesterPanel);
		}

		final JLabel statusAndOtherLabel = new JLabel();
		add(statusAndOtherLabel, BorderLayout.EAST);
		statusAndOtherLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusAndOtherLabel.setText("Status and other information");

		final JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC},
			new RowSpec[] {
				FormFactory.MIN_ROWSPEC,
				RowSpec.decode("34px:grow(1.0)"),
				FormFactory.DEFAULT_ROWSPEC}));
		searchPanel.setBorder(new TitledBorder(null, "Find Course", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		add(searchPanel, BorderLayout.WEST);

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(150, 28));
		searchPanel.add(searchField, new CellConstraints("1, 1, 1, 1, fill, fill"));

		courseList = new JList();
		searchPanel.add(courseList, new CellConstraints("1, 2, 1, 1, center, fill"));

		final JButton addCourseButton = new JButton();
		addCourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				NewCourseDialog ncd = new NewCourseDialog();
				ncd.setVisible(true);
			}
		});
		addCourseButton.setText("Add Course");
		searchPanel.add(addCourseButton, new CellConstraints(1, 3));
		//
	}
	
	public void setController(POSController controller){
		controller.setSemesterPanels(semesterPanels);
	}
}
