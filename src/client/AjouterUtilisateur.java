package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

public class AjouterUtilisateur extends JDialog implements ActionListener{

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
	private JLabel labelChoix = new JLabel("Choix des groupes");
	private List<JCheckBox> choixgroupes = new ArrayList<>();
	private JButton buttValider = new JButton("Valider");

	public AjouterUtilisateur(InterfaceServeur parent) {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.parent = parent;

		choixType.addItem("Utilisateur Campus");
		choixType.addItem("Agents");

		// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel panelConnexion = new JPanel();
		JPanel listeGroupeCheckBox = new JPanel();
		JPanel panelValidation = new JPanel();
		List<Groupe> listeGroupe = new ArrayList<>();
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
		contentPane.add(panelConnexion, BorderLayout.CENTER);
		contentPane.add(panelValidation, BorderLayout.SOUTH);

		panelConnexion.setLayout(new GridLayout(6, 2, 10, 10));
		panelConnexion.add(labelNom, 0);
		panelConnexion.add(saisieNom, 1);
		panelConnexion.add(labelPrenom, 2);
		panelConnexion.add(saisiePrenom, 3);
		panelConnexion.add(labelUsername, 4);
		panelConnexion.add(saisieUsername, 5);
		panelConnexion.add(labelMdp, 6);
		panelConnexion.add(saisieMdp, 7);
		panelConnexion.add(labelChoixType, 8);
		panelConnexion.add(choixType, 9);
		panelConnexion.add(labelChoix, 10);
		panelConnexion.add(listeGroupeCheckBox, 11);
		error.setForeground(Color.RED);

		saisieNom.setSize(10, 10);

		listeGroupeCheckBox.setLayout(new GridLayout(listeGroupe.size(), 2, 10, 10));
		for(Groupe grp : listeGroupe) {
			choixgroupes.add(new JCheckBox(grp.getNom()));
		}
		for(int i=0; i<choixgroupes.size(); i++) {
			listeGroupeCheckBox.add(choixgroupes.get(i), i);
		}

		panelValidation.setLayout(new BorderLayout(0, 20));
		panelValidation.add(buttValider, BorderLayout.CENTER);
		panelValidation.add(error, BorderLayout.NORTH);

		buttValider.addActionListener(this);

		this.setContentPane(contentPane);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==buttValider) {
			//if(!(saisieNom.getText().isEmpty() || saisiePrenom.getText().isEmpty() || saisieUsername.getText().isEmpty() || saisieMdp.getText().isEmpty())) {

			//}
			List<Utilisateur> listUsers = this.parent.getListUser();
			ListUtilisateurTableau modeletableUtilisateur = this.parent.getModeleTableUtilsateur();
			Utilisateur newUser = null;
			if(choixType.getSelectedItem().toString().equals("Agents")) {
				newUser = new Agents(saisieNom.getText(), saisiePrenom.getText(), 
						saisieUsername.getText(), saisieMdp.getText(), new Groupe("test"));
			}else {
				newUser = new UtilisateurCampus(saisieNom.getText(), saisiePrenom.getText(), 
						saisieUsername.getText(), saisieMdp.getText(), new Groupe("test"));
			}
			listUsers.add(newUser);
			modeletableUtilisateur.addRow(newUser.getNom());
			this.dispose();
		}
	}

}
