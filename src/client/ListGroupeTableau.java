package client;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import global.Groupe;

public class ListGroupeTableau extends AbstractTableModel{

	private static final long serialVersionUID = -559826568522533082L;
	
	private List<Groupe> listGroupe;

	public ListGroupeTableau(List<Groupe> listGroupe) {
		this.listGroupe = listGroupe;
	}

	@Override
	public int getRowCount() {
		return this.listGroupe.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Groupe grp = this.listGroupe.get(rowIndex);
		switch(columnIndex) {
			case 0: return grp.getNom();
			default : return null;
		}
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex) {
			case 0: return "Nom";
			default: return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	 public void addRow(String value){
        fireTableRowsInserted(listGroupe.size() - 1, listGroupe.size() - 1);
        int row = listGroupe.size() -1 ;
        int col = 1;
        setValueAt(value, row, col);            
	 }
	
	
}
