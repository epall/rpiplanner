package rpiplanner.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTree;

import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.validation.ValidationResult;

public class DegreeProgressPanel extends JPanel {

	private JProgressBar progressBar;
	private JTree tree;
	private PlanOfStudy plan;
	private Degree degree;
	/**
	 * Create the panel
	 */
	public DegreeProgressPanel() {
		super();
		setLayout(new BorderLayout());

		progressBar = new JProgressBar();
		add(progressBar, BorderLayout.NORTH);

		tree = new JTree();
		add(tree, BorderLayout.CENTER);
		//
	}

	public DegreeProgressPanel(PlanOfStudy plan, Degree degree){
		this();
		this.plan = plan;
		this.degree = degree;
		validatePlan();
	}
	
	private void validatePlan(){
		ValidationResult validation = degree.getValidator().validate(plan);
		progressBar.setValue(validation.percentComplete());
	}
}
