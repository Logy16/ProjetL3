package client;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import global.Agents;
import global.Groupe;
import global.Utilisateur;

public class InterfaceServeur extends JFrame {

	private static final long serialVersionUID = -2410156028365273198L;

	public InterfaceServeur() {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		
		Groupe groupeTest1 = new Groupe("TDA1");
		Utilisateur test = new Agents("TEST1", "testeur1", "test3", "mdptest");
		Utilisateur test2 = new Agents("TEST2", "testeu2r", "test3", "mdptest");
		Utilisateur test3 = new Agents("TEST3", "testeu3r", "test3", "mdptest");
		Utilisateur test4 = new Agents("TEST4", "testeu4r", "test3", "mdptest");
		Utilisateur test5 = new Agents("TEST5", "testeu5r", "test3", "mdptest");
		groupeTest1.addUtilisateurs(test, test2, test3, test4, test5);
		GroupTableau modele = new GroupTableau(groupeTest1);
		
		JTable listeEG = new JTable(modele);
		TableColumnModel modeleColonne = listeEG.getColumnModel();
		listeEG.setRowHeight(50);
		for (int i =0; i< groupeTest1.getNbUtilisateurs(); i++) {
			modeleColonne.getColumn(i).setPreferredWidth(100);
			modeleColonne.getColumn(i).setCellRenderer(new RenduGroupeTableau());
		}
		
		// Paramétrages des composants
		contentPane.setLayout(new FlowLayout());
		contentPane.add(listeEG);
		
		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/serveur.jpg")).getImage());
		this.pack();
	}
}
