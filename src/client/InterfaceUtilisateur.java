package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;
import server.Client;

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

	private Utilisateur connectedUser;
	private Fil selectedFil = null;

	private Set<Groupe> listgrp;
	private Set<Fil> listeFil;
	private List<DefaultMutableTreeNode> listNoeud = new ArrayList<>();

	public InterfaceUtilisateur(Utilisateur connectedUser, Client c) {
		super();
		this.setTitle("Interface Utilisateur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.client = c;
		this.connectedUser = connectedUser;

		// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel gauche = new JPanel();
		JPanel bouton = new JPanel();
		// Récupérer les groupes et les fils de discussion
		try {
			listgrp = this.client.getGroupes(connectedUser);
			if (listgrp != null) {
				for (Iterator<Groupe> iteGrp = listgrp.iterator(); iteGrp.hasNext();) {
					Groupe actualGroupe = iteGrp.next();
					listeFil = client.getFils(actualGroupe);
					if (listeFil != null) {
						for (Iterator<Fil> iteFil = this.client.getFils(actualGroupe).iterator(); iteFil.hasNext();) {
							Fil actualFil = iteFil.next();
							listeFil.add(actualFil);
						}
					} else {
						listeFil = new TreeSet<Fil>();
					}
				}

				for (Groupe grp : listgrp) {
					listeGroupBox.addItem(grp);
				}

				// Création de l'arbre
				DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Racine");
				for (Iterator<Groupe> iteGrp = listgrp.iterator(); iteGrp.hasNext();) {
					Groupe actualGroupe = iteGrp.next();
					DefaultMutableTreeNode noeud = new DefaultMutableTreeNode(actualGroupe.getNom());
					listNoeud.add(noeud);
					racine.add(noeud);
					if (listeFil != null) {
						for (Iterator<Fil> iteFil = listeFil.iterator(); iteFil.hasNext();) {
							Fil actualFil = iteFil.next();
							if (actualFil.getGroupe().equals(actualGroupe)) {
								DefaultMutableTreeNode feuille = new DefaultMutableTreeNode(actualFil.getSujet());
								noeud.add(feuille);
							}
						}
					}
				}

				listeTickets = new JTree(racine);

			}
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}

		listeTickets.setRootVisible(false);
		listeTickets.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		listeTickets.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode nodeSelected = (DefaultMutableTreeNode) listeTickets
						.getLastSelectedPathComponent();
				if (nodeSelected != null && nodeSelected.getChildCount() == 0) {
					Object nodeInfo = nodeSelected.getUserObject();

					nomTicket.setText(nodeInfo.toString());
					txtSaisie.setText("Envoyer un message dans : <<" + nodeInfo + ">>");
					if (listeFil != null) {
						for (Fil sameFil : listeFil) {
							if (sameFil.getSujet().equals(nodeInfo)) {
								selectedFil = sameFil;
							}
						}
					}

					if (selectedFil != null) {
						affichageMess.removeAll();
						try {
							client.lireMessagesFil(selectedFil, connectedUser);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						for (Iterator<Message> ite = selectedFil.getMessages().iterator(); ite.hasNext();) {
							Message currentMess = ite.next();
							JLabel labelMessage = new JLabel(
									"<html>" + currentMess.getTexte() + "<br/>" + currentMess.getExpediteur().getNom()
											+ ", " + currentMess.getDate() + "<br/><br/></html>");
							try {
								switch (client.getMessageStatus(currentMess)) {
								case EN_ATTENTE:
									labelMessage.setBackground(Color.RED);
									break;
								case LU:
									labelMessage.setBackground(Color.ORANGE);
									break;
								case RECU:
									labelMessage.setBackground(Color.GREEN);
									break;
								default:
									break;
								}
							} catch (ClassNotFoundException | IOException e1) {
								e1.printStackTrace();
							}
							labelMessage.setOpaque(true);
							affichageMess.add(labelMessage);
						}
					}

				}
			}
		});
		nomTicket.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

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
		affichageMess.setLayout(new GridLayout(100, 1));
		saisieTxt.setLayout(new BorderLayout());

		subjectTicket.add(nomTicket);
		saisieTxt.add(txtSaisie, BorderLayout.NORTH);
		saisieTxt.add(zoneSaisie, BorderLayout.CENTER);
		saisieTxt.add(buttonEnvoyer, BorderLayout.SOUTH);

		// Taille du texte
		listeTickets.setFont(new Font("Arial", 0, 15));
		bouton.setFont(new Font("Arial", 0, 15));

		// Ajout de bordures
		contentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gauche.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		droite.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		subjectTicket.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		saisieTxt.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		contentPane.setBackground(Color.LIGHT_GRAY);

		// Ajout des listeners sur les boutons
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
		if (e.getSource() == buttonEnvoyer) {
			if (!zoneSaisie.getText().isEmpty()) {
				Message currentMess = new Message(zoneSaisie.getText(), new Date(), this.connectedUser, selectedFil);
				try {
					client.sendMessage(zoneSaisie.getText(), this.connectedUser, selectedFil);
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}

				affichageMess.add(new JLabel("<html>" + currentMess.getTexte() + "<br/>"
						+ currentMess.getExpediteur().getNom() + ", " + currentMess.getDate() + "<br/><br/></html>"));
				zoneSaisie.setText("");
			}
		}

		if (e.getSource() == buttnewSubject) {
			AddFil newFil = new AddFil(this, this.connectedUser, listgrp, listeFil);
			newFil.setModal(true);
			newFil.setVisible(true);
		}

		if (e.getSource() == buttRefresh) {
			DefaultTreeModel model = (DefaultTreeModel) listeTickets.getModel();
			model.reload();
		}
	}

	public JTree getTree() {
		return this.listeTickets;
	}

	public Client getClient() {
		return this.client;
	}

	public DefaultMutableTreeNode getSelectedNode() {
		DefaultMutableTreeNode nodeSelect = (DefaultMutableTreeNode) listeTickets.getLastSelectedPathComponent();
		if (nodeSelect.getChildCount() > 0) {
			return nodeSelect;
		} else {
			return (DefaultMutableTreeNode) nodeSelect.getParent();
		}
	}
}
