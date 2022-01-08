package client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import global.Agents;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;
import global.UtilisateurCampus;
import server.Client;
import server.Server;
import server.SimpleAPIServerSQL;

public class InterfaceUtilisateur extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5093493472239406706L;
	private Client client;
	private JTree listeTickets;
	private JPanel affichageMess;
	private JLabel nomTicket = new JLabel("Aucun sujet choisi pour le moment");
	private JLabel txtSaisie = new JLabel("Envoyer un message dans : ");
	private JButton buttonEnvoyer = new JButton("Envoyer");
	private JButton buttnewSubject = new JButton("Nouveau Sujet");
	private JButton buttRefresh = new JButton("Rafraichir");
	private JTextArea zoneSaisie;
	private JComboBox<Groupe> listeGroupBox = new JComboBox<>();
	private DefaultMutableTreeNode node;
   	private Object nodeInfo;
	
	Utilisateur test = new Agents("TEST", "testeur", "test3", "mdptest");
	
	private Fil selectedFil = null;
	
	private List<Groupe> listgrp = new ArrayList<>();
	private List<Fil> listeFil = new ArrayList<>();
	private List<DefaultMutableTreeNode> listNoeud = new ArrayList<>();

	public InterfaceUtilisateur(Client c) {
		super();
		this.setTitle("Interface Utilisateur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.client = c;

	// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel gauche = new JPanel();
		JPanel bouton = new JPanel();
		
		//TODO : Récupérer les groupes et les fils de discussion
		//listgrp = 
		//listeFil = 
		
			//Partie de test à supprimer
			Groupe groupeTest1 = new Groupe("TDA1");
			Groupe groupeTest2 = new Groupe("TDA2");
			Groupe groupeTest3 = new Groupe("TDA3");
			Groupe groupeTest4 = new Groupe("TDA4");
			listgrp.add(groupeTest1);
			listgrp.add(groupeTest2);
			listgrp.add(groupeTest3);
			listgrp.add(groupeTest4);
			
			Fil filtest = new Fil("Sujet écriture", groupeTest2, test);
			Fil filtest2 = new Fil("Sujet 1", groupeTest1, test);
			filtest.addMessage(new Message("Test de message 1", new Date(), new UtilisateurCampus("a", "a", "a", "a", groupeTest2), filtest));
			filtest.addMessage(new Message("Test de message 2", new Date(), new UtilisateurCampus("a", "a", "a", "a", groupeTest2), filtest));
			filtest2.addMessage(new Message("Test de message 11", new Date(), new UtilisateurCampus("a", "a", "a", "a", groupeTest1), filtest2));
			filtest2.addMessage(new Message("Test de message 12", new Date(), new UtilisateurCampus("a", "a", "a", "a", groupeTest1), filtest2));
			listeFil.add(filtest);
			listeFil.add(filtest2);
			listeFil.add(new Fil("Sujet 2", groupeTest2, test));
			listeFil.add(new Fil("Sujet jeux vidéo", groupeTest3, test));
			listeFil.add(new Fil("Sujet cours POOMO", groupeTest4, test));
		
		for(Groupe grp : listgrp) {
			listeGroupBox.addItem(grp);
		}
			
		//Création de l'arbre
		DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Racine");
		for(int i = 0; i<listgrp.size(); i++) {
			DefaultMutableTreeNode noeud = new DefaultMutableTreeNode(listgrp.get(i).getNom());
			listNoeud.add(noeud);
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
				node = (DefaultMutableTreeNode) listeTickets.getLastSelectedPathComponent();
		        if(node.getChildCount() == 0) {
		        	nodeInfo = node.getUserObject();
		        	affichageMess.removeAll();
		        	
			        nomTicket.setText(nodeInfo.toString());		
			        txtSaisie.setText("Envoyer un message dans : <<" + nodeInfo + ">>");
			        
			        for(Fil sameFil : listeFil) {
			        	if(sameFil.getSujet().equals(nodeInfo)) {
			        		 selectedFil = sameFil;
			        	}
			        }
			        
			        for(Iterator<Message> ite = selectedFil.getMessages().iterator(); ite.hasNext();) {
			        	Message currentMess = ite.next();
			        	JLabel labelMessage = new JLabel("<html>" + currentMess.getTexte() + "<br/>" 
			        			+ currentMess.getExpediteur().getNom() + ", " 
			        			+ currentMess.getDate() + "<br/><br/></html>");
			        	switch(currentMess.getEtat()) {
			        		case EN_ATTENTE: labelMessage.setBackground(Color.RED);
			        		case LU: labelMessage.setBackground(Color.ORANGE);
			        		case RECU: labelMessage.setBackground(Color.GREEN);
			        	}
			        	labelMessage.setOpaque(true);
			        	affichageMess.add(labelMessage);
			        }
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
		droite.add(new JScrollPane(affichageMess), BorderLayout.CENTER);
		droite.add(saisieTxt, BorderLayout.SOUTH);

		subjectTicket.setLayout(new FlowLayout());
		affichageMess.setLayout(new GridLayout(100,1));
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
		
		//Ajout des listeners sur les boutons
		buttnewSubject.addActionListener(this);
		buttonEnvoyer.addActionListener(this);
		buttRefresh.addActionListener(this);


		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/discussion.png")).getImage());
		this.pack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonEnvoyer) {
			if(!zoneSaisie.getText().isEmpty()) {
				Message currentMess = new Message(zoneSaisie.getText(), new Date(), test, selectedFil);
				
				/*Fil filSelected = null;
				for(Fil fil : listeFil) {
					if(fil.getSujet() == node.getUserObject()) {
						filSelected = fil;
					}
				}
				
				try {
					client.sendMessage(currentMess.getTexte(), test, filSelected);
				}catch (ClassNotFoundException | IOException excep) {
					System.out.println(excep);
				}*/
				
				affichageMess.add(new JLabel("<html>" + currentMess.getTexte() + "<br/>" 
	        			+ currentMess.getExpediteur().getNom() + ", " 
	        			+ currentMess.getDate() + "<br/><br/></html>"));
				SwingUtilities.updateComponentTreeUI(this);
				zoneSaisie.setText("");
			}
		}
		
		if(e.getSource() == buttnewSubject) {
			/*AddFil newFil = new AddFil(listgrp, listeFil);
			newFil.setModal(true);	
			newFil.setVisible(true);
			this.listeFil = newFil.getnewListFil();
			String repSujet = JOptionPane.showInputDialog("Saisir le sujet");
			String grpSelecteed = JOptionPane.showInputDialog(listeGroupBox);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
					 listeTickets.getLastSelectedPathComponent()
			TreeNode groupeTree = node.getParent();
			Groupe repGroupe = null;
			for(Groupe grp : listgrp) {
				if(grp.getNom() == groupeTree.toString()) {
					repGroupe = grp;
				}
			}
			
			Fil newFil = new Fil(repSujet, repGroupe, test);*/
			
			
			SwingUtilities.updateComponentTreeUI(this);
		}
		
		if(e.getSource() == buttRefresh) {
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}
