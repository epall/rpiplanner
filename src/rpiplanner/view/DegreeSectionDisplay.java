package rpiplanner.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import rpiplanner.validation.ValidationResult.Section;

public class DegreeSectionDisplay extends JPanel {

	private JLabel statusLabel;
	private JLabel nameLabel;
	/**
	 * Create the panel
	 */
	public DegreeSectionDisplay() {
		super();
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				ColumnSpec.decode("center:default:grow(1.0)")},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default")}));

		nameLabel = new JLabel();
		nameLabel.setText("Section");
		add(nameLabel, new CellConstraints("1, 1, 1, 1"));

		statusLabel = new JLabel();
		statusLabel.setText("New JLabel");
		add(statusLabel, new CellConstraints(1, 3));
		//
	}

	public void setValidationResult(Section result){
		if(result.missingCourses().length > 0){
			statusLabel.setText("Missing "+result.missingCourses()[0]);
		}
		else{
			statusLabel.setText("Good!");
		}
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		nameLabel.setText(name);
	}
}
