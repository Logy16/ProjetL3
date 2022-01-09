package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import global.Groupe;
import global.Utilisateur;
import server.Client;

public class InterfaceServeur extends JFrame implements ActionListener {

	private static final long serialVersionUID = -2410156028365273198L;

	private Client client;

	private JButton buttAjouter = new JButton("Ajouter");
	private JButton buttModif = new JButton("Modifier");
	private JButton buttDelete = new JButton("Supprimer");

	private JTabbedPane onglets = new JTabbedPane();
	private JTable tableUtilisateur;
	private JTable tableGrp;

	private ListUtilisateurTableau modeleUti;
	private ListGroupeTableau modeleGrp;

	private List<Utilisateur> listUtilisateur = new ArrayList<>();
	private List<Groupe> listGroupe = new ArrayList<>();

	public InterfaceServeur(Client client) {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.client = client;
		// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel bas = new JPanel();

		// Récupérer la liste des utilisateurs
		Set<Groupe> listGroupeBD = null;
		Set<Utilisateur> listUtiBDFromGrp = null;
		try {
			listGroupeBD = client.getGroupe();
			listUtiBDFromGrp = client.getUtilisateurs();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		listGroupe.addAll(listGroupeBD);
		listUtilisateur.addAll(listUtiBDFromGrp);

		// On génère les tableaux d'utilisateurs et de groupes
		modeleUti = new ListUtilisateurTableau(listUtilisateur, client);
		tableUtilisateur = new JTable(modeleUti);
		tableUtilisateur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableUtilisateur.setRowHeight(25);

		modeleGrp = new ListGroupeTableau(listGroupe);
		tableGrp = new JTable(modeleGrp);
		tableGrp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableGrp.setRowHeight(50);

		TableColumnModel modeleColonneUti = tableUtilisateur.getColumnModel();
		for (int i = 0; i < 3; i++) {
			modeleColonneUti.getColumn(i).setPreferredWidth(150);
			modeleColonneUti.getColumn(i).setCellRenderer(new RenduGroupeTableau());
		}

		/*
		 * TableColumnModel modeleColonneGrp = tableGrp.getColumnModel();
		 * modeleColonneGrp.getColumn(0).setPreferredWidth(150);
		 * modeleColonneGrp.getColumn(0).setCellRenderer(new RenduGroupeTableau());
		 */

		// Paramétrages des composants
		contentPane.setLayout(new BorderLayout());
		contentPane.add(onglets, BorderLayout.CENTER);
		contentPane.add(bas, BorderLayout.SOUTH);

		onglets.addTab("Utilisateurs", new JScrollPane(tableUtilisateur));
		onglets.addTab("Groupes", new JScrollPane(tableGrp));

		bas.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		buttAjouter.setPreferredSize(new Dimension(100, 25));
		buttModif.setPreferredSize(buttAjouter.getPreferredSize());
		buttDelete.setPreferredSize(buttAjouter.getPreferredSize());
		bas.add(buttAjouter);
		bas.add(buttModif);
		bas.add(buttDelete);

		// Listeners
		buttAjouter.addActionListener(this);
		buttModif.addActionListener(this);
		buttDelete.addActionListener(this);

		tableUtilisateur.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// Clique sur une ligne pour surligner
			}
		});

		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/serveur.jpg")).getImage());
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (onglets.getTitleAt(onglets.getSelectedIndex()).equals("Utilisateurs")) {
			if (e.getSource() == buttAjouter) {
				UpdateUtilisateur frameAjout = new UpdateUtilisateur(this, client);
				frameAjout.setVisible(true);
				frameAjout.setModal(false);

			}
			
			if(e.getSource()==buttModif) {
				Utilisateur user = null;
				try {
					user = client.getUtilisateur(tableUtilisateur.getValueAt(
							tableUtilisateur.getSelectedRow(), 2).toString());
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				UpdateUtilisateur frameModif = new UpdateUtilisateur(this, user, client);		

				frameModif.setVisible(true);
				frameModif.setModal(true);
			}

			if (e.getSource() == buttDelete) {
				for (Iterator<Utilisateur> ite = listUtilisateur.iterator(); ite.hasNext();) {
					Utilisateur actualUti = ite.next();
					if (actualUti.getNom()
							.equals(tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 0).toString())) {
						ite.remove();
						try {
							client.supprimerUtilisateur(actualUti);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						modeleUti.fireTableRowsDeleted(tableUtilisateur.getSelectedRow(),
								tableUtilisateur.getSelectedRow());
						break;
					}
				}
			}
		} else {
			if (e.getSource() == buttAjouter) {
				String repNomGrp = JOptionPane.showInputDialog("Saisir le nom du groupe");
				if (!repNomGrp.isEmpty()) {
					Groupe newGrp = new Groupe(repNomGrp);
					listGroupe.add(newGrp);
					try {
						client.createGroupe(newGrp.getNom());
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					modeleGrp.addRow(newGrp.getNom());
				} else {
					JOptionPane.showMessageDialog(this, "Veuillez saisir quelque chose");
				}
			}

			/*
			 * if(e.getSource()==buttModif) { for(Iterator<Groupe> ite =
			 * listGroupe.iterator(); ite.hasNext();) { Groupe actualGrp = ite.next();
			 * if(actualGrp.getNom().equals(tableGrp.getValueAt(tableGrp.getSelectedRow(),
			 * 0).toString())) { String repNewNomGrp =
			 * JOptionPane.showInputDialog("Saisir le nom du groupe", actualGrp.getNom());
			 * actualGrp.setNom(repNewNomGrp); client.
			 * modeleGrp.fireTableRowsUpdated(tableGrp.getSelectedRow(),
			 * tableGrp.getSelectedRow()); } } }
			 */

			if (e.getSource() == buttDelete) {
				for (Iterator<Groupe> ite = listGroupe.iterator(); ite.hasNext();) {
					Groupe actualGrp = ite.next();
					if (actualGrp.getNom().equals(tableGrp.getValueAt(tableGrp.getSelectedRow(), 0).toString())) {
						ite.remove();
						try {
							client.supprimerGroupe(actualGrp);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						modeleGrp.fireTableRowsDeleted(tableGrp.getSelectedRow(), tableGrp.getSelectedRow());
						break;
					}
				}
			}
		}
	}

	public ListUtilisateurTableau getModeleTableUtilsateur() {
		return this.modeleUti;
	}

	public JTable getTableUtilsateur() {
		return this.tableUtilisateur;
	}

	public List<Utilisateur> getListUser() {
		return this.listUtilisateur;
	}

	public ListGroupeTableau getTableGroup() {
		return this.modeleGrp;
	}
}
