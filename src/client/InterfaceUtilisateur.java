package client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import global.Agents;
import global.Fil;
import global.Groupe;
import global.Utilisateur;

public class InterfaceUtilisateur extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5093493472239406706L;
	private JLabel nomTicket = new JLabel("DU BLABLA DE TEST");
	private JLabel txtSaisie = new JLabel("Envoyer un message dans : ");
	private List<Fil> listeFil = new ArrayList<>();

	public InterfaceUtilisateur() {
		super();
		this.setTitle("Interface Utilisateur");
		// this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel gauche = new JPanel();
		JPanel bouton = new JPanel();
		
		Groupe groupeTest1 = new Groupe("TDA1");
		Groupe groupeTest2 = new Groupe("TDA2");
		Groupe groupeTest3 = new Groupe("TDA3");
		Groupe groupeTest4 = new Groupe("TDA4");
		Utilisateur test = new Agents("TEST", "testeur", "test3", "mdptest");
		listeFil.add(new Fil("Sujet jeux vidéo", groupeTest1, test));
		listeFil.add(new Fil("Sujet 1", groupeTest1, test));
		listeFil.add(new Fil("Sujet 2", groupeTest2, test));
		listeFil.add(new Fil("Sujet jeux vidéo", groupeTest3, test));
		listeFil.add(new Fil("Sujet cours POOMO", groupeTest4, test));
		
		JTree listeTickets;
		DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Racine");
		for(int i = 0; i<listeFil.size(); i++) {
			DefaultMutableTreeNode noeud = new DefaultMutableTreeNode(listeFil.get(i).getGroupe().getNom());
			racine.add(noeud);
			for(int j = 0; j<listeFil.size(); j++) {
				DefaultMutableTreeNode feuille = new DefaultMutableTreeNode(listeFil.get(j).getSujet());
				noeud.add(feuille);
			}		
		}
		
		
		listeTickets = new JTree(racine);
		JPanel droite = new JPanel();
		JPanel subjectTicket = new JPanel();
		JPanel affichageMess = new JPanel();
		JPanel saisieTxt = new JPanel();
		JTextArea zoneSaisie = new JTextArea(2, 100);

		// Paramétrages des composants
		contentPane.setLayout(new BorderLayout());
		contentPane.add(gauche, BorderLayout.WEST);
		contentPane.add(droite, BorderLayout.CENTER);

		gauche.setLayout(new BorderLayout());
		gauche.add(bouton, BorderLayout.NORTH);
		gauche.add(listeTickets, BorderLayout.CENTER);

		bouton.setLayout(new FlowLayout());
		listeTickets.setLayout(new GridLayout(10, 1, 0, 5));
		listeTickets.setRootVisible(false);

		Button testButton = new Button("On test un boutton");
		bouton.add(testButton);

		droite.setLayout(new BorderLayout());
		droite.add(subjectTicket, BorderLayout.NORTH);
		droite.add(affichageMess, BorderLayout.CENTER);
		droite.add(saisieTxt, BorderLayout.SOUTH);

		subjectTicket.setLayout(new FlowLayout());
		affichageMess.setLayout(new GridLayout());
		saisieTxt.setLayout(new FlowLayout());

		subjectTicket.add(nomTicket);
		saisieTxt.add(txtSaisie);
		saisieTxt.add(zoneSaisie);
		
		//Taille du texte
		listeTickets.setFont(new Font("Arial",0,15));
		bouton.setFont(new Font("Arial",0,15));

		//Ajout de bordures
		contentPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5,5,5,5),
				BorderFactory.createLoweredSoftBevelBorder()));
		gauche.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		droite.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		contentPane.setBackground(Color.LIGHT_GRAY);
		/*bouton.setBackground(Color.RED);
		listeTickets.setBackground(Color.BLUE);
		subjectTicket.setBackground(Color.GREEN);
		affichageMess.setBackground(Color.YELLOW);
		saisieTxt.setBackground(Color.PINK);*/

		// Gestion des évènements

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quitter();
			}
		});

		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/discussion.png")).getImage());
		this.pack();

	}

	/**
	 * Permet de quitter l'application si l'utilisateur répond oui
	 */
	public void quitter() {
		int reponse = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir quitter?", "Quitter l'application?",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (reponse == JOptionPane.YES_OPTION) {
			System.exit(0);
			/*
			 * }else if(reponse == JOptionPane.NO_OPTION) { int reponseMenu =
			 * JOptionPane.showConfirmDialog(this,
			 * "Voulez-vous revenir au menu principal?","Continuer?",JOptionPane.
			 * YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); if (reponseMenu ==
			 * JOptionPane.YES_OPTION) { MenuBatailleNavale MBN = new MenuBatailleNavale();
			 * MBN.setVisible(true); this.dispose(); }
			 */
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * if(e.getSource() == 2) { this.nomTicket.setText(); }
		 */

	}

}
