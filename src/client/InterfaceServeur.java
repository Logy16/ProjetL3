package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import global.Agents;
import global.Groupe;
import global.Utilisateur;
import global.UtilisateurCampus;

public class InterfaceServeur extends JFrame implements ActionListener, TableModelListener{

	private static final long serialVersionUID = -2410156028365273198L;
	
	private JButton buttAjouterUti = new JButton("Ajouter un utilisateur");
	private JButton buttModifUti = new JButton("Modifier un utilisateur");
	private JButton buttDeleteUti = new JButton("Supprimer un utilisateur");
	private JButton buttAjouterGrp = new JButton("Ajouter un groupe");
	private JButton buttModifGrp = new JButton("Modifier un groupe");
	private JButton buttDeleteGrp = new JButton("Supprimer un groupe");
	
	private JTabbedPane onglets = new JTabbedPane();
	private JTable tableUtilisateur;
	private JTable tableGrp;
	
	private ListUtilisateurTableau modeleUti;
	private ListGroupeTableau modeleGrp;
	
	private List<Utilisateur> listUtilisateur = new ArrayList<>();
	private List<Groupe> listGroupe = new ArrayList<>();

	public InterfaceServeur() {
		super();
		this.setTitle("Interface Serveur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel bas = new JPanel();
		
		//Récupérer la liste des utilisateurs
		
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
		
		//On génère les tableaux d'utilisateurs et de groupes
		modeleUti = new ListUtilisateurTableau(listUtilisateur);
		tableUtilisateur = new JTable(modeleUti);
		tableUtilisateur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableUtilisateur.setRowHeight(25);
		
		modeleGrp = new ListGroupeTableau(listGroupe);
		tableGrp = new JTable(modeleGrp);
		tableGrp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableGrp.setRowHeight(50);
		
		TableColumnModel modeleColonneUti = tableUtilisateur.getColumnModel();
		for (int i=0; i<4; i++) {
			modeleColonneUti.getColumn(i).setPreferredWidth(150);
			modeleColonneUti.getColumn(i).setCellRenderer(new RenduGroupeTableau());
		}
		
		TableColumnModel modeleColonneGrp = tableGrp.getColumnModel();
		modeleColonneGrp.getColumn(0).setPreferredWidth(150);
		modeleColonneGrp.getColumn(0).setCellRenderer(new RenduGroupeTableau());
		
		
		// Paramétrages des composants
		contentPane.setLayout(new BorderLayout());
		contentPane.add(onglets, BorderLayout.CENTER);
		contentPane.add(bas, BorderLayout.SOUTH);
		
		onglets.addTab("Utilisateurs", new JScrollPane(tableUtilisateur));
		onglets.addTab("Groupes", new JScrollPane(tableGrp));
		
		bas.setLayout(new FlowLayout(5, 20, 20));
		if(onglets.getTitleAt(0).equals("Utilisateurs")) {
			bas.removeAll();
			bas.add(buttAjouterUti);
			bas.add(buttModifUti);
			bas.add(buttDeleteUti);
		}/*else {
			bas.removeAll();
			bas.add(buttAjouterGrp);
			bas.add(buttModifGrp);
			bas.add(buttDeleteGrp);
		}*/
		System.out.println(onglets.getTitleAt(0));
		System.out.println(onglets.getTitleAt(1));
		System.out.println(onglets.getTitleAt(0).equals("Utilisateurs"));
		
		//Listeners
		buttAjouterUti.addActionListener(this);
		buttModifUti.addActionListener(this);
		buttDeleteUti.addActionListener(this);
		buttAjouterGrp.addActionListener(this);
		buttModifGrp.addActionListener(this);
		buttDeleteGrp.addActionListener(this);
		
		tableUtilisateur.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Clique sur une ligne
			}
		});
		
		// Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/serveur.jpg")).getImage());
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(onglets.getTitleAt(0).equals("Utilisateurs")) {
			if(e.getSource()==buttAjouterUti) {
				AjouterUtilisateur frameAjout = new AjouterUtilisateur(this);
				frameAjout.setVisible(true);
				frameAjout.setModal(false);
				
			}
			
			if(e.getSource()==buttModifUti) {
				UpdateUtilisateur frameModif = new UpdateUtilisateur(this, tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 0).toString(),
						tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 1).toString());		
				frameModif.setVisible(true);
				frameModif.setModal(true);
			}
			
			if(e.getSource()==buttDeleteUti) {
				for(Iterator<Utilisateur> ite = listUtilisateur.iterator(); ite.hasNext();) {
					Utilisateur actualUti = ite.next();
					if(actualUti.getNom().equals(tableUtilisateur.getValueAt(tableUtilisateur.getSelectedRow(), 0).toString())) {
						ite.remove();
						//TODO : effacer en base
						modeleUti.fireTableRowsDeleted(tableUtilisateur.getSelectedRow(), tableUtilisateur.getSelectedRow());
						break;
					}
				}
			}
			
		}else {
			
		}
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
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
