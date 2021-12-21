package server;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;

public interface IServer {

	/**
	 * Teste la demande de connexion d'un utilisateur au serveur
	 * 
	 * @param login    : login de l'utilisateur a tester
	 * @param password : mot de passe de l'utilisateur a tester
	 * @return true si le mot de passe correspond bien à celui de l'utilisateur,
	 *         false sinon
	 * @author Nemo
	 **/
	public boolean demandeConnexion(String login, String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * Permet de créer un nouveau fil et de l'ajouter à la base de donnée
	 * 
	 * @param titre    : titre à attribuer au fil
	 * @param groupe   : groupe auquel s'adresse le fil
	 * @param createur : utilisateur a l'origine du fil
	 * @param message  : premier message envoyé par le créateur sur le fil
	 * @author Nemo
	 **/
	public void creerFil(String titre, Groupe groupe, Utilisateur createur, String message);

	/**
	 * Permet de créer un nouveau groupe et de l'ajouter à la base de donnée
	 * 
	 * @param nom : nom à attribuer au groupe
	 * @author Nemo
	 **/
	public void creerGroupe(String nom);

	/**
	 * Permet de tester si un utilisateur appartient a un groupe
	 * 
	 * @param user   : utilisateur a tester
	 * @param groupe : groupe a tester
	 * @return true si l'utilisateur est dans le groupe, false sinon
	 * @author Nemo
	 **/
	public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe);

	/**
	 * Permet d'ajouter un utilisateur dans un groupe s'il n'y est pas déjà et de
	 * mettre à jour la base de donnée
	 * 
	 * @param user   : utilisateur a ajouter dans le groupe
	 * @param groupe : groupe dans lequel l'utilisateur sera rajouté
	 * @author Nemo
	 **/
	public void addUserToGroupe(Utilisateur user, Groupe groupe);

	/**
	 * Permet de créer un nouvel agent et de l'ajouter à la base de donnée
	 * 
	 * @param nom               : nom à attribuer à l'agent
	 * @param prenom            : prenom à attribuer à l'agent
	 * @param id                : id de l'agent
	 * @param notHashedPassword : password brut de l'agent
	 * @param groupes           : groupe(s) auquel(s) appartient l'agent
	 * @author Nemo
	 **/
	public void addAgent(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes);

	/**
	 * Permet de créer un nouvel utilisateur du campus et de l'ajouter à la base de
	 * donnée
	 * 
	 * @param nom               : nom à attribuer à l'utilisateur
	 * @param prenom            : prenom à attribuer à l'utilisateur
	 * @param id                : id de l'utilisateur
	 * @param notHashedPassword : password brut de l'utilisateur
	 * @param groupes           : groupe(s) auquel(s) appartient l'utilisateur
	 * @author Nemo
	 **/
	public void addUtilisateurCampus(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes);

	/**
	 * Permet d'envoyer un message dans un fil de discussion
	 * 
	 * @param message  : message à envoyer sur le fil
	 * @param envoyeur : utilisateur à la source du message
	 * @param fil      : fil de discussion sur lequel doit être envoyé le message
	 * @author Nemo
	 **/
	public void envoyerMessage(String message, Utilisateur envoyeur, Fil fil);

	/**
	 * Permet d'obtenir l'Etat du message passé en paramètre
	 * 
	 * @param message : message dont on souhaite obtenir l'etat
	 * @return l'Etat du message passé en paramètre
	 * @author Nemo
	 **/
	public Etat getMessageStatus(Message message);

}
