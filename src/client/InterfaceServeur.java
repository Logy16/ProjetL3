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

import global.Agents;
import global.Groupe;
import global.Utilisateur;
import global.UtilisateurCampus;
import server.Client;
import server.Server;

public class InterfaceServeur extends JFrame implements ActionListener{

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
		
	// Cr�ations des composants
		// Cr�ations des Panels
		JPanel contentPane = new JPanel();
		JPanel bas = new JPanel();
		
		//R�cup�rer la liste des utilisateurs
		
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
			listGroupe.add(grp1);
			listGroupe.add(grp2);
		
		//On g�n�re les tableaux d'utilisateurs et de groupes
		modeleUti = new ListUtilisateurTableau(listUtilisateur);
		tableUtilisateur = new JTable(modeleUti);
		tableUtilisateur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableUtilisateur.setRowHeight(25);
		
		modeleGrp = new ListGroupeTableau(listGroupe);
		tableGrp = new JTable(modeleGrp);
		tableGrp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableGrp.setRowHeight(50);
		
		TableColumnModel modeleColonneUti = tableUtilisateur.getColumnModel();
		for (int i=0; i<3; i++) {
			modeleColonneUti.getColumn(i).setPreferredWidth(150);
			modeleColonneUti.getColumn(i).setCellRenderer(new RenduGroupeTableau());
		}
		
		/*TableColumnModel modeleColonneGrp = tableGrp.getColumnModel();
		modeleColonneGrp.getColumn(0).setPreferredWidth(150);
		modeleColonneGrp.getColumn(0).setCellRenderer(new RenduGroupeTableau());*/
		
		
		// Param�trages des composants
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

		
		//Listeners
		buttAjouter.addActionListener(this);
		buttModif.addActionListener(this);
		buttDelete.addActionListener(this);
		
		tableUtilisateur.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Clique sur une ligne pour surligner
			}
		});
		
		// Param�trage de la fen�tre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/serveur.jpg")).getImage());
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(onglets.getTitleAt(onglets.getSelectedIndex()).equals("Utilisateurs")) {
			if(e.getSource()==buttAjouter) {
				UpdateUtilisateur frameAjout = new UpdateUtilisateur(this, client);
				frameAjout.setVisible(true);
				frameAjout.setModal(false);
				
			}
			
			if(e.getSource()==buttModif) {
				UpdateUtilisateur frameModif = new UpdateUtilisateur(this, tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 0).toString(),
						tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 1).toString(), client);		
				frameModif.setVisible(true);
				frameModif.setModal(true);
			}
			
			if(e.getSource()==buttDelete) {
				for(Iterator<Utilisateur> ite = listUtilisateur.iterator(); ite.hasNext();) {
					Utilisateur actualUti = ite.next();
					if(actualUti.getNom().equals(tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 0).toString())) {
						ite.remove();
						try {
							client.supprimerUtilisateur(actualUti);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						modeleUti.fireTableRowsDeleted(tableUtilisateur.getSelectedRow(), tableUtilisateur.getSelectedRow());
						break;
					}
				}
			}
		}else {
			if(e.getSource()==buttAjouter) {
				String repNomGrp = JOptionPane.showInputDialog("Saisir le nom du groupe");
				if(!repNomGrp.isEmpty()) {
					Groupe newGrp = new Groupe(repNomGrp);
					listGroupe.add(newGrp);
					try {
						client.createGroupe(newGrp.getNom());
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					modeleGrp.addRow(newGrp.getNom());
				}else {
					JOptionPane.showMessageDialog(this, "Veuillez saisir quelque chose");
				}
			}
			
			if(e.getSource()==buttModif) {
				for(Iterator<Groupe> ite = listGroupe.iterator(); ite.hasNext();) {
					Groupe actualGrp = ite.next();
					if(actualGrp.getNom().equals(tableGrp.getValueAt(tableGrp.getSelectedRow(), 0).toString())) {
						String repNewNomGrp = JOptionPane.showInputDialog("Saisir le nom du groupe", actualGrp.getNom());
						actualGrp.setNom(repNewNomGrp);
						//client.
						modeleGrp.fireTableRowsUpdated(tableGrp.getSelectedRow(), tableGrp.getSelectedRow());
					}
				}
			}
			
			if(e.getSource()==buttDelete) {
				for(Iterator<Groupe> ite = listGroupe.iterator(); ite.hasNext();) {
					Groupe actualGrp = ite.next();
					if(actualGrp.getNom().equals(tableGrp.getValueAt(tableGrp.getSelectedRow(), 0).toString())) {
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
