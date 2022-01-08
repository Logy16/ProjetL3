package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import global.Utilisateur;

public class InterfaceConnexion extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6832595074713893710L;

	private JLabel labelUsername = new JLabel("Identifiant");
	private JLabel labelMdp = new JLabel("Mot de passe");
	private JLabel error = new JLabel("");
	private JTextField saisieUsername = new JTextField();
	private JPasswordField saisieMdp = new JPasswordField();
	private JLabel labelChoix = new JLabel("Choix de l'interface");
	private JComboBox<String> choixInterface = new JComboBox<>();

	private JButton buttConnexion = new JButton("Connexion");

	public InterfaceConnexion() {
		super();
		this.setTitle("Connexion");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		choixInterface.addItem("Utilisateur");
		choixInterface.addItem("Gestion");

		// Créations des composants
		// Créations des Panels
		JPanel contentPane = new JPanel();
		JPanel panelConnexion = new JPanel();
		JPanel panelValidation = new JPanel();

		contentPane.setLayout(new BorderLayout());
		contentPane.add(panelConnexion, BorderLayout.CENTER);
		contentPane.add(panelValidation, BorderLayout.SOUTH);

		panelConnexion.setLayout(new GridLayout(3, 2, 10, 10));
		panelConnexion.add(labelUsername, 0);
		panelConnexion.add(saisieUsername, 1);
		panelConnexion.add(labelMdp, 2);
		panelConnexion.add(saisieMdp, 3);
		panelConnexion.add(labelChoix, 4);
		panelConnexion.add(choixInterface, 5);
		error.setForeground(Color.RED);

		panelValidation.setLayout(new BorderLayout(0, 20));
		panelValidation.add(buttConnexion, BorderLayout.CENTER);
		panelValidation.add(error, BorderLayout.NORTH);

		buttConnexion.addActionListener(this);

		this.setContentPane(contentPane);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttConnexion) {
			if (saisieUsername.getText().isEmpty() || saisieMdp.getPassword().length == 0) {
				error.setText("Identifiant ou mot de passe invalide");
			} else {
				Utilisateur connectedUser = null;
				try {
					if (client.demandeConnexion(saisieUsername.getText(), saisieMdp.getPassword().toString())) {
						client.getUtilisateur(saisieUsername.getText());
					}
				} catch (IOException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				if (choixInterface.getSelectedItem().equals("Utilisateur")) {
					InterfaceUtilisateur iUtilisateur = new InterfaceUtilisateur(connectedUser, client);
					iUtilisateur.setVisible(true);
					this.dispose();
				} else {
					InterfaceServeur iServer = new InterfaceServeur(connectedUser, client);
					iServer.setVisible(true);
					this.dispose();
				}
			}
		}
	}

}
