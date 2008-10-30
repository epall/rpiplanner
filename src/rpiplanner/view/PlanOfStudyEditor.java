package rpiplanner.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import rpiplanner.POSController;
import rpiplanner.SchoolInformation;
import rpiplanner.model.Course;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PlanOfStudyEditor extends JPanel {
	private JPanel courseDetailsPanel;
	private JTextArea descriptionTextArea;
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
				RowSpec.decode("fill:0dlu:grow(1.0)")}));

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

		final JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("default:grow(1.0)")},
			new RowSpec[] {
				FormFactory.MIN_ROWSPEC,
				RowSpec.decode("fill:0px:grow(1.0)"),
				FormFactory.DEFAULT_ROWSPEC}));
		searchPanel.setBorder(new TitledBorder(null, "Find Course", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		add(searchPanel, new CellConstraints("1, 2, 1, 1, fill, fill"));

		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(150, 28));
		searchPanel.add(searchField, new CellConstraints("1, 1, 1, 1, fill, fill"));

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		searchPanel.add(scrollPane, new CellConstraints("1, 2, 1, 1, fill, fill"));

		courseList = new JList();
		scrollPane.setViewportView(courseList);
		courseList.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		courseList.setBackground(Color.WHITE);

		final JButton addCourseButton = new JButton();
		addCourseButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				NewCourseDialog ncd = new NewCourseDialog();
				ncd.setVisible(true);
			}
		});
		addCourseButton.setText("Add Course");
		searchPanel.add(addCourseButton, new CellConstraints(1, 3));

		courseDetailsPanel = new JPanel();
		courseDetailsPanel.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow(1.0)")},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow(1.0)")}));
		courseDetailsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Course details"));
		add(courseDetailsPanel, new CellConstraints(3, 2));

		final JLabel titleLabel = new JLabel();
		titleLabel.setText("Title:");
		courseDetailsPanel.add(titleLabel, new CellConstraints(1, 3));

		final JLabel departmentLabel = new JLabel();
		departmentLabel.setText("Department:");
		courseDetailsPanel.add(departmentLabel, new CellConstraints());

		final JLabel descriptionLabel = new JLabel();
		descriptionLabel.setText("Description:");
		courseDetailsPanel.add(descriptionLabel, new CellConstraints(1, 7));

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setName("description");
		descriptionTextArea.setEditable(false);
		courseDetailsPanel.add(descriptionTextArea, new CellConstraints(1, 9, 3, 1));

		final JTextField departmentField = new JTextField();
		departmentField.setEditable(false);
		departmentField.setName("department");
		courseDetailsPanel.add(departmentField, new CellConstraints(3, 1));

		final JTextField titleField = new JTextField();
		titleField.setEditable(false);
		titleField.setName("title");
		courseDetailsPanel.add(titleField, new CellConstraints(3, 3));

		final JTextField catalogField = new JTextField();
		catalogField.setEditable(false);
		catalogField.setName("catalogNumber");
		courseDetailsPanel.add(catalogField, new CellConstraints(3, 5));

		final JLabel catalogNumberLabel = new JLabel();
		catalogNumberLabel.setText("Catalog number:");
		courseDetailsPanel.add(catalogNumberLabel, new CellConstraints(1, 5));
		//
	}
	
	public void setController(final POSController controller){
		controller.setSemesterPanels(semesterPanels);
		controller.setCourseDetailsPanel(courseDetailsPanel);
		courseList.setModel(controller.getCourseListModel());
		courseList.setTransferHandler(new CourseTransferHandler(controller));
		courseList.setDragEnabled(true);
		courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		courseList.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(final MouseEvent e) {
				int index = courseList.locationToIndex(e.getPoint());
				if(index != -1){
					Course mouseOver = (Course)courseList.getModel().getElementAt(index);
					controller.setDetailDisplay(mouseOver);
				}
				else{
					controller.setDetailDisplay(null);
				}
			}
		});
		courseList.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseExited(MouseEvent e) {
				controller.setDetailDisplay(null);
			}
		});
		
		searchField.getDocument().addDocumentListener(new DocumentListener(){

			public void changedUpdate(DocumentEvent e) {
				try {
					controller.searchTextChanged(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (BadLocationException e1) {
					// won't ever happen
				}
			}

			public void insertUpdate(DocumentEvent e) {
				try {
					controller.searchTextChanged(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (BadLocationException e1) {
					// won't ever happen
				}
			}

			public void removeUpdate(DocumentEvent e) {
				try {
					controller.searchTextChanged(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (BadLocationException e1) {
					// won't ever happen
				}
			}
		});
	}
}
