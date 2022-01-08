package client;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenduGroupeTableau extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 2686297768828832513L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		setText(String.valueOf(value));
		setFont(new Font("Arial", Font.BOLD, 12));
		setHorizontalAlignment(JLabel.CENTER);
		return this;
	}

}
