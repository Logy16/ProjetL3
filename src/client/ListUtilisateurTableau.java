package client;

import java.awt.FlowLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import global.Utilisateur;

public class ListUtilisateurTableau extends AbstractTableModel {

	private static final long serialVersionUID = -5435173877337469288L;
	
	List<Utilisateur> listUti;
	
	public ListUtilisateurTableau(List<Utilisateur> listUti) {
		this.listUti = listUti;
	}

	@Override
	public int getRowCount() {
		return this.listUti.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Utilisateur user = this.listUti.get(rowIndex);
		switch(columnIndex) {
			case 0: return user.getNom();
			case 1: return user.getPrenom();
			case 2: return user. classToString();
			case 3: return user.listGroupToString();
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
			default: return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
