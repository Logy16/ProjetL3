package client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import global.Agents;
import global.Fil;
import global.Groupe;
import global.Utilisateur;

public class InterfaceUtilisateur extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5093493472239406706L;
	private JPanel affichageMess;
	private JLabel nomTicket = new JLabel("Aucun sujet choisi pour le moment");
	private JLabel txtSaisie = new JLabel("Envoyer un message dans : ");
	private JButton buttonEnvoyer = new JButton("Envoyer");
	private JButton buttnewSubject = new JButton("Nouveau Sujet");
	private JButton buttRefresh = new JButton("Rafraichir");
	private JTextArea zoneSaisie;

	public InterfaceUtilisateur() {
		super();
		this.setTitle("Interface Utilisateur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel gauche = new JPanel();
		JPanel bouton = new JPanel();
		
		/*
		 *************** BD ***************** 
		*/
		//Récupérer les groupes et les fils de discussion
		List<Groupe> listgrp = new ArrayList<>();
		List<Fil> listeFil = new ArrayList<>();
			//Partie de test à supprimer
			Groupe groupeTest1 = new Groupe("TDA1");
			Groupe groupeTest2 = new Groupe("TDA2");
			Groupe groupeTest3 = new Groupe("TDA3");
			Groupe groupeTest4 = new Groupe("TDA4");
			listgrp.add(groupeTest1);
			listgrp.add(groupeTest2);
			listgrp.add(groupeTest3);
			listgrp.add(groupeTest4);
			Utilisateur test = new Agents("TEST", "testeur", "test3", "mdptest");
			listeFil.add(new Fil("Sujet jeux vidéo", groupeTest1, test));
			listeFil.add(new Fil("Sujet 1", groupeTest1, test));
			listeFil.add(new Fil("Sujet 2", groupeTest2, test));
			listeFil.add(new Fil("Sujet jeux vidéo", groupeTest3, test));
			listeFil.add(new Fil("Sujet cours POOMO", groupeTest4, test));
		
		//Création de l'arbre
		JTree listeTickets;
		DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Racine");
		for(int i = 0; i<listgrp.size(); i++) {
			DefaultMutableTreeNode noeud = new DefaultMutableTreeNode(listgrp.get(i).getNom());
			racine.add(noeud);
			for(int j = 0; j<listeFil.size(); j++) {
				if(listeFil.get(j).getGroupe().equals(listgrp.get(i))) {
					DefaultMutableTreeNode feuille = new DefaultMutableTreeNode(listeFil.get(j).getSujet());
					noeud.add(feuille);
				}
			}		
		}
		listeTickets = new JTree(racine);
		listeTickets.setRootVisible(false);
		listeTickets.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		listeTickets.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						 listeTickets.getLastSelectedPathComponent();
		        if(node.getChildCount() == 0) {
		        	Object nodeInfo = node.getUserObject();
			        nomTicket.setText(nodeInfo.toString());		
			        txtSaisie.setText("Envoyer un message dans : <<" + nodeInfo.toString() + ">>");
		        }  		
			}
		});
		nomTicket.setFont(new Font(Font.DIALOG,  Font.BOLD, 15));
	
		
		JPanel droite = new JPanel();
		JPanel subjectTicket = new JPanel();
		JPanel saisieTxt = new JPanel();
		affichageMess = new JPanel();
		zoneSaisie = new JTextArea(2, 100);

		// Paramétrages des composants
		contentPane.setLayout(new BorderLayout());
		contentPane.add(gauche, BorderLayout.WEST);
		contentPane.add(droite, BorderLayout.CENTER);

		gauche.setLayout(new BorderLayout());
		gauche.add(bouton, BorderLayout.NORTH);
		gauche.add(new JScrollPane(listeTickets), BorderLayout.CENTER);

		bouton.setLayout(new BorderLayout());
		bouton.add(buttnewSubject, BorderLayout.NORTH);
		bouton.add(buttRefresh, BorderLayout.SOUTH);

		droite.setLayout(new BorderLayout());
		droite.add(subjectTicket, BorderLayout.NORTH);
		droite.add(affichageMess, BorderLayout.CENTER);
		droite.add(saisieTxt, BorderLayout.SOUTH);

		subjectTicket.setLayout(new FlowLayout());
		affichageMess.setLayout(new GridLayout());
		saisieTxt.setLayout(new BorderLayout());

		subjectTicket.add(nomTicket);
		saisieTxt.add(txtSaisie, BorderLayout.NORTH);
		saisieTxt.add(zoneSaisie, BorderLayout.CENTER);
		saisieTxt.add(buttonEnvoyer, BorderLayout.SOUTH);
		
		//Taille du texte
		listeTickets.setFont(new Font("Arial",0,15));
		bouton.setFont(new Font("Arial",0,15));

		//Ajout de bordures
		contentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gauche.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		droite.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		subjectTicket.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		saisieTxt.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		contentPane.setBackground(Color.LIGHT_GRAY);


		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/discussion.png")).getImage());
		this.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonEnvoyer) {
			affichageMess.add(new JButton(zoneSaisie.getText()));
		}
		if(e.getSource() == buttnewSubject) {
			
		}
		if(e.getSource() == buttRefresh) {
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}
