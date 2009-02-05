package rpiplanner.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXCollapsiblePane.Direction;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import rpiplanner.validation.ValidationResult.Section;

public class DegreeSectionDisplay extends JPanel {

	private JLabel statusLabel;
	private JLabel nameLabel;
	private JXCollapsiblePane detailsPane;
	/**
	 * Create the panel
	 */
	public DegreeSectionDisplay() {
		super();
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default:grow(1.0)")},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow(1.0)")}));

		nameLabel = new JLabel();
		nameLabel.setText("Section");
		add(nameLabel, new CellConstraints("3, 1, 1, 1"));

		final JToggleButton detailsToggle = new JToggleButton();
		detailsToggle.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				detailsPane.setCollapsed(!detailsToggle.getModel().isSelected());
			}
		});
		detailsToggle.setText("Details");
		add(detailsToggle, new CellConstraints());
		
		detailsPane = new JXCollapsiblePane(Direction.DOWN);
		add(detailsPane, new CellConstraints("1, 3, 3, 1"));
		detailsPane.setCollapsed(true);

		statusLabel = new JLabel();
		statusLabel.setText("Status");
		detailsPane.add(statusLabel, BorderLayout.NORTH);
		//
	}

	public void setValidationResult(Section result){
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		nameLabel.setText(name);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
}
