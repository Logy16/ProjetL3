package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import global.Agents;
import global.Groupe;
import global.Utilisateur;
import global.UtilisateurCampus;

public class InterfaceServeur extends JFrame {

	private static final long serialVersionUID = -2410156028365273198L;

	public InterfaceServeur() {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel bas = new JPanel();
		
		//Récupérer la liste des utilisateurs
		List<Utilisateur> listUtilisateur = new ArrayList<>();
		//Test Local a supprimer
			Groupe grp1 = new Groupe("TDDDD");
			Groupe grp2 = new Groupe("TTTTTD");
			Utilisateur test = new Agents("TEST1", "testeur1", "test3", "mdptest");
			Utilisateur test2 = new Agents("TEST2", "testeu2r", "test3", "mdptest");
			Utilisateur test3 = new UtilisateurCampus("TEST3", "testeu3r", "test2", "mdptest");
			Utilisateur test4 = new Agents("TEST4", "testeu4r", "test3", "mdptest");
			Utilisateur test5 = new Agents("TEST5", "testeu5r", "test3", "mdptest");
			grp1.addUtilisateurs(test2,test4);
			grp2.addUtilisateurs(test,test4);
			listUtilisateur.add(test);
			listUtilisateur.add(test2);
			listUtilisateur.add(test3);
			listUtilisateur.add(test4);
			listUtilisateur.add(test5);
		
		ListUtilisateurTableau modele = new ListUtilisateurTableau(listUtilisateur);
		JTable listeEG = new JTable(modele);
		listeEG.setRowHeight(50);
		
		TableColumnModel modeleColonne = listeEG.getColumnModel();
		for (int i=0; i<4; i++) {
			modeleColonne.getColumn(i).setPreferredWidth(150);
			modeleColonne.getColumn(i).setCellRenderer(new RenduGroupeTableau());
		}
		
		// Paramétrages des composants
		contentPane.setLayout(new BorderLayout());
		contentPane.add(listeEG, BorderLayout.CENTER);
		
		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/serveur.jpg")).getImage());
		this.pack();
	}
}
