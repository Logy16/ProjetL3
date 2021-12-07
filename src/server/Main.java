package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import global.Agents;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;
import global.UtilisateurCampus;


public class Main {
	public static void main(String[] args) {
		Connection connection;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projets5?serverTimezone=UTC","root","");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		//TESTS ECRITURE DANS BASE DE DONNÉES
		
		//Création d'une instance de l'API
		APIServerSQL api = new SimpleAPIServerSQL(connection);
		
		
		//Création du groupe TPA41
		Groupe tpa41 = new Groupe("TPA41");
		
		//Enregistrement dans la base de données du groupe
		api.createGroupe(tpa41);
		
		//Création des utilisateurs
		Utilisateur jules = new UtilisateurCampus("DI SCALA", "Jules", "Liouss", "testmdp", tpa41);
		Utilisateur nemo = new Agents("BOUILLON", "Nemo", "Pastorale", "testmdpnemo", tpa41);
		
		//Enregistrement des utilisateurs dans la base de données
		api.setUtilisateur(jules);
		api.setUtilisateur(nemo);
		
		//Création d'un fil
		Fil fil = new Fil("Problème de réseau dans les salles S4 et s5", tpa41, jules);
		//Enregistrement du fil dans la base de données
		api.createFil(fil);
		
		//Création de messages
		Message msg1 = new Message("Internet marche plus dans les salles S", new Date(), jules, fil);
		Message msg2 = new Message("Oui ça devient chiant là y'a jamais internet", new Date(), nemo, fil);
		
		//Enregistrement des messages dans la base de donnée
		api.sendMessage(msg1);
		api.sendMessage(msg2);
		
		
		//TESTS LECTURE DANS BASE DE DONNÉES
		
		//Affichage des utilisateurs
		Utilisateur importedLiouss = api.getUtilisateur(jules.getIdentifiant());
		System.out.println(importedLiouss.toString());
		Utilisateur importedPastorale = api.getUtilisateur(nemo.getIdentifiant());
		System.out.println(importedPastorale.toString());
		
		//Affichage des messages dans le premier fil de chaque groupe
		for(Groupe g : api.getGroupesFromUser(importedLiouss)) {
			Fil loadedFil = api.getFils(g).first();
			for(Message m : loadedFil.getMessages()) {
				System.out.println(m.getTexte());
				api.hasReadMessage(m, jules);
				api.hasReadMessage(m, nemo);
				System.out.println("Etat du message : "+api.getMessageState(m));
			}
		}
		
		
		
		
	}
}
