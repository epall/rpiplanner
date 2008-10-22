package rpiplanner.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import rpiplanner.POSController;
import rpiplanner.SchoolInformation;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PlanOfStudyEditor extends JPanel {
	private JList courseList;
	private JTextField searchField;
	private ArrayList<JPanel> semesterPanels = new ArrayList<JPanel>(8);
	
	public PlanOfStudyEditor(POSController controller){
		super();
	}

	public PlanOfStudyEditor() {
		super();
		setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("200px:grow(1.0)"),
				ColumnSpec.decode("300px:grow(3.0)"),
				ColumnSpec.decode("181px:grow(2.0)")},
			new RowSpec[] {
				FormFactory.MIN_ROWSPEC,
				RowSpec.decode("350px:grow(1.0)")}));

		final JPanel titlePanel = new JPanel();
		add(titlePanel, new CellConstraints("1, 1, 3, 1, fill, fill"));

		final JLabel rpiPlannerLabel = new JLabel();
		rpiPlannerLabel.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		rpiPlannerLabel.setText("RPI Planner");
		titlePanel.add(rpiPlannerLabel);

		final JPanel planPanel = new JPanel();
		planPanel.setLayout(new GridLayout(4, 2));
		add(planPanel, new CellConstraints("2, 2, 1, 1, fill, top"));

		for(int i = 0; i < SchoolInformation.DEFAULT_NUM_SEMESTERS; i++){
			final JPanel semesterPanel = new JPanel();
			semesterPanel.setBorder(new CompoundBorder(new EmptyBorder(5,5,5,5), new EtchedBorder()));
			semesterPanel.setLayout(new BoxLayout(semesterPanel, BoxLayout.Y_AXIS));
			planPanel.add(semesterPanel);
			semesterPanels.add(semesterPanel);
		}

		final JLabel statusAndOtherLabel = new JLabel();
		add(statusAndOtherLabel, new CellConstraints("3, 2, 1, 1, fill, fill"));
		statusAndOtherLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusAndOtherLabel.setText("Status and other information");

		final JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("default:grow(1.0)")},
			new RowSpec[] {
				FormFactory.MIN_ROWSPEC,
				RowSpec.decode("34px:grow(1.0)"),
				FormFactory.DEFAULT_ROWSPEC}));
		searchPanel.setBorder(new TitledBorder(null, "Find Course", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		add(searchPanel, new CellConstraints("1, 2, 1, 1, fill, fill"));

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(150, 28));
		searchPanel.add(searchField, new CellConstraints("1, 1, 1, 1, fill, fill"));

		courseList = new JList();
		courseList.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		courseList.setBackground(Color.WHITE);
		searchPanel.add(courseList, new CellConstraints("1, 2, 1, 1, fill, fill"));

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
		courseList.setModel(controller.getCourseListModel());
		courseList.setTransferHandler(new CourseTransferHandler(controller));
		courseList.setDragEnabled(true);
		courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}
