package rpiplanner.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import rpiplanner.model.Degree;
import rpiplanner.model.ValidationError;

public class DegreeListCell extends JLabel implements ListCellRenderer {
	public DegreeListCell() {
		// Don't paint behind the component
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Degree degree = (Degree) value;
		ValidationError.Type status = degree.validationStatus();
		 if (status == ValidationError.Type.ERROR)
			setBackground(Color.red);
		else if(status == ValidationError.Type.WARNING)
			setBackground(Color.yellow);
		else
			setBackground(Color.white);
		setText(degree.toString());

		return this;
	}

}
