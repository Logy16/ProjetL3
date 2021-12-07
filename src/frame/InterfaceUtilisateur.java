package frame;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pasencoredenom.Fil;

public class InterfaceUtilisateur extends JFrame implements ActionListener {
	
	private JLabel nomTicket = new JLabel("DU BLABLA DE TEST");
	private JLabel txtSaisie = new JLabel("Envoyer un message dans : ");
	private List<Fil> listeFil = new ArrayList<>();

	public InterfaceUtilisateur() {
		super();
		//this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle("Titre");
		
	//Créations des composants
		//Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel gauche = new JPanel();
		JPanel bouton = new JPanel();
		JPanel listeTickets = new JPanel();
		JPanel droite = new JPanel();
		JPanel subjectTicket = new JPanel();
		JPanel affichageMess = new JPanel();
		JPanel saisieTxt = new JPanel();
		JTextArea zoneSaisie = new JTextArea(3,100);
		
		//Paramétrages des composants
		contentPane.setLayout(new BorderLayout());
		contentPane.add(gauche, BorderLayout.WEST);
		contentPane.add(droite, BorderLayout.CENTER);
		
		gauche.setLayout(new BorderLayout());
		gauche.add(bouton, BorderLayout.NORTH);
		gauche.add(listeTickets, BorderLayout.CENTER);
		
		bouton.setLayout(new FlowLayout());
		listeTickets.setLayout(new GridLayout(10, 1, 0, 5));
		
		bouton.add(new Button("On test un boutton"));
		listeFil.add(new Fil("Sujet jeux vidéo"));
		listeFil.add(new Fil("Sujet cours POOMO"));
		for(int i=0; i<listeFil.size(); i++) {
			listeTickets.add(new Button(listeFil.get(i).getSujet()));
		}
		
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
		
		
		contentPane.setBackground(Color.LIGHT_GRAY);
		bouton.setBackground(Color.RED);
		listeTickets.setBackground(Color.BLUE);
		subjectTicket.setBackground(Color.GREEN);
		affichageMess.setBackground(Color.YELLOW);
		saisieTxt.setBackground(Color.PINK);
		
		//Gestion des évènements
	
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quitter();
			}
		});
		
	//Paramétrage de la fenêtre
		this.setContentPane(contentPane);
		this.pack();
		
	}
			
	/**
	 * Permet de quitter l'application si l'utilisateur répond oui
	 */
	public void quitter() {
		int reponse = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir quitter?","Quitter l'application?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
		if (reponse == JOptionPane.YES_OPTION) {
			System.exit(0);  
		/*}else if(reponse == JOptionPane.NO_OPTION) {
			int reponseMenu = JOptionPane.showConfirmDialog(this, "Voulez-vous revenir au menu principal?","Continuer?",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
			if (reponseMenu == JOptionPane.YES_OPTION) {
				MenuBatailleNavale MBN = new MenuBatailleNavale();
				MBN.setVisible(true);
				this.dispose();
			}*/
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*if(e.getSource() == 2) {
			this.nomTicket.setText();
		}*/
		
	} 
	
}
