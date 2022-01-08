package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import global.Agents;
import global.Groupe;
import global.Utilisateur;
import global.UtilisateurCampus;

public class UpdateUtilisateur extends JDialog implements ActionListener{

	private static final long serialVersionUID = 3720849060290140719L;

	private InterfaceServeur parent;

	private JLabel labelNom = new JLabel("Nom");
	private JLabel labelPrenom = new JLabel("Prénom");
	private JLabel labelUsername = new JLabel("Identifiant");
	private JLabel labelMdp = new JLabel("Mot de passe");
	private JLabel error = new JLabel("");
	private JTextField saisieNom = new JTextField();
	private JTextField saisiePrenom = new JTextField();
	private JTextField saisieUsername = new JTextField();
	private JTextField saisieMdp = new JTextField();
	private JLabel labelChoixType = new JLabel("Type d'utilisateur");
	private JComboBox<String> choixType = new JComboBox<>();
	private JLabel labelChoix = new JLabel("<html>Choix des groupes<br/></html>");
	private List<JCheckBox> choixgroupes = new ArrayList<>();
	private JButton buttValiderAjout = new JButton("Valider");
	private JButton buttValiderModif = new JButton("Valider");
	
	private List<Groupe> listeGroupe = new ArrayList<>();
	
	public UpdateUtilisateur(InterfaceServeur parent) {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.parent = parent;

		choixType.addItem("Utilisateur Campus");
		choixType.addItem("Agents");

		// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel panelSaisieDonnees = new JPanel();
		JPanel panelGroupeCheckBox = new JPanel();
		JPanel panelValidation = new JPanel();
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
		listeGroupe.add(grp1);
		listeGroupe.add(grp2);
		listeGroupe.add(grp1);
		listeGroupe.add(grp2);

		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelSaisieDonnees, BorderLayout.CENTER);
		contentPane.add(panelValidation, BorderLayout.SOUTH);

		panelSaisieDonnees.setLayout(new GridLayout(6, 2, 10, 10));
		panelSaisieDonnees.add(labelNom, 0);
		panelSaisieDonnees.add(saisieNom, 1);
		panelSaisieDonnees.add(labelPrenom, 2);
		panelSaisieDonnees.add(saisiePrenom, 3);
		panelSaisieDonnees.add(labelUsername, 4);
		panelSaisieDonnees.add(saisieUsername, 5);
		panelSaisieDonnees.add(labelMdp, 6);
		panelSaisieDonnees.add(saisieMdp, 7);
		panelSaisieDonnees.add(labelChoixType, 8);
		panelSaisieDonnees.add(choixType, 9);
		panelSaisieDonnees.add(labelChoix, 10);
		
		error.setForeground(Color.RED);
		saisieNom.setPreferredSize(new Dimension(10,10));

		panelGroupeCheckBox.setLayout(new GridLayout(listeGroupe.size(), 2, 10, 10));
		for(Groupe grp : listeGroupe) {
			choixgroupes.add(new JCheckBox(grp.getNom()));
		}
		for(int i=0; i<choixgroupes.size(); i++) {
			panelGroupeCheckBox.add(choixgroupes.get(i), i);
		}

		panelValidation.setLayout(new BorderLayout(0, 20));
		panelValidation.add(buttValiderAjout, BorderLayout.SOUTH);
		panelValidation.add(error, BorderLayout.CENTER);
		panelValidation.add(panelGroupeCheckBox, BorderLayout.NORTH);

		buttValiderAjout.addActionListener(this);

		this.setContentPane(contentPane);
		this.pack();
	}

	public UpdateUtilisateur(InterfaceServeur parent, String nom, String prenom) {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.parent = parent;

		choixType.addItem("Utilisateur Campus");
		choixType.addItem("Agents");

		// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel panelSaisieDonnees = new JPanel();
		JPanel panelGroupeCheckBox = new JPanel();
		JPanel panelValidation = new JPanel();
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
		listeGroupe.add(grp1);
		listeGroupe.add(grp2);
		listeGroupe.add(grp1);
		listeGroupe.add(grp2);

		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelSaisieDonnees, BorderLayout.CENTER);
		contentPane.add(panelValidation, BorderLayout.SOUTH);

		panelSaisieDonnees.setLayout(new GridLayout(6, 2, 10, 10));
		panelSaisieDonnees.add(labelNom, 0);
		panelSaisieDonnees.add(saisieNom, 1);
		panelSaisieDonnees.add(labelPrenom, 2);
		panelSaisieDonnees.add(saisiePrenom, 3);
		panelSaisieDonnees.add(labelUsername, 4);
		panelSaisieDonnees.add(saisieUsername, 5);
		panelSaisieDonnees.add(labelMdp, 6);
		panelSaisieDonnees.add(saisieMdp, 7);
		panelSaisieDonnees.add(labelChoixType, 8);
		panelSaisieDonnees.add(choixType, 9);
		panelSaisieDonnees.add(labelChoix, 10);
		
		error.setForeground(Color.RED);
		saisieNom.setPreferredSize(new Dimension(10,10));

		panelGroupeCheckBox.setLayout(new GridLayout(listeGroupe.size(), 2, 10, 10));
		for(Groupe grp : listeGroupe) {
			choixgroupes.add(new JCheckBox(grp.getNom()));
		}
		for(int i=0; i<choixgroupes.size(); i++) {
			panelGroupeCheckBox.add(choixgroupes.get(i), i);
		}

		panelValidation.setLayout(new BorderLayout(0, 20));
		panelValidation.add(buttValiderAjout, BorderLayout.SOUTH);
		panelValidation.add(error, BorderLayout.CENTER);
		panelValidation.add(panelGroupeCheckBox, BorderLayout.NORTH);

		buttValiderAjout.addActionListener(this);

		this.setContentPane(contentPane);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==buttValiderAjout) {
			//if(!(saisieNom.getText().isEmpty() || saisiePrenom.getText().isEmpty() || saisieUsername.getText().isEmpty() || saisieMdp.getText().isEmpty())) {

			//}
			List<Groupe> groupesChoisis = new ArrayList<>();
			int cpt = 0;
			for(int i = 0; i<listeGroupe.size(); i++) {
				if(choixgroupes.get(i).isSelected()) {
					groupesChoisis.add(listeGroupe.get(i));
					cpt++;
				}
			}

			List<Utilisateur> listUsers = this.parent.getListUser();
			ListUtilisateurTableau modeletableUtilisateur = this.parent.getModeleTableUtilsateur();
			Utilisateur newUser = null;
			if(choixType.getSelectedItem().toString().equals("Agents")) {
				newUser = new Agents(saisieNom.getText(), saisiePrenom.getText(), 
						saisieUsername.getText(), saisieMdp.getText(), new Groupe("Bloquer"));
			}else {
				newUser = new UtilisateurCampus(saisieNom.getText(), saisiePrenom.getText(), 
						saisieUsername.getText(), saisieMdp.getText(), new Groupe("Bloquer"));
			}
			listUsers.add(newUser);
			modeletableUtilisateur.addRow(newUser.getNom());
			this.dispose();
		}
		
		if(e.getSource()==buttValiderModif) {
			//if(!(saisieNom.getText().isEmpty() || saisiePrenom.getText().isEmpty())) {

			//}
			List<Utilisateur> listUsers = this.parent.getListUser();
			ListUtilisateurTableau modeletableUtilisateur = this.parent.getModeleTableUtilsateur();
			JTable tableUtilisateur = this.parent.getTableUtilsateur();
			String nameUserToModify = (String) modeletableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 0);
			Utilisateur uti = null;
			for(Iterator<Utilisateur> ite = listUsers.iterator(); ite.hasNext();) {
				uti = ite.next();
				if(uti.getNom().equals(nameUserToModify)) {
					break;
				}
			}
			uti.setNom(saisieNom.getText());
			uti.setPrenom(saisiePrenom.getText());
			modeletableUtilisateur.fireTableRowsUpdated(tableUtilisateur.getSelectedRow(), tableUtilisateur.getSelectedRow());
			this.dispose();
		}
	}

}
