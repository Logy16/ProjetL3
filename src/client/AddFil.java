package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import global.Fil;
import global.Groupe;

public class AddFil extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = -4018302795480161975L;
	
	private List<Fil> newListFil;
	
	private JButton buttValider = new JButton("Valider");
	private JTextField saisieSujet = new JTextField(10);
	private JComboBox<Groupe> listeGroupBox = new JComboBox<>();

	public AddFil(List<Groupe> listGroup, List<Fil> listFil) {
		super();
		this.setTitle("Interface Ajout Fil");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
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
			listeGroupBox.addItem(grp);
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
				//Ajouter en base
				Fil newFil = new Fil(saisieSujet.getText(), (Groupe)listeGroupBox.getSelectedItem() , null);
				this.newListFil.add(newFil);
				this.dispose();
			}	
		}
	} 
	
	public List<Fil> getnewListFil(){
		return this.newListFil;
	}

}
