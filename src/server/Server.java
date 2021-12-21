package server;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import global.Agents;
import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;
import global.UtilisateurCampus;

public class Server implements IServer {
	protected APIServerSQL api;
	private Connection connection;

	public Server() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projets5?serverTimezone=UTC",
					"root", "");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		this.api = new SimpleAPIServerSQL(connection);
	}

	@Override
	public boolean demandeConnexion(String login, String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String hashedPassword = new String(MessageDigest.getInstance("md5").digest(password.getBytes("UTF-8")),
				StandardCharsets.UTF_8);
		Utilisateur utilisateurTest = api.getUtilisateur(login);
		return utilisateurTest.getHashedPassword().equals(hashedPassword)
				&& utilisateurTest.getIdentifiant().equals(login);
	}

	@Override
	public void creerFil(String titre, Groupe groupe, Utilisateur createur, String message) {
		Fil fil = new Fil(titre, groupe, createur);
		api.createFil(fil);
		Message premierMessage = new Message(message, new Date(), createur, fil);
		fil.addMessage(premierMessage);
		api.sendMessage(premierMessage);
		for (Utilisateur utilisateurFil : fil.getGroupe().getUtilisateurs()) {
			api.hasSentMessage(premierMessage, utilisateurFil);
		}
		api.hasReadMessage(premierMessage, createur);
	}

	@Override
	public void creerGroupe(String nom) {
		Groupe groupe = new Groupe(nom);
		api.createGroupe(groupe);
	}

	@Override
	public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe) {
		boolean userInGroupe = false;
		for (Utilisateur utilisateurInGroupe : groupe.getUtilisateurs()) {
			if (utilisateurInGroupe.equals(user)) {
				userInGroupe = true;
			}
		}
		return userInGroupe;
	}

	@Override
	public void addUserToGroupe(Utilisateur user, Groupe groupe) {
		if (!testIfUserInGroupe(user, groupe)) {
			groupe.addUtilisateurs(user);
			api.setUtilisateur(user);
		}
	}

	@Override
	public void addAgent(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes) {
		boolean ajoute = false;
		Utilisateur newUser = new Agents(nom, prenom, id, notHashedPassword, groupes);
		for (Groupe groupeLink : groupes) {
			for (Utilisateur utilisateurLink : groupeLink.getUtilisateurs()) {
				if (utilisateurLink.equals(newUser)) {
					ajoute = true;
				}
			}
			if (!ajoute) {
				groupeLink.addUtilisateurs(newUser);
			}
			ajoute = false;
		}
		api.setUtilisateur(newUser);
	}

	@Override
	public void addUtilisateurCampus(String nom, String prenom, String id, String notHashedPassword,
			Groupe... groupes) {
		boolean ajoute = false;
		Utilisateur newUser = new UtilisateurCampus(nom, prenom, id, notHashedPassword, groupes);
		for (Groupe groupeLink : groupes) {
			for (Utilisateur utilisateurLink : groupeLink.getUtilisateurs()) {
				if (utilisateurLink.equals(newUser)) {
					ajoute = true;
				}
			}
			if (!ajoute) {
				groupeLink.addUtilisateurs(newUser);
			}
			ajoute = false;
		}
		api.setUtilisateur(newUser);
	}

	@Override
	public void envoyerMessage(String message, Utilisateur envoyeur, Fil fil) {
		Message newMessage = new Message(message, new Date(), envoyeur, fil);
		fil.addMessage(newMessage);
		api.sendMessage(newMessage);
		for (Utilisateur utilisateurFil : fil.getGroupe().getUtilisateurs()) {
			api.hasSentMessage(newMessage, utilisateurFil);
		}
		api.hasReadMessage(newMessage, envoyeur);
	}

	@Override
	public void lireMessageFil(Fil fil, Utilisateur lecteur) {
		if (testIfUserInGroupe(lecteur, fil.getGroupe()) || fil.getCreateur().equals(lecteur)) {
			for (Message messageFil : fil.getMessages()) {
				if (getMessageStatus(messageFil) != Etat.LU) {
					api.hasReadMessage(messageFil, lecteur);
				}
			}
		}
	}

	@Override
	public Etat getMessageStatus(Message message) {
		return api.getMessageState(message);
	}

	@Override
	public void modifierNomUser(Utilisateur user, String newNom) {
		user.setNom(newNom);
		api.setUtilisateur(user);
	}

	@Override
	public void modifierPrenomUser(Utilisateur user, String newPrenom) {
		user.setPrenom(newPrenom);
		api.setUtilisateur(user);
	}

	@Override
	public void supprimerUtilisateur(Utilisateur user) {
		api.removeUtilisateur(user);
		Iterator<Groupe> listIterator = user.getGroupes().iterator();
		for (; listIterator.hasNext();) {
			Groupe groupeDel = listIterator.next();
			groupeDel.removeUtilisateurs(user);
		}
	}

	@Override
	public void supprimerGroupe(Groupe groupe) {
		api.removeGroupe(groupe);
		groupe.removeUtilisateurs(groupe.getUtilisateurs());
	}
}
