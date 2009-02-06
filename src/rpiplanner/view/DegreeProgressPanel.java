package rpiplanner.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.Box;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import org.jruby.exceptions.RaiseException;

import rpiplanner.POSController;
import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.validation.ValidationResult;

public class DegreeProgressPanel extends JPanel {

	private SpringLayout springLayout;
	private JComboBox degreeComboBox;
	private JPanel sectionContainer;
	private JProgressBar progressBar;
	private PlanOfStudy plan;
	private Degree degree;
	private HashMap<String, DegreeSectionDisplay> sections = new HashMap<String, DegreeSectionDisplay>();
	
	/**
	 * Create the panel
	 */
	public DegreeProgressPanel() {
		super();
		setLayout(new BorderLayout());

		degreeComboBox = new JComboBox();
		add(degreeComboBox, BorderLayout.SOUTH);

		progressBar = new JProgressBar();
		add(progressBar, BorderLayout.NORTH);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		sectionContainer = new JPanel();
		springLayout = new SpringLayout();
		sectionContainer.setLayout(springLayout);
		scrollPane.setViewportView(sectionContainer);
		//
	}

	public DegreeProgressPanel(PlanOfStudy plan, Degree degree){
		this();
		initialize(plan, degree);
	}
	
	private void validatePlan(){
		if(degree == null)
			return;
		try{
			ValidationResult validation = degree.getValidator().validate(plan);
			progressBar.setValue(validation.percentComplete());
			for (String sectionName : degree.getSectionNames()) {
				DegreeSectionDisplay dsd = sections.get(sectionName);
				dsd.setValidationResult(validation
						.getSectionResults(sectionName));
			}
		} catch (RaiseException e) {
			System.err.println(e.getStackTrace()[0].toString());
			System.err.println(e.getException().inspect().toString());
		}
	}
	
	private void initialize(PlanOfStudy plan, Degree degree){
		this.plan = plan;
		plan.addPropertyChangeListener("courses", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				validatePlan();
			}
		});
		
		this.degree = degree;
		
		rebuildSections();
	}
	
	private void rebuildSections(){
		sections.clear();
		sectionContainer.removeAll();
		if (degree != null) {
			DegreeSectionDisplay prev = null;
			for (String s : degree.getSectionNames()) {
				DegreeSectionDisplay dsd = new DegreeSectionDisplay();
				dsd.setName(s);
				sections.put(s, dsd);
				sectionContainer.add(dsd);
				springLayout.putConstraint(SpringLayout.EAST, dsd, -2, SpringLayout.EAST, sectionContainer);
				springLayout.putConstraint(SpringLayout.WEST, dsd, 2, SpringLayout.WEST, sectionContainer);
				if(prev == null)
					springLayout.putConstraint(SpringLayout.NORTH, dsd, 2, SpringLayout.NORTH, sectionContainer);
				else
					springLayout.putConstraint(SpringLayout.NORTH, dsd, 2, SpringLayout.SOUTH, prev);
				prev = dsd;
			}
			sectionContainer.revalidate();
		}
	}

	public void initialize(POSController controller) {
		if (controller.getPlan().getDegrees().size() == 0)
			initialize(controller.getPlan(), null);
		else
			initialize(controller.getPlan(), controller.getPlan().getDegrees()
					.get(0));
		
		degreeComboBox.setModel(controller.getDegreeListModel());
		degreeComboBox.setSelectedItem(this.degree);
		degreeComboBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					degree = (Degree)e.getItem();
					plan.getDegrees().clear();
					plan.getDegrees().add(degree);
					rebuildSections();
					validatePlan();
				} else { // deselected
					plan.getDegrees().remove(e.getItem());
				}
			}
		});
		
		validatePlan();
	}
}
