/*
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package rpiplanner.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import rpiplanner.model.Degree;
import rpiplanner.model.ValidationError;

public class DegreeListCell extends DefaultListCellRenderer {
	public DegreeListCell() {
		// Don't paint behind the component
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof Degree) {
			Degree degree = (Degree) value;
			ValidationError.Type status = degree.validationStatus();
			if (status == ValidationError.Type.ERROR)
				setBackground(Color.red);
			else if (status == ValidationError.Type.WARNING)
				setBackground(Color.yellow);
			else
				setBackground(Color.white);
			setForeground(Color.black);
			setText(degree.toString());
		}
		return this;
	}

}
