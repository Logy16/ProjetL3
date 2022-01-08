package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import global.Fil;
import global.Groupe;
import global.Utilisateur;
import server.Client;

public class AddFil extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = -4018302795480161975L;
	
	private InterfaceUtilisateur parent;
	private Utilisateur connectedUser;
	
	private List<Fil> newListFil;
	
	private JButton buttValider = new JButton("Valider");
	private JTextField saisieSujet = new JTextField(20);
	private JComboBox<String> listeGroupBox = new JComboBox<>();

	public AddFil(InterfaceUtilisateur parent, Utilisateur connectedUser, List<Groupe> listGroup, List<Fil> listFil) {
		super();
		this.setTitle("Interface Ajout Fil");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.parent = parent;
		this.connectedUser = connectedUser;
		this.newListFil = listFil;
		
		JPanel contentPane = new JPanel();
		JPanel subjectPane = new JPanel();
		JPanel groupePane = new JPanel();
		JPanel validationPane = new JPanel();
		
		saisieSujet.setBorder(BorderFactory.createTitledBorder("Sujet"));
		
		contentPane.setLayout(new BorderLayout());
		contentPane.add(subjectPane, BorderLayout.NORTH);
		contentPane.add(groupePane, BorderLayout.CENTER);
		contentPane.add(validationPane, BorderLayout.SOUTH);
		
		subjectPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		subjectPane.add(saisieSujet);
		

		groupePane.setLayout(new FlowLayout(FlowLayout.LEFT));
		for(Groupe grp : listGroup) {
			listeGroupBox.addItem(grp.getNom());
		}
		listeGroupBox.setBorder(BorderFactory.createTitledBorder("Groupe"));
		listeGroupBox.setPreferredSize(saisieSujet.getPreferredSize());
		groupePane.add(listeGroupBox);
		
		validationPane.setLayout(new FlowLayout());
		validationPane.add(buttValider);
		
		//Listener
		buttValider.addActionListener(this);
		
		this.setContentPane(contentPane);
		this.setIconImage(new ImageIcon(getClass().getResource("/img/discussion.png")).getImage());
		this.pack();

		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quitter();
			}
		});
	}
	
	/**
	 * 
	 */
	public void quitter() {
		int reponse = JOptionPane.showConfirmDialog(this, "Ne pas ajouter de nouveau sujet?","Abandon ajout",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
		if (reponse == JOptionPane.YES_OPTION) {
			this.dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttValider) {
			if(saisieSujet.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Le champ sujet doit être renseigner");
			}else {
				JTree listeTickets = this.parent.getTree();
				Fil newFil = null;
				//Ajouter en base
				Client client = parent.getClient();
				try {
					newFil = client.demandeCreationFil(saisieSujet.getText(), this.connectedUser, (Groupe)listeGroupBox.getSelectedItem(), "");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) listeTickets.getLastSelectedPathComponent();
				DefaultMutableTreeNode feuille = new DefaultMutableTreeNode(newFil.getSujet());
				node.add(feuille);
				DefaultTreeModel model = (DefaultTreeModel) listeTickets.getModel();
				model.reload();
				this.newListFil.add(newFil);
				this.dispose();
			}	
		}
	}
}
