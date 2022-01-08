package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
	
	private Set<Fil> newListFil;
	private Set<Groupe> newListGrp;
	
	private JButton buttValider = new JButton("Valider");
	private JTextField saisieSujet = new JTextField(20);
	private JTextArea saisieFirstMessage = new JTextArea();
	private JComboBox<String> listeGroupBox = new JComboBox<>();

	public AddFil(InterfaceUtilisateur parent, Utilisateur connectedUser, Set<Groupe> listGroup, Set<Fil> listFil) {
		super();
		this.setTitle("Interface Ajout Fil");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.parent = parent;
		this.connectedUser = connectedUser;
		this.newListFil = listFil;
		this.newListGrp = listGroup;
		
		JPanel contentPane = new JPanel();
		JPanel subjectPane = new JPanel();
		JPanel groupePane = new JPanel();
		JPanel validationPane = new JPanel();
		
		saisieSujet.setBorder(BorderFactory.createTitledBorder("Sujet"));
		saisieFirstMessage.setBorder(BorderFactory.createTitledBorder("Premier Message"));
		
		contentPane.setLayout(new BorderLayout());
		contentPane.add(subjectPane, BorderLayout.NORTH);
		contentPane.add(groupePane, BorderLayout.CENTER);
		contentPane.add(validationPane, BorderLayout.SOUTH);
		
		subjectPane.setLayout(new GridLayout(2, 1, 10, 10));
		subjectPane.add(saisieSujet);
		subjectPane.add(saisieFirstMessage);
		

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
				JTree tree = parent.getTree();
				Client client = parent.getClient();
				Groupe actualGrp = null;
				String nomGrp = listeGroupBox.getSelectedItem().toString();
				for(Iterator<Groupe> iteGrp = newListGrp.iterator(); iteGrp.hasNext();) {
					actualGrp = iteGrp.next();
					if(actualGrp.getNom().equals(nomGrp)) {
						break;
					}
				}
				Fil newFil = null;
				try {
					newFil = client.demandeCreationFil(saisieSujet.getText(), this.connectedUser, 
							actualGrp, saisieFirstMessage.getText());
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				
				for(int i=0; i<tree.getModel().getChildCount(tree.getModel().getRoot()); i++){
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getModel().getChild(tree.getModel().getRoot(), i);
					if(nomGrp.equals(selectedNode.toString())) {
						DefaultMutableTreeNode newFeuille = new DefaultMutableTreeNode(newFil.getSujet());
						selectedNode.add(newFeuille);
						break;
					}
				}

				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				model.reload();
				this.newListFil.add(newFil);
				this.dispose();
			}	
		}
	}
}
