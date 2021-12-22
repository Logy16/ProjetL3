package client;

import javax.swing.table.AbstractTableModel;

import global.Groupe;
import global.Utilisateur;

public class GroupTableau extends AbstractTableModel {

	private static final long serialVersionUID = -5435173877337469288L;
	
	Groupe groupe;
	
	public GroupTableau(Groupe groupe) {
		this.groupe = groupe;
	}

	@Override
	public int getRowCount() {
		return this.groupe.getNbUtilisateurs();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Utilisateur user = this.groupe.getUtilisateurs()[rowIndex];
		switch(columnIndex) {
			case 0: return user.getNom();
			case 1: return user.getPrenom();
			case 2: return user.getClass();
			case 3: return this.groupe.getNom();
			case 4 : return "img";
			default : return null;
		}
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex) {
			case 0: return "Nom";
			case 1: return "Prénom";
			case 2: return "Type";
			case 3: return "Groupe";
			case 4 : return "Options";
			default : return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
