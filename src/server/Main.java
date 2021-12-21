package server;

import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;

public class Main {
	public static void main(String[] args) {
		/*
		 * Connection connection; try { connection = DriverManager.getConnection(
		 * "jdbc:mysql://localhost:3306/projets5?serverTimezone=UTC","root",""); } catch
		 * (SQLException e) { e.printStackTrace(); return; }
		 */
		// TESTS ECRITURE DANS BASE DE DONNÉES

		// Création d'une instance de l'API
		// APIServerSQL api = new SimpleAPIServerSQL(connection);

		Server server = new Server();

		// Création du groupe TPA41
		// Groupe tpa41 = new Groupe("TPA41");
		// Enregistrement dans la base de données du groupe
		// api.createGroupe(tpa41);
		server.creerGroupe("TPA41");

		// Création des utilisateurs
		// Utilisateur jules = new UtilisateurCampus("DI SCALA", "Jules", "Liouss",
		// "testmdp", tpa41);
		// Utilisateur nemo = new Agents("BOUILLON", "Nemo", "Pastorale", "testmdpnemo",
		// tpa41);
		// Enregistrement des utilisateurs dans la base de données
		// api.setUtilisateur(jules);
		// api.setUtilisateur(nemo);
		server.addUtilisateurCampus("DI SCALA", "Jules", "Liouss", "testmdp", server.api.getGroupe("TPA41"));
		server.addAgent("BOUILLON", "Nemo", "Pastorale", "testmdpnemo", server.api.getGroupe("TPA41"));

		// Création d'un fil
		// Fil fil = new Fil("Problème de réseau dans les salles S4 et s5", tpa41,
		// jules);
		// Enregistrement du fil dans la base de données
		// api.createFil(fil);
		server.creerFil("Problème de réseau dans les salles S4 et S5", server.api.getGroupe("TPA41"),
				server.api.getUtilisateur("Liouss"), "Internet marche plus dans les salles S");

		// Création de messages
		// Message msg1 = new Message("Internet marche plus dans les salles S", new
		// Date(), jules, fil);
		// Message msg2 = new Message("Oui ça devient chiant là y'a jamais internet",
		// new Date(), nemo, fil);
		server.envoyerMessage("Oui ça devient chiant là y'a jamais internet", server.api.getUtilisateur("Pastorale"),
				server.api.getFil("Problème de réseau dans les salles S4 et S5"));

		// Enregistrement des messages dans la base de donnée
		// api.sendMessage(msg1);
		// api.sendMessage(msg2);

		// TESTS LECTURE DANS BASE DE DONNÉES

		// Affichage des utilisateurs
		Utilisateur importedLiouss = server.api.getUtilisateur("Liouss");
		System.out.println(importedLiouss.toString());
		Utilisateur importedPastorale = server.api.getUtilisateur("Pastorale");
		System.out.println(importedPastorale.toString());

		// Affichage des messages dans le premier fil de chaque groupe
		for (Groupe g : server.api.getGroupesFromUser(importedLiouss)) {
			Fil loadedFil = server.api.getFils(g).first();
			for (Message m : loadedFil.getMessages()) {
				System.out.println(m.getTexte());
				server.api.hasReadMessage(m, server.api.getUtilisateur("Liouss"));
				server.api.hasReadMessage(m, server.api.getUtilisateur("Pastorale"));
				System.out.println("Etat du message : " + server.api.getMessageState(m));
			}
		}

	}
}
