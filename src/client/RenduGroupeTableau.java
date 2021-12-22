package client;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenduGroupeTableau extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 2686297768828832513L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		setText(String.valueOf(value));
		setHorizontalAlignment(JLabel.CENTER);
		return this;
	}

}
